package com.knight.kotlin.library_permiss.delegate

import android.app.Activity
import android.content.Context
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.AndroidVersion
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid14
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.utils.PermissionUtils
import com.knight.kotlin.library_permiss.utils.PermissionUtils.containsPermission


/**
 * Author:Knight
 * Time:2024/1/10 16:18
 * Description:PermissionDelegateImplV34
 */
@RequiresApi(api = AndroidVersion.ANDROID_14)
open class PermissionDelegateImplV34 : PermissionDelegateImplV33(){


    override fun isGrantedPermission(
        context: Context,
        permission: String
    ): Boolean {
        if (PermissionUtils.equalsPermission(permission, Permission.READ_MEDIA_VISUAL_USER_SELECTED)) {
            if (!AndroidVersion.isAndroid14()) {
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
        if (PermissionUtils.equalsPermission(permission, Permission.READ_MEDIA_VISUAL_USER_SELECTED)) {
            if (!AndroidVersion.isAndroid14()) {
                return false;
            }
            return !PermissionUtils.checkSelfPermission(activity, permission) &&
                    !PermissionUtils.shouldShowRequestPermissionRationale(activity, permission);
        }

        return super.isDoNotAskAgainPermission(activity, permission);
    }

    override fun recheckPermissionResult( context: Context,  permission: String, grantResult: Boolean): Boolean {
        // 如果是在 Android 14 上面，并且是图片权限或者视频权限，则需要重新检查权限的状态
        // 这是因为用户授权部分图片或者视频的时候，READ_MEDIA_VISUAL_USER_SELECTED 权限状态是授予的
        // 但是 READ_MEDIA_IMAGES 和 READ_MEDIA_VIDEO 的权限状态是拒绝的
        if (isAndroid14() &&
            containsPermission(
                arrayOf(Permission.READ_MEDIA_IMAGES, Permission.READ_MEDIA_VIDEO).toList(), permission
            )
        ) {
            return isGrantedPermission(context!!, Permission.READ_MEDIA_VISUAL_USER_SELECTED)
        }

        return super.recheckPermissionResult(context!!, permission!!, grantResult)
    }
}