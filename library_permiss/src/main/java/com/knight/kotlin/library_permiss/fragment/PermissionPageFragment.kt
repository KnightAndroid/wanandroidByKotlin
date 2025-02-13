package com.knight.kotlin.library_permiss.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.knight.kotlin.library_permiss.StartActivityManager
import com.knight.kotlin.library_permiss.XXPermissions
import com.knight.kotlin.library_permiss.listener.OnPermissionPageCallback
import com.knight.kotlin.library_permiss.permissions.PermissionApi.getGrantedPermissions
import com.knight.kotlin.library_permiss.permissions.PermissionApi.getSmartPermissionIntent
import com.knight.kotlin.library_permiss.utils.PermissionUtils.postActivityResult


/**
 * Author:Knight
 * Time:2023/8/30 16:55
 * Description:PermissionPageFragment
 */
class PermissionPageFragment : Fragment(),Runnable {


    companion object {
        /** 请求的权限组  */
        private const val REQUEST_PERMISSIONS = "request_permissions"

        /**
         * 开启权限申请
         */
        fun beginRequest(
            activity: FragmentActivity, permissions: ArrayList<String>,
            callback: OnPermissionPageCallback
        ) {
            val fragment = PermissionPageFragment()
            val bundle = Bundle()
            bundle.putStringArrayList(REQUEST_PERMISSIONS, permissions)
            fragment.setArguments(bundle)
            // 设置保留实例，不会因为屏幕方向或配置变化而重新创建
            fragment.setRetainInstance(true)
            // 设置权限申请标记
            fragment.setRequestFlag(true)
            // 设置权限回调监听
            fragment.setOnPermissionPageCallback(callback)
            // 绑定到 Activity 上面
            fragment.attachByActivity(activity)
        }

    }

    /** 权限回调对象  */
    private var mCallBack: OnPermissionPageCallback? = null

    /** 权限申请标记  */
    private var mRequestFlag = false

    /** 是否申请了权限  */
    private var mStartActivityFlag = false

    /**
     * 绑定 Activity
     */
    fun attachByActivity(activity: FragmentActivity) {
        activity.supportFragmentManager.beginTransaction().add(this, this.toString())
            .commitAllowingStateLoss()
    }

    /**
     * 解绑 Activity
     */
    fun detachByActivity(activity: FragmentActivity) {
        activity.supportFragmentManager.beginTransaction().remove(this).commitAllowingStateLoss()
    }

    /**
     * 设置权限监听回调监听
     */
    fun setOnPermissionPageCallback(callback: OnPermissionPageCallback) {
        mCallBack = callback
    }

    /**
     * 权限申请标记（防止系统杀死应用后重新触发请求的问题）
     */
    fun setRequestFlag(flag: Boolean) {
        mRequestFlag = flag
    }

    override fun onResume() {
        super.onResume()

        // 如果当前 Fragment 是通过系统重启应用触发的，则不进行权限申请
        if (!mRequestFlag) {
            activity?.let { detachByActivity(it) }
            return
        }
        if (mStartActivityFlag) {
            return
        }
        mStartActivityFlag = true
        val arguments = arguments
        val activity = activity
        if (arguments == null || activity == null) {
            return
        }
        val permissions: List<String> = arguments.getStringArrayList(REQUEST_PERMISSIONS) ?: mutableListOf()
        getActivity()?.let {
            getSmartPermissionIntent(it, permissions)?.let {
                StartActivityManager.startActivityForResult(
                    this,
                    it,
                    XXPermissions.REQUEST_CODE
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int,  data: Intent?) {
        if (requestCode != XXPermissions.REQUEST_CODE) {
            return
        }
        val activity = activity
        val arguments = arguments
        if (activity == null || arguments == null) {
            return
        }
        val allPermissions = arguments.getStringArrayList(REQUEST_PERMISSIONS)
        if (allPermissions == null || allPermissions.isEmpty()) {
            return
        }
        postActivityResult(allPermissions, this)
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun run() {
        // 如果用户离开太久，会导致 Activity 被回收掉
        // 所以这里要判断当前 Fragment 是否有被添加到 Activity
        // 可在开发者模式中开启不保留活动来复现这个 Bug
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
        val allPermissions: List<String>? = arguments?.getStringArrayList(REQUEST_PERMISSIONS)

        val grantedPermissions = allPermissions?.let {
            getGrantedPermissions(
                activity,
                it
            )
        }
        if (grantedPermissions?.size ?: 0 == allPermissions?.size ?: 0) {
            callback.onGranted()
        } else {
            callback.onDenied()
        }

        detachByActivity(activity)
    }
}