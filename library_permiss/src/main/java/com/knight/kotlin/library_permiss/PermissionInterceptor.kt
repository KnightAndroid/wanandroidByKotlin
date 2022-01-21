package com.knight.kotlin.library_permiss

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import androidx.fragment.app.FragmentActivity
import com.knight.kotlin.library_permiss.listener.IPermissionInterceptor
import com.knight.kotlin.library_permiss.listener.OnPermissionCallback
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_util.toast.ToastUtils

/**
 * Author:Knight
 * Time:2022/1/21 11:45
 * Description:PermissionInterceptor
 */
class PermissionInterceptor: IPermissionInterceptor {

    override fun grantedPermissions(
        activity: FragmentActivity,
        allPermissions: List<String>,
        grantedPermissions: List<String>,
        all: Boolean,
        callback: OnPermissionCallback?
    ) {
        if (callback != null) {
            callback.onGranted(grantedPermissions, all)
        }
    }

    override fun deniedPermissions(
        activity: FragmentActivity,
        allPermissions: List<String>,
        deniedPermissions: List<String>,
        never: Boolean,
        callback: OnPermissionCallback?
    ) {
        callback?.onDenied(deniedPermissions, never)
        if (never) {
            showPermissionDialog(activity, deniedPermissions)
            return
        }
        if (deniedPermissions.size == 1 && Permission.ACCESS_BACKGROUND_LOCATION.equals(
                deniedPermissions[0]
            )
        ) {
            ToastUtils.show(R.string.permission_fail_four)
            return
        }
        if (callback == null) {
            return
        }
        ToastUtils.show(R.string.permission_fail_one)
    }

    /**
     *
     * 显示授权对话框
     * @param activity
     * @param permissions
     */
    protected fun showPermissionDialog(activity: FragmentActivity, permissions: List<String>) {
        AlertDialog.Builder(activity)
            .setTitle(R.string.permission_alert)
            .setCancelable(false)
            .setMessage(getPermissionHint(activity, permissions))
            .setPositiveButton(R.string.permission_goto,
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                    XXPermissions.startPermissionActivity(activity, permissions)
                })
            .show()
    }

    /**
     *
     * 根据权限获取提示
     * @param context
     * @param permissions
     * @return
     */
    protected fun getPermissionHint(context: Context, permissions: List<String>): String {
        if (permissions == null || permissions.isEmpty()) {
            return context.getString(R.string.permission_fail_two)
        }
        val hints: MutableList<String> = ArrayList()
        for (permission in permissions) {
            when (permission) {
                Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE, Permission.MANAGE_EXTERNAL_STORAGE -> {
                    val hint = context.getString(R.string.permission_storage)
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                Permission.CAMERA -> {
                    val hint = context.getString(R.string.permission_camera)
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                Permission.RECORD_AUDIO -> {
                    val hint = context.getString(R.string.permission_microphone)
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_BACKGROUND_LOCATION -> {
                    var hint: String
                    hint = if (!permissions.contains(Permission.ACCESS_FINE_LOCATION)
                        && !permissions.contains(Permission.ACCESS_COARSE_LOCATION)
                    ) {
                        context.getString(R.string.permission_location_background)
                    } else {
                        context.getString(R.string.permission_location)
                    }
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                Permission.BLUETOOTH_SCAN, Permission.BLUETOOTH_CONNECT, Permission.BLUETOOTH_ADVERTISE -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        val hint = context.getString(R.string.permission_bluetooth)
                        if (!hints.contains(hint)) {
                            hints.add(hint)
                        }
                    }
                }
                Permission.READ_PHONE_STATE, Permission.CALL_PHONE, Permission.ADD_VOICEMAIL, Permission.USE_SIP, Permission.READ_PHONE_NUMBERS, Permission.ANSWER_PHONE_CALLS -> {
                    val hint = context.getString(R.string.permission_phone)
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                Permission.GET_ACCOUNTS, Permission.READ_CONTACTS, Permission.WRITE_CONTACTS -> {
                    val hint = context.getString(R.string.permisssion_contacts)
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                Permission.READ_CALENDAR, Permission.WRITE_CALENDAR -> {
                    val hint = context.getString(R.string.permission_calendar)
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                Permission.READ_CALL_LOG, Permission.WRITE_CALL_LOG, Permission.PROCESS_OUTGOING_CALLS -> {
                    val hint =
                        context.getString(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) R.string.permission_call_log else R.string.permission_phone)
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                Permission.BODY_SENSORS -> {
                    val hint = context.getString(R.string.permission_sensors)
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                Permission.ACTIVITY_RECOGNITION -> {
                    val hint = context.getString(R.string.permission_activity_recognition)
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                Permission.SEND_SMS, Permission.RECEIVE_SMS, Permission.READ_SMS, Permission.RECEIVE_WAP_PUSH, Permission.RECEIVE_MMS -> {
                    val hint = context.getString(R.string.permission_sms)
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                Permission.REQUEST_INSTALL_PACKAGES -> {
                    val hint = context.getString(R.string.permission_install)
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                Permission.NOTIFICATION_SERVICE -> {
                    val hint = context.getString(R.string.permission_notification)
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                Permission.SYSTEM_ALERT_WINDOW -> {
                    val hint = context.getString(R.string.permission_window)
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                Permission.WRITE_SETTINGS -> {
                    val hint = context.getString(R.string.permission_setting)
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                Permission.PACKAGE_USAGE_STATS -> {
                    val hint = context.getString(R.string.permission_task)
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                else -> {
                }
            }
        }
        if (!hints.isEmpty()) {
            val builder = StringBuilder()
            for (text in hints) {
                if (builder.length == 0) {
                    builder.append(text)
                } else {
                    builder.append("、").append(text)
                }
            }
            builder.append(" ")
            return context.getString(R.string.permission_fail_three, builder.toString())
        }
        return context.getString(R.string.permission_fail_two)
    }


}