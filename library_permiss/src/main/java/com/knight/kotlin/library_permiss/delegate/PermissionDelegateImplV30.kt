package com.knight.kotlin.library_permiss.delegate

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.AndroidVersionTools
import com.knight.kotlin.library_permiss.AndroidVersionTools.isAndroid10
import com.knight.kotlin.library_permiss.AndroidVersionTools.isAndroid11
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.utils.PermissionUtils
import com.knight.kotlin.library_permiss.utils.PermissionUtils.areActivityIntent
import com.knight.kotlin.library_permiss.utils.PermissionUtils.equalsPermission
import com.knight.kotlin.library_permiss.utils.PermissionUtils.getPackageNameUri


/**
 * Author:Knight
 * Time:2023/8/30 16:02
 * Description:PermissionDelegateImplV30
 */
@RequiresApi(api = AndroidVersionTools.ANDROID_11)
open class PermissionDelegateImplV30 : PermissionDelegateImplV29(){
    override fun isGrantedPermission(
         context: Context,
         permission: String,
        skipRequest: Boolean
    ): Boolean {
        if (equalsPermission(permission!!, Permission.MANAGE_EXTERNAL_STORAGE)) {
            if (!isAndroid11()) {
                // 这个是 Android 10 上面的历史遗留问题，假设申请的是 MANAGE_EXTERNAL_STORAGE 权限
                // 必须要在 AndroidManifest.xml 中注册 android:requestLegacyExternalStorage="true"
                if (isAndroid10() && !isUseDeprecationExternalStorage()) {
                    return false
                }
                return PermissionUtils.isGrantedPermission(
                    context,
                    Permission.READ_EXTERNAL_STORAGE
                ) &&
                        PermissionUtils.isGrantedPermission(
                            context,
                            Permission.WRITE_EXTERNAL_STORAGE
                        )
            }
            return isGrantedManageStoragePermission()
        }

        return super.isGrantedPermission(context!!, permission, skipRequest)
    }

    override fun isDoNotAskAgainPermission(
         activity: Activity,
         permission: String
    ): Boolean {
        if (equalsPermission(permission!!, Permission.MANAGE_EXTERNAL_STORAGE)) {
            return false
        }

        return super.isDoNotAskAgainPermission(activity!!, permission)
    }

    override fun getPermissionSettingIntent(
         context: Context,
         permission: String
    ): Intent? {
        if (equalsPermission(permission!!, Permission.MANAGE_EXTERNAL_STORAGE)) {
            return getManageStoragePermissionIntent(context)
        }

        return super.getPermissionSettingIntent(context, permission)
    }

    /**
     * 是否有所有文件的管理权限
     */
    @RequiresApi(AndroidVersionTools.ANDROID_11)
    private fun isGrantedManageStoragePermission(): Boolean {
        return Environment.isExternalStorageManager()
    }

    /**
     * 获取所有文件的管理权限设置界面意图
     */
    private fun getManageStoragePermissionIntent( context: Context): Intent {
        if (!isAndroid11()) {
            return getApplicationDetailsIntent(context)
        }
        var intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
        intent.setData(getPackageNameUri(context))

        if (!areActivityIntent(context, intent)) {
            intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
        }

        if (!areActivityIntent(context, intent)) {
            intent = getApplicationDetailsIntent(context)
        }
        return intent
    }


}