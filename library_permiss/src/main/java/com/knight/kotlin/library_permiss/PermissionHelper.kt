package com.knight.kotlin.library_permiss

import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.utils.PermissionUtils.containsPermission


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/13 14:17
 * @descript:
 */
internal object PermissionHelper {
    /** 特殊权限列表  */
    private val SPECIAL_PERMISSION_LIST: MutableList<String> = ArrayList(12)

    /** 权限和 Android 版本对应的集合  */
    private val PERMISSION_VERSION_MAP: MutableMap<String, Int> = HashMap(53)

    /** 框架自己虚拟出来的权限列表（此类权限不需要清单文件中静态注册也能动态申请）  */
    private val VIRTUAL_PERMISSION_LIST: MutableList<String> = ArrayList(4)

    init {
        SPECIAL_PERMISSION_LIST.add(Permission.SCHEDULE_EXACT_ALARM)
        SPECIAL_PERMISSION_LIST.add(Permission.MANAGE_EXTERNAL_STORAGE)
        SPECIAL_PERMISSION_LIST.add(Permission.REQUEST_INSTALL_PACKAGES)
        SPECIAL_PERMISSION_LIST.add(Permission.PICTURE_IN_PICTURE)
        SPECIAL_PERMISSION_LIST.add(Permission.SYSTEM_ALERT_WINDOW)
        SPECIAL_PERMISSION_LIST.add(Permission.WRITE_SETTINGS)
        SPECIAL_PERMISSION_LIST.add(Permission.ACCESS_NOTIFICATION_POLICY)
        SPECIAL_PERMISSION_LIST.add(Permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
        SPECIAL_PERMISSION_LIST.add(Permission.PACKAGE_USAGE_STATS)
        SPECIAL_PERMISSION_LIST.add(Permission.NOTIFICATION_SERVICE)
        SPECIAL_PERMISSION_LIST.add(Permission.BIND_NOTIFICATION_LISTENER_SERVICE)
        SPECIAL_PERMISSION_LIST.add(Permission.BIND_VPN_SERVICE)

        PERMISSION_VERSION_MAP[Permission.SCHEDULE_EXACT_ALARM] = AndroidVersion.ANDROID_12
        PERMISSION_VERSION_MAP[Permission.MANAGE_EXTERNAL_STORAGE] = AndroidVersion.ANDROID_11
        PERMISSION_VERSION_MAP[Permission.REQUEST_INSTALL_PACKAGES] = AndroidVersion.ANDROID_8
        PERMISSION_VERSION_MAP[Permission.PICTURE_IN_PICTURE] = AndroidVersion.ANDROID_8
        PERMISSION_VERSION_MAP[Permission.SYSTEM_ALERT_WINDOW] = AndroidVersion.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.WRITE_SETTINGS] = AndroidVersion.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.ACCESS_NOTIFICATION_POLICY] = AndroidVersion.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS] = AndroidVersion.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.PACKAGE_USAGE_STATS] = AndroidVersion.ANDROID_5
        PERMISSION_VERSION_MAP[Permission.NOTIFICATION_SERVICE] = AndroidVersion.ANDROID_4_4
        PERMISSION_VERSION_MAP[Permission.BIND_NOTIFICATION_LISTENER_SERVICE] = AndroidVersion.ANDROID_4_3
        PERMISSION_VERSION_MAP[Permission.BIND_VPN_SERVICE] = AndroidVersion.ANDROID_4_0

        PERMISSION_VERSION_MAP[Permission.READ_MEDIA_VISUAL_USER_SELECTED] = AndroidVersion.ANDROID_14
        PERMISSION_VERSION_MAP[Permission.POST_NOTIFICATIONS] = AndroidVersion.ANDROID_13
        PERMISSION_VERSION_MAP[Permission.NEARBY_WIFI_DEVICES] = AndroidVersion.ANDROID_13
        PERMISSION_VERSION_MAP[Permission.BODY_SENSORS_BACKGROUND] = AndroidVersion.ANDROID_13
        PERMISSION_VERSION_MAP[Permission.READ_MEDIA_IMAGES] = AndroidVersion.ANDROID_13
        PERMISSION_VERSION_MAP[Permission.READ_MEDIA_VIDEO] = AndroidVersion.ANDROID_13
        PERMISSION_VERSION_MAP[Permission.READ_MEDIA_AUDIO] = AndroidVersion.ANDROID_13
        PERMISSION_VERSION_MAP[Permission.BLUETOOTH_SCAN] = AndroidVersion.ANDROID_12
        PERMISSION_VERSION_MAP[Permission.BLUETOOTH_CONNECT] = AndroidVersion.ANDROID_12
        PERMISSION_VERSION_MAP[Permission.BLUETOOTH_ADVERTISE] = AndroidVersion.ANDROID_12
        PERMISSION_VERSION_MAP[Permission.ACCESS_BACKGROUND_LOCATION] = AndroidVersion.ANDROID_10
        PERMISSION_VERSION_MAP[Permission.ACTIVITY_RECOGNITION] = AndroidVersion.ANDROID_10
        PERMISSION_VERSION_MAP[Permission.ACCESS_MEDIA_LOCATION] = AndroidVersion.ANDROID_10
        PERMISSION_VERSION_MAP[Permission.ACCEPT_HANDOVER] = AndroidVersion.ANDROID_9
        PERMISSION_VERSION_MAP[Permission.ANSWER_PHONE_CALLS] = AndroidVersion.ANDROID_8
        PERMISSION_VERSION_MAP[Permission.READ_PHONE_NUMBERS] = AndroidVersion.ANDROID_8
        PERMISSION_VERSION_MAP[Permission.GET_INSTALLED_APPS] = AndroidVersion.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.READ_EXTERNAL_STORAGE] = AndroidVersion.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.WRITE_EXTERNAL_STORAGE] = AndroidVersion.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.CAMERA] = AndroidVersion.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.RECORD_AUDIO] = AndroidVersion.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.ACCESS_FINE_LOCATION] = AndroidVersion.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.ACCESS_COARSE_LOCATION] = AndroidVersion.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.READ_CONTACTS] = AndroidVersion.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.WRITE_CONTACTS] = AndroidVersion.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.GET_ACCOUNTS] = AndroidVersion.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.READ_CALENDAR] = AndroidVersion.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.WRITE_CALENDAR] = AndroidVersion.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.READ_PHONE_STATE] = AndroidVersion.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.CALL_PHONE] = AndroidVersion.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.READ_CALL_LOG] = AndroidVersion.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.WRITE_CALL_LOG] = AndroidVersion.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.ADD_VOICEMAIL] = AndroidVersion.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.USE_SIP] = AndroidVersion.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.PROCESS_OUTGOING_CALLS] = AndroidVersion.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.BODY_SENSORS] = AndroidVersion.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.SEND_SMS] = AndroidVersion.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.RECEIVE_SMS] = AndroidVersion.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.READ_SMS] = AndroidVersion.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.RECEIVE_WAP_PUSH] = AndroidVersion.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.RECEIVE_MMS] = AndroidVersion.ANDROID_6

        VIRTUAL_PERMISSION_LIST.add(Permission.NOTIFICATION_SERVICE)
        VIRTUAL_PERMISSION_LIST.add(Permission.BIND_NOTIFICATION_LISTENER_SERVICE)
        VIRTUAL_PERMISSION_LIST.add(Permission.BIND_VPN_SERVICE)
        VIRTUAL_PERMISSION_LIST.add(Permission.PICTURE_IN_PICTURE)
    }

    /**
     * 判断某个权限是否是特殊权限
     */
    fun isSpecialPermission(permission: String): Boolean {
        return containsPermission(SPECIAL_PERMISSION_LIST, permission)
    }

    /**
     * 获取权限是从哪个 Android 版本新增的
     */
    fun findAndroidVersionByPermission(permission: String): Int {
        val androidVersion = PERMISSION_VERSION_MAP[permission] ?: return 0
        return androidVersion
    }

    /**
     * 判断权限是否为框架自己虚拟出来的
     */
    fun isVirtualPermission(permission: String): Boolean {
        return containsPermission(VIRTUAL_PERMISSION_LIST, permission)
    }
}