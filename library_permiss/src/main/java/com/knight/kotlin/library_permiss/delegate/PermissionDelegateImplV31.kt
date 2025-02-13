package com.knight.kotlin.library_permiss.delegate

import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.AndroidVersion
import com.knight.kotlin.library_permiss.AndroidVersion.getTargetSdkVersionCode
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid12
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid6
import com.knight.kotlin.library_permiss.PermissionIntentManager
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.utils.PermissionUtils
import com.knight.kotlin.library_permiss.utils.PermissionUtils.areActivityIntent
import com.knight.kotlin.library_permiss.utils.PermissionUtils.checkSelfPermission
import com.knight.kotlin.library_permiss.utils.PermissionUtils.containsPermission
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
        if (equalsPermission(permission, Permission.SCHEDULE_EXACT_ALARM)) {
            if (!isAndroid12()) {
                return true
            }
            return isGrantedAlarmPermission(context)
        }

        if (equalsPermission(permission, Permission.BLUETOOTH_SCAN)) {
            if (!isAndroid6()) {
                return true
            }
            if (!isAndroid12()) {
                return checkSelfPermission(context, Permission.ACCESS_FINE_LOCATION)
            }
            return checkSelfPermission(context, permission)
        }

        if (containsPermission(
                arrayOf(
                    Permission.BLUETOOTH_CONNECT,
                    Permission.BLUETOOTH_ADVERTISE
                ).toList(), permission
            )
        ) {
            if (!isAndroid12()) {
                return true
            }
            return checkSelfPermission(context, permission)
        }

        return super.isGrantedPermission(context, permission)
    }

    override fun isDoNotAskAgainPermission(
        activity: Activity,
         permission: String
    ): Boolean {
        if (equalsPermission(permission, Permission.SCHEDULE_EXACT_ALARM)) {
            return false
        }

        if (equalsPermission(permission, Permission.BLUETOOTH_SCAN)) {
            if (!isAndroid6()) {
                return false
            }
            if (!isAndroid12()) {
                return !checkSelfPermission(activity, Permission.ACCESS_FINE_LOCATION) &&
                        !shouldShowRequestPermissionRationale(activity, Permission.ACCESS_FINE_LOCATION)
            }
            return !checkSelfPermission(activity, permission) &&
                    !shouldShowRequestPermissionRationale(activity, permission)
        }

        if (containsPermission(
                arrayOf(
                    Permission.BLUETOOTH_CONNECT,
                    Permission.BLUETOOTH_ADVERTISE
                ).toList(), permission
            )
        ) {
            if (!isAndroid12()) {
                return false
            }
            return !checkSelfPermission(activity, permission) &&
                    !shouldShowRequestPermissionRationale(activity, permission)
        }

        if (equalsPermission(permission, Permission.ACCESS_BACKGROUND_LOCATION) &&
            isAndroid6() && getTargetSdkVersionCode(activity) >= AndroidVersion.ANDROID_12
        ) {
            if (!checkSelfPermission(activity, Permission.ACCESS_FINE_LOCATION) &&
                !checkSelfPermission(activity, Permission.ACCESS_COARSE_LOCATION)
            ) {
                return !shouldShowRequestPermissionRationale(activity, Permission.ACCESS_FINE_LOCATION) &&
                        !shouldShowRequestPermissionRationale(activity, Permission.ACCESS_COARSE_LOCATION)
            }

            return !checkSelfPermission(activity, permission) &&
                    !shouldShowRequestPermissionRationale(activity, permission)
        }

        return super.isDoNotAskAgainPermission(activity, permission)
    }

    override fun  getPermissionSettingIntent(
        context: Context,
        permission: String
    ): Intent? {
        if (PermissionUtils.equalsPermission(permission, Permission.SCHEDULE_EXACT_ALARM)) {
            if (!AndroidVersion.isAndroid12()) {
                return getApplicationDetailsIntent(context);
            }
            return getAlarmPermissionIntent(context);
        }

        return super.getPermissionSettingIntent(context, permission);
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