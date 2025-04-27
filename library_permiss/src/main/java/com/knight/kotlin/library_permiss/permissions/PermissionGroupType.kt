package com.knight.kotlin.library_permiss.permissions


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/27 11:27
 * @descript:
 */
enum class PermissionGroupType {
    /** 存储权限  */
    STORAGE,

    /** 日历权限组  */
    CALENDAR,

    /** 联系人权限组  */
    CONTACTS,

    /** 短信权限组  */
    SMS,

    /** 位置权限组  */
    LOCATION,

    /** 传感器权限组  */
    SENSORS,

    /** 通话记录权限组  */
    CALL_LOG,

    /** 附近设备权限组  */
    NEARBY_DEVICES,

    /** 照片和视频权限组  */
    IMAGE_AND_VIDEO_MEDIA
}