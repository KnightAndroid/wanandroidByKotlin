package com.knight.kotlin.library_permiss.delegate

import android.app.Activity
import android.content.Context
import android.os.Environment
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.AndroidVersion
import com.knight.kotlin.library_permiss.AndroidVersion.getTargetSdkVersionCode
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid11
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid13
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.utils.PermissionUtils


/**
 * Author:Knight
 * Time:2023/8/30 16:00
 * Description:PermissionDelegateImplV29
 */
@RequiresApi(api = AndroidVersion.ANDROID_10)
open class PermissionDelegateImplV29 : PermissionDelegateImplV28(){

    override fun isGrantedPermission(
        context: Context,
        permission: String
    ): Boolean {
        if (PermissionUtils.equalsPermission(permission, Permission.ACCESS_MEDIA_LOCATION)) {
            if (!AndroidVersion.isAndroid6()) {
                return true;
            }
            if (!AndroidVersion.isAndroid10()) {
                return PermissionUtils.checkSelfPermission(context, Permission.READ_EXTERNAL_STORAGE);
            }
            return isGrantedReadStoragePermission(context) &&
                    PermissionUtils.checkSelfPermission(context, Permission.ACCESS_MEDIA_LOCATION);
        }

        if (PermissionUtils.equalsPermission(permission, Permission.ACCESS_BACKGROUND_LOCATION)) {
            if (!AndroidVersion.isAndroid6()) {
                return true;
            }
            if (!AndroidVersion.isAndroid10()) {
                return PermissionUtils.checkSelfPermission(context, Permission.ACCESS_FINE_LOCATION);
            }
            return PermissionUtils.checkSelfPermission(context, permission);
        }

        if (PermissionUtils.equalsPermission(permission, Permission.ACTIVITY_RECOGNITION)) {
            if (!AndroidVersion.isAndroid10()) {
                return true;
            }
            return PermissionUtils.checkSelfPermission(context, permission);
        }

        return super.isGrantedPermission(context, permission);
    }

    override fun isDoNotAskAgainPermission(
        activity: Activity,
        permission: String
    ): Boolean {
        if (PermissionUtils.equalsPermission(permission, Permission.ACCESS_BACKGROUND_LOCATION)) {
            if (!AndroidVersion.isAndroid6()) {
                return false;
            }
            if (!AndroidVersion.isAndroid10()) {
                return !PermissionUtils.checkSelfPermission(activity, Permission.ACCESS_FINE_LOCATION) &&
                        !PermissionUtils.shouldShowRequestPermissionRationale(activity, Permission.ACCESS_FINE_LOCATION);
            }
            // 先检查前台的定位权限是否拒绝了
            if (!PermissionUtils.checkSelfPermission(activity, Permission.ACCESS_FINE_LOCATION)) {
                // 如果是的话就判断前台的定位权限是否被永久拒绝了
                return !PermissionUtils.shouldShowRequestPermissionRationale(activity, Permission.ACCESS_FINE_LOCATION);
            }
            // 如果不是的话再去判断后台的定位权限是否被拒永久拒绝了
            return !PermissionUtils.checkSelfPermission(activity, permission) &&
                    !PermissionUtils.shouldShowRequestPermissionRationale(activity, permission);
        }

        if (PermissionUtils.equalsPermission(permission, Permission.ACCESS_MEDIA_LOCATION)) {
            if (!AndroidVersion.isAndroid6()) {
                return false;
            }
            if (!AndroidVersion.isAndroid10()) {
                return !PermissionUtils.checkSelfPermission(activity, Permission.READ_EXTERNAL_STORAGE) &&
                        !PermissionUtils.shouldShowRequestPermissionRationale(activity, Permission.READ_EXTERNAL_STORAGE);
            }
            return isGrantedReadStoragePermission(activity) &&
                    !PermissionUtils.checkSelfPermission(activity, permission) &&
                    !PermissionUtils.shouldShowRequestPermissionRationale(activity, permission);
        }

        if (PermissionUtils.equalsPermission(permission, Permission.ACTIVITY_RECOGNITION)) {
            if (!AndroidVersion.isAndroid10()) {
                return false;
            }
            return !PermissionUtils.checkSelfPermission(activity, permission) &&
                    !PermissionUtils.shouldShowRequestPermissionRationale(activity, permission);
        }

        return super.isDoNotAskAgainPermission(activity, permission);
    }


    companion object {
        /**
         * 是否采用的是非分区存储的模式
         */
        fun isUseDeprecationExternalStorage(): Boolean {
            return Environment.isExternalStorageLegacy()
        }


    }

    /**
     * 判断是否授予了读取文件的权限
     */
    fun isGrantedReadStoragePermission(context: Context): Boolean {
        if (isAndroid13() && getTargetSdkVersionCode(context) >= AndroidVersion.ANDROID_13) {
            return PermissionUtils.checkSelfPermission(context, Permission.READ_MEDIA_IMAGES) ||
                    isGrantedPermission(context, Permission.MANAGE_EXTERNAL_STORAGE)
        }
        return if (isAndroid11() && getTargetSdkVersionCode(context) >= AndroidVersion.ANDROID_11) {
            PermissionUtils.checkSelfPermission(context, Permission.READ_EXTERNAL_STORAGE) ||
                    isGrantedPermission(context, Permission.MANAGE_EXTERNAL_STORAGE)
        } else PermissionUtils.checkSelfPermission(context, Permission.READ_EXTERNAL_STORAGE)
    }


}