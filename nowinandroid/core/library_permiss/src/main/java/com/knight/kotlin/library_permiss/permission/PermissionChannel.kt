package com.knight.kotlin.library_permiss.permission


/**
 * @author created by luguian
 * @organize
 * @Date 2025/9/17 16:07
 * @descript:权限请求通道
 */
enum class PermissionChannel {
    /** [android.app.Activity.requestPermissions]  */
    REQUEST_PERMISSIONS,

    /** [android.app.Activity.startActivityForResult]  */
    START_ACTIVITY_FOR_RESULT
}