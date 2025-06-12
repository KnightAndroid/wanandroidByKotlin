package com.knight.kotlin.library_base.ktx

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * @Description
 * @Author knight
 * @Time 2024/12/30 21:04
 *
 */

inline fun <reified F : Fragment> FragmentManager.showFragment(
    @IdRes containerViewId: Int,
    tag: String? = null,
    args: Bundle? = null
) {
    beginTransaction()
        .replace(containerViewId, F::class.java, args, tag)
        .commitAllowingStateLoss()
}

fun FragmentManager.hide(@IdRes containerViewId: Int) {
    val fragment = findFragmentById(containerViewId) ?: return
    beginTransaction().remove(fragment).commitAllowingStateLoss()
}

fun FragmentManager.hide(fragment: Fragment) {
    beginTransaction().remove(fragment).commitAllowingStateLoss()
}