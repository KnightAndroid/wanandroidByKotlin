package com.knight.kotlin.library_permiss.delegate

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.AndroidVersion
import com.knight.kotlin.library_permiss.AndroidVersion.getTargetSdkVersionCode
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid13
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid6
import com.knight.kotlin.library_permiss.NotificationPermissionCompat.getPermissionIntent
import com.knight.kotlin.library_permiss.NotificationPermissionCompat.isGrantedPermission
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.utils.PermissionUtils.checkSelfPermission
import com.knight.kotlin.library_permiss.utils.PermissionUtils.containsPermission
import com.knight.kotlin.library_permiss.utils.PermissionUtils.equalsPermission
import com.knight.kotlin.library_permiss.utils.PermissionUtils.shouldShowRequestPermissionRationale


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
        if (equalsPermission(permission, Permission.BODY_SENSORS_BACKGROUND)) {
            if (!isAndroid6()) {
                return true
            }
            if (!isAndroid13()) {
                return checkSelfPermission(context, Permission.BODY_SENSORS)
            }
            // 有后台传感器权限的前提条件是授予了前台的传感器权限
            return checkSelfPermission(context, Permission.BODY_SENSORS) &&
                    checkSelfPermission(context, permission)
        }

        if (equalsPermission(permission, Permission.POST_NOTIFICATIONS)) {
            if (!isAndroid13()) {
                return isGrantedPermission(context)
            }
            return checkSelfPermission(context, permission)
        }

        if (equalsPermission(permission, Permission.NEARBY_WIFI_DEVICES)) {
            if (!isAndroid6()) {
                return true
            }
            if (!isAndroid13()) {
                return checkSelfPermission(context, Permission.ACCESS_FINE_LOCATION)
            }
            return checkSelfPermission(context, permission)
        }

        if (containsPermission(
                arrayOf(
                    Permission.READ_MEDIA_IMAGES,
                    Permission.READ_MEDIA_VIDEO,
                    Permission.READ_MEDIA_AUDIO
                ).toList(), permission
            )
        ) {
            if (!isAndroid6()) {
                return true
            }
            if (!isAndroid13()) {
                return checkSelfPermission(context, Permission.READ_EXTERNAL_STORAGE)
            }
            return checkSelfPermission(context, permission)
        }

        if (isAndroid13() && getTargetSdkVersionCode(context) >= AndroidVersion.ANDROID_13) {
            // 亲测当这两个条件满足的时候，在 Android 13 不能申请 WRITE_EXTERNAL_STORAGE，会被系统直接拒绝
            // 不会弹出系统授权对话框，框架为了保证不同 Android 版本的回调结果一致性，这里直接返回 true 给到外层
            if (equalsPermission(permission, Permission.WRITE_EXTERNAL_STORAGE)) {
                return true
            }

            if (equalsPermission(permission, Permission.READ_EXTERNAL_STORAGE)) {
                return checkSelfPermission(context, Permission.READ_MEDIA_IMAGES) &&
                        checkSelfPermission(context, Permission.READ_MEDIA_VIDEO) &&
                        checkSelfPermission(context, Permission.READ_MEDIA_AUDIO)
            }
        }

        return super.isGrantedPermission(context, permission)
    }

    override fun isDoNotAskAgainPermission(
        activity: Activity,
        permission: String
    ): Boolean {
        if (equalsPermission(permission, Permission.BODY_SENSORS_BACKGROUND)) {
            if (!isAndroid6()) {
                return false
            }
            if (!isAndroid13()) {
                return !checkSelfPermission(activity, Permission.BODY_SENSORS) &&
                        !shouldShowRequestPermissionRationale(activity, Permission.BODY_SENSORS)
            }
            // 先检查前台的传感器权限是否拒绝了
            if (!checkSelfPermission(activity, Permission.BODY_SENSORS)) {
                // 如果是的话就判断前台的传感器权限是否被永久拒绝了
                return !shouldShowRequestPermissionRationale(activity, Permission.BODY_SENSORS)
            }
            // 如果不是的话再去判断后台的传感器权限是否被拒永久拒绝了
            return !checkSelfPermission(activity, permission) &&
                    !shouldShowRequestPermissionRationale(activity, permission)
        }

        if (equalsPermission(permission, Permission.POST_NOTIFICATIONS)) {
            if (!isAndroid13()) {
                return false
            }
            return !checkSelfPermission(activity, permission) &&
                    !shouldShowRequestPermissionRationale(activity, permission)
        }

        if (equalsPermission(permission, Permission.NEARBY_WIFI_DEVICES)) {
            if (!isAndroid6()) {
                return false
            }
            if (!isAndroid13()) {
                return !checkSelfPermission(activity, Permission.ACCESS_FINE_LOCATION) &&
                        !shouldShowRequestPermissionRationale(activity, Permission.ACCESS_FINE_LOCATION)
            }
            return !checkSelfPermission(activity, permission) &&
                    !shouldShowRequestPermissionRationale(activity, permission)
        }

        if (containsPermission(
                arrayOf(
                    Permission.READ_MEDIA_IMAGES,
                    Permission.READ_MEDIA_VIDEO,
                    Permission.READ_MEDIA_AUDIO
                ).toList(), permission
            )
        ) {
            if (!isAndroid6()) {
                return false
            }
            if (!isAndroid13()) {
                return !checkSelfPermission(activity, Permission.READ_EXTERNAL_STORAGE) &&
                        !shouldShowRequestPermissionRationale(activity, Permission.READ_EXTERNAL_STORAGE)
            }
            return !checkSelfPermission(activity, permission) &&
                    !shouldShowRequestPermissionRationale(activity, permission)
        }

        if (isAndroid13() && getTargetSdkVersionCode(activity) >= AndroidVersion.ANDROID_13) {
            if (equalsPermission(permission, Permission.WRITE_EXTERNAL_STORAGE)) {
                return false
            }

            if (equalsPermission(permission, Permission.READ_EXTERNAL_STORAGE)) {
                return !checkSelfPermission(activity, Permission.READ_MEDIA_IMAGES) &&
                        !shouldShowRequestPermissionRationale(activity, Permission.READ_MEDIA_IMAGES) &&
                        !checkSelfPermission(activity, Permission.READ_MEDIA_VIDEO) &&
                        !shouldShowRequestPermissionRationale(activity, Permission.READ_MEDIA_VIDEO) &&
                        !checkSelfPermission(activity, Permission.READ_MEDIA_AUDIO) &&
                        !shouldShowRequestPermissionRationale(activity, Permission.READ_MEDIA_AUDIO)
            }
        }

        return super.isDoNotAskAgainPermission(activity, permission)
    }
    override fun recheckPermissionResult(context: Context,  permission: String, grantResult: Boolean): Boolean {
        if (isAndroid13() && getTargetSdkVersionCode(context) >= AndroidVersion.ANDROID_13 &&
            equalsPermission(permission, Permission.WRITE_EXTERNAL_STORAGE)
        ) {
            // 在 Android 13 不能申请 WRITE_EXTERNAL_STORAGE，会被系统直接拒绝，在这里需要重新检查权限的状态
            return isGrantedPermission(context, permission)
        }

        return super.recheckPermissionResult(context!!, permission!!, grantResult)
    }
    override fun getPermissionSettingIntent(
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
        } else super.getPermissionSettingIntent(context, permission)
    }
}