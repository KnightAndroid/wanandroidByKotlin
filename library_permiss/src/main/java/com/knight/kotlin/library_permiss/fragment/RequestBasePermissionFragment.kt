package com.knight.kotlin.library_permiss.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.knight.kotlin.library_permiss.utils.PermissionUtils


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/27 14:26
 * @descript:权限申请专用的 Fragment 基类
 */
abstract class RequestBasePermissionFragment : Fragment() {
    /** 权限申请标记（防止系统杀死应用后重新触发请求的问题）  */
    private var mRequestFlag = false

    /** 权限请求是否已经发起  */
    private var mAlreadyRequest = false

    /** Activity 屏幕方向  */
    private var mScreenOrientation = 0

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val activity: Activity = getActivity() ?: return
        // 如果当前没有锁定屏幕方向就获取当前屏幕方向并进行锁定
        mScreenOrientation = activity.getRequestedOrientation()
        if (mScreenOrientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
            return
        }

        // 锁定当前 Activity 方向
        PermissionUtils.lockActivityOrientation(activity)
    }

    override fun onDetach() {
        super.onDetach()
        val activity: Activity? = getActivity()
        if (activity == null || mScreenOrientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED || activity.getRequestedOrientation() === ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
            return
        }
        // 为什么这里不用跟上面一样 try catch ？因为这里是把 Activity 方向取消固定，只有设置横屏或竖屏的时候才可能触发 crash
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
    }

    override fun onResume() {
        super.onResume()

        // 如果当前 Fragment 是通过系统重启应用触发的，则不进行权限申请
        if (!mRequestFlag) {
            detachByActivity(getActivity())
            return
        }

        // 如果在 Activity 不可见的状态下添加 Fragment 并且去申请权限会导致授权对话框显示不出来
        // 所以必须要在 Fragment 的 onResume 来申请权限，这样就可以保证应用回到前台的时候才去申请权限
        if (mAlreadyRequest) {
            return
        }

        mAlreadyRequest = true
        startPermissionRequest()
    }

    /**
     * 开启权限请求
     */
    abstract fun startPermissionRequest()

    /**
     * 设置权限申请标记
     */
    fun setRequestFlag(flag: Boolean) {
        mRequestFlag = flag
    }

    /**
     * 绑定 Activity
     */
    fun attachByActivity(activity: Activity) {
        if (activity is FragmentActivity) {
            val fragmentManager = activity.supportFragmentManager
            fragmentManager.beginTransaction()
                .add(this, this.toString())
                .commitAllowingStateLoss()
        } else {
            // 不是 FragmentActivity，无法使用 supportFragmentManager
            throw IllegalArgumentException("Activity must be a FragmentActivity")
        }
    }

    /**
     * 解绑 Activity
     */
    fun detachByActivity( activity: Activity?) {
        if (activity is FragmentActivity) {
            val fragmentManager = activity.supportFragmentManager
            fragmentManager.beginTransaction()
                .remove(this)
                .commitAllowingStateLoss()
        }
    }

    companion object {
        /** 请求的权限  */
        const val REQUEST_PERMISSIONS: String = "request_permissions"
    }
}