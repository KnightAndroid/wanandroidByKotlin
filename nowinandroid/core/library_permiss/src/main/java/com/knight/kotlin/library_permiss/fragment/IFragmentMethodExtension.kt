package com.knight.kotlin.library_permiss.fragment

import com.knight.kotlin.library_permiss.core.RequestPermissionDelegateImpl
import com.knight.kotlin.library_permiss.listener.OnPermissionFlowCallback

/**
 * @Description Fragment 扩展接口方法
 * @Author knight
 * @Time 2025/6/8 16:59
 *
 */

interface IFragmentMethodExtension<FM> {
    /** 获取请求权限的实现逻辑  */
    fun getRequestPermissionDelegateImpl(): RequestPermissionDelegateImpl

    /** 提交绑定  */
    fun commitAttach(fragmentManager: FM)

    /** 提交解绑  */
    fun commitDetach()

    /** 设置权限回调监听  */
    fun setCallback( callback: OnPermissionFlowCallback)

    /** 设置权限申请标记  */
    fun setRequestFlag(flag: Boolean)
}