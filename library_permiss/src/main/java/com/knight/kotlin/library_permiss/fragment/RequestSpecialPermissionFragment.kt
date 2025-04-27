package com.knight.kotlin.library_permiss.fragment

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.PermissionActivityIntentHandler
import com.knight.kotlin.library_permiss.XXPermissions
import com.knight.kotlin.library_permiss.listener.OnPermissionPageCallback
import com.knight.kotlin.library_permiss.permissions.PermissionApi
import com.knight.kotlin.library_permiss.permissions.PermissionApi.getSmartPermissionIntent
import com.knight.kotlin.library_permiss.utils.PermissionUtils


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/27 14:40
 * @descript:
 */
class RequestSpecialPermissionFragment : RequestBasePermissionFragment(), Runnable {
    /** 权限回调对象  */

    private var mCallBack: OnPermissionPageCallback? = null

    /**
     * 设置权限监听回调监听
     */
    fun setOnPermissionPageCallback(callback: OnPermissionPageCallback?) {
        mCallBack = callback
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun startPermissionRequest() {
        val arguments = arguments
        val activity: Activity? = activity
        if (arguments == null || activity == null) {
            return
        }
        val permissions: List<String>? = arguments.getStringArrayList(REQUEST_PERMISSIONS)
        if (permissions == null || permissions.isEmpty()) {
            return
        }
        PermissionActivityIntentHandler.startActivityForResult(
            this,
            getSmartPermissionIntent(activity, permissions)!!, XXPermissions.REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int,  data: Intent?) {
        if (requestCode != XXPermissions.REQUEST_CODE) {
            return
        }

        val activity: Activity? = activity
        val arguments = arguments
        if (activity == null || arguments == null) {
            return
        }
        val allPermissions: List<String>? = arguments.getStringArrayList(REQUEST_PERMISSIONS)
        if (allPermissions == null || allPermissions.isEmpty()) {
            return
        }

        PermissionUtils.postActivityResult(allPermissions, this)
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun run() {
        // 如果用户离开太久，会导致 Activity 被回收掉
        // 所以这里要判断当前 Fragment 是否有被添加到 Activity
        // 可在开发者模式中开启不保留活动来复现这个 Bug
        if (!isAdded) {
            return
        }

        val activity = activity ?: return

        val callback = mCallBack
        mCallBack = null

        if (callback == null) {
            detachByActivity(activity)
            return
        }

        val arguments = arguments

        val allPermissions: List<String>? = arguments!!.getStringArrayList(REQUEST_PERMISSIONS)
        if (allPermissions == null || allPermissions.isEmpty()) {
            return
        }

        val grantedPermissions = PermissionApi.getGrantedPermissions(activity, allPermissions)
        if (grantedPermissions.size == allPermissions.size) {
            callback.onGranted()
        } else {
            callback.onDenied()
        }

        detachByActivity(activity)
    }

    companion object {
        /**
         * 开启权限申请
         */
        fun launch(
             activity: Activity,  permissions: List<String?>,
             callback: OnPermissionPageCallback
        ) {
            val fragment = RequestSpecialPermissionFragment()
            val bundle = Bundle()
            if (permissions is ArrayList<*>) {
                bundle.putStringArrayList(REQUEST_PERMISSIONS, permissions as ArrayList<String?>)
            } else {
                bundle.putStringArrayList(REQUEST_PERMISSIONS, ArrayList(permissions))
            }
            fragment.arguments = bundle
            // 设置保留实例，不会因为屏幕方向或配置变化而重新创建
            fragment.retainInstance = true
            // 设置权限申请标记
            fragment.setRequestFlag(true)
            // 设置权限回调监听
            fragment.setOnPermissionPageCallback(callback)
            // 绑定到 Activity 上面
            fragment.attachByActivity(activity)
        }
    }
}