package com.knight.kotlin.library_permiss.core

import android.app.Activity
import android.content.Intent
import androidx.annotation.IntRange
import com.knight.kotlin.library_permiss.fragment.IFragmentMethod
import com.knight.kotlin.library_permiss.manager.PermissionRequestCodeManager.releaseRequestCode
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.start.StartActivityAgent
import com.knight.kotlin.library_permiss.tools.PermissionApi.getBestPermissionSettingIntent

/**
 * @Description
 * @Author knight
 * @Time 2025/6/8 20:17
 *
 */
class RequestPermissionDelegateImplBySpecial( fragmentMethod: IFragmentMethod<*, *>) :
    RequestPermissionDelegateImpl(fragmentMethod) {
    /** 忽略 onActivityResult 回调的总次数  */
    private var mIgnoreActivityResultCount = 0

    override fun startPermissionRequest(
        activity: Activity, permissions: List<IPermission>?,
        @IntRange(from = 1, to = 65535) requestCode: Int
    ) {
        getBestPermissionSettingIntent(activity, permissions)?.let {
            StartActivityAgent.startActivityForResult(
                activity, getStartActivityDelegate(),
                it.toMutableList(),
                requestCode
            ) { mIgnoreActivityResultCount++ }
        }
    }

    override fun onFragmentActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        // 如果回调中的请求码和请求时设置的请求码不一致，则证明回调有问题，则不往下执行代码
        if (requestCode != getPermissionRequestCode()) {
            return
        }
        // 如果调用 startActivityForResult 出现跳转失败，框架会自动捕获跳转失败导致的 Exception，
        // 这样做是为了以防应用程序出现崩溃，并且会自动拿下一个 Intent 进行重试，直到找到能跳转的 Intent 为止，
        // 但是这样会出现一个问题，startActivityForResult 每次跳转失败就会导致系统触发一次 onActivityResult 回调，
        // 这样就可能会出现触发多次 onActivityResult 回调的情况，从而导致权限实际还没有申请完，但是已经通知回调的尴尬局面，
        // 在这种情况下只判断 requestCode 是否一样已经没有办法避免这个问题了，经过多轮思考，能想到一个比较好的解决方案，
        // 就是要记录 startActivityForResult 跳转失败的次数，然后在 onActivityResult 回调中按次减掉，
        // 也就是把那些 startActivityForResult 失败导致的回调给过滤掉，只有当这个次数减成 0 的时候，才能去回调权限请求的结果。
        if (mIgnoreActivityResultCount > 0) {
            mIgnoreActivityResultCount--
            return
        }
        // 释放对这个请求码的占用
        releaseRequestCode(requestCode)
        // 通知权限请求回调
        notificationPermissionCallback()
    }
}