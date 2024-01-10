package com.knight.kotlin.library_permiss.delegate

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.AndroidVersion
import com.knight.kotlin.library_permiss.NotificationPermissionCompat.getPermissionIntent
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.utils.PermissionUtils
import com.knight.kotlin.library_permiss.utils.PermissionUtils.equalsPermission


/**
 * Author:Knight
 * Time:2023/8/30 16:10
 * Description:PermissionDelegateImplV33
 */
@RequiresApi(api = AndroidVersion.ANDROID_13)
open class PermissionDelegateImplV33 :  PermissionDelegateImplV31(){
    override fun isGrantedPermission(
        context: Context,
         permission: String
    ): Boolean {
        if (PermissionUtils.equalsPermission(permission, Permission.BODY_SENSORS_BACKGROUND)) {
            // 有后台传感器权限的前提条件是要有前台的传感器权限
            return PermissionUtils.checkSelfPermission(context, Permission.BODY_SENSORS) &&
                    PermissionUtils.checkSelfPermission(context, Permission.BODY_SENSORS_BACKGROUND)
        }

        if (PermissionUtils.equalsPermission(permission, Permission.POST_NOTIFICATIONS) ||
            PermissionUtils.equalsPermission(permission, Permission.NEARBY_WIFI_DEVICES) ||
            PermissionUtils.equalsPermission(permission, Permission.READ_MEDIA_IMAGES) ||
            PermissionUtils.equalsPermission(permission, Permission.READ_MEDIA_VIDEO) ||
            PermissionUtils.equalsPermission(permission, Permission.READ_MEDIA_AUDIO)) {
            return PermissionUtils.checkSelfPermission(context, permission)
        }

        if (AndroidVersion.getTargetSdkVersionCode(context) >= AndroidVersion.ANDROID_13) {
            // 亲测当这两个条件满足的时候，在 Android 13 不能申请 WRITE_EXTERNAL_STORAGE，会被系统直接拒绝
            // 不会弹出系统授权对话框，框架为了保证不同 Android 版本的回调结果一致性，这里直接返回 true 给到外层
            if (PermissionUtils.equalsPermission(permission, Permission.WRITE_EXTERNAL_STORAGE)) {
                return true
            }

            if (PermissionUtils.equalsPermission(permission, Permission.READ_EXTERNAL_STORAGE)) {
                return PermissionUtils.checkSelfPermission(context, Permission.READ_MEDIA_IMAGES) &&
                        PermissionUtils.checkSelfPermission(context, Permission.READ_MEDIA_VIDEO) &&
                        PermissionUtils.checkSelfPermission(context, Permission.READ_MEDIA_AUDIO)
            }
        }

        return super.isGrantedPermission(context, permission)
    }

    override fun isDoNotAskAgainPermission(
        activity: Activity,
        permission: String
    ): Boolean {
        if (PermissionUtils.equalsPermission(permission, Permission.BODY_SENSORS_BACKGROUND)) {
            if (!PermissionUtils.checkSelfPermission(activity, Permission.BODY_SENSORS)) {
                return !PermissionUtils.shouldShowRequestPermissionRationale(activity, Permission.BODY_SENSORS);
            }
            return !PermissionUtils.checkSelfPermission(activity, permission) &&
                    !PermissionUtils.shouldShowRequestPermissionRationale(activity, permission);
        }

        if (PermissionUtils.equalsPermission(permission, Permission.POST_NOTIFICATIONS) ||
            PermissionUtils.equalsPermission(permission, Permission.NEARBY_WIFI_DEVICES) ||
            PermissionUtils.equalsPermission(permission, Permission.READ_MEDIA_IMAGES) ||
            PermissionUtils.equalsPermission(permission, Permission.READ_MEDIA_VIDEO) ||
            PermissionUtils.equalsPermission(permission, Permission.READ_MEDIA_AUDIO)) {
            return !PermissionUtils.checkSelfPermission(activity, permission) &&
                    !PermissionUtils.shouldShowRequestPermissionRationale(activity, permission);
        }

        if (AndroidVersion.getTargetSdkVersionCode(activity) >= AndroidVersion.ANDROID_13) {

            if (PermissionUtils.equalsPermission(permission, Permission.WRITE_EXTERNAL_STORAGE)) {
                return false;
            }

            if (PermissionUtils.equalsPermission(permission, Permission.READ_EXTERNAL_STORAGE)) {
                return !PermissionUtils.checkSelfPermission(activity, Permission.READ_MEDIA_IMAGES) &&
                        !PermissionUtils.shouldShowRequestPermissionRationale(activity, Permission.READ_MEDIA_IMAGES) &&
                        !PermissionUtils.checkSelfPermission(activity, Permission.READ_MEDIA_VIDEO) &&
                        !PermissionUtils.shouldShowRequestPermissionRationale(activity, Permission.READ_MEDIA_VIDEO) &&
                        !PermissionUtils.checkSelfPermission(activity, Permission.READ_MEDIA_AUDIO) &&
                        !PermissionUtils.shouldShowRequestPermissionRationale(activity, Permission.READ_MEDIA_AUDIO);
            }
        }

        return super.isDoNotAskAgainPermission(activity, permission);
    }

    override fun getPermissionIntent(
        context: Context,
        permission: String
    ): Intent? {
        // Github issue 地址：https://github.com/getActivity/XXPermissions/issues/208
        // POST_NOTIFICATIONS 要跳转到权限设置页和 NOTIFICATION_SERVICE 权限是一样的
        return if (equalsPermission(
                permission, Permission.POST_NOTIFICATIONS
            )
        ) {
            getPermissionIntent(context)
        } else super.getPermissionIntent(context, permission)
    }
}