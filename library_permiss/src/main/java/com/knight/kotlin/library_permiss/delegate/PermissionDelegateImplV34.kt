package com.knight.kotlin.library_permiss.delegate

import android.app.Activity
import android.content.Context
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.AndroidVersionTools
import com.knight.kotlin.library_permiss.AndroidVersionTools.isAndroid14
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.utils.PermissionUtils


/**
 * Author:Knight
 * Time:2024/1/10 16:18
 * Description:PermissionDelegateImplV34
 */
@RequiresApi(api = AndroidVersionTools.ANDROID_14)
open class PermissionDelegateImplV34 : PermissionDelegateImplV33(){


    override fun isGrantedPermission(
         context: Context,
         permission: String,
        skipRequest: Boolean
    ): Boolean {
        if (PermissionUtils.equalsPermission(
                permission!!,
                Permission.READ_MEDIA_VISUAL_USER_SELECTED
            )
        ) {
            if (!isAndroid14()) {
                return true
            }
            return PermissionUtils.isGrantedPermission(context, permission)
        }

        if (!skipRequest && isAndroid14() &&
            (PermissionUtils.equalsPermission(permission, Permission.READ_MEDIA_IMAGES) ||
                    PermissionUtils.equalsPermission(permission, Permission.READ_MEDIA_VIDEO))
        ) {
            // 如果是在 Android 14 上面，并且是图片权限或者视频权限，则需要重新检查权限的状态
            // 这是因为用户授权部分图片或者视频的时候，READ_MEDIA_VISUAL_USER_SELECTED 权限状态是授予的
            // 但是 READ_MEDIA_IMAGES 和 READ_MEDIA_VIDEO 的权限状态是拒绝的
            // 为了权限回调不出现失败，这里只能返回 true，这样告诉外层请求其实是成功的
            return isGrantedPermission(
                context,
                Permission.READ_MEDIA_VISUAL_USER_SELECTED,
                skipRequest
            )
        }

        return super.isGrantedPermission(context!!, permission, skipRequest)
    }

    override fun isDoNotAskAgainPermission(
         activity: Activity,
         permission: String
    ): Boolean {
        if (PermissionUtils.equalsPermission(
                permission!!,
                Permission.READ_MEDIA_VISUAL_USER_SELECTED
            )
        ) {
            if (!isAndroid14()) {
                return false
            }
            return PermissionUtils.isDoNotAskAgainPermission(activity, permission)
        }

        return super.isDoNotAskAgainPermission(activity!!, permission)
    }
}