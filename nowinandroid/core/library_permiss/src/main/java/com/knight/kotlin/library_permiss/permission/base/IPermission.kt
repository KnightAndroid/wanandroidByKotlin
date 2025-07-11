package com.knight.kotlin.library_permiss.permission.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import com.knight.kotlin.library_permiss.manifest.AndroidManifestInfo
import com.knight.kotlin.library_permiss.permission.PermissionType
import com.knight.kotlin.library_permiss.tools.PermissionVersion


/**
 * @Description
 * @Author knight
 * @Time 2025/7/10 20:51
 *
 */

interface IPermission : Parcelable {
    /**
     * 获取权限的名称
     */
    
    fun getPermissionName(): String

    /**
     * 获取权限的类型
     */
    
    fun getPermissionType(): PermissionType

    /**
     * 获取权限的组别
     */
    
    fun getPermissionGroup(): String? {
        // 返回空表示没有组别
        return null
    }

    /**
     * 获取权限出现的 Android 版本
     */
    fun getFromAndroidVersion(): Int

    /**
     * 获取使用这个权限最低要求的 targetSdk 版本
     */
    fun getMinTargetSdkVersion(): Int {
        return getFromAndroidVersion()
    }

    /**
     * 获取当前权限对应的旧权限
     */
    
    fun getOldPermissions(context: Context): List<IPermission>? {
        // 表示没有旧权限
        return null
    }

    /**
     * 获取当前权限对应的前台权限
     */
    
    fun getForegroundPermissions( context: Context): List<IPermission>? {
        // 表示没有前台权限
        return null
    }

    /**
     * 当前权限是否为后台权限
     */
    fun isBackgroundPermission(context: Context): Boolean {
        val foregroundPermission = getForegroundPermissions(context) ?: return false
        return !foregroundPermission.isEmpty()
    }

    /**
     * 当前是否支持请求该权限
     */
    fun isSupportRequestPermission(context: Context): Boolean {
        // 如果当前权限是否在低版本（不受支持的版本）上面运行，则证明不支持请求该权限
        // 例如 MANAGE_EXTERNAL_STORAGE 权限是 Android 11 才出现的权限，在 Android 10 上面肯定是不支持申请
        return getFromAndroidVersion() <= PermissionVersion.getCurrentVersion()
    }

    /**
     * 判断当前权限是否授予
     */
    fun isGrantedPermission( context: Context): Boolean {
        return isGrantedPermission(context, true)
    }

    /**
     * 判断当前权限是否授予
     *
     * @param skipRequest       是否跳过申请直接判断的？
     */
    fun isGrantedPermission( context: Context, skipRequest: Boolean): Boolean

    /**
     * 判断当前权限是否被用户勾选了《不再询问的选项》
     */
    fun isDoNotAskAgainPermission( activity: Activity): Boolean

    /**
     * 获取当前权限所有可用的设置页意图
     *
     * 需要注意的是：无需在此方法中判断设置页的意图是否存在再添加，
     * 因为框架在跳转的时候框架会先过滤一遍不存在的意图，
     * 另外通过代码事先判断出来存在的意图也有可能会跳转失败，
     * 如果出现跳转失败会自动使用下一个意图进行跳转，
     * 总结：不存在的意图铁定会跳转失败，存在的意图不一定 100% 会跳转成功。
     */
    
    fun getPermissionSettingIntents( context: Context): MutableList<Intent>

    /**
     * 获取权限请求的间隔时间
     */
    fun getRequestIntervalTime( context: Context): Int {
        return 0
    }

    /**
     * 获取处理权限结果的等待时间
     */
    fun getResultWaitTime( context: Context): Int {
        return 0
    }

    /**
     * 检查权限请求是否合规
     */
    fun checkCompliance(
         activity: Activity,
         requestPermissions: List<IPermission>,
         androidManifestInfo: AndroidManifestInfo
    ) {
    }
}