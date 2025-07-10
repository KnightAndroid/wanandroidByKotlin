package com.knight.kotlin.library_permiss.fragment

import android.app.Activity

/**
 * @Description Fragment 接口方法
 * @Author knight
 * @Time 2025/6/8 16:58
 *
 */

interface IFragmentMethod<A : Activity, FM> : IFragmentMethodNative<A>,
    IFragmentMethodExtension<FM>