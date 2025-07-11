package com.knight.kotlin.library_permiss.fragment.factory

import android.app.Activity
import androidx.fragment.app.FragmentManager
import com.knight.kotlin.library_permiss.core.OnPermissionFlowCallback
import com.knight.kotlin.library_permiss.fragment.IFragmentMethod
import com.knight.kotlin.library_permiss.fragment.impl.app.PermissionFragmentAppByDangerous
import com.knight.kotlin.library_permiss.fragment.impl.app.PermissionFragmentAppBySpecial
import com.knight.kotlin.library_permiss.manager.PermissionRequestCodeManager
import com.knight.kotlin.library_permiss.manager.PermissionRequestCodeManager.generateRandomRequestCode
import com.knight.kotlin.library_permiss.permission.PermissionType
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
         permissions: List<IPermission>,
         permissionType: PermissionType,
        callback: OnPermissionFlowCallback
    ) {
        val fragment: IFragmentMethod<Activity, FragmentManager>
        if (permissionType === PermissionType.SPECIAL) {
            fragment = PermissionFragmentAppBySpecial()
        } else {
            fragment = PermissionFragmentAppByDangerous()
        }
        val maxRequestCode = PermissionRequestCodeManager.REQUEST_CODE_LIMIT_HIGH_VALUE
        val requestCode = generateRandomRequestCode(maxRequestCode)
        fragment.setArguments(generatePermissionArguments(permissions, requestCode))
        fragment.setRetainInstance(true)
        fragment.setRequestFlag(true)
        fragment.setCallback(callback)
        getFragmentManager()?.let { fragment.commitAttach(it) }
    }
}