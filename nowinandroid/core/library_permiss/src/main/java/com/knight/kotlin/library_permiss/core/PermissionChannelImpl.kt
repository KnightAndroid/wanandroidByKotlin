package com.knight.kotlin.library_permiss.core

import android.app.Activity
import androidx.annotation.IntRange
import com.knight.kotlin.library_permiss.fragment.IFragmentCallback
import com.knight.kotlin.library_permiss.fragment.IFragmentMethod
import com.knight.kotlin.library_permiss.manager.ActivityOrientationManager.unlockActivityOrientation
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.start.IStartActivityDelegate
import com.knight.kotlin.library_permiss.tools.PermissionApi.getMaxWaitTimeByPermissions
import com.knight.kotlin.library_permiss.tools.PermissionTaskHandler
import com.knight.kotlin.library_permiss.tools.PermissionUtils
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid13


/**
 * @Description 请求权限实现类
 * @Author knight
 * @Time 2025/6/8 20:09
 *
 */

abstract class PermissionChannelImpl protected constructor(fragmentMethod: IFragmentMethod<*, *>) : IFragmentCallback {
    /** 任务令牌  */
    private val mTaskToken = Any()

    /** 非系统重启标记  */
    private var mNonSystemRestartMark = false

    /** 权限请求是否已经发起  */
    private var mAlreadyRequest = false

    /** 当前 Fragment 是否为手动解绑  */
    private var mManualDetach = false

    /** Fragment 方法对象  */
    
    private val mFragmentMethod = fragmentMethod

    /** 权限回调对象  */
    
    private var mPermissionFragmentCallback: OnPermissionFragmentCallback? = null

    fun setNonSystemRestartMark(nonSystemRestartMark: Boolean) {
        mNonSystemRestartMark = nonSystemRestartMark
    }

    fun setPermissionFragmentCallback( callback: OnPermissionFragmentCallback?) {
        mPermissionFragmentCallback = callback
    }

    
    private fun getPermissionFragmentCallback(): OnPermissionFragmentCallback? {
        return mPermissionFragmentCallback
    }

    
    private fun getActivity(): Activity? {
        return mFragmentMethod.getActivity()
    }

    private fun commitFragmentDetach() {
        mManualDetach = true
        mFragmentMethod.commitFragmentDetach()
    }

    private fun isFragmentUnavailable(): Boolean {
        // 如果用户离开太久，会导致 Activity 被回收掉
        // 所以这里要判断当前 Fragment 是否有被添加到 Activity
        // 可在开发者模式中开启不保留活动来复现这个 Bug
        return !mFragmentMethod.isAdded() || mFragmentMethod.isRemoving()
    }

    protected fun requestPermissions(permissions: Array<String>, @IntRange(from = 1, to = 65535) requestCode: Int) {
        try {
            mFragmentMethod.requestPermissions(permissions, requestCode)
        } catch (e: Exception) {
            // 在某些极端情况下，调用系统的 requestPermissions 方法时会出现崩溃，刚开始我还以为是 Android 6.0 以下的设备触发的 Bug，
            // 结果发现 Android 6.0 及以上也有这个问题，你永远无法想象现实到底有多魔幻，经过分析得出结论，出现这种情况有以下几种可能：
            //   1. 厂商开发工程师修改了 com.android.packageinstaller 系统应用的包名，但是没有自测好就上线了（概率较小）
            //   2. 厂商开发工程师删除了 com.android.packageinstaller 这个系统应用，但是没有自测好就上线了（概率较小）
            //   3. 厂商开发工程师在修改 Android 系统源码的时候，改动的代码影响到权限模块，但是没有自测好就上线了（概率较小）
            //   4. 厂商主动阉割掉了权限申请功能，例如在电视 TV 设备上面，间接导致请求危险权限的 App 一请求权限就闪退（概率较小）
            //   5. 用户有 Root 权限，在精简系统 App 的时候不小心删掉了 com.android.packageinstaller 这个系统应用（概率较大）
            // 经过分析 Activity.requestPermissions 的源码，它本质上还是调用 startActivityForResult，只不过 Activity 找不到了而已，
            // 目前能想到最好的解决方式，就是用 try catch 避免它出现崩溃，看到这里你可能会有一个疑问，就简单粗暴 try catch？你确定没问题？
            // 会不会导致 onRequestPermissionsResult 没有回调？从而导致权限请求流程卡住的情况？虽然这个问题没有办法测试，但理论上是不会的，
            // 因为我用了错误的 Intent 进行 startActivityForResult 并进行 try catch 做实验，结果 onActivityResult 还是有被系统正常回调，
            // 证明对 startActivityForResult 进行 try catch 并不会影响 onActivityResult 的回调，我还分析了 Activity 回调方面的源码实现，
            // 发现无论是 onRequestPermissionsResult 还是 onActivityResult，回调它们的都是 dispatchActivityResult 方法，
            // 在那种极端情况下，既然 onActivityResult 能被回调，那么就证明 dispatchActivityResult 肯定有被系统正常调用的，
            // 同理 onRequestPermissionsResult 也肯定会被 dispatchActivityResult 正常调用，从而形成一个完整的逻辑闭环。
            // 补充测试结论：我在 debug 了 Activity.requestPermissions 方法，偷偷修改权限请求 Intent 的 Action 成错误的，结果权限回调能正常回调。
            // 如果真的出现这种极端情况，所有危险权限的申请必然会走失败的回调，但是框架要做的是：尽量让应用不要崩溃，并且能走完整个权限申请的流程。
            // 涉及到此问题相关 Github issue 地址：
            //   1. https://github.com/getActivity/XXPermissions/issues/153
            //   2. https://github.com/getActivity/XXPermissions/issues/126
            //   3. https://github.com/getActivity/XXPermissions/issues/327
            //   4. https://github.com/getActivity/XXPermissions/issues/339
            //   5. https://github.com/guolindev/PermissionX/issues/92
            //   6. https://github.com/yanzhenjie/AndPermission/issues/72
            //   7. https://github.com/yanzhenjie/AndPermission/issues/28
            //   8. https://github.com/permissions-dispatcher/PermissionsDispatcher/issues/288
            //   9. https://github.com/googlesamples/easypermissions/issues/342
            //   10. https://github.com/HuanTanSheng/EasyPhotos/issues/256
            //   11. https://github.com/oasisfeng/island/issues/67
            //   12. https://github.com/Rakashazi/emu-ex-plus-alpha/issues/137
            //   13. https://github.com/hyb1996-guest/AutoJsIssueReport/issues/1792
            //   14. https://github.com/hyb1996-guest/AutoJsIssueReport/issues/1794
            //   15. https://github.com/hyb1996-guest/AutoJsIssueReport/issues/1795
            //   16. https://github.com/hyb1996-guest/AutoJsIssueReport/issues/2012
            //   17. https://github.com/hyb1996-guest/AutoJsIssueReport/issues/18264
            // android.content.ActivityNotFoundException: No Activity found to handle Intent
            // { act=android.content.pm.action.REQUEST_PERMISSIONS pkg=com.android.packageinstaller (has extras) }
            e.printStackTrace()
        }
    }

    @Suppress("deprecation")
    
    protected fun getPermissionRequestList(): List<IPermission>? {
        val arguments = mFragmentMethod.getArguments() ?: return null
        return if (isAndroid13()) {
            arguments.getParcelableArrayList(REQUEST_PERMISSIONS, IPermission::class.java)
        } else {
            arguments.getParcelableArrayList(REQUEST_PERMISSIONS)
        }
    }

    protected fun getPermissionRequestCode(): Int {
        val arguments = mFragmentMethod.getArguments() ?: return 0
        return arguments.getInt(REQUEST_CODE)
    }

    protected fun sendTask( runnable: Runnable?, delayMillis: Long) {
        PermissionTaskHandler.sendTask(runnable!!, mTaskToken, delayMillis)
    }

    protected fun cancelTask() {
        PermissionTaskHandler.cancelTask(mTaskToken)
    }

    protected fun getStartActivityDelegate(): IStartActivityDelegate {
        return mFragmentMethod
    }

    /**
     * 开启权限请求
     */
    protected abstract fun startPermissionRequest(
         activity: Activity,  permissions: List<IPermission>,
        @IntRange(from = 1, to = 65535) requestCode: Int
    )

    override fun onFragmentResume() {
        // 如果当前 Fragment 是通过系统重启应用触发的，则不进行权限申请
        // 防止系统杀死应用后重新触发请求权限的问题
        if (!mNonSystemRestartMark) {
            mFragmentMethod.commitFragmentDetach()
            return
        }

        // 如果在 Activity 不可见的状态下添加 Fragment 并且去申请权限会导致授权对话框显示不出来
        // 所以必须要在 Fragment 的 onResume 来申请权限，这样就可以保证应用回到前台的时候才去申请权限
        if (mAlreadyRequest) {
            return
        }

        mAlreadyRequest = true
        val activity = getActivity()
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
        if (activity != null) {
            startPermissionRequest(activity, permissions, requestCode)
        }
        val callback = getPermissionFragmentCallback() ?: return
        callback.onRequestPermissionNow()
    }

    override fun onFragmentDestroy() {
        // 取消执行任务
        cancelTask()
        val callback = getPermissionFragmentCallback()
        // 如果回调还没有置空，则证明前面没有回调权限回调完成
        if (callback != null) {
            // 告诉外层本次权限回调有异常
            callback.onRequestPermissionAnomaly()
            // 释放回调对象，避免内存泄漏
            setPermissionFragmentCallback(null)
        }
        if (mManualDetach) {
            return
        }
        val activity = getActivity()
        // 检查 Activity 是不是不可用
        if (PermissionUtils.isActivityUnavailable(activity)) {
            return
        }
        // 如果不是手动解绑绑定，则证明是系统解除绑定，这里需要恢复 Activity 屏幕方向
        // 如果是手动解除绑定，则会在所有的权限都申请完了之后恢复 Activity 屏幕方向
        unlockActivityOrientation(activity!!)
    }

    /**
     * 通知权限回调
     */
    protected fun notificationPermissionCallback() {
        val activity = getActivity()
        if (PermissionUtils.isActivityUnavailable(activity)) {
            return
        }
        // 延迟处理权限请求的结果
        sendTask({ this.handlerPermissionCallback() }, getMaxWaitTimeByPermissions(activity!!, getPermissionRequestList()).toLong())
    }

    /**
     * 处理权限回调
     */
    protected fun handlerPermissionCallback() {
        if (isFragmentUnavailable()) {
            return
        }

        val activity = getActivity()
        if (PermissionUtils.isActivityUnavailable(activity)) {
            return
        }

        val callback = getPermissionFragmentCallback()
        // 释放监听对象的引用
        setPermissionFragmentCallback(null)

        callback?.onRequestPermissionFinish()

        // 将 Fragment 移除
        commitFragmentDetach()
    }

    companion object {
        /** 请求的权限  */
        const val REQUEST_PERMISSIONS: String = "request_permissions"

        /** 请求码（自动生成） */
        const val REQUEST_CODE: String = "request_code"
    }
}