package com.knight.kotlin.library_permiss.permission.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Parcel
import androidx.core.app.ActivityCompat
import com.knight.kotlin.library_permiss.permission.PermissionType
import com.knight.kotlin.library_permiss.permission.base.BasePermission
import com.knight.kotlin.library_permiss.tools.PermissionSettingPage
import com.knight.kotlin.library_permiss.tools.PermissionVersion
import com.knight.kotlin.library_permiss.tools.PhoneRomUtils
import com.knight.kotlin.library_permiss.tools.PhoneRomUtils.getRomVersionName
import com.knight.kotlin.library_permiss.tools.PhoneRomUtils.isHyperOs
import com.knight.kotlin.library_permiss.tools.PhoneRomUtils.isMiui


/**
 * @Description
 * @Author knight
 * @Time 2025/7/10 20:54
 *
 */

abstract class DangerousPermission : BasePermission {
    protected constructor() : super()

    protected constructor(`in`: Parcel) : super(`in`)

    
    override fun getPermissionType(): PermissionType {
        return PermissionType.DANGEROUS
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
        return !checkSelfPermission(activity, getPermissionName()!!) &&
                !ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    getPermissionName()!!
                )
    }

    /**
     * 在低版本的系统上面判断权限是否被用户勾选了《不再询问的选项》
     */
    protected open fun isDoNotAskAgainPermissionByLowVersion(activity: Activity): Boolean {
        return false
    }

    
    override fun getPermissionSettingIntents(context: Context): MutableList<Intent> {
        val intentList: MutableList<Intent> = ArrayList(5)
        var intent: Intent

        // 如果当前厂商系统是澎湃或者 miui 的话，并且已经开启小米系统优化的前提下
        // 优先跳转到小米特有的应用权限设置页，这样做可以优化用户授权的体验
        if (isMiui() && PhoneRomUtils.isXiaomiSystemOptimization()) {
            intent = PermissionSettingPage.getXiaoMiApplicationPermissionPageIntent(context)
            intentList.add(intent)
        } else if (isHyperOs() && PhoneRomUtils.isXiaomiSystemOptimization()) {
            val romVersionName = getRomVersionName()
            // 这里需要过滤 2.0.0.0 ~ 2.0.5.0 范围的版本，因为我在小米云测上面测试了，这个范围的版本直接跳转到小米特有的应用权限设置页有问题
            // 实测在 2.0.6.0 这个问题才被解决，但是澎湃 1.0 无论是什么版本都没有这个问题，所以基本锁定这个问题是在 2.0.0.0 ~ 2.0.5.0 的版本
            // 这是因为小米在刚开始做澎湃 2.0 的时候，小米特有的权限设置页还是一个半成品，跳转后里面没有危险权限的选项，只有一个《其他权限》的选项
            // 并且其他权限的选项点进去后还只有可伶的几个权限：桌面快捷方式、通知类短信、锁屏显示、后台弹出界面、显示悬浮窗
            if (romVersionName != null && !romVersionName.matches("^2\\.0\\.[012345]\\.\\d+$".toRegex())) {
                intent = PermissionSettingPage.getXiaoMiApplicationPermissionPageIntent(context)
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