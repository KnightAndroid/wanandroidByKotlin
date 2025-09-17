package com.knight.kotlin.library_permiss.fragment.factory

import android.app.Activity
import androidx.fragment.app.FragmentManager
import com.knight.kotlin.library_permiss.core.OnPermissionFragmentCallback
import com.knight.kotlin.library_permiss.fragment.IFragmentMethod
import com.knight.kotlin.library_permiss.fragment.impl.app.PermissionAppFragmentByRequestPermissions
import com.knight.kotlin.library_permiss.fragment.impl.app.PermissionAppFragmentByStartActivityForResult
import com.knight.kotlin.library_permiss.manager.PermissionRequestCodeManager
import com.knight.kotlin.library_permiss.manager.PermissionRequestCodeManager.generateRandomRequestCode
import com.knight.kotlin.library_permiss.permission.PermissionChannel
import com.knight.kotlin.library_permiss.permission.base.IPermission


/**
 * @Description
 * @Author knight
 * @Time 2025/6/8 19:32
 *
 */

class PermissionFragmentFactoryByApp( activity: Activity,  fragmentManager: FragmentManager?) :
    PermissionFragmentFactory<Activity, FragmentManager>(activity, fragmentManager) {
    override fun createAndCommitFragment(
        permissions: List<IPermission?>,
        permissionChannel: PermissionChannel,
        callback: OnPermissionFragmentCallback
    ) {
        val fragment: IFragmentMethod<Activity, FragmentManager>
        if (permissionChannel === PermissionChannel.REQUEST_PERMISSIONS) {
            fragment = PermissionAppFragmentByRequestPermissions()
        } else {
            fragment = PermissionAppFragmentByStartActivityForResult()
        }
        val maxRequestCode = PermissionRequestCodeManager.REQUEST_CODE_LIMIT_HIGH_VALUE
        val requestCode = generateRandomRequestCode(maxRequestCode)
        fragment.setArguments(generatePermissionArguments(permissions!!, requestCode))
        fragment.setRetainInstance(true)
        fragment.setNonSystemRestartMark(true)
        fragment.setPermissionFragmentCallback(callback)
        getFragmentManager()?.let { fragment.commitFragmentAttach(it) }
    }
}