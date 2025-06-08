package com.knight.kotlin.library_permiss


import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_permiss.PermissionConverter.getNamesByPermissions
import com.knight.kotlin.library_permiss.listener.OnPermissionCallback
import com.knight.kotlin.library_permiss.listener.OnPermissionInterceptor
import com.knight.kotlin.library_permiss.listener.OnPermissionPageCallback
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_util.toast


/**
 * @Description
 * @Author knight
 * @Time 2025/6/8 22:44
 * 
 */

class PermissionInterceptor : OnPermissionInterceptor {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun deniedPermissionRequest(
         activity: Activity,  requestPermissions: List<String>,
         deniedPermissions: List<String>, doNotAskAgain: Boolean,
         callback: OnPermissionCallback?
    ) {
        callback?.onDenied(deniedPermissions, doNotAskAgain)

        if (doNotAskAgain) {
            if (deniedPermissions.size == 1 && Permission.ACCESS_MEDIA_LOCATION.equals(
                    deniedPermissions[0]
                )
            ) {
                toast(R.string.permission_media_location_hint_fail)
                return
            }

            showPermissionSettingDialog(activity, requestPermissions, deniedPermissions, callback)
            return
        }

        if (deniedPermissions.size == 1) {
            val deniedPermission = deniedPermissions[0]

            val backgroundPermissionOptionLabel = getBackgroundPermissionOptionLabel(activity)

            if (Permission.ACCESS_BACKGROUND_LOCATION.equals(deniedPermission)) {
                toast(
                    activity.getString(
                        R.string.permission_background_location_fail_hint,
                        backgroundPermissionOptionLabel
                    )
                )
                return
            }

            if (Permission.BODY_SENSORS_BACKGROUND.equals(deniedPermission)) {
                toast(
                    activity.getString(
                        R.string.permission_background_sensors_fail_hint,
                        backgroundPermissionOptionLabel
                    )
                )
                return
            }
        }

        val message: String
        val permissionNames = getNamesByPermissions(activity, deniedPermissions)
        message = if (!permissionNames.isEmpty()) {
            activity.getString(R.string.permission_fail_assign_hint, permissionNames)
        } else {
            activity.getString(R.string.permission_fail_hint)
        }
        toast(message)
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun showPermissionSettingDialog(
        activity: Activity?, requestPermissions: List<String>,
        deniedPermissions: List<String>, callback: OnPermissionCallback?
    ) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            return
        }

        var message: String? = null

        val permissionNames = getNamesByPermissions(activity, deniedPermissions)
        if (!permissionNames.isEmpty()) {
            if (deniedPermissions.size == 1) {
                val deniedPermission = deniedPermissions[0]

                if (Permission.ACCESS_BACKGROUND_LOCATION.equals(deniedPermission)) {
                    message = activity.getString(
                        R.string.permission_manual_assign_fail_background_location_hint,
                        getBackgroundPermissionOptionLabel(activity)
                    )
                } else if (Permission.BODY_SENSORS_BACKGROUND.equals(deniedPermission)) {
                    message = activity.getString(
                        R.string.permission_manual_assign_fail_background_sensors_hint,
                        getBackgroundPermissionOptionLabel(activity)
                    )
                }
            }
            if (TextUtils.isEmpty(message)) {
                message = activity.getString(
                    R.string.permission_manual_assign_fail_hint,
                    permissionNames
                )
            }
        } else {
            message = activity.getString(R.string.permission_manual_fail_hint)
        }

        showDialog(
            activity,
            activity.getString(R.string.permission_alert),
            message,
            true,
            activity.getString(R.string.permission_goto_setting_page)
        ) { dialog: DialogInterface, which: Int ->
            dialog.dismiss()
            XXPermissions.startPermissionActivity(
                activity,
                deniedPermissions,
                object : OnPermissionPageCallback {
                    override fun onGranted() {
                        if (callback == null) {
                            return
                        }
                        callback.onGranted(requestPermissions, true)
                    }

                    override fun onDenied() {
                        showPermissionSettingDialog(
                            activity,
                            requestPermissions,
                            XXPermissions.getDeniedPermissions(activity, requestPermissions),
                            callback
                        )
                    }
                })
        }
    }

    /**
     * 获取后台权限的《始终允许》选项的文案
     */
    
    private fun getBackgroundPermissionOptionLabel(context: Context): String {
        var backgroundPermissionOptionLabel = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            backgroundPermissionOptionLabel =
                context.packageManager.backgroundPermissionOptionLabel.toString()
        }
        if (TextUtils.isEmpty(backgroundPermissionOptionLabel)) {
            backgroundPermissionOptionLabel =
                context.getString(R.string.permission_background_default_option_label)
        }
        return backgroundPermissionOptionLabel
    }

    private fun showDialog(
         activity: Activity,
         dialogTitle: String,
         dialogMessage: String?,
        dialogCancelable: Boolean,
         confirmButtonText: String,
         confirmListener: DialogInterface.OnClickListener
    ) {
        showDialog(
            activity,
            dialogTitle,
            dialogMessage,
            dialogCancelable,
            confirmButtonText,
            confirmListener,
            null,
            null
        )
    }

    /**
     * 显示对话框
     *
     * @param activity                  Activity 对象
     * @param dialogTitle               对话框标题
     * @param dialogMessage             对话框消息
     * @param dialogCancelable          对话框是否可取消
     * @param confirmButtonText         对话框确认按钮文本
     * @param confirmListener           对话框确认按钮点击事件
     * @param cancelButtonText          对话框取消按钮文本
     * @param cancelListener            对话框取消按钮点击事件
     */
    private fun showDialog(
        activity: Activity,
        dialogTitle: String,
        dialogMessage: String?,
        dialogCancelable: Boolean,
        confirmButtonText: String,
        confirmListener: DialogInterface.OnClickListener,
        cancelButtonText: String?,
        cancelListener: DialogInterface.OnClickListener?
    ) {
        // 另外这里需要判断 Activity 的类型来申请权限，这是因为只有 AppCompatActivity 才能调用 Support 包的 AlertDialog 来显示，否则会出现报错
        // java.lang.IllegalStateException: You need to use a Theme.AppCompat theme (or descendant) with this activity
        // 为什么不直接用 App 包 AlertDialog 来显示，而是两套规则？因为 App 包 AlertDialog 是系统自带的类，不同 Android 版本展现的样式可能不太一样
        // 如果这个 Android 版本比较低，那么这个对话框的样式就会变得很丑，准确来讲也不能说丑，而是当时系统的 UI 设计就是那样，它只是跟随系统的样式而已
        val dialog: AlertDialog = if (activity is AppCompatActivity) {
            AlertDialog.Builder(activity)
                .setTitle(dialogTitle)
                .setMessage(dialogMessage)
                .setCancelable(dialogCancelable)
                .setPositiveButton(confirmButtonText, confirmListener)
                .setNegativeButton(cancelButtonText, cancelListener)
                .create()
        } else {
            AlertDialog.Builder(activity)
                .setTitle(dialogTitle)
                .setMessage(dialogMessage)
                .setCancelable(dialogCancelable)
                .setPositiveButton(confirmButtonText, confirmListener)
                .setNegativeButton(cancelButtonText, cancelListener)
                .create()
        }
        dialog.show()
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(CacheUtils.getThemeColor())
        // 将 Activity 和 Dialog 生命周期绑定在一起，避免可能会出现的内存泄漏
        // 当然如果上面创建的 Dialog 已经有做了生命周期管理，则不需要执行下面这行代码
        WindowLifecycleManager.bindDialogLifecycle(activity, dialog)
    }
}