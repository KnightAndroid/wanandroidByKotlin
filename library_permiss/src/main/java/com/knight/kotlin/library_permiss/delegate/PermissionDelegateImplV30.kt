package com.knight.kotlin.library_permiss.delegate

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.AndroidVersion
import com.knight.kotlin.library_permiss.PermissionIntentManager
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.utils.PermissionUtils.areActivityIntent
import com.knight.kotlin.library_permiss.utils.PermissionUtils.equalsPermission
import com.knight.kotlin.library_permiss.utils.PermissionUtils.getPackageNameUri


/**
 * Author:Knight
 * Time:2023/8/30 16:02
 * Description:PermissionDelegateImplV30
 */
@RequiresApi(api = AndroidVersion.ANDROID_11)
open class PermissionDelegateImplV30 : PermissionDelegateImplV29(){
    override fun isGrantedPermission(
       context: Context,
       permission: String
    ): Boolean {
        return if (equalsPermission(
                permission, Permission.MANAGE_EXTERNAL_STORAGE
            )
        ) {
            isGrantedManageStoragePermission()
        } else super.isGrantedPermission(context, permission)
    }

    override fun isPermissionPermanentDenied(
       activity: Activity,
       permission: String
    ): Boolean {
        return if (equalsPermission(
                permission, Permission.MANAGE_EXTERNAL_STORAGE
            )
        ) {
            false
        } else super.isPermissionPermanentDenied(activity, permission)
    }

    override fun getPermissionIntent(
        context: Context,
        permission: String
    ): Intent? {
        return if (equalsPermission(
                permission, Permission.MANAGE_EXTERNAL_STORAGE
            )
        ) {
            getManageStoragePermissionIntent(context)
        } else super.getPermissionIntent(context, permission)
    }


    companion object {
        /**
         * 是否有所有文件的管理权限
         */
        private fun isGrantedManageStoragePermission(): Boolean {
            return Environment.isExternalStorageManager()
        }

        /**
         * 获取所有文件的管理权限设置界面意图
         */
        private fun getManageStoragePermissionIntent(context: Context): Intent {
            var intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.data = getPackageNameUri(context)
            if (!areActivityIntent(context, intent)) {
                intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            }
            if (!areActivityIntent(context, intent)) {
                intent = PermissionIntentManager.getApplicationDetailsIntent(context)
            }
            return intent
        }
    }


}