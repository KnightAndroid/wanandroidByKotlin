package com.knight.kotlin.library_permiss

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_permiss.listener.OnPermissionCallback
import com.knight.kotlin.library_permiss.listener.OnPermissionInterceptor
import com.knight.kotlin.library_permiss.permissions.Permission


/**
 * Author:Knight
 * Time:2022/1/21 11:45
 * Description:PermissionInterceptor
 */
class PermissionInterceptor: OnPermissionInterceptor {

    val HANDLER: Handler = Handler(Looper.getMainLooper())

    /** 权限申请标记  */
    private var mRequestFlag = false

    /** 权限申请说明 Popup  */
    private var mPermissionPopup: PopupWindow? = null

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun launchPermissionRequest(
         activity: FragmentActivity,
         allPermissions: List<String>,
         callback: OnPermissionCallback
    ) {
        mRequestFlag = true
        val deniedPermissions: List<String> = XXPermissions.getDenied(activity, allPermissions)
        val message = activity.getString(
            R.string.permission_message,
            PermissionNameConvert.getPermissionString(activity, deniedPermissions)
        )
        val decorView = activity.window.decorView as ViewGroup
        val activityOrientation = activity.resources.configuration.orientation
        var showPopupWindow = activityOrientation == Configuration.ORIENTATION_PORTRAIT
        for (permission in allPermissions) {
            if (!XXPermissions.isSpecial(permission)) {
                continue
            }
            if (XXPermissions.isGranted(activity, permission)) {
                continue
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R &&
                TextUtils.equals(Permission.MANAGE_EXTERNAL_STORAGE, permission)
            ) {
                continue
            }
            // 如果申请的权限带有特殊权限，并且还没有授予的话
            // 就不用 PopupWindow 对话框来显示，而是用 Dialog 来显示
            showPopupWindow = false
            break
        }
        if (showPopupWindow) {
            PermissionHandler.request(activity, allPermissions, callback, this)
            // 延迟 300 毫秒是为了避免出现 PopupWindow 显示然后立马消失的情况
            // 因为框架没有办法在还没有申请权限的情况下，去判断权限是否永久拒绝了，必须要在发起权限申请之后
            // 所以只能通过延迟显示 PopupWindow 来做这件事，如果 300 毫秒内权限申请没有结束，证明本次申请的权限没有永久拒绝
            HANDLER.postDelayed({
                if (!mRequestFlag) {
                    return@postDelayed
                }
                if (activity.isFinishing || Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed
                ) {
                    return@postDelayed
                }
                showPopupWindow(activity, decorView, message)
            }, 300)
        } else {
            // 注意：这里的 Dialog 只是示例，没有用 DialogFragment 来处理 Dialog 生命周期
            val dialog = AlertDialog.Builder(activity)
                .setTitle(R.string.permission_description)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.permission_granted) { dialog, which ->
                    dialog.dismiss()
                    PermissionHandler.request(activity, allPermissions, callback, this)
                }
                .setNegativeButton(R.string.permission_denied) { dialog, which ->
                    dialog.dismiss()
                    if (callback == null) {
                        return@setNegativeButton
                    }
                    callback.onDenied(deniedPermissions, false)
                }
                .show()
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(CacheUtils.getThemeColor())
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(com.knight.kotlin.library_base.R.color.base_cancel)
        }
    }

//    fun grantedPermissionRequest(
//        activity: Activity?,  allPermissions: List<String>,
//        grantedPermissions: List<String>, allGranted: Boolean,
//         callback: OnPermissionCallback
//    ) {
//        if (callback == null) {
//            return
//        }
//        callback.onGranted(grantedPermissions, allGranted)
//    }
//
//    fun deniedPermissionRequest(
//         activity: Activity, allPermissions: List<String>,
//         deniedPermissions: List<String>, doNotAskAgain: Boolean,
//        callback: OnPermissionCallback
//    ) {
//        callback?.onDenied(deniedPermissions, doNotAskAgain)
//        if (doNotAskAgain) {
//            if (deniedPermissions.size == 1 && Permission.ACCESS_MEDIA_LOCATION.equals(
//                    deniedPermissions[0]
//                )
//            ) {
//                ToastUtils.show(R.string.permission_media_location_hint_fail)
//                return
//            }
//            showPermissionSettingDialog(activity, allPermissions, deniedPermissions, callback)
//            return
//        }
//        if (deniedPermissions.size == 1) {
//            val deniedPermission = deniedPermissions[0]
//            val backgroundPermissionOptionLabel = getBackgroundPermissionOptionLabel(activity)
//            if (Permission.ACCESS_BACKGROUND_LOCATION.equals(deniedPermission)) {
//                ToastUtils.show(
//                    activity.getString(
//                        R.string.permission_background_location_fail_hint,
//                        backgroundPermissionOptionLabel
//                    )
//                )
//                return
//            }
//            if (Permission.BODY_SENSORS_BACKGROUND.equals(deniedPermission)) {
//                ToastUtils.show(
//                    activity.getString(
//                        R.string.permission_background_sensors_fail_hint,
//                        backgroundPermissionOptionLabel
//                    )
//                )
//                return
//            }
//        }
//        val message: String
//        val permissionNames: List<String> =
//            PermissionNameConvert.permissionsToNames(activity, deniedPermissions)
//        message = if (!permissionNames.isEmpty()) {
//            activity.getString(
//                R.string.permission_fail_assign_hint,
//                PermissionNameConvert.listToString(activity, permissionNames)
//            )
//        } else {
//            activity.getString(R.string.permission_fail_hint)
//        }
//        ToastUtils.show(message)
//    }

    override fun finishPermissionRequest(
        activity: Activity,  allPermissions: List<String>,
        skipRequest: Boolean,  callback: OnPermissionCallback
    ) {
        mRequestFlag = false
        dismissPopupWindow()
    }

    private fun showPopupWindow(activity: Activity, decorView: ViewGroup, message: String) {
        if (mPermissionPopup == null) {
            val contentView: View = LayoutInflater.from(activity)
                .inflate(R.layout.permission_description_popup, decorView, false)
            mPermissionPopup = PopupWindow(activity)
            mPermissionPopup?.let{
                it.setContentView(contentView)
                it.setWidth(WindowManager.LayoutParams.MATCH_PARENT)
                it.setHeight(WindowManager.LayoutParams.WRAP_CONTENT)
                it.setAnimationStyle(androidx.appcompat.R.style.Animation_AppCompat_Dialog)
                it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                it.setTouchable(true)
                it.setOutsideTouchable(true)
            }

        }
        val messageView = mPermissionPopup!!.getContentView()
            .findViewById<TextView>(R.id.tv_permission_description_message)
        messageView.text = message
        // 注意：这里的 PopupWindow 只是示例，没有监听 Activity onDestroy 来处理 PopupWindow 生命周期
        mPermissionPopup!!.showAtLocation(decorView, Gravity.TOP, 0, 0)
    }

    private fun dismissPopupWindow() {
        if (mPermissionPopup == null) {
            return
        }
        if (!mPermissionPopup!!.isShowing()) {
            return
        }
        mPermissionPopup!!.dismiss()
    }





}