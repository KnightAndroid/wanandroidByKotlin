package com.knight.kotlin.library_permiss.permissions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.knight.kotlin.library_permiss.AndroidVersion
import com.knight.kotlin.library_permiss.delegate.PermissionDelegateImplV14
import com.knight.kotlin.library_permiss.delegate.PermissionDelegateImplV18
import com.knight.kotlin.library_permiss.delegate.PermissionDelegateImplV19
import com.knight.kotlin.library_permiss.delegate.PermissionDelegateImplV21
import com.knight.kotlin.library_permiss.delegate.PermissionDelegateImplV23
import com.knight.kotlin.library_permiss.delegate.PermissionDelegateImplV26
import com.knight.kotlin.library_permiss.delegate.PermissionDelegateImplV28
import com.knight.kotlin.library_permiss.delegate.PermissionDelegateImplV29
import com.knight.kotlin.library_permiss.delegate.PermissionDelegateImplV30
import com.knight.kotlin.library_permiss.delegate.PermissionDelegateImplV31
import com.knight.kotlin.library_permiss.delegate.PermissionDelegateImplV33
import com.knight.kotlin.library_permiss.listener.PermissionDelegate
import com.knight.kotlin.library_permiss.utils.PermissionUtils


/**
 * Author:Knight
 * Time:2023/8/29 17:41
 * Description:PermissionApi
 */
object PermissionApi {



    private var DELEGATE: PermissionDelegate = if (AndroidVersion.isAndroid13()) {
         PermissionDelegateImplV33 ()
    } else if (AndroidVersion.isAndroid12()) {
        PermissionDelegateImplV31 ();
    } else if (AndroidVersion.isAndroid11()) {
       PermissionDelegateImplV30 ();
    } else if (AndroidVersion.isAndroid10()) {
        PermissionDelegateImplV29 ();
    } else if (AndroidVersion.isAndroid9()) {
        PermissionDelegateImplV28 ();
    } else if (AndroidVersion.isAndroid8()) {
         PermissionDelegateImplV26 ();
    } else if (AndroidVersion.isAndroid6()) {
        PermissionDelegateImplV23 ();
    } else if (AndroidVersion.isAndroid5()) {
        PermissionDelegateImplV21 ();
    } else if (AndroidVersion.isAndroid4_4()) {
       PermissionDelegateImplV19 ();
    } else if (AndroidVersion.isAndroid4_3()) {
         PermissionDelegateImplV18 ();
    } else {
        PermissionDelegateImplV14 ();
    }


    /**
     * 判断某个权限是否授予
     */
    fun isGrantedPermission(context: Context, permission: String): Boolean {
        return DELEGATE.isGrantedPermission(context, permission)
    }

    /**
     * 判断某个权限是否被永久拒绝
     */
    fun isPermissionPermanentDenied(
        activity: Activity,
        permission: String
    ): Boolean {
        return DELEGATE.isPermissionPermanentDenied(activity, permission)
    }

    /**
     * 获取权限设置页意图
     */
    fun getPermissionIntent(context: Context, permission: String): Intent? {
        return DELEGATE.getPermissionIntent(context, permission)
    }

    /**
     * 判断某个权限是否是特殊权限
     */
    fun isSpecialPermission(permission: String?): Boolean {
        return PermissionUtils.isSpecialPermission(permission!!)
    }

    /**
     * 判断某个权限集合是否包含特殊权限
     */
    fun containsSpecialPermission(permissions: List<String?>?): Boolean {
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
    fun getGrantedPermissions(
        context: Context,
        permissions: List<String>
    ): List<String> {
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
    fun getDeniedPermissions(
        context: Context,
        permissions: List<String>
    ): List<String> {
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
    fun isPermissionPermanentDenied(
        activity: Activity,
        permissions: List<String>
    ): Boolean {
        for (permission in permissions) {
            if (isPermissionPermanentDenied(activity, permission)) {
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
    fun getDeniedPermissions(
        permissions: List<String>,
        grantResults: IntArray
    ): List<String> {
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
    fun getGrantedPermissions(
        permissions: List<String>,
        grantResults: IntArray
    ): List<String> {
        val grantedPermissions: MutableList<String> = ArrayList()
        for (i in grantResults.indices) {
            // 把授予过的权限加入到集合中
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                grantedPermissions.add(permissions[i])
            }
        }
        return grantedPermissions
    }
}