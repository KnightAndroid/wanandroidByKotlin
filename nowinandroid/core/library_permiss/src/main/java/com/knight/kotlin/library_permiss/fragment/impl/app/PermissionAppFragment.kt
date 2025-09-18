package com.knight.kotlin.library_permiss.fragment.impl.app

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.knight.kotlin.library_permiss.core.OnPermissionFragmentCallback
import com.knight.kotlin.library_permiss.fragment.IFragmentMethod


/**
 * @Description 权限 Fragment 类（ {@link android.app.Fragment} ）
 * @Author knight
 * @Time 2025/6/8 19:25
 *
 */

abstract class PermissionAppFragment : Fragment(), IFragmentMethod<Activity, FragmentManager> {
    override fun setPermissionFragmentCallback(callback: OnPermissionFragmentCallback) {
        getPermissionChannelImpl().setPermissionFragmentCallback(callback)
    }

    override fun setNonSystemRestartMark(nonSystemRestartMark: Boolean) {
        getPermissionChannelImpl().setNonSystemRestartMark(nonSystemRestartMark)
    }

    override fun commitFragmentAttach( fragmentManager: FragmentManager) {
        if (fragmentManager == null) {
            return
        }
        fragmentManager.beginTransaction().add(this, this.toString()).commitAllowingStateLoss()
    }

    override fun commitFragmentDetach() {
        val fragmentManager = fragmentManager ?: return
        fragmentManager.beginTransaction().remove(this).commitAllowingStateLoss()
    }

    override fun onResume() {
        super.onResume()
        getPermissionChannelImpl().onFragmentResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        getPermissionChannelImpl().onFragmentDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,  grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        getPermissionChannelImpl().onFragmentRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        getPermissionChannelImpl().onFragmentActivityResult(requestCode, resultCode, data)
    }
}