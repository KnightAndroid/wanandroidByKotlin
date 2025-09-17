package com.knight.kotlin.library_permiss.fragment.impl.app

import com.knight.kotlin.library_permiss.core.PermissionChannelImpl
import com.knight.kotlin.library_permiss.core.PermissionChannelImplByRequestPermissions


/**
 * @Description 权限 Fragment 类（ {@link android.app.Fragment} + {@link android.app.Activity#requestPermissions(String[], int)} ）
 * @Author knight
 * @Time 2025/6/8 19:27
 *
 */

class PermissionAppFragmentByRequestPermissions : PermissionAppFragment() {

    private val mPermissionChannelImpl: PermissionChannelImpl = PermissionChannelImplByRequestPermissions(this)


    override fun getPermissionChannelImpl(): PermissionChannelImpl {
        return mPermissionChannelImpl
    }
}