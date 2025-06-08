package com.knight.kotlin.library_permiss.delegate

import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.AndroidVersionTools
import com.knight.kotlin.library_permiss.AndroidVersionTools.getTargetSdkVersionCode
import com.knight.kotlin.library_permiss.AndroidVersionTools.isAndroid12
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.utils.PermissionUtils
import com.knight.kotlin.library_permiss.utils.PermissionUtils.areActivityIntent
import com.knight.kotlin.library_permiss.utils.PermissionUtils.equalsPermission
import com.knight.kotlin.library_permiss.utils.PermissionUtils.getPackageNameUri


/**
 * Author:Knight
 * Time:2023/8/30 16:06
 * Description:PermissionDelegateImplV31
 */
@RequiresApi(api = AndroidVersionTools.ANDROID_12)
open class PermissionDelegateImplV31 : PermissionDelegateImplV30(){
    override fun isGrantedPermission(
         context: Context,
         permission: String,
        skipRequest: Boolean
    ): Boolean {
        if (equalsPermission(permission!!, Permission.SCHEDULE_EXACT_ALARM)) {
            return isGrantedAlarmPermission(context)
        }

        if (equalsPermission(permission, Permission.BLUETOOTH_SCAN)) {
            if (!isAndroid12()) {
                return PermissionUtils.isGrantedPermission(context, Permission.ACCESS_FINE_LOCATION)
            }
            return PermissionUtils.isGrantedPermission(context, permission)
        }

        if (equalsPermission(permission, Permission.BLUETOOTH_CONNECT) ||
            equalsPermission(permission, Permission.BLUETOOTH_ADVERTISE)
        ) {
            if (!isAndroid12()) {
                return true
            }
            return PermissionUtils.isGrantedPermission(context, permission)
        }

        return super.isGrantedPermission(context, permission, skipRequest)
    }

    override fun isDoNotAskAgainPermission(
         activity: Activity,
         permission: String
    ): Boolean {
        if (equalsPermission(permission!!, Permission.SCHEDULE_EXACT_ALARM)) {
            return false
        }

        if (equalsPermission(permission, Permission.BLUETOOTH_SCAN)) {
            if (!isAndroid12()) {
                return PermissionUtils.isDoNotAskAgainPermission(
                    activity,
                    Permission.ACCESS_FINE_LOCATION
                )
            }
            return PermissionUtils.isDoNotAskAgainPermission(activity, permission)
        }

        if (equalsPermission(permission, Permission.BLUETOOTH_CONNECT) ||
            equalsPermission(permission, Permission.BLUETOOTH_ADVERTISE)
        ) {
            if (!isAndroid12()) {
                return false
            }
            return PermissionUtils.isDoNotAskAgainPermission(activity, permission)
        }

        if (equalsPermission(permission, Permission.ACCESS_BACKGROUND_LOCATION) &&
            getTargetSdkVersionCode(activity!!) >= AndroidVersionTools.ANDROID_12
        ) {
            if (!PermissionUtils.isGrantedPermission(activity, Permission.ACCESS_FINE_LOCATION) &&
                !PermissionUtils.isGrantedPermission(activity, Permission.ACCESS_COARSE_LOCATION)
            ) {
                return PermissionUtils.isDoNotAskAgainPermission(
                    activity,
                    Permission.ACCESS_FINE_LOCATION
                ) &&
                        PermissionUtils.isDoNotAskAgainPermission(
                            activity,
                            Permission.ACCESS_COARSE_LOCATION
                        )
            }
            return PermissionUtils.isDoNotAskAgainPermission(activity, permission)
        }

        return super.isDoNotAskAgainPermission(activity!!, permission)
    }

    override fun getPermissionSettingIntent(
         context: Context,
         permission: String
    ): Intent? {
        if (equalsPermission(permission!!, Permission.SCHEDULE_EXACT_ALARM)) {
            return getAlarmPermissionIntent(context)
        }

        return super.getPermissionSettingIntent(context, permission)
    }

    /**
     * 是否有闹钟权限
     */
    private fun isGrantedAlarmPermission( context: Context): Boolean {
        if (!isAndroid12()) {
            return true
        }
        return context.getSystemService(AlarmManager::class.java).canScheduleExactAlarms()
    }

    /**
     * 获取闹钟权限设置界面意图
     */
    private fun getAlarmPermissionIntent( context: Context): Intent {
        if (!isAndroid12()) {
            return getApplicationDetailsIntent(context)
        }
        var intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
        intent.setData(getPackageNameUri(context))
        if (!areActivityIntent(context, intent)) {
            intent = getApplicationDetailsIntent(context)
        }
        return intent
    }
}