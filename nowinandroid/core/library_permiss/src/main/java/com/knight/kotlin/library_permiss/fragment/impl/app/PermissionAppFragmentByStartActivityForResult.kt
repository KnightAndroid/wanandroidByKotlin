package com.knight.kotlin.library_permiss.fragment.impl.app

import com.knight.kotlin.library_permiss.core.PermissionChannelImpl
import com.knight.kotlin.library_permiss.core.PermissionChannelImplByStartActivityForResult


/**
 * @Description 权限 Fragment 类（ {@link android.app.Fragment} + {@link android.app.Activity#startActivityForResult(Intent, int)} ）
 * @Author knight
 * @Time 2025/6/8 19:28
 *
 */

class PermissionAppFragmentByStartActivityForResult : PermissionAppFragment() {


    private val mPermissionChannelImpl: PermissionChannelImpl = PermissionChannelImplByStartActivityForResult(this)


    override fun getPermissionChannelImpl(): PermissionChannelImpl {
        return mPermissionChannelImpl
    }
}