package com.knight.kotlin.library_permiss.listener

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.VpnService
import com.knight.kotlin.library_permiss.AndroidVersion.getAndroidVersionCode
import com.knight.kotlin.library_permiss.PermissionHelper
import com.knight.kotlin.library_permiss.PermissionIntentManager
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.permissions.PermissionApi.isSpecialPermission
import com.knight.kotlin.library_permiss.utils.PermissionUtils.areActivityIntent
import com.knight.kotlin.library_permiss.utils.PermissionUtils.equalsPermission


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/13 11:16
 * @descript:权限委托基础实现
 */
open class PermissionDelegateImplBase : PermissionDelegate {
    override fun isGrantedPermission(context: Context,  permission: String): Boolean {
        if (equalsPermission(permission!!, Permission.BIND_VPN_SERVICE)) {
            return isGrantedVpnPermission(context)
        }

        return true
    }

    override fun isDoNotAskAgainPermission(activity: Activity, permission: String): Boolean {
        if (equalsPermission(permission!!, Permission.BIND_VPN_SERVICE)) {
            return false
        }

        return false
    }

    override fun recheckPermissionResult(context: Context,  permission: String, grantResult: Boolean): Boolean {
        // 如果这个权限是特殊权限，则需要重新检查权限的状态
        if (isSpecialPermission(permission!!)) {
            return isGrantedPermission(context, permission)
        }

        if (PermissionHelper.findAndroidVersionByPermission(permission) > getAndroidVersionCode()) {
            // 如果是申请了新权限，但却是旧设备上面运行的，会被系统直接拒绝，在这里需要重新检查权限的状态
            return isGrantedPermission(context, permission)
        }
        return grantResult
    }



    override fun getPermissionSettingIntent(context: Context, permission: String): Intent? {
        if (equalsPermission(permission, Permission.BIND_VPN_SERVICE)) {
            return getVpnPermissionIntent(context)
        }

        return PermissionIntentManager.getApplicationDetailsIntent(context, listOf(permission))
    }

    companion object {
        /**
         * 是否有 VPN 权限
         */
        private fun isGrantedVpnPermission( context: Context): Boolean {
            return VpnService.prepare(context) == null
        }

        /**
         * 获取 VPN 权限设置界面意图
         */
        private fun getVpnPermissionIntent( context: Context): Intent {
            var intent = VpnService.prepare(context)
            if (!areActivityIntent(context, intent)) {
                intent = PermissionIntentManager.getApplicationDetailsIntent(context)
            }
            return intent
        }

        /**
         * 获取应用详情页 Intent
         */
        fun getApplicationDetailsIntent( context: Context): Intent {
            return PermissionIntentManager.getApplicationDetailsIntent(context!!)
        }
    }
}