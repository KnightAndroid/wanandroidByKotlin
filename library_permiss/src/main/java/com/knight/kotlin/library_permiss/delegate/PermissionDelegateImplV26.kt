package com.knight.kotlin.library_permiss.delegate

import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.AndroidVersion
import com.knight.kotlin.library_permiss.PermissionIntentManager
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.utils.PermissionUtils
import com.knight.kotlin.library_permiss.utils.PermissionUtils.areActivityIntent
import com.knight.kotlin.library_permiss.utils.PermissionUtils.checkOpNoThrow
import com.knight.kotlin.library_permiss.utils.PermissionUtils.checkSelfPermission
import com.knight.kotlin.library_permiss.utils.PermissionUtils.equalsPermission
import com.knight.kotlin.library_permiss.utils.PermissionUtils.getPackageNameUri


/**
 * Author:Knight
 * Time:2023/8/30 15:55
 * Description:PermissionDelegateImplV26
 */
@RequiresApi(api = AndroidVersion.ANDROID_8)
open class PermissionDelegateImplV26 :PermissionDelegateImplV23() {

    override fun isGrantedPermission(
         context: Context,
         permission: String
    ): Boolean {
        if (equalsPermission(permission, Permission.REQUEST_INSTALL_PACKAGES)) {
            return isGrantedInstallPermission(context)
        }
        if (equalsPermission(permission, Permission.PICTURE_IN_PICTURE)) {
            return isGrantedPictureInPicturePermission(context)
        }
        return if (equalsPermission(
                permission, Permission.READ_PHONE_NUMBERS
            ) ||
            equalsPermission(permission, Permission.ANSWER_PHONE_CALLS)
        ) {
            checkSelfPermission(context, permission)
        } else super.isGrantedPermission(context, permission)
    }

    override  fun isDoNotAskAgainPermission(
        activity: Activity,
        permission: String
    ): Boolean {
        if (PermissionUtils.equalsPermission(permission, Permission.REQUEST_INSTALL_PACKAGES)) {
            return false
        }

        if (PermissionUtils.equalsPermission(permission, Permission.PICTURE_IN_PICTURE)) {
            return false
        }

        if (PermissionUtils.equalsPermission(permission, Permission.READ_PHONE_NUMBERS) ||
            PermissionUtils.equalsPermission(permission, Permission.ANSWER_PHONE_CALLS)) {
            return !PermissionUtils.checkSelfPermission(activity, permission) &&
                    !PermissionUtils.shouldShowRequestPermissionRationale(activity, permission)
        }
        return super.isDoNotAskAgainPermission(activity, permission)
    }

    override fun getPermissionIntent(
        context: Context,
        permission: String
    ): Intent? {
        if (equalsPermission(permission, Permission.REQUEST_INSTALL_PACKAGES)) {
            return getInstallPermissionIntent(context)
        }
        return if (equalsPermission(
                permission, Permission.PICTURE_IN_PICTURE
            )
        ) {
            getPictureInPicturePermissionIntent(context)
        } else super.getPermissionIntent(context, permission)
    }

    companion object {
        /**
         * 是否有安装权限
         */
        private fun isGrantedInstallPermission( context: Context): Boolean {
            return context.packageManager.canRequestPackageInstalls()
        }

        /**
         * 获取安装权限设置界面意图
         */
        private fun getInstallPermissionIntent( context: Context): Intent? {
            var intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
            intent.data = getPackageNameUri(context)
            if (!areActivityIntent(context, intent)) {
                intent = PermissionIntentManager.getApplicationDetailsIntent(context)
            }
            return intent
        }

        /**
         * 是否有画中画权限
         */
        private fun isGrantedPictureInPicturePermission( context: Context): Boolean {
            return checkOpNoThrow(context, AppOpsManager.OPSTR_PICTURE_IN_PICTURE)
        }

        /**
         * 获取画中画权限设置界面意图
         */
        private fun getPictureInPicturePermissionIntent( context: Context): Intent? {
            // android.provider.Settings.ACTION_PICTURE_IN_PICTURE_SETTINGS
            var intent = Intent("android.settings.PICTURE_IN_PICTURE_SETTINGS")
            intent.data = getPackageNameUri(context)
            if (!areActivityIntent(context, intent)) {
                intent = PermissionIntentManager.getApplicationDetailsIntent(context)
            }
            return intent
        }
    }

}