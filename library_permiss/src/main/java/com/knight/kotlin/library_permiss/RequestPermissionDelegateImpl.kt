package com.knight.kotlin.library_permiss

import android.app.Activity
import com.knight.kotlin.library_permiss.PermissionActivityIntentHandler.IStartActivityDelegate
import com.knight.kotlin.library_permiss.PermissionTaskHandler.cancelTask
import com.knight.kotlin.library_permiss.PermissionTaskHandler.sendTask
import com.knight.kotlin.library_permiss.listener.IFragmentCallback
import com.knight.kotlin.library_permiss.listener.IFragmentMethod
import com.knight.kotlin.library_permiss.listener.OnPermissionFlowCallback
import com.knight.kotlin.library_permiss.utils.PermissionUtils


/**
 * @Description
 * @Author knight
 * @Time 2025/6/8 20:09
 *
 */

abstract class RequestPermissionDelegateImpl(fragmentMethod: IFragmentMethod<*, *>) :
    IFragmentCallback {
    /** 任务令牌  */
    
    private val mTaskToken = Any()

    /** 权限申请标记（防止系统杀死应用后重新触发请求的问题）  */
    private var mRequestFlag = false

    /** 权限请求是否已经发起  */
    private var mAlreadyRequest = false

    /** 当前 Fragment 是否为手动解绑  */
    private var mManualDetach = false

    /** Fragment 方法对象  */
    
    private val mFragmentMethod = fragmentMethod

    /** 权限回调对象  */
    
    private var mCallBack: OnPermissionFlowCallback? = null

    fun setRequestFlag(flag: Boolean) {
        mRequestFlag = flag
    }

    fun setCallback( callback: OnPermissionFlowCallback?) {
        mCallBack = callback
    }

    
    fun getCallBack(): OnPermissionFlowCallback? {
        return mCallBack
    }

    
    fun getActivity(): Activity {
        return mFragmentMethod.getActivity()!!
    }

    fun commitDetach() {
        mManualDetach = true
        mFragmentMethod.commitDetach()
    }

    fun isFragmentUnavailable(): Boolean {
        // 如果用户离开太久，会导致 Activity 被回收掉
        // 所以这里要判断当前 Fragment 是否有被添加到 Activity
        // 可在开发者模式中开启不保留活动来复现这个 Bug
        return !mFragmentMethod.isAdded() || mFragmentMethod.isRemoving()
    }

    fun requestPermissions( permissions: Array<String>, requestCode: Int) {
        mFragmentMethod.requestPermissions(permissions, requestCode)
    }

    
    fun getPermissionRequestList(): List<String>? {
        val arguments = mFragmentMethod.getArguments() ?: return null
        return arguments.getStringArrayList(REQUEST_PERMISSIONS)
    }

    fun getPermissionRequestCode(): Int {
        val arguments = mFragmentMethod.getArguments() ?: return 0
        return arguments.getInt(REQUEST_CODE)
    }

    fun sendTask( runnable: Runnable?, delayMillis: Long) {
        sendTask(runnable!!, mTaskToken, delayMillis)
    }

    fun cancelTask() {
        cancelTask(mTaskToken)
    }

    fun getStartActivityDelegate(): IStartActivityDelegate {
        return mFragmentMethod
    }

    /**
     * 开启权限请求
     */
    abstract fun startPermissionRequest(
         activity: Activity,
         permissions: List<String>,
        requestCode: Int
    )

    override fun onFragmentResume() {
        // 如果当前 Fragment 是通过系统重启应用触发的，则不进行权限申请
        if (!mRequestFlag) {
            mFragmentMethod.commitDetach()
            return
        }

        // 如果在 Activity 不可见的状态下添加 Fragment 并且去申请权限会导致授权对话框显示不出来
        // 所以必须要在 Fragment 的 onResume 来申请权限，这样就可以保证应用回到前台的时候才去申请权限
        if (mAlreadyRequest) {
            return
        }

        mAlreadyRequest = true
        val activity: Activity = getActivity()
        // 检查 Activity 是不是不可用
        if (PermissionUtils.isActivityUnavailable(activity)) {
            return
        }
        val requestCode = getPermissionRequestCode()
        if (requestCode <= 0) {
            return
        }
        val permissions = getPermissionRequestList()
        if (permissions == null || permissions.isEmpty()) {
            return
        }
        startPermissionRequest(activity, permissions, requestCode)
        // 锁定 Activity 屏幕方向
        ActivityOrientationControl.lockActivityOrientation(activity)
        val callback = getCallBack() ?: return
        callback.onRequestPermissionNow()
    }

    override fun onFragmentDestroy() {
        // 取消执行任务
        cancelTask()
        val callBack = getCallBack()
        // 如果回调还没有置空，则证明前面没有回调权限回调完成
        if (callBack != null) {
            // 告诉外层本次权限回调有异常
            callBack.onRequestPermissionAnomaly()
            // 释放回调对象，避免内存泄漏
            setCallback(null)
        }
        if (mManualDetach) {
            return
        }
        val activity: Activity = getActivity()
        // 检查 Activity 是不是不可用
        if (PermissionUtils.isActivityUnavailable(activity)) {
            return
        }
        // 如果不是手动解绑绑定，则证明是系统解除绑定，这里需要恢复 Activity 屏幕方向
        // 如果是手动解除绑定，则会在所有的权限都申请完了之后恢复 Activity 屏幕方向
        ActivityOrientationControl.unlockActivityOrientation(activity)
    }

    companion object {
        /** 请求的权限  */
        const val REQUEST_PERMISSIONS: String = "request_permissions"

        /** 请求码（自动生成） */
        const val REQUEST_CODE: String = "request_code"
    }
}