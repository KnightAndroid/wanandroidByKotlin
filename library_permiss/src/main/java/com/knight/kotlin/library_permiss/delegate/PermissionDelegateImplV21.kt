package com.knight.kotlin.library_permiss.delegate

import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.knight.kotlin.library_permiss.AndroidVersionTools.isAndroid10
import com.knight.kotlin.library_permiss.AndroidVersionTools.isAndroid5
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.utils.PermissionUtils
import com.knight.kotlin.library_permiss.utils.PermissionUtils.areActivityIntent
import com.knight.kotlin.library_permiss.utils.PermissionUtils.checkOpNoThrow
import com.knight.kotlin.library_permiss.utils.PermissionUtils.equalsPermission
import com.knight.kotlin.library_permiss.utils.PermissionUtils.getPackageNameUri


/**
 * Author:Knight
 * Time:2023/8/30 15:44
 * Description:PermissionDelegateImplV21
 */
open class PermissionDelegateImplV21 : PermissionDelegateImplV19(){
    override fun isGrantedPermission(
         context: Context,
         permission: String,
        skipRequest: Boolean
    ): Boolean {
        if (PermissionUtils.equalsPermission(permission, Permission.PACKAGE_USAGE_STATS)) {
            return isGrantedPackagePermission(context)
        }

        return super.isGrantedPermission(context, permission!!, skipRequest)
    }

    override fun isDoNotAskAgainPermission(
         activity: Activity,
         permission: String
    ): Boolean {
        if (PermissionUtils.equalsPermission(permission, Permission.PACKAGE_USAGE_STATS)) {
            return false
        }

        return super.isDoNotAskAgainPermission(activity!!, permission!!)
    }

    override fun getPermissionSettingIntent(
         context: Context,
         permission: String
    ): Intent? {
        if (PermissionUtils.equalsPermission(permission, Permission.PACKAGE_USAGE_STATS)) {
            return getPackagePermissionIntent(context)
        }

        return super.getPermissionSettingIntent(context, permission!!)
    }

    /**
     * 是否有使用统计权限
     */
    private fun isGrantedPackagePermission( context: Context): Boolean {
        if (!isAndroid5()) {
            return true
        }
        return PermissionUtils.checkOpNoThrow(context, AppOpsManager.OPSTR_GET_USAGE_STATS)
    }

    /**
     * 获取使用统计权限设置界面意图
     */
    private fun getPackagePermissionIntent( context: Context): Intent {
        if (!isAndroid5()) {
            return getApplicationDetailsIntent(context)
        }
        var intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        if (isAndroid10()) {
            // 经过测试，只有在 Android 10 及以上加包名才有效果
            // 如果在 Android 10 以下加包名会导致无法跳转
            intent.setData(PermissionUtils.getPackageNameUri(context))
        }
        if (!PermissionUtils.areActivityIntent(context, intent)) {
            intent = getApplicationDetailsIntent(context)
        }
        return intent
    }

}