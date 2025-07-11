package com.knight.kotlin.library_permiss


import XXPermissions
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.knight.kotlin.library_permiss.PermissionConverter.getNickNamesByPermissions
import com.knight.kotlin.library_permiss.WindowLifecycleManager.bindDialogLifecycle
import com.knight.kotlin.library_permiss.permission.PermissionGroups
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_util.toast


/**
 * @Description
 * @Author knight
 * @Time 2025/6/8 22:44
 * 
 */

class PermissionInterceptor : OnPermissionInterceptor {
    override fun deniedPermissionRequest(
         activity: Activity,  requestPermissions: List<IPermission>,
         deniedPermissions: List<IPermission>, doNotAskAgain: Boolean,
        callback: OnPermissionCallback
    ) {
        callback?.onDenied(deniedPermissions, doNotAskAgain)

        val permissionHint = generatePermissionHint(activity, deniedPermissions, doNotAskAgain)
        if (!doNotAskAgain) {
            // 如果没有勾选不再询问选项，就弹 Toast 提示给用户
            toast(permissionHint)
            return
        }

        // 如果勾选了不再询问选项，就弹 Dialog 引导用户去授权
        showPermissionSettingDialog(activity, requestPermissions, deniedPermissions, callback, permissionHint)
    }

    private fun showPermissionSettingDialog(
         activity: Activity,  requestPermissions: List<IPermission>,
         deniedPermissions: List<IPermission>, callback: OnPermissionCallback,
         permissionHint: String
    ) {
        if (activity.isFinishing || activity.isDestroyed) {
            return
        }

        val dialogTitle = activity.getString(R.string.permission_alert)
        val confirmButtonText = activity.getString(R.string.permission_go_to_authorization)
        val confirmListener = DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
            dialog.dismiss()
            XXPermissions.startPermissionActivity(activity, deniedPermissions, object : OnPermissionPageCallback {
                override fun onGranted() {

                    // 用户全部授权了，回调成功给外层监听器，免得用户还要再发起权限申请
                    callback?.onGranted(requestPermissions, true)
                }

                override fun onDenied() {
                    val latestDeniedPermissions = XXPermissions.getDeniedPermissions(activity, requestPermissions)
                    // 递归显示对话框，让提示用户授权，只不过对话框是可取消的，用户不想授权了，随时可以点击返回键或者对话框蒙层来取消显示
                    showPermissionSettingDialog(
                        activity, requestPermissions, latestDeniedPermissions, callback,
                        generatePermissionHint(activity, latestDeniedPermissions, true)
                    )
                }
            })
        }

        // 另外这里需要判断 Activity 的类型来申请权限，这是因为只有 AppCompatActivity 才能调用 Support 包的 AlertDialog 来显示，否则会出现报错
        // java.lang.IllegalStateException: You need to use a Theme.AppCompat theme (or descendant) with this activity
        // 为什么不直接用 App 包 AlertDialog 来显示，而是两套规则？因为 App 包 AlertDialog 是系统自带的类，不同 Android 版本展现的样式可能不太一样
        // 如果这个 Android 版本比较低，那么这个对话框的样式就会变得很丑，准确来讲也不能说丑，而是当时系统的 UI 设计就是那样，它只是跟随系统的样式而已
        val dialog: Dialog = if (activity is AppCompatActivity) {
            AlertDialog.Builder(activity)
                .setTitle(dialogTitle)
                .setMessage(permissionHint) // 这里需要设置成可取消的，这样用户不想授权了，随时可以点击返回键或者对话框蒙层来取消显示 Dialog
                .setCancelable(true)
                .setPositiveButton(confirmButtonText, confirmListener)
                .create()
        } else {
            AlertDialog.Builder(activity)
                .setTitle(dialogTitle)
                .setMessage(permissionHint) // 这里需要设置成可取消的，这样用户不想授权了，随时可以点击返回键或者对话框蒙层来取消显示 Dialog
                .setCancelable(true)
                .setPositiveButton(confirmButtonText, confirmListener)
                .create()
        }
        dialog.show()
        // 将 Activity 和 Dialog 生命周期绑定在一起，避免可能会出现的内存泄漏
        // 当然如果上面创建的 Dialog 已经有做了生命周期管理，则不需要执行下面这行代码
        bindDialogLifecycle(activity, dialog)
    }

    /**
     * 生成权限提示文案
     */
    
    private fun generatePermissionHint( activity: Activity,  deniedPermissions: List<IPermission>, doNotAskAgain: Boolean): String {
        val deniedPermissionCount = deniedPermissions.size
        var deniedLocationPermissionCount = 0
        var deniedSensorsPermissionCount = 0
        for (deniedPermission in deniedPermissions) {
            val permissionGroup = deniedPermission?.getPermissionGroup()
            if (TextUtils.isEmpty(permissionGroup)) {
                continue
            }
            if (PermissionGroups.LOCATION == permissionGroup) {
                deniedLocationPermissionCount++
            } else if (PermissionGroups.SENSORS == permissionGroup) {
                deniedSensorsPermissionCount++
            }
        }

        if (deniedLocationPermissionCount == deniedPermissionCount) {
            if (deniedLocationPermissionCount == 1) {
                if (VERSION.SDK_INT >= VERSION_CODES.Q &&
                    XXPermissions.equalsPermission(deniedPermissions[0], PermissionNames.ACCESS_BACKGROUND_LOCATION)
                ) {
                    return activity.getString(
                        if (doNotAskAgain) R.string.permission_location_fail_hint_1 else R.string.permission_location_fail_hint_2,
                        getBackgroundPermissionOptionLabel(activity)
                    )
                } else if (VERSION.SDK_INT >= VERSION_CODES.S &&
                    XXPermissions.equalsPermission(deniedPermissions[0], PermissionNames.ACCESS_FINE_LOCATION)
                ) {
                    // 如果请求的定位权限中，既包含了精确定位权限，又包含了模糊定位权限或者后台定位权限，
                    // 但是用户只同意了模糊定位权限的情况或者后台定位权限，并没有同意精确定位权限的情况，就提示用户开启确切位置选项
                    // 需要注意的是 Android 12 才将模糊定位权限和精确定位权限的授权选项进行分拆，之前的版本没有区分得那么仔细
                    return activity.getString(if (doNotAskAgain) R.string.permission_location_fail_hint_3 else R.string.permission_location_fail_hint_4)
                }
            } else {
                if (VERSION.SDK_INT >= VERSION_CODES.Q && doNotAskAgain &&
                    XXPermissions.containsPermission(deniedPermissions, PermissionNames.ACCESS_BACKGROUND_LOCATION)
                ) {
                    return if (VERSION.SDK_INT >= VERSION_CODES.S &&
                        XXPermissions.containsPermission(deniedPermissions, PermissionNames.ACCESS_FINE_LOCATION)
                    ) {
                        activity.getString(
                            R.string.permission_location_fail_hint_5,
                            getBackgroundPermissionOptionLabel(activity)
                        )
                    } else {
                        activity.getString(
                            R.string.permission_location_fail_hint_6,
                            getBackgroundPermissionOptionLabel(activity)
                        )
                    }
                }
            }
        } else if (deniedSensorsPermissionCount == deniedPermissionCount) {
            if (deniedPermissionCount == 1) {
                if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU &&
                    XXPermissions.equalsPermission(deniedPermissions[0], PermissionNames.BODY_SENSORS_BACKGROUND)
                ) {
                    return activity.getString(
                        if (doNotAskAgain) R.string.permission_sensors_fail_hint_1 else R.string.permission_sensors_fail_hint_2,
                        getBackgroundPermissionOptionLabel(activity)
                    )
                }
            } else {
                if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU && doNotAskAgain) {
                    return activity.getString(
                        R.string.permission_sensors_fail_hint_3,
                        getBackgroundPermissionOptionLabel(activity)
                    )
                }
            }
        }

        return activity.getString(
            if (doNotAskAgain) R.string.permission_fail_assign_hint_1 else R.string.permission_fail_assign_hint_2,
            getNickNamesByPermissions(activity, deniedPermissions)
        )
    }

    /**
     * 获取后台权限的《始终允许》选项的文案
     */
    
    private fun getBackgroundPermissionOptionLabel(context: Context): String {
        val packageManager = context.packageManager
        if (packageManager != null && VERSION.SDK_INT >= VERSION_CODES.R) {
            val backgroundPermissionOptionLabel = packageManager.backgroundPermissionOptionLabel
            if (!TextUtils.isEmpty(backgroundPermissionOptionLabel)) {
                return backgroundPermissionOptionLabel.toString()
            }
        }

        return if (VERSION.SDK_INT >= VERSION_CODES.R) {
            context.getString(R.string.permission_background_default_option_label_api30)
        } else {
            context.getString(R.string.permission_background_default_option_label_api29)
        }
    }
}