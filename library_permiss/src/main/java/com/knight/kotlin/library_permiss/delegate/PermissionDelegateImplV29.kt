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
            return hasReadStoragePermission(context) &&
                    PermissionUtils.checkSelfPermission(context, Permission.ACCESS_MEDIA_LOCATION);
        }

        if (PermissionUtils.equalsPermission(permission, Permission.ACCESS_BACKGROUND_LOCATION) ||
            PermissionUtils.equalsPermission(permission, Permission.ACTIVITY_RECOGNITION)) {
            return PermissionUtils.checkSelfPermission(context, permission);
        }

        // 向下兼容 Android 11 新权限
        if (!AndroidVersion.isAndroid11()) {
            if (PermissionUtils.equalsPermission(permission, Permission.MANAGE_EXTERNAL_STORAGE)) {
                // 这个是 Android 10 上面的历史遗留问题，假设申请的是 MANAGE_EXTERNAL_STORAGE 权限
                // 必须要在 AndroidManifest.xml 中注册 android:requestLegacyExternalStorage="true"
                if (!isUseDeprecationExternalStorage()) {
                    return false;
                }
            }
        }

        return super.isGrantedPermission(context, permission);
    }

    override fun isDoNotAskAgainPermission(
        activity: Activity,
        permission: String
    ): Boolean {
        if (PermissionUtils.equalsPermission(permission, Permission.ACCESS_BACKGROUND_LOCATION)) {
            if (!PermissionUtils.checkSelfPermission(activity, Permission.ACCESS_FINE_LOCATION)) {
                return !PermissionUtils.shouldShowRequestPermissionRationale(activity, Permission.ACCESS_FINE_LOCATION);
            }
            return !PermissionUtils.checkSelfPermission(activity, permission) &&
                    !PermissionUtils.shouldShowRequestPermissionRationale(activity, permission);
        }

        if (PermissionUtils.equalsPermission(permission, Permission.ACCESS_MEDIA_LOCATION)) {
            return hasReadStoragePermission(activity) &&
                    !PermissionUtils.checkSelfPermission(activity, permission) &&
                    !PermissionUtils.shouldShowRequestPermissionRationale(activity, permission);
        }

        if (PermissionUtils.equalsPermission(permission, Permission.ACTIVITY_RECOGNITION)) {
            return !PermissionUtils.checkSelfPermission(activity, permission) &&
                    !PermissionUtils.shouldShowRequestPermissionRationale(activity, permission);
        }

        // 向下兼容 Android 11 新权限
        if (!AndroidVersion.isAndroid11()) {
            if (PermissionUtils.equalsPermission(permission, Permission.MANAGE_EXTERNAL_STORAGE)) {
                // 处理 Android 10 上面的历史遗留问题
                if (!isUseDeprecationExternalStorage()) {
                    return true;
                }
            }
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
     * 是否有读取文件的权限
     */
    fun hasReadStoragePermission(context: Context): Boolean {
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