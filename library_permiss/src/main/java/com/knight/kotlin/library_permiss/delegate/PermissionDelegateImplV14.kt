package com.knight.kotlin.library_permiss.delegate

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.VpnService
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.AndroidVersion
import com.knight.kotlin.library_permiss.PermissionIntentManager
import com.knight.kotlin.library_permiss.listener.PermissionDelegate
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.utils.PermissionUtils
import com.knight.kotlin.library_permiss.utils.PermissionUtils.areActivityIntent


/**
 * Author:Knight
 * Time:2023/8/30 15:35
 * Description:PermissionDelegateImplV14
 */
@RequiresApi(api = AndroidVersion.ANDROID_4_0)
open class PermissionDelegateImplV14 : PermissionDelegate {
    override fun isGrantedPermission(context: Context, permission: String): Boolean {
        // 检测 VPN 权限
        if (PermissionUtils.equalsPermission(permission, Permission.BIND_VPN_SERVICE)) {
            return isGrantedVpnPermission(context)
        }

        return true
    }

    override fun isPermissionPermanentDenied(activity: Activity, permission: String): Boolean {
        return false
    }

    override fun getPermissionIntent(context: Context, permission: String): Intent? {
        if (PermissionUtils.equalsPermission(permission, Permission.BIND_VPN_SERVICE)) {
            return getVpnPermissionIntent(context)
        }

        return PermissionIntentManager.getApplicationDetailsIntent(context);
    }


    companion object {
        /**
         * 是否有 VPN 权限
         */
        private fun isGrantedVpnPermission(context: Context): Boolean {
            return VpnService.prepare(context) == null
        }

        /**
         * 获取 VPN 权限设置界面意图
         */
        private fun getVpnPermissionIntent(context: Context): Intent {
            var intent = VpnService.prepare(context)
            if (!areActivityIntent(context, intent)) {
                intent = PermissionIntentManager.getApplicationDetailsIntent(context)
            }
            return intent
        }
    }
}