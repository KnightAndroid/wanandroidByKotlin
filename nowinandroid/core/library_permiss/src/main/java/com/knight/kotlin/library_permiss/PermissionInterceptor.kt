package com.knight.kotlin.library_permiss



import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.hjq.permissions.XXPermissions.containsPermission
import com.hjq.permissions.XXPermissions.equalsPermission
import com.hjq.permissions.XXPermissions.getDeniedPermissions
import com.hjq.permissions.XXPermissions.isDoNotAskAgainPermissions
import com.hjq.permissions.XXPermissions.isHealthPermission
import com.hjq.permissions.XXPermissions.startPermissionActivity
import com.knight.kotlin.library_permiss.PermissionConverter.getNickNamesByPermissions
import com.knight.kotlin.library_permiss.WindowLifecycleManager.bindDialogLifecycle
import com.knight.kotlin.library_permiss.permission.PermissionGroups
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_util.toast.ToastUtils


/**
 * @Description
 * @Author knight
 * @Time 2025/6/8 22:44
 * 
 */

class PermissionInterceptor : OnPermissionInterceptor {
    override fun onRequestPermissionEnd(
         activity: Activity, skipRequest: Boolean,
         requestList: List<IPermission>,
         grantedList: List<IPermission>,
         deniedList: List<IPermission>,
         callback: OnPermissionCallback
    ) {
        callback.onResult(grantedList, deniedList)

        if (deniedList.isEmpty()) {
            return
        }
        val doNotAskAgain = isDoNotAskAgainPermissions(activity, deniedList)
        val permissionHint = generatePermissionHint(activity, deniedList, doNotAskAgain)
        if (!doNotAskAgain) {
            // 如果没有勾选不再询问选项，就弹 Toast 提示给用户
            ToastUtils.show(permissionHint)
            return
        }

        // 如果勾选了不再询问选项，就弹 Dialog 引导用户去授权
        showPermissionSettingDialog(activity, requestList, deniedList, callback, permissionHint)
    }

    private fun showPermissionSettingDialog(
         activity: Activity,
         requestList: List<IPermission>,
         deniedList: List<IPermission>,
         callback: OnPermissionCallback,
         permissionHint: String
    ) {
        if (activity.isFinishing || activity.isDestroyed) {
            return
        }

        val dialogTitle = activity.getString(R.string.permission_alert)
        val confirmButtonText = activity.getString(R.string.permission_go_to_authorization)
        val confirmListener = DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
            dialog.dismiss()
            startPermissionActivity(activity, deniedList, object : OnPermissionCallback {
                override fun onResult( grantedList: List<IPermission>,  deniedList: List<IPermission>) {
                    val latestDeniedList = getDeniedPermissions(activity, requestList)
                    val allGranted = latestDeniedList.isEmpty()
                    if (!allGranted) {
                        // 递归显示对话框，让提示用户授权，只不过对话框是可取消的，用户不想授权了，随时可以点击返回键或者对话框蒙层来取消显示
                        showPermissionSettingDialog(
                            activity, requestList, latestDeniedList, callback,
                            generatePermissionHint(activity, latestDeniedList, true)
                        )
                        return
                    }

                    if (callback == null) {
                        return
                    }
                    // 用户全部授权了，回调成功给外层监听器，免得用户还要再发起权限申请
                    callback!!.onResult(requestList, latestDeniedList)
                }
            })
        }

        // 另外这里需要判断 Activity 的类型来申请权限，这是因为只有 AppCompatActivity 才能调用 Support 库的 AlertDialog 来显示，否则会出现报错
        // java.lang.IllegalStateException: You need to use a Theme.AppCompat theme (or descendant) with this activity
        // 为什么不直接用 App 包 AlertDialog 来显示，而是两套规则？因为 App 包 AlertDialog 是系统自带的类，不同 Android 版本展现的样式可能不太一样
        // 如果这个 Android 版本比较低，那么这个对话框的样式就会变得很丑，准确来讲也不能说丑，而是当时系统的 UI 设计就是那样，它只是跟随系统的样式而已
        val dialog: AlertDialog = if (activity is AppCompatActivity) {
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
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(com.knight.kotlin.library_common.util.CacheUtils.getThemeColor())
        // 将 Activity 和 Dialog 生命周期绑定在一起，避免可能会出现的内存泄漏
        // 当然如果上面创建的 Dialog 已经有做了生命周期管理，则不需要执行下面这行代码
        bindDialogLifecycle(activity, dialog)
    }

    /**
     * 生成权限提示文案
     */
    
    private fun generatePermissionHint( activity: Activity,  deniedList: List<IPermission>, doNotAskAgain: Boolean): String {
        val deniedPermissionCount = deniedList.size
        var deniedLocationPermissionCount = 0
        var deniedSensorsPermissionCount = 0
        var deniedHealthPermissionCount = 0
        for (deniedPermission in deniedList) {
            val permissionGroup = deniedPermission.getPermissionGroup(activity)
            if (TextUtils.isEmpty(permissionGroup)) {
                continue
            }
            if (PermissionGroups.LOCATION == permissionGroup) {
                deniedLocationPermissionCount++
            } else if (PermissionGroups.SENSORS == permissionGroup) {
                deniedSensorsPermissionCount++
            } else if (isHealthPermission(deniedPermission)) {
                deniedHealthPermissionCount++
            }
        }

        if (deniedLocationPermissionCount == deniedPermissionCount && VERSION.SDK_INT >= VERSION_CODES.Q) {
            if (deniedLocationPermissionCount == 1) {
                if (equalsPermission(deniedList[0], PermissionNames.ACCESS_BACKGROUND_LOCATION)) {
                    return activity.getString(
                        R.string.permission_fail_hint_1,
                        activity.getString(R.string.permission_location_background),
                        getBackgroundPermissionOptionLabel(activity)
                    )
                } else if (VERSION.SDK_INT >= VERSION_CODES.S &&
                    equalsPermission(deniedList[0], PermissionNames.ACCESS_FINE_LOCATION)
                ) {
                    // 如果请求的定位权限中，既包含了精确定位权限，又包含了模糊定位权限或者后台定位权限，
                    // 但是用户只同意了模糊定位权限的情况或者后台定位权限，并没有同意精确定位权限的情况，就提示用户开启确切位置选项
                    // 需要注意的是 Android 12 才将模糊定位权限和精确定位权限的授权选项进行分拆，之前的版本没有区分得那么仔细
                    return activity.getString(
                        R.string.permission_fail_hint_3,
                        activity.getString(R.string.permission_location_fine),
                        activity.getString(R.string.permission_location_fine_option)
                    )
                }
            } else {
                if (containsPermission(deniedList, PermissionNames.ACCESS_BACKGROUND_LOCATION)) {
                    return if (VERSION.SDK_INT >= VERSION_CODES.S &&
                        containsPermission(deniedList, PermissionNames.ACCESS_FINE_LOCATION)
                    ) {
                        activity.getString(
                            R.string.permission_fail_hint_2,
                            activity.getString(R.string.permission_location),
                            getBackgroundPermissionOptionLabel(activity),
                            activity.getString(R.string.permission_location_fine_option)
                        )
                    } else {
                        activity.getString(
                            R.string.permission_fail_hint_1,
                            activity.getString(R.string.permission_location),
                            getBackgroundPermissionOptionLabel(activity)
                        )
                    }
                }
            }
        } else if (deniedSensorsPermissionCount == deniedPermissionCount && VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
            if (deniedPermissionCount == 1) {
                if (equalsPermission(deniedList[0], PermissionNames.BODY_SENSORS_BACKGROUND)) {
                    return if (VERSION.SDK_INT >= VERSION_CODES.BAKLAVA) {
                        activity.getString(
                            R.string.permission_fail_hint_1,
                            activity.getString(R.string.permission_health_data_background),
                            activity.getString(R.string.permission_health_data_background_option)
                        )
                    } else {
                        activity.getString(
                            R.string.permission_fail_hint_1,
                            activity.getString(R.string.permission_body_sensors_background),
                            getBackgroundPermissionOptionLabel(activity)
                        )
                    }
                }
            } else {
                if (doNotAskAgain) {
                    return if (VERSION.SDK_INT >= VERSION_CODES.BAKLAVA) {
                        activity.getString(
                            R.string.permission_fail_hint_1,
                            activity.getString(R.string.permission_health_data),
                            activity.getString(R.string.permission_allow_all_option)
                        )
                    } else {
                        activity.getString(
                            R.string.permission_fail_hint_1,
                            activity.getString(R.string.permission_body_sensors),
                            getBackgroundPermissionOptionLabel(activity)
                        )
                    }
                }
            }
        } else if (deniedHealthPermissionCount == deniedPermissionCount && VERSION.SDK_INT >= VERSION_CODES.BAKLAVA) {
            when (deniedPermissionCount) {
                1 -> if (equalsPermission(deniedList[0], PermissionNames.READ_HEALTH_DATA_IN_BACKGROUND)) {
                    return activity.getString(
                        R.string.permission_fail_hint_3,
                        activity.getString(R.string.permission_health_data_background),
                        activity.getString(R.string.permission_health_data_background_option)
                    )
                } else if (equalsPermission(deniedList[0], PermissionNames.READ_HEALTH_DATA_HISTORY)) {
                    return activity.getString(
                        R.string.permission_fail_hint_3,
                        activity.getString(R.string.permission_health_data_past),
                        activity.getString(R.string.permission_health_data_past_option)
                    )
                }

                2 -> if (containsPermission(deniedList, PermissionNames.READ_HEALTH_DATA_HISTORY) &&
                    containsPermission(deniedList, PermissionNames.READ_HEALTH_DATA_IN_BACKGROUND)
                ) {
                    return activity.getString(
                        R.string.permission_fail_hint_3,
                        activity.getString(R.string.permission_health_data_past) + activity.getString(R.string.permission_and) + activity.getString(
                            R.string.permission_health_data_background
                        ),
                        activity.getString(R.string.permission_health_data_past_option) + activity.getString(R.string.permission_and) + activity.getString(
                            R.string.permission_health_data_background_option
                        )
                    )
                } else if (containsPermission(deniedList, PermissionNames.READ_HEALTH_DATA_HISTORY)) {
                    return activity.getString(
                        R.string.permission_fail_hint_2,
                        activity.getString(R.string.permission_health_data) + activity.getString(R.string.permission_and) + activity.getString(R.string.permission_health_data_past),
                        activity.getString(R.string.permission_allow_all_option),
                        activity.getString(R.string.permission_health_data_background_option)
                    )
                } else if (containsPermission(deniedList, PermissionNames.READ_HEALTH_DATA_IN_BACKGROUND)) {
                    return activity.getString(
                        R.string.permission_fail_hint_2,
                        activity.getString(R.string.permission_health_data) + activity.getString(R.string.permission_and) + activity.getString(R.string.permission_health_data_background),
                        activity.getString(R.string.permission_allow_all_option),
                        activity.getString(R.string.permission_health_data_background_option)
                    )
                }

                else -> if (containsPermission(deniedList, PermissionNames.READ_HEALTH_DATA_HISTORY) &&
                    containsPermission(deniedList, PermissionNames.READ_HEALTH_DATA_IN_BACKGROUND)
                ) {
                    return activity.getString(
                        R.string.permission_fail_hint_2,
                        activity.getString(R.string.permission_health_data) + activity.getString(R.string.permission_and) + activity.getString(R.string.permission_health_data_past) + activity.getString(
                            R.string.permission_and
                        ) + activity.getString(R.string.permission_health_data_background),
                        activity.getString(R.string.permission_allow_all_option),
                        activity.getString(R.string.permission_health_data_past_option) + activity.getString(R.string.permission_and) + activity.getString(
                            R.string.permission_health_data_background_option
                        )
                    )
                }
            }
            return activity.getString(
                R.string.permission_fail_hint_1,
                activity.getString(R.string.permission_health_data),
                activity.getString(R.string.permission_allow_all_option)
            )
        }

        return activity.getString(
            if (doNotAskAgain) R.string.permission_fail_assign_hint_1 else R.string.permission_fail_assign_hint_2,
            getNickNamesByPermissions(activity, deniedList)
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

        return context.getString(R.string.permission_allow_all_the_time_option)
    }
}