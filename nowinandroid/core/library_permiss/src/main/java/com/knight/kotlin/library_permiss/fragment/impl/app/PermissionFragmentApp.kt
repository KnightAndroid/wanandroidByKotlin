package com.knight.kotlin.library_permiss.fragment.impl.app

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.knight.kotlin.library_permiss.fragment.IFragmentMethod
import com.knight.kotlin.library_permiss.listener.OnPermissionFlowCallback


/**
 * @Description
 * @Author knight
 * @Time 2025/6/8 19:25
 *
 */

abstract class PermissionFragmentApp : Fragment(), IFragmentMethod<Activity, FragmentManager> {
    /**
     * 设置回调对象
     */
    override fun setCallback(callback: OnPermissionFlowCallback) {
        getRequestPermissionDelegateImpl().setCallback(callback)
    }


    /**
     * 设置请求 Flag
     */
    override fun setRequestFlag(flag: Boolean) {
        getRequestPermissionDelegateImpl().setRequestFlag(flag)
    }

    /**
     * 提交绑定
     */
    override fun commitAttach( fragmentManager: FragmentManager) {
        if (fragmentManager == null) {
            return
        }
        fragmentManager.beginTransaction().add(this, this.toString()).commitAllowingStateLoss()
    }

    /**
     * 提交解绑
     */
    override fun commitDetach() {
        val fragmentManager: FragmentManager = getFragmentManager() ?: return
        fragmentManager.beginTransaction().remove(this).commitAllowingStateLoss()
    }

    override fun onResume() {
        super.onResume()
        getRequestPermissionDelegateImpl().onFragmentResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        getRequestPermissionDelegateImpl().onFragmentDestroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
         permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        getRequestPermissionDelegateImpl().onFragmentRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        getRequestPermissionDelegateImpl().onFragmentActivityResult(requestCode, resultCode, data)
    }
}