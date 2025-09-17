package com.knight.kotlin.library_permiss.fragment.impl.support

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.knight.kotlin.library_permiss.core.OnPermissionFragmentCallback
import com.knight.kotlin.library_permiss.fragment.IFragmentMethod


/**
 * @Description 权限 Fragment 类（ {@link android.support.v4.app.Fragment} ）
 * @Author knight
 * @Time 2025/6/8 19:34
 *
 */

abstract class PermissionSupportFragment : Fragment(),
    IFragmentMethod<FragmentActivity, FragmentManager> {
    /**
     * 设置回调对象
     */
    override fun setPermissionFragmentCallback( callback: OnPermissionFragmentCallback?) {
        getPermissionChannelImpl().setPermissionFragmentCallback(callback)
    }

    /**
     * 设置非系统重启标记
     */
    override fun setNonSystemRestartMark(nonSystemRestartMark: Boolean) {
        getPermissionChannelImpl().setNonSystemRestartMark(nonSystemRestartMark)
    }

    /**
     * 提交 Fragment 绑定
     */
    override fun commitFragmentAttach( fragmentManager: FragmentManager?) {
        if (fragmentManager == null) {
            return
        }
        fragmentManager.beginTransaction().add(this, this.toString()).commitAllowingStateLoss()
    }

    /**
     * 提交 Fragment 解绑
     */
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

    override fun onRequestPermissionsResult(requestCode: Int,  permissions: Array<String?>,  grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        getPermissionChannelImpl().onFragmentRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        getPermissionChannelImpl().onFragmentActivityResult(requestCode, resultCode, data)
    }
}