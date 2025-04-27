package com.knight.kotlin.library_permiss.permissions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.AndroidVersion.getAndroidVersionCode
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid11
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid13
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
    private var DELEGATE: PermissionDelegate = PermissionDelegateImplV34()
//    private var DELEGATE: PermissionDelegate =
//    if (AndroidVersion.isAndroid14()) {
//        PermissionDelegateImplV34()
//    } else if (AndroidVersion.isAndroid13()) {
//         PermissionDelegateImplV33 ()
//    } else if (AndroidVersion.isAndroid12()) {
//        PermissionDelegateImplV31 ()
//    } else if (AndroidVersion.isAndroid11()) {
//       PermissionDelegateImplV30 ()
//    } else if (AndroidVersion.isAndroid10()) {
//        PermissionDelegateImplV29 ()
//    } else if (AndroidVersion.isAndroid9()) {
//        PermissionDelegateImplV28 ()
//    } else if (AndroidVersion.isAndroid8()) {
//         PermissionDelegateImplV26 ()
//    } else if (AndroidVersion.isAndroid6()) {
//        PermissionDelegateImplV23 ()
//    } else if (AndroidVersion.isAndroid5()) {
//        PermissionDelegateImplV21 ()
//    } else if (AndroidVersion.isAndroid4_4()) {
//       PermissionDelegateImplV19 ()
//    } else if (AndroidVersion.isAndroid4_3()) {
//         PermissionDelegateImplV18 ()
//    } else {
//        PermissionDelegateImplV14 ()
//    }


    /**
     * 判断某个权限是否授予
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun isGrantedPermission(context: Context,permission: String): Boolean {
        return DELEGATE.isGrantedPermission(context, permission)
    }

    /**
     * 判断某个权限是否被永久拒绝
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun isDoNotAskAgainPermission( activity: Activity, permission: String): Boolean {
        return DELEGATE.isDoNotAskAgainPermission(activity, permission)
    }

    /**
     * 获取权限设置页的意图
     */

    fun getPermissionSettingIntent(context: Context, permission: String): Intent? {
        return DELEGATE.getPermissionSettingIntent(context, permission)
    }

    /**
     * 重新检查权限回调的结果
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun recheckPermissionResult(context: Context, permission: String, grantResult: Boolean): Boolean {
        return DELEGATE.recheckPermissionResult(context, permission, grantResult)
    }

    /**
     * 判断某个权限是否是特殊权限
     */
    fun isSpecialPermission(@NonNull permission: String): Boolean {
        return PermissionHelper.isSpecialPermission(permission)
    }

    /**
     * 判断某个权限集合是否包含特殊权限
     */
    fun containsSpecialPermission(permissions: List<String>?): Boolean {
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
     * 判断某些权限是否全部被授予
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun isGrantedPermissions(@NonNull context: Context, @NonNull permissions: List<String>): Boolean {
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
    fun getGrantedPermissions(@NonNull context: Context, @NonNull permissions: List<String>): List<String> {
        val grantedPermission: MutableList<String> = ArrayList(permissions.size)
        for (permission in permissions) {
            if (isGrantedPermission(context, permission)) {
                grantedPermission.add(permission)
            }
        }
        return grantedPermission
    }

    /**
     * 获取已经拒绝的权限
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun getDeniedPermissions(@NonNull context: Context, @NonNull permissions: List<String>): List<String> {
        val deniedPermission: MutableList<String> = ArrayList(permissions.size)
        for (permission in permissions) {
            if (!isGrantedPermission(context, permission)) {
                deniedPermission.add(permission)
            }
        }
        return deniedPermission
    }

    /**
     * 在权限组中检查是否有某个权限是否被永久拒绝
     *
     * @param activity              Activity对象
     * @param permissions            请求的权限
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun isDoNotAskAgainPermissions(@NonNull activity: Activity, @NonNull permissions: List<String>): Boolean {
        for (permission in permissions) {
            if (isDoNotAskAgainPermission(activity, permission)) {
                return true
            }
        }
        return false
    }

    /**
     * 获取没有授予的权限
     *
     * @param permissions           需要请求的权限组
     * @param grantResults          允许结果组
     */
    fun getDeniedPermissions(@NonNull permissions: List<String>, @NonNull grantResults: IntArray): List<String> {
        val deniedPermissions: MutableList<String> = ArrayList()
        for (i in grantResults.indices) {
            // 把没有授予过的权限加入到集合中
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permissions[i])
            }
        }
        return deniedPermissions
    }

    /**
     * 获取已授予的权限
     *
     * @param permissions       需要请求的权限组
     * @param grantResults      允许结果组
     */
    fun getGrantedPermissions(@NonNull permissions: List<String>, @NonNull grantResults: IntArray): List<String> {
        val grantedPermissions: MutableList<String> = ArrayList()
        for (i in grantResults.indices) {
            // 把授予过的权限加入到集合中
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                grantedPermissions.add(permissions[i])
            }
        }
        return grantedPermissions
    }

    /**
     * 根据传入的权限自动选择最合适的权限设置页
     *
     * @param permissions                 请求失败的权限
     */

    fun getSmartPermissionIntent(context: Context, permissions: List<String>?): Intent? {
        // 如果失败的权限里面不包含特殊权限
        if (permissions == null || permissions.isEmpty()) {
            return getApplicationDetailsIntent(context!!)
        }

        // 危险权限统一处理
        if (!containsSpecialPermission(permissions)) {
            if (permissions.size == 1) {
                return getPermissionSettingIntent(context, permissions[0])
            }
            return getApplicationDetailsIntent(context, permissions)
        }

        // 特殊权限统一处理
        when (permissions.size) {
            1 ->                 // 如果当前只有一个权限被拒绝了
                return getPermissionSettingIntent(context, permissions[0])

            2 -> if (!isAndroid13() &&
                containsPermission(permissions, Permission.NOTIFICATION_SERVICE) &&
                containsPermission(permissions, Permission.POST_NOTIFICATIONS)
            ) {
                return getPermissionSettingIntent(context, Permission.NOTIFICATION_SERVICE)
            }

            3 -> if (isAndroid11() &&
                containsPermission(permissions, Permission.MANAGE_EXTERNAL_STORAGE) &&
                containsPermission(permissions, Permission.READ_EXTERNAL_STORAGE) &&
                containsPermission(permissions, Permission.WRITE_EXTERNAL_STORAGE)
            ) {
                return getPermissionSettingIntent(context, Permission.MANAGE_EXTERNAL_STORAGE)
            }

            else -> {}
        }
        return getApplicationDetailsIntent(context)
    }

    /**
     * 通过新权限兼容旧权限
     *
     * @param requestPermissions            请求的权限组
     */
    fun compatibleOldPermissionByNewPermission(@NonNull requestPermissions: List<String>): List<String> {
        val permissions: MutableList<String> = ArrayList(requestPermissions)
        for (permission in requestPermissions) {
            // 如果当前运行的 Android 版本大于权限出现的 Android 版本，则证明这个权限在当前设备上不用向下兼容
            if (getAndroidVersionCode() >= findAndroidVersionByPermission(permission!!)) {
                continue
            }
            // 通过新权限查询到对应的旧权限
            val oldPermissions: Array<String> = PermissionHelper.queryOldPermissionByNewPermission(permission) ?: continue
            for (oldPermission in oldPermissions) {
                // 如果请求列表已经包含此权限，就不重复添加，直接跳过
                if (PermissionUtils.containsPermission(permissions, oldPermission)) {
                    continue
                }
                // 添加旧版的权限
                permissions.add(oldPermission)
            }
        }
        return permissions
    }
}