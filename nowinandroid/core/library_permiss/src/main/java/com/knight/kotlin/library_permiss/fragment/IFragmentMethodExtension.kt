package com.knight.kotlin.library_permiss.fragment

import com.knight.kotlin.library_permiss.core.OnPermissionFragmentCallback
import com.knight.kotlin.library_permiss.core.PermissionChannelImpl


/**
 * @Description Fragment 扩展接口方法
 * @Author knight
 * @Time 2025/6/8 16:59
 *
 */

interface IFragmentMethodExtension<FM> {
    /**
     * 获取权限请求通道的实现逻辑
     */
    fun getPermissionChannelImpl(): PermissionChannelImpl

    /**
     * 提交 Fragment 绑定
     */
    fun commitFragmentAttach(fragmentManager: FM)

    /**
     * 提交 Fragment 解绑
     */
    fun commitFragmentDetach()

    /**
     * 设置权限请求流程回调
     */
    fun setPermissionFragmentCallback(callback: OnPermissionFragmentCallback)

    /**
     * 设置非系统重启标记
     */
    fun setNonSystemRestartMark(nonSystemRestartMark: Boolean)
}