package com.knight.kotlin.library_permiss.manager

import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.tools.PermissionUtils


/**
 * @author created by luguian
 * @organize
 * @Date 2025/9/17 11:07
 * @descript: 已请求权限的管理类
 */
object AlreadyRequestPermissionsManager {
    /** 已请求过的权限集  */
    private val ALREADY_REQUEST_PERMISSIONS_LIST: MutableList<String> = ArrayList()

    /**
     * 添加已申请过的权限
     */
    fun addAlreadyRequestPermissions(permissions: List<IPermission>) {
        if (permissions == null || permissions.isEmpty()) {
            return
        }
        for (permission in permissions) {
            val permissionName = permission.getPermissionName()
            if (PermissionUtils.containsPermissionByString(ALREADY_REQUEST_PERMISSIONS_LIST, permissionName)) {
                continue
            }
            ALREADY_REQUEST_PERMISSIONS_LIST.add(permissionName)
        }
    }

    /**
     * 判断某些权限是否申请过
     */
    fun isAlreadyRequestPermissions(permission: IPermission?): Boolean {
        if (permission == null) {
            return false
        }
        return PermissionUtils.containsPermissionByString(ALREADY_REQUEST_PERMISSIONS_LIST, permission.getPermissionName())
    }
}