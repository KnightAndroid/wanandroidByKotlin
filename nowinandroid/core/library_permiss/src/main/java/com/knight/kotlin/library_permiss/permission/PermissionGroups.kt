package com.knight.kotlin.library_permiss.permission


/**
 * @author created by luguian
 * @organize
 * @Date 2025/7/11 9:23
 * @descript:权限组的名称常量集
 */
object PermissionGroups {
    /**
     * 权限组的后缀名称
     */
    const val SUFFIX: String = "_group"

    /**
     * 存储权限组，包含以下权限
     *
     * [PermissionNames.READ_EXTERNAL_STORAGE]
     * [PermissionNames.WRITE_EXTERNAL_STORAGE]
     */
    const val STORAGE: String = "storage" + SUFFIX

    /**
     * 日历权限组，包含以下权限
     *
     * [PermissionNames.READ_CALENDAR]
     * [PermissionNames.WRITE_CALENDAR]
     */
    const val CALENDAR: String = "calendar" + SUFFIX

    /**
     * 通讯录权限组，包含以下权限
     *
     * [PermissionNames.READ_CONTACTS]
     * [PermissionNames.WRITE_CONTACTS]
     * [PermissionNames.GET_ACCOUNTS]
     */
    const val CONTACTS: String = "contacts" + SUFFIX

    /**
     * 短信权限组，包含以下权限
     *
     * [PermissionNames.SEND_SMS]
     * [PermissionNames.READ_SMS]
     * [PermissionNames.RECEIVE_SMS]
     * [PermissionNames.RECEIVE_WAP_PUSH]
     * [PermissionNames.RECEIVE_MMS]
     */
    const val SMS: String = "sms" + SUFFIX

    /**
     * 位置权限组，包含以下权限
     *
     * [PermissionNames.ACCESS_COARSE_LOCATION]
     * [PermissionNames.ACCESS_FINE_LOCATION]
     * [PermissionNames.ACCESS_BACKGROUND_LOCATION]
     *
     * 注意：在 Android 12 的时候，蓝牙相关的权限已经归到附近设备的权限组了，但是在 Android 12 之前，蓝牙相关的权限归属定位权限组
     * [PermissionNames.BLUETOOTH_SCAN]
     * [PermissionNames.BLUETOOTH_CONNECT]
     * [PermissionNames.BLUETOOTH_ADVERTISE]
     *
     * 注意：在 Android 13 的时候，WIFI 相关的权限已经归到附近设备的权限组了，但是在 Android 13 之前，WIFI 相关的权限归属定位权限组
     * [PermissionNames.NEARBY_WIFI_DEVICES]
     */
    const val LOCATION: String = "location" + SUFFIX

    /**
     * 传感器权限组，包含以下权限
     *
     * [PermissionNames.BODY_SENSORS]
     * [PermissionNames.BODY_SENSORS_BACKGROUND]
     */
    const val SENSORS: String = "sensors" + SUFFIX

    /**
     * 电话权限组，包含以下权限
     *
     * [PermissionNames.READ_PHONE_STATE]
     * [PermissionNames.CALL_PHONE]
     * [PermissionNames.ADD_VOICEMAIL]
     * [PermissionNames.USE_SIP]
     * [PermissionNames.READ_PHONE_NUMBERS]
     * [PermissionNames.ANSWER_PHONE_CALLS]
     * [PermissionNames.ACCEPT_HANDOVER]
     *
     * 注意：在 Android 9.0 的时候，读写通话记录权限已经归到一个单独的权限组了，但是在 Android 9.0 之前，读写通话记录权限归属电话权限组
     * [PermissionNames.READ_CALL_LOG]
     * [PermissionNames.WRITE_CALL_LOG]
     * [PermissionNames.PROCESS_OUTGOING_CALLS]
     */
    const val PHONE: String = "phone" + SUFFIX

    /**
     * 通话记录权限组（在 Android 9.0 的时候，读写通话记录权限已经归到一个单独的权限组了，但是在 Android 9.0 之前，读写通话记录权限归属电话权限组），包含以下权限
     *
     * [PermissionNames.READ_CALL_LOG]
     * [PermissionNames.WRITE_CALL_LOG]
     * [PermissionNames.PROCESS_OUTGOING_CALLS]
     */
    const val CALL_LOG: String = "call_log" + SUFFIX

    /**
     * 附近设备权限组，包含以下权限
     *
     * 在 Android 12 的时候，蓝牙相关的权限已经归到附近设备的权限组了，但是在 Android 12 之前，蓝牙相关的权限归属定位权限组
     * [PermissionNames.BLUETOOTH_SCAN]
     * [PermissionNames.BLUETOOTH_CONNECT]
     * [PermissionNames.BLUETOOTH_ADVERTISE]
     *
     * 注意：在 Android 13 的时候，WIFI 相关的权限已经归到附近设备的权限组了，但是在 Android 13 之前，WIFI 相关的权限归属定位权限组
     * [PermissionNames.NEARBY_WIFI_DEVICES]
     */
    const val NEARBY_DEVICES: String = "nearby_devices" + SUFFIX

    /**
     * 照片和视频权限组（注意：不包含音频权限） ，包含以下权限
     *
     * [PermissionNames.READ_MEDIA_IMAGES]
     * [PermissionNames.READ_MEDIA_VIDEO]
     * [PermissionNames.READ_MEDIA_VISUAL_USER_SELECTED]
     */
    const val IMAGE_AND_VIDEO_MEDIA: String = "image_and_video_media" + SUFFIX
}