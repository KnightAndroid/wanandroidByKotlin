package com.knight.kotlin.library_permiss.fragment

import android.app.Activity
import android.os.Bundle
import androidx.annotation.IntRange
import com.knight.kotlin.library_permiss.start.IStartActivityDelegate


/**
 * @Description Fragment 原生接口方法
 * @Author knight
 * @Time 2025/6/8 17:00
 *
 */

interface IFragmentMethodNative<A : Activity> : IStartActivityDelegate {
    /** 获得 Activity 对象  */
    fun getActivity(): A?


    /** 请求权限  */
    fun requestPermissions(
        permissions: Array<String>,
        @IntRange(from = 1, to = 65535) requestCode: Int
    )

    /** 获得参数集  */
    fun getArguments(): Bundle

    /** 设置参数集  */
    fun setArguments(args: Bundle)

    /** 设置是否保存实例，如果设置保存，则不会因为屏幕方向或配置变化而重新创建  */
    fun setRetainInstance(retain: Boolean)

    /** 当前 Fragment 是否已添加绑定  */
    fun isAdded(): Boolean

    /** 当前 Fragment 是否已移除  */
    fun isRemoving(): Boolean
}