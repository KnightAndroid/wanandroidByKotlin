package com.knight.kotlin.library_permiss.delegate

import android.app.Activity
import android.content.Context
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.AndroidVersion
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.utils.PermissionUtils

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
            return PermissionUtils.checkSelfPermission(context, Permission.READ_MEDIA_VISUAL_USER_SELECTED);
        }

        // 如果用户授予了部分照片访问，那么 READ_MEDIA_VISUAL_USER_SELECTED 权限状态是授予的，而 READ_MEDIA_IMAGES 权限状态是拒绝的
        if (PermissionUtils.equalsPermission(permission, Permission.READ_MEDIA_IMAGES) &&
            !PermissionUtils.checkSelfPermission(context, Permission.READ_MEDIA_IMAGES)) {
            return PermissionUtils.checkSelfPermission(context, Permission.READ_MEDIA_VISUAL_USER_SELECTED);
        }

        // 如果用户授予了部分视频访问，那么 READ_MEDIA_VISUAL_USER_SELECTED 权限状态是授予的，而 READ_MEDIA_VIDEO 权限状态是拒绝的
        if (PermissionUtils.equalsPermission(permission, Permission.READ_MEDIA_VIDEO) &&
            !PermissionUtils.checkSelfPermission(context, Permission.READ_MEDIA_VIDEO)) {
            return PermissionUtils.checkSelfPermission(context, Permission.READ_MEDIA_VISUAL_USER_SELECTED);
        }

        return super.isGrantedPermission(context, permission);
    }

    override fun isDoNotAskAgainPermission(
        activity: Activity,
        permission: String
    ): Boolean {
        if (PermissionUtils.equalsPermission(permission, Permission.READ_MEDIA_VISUAL_USER_SELECTED)) {
            return !PermissionUtils.checkSelfPermission(activity, permission) &&
                    !PermissionUtils.shouldShowRequestPermissionRationale(activity, permission)
        }

        return super.isDoNotAskAgainPermission(activity, permission)
    }
}