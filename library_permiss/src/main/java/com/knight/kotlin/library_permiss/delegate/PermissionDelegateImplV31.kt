package com.knight.kotlin.library_permiss.delegate

import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.AndroidVersion
import com.knight.kotlin.library_permiss.PermissionIntentManager
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.utils.PermissionUtils.areActivityIntent
import com.knight.kotlin.library_permiss.utils.PermissionUtils.checkSelfPermission
import com.knight.kotlin.library_permiss.utils.PermissionUtils.equalsPermission
import com.knight.kotlin.library_permiss.utils.PermissionUtils.getPackageNameUri
import com.knight.kotlin.library_permiss.utils.PermissionUtils.shouldShowRequestPermissionRationale


/**
 * Author:Knight
 * Time:2023/8/30 16:06
 * Description:PermissionDelegateImplV31
 */
@RequiresApi(api = AndroidVersion.ANDROID_12)
open class PermissionDelegateImplV31 : PermissionDelegateImplV30(){
    override fun isGrantedPermission(
        context: Context,
        permission: String
    ): Boolean {
        // 检测闹钟权限
        if (equalsPermission(permission, Permission.SCHEDULE_EXACT_ALARM)) {
            return isGrantedAlarmPermission(context)
        }
        return if (equalsPermission(
                permission, Permission.BLUETOOTH_SCAN
            ) ||
            equalsPermission(
                permission, Permission.BLUETOOTH_CONNECT
            ) ||
            equalsPermission(
                permission, Permission.BLUETOOTH_ADVERTISE
            )
        ) {
            checkSelfPermission(context, permission)
        } else super.isGrantedPermission(context, permission)
    }

    override fun isDoNotAskAgainPermission(
        activity: Activity,
         permission: String
    ): Boolean {
        if (equalsPermission(permission, Permission.SCHEDULE_EXACT_ALARM)) {
            return false
        }
        if (equalsPermission(permission, Permission.BLUETOOTH_SCAN) ||
            equalsPermission(permission, Permission.BLUETOOTH_CONNECT) ||
            equalsPermission(permission, Permission.BLUETOOTH_ADVERTISE)
        ) {
            return !checkSelfPermission(activity, permission) &&
                    !shouldShowRequestPermissionRationale(activity, permission)
        }
        return if (activity.applicationInfo.targetSdkVersion >= AndroidVersion.ANDROID_12 &&
            equalsPermission(permission, Permission.ACCESS_BACKGROUND_LOCATION)
        ) {
            if (!checkSelfPermission(activity, Permission.ACCESS_FINE_LOCATION) &&
                !checkSelfPermission(activity, Permission.ACCESS_COARSE_LOCATION)
            ) {
                !shouldShowRequestPermissionRationale(activity, Permission.ACCESS_FINE_LOCATION) &&
                        !shouldShowRequestPermissionRationale(
                            activity,
                            Permission.ACCESS_COARSE_LOCATION
                        )
            } else !checkSelfPermission(activity, permission) &&
                    !shouldShowRequestPermissionRationale(activity, permission)
        } else super.isDoNotAskAgainPermission(activity, permission!!)
    }

    override fun getPermissionIntent(
        context: Context,
        permission: String
    ): Intent? {
        return if (equalsPermission(
                permission, Permission.SCHEDULE_EXACT_ALARM
            )
        ) {
            getAlarmPermissionIntent(context)
        } else super.getPermissionIntent(context, permission)
    }

    companion object {
        /**
         * 是否有闹钟权限
         */
        private fun isGrantedAlarmPermission( context: Context): Boolean {
            return context.getSystemService(AlarmManager::class.java).canScheduleExactAlarms()
        }

        /**
         * 获取闹钟权限设置界面意图
         */
        private fun getAlarmPermissionIntent(context: Context): Intent {
            var intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            intent.data = getPackageNameUri(context)
            if (!areActivityIntent(context, intent)) {
                intent = PermissionIntentManager.getApplicationDetailsIntent(context)
            }
            return intent
        }
    }
}