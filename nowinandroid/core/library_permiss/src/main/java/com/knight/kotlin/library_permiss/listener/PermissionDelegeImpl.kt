package com.knight.kotlin.library_permiss.listener

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.VpnService
import com.knight.kotlin.library_permiss.PermissionIntentManager
import com.knight.kotlin.library_permiss.PermissionIntentManager.getApplicationDetailsIntent
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.utils.PermissionUtils


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/13 11:16
 * @descript:权限委托基础实现
 */
open class PermissionDelegateImpl : PermissionDelegate {
    override fun isGrantedPermission(
         context: Context,
         permission: String,
        skipRequest: Boolean
    ): Boolean {
        if (PermissionUtils.equalsPermission(permission, Permission.BIND_VPN_SERVICE)) {
            return isGrantedVpnPermission(context)
        }

        return true
    }

    override fun isDoNotAskAgainPermission(
         activity: Activity,
         permission: String
    ): Boolean {
        if (PermissionUtils.equalsPermission(permission, Permission.BIND_VPN_SERVICE)) {
            return false
        }

        return false
    }

    override fun getPermissionSettingIntent(
         context: Context,
         permission: String
    ): Intent? {
        if (PermissionUtils.equalsPermission(permission, Permission.BIND_VPN_SERVICE)) {
            return getVpnPermissionIntent(context)
        }

        return getApplicationDetailsIntent(context, listOf(permission))
    }

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
        if (!PermissionUtils.areActivityIntent(context, intent)) {
            intent = PermissionIntentManager.getApplicationDetailsIntent(context)
        }
        return intent
    }

    /**
     * 获取应用详情页 Intent
     */
    fun getApplicationDetailsIntent( context: Context?): Intent {
        return PermissionIntentManager.getApplicationDetailsIntent(context!!)
    }
}