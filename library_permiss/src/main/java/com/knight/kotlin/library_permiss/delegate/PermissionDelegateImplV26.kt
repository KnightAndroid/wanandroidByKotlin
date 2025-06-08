package com.knight.kotlin.library_permiss.delegate

import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.AndroidVersionTools
import com.knight.kotlin.library_permiss.AndroidVersionTools.isAndroid8
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.utils.PermissionUtils
import com.knight.kotlin.library_permiss.utils.PermissionUtils.areActivityIntent
import com.knight.kotlin.library_permiss.utils.PermissionUtils.checkOpNoThrow
import com.knight.kotlin.library_permiss.utils.PermissionUtils.getPackageNameUri


/**
 * Author:Knight
 * Time:2023/8/30 15:55
 * Description:PermissionDelegateImplV26
 */
@RequiresApi(api = AndroidVersionTools.ANDROID_8)
open class PermissionDelegateImplV26 :PermissionDelegateImplV23() {

    override fun isGrantedPermission(
         context: Context,
         permission: String,
        skipRequest: Boolean
    ): Boolean {
        if (PermissionUtils.equalsPermission(permission!!, Permission.REQUEST_INSTALL_PACKAGES)) {
            return isGrantedInstallPermission(context)
        }

        if (PermissionUtils.equalsPermission(permission, Permission.PICTURE_IN_PICTURE)) {
            return isGrantedPictureInPicturePermission(context)
        }

        if (PermissionUtils.equalsPermission(permission, Permission.READ_PHONE_NUMBERS)) {
            if (!isAndroid8()) {
                return PermissionUtils.isGrantedPermission(context, Permission.READ_PHONE_STATE)
            }
            return PermissionUtils.isGrantedPermission(context, permission)
        }

        if (PermissionUtils.equalsPermission(permission, Permission.ANSWER_PHONE_CALLS)) {
            if (!isAndroid8()) {
                return true
            }
            return PermissionUtils.isGrantedPermission(context, permission)
        }

        return super.isGrantedPermission(context, permission, skipRequest)
    }

    override fun isDoNotAskAgainPermission(
         activity: Activity,
         permission: String
    ): Boolean {
        if (PermissionUtils.equalsPermission(permission!!, Permission.REQUEST_INSTALL_PACKAGES)) {
            return false
        }

        if (PermissionUtils.equalsPermission(permission, Permission.PICTURE_IN_PICTURE)) {
            return false
        }

        if (PermissionUtils.equalsPermission(permission, Permission.READ_PHONE_NUMBERS)) {
            if (!isAndroid8()) {
                return PermissionUtils.isDoNotAskAgainPermission(
                    activity,
                    Permission.READ_PHONE_STATE
                )
            }
            return PermissionUtils.isDoNotAskAgainPermission(activity, permission)
        }

        if (PermissionUtils.equalsPermission(permission, Permission.ANSWER_PHONE_CALLS)) {
            if (!isAndroid8()) {
                return false
            }
            return PermissionUtils.isDoNotAskAgainPermission(activity, permission)
        }

        return super.isDoNotAskAgainPermission(activity!!, permission)
    }

    override fun getPermissionSettingIntent(
         context: Context,
         permission: String
    ): Intent? {
        if (PermissionUtils.equalsPermission(permission!!, Permission.REQUEST_INSTALL_PACKAGES)) {
            return getInstallPermissionIntent(context)
        }

        if (PermissionUtils.equalsPermission(permission, Permission.PICTURE_IN_PICTURE)) {
            return getPictureInPicturePermissionIntent(context)
        }

        return super.getPermissionSettingIntent(context, permission)
    }

    /**
     * 是否有安装权限
     */
    private fun isGrantedInstallPermission( context: Context): Boolean {
        if (!isAndroid8()) {
            return true
        }
        return context.packageManager.canRequestPackageInstalls()
    }

    /**
     * 获取安装权限设置界面意图
     */
    private fun getInstallPermissionIntent( context: Context): Intent {
        if (!isAndroid8()) {
            return getApplicationDetailsIntent(context)
        }
        var intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
        intent.setData(getPackageNameUri(context))
        if (!areActivityIntent(context, intent)) {
            intent = getApplicationDetailsIntent(context)
        }
        return intent
    }

    /**
     * 是否有画中画权限
     */
    private fun isGrantedPictureInPicturePermission( context: Context): Boolean {
        if (!isAndroid8()) {
            return true
        }
        return checkOpNoThrow(context, AppOpsManager.OPSTR_PICTURE_IN_PICTURE)
    }

    /**
     * 获取画中画权限设置界面意图
     */
    private fun getPictureInPicturePermissionIntent( context: Context): Intent {
        if (!isAndroid8()) {
            return getApplicationDetailsIntent(context)
        }
        // android.provider.Settings.ACTION_PICTURE_IN_PICTURE_SETTINGS
        var intent = Intent("android.settings.PICTURE_IN_PICTURE_SETTINGS")
        intent.setData(getPackageNameUri(context))
        if (!areActivityIntent(context, intent)) {
            intent = getApplicationDetailsIntent(context)
        }
        return intent
    }

}