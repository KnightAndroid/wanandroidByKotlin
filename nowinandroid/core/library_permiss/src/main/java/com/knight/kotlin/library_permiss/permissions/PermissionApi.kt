package com.knight.kotlin.library_permiss.permissions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.AndroidVersionTools.getCurrentAndroidVersionCode
import com.knight.kotlin.library_permiss.PermissionHelper
import com.knight.kotlin.library_permiss.PermissionHelper.findAndroidVersionByPermission
import com.knight.kotlin.library_permiss.PermissionIntentManager.getApplicationDetailsIntent
import com.knight.kotlin.library_permiss.delegate.PermissionDelegateImplV34
import com.knight.kotlin.library_permiss.listener.PermissionDelegate
import com.knight.kotlin.library_permiss.utils.PermissionUtils
import com.knight.kotlin.library_permiss.utils.PermissionUtils.containsPermission


/**
 * Author:Knight
 * Time:2023/8/29 17:41
 * Description:PermissionApi
 */
object PermissionApi {

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    val DELEGATE: PermissionDelegate = PermissionDelegateImplV34()

    /**
     * 判断某个权限是否授予
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun isGrantedPermission(context: Context, permission: String): Boolean {
        return isGrantedPermission(context, permission, true)
    }

    /**
     * 判断某个权限是否授予
     *
     * @param skipRequest           是否跳过权限请求，直接判断权限状态
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun isGrantedPermission(
        context: Context,
        permission: String,
        skipRequest: Boolean
    ): Boolean {
        return DELEGATE.isGrantedPermission(context, permission, skipRequest)
    }

    /**
     * 判断某个权限是否被永久拒绝
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun isDoNotAskAgainPermission(
         activity: Activity,
         permission: String
    ): Boolean {
        return DELEGATE.isDoNotAskAgainPermission(activity, permission)
    }

    /**
     * 获取权限设置页的意图
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun getPermissionSettingIntent(
        context: Context,
        permission: String
    ): Intent {
        return DELEGATE.getPermissionSettingIntent(context, permission)!!
    }

    /**
     * 判断某个权限是否是特殊权限
     */
    fun isSpecialPermission(permission: String?): Boolean {
        return PermissionHelper.isSpecialPermission(permission!!)
    }

    /**
     * 判断某个权限是否是后台权限
     */
    fun isBackgroundPermission(permission: String): Boolean {
        return PermissionHelper.isBackgroundPermission(permission)
    }

    /**
     * 判断某个权限集合是否包含特殊权限
     */
    fun containsSpecialPermission(permissions: List<String?>): Boolean {
        if (permissions == null || permissions.isEmpty()) {
            return false
        }

        for (permission in permissions) {
            if (isSpecialPermission(permission)) {
                return true
            }
        }
        return false
    }

    /**
     * 判断某个权限集合是否包含后台权限
     */
    fun containsBackgroundPermission(permissions: List<String?>): Boolean {
        if (permissions == null || permissions.isEmpty()) {
            return false
        }

        for (permission in permissions) {
            if (isBackgroundPermission(permission!!)) {
                return true
            }
        }
        return false
    }

    /**
     * 判断某些权限是否全部被授予
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun isGrantedPermissions(
        context: Context,
        permissions: List<String>
    ): Boolean {
        if (permissions.isEmpty()) {
            return false
        }

        for (permission in permissions) {
            if (!isGrantedPermission(context, permission)) {
                return false
            }
        }

        return true
    }

    /**
     * 获取已经授予的权限
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun getGrantedPermissions(
       context: Context,
       permissions: List<String>
    ): List<String> {
        val grantedPermissions: MutableList<String> = ArrayList(permissions.size)
        for (permission in permissions) {
            if (isGrantedPermission(context, permission)) {
                grantedPermissions.add(permission)
            }
        }
        return grantedPermissions
    }

    /**
     * 获取已经拒绝的权限
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun getDeniedPermissions(
        context: Context,
        permissions: List<String>
    ): List<String> {
        val deniedPermissions: MutableList<String> = ArrayList(permissions.size)
        for (permission in permissions) {
            if (!isGrantedPermission(context, permission)) {
                deniedPermissions.add(permission)
            }
        }
        return deniedPermissions
    }

    /**
     * 在权限组中检查是否有某个权限是否被永久拒绝
     *
     * @param activity              Activity对象
     * @param permissions            请求的权限
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun isDoNotAskAgainPermissions(
        activity: Activity,
        permissions: List<String>
    ): Boolean {
        for (permission in permissions) {
            if (isDoNotAskAgainPermission(activity, permission)) {
                return true
            }
        }
        return false
    }

    /**
     * 根据传入的权限自动选择最合适的权限设置页的意图
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun getBestPermissionSettingIntent(
        context: Context,
        permissions: List<String>
    ): Intent {
        // 如果失败的权限里面不包含特殊权限
        if (permissions == null || permissions.isEmpty()) {
            return getApplicationDetailsIntent(context!!)
        }

        // 创建一个新的集合对象，避免复用对象可能引发外层的冲突
        val realPermissions: MutableList<String> = ArrayList(permissions)
        for (permission in permissions) {
            if (findAndroidVersionByPermission(permission!!) >
                getCurrentAndroidVersionCode()
            ) {
                // 如果当前权限是高版本才出现的权限，则进行剔除
                realPermissions.remove(permission)
                continue
            }
            val oldPermissions: List<String> = PermissionHelper
                .queryOldPermissionByNewPermission(permission)
                ?.let { PermissionUtils.asArrayList(*it) } ?: emptyList()
            // 1. 如果旧版本列表不为空，并且当前权限是特殊权限，就剔除它对应的旧版本权限
            // 例如：MANAGE_EXTERNAL_STORAGE -> READ_EXTERNAL_STORAGE、WRITE_EXTERNAL_STORAGE
            // 2. 如果旧版本列表不为空，并且当前权限对应的旧版本权限包含了特殊权限，就剔除它对应的旧版本权限
            // 例如：POST_NOTIFICATIONS -> NOTIFICATION_SERVICE
            if (!oldPermissions.isEmpty() && (isSpecialPermission(permission) || containsSpecialPermission(
                    oldPermissions
                ))
            ) {
                realPermissions.removeAll(oldPermissions)
            }
        }

        if (realPermissions.isEmpty()) {
            return getApplicationDetailsIntent(context!!)
        }

        if (realPermissions.size == 1) {
            return getPermissionSettingIntent(context, realPermissions[0])
        }

        return getApplicationDetailsIntent(context!!, realPermissions)
    }

    /**
     * 根据新权限添加旧权限
     */
    fun addOldPermissionsByNewPermissions(requestPermissions: MutableList<String>) {
        // 需要补充的权限列表
        var needSupplementPermissions: MutableList<String>? = null
        for (permission in requestPermissions) {
            // 如果当前运行的 Android 版本大于权限出现的 Android 版本，则证明这个权限在当前设备上不用添加旧权限
            if (getCurrentAndroidVersionCode() >= findAndroidVersionByPermission(
                    permission!!
                )
            ) {
                continue
            }
            // 通过新权限查询到对应的旧权限
            val oldPermissions =
                PermissionHelper.queryOldPermissionByNewPermission(permission) ?: continue
            for (oldPermission in oldPermissions) {
                // 如果请求列表已经包含此权限，就不重复添加，直接跳过
                if (containsPermission(requestPermissions, oldPermission)) {
                    continue
                }
                if (needSupplementPermissions == null) {
                    needSupplementPermissions = ArrayList()
                }
                // 先检查一下有没有添加过，避免重复添加
                if (containsPermission(needSupplementPermissions, oldPermission)) {
                    continue
                }
                // 添加旧版的权限到需要补充的权限列表中
                // 这里解释一下为什么直接添加到 requestPermissions 对象？而是重新弄一个新的集合来存放
                // 这是当前 for 循环正在遍历 requestPermissions 对象，如果在此时添加新的元素，会导致异常
                needSupplementPermissions.add(oldPermission)
            }
        }

        if (needSupplementPermissions == null || needSupplementPermissions.isEmpty()) {
            return
        }
        requestPermissions.addAll(needSupplementPermissions)
    }

    /**
     * 调整权限的请求顺序
     */
    fun adjustPermissionsSort( requestPermissions: MutableList<String>) {
        // 获取低等级权限列表
        val lowLevelPermissions: List<String> = PermissionHelper.getLowLevelPermissions()
        for (lowLevelPermission in lowLevelPermissions) {
            if (!containsPermission(requestPermissions, lowLevelPermission)) {
                continue
            }
            // 如果请求的权限中包含这个低等级权限，则先删除再添加，这个权限就会排到最后面了
            // 这样做的好处在于，可以避免出现的一种情况，当前这个权限严重依赖其他权限
            // 例如：ACCESS_MEDIA_LOCATION 权限需要已授予存储相关权限的情况下才可以申请成功
            requestPermissions.remove(lowLevelPermission)
            requestPermissions.add(lowLevelPermission)
        }
    }

    /**
     * 判断传入的权限组是不是都是危险权限
     */
    fun areAllDangerousPermission( permissions: List<String?>): Boolean {
        for (permission in permissions) {
            if (isSpecialPermission(permission)) {
                return false
            }
        }
        return true
    }
}