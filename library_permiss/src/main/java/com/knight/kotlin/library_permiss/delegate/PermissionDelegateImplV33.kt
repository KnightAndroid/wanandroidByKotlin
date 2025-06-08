package com.knight.kotlin.library_permiss.delegate

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.AndroidVersionTools
import com.knight.kotlin.library_permiss.AndroidVersionTools.isAdaptationAndroidVersionNewFeatures
import com.knight.kotlin.library_permiss.AndroidVersionTools.isAndroid13
import com.knight.kotlin.library_permiss.NotificationPermissionCompat.getPermissionIntent
import com.knight.kotlin.library_permiss.NotificationPermissionCompat.isGrantedPermission
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.utils.PermissionUtils
import com.knight.kotlin.library_permiss.utils.PermissionUtils.equalsPermission


/**
 * Author:Knight
 * Time:2023/8/30 16:10
 * Description:PermissionDelegateImplV33
 */
@RequiresApi(api = AndroidVersionTools.ANDROID_13)
open class PermissionDelegateImplV33 :  PermissionDelegateImplV31(){
    override fun isGrantedPermission(
         context: Context,
         permission: String,
        skipRequest: Boolean
    ): Boolean {
        if (PermissionUtils.equalsPermission(permission, Permission.BODY_SENSORS_BACKGROUND)) {
            if (!isAndroid13()) {
                return PermissionUtils.isGrantedPermission(context, Permission.BODY_SENSORS)
            }
            // 有后台传感器权限的前提条件是授予了前台的传感器权限
            return PermissionUtils.isGrantedPermission(context, Permission.BODY_SENSORS) &&
                    PermissionUtils.isGrantedPermission(context, permission)
        }

        if (PermissionUtils.equalsPermission(permission, Permission.POST_NOTIFICATIONS)) {
            if (!isAndroid13()) {
                return isGrantedPermission(context!!)
            }
            return PermissionUtils.isGrantedPermission(context, permission)
        }

        if (PermissionUtils.equalsPermission(permission, Permission.NEARBY_WIFI_DEVICES)) {
            if (!isAndroid13()) {
                return PermissionUtils.isGrantedPermission(context, Permission.ACCESS_FINE_LOCATION)
            }
            return PermissionUtils.isGrantedPermission(context, permission)
        }

        if (PermissionUtils.equalsPermission(permission, Permission.READ_MEDIA_IMAGES) ||
            PermissionUtils.equalsPermission(permission, Permission.READ_MEDIA_VIDEO) ||
            PermissionUtils.equalsPermission(permission, Permission.READ_MEDIA_AUDIO)
        ) {
            if (!isAndroid13()) {
                return PermissionUtils.isGrantedPermission(
                    context,
                    Permission.READ_EXTERNAL_STORAGE
                )
            }
            return PermissionUtils.isGrantedPermission(context, permission)
        }

        if (isAdaptationAndroidVersionNewFeatures(context, AndroidVersionTools.ANDROID_13) &&
            PermissionUtils.equalsPermission(permission, Permission.READ_EXTERNAL_STORAGE)
        ) {
            return PermissionUtils.isGrantedPermission(context, Permission.READ_MEDIA_IMAGES) &&
                    PermissionUtils.isGrantedPermission(context, Permission.READ_MEDIA_VIDEO) &&
                    PermissionUtils.isGrantedPermission(context, Permission.READ_MEDIA_AUDIO)
        }

        return super.isGrantedPermission(context!!, permission!!, skipRequest)
    }

    override fun isDoNotAskAgainPermission(
         activity: Activity,
         permission: String
    ): Boolean {
        if (PermissionUtils.equalsPermission(permission, Permission.BODY_SENSORS_BACKGROUND)) {
            if (!isAndroid13()) {
                return PermissionUtils.isDoNotAskAgainPermission(activity, Permission.BODY_SENSORS)
            }
            // 先检查前台的传感器权限是否拒绝了
            if (!PermissionUtils.isGrantedPermission(activity, Permission.BODY_SENSORS)) {
                // 如果是的话就判断前台的传感器权限是否被永久拒绝了
                return PermissionUtils.isDoNotAskAgainPermission(activity, Permission.BODY_SENSORS)
            }
            // 如果不是的话再去判断后台的传感器权限是否被拒永久拒绝了
            return PermissionUtils.isDoNotAskAgainPermission(activity, permission)
        }

        if (PermissionUtils.equalsPermission(permission, Permission.POST_NOTIFICATIONS)) {
            if (!isAndroid13()) {
                return false
            }
            return PermissionUtils.isDoNotAskAgainPermission(activity, permission)
        }

        if (PermissionUtils.equalsPermission(permission, Permission.NEARBY_WIFI_DEVICES)) {
            if (!isAndroid13()) {
                return PermissionUtils.isDoNotAskAgainPermission(
                    activity,
                    Permission.ACCESS_FINE_LOCATION
                )
            }
            return PermissionUtils.isDoNotAskAgainPermission(activity, permission)
        }

        if (PermissionUtils.equalsPermission(permission, Permission.READ_MEDIA_IMAGES) ||
            PermissionUtils.equalsPermission(permission, Permission.READ_MEDIA_VIDEO) ||
            PermissionUtils.equalsPermission(permission, Permission.READ_MEDIA_AUDIO)
        ) {
            if (!isAndroid13()) {
                return PermissionUtils.isDoNotAskAgainPermission(
                    activity,
                    Permission.READ_EXTERNAL_STORAGE
                )
            }
            return PermissionUtils.isDoNotAskAgainPermission(activity, permission)
        }

        if (isAdaptationAndroidVersionNewFeatures(activity, AndroidVersionTools.ANDROID_13) &&
            PermissionUtils.equalsPermission(permission, Permission.READ_EXTERNAL_STORAGE)
        ) {
            return PermissionUtils.isDoNotAskAgainPermission(
                activity,
                Permission.READ_MEDIA_IMAGES
            ) &&
                    PermissionUtils.isDoNotAskAgainPermission(
                        activity,
                        Permission.READ_MEDIA_VIDEO
                    ) &&
                    PermissionUtils.isDoNotAskAgainPermission(activity, Permission.READ_MEDIA_AUDIO)
        }

        return super.isDoNotAskAgainPermission(activity!!, permission!!)
    }

    override fun getPermissionSettingIntent(
         context: Context,
         permission: String
    ): Intent? {
        // Github issue 地址：https://github.com/getActivity/XXPermissions/issues/208
        // POST_NOTIFICATIONS 要跳转到权限设置页和 NOTIFICATION_SERVICE 权限是一样的
        if (PermissionUtils.equalsPermission(permission, Permission.POST_NOTIFICATIONS)) {
            return getPermissionIntent(context!!)
        }

        return super.getPermissionSettingIntent(context!!, permission!!)
    }
}