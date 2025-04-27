package com.knight.kotlin.library_permiss

import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.permissions.PermissionGroupType
import com.knight.kotlin.library_permiss.utils.PermissionUtils.containsPermission
import java.util.Arrays


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


    /** 新旧权限映射集合  */
    private val NEW_AND_OLD_PERMISSION_MAP: HashMap<String, Array<String>> = HashMap(10)

    /** 后台权限列表  */
    private val BACKGROUND_PERMISSION_LIST: MutableList<String> = ArrayList(2)

    /** 危险权限组集合  */
    private val DANGEROUS_PERMISSION_GROUP_MAP: HashMap<PermissionGroupType, List<String>> = HashMap(9)

    /** 危险权限对应的类型集合  */
    private val DANGEROUS_PERMISSION_GROUP_TYPE_MAP: HashMap<String, PermissionGroupType> = HashMap(25)
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


        // Android 13 以下开启通知栏服务，需要用到旧的通知栏权限（框架自己虚拟出来的）
        NEW_AND_OLD_PERMISSION_MAP.put(Permission.POST_NOTIFICATIONS, arrayOf<String>(Permission.NOTIFICATION_SERVICE))

        // Android 13 以下使用 WIFI 功能需要用到精确定位的权限
        NEW_AND_OLD_PERMISSION_MAP.put(Permission.NEARBY_WIFI_DEVICES, arrayOf<String>(Permission.ACCESS_FINE_LOCATION))

        // Android 13 以下访问媒体文件需要用到读取外部存储的权限
        NEW_AND_OLD_PERMISSION_MAP.put(Permission.READ_MEDIA_IMAGES, arrayOf<String>(Permission.READ_EXTERNAL_STORAGE))
        NEW_AND_OLD_PERMISSION_MAP.put(Permission.READ_MEDIA_VIDEO, arrayOf<String>(Permission.READ_EXTERNAL_STORAGE))
        NEW_AND_OLD_PERMISSION_MAP.put(Permission.READ_MEDIA_AUDIO, arrayOf<String>(Permission.READ_EXTERNAL_STORAGE))

        // Android 12 以下扫描蓝牙需要精确定位权限
        NEW_AND_OLD_PERMISSION_MAP.put(Permission.BLUETOOTH_SCAN, arrayOf<String>(Permission.ACCESS_FINE_LOCATION))

        // Android 11 以下访问完整的文件管理需要用到读写外部存储的权限
        NEW_AND_OLD_PERMISSION_MAP.put(Permission.MANAGE_EXTERNAL_STORAGE, arrayOf<String>(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE))

        // Android 10 以下获取运动步数需要用到传感器权限（因为 ACTIVITY_RECOGNITION 是从 Android 10 开始才从传感器权限中剥离成独立权限）
        NEW_AND_OLD_PERMISSION_MAP.put(Permission.ACTIVITY_RECOGNITION, arrayOf<String>(Permission.BODY_SENSORS))

        // Android 8.0 以下读取电话号码需要用到读取电话状态的权限
        NEW_AND_OLD_PERMISSION_MAP.put(Permission.READ_PHONE_NUMBERS, arrayOf<String>(Permission.READ_PHONE_STATE))


        // 后台定位权限
        BACKGROUND_PERMISSION_LIST.add(Permission.ACCESS_BACKGROUND_LOCATION)

        // 后台传感器权限
        BACKGROUND_PERMISSION_LIST.add(Permission.BODY_SENSORS_BACKGROUND)


        // 存储权限组
        val storagePermissionGroup = Arrays.asList(
            Permission.READ_EXTERNAL_STORAGE,
            Permission.WRITE_EXTERNAL_STORAGE
        )
        DANGEROUS_PERMISSION_GROUP_MAP.put(PermissionGroupType.STORAGE, storagePermissionGroup)
        for (permission in storagePermissionGroup) {
            DANGEROUS_PERMISSION_GROUP_TYPE_MAP.put(permission, PermissionGroupType.STORAGE)
        }

        // 日历权限组
        val calendarPermissionGroup = Arrays.asList(
            Permission.READ_CALENDAR,
            Permission.WRITE_CALENDAR
        )
        DANGEROUS_PERMISSION_GROUP_MAP.put(PermissionGroupType.CALENDAR, calendarPermissionGroup)
        for (permission in calendarPermissionGroup) {
            DANGEROUS_PERMISSION_GROUP_TYPE_MAP.put(permission, PermissionGroupType.CALENDAR)
        }

        // 联系人权限组
        val contactsPermissionGroup = Arrays.asList(
            Permission.READ_CONTACTS,
            Permission.WRITE_CONTACTS
        )
        DANGEROUS_PERMISSION_GROUP_MAP.put(PermissionGroupType.CONTACTS, contactsPermissionGroup)
        for (permission in contactsPermissionGroup) {
            DANGEROUS_PERMISSION_GROUP_TYPE_MAP.put(permission, PermissionGroupType.CONTACTS)
        }

        // 短信权限组
        val smsPermissionGroup = Arrays.asList(
            Permission.SEND_SMS,
            Permission.READ_SMS,
            Permission.RECEIVE_SMS,
            Permission.RECEIVE_WAP_PUSH,
            Permission.RECEIVE_MMS
        )
        DANGEROUS_PERMISSION_GROUP_MAP.put(PermissionGroupType.SMS, smsPermissionGroup)
        for (permission in smsPermissionGroup) {
            DANGEROUS_PERMISSION_GROUP_TYPE_MAP.put(permission, PermissionGroupType.SMS)
        }

        // 定位权限组
        val locationPermissionGroup = Arrays.asList(
            Permission.ACCESS_COARSE_LOCATION,
            Permission.ACCESS_FINE_LOCATION,
            Permission.ACCESS_BACKGROUND_LOCATION
        )
        DANGEROUS_PERMISSION_GROUP_MAP.put(PermissionGroupType.LOCATION, locationPermissionGroup)
        for (permission in locationPermissionGroup) {
            DANGEROUS_PERMISSION_GROUP_TYPE_MAP.put(permission, PermissionGroupType.LOCATION)
        }

        // 传感器权限组
        val sensorsPermissionGroup = Arrays.asList(
            Permission.BODY_SENSORS,
            Permission.BODY_SENSORS_BACKGROUND
        )
        DANGEROUS_PERMISSION_GROUP_MAP.put(PermissionGroupType.SENSORS, sensorsPermissionGroup)
        for (permission in sensorsPermissionGroup) {
            DANGEROUS_PERMISSION_GROUP_TYPE_MAP.put(permission, PermissionGroupType.SENSORS)
        }

        // 通话记录权限组
        val callLogPermissionGroup = Arrays.asList(
            Permission.READ_CALL_LOG,
            Permission.WRITE_CALL_LOG
        )
        DANGEROUS_PERMISSION_GROUP_MAP.put(PermissionGroupType.CALL_LOG, callLogPermissionGroup)
        for (permission in callLogPermissionGroup) {
            DANGEROUS_PERMISSION_GROUP_TYPE_MAP.put(permission, PermissionGroupType.CALL_LOG)
        }

        // 附近设备权限组
        val nearbyDevicesPermissionGroup = Arrays.asList(
            Permission.BLUETOOTH_SCAN,
            Permission.BLUETOOTH_CONNECT,
            Permission.BLUETOOTH_ADVERTISE,
            Permission.NEARBY_WIFI_DEVICES
        )
        DANGEROUS_PERMISSION_GROUP_MAP.put(PermissionGroupType.NEARBY_DEVICES, nearbyDevicesPermissionGroup)
        for (permission in nearbyDevicesPermissionGroup) {
            DANGEROUS_PERMISSION_GROUP_TYPE_MAP.put(permission, PermissionGroupType.NEARBY_DEVICES)
        }

        // 读取照片和视频媒体文件权限组
        val imageAndVideoPermissionGroup = Arrays.asList(
            Permission.READ_MEDIA_IMAGES,
            Permission.READ_MEDIA_VIDEO,
            Permission.READ_MEDIA_VISUAL_USER_SELECTED
        )
        DANGEROUS_PERMISSION_GROUP_MAP.put(PermissionGroupType.IMAGE_AND_VIDEO_MEDIA, imageAndVideoPermissionGroup)
        for (permission in imageAndVideoPermissionGroup) {
            DANGEROUS_PERMISSION_GROUP_TYPE_MAP.put(permission, PermissionGroupType.IMAGE_AND_VIDEO_MEDIA)
        }
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

    /**
     * 通过新权限查询到对应的旧权限
     */
    fun queryOldPermissionByNewPermission(permission: String?): Array<String>? {
        return NEW_AND_OLD_PERMISSION_MAP[permission]
    }

    /**
     * 查询危险权限所在的权限组类型
     */

    fun queryDangerousPermissionGroupType( permission: String): PermissionGroupType? {
        return DANGEROUS_PERMISSION_GROUP_TYPE_MAP[permission]
    }

    /**
     * 查询危险权限所在的权限组
     */

    fun getDangerousPermissionGroup(permissionsGroupType: PermissionGroupType): List<String>? {
        return DANGEROUS_PERMISSION_GROUP_MAP[permissionsGroupType]
    }

    /**
     * 判断申请的权限列表中是否包含后台权限
     */
    fun containsBackgroundPermission(permissions: List<String?>): Boolean {
        for (backgroundPermission in BACKGROUND_PERMISSION_LIST) {
            if (permissions.contains(backgroundPermission)) {
                return true
            }
        }
        return false
    }

    /**
     * 判断某个权限是否为后台权限
     */
    fun isBackgroundPermission(permission: String?): Boolean {
        return BACKGROUND_PERMISSION_LIST.contains(permission)
    }

    /**
     * 从权限组中获取到后台权限
     */

    fun getBackgroundPermissionByGroup(permissions: List<String>): String? {
        for (permission in permissions) {
            if (isBackgroundPermission(permission)) {
                return permission
            }
        }
        return null
    }
}