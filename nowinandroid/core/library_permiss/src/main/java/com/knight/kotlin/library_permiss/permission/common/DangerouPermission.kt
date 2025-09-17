package com.knight.kotlin.library_permiss.permission.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Parcel
import com.knight.kotlin.library_permiss.manager.AlreadyRequestPermissionsManager
import com.knight.kotlin.library_permiss.permission.base.BasePermission
import com.knight.kotlin.library_permiss.tools.PermissionSettingPage.getXiaoMiApplicationPermissionPageIntent
import com.knight.kotlin.library_permiss.tools.PermissionVersion


/**
 * @Description 危险权限的基类
 * @Author knight
 * @Time 2025/7/10 20:54
 *
 */

abstract class DangerousPermission : BasePermission {
    protected constructor() : super()

    protected constructor(`in`: Parcel) : super(`in`)



    override fun getPermissionChannel( context: Context): PermissionChannel {
        return PermissionChannel.REQUEST_PERMISSIONS
    }



    override fun getPermissionPageType( context: Context): PermissionPageType {
        return PermissionPageType.TRANSPARENT_ACTIVITY
    }
    override fun isGrantedPermission( context: Context, skipRequest: Boolean): Boolean {
        // 判断权限是不是在旧系统上面运行（权限出现的版本 > 当前系统的版本）
        if (getFromAndroidVersion() > PermissionVersion.getCurrentVersion()) {
            return isGrantedPermissionByLowVersion(context, skipRequest)
        }
        return isGrantedPermissionByStandardVersion(context, skipRequest)
    }

    /**
     * 在标准版本的系统上面判断权限是否授予
     */
    protected open fun isGrantedPermissionByStandardVersion(
         context: Context,
        skipRequest: Boolean
    ): Boolean {
        if (!PermissionVersion.isAndroid6()) {
            return true
        }
        return checkSelfPermission(context!!, getPermissionName()!!)
    }

    /**
     * 在低版本的系统上面判断权限是否授予
     */
    protected open fun isGrantedPermissionByLowVersion(
         context: Context,
        skipRequest: Boolean
    ): Boolean {
        return true
    }

    override fun isDoNotAskAgainPermission(activity: Activity): Boolean {
        // 判断权限是不是在旧系统上面运行（权限出现的版本 > 当前系统的版本）
        if (getFromAndroidVersion() > PermissionVersion.getCurrentVersion()) {
            return isDoNotAskAgainPermissionByLowVersion(activity)
        }
        return isDoNotAskAgainPermissionByStandardVersion(activity)
    }

    /**
     * 在标准版本的系统上面判断权限是否被用户勾选了《不再询问的选项》
     */
    protected open fun isDoNotAskAgainPermissionByStandardVersion(activity: Activity): Boolean {
        if (!PermissionVersion.isAndroid6()) {
            return false
        }
        // 判断用户是否勾选了不再询问选项的前提条件
        // 1. 必须是本次运行状态已经申请过的权限
        // 2. 必须是未授权的权限
        // 通过以上两个条件就可以判断用户在拒绝的时候是否勾选了《不再询问》的选项，你可能会说为什么要写得那么麻烦？
        // 这是因为 Google 把 shouldShowRequestPermissionRationale 设计得很坑，用户在没有勾选《不再询问》的选项情况下，
        // shouldShowRequestPermissionRationale 也可能返回 false，这种情况就是你本次运行状态下没有申请过这个权限就调用它，
        // 这是 Google 压根不想让你知道用户是不是勾选了《不再询问》的选项，你只能在本次运行状态申请过这个权限才能知道，否则没有其他方法。
        // 目前框架针对这个问题进行了一些优化，主要针对在同时申请了前台权限和后台权限的场景，用户在明确拒绝了前台权限的条件下，
        // 与之对应的后台权限框架并没有继续去申请（因为申请了必然失败），就会导致 shouldShowRequestPermissionRationale 判断出现不准的问题。
        // 但是这样做仍然是有瑕疵的，就是应用本次运行状态如果没有申请过这个权限，直接用 shouldShowRequestPermissionRationale 判断是有问题的，
        // 只有在应用本次运行状态申请过一次这个权限才能用 shouldShowRequestPermissionRationale 准确判断用户是否勾选了《不再询问》的选项。
        // 你可能会说：为什么不永久存储 shouldShowRequestPermissionRationale 状态到磁盘上面？这样不是比你这种做法更加 very good？
        // 这个问题其实别人已经提过了，这里就不再重复解答了，传送地址：https://github.com/getActivity/XXPermissions/issues/154，
        // 目前这套处理方案是目前能想到的最佳解决方案了，如果你还有更好的做法，欢迎通过 issue 告诉我，我会持续跟进并优化这个问题。
        return AlreadyRequestPermissionsManager.isAlreadyRequestPermissions(this) &&
                !checkSelfPermission(activity, getRequestPermissionName(activity)) &&
                !shouldShowRequestPermissionRationale(activity, getRequestPermissionName(activity));
    }

    /**
     * 在低版本的系统上面判断权限是否被用户勾选了《不再询问的选项》
     */
    protected open fun isDoNotAskAgainPermissionByLowVersion(activity: Activity): Boolean {
        return false
    }



    override fun getPermissionSettingIntents( context: Context, skipRequest: Boolean): MutableList<Intent> {
        val intentList: MutableList<Intent> = ArrayList(5)
        var intent: Intent

        // 如果当前厂商系统是 HyperOS 或者 MIUI 的话，并且已经开启小米系统优化的前提下
        // 优先跳转到小米特有的应用权限设置页，这样做可以优化用户授权的体验
        // 需要注意的是，有人反馈 MIUI 国际版不能跳转到小米特有的权限设置页来设置危险权限
        // Github 地址：https://github.com/getActivity/XXPermissions/issues/398
        if (DeviceOs.isMiuiByChina() && DeviceOs.isMiuiOptimization()) {
            intent = getXiaoMiApplicationPermissionPageIntent(context)
            intentList.add(intent)
        } else if (DeviceOs.isHyperOsByChina() && DeviceOs.isHyperOsOptimization()) {
            val osVersionName: String = DeviceOs.getOsVersionName()
            // 这里需要过滤 2.0.0.0 ~ 2.0.5.0 范围的版本，因为我在小米云测上面测试了，这个范围的版本直接跳转到小米特有的应用权限设置页有问题
            // 实测在 2.0.6.0 这个问题才被解决，但是 HyperOS 1.0 无论是什么版本都没有这个问题，所以基本锁定这个问题是在 2.0.0.0 ~ 2.0.5.0 的版本
            // 这是因为小米在刚开始做 HyperOS 2.0 的时候，小米特有的权限设置页还是一个半成品，跳转后里面没有危险权限的选项，只有一个《其他权限》的选项
            // 并且其他权限的选项点进去后还只有可伶的几个权限：桌面快捷方式、通知类短信、锁屏显示、后台弹出界面、显示悬浮窗
            if (!osVersionName.matches("^2\\.0\\.[0-5]\\.\\d+$".toRegex())) {
                intent = getXiaoMiApplicationPermissionPageIntent(context)
                intentList.add(intent)
            }
        }

        intent = getApplicationDetailsSettingIntent(context)
        intentList.add(intent)

        intent = getManageApplicationSettingIntent()
        intentList.add(intent)

        intent = getApplicationSettingIntent()
        intentList.add(intent)

        intent = getAndroidSettingIntent()
        intentList.add(intent)

        return intentList
    }

    override fun isRegisterPermissionByManifestFile(): Boolean {
        // 危险权限默认需要在清单文件中注册，这样定义是为了避免外层在自定义特殊权限的时候，还要去重写此方法
        return true
    }
}