package com.knight.kotlin.library_permiss

import android.app.Activity
import androidx.fragment.app.FragmentManager

import com.knight.kotlin.library_permiss.listener.IFragmentMethod
import com.knight.kotlin.library_permiss.listener.OnPermissionFlowCallback
import com.knight.kotlin.library_permiss.permissions.PermissionType


/**
 * @Description
 * @Author knight
 * @Time 2025/6/8 19:32
 *
 */

internal class PermissionFragmentFactoryByApp(
    activity: Activity,
    fragmentManager: FragmentManager?
) :
    PermissionFragmentFactory<Activity, FragmentManager>(activity, fragmentManager) {
    override fun createAndCommitFragment(
        permissions: List<String?>,
        permissionType: PermissionType,
        callback: OnPermissionFlowCallback
    ) {
        val fragment: IFragmentMethod<Activity, FragmentManager> =
            if (permissionType === PermissionType.SPECIAL) {
                PermissionFragmentAppBySpecial()
            } else {
                PermissionFragmentAppByDangerous()
            }
        val maxRequestCode: Int = PermissionRequestCodeManager.REQUEST_CODE_LIMIT_HIGH_VALUE
        val requestCode: Int =
            PermissionRequestCodeManager.generateRandomRequestCode(maxRequestCode)
        fragment.setArguments(generatePermissionArguments(permissions, requestCode))
        fragment.setRetainInstance(true)
        fragment.setRequestFlag(true)
        fragment.setCallback(callback!!)
        getFragmentManager()?.let {
            fragment.commitAttach(it)
        }

    }
}