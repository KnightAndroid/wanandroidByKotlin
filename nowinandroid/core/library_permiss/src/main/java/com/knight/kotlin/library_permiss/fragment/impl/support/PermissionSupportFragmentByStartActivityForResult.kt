package com.knight.kotlin.library_permiss.fragment.impl.support

import com.knight.kotlin.library_permiss.core.PermissionChannelImpl
import com.knight.kotlin.library_permiss.core.PermissionChannelImplByStartActivityForResult


/**
 * @Description 权限 Fragment 类（ {@link android.support.v4.app.Fragment} + {@link android.app.Activity#startActivityForResult(Intent, int)} ）
 * @Author knight
 * @Time 2025/6/8 19:36
 *
 */

class PermissionSupportFragmentByStartActivityForResult : PermissionSupportFragment() {


    private val mPermissionChannelImpl: PermissionChannelImpl = PermissionChannelImplByStartActivityForResult(this)

    override fun getPermissionChannelImpl(): PermissionChannelImpl {
        return mPermissionChannelImpl
    }
}