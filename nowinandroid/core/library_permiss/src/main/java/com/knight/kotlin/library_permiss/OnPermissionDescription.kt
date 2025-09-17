package com.knight.kotlin.library_permiss

import android.app.Activity
import com.knight.kotlin.library_permiss.permission.base.IPermission


/**
 * @Description 权限说明接口
 * @Author knight
 * @Time 2025/6/8 17:06
 *
 */

interface OnPermissionDescription {
    /**
     * 询问是否要发起权限请求
     *
     * @param requestList                   请求的权限列表
     * @param continueRequestRunnable       继续请求任务对象
     * @param breakRequestRunnable          中断请求任务对象
     */
    fun askWhetherRequestPermission(
        activity: Activity,
        requestList: List<IPermission>,
        continueRequestRunnable: Runnable,
        breakRequestRunnable: Runnable
    )

    /**
     * 权限请求开始
     *
     * @param requestList                   请求的权限列表
     */
    fun onRequestPermissionStart(activity: Activity, requestList: List<IPermission>)

    /**
     * 权限请求结束
     *
     * @param requestList                   请求的权限列表
     */
    fun onRequestPermissionEnd(activity: Activity, requestList: List<IPermission>)
}