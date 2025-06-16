package com.knight.kotlin.library_permiss

import com.knight.kotlin.library_permiss.AndroidVersionTools.getCurrentAndroidVersionCode
import com.knight.kotlin.library_permiss.AndroidVersionTools.isAndroid11
import com.knight.kotlin.library_permiss.AndroidVersionTools.isAndroid12
import com.knight.kotlin.library_permiss.AndroidVersionTools.isAndroid13
import com.knight.kotlin.library_permiss.AndroidVersionTools.isAndroid8
import com.knight.kotlin.library_permiss.AndroidVersionTools.isAndroid9
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.permissions.PermissionGroupType
import com.knight.kotlin.library_permiss.utils.PermissionUtils
import com.knight.kotlin.library_permiss.utils.PermissionUtils.containsPermission
import com.knight.kotlin.library_permiss.utils.PhoneRomUtils.isEmui
import com.knight.kotlin.library_permiss.utils.PhoneRomUtils.isHarmonyOs
import com.knight.kotlin.library_permiss.utils.PhoneRomUtils.isMiui
import java.util.EnumMap
import kotlin.math.max


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/13 14:17
 * @descript:
 */
internal object PermissionHelper {

    /** 特殊权限列表  */
    private 
    val SPECIAL_PERMISSION_LIST: MutableList<String> = ArrayList(12)

    /** 权限和 Android 版本对应的集合  */
    private val PERMISSION_VERSION_MAP: MutableMap<String, Int> = HashMap(53)

    /** 框架自己虚拟出来的权限列表（此类权限不需要清单文件中静态注册也能动态申请）  */
    private val VIRTUAL_PERMISSION_LIST: ArrayList<String> = ArrayList(4)

    /** 新旧权限映射集合  */
    private val NEW_AND_OLD_PERMISSION_MAP: HashMap<String, Array<String>> = HashMap(10)

    /** 后台权限列表  */
    private val BACKGROUND_PERMISSION_MAP: HashMap<String, List<String>> = HashMap(2)

    /** 危险权限组集合  */
    private val DANGEROUS_PERMISSION_GROUP_MAP: EnumMap<PermissionGroupType, List<String>> = EnumMap(
        PermissionGroupType::class.java
    )

    /** 危险权限对应的类型集合  */
    private 
    val DANGEROUS_PERMISSION_GROUP_TYPE_MAP: HashMap<String, PermissionGroupType> = HashMap(25)

    /** 低等级权限列表（排序时放最后）  */
    private 
    val LOW_LEVEL_PERMISSION_LIST: MutableList<String> = ArrayList(3)

    /** 权限请求间隔时长  */
    private 
    val PERMISSIONS_REQUEST_INTERVAL_TIME: HashMap<String, Int> = HashMap(2)

    /** 权限结果等待时长  */
    private 
    val PERMISSIONS_RESULT_WAIT_TIME: HashMap<String, Int> = HashMap(25)

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


        /* ---------------------------------------------------------------------------------------------------- */
        PERMISSION_VERSION_MAP[Permission.SCHEDULE_EXACT_ALARM] = AndroidVersionTools.ANDROID_12
        PERMISSION_VERSION_MAP[Permission.MANAGE_EXTERNAL_STORAGE] = AndroidVersionTools.ANDROID_11
        PERMISSION_VERSION_MAP[Permission.REQUEST_INSTALL_PACKAGES] = AndroidVersionTools.ANDROID_8
        PERMISSION_VERSION_MAP[Permission.PICTURE_IN_PICTURE] = AndroidVersionTools.ANDROID_8


        // 虽然悬浮窗权限是在 Android 6.0 新增的权限，但是有些国产的厂商在 Android 6.0 之前的版本就自己加了，并且框架已经有做兼容了
        // 所以为了兼容更低的 Android 版本，这里需要将悬浮窗权限出现的 Android 版本成 API 17（即框架要求 minSdkVersion 版本）
        PERMISSION_VERSION_MAP[Permission.SYSTEM_ALERT_WINDOW] = AndroidVersionTools.ANDROID_4_2
        PERMISSION_VERSION_MAP[Permission.WRITE_SETTINGS] = AndroidVersionTools.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.ACCESS_NOTIFICATION_POLICY] =
            AndroidVersionTools.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS] =
            AndroidVersionTools.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.PACKAGE_USAGE_STATS] = AndroidVersionTools.ANDROID_5
        PERMISSION_VERSION_MAP[Permission.NOTIFICATION_SERVICE] = AndroidVersionTools.ANDROID_4_4
        PERMISSION_VERSION_MAP[Permission.BIND_NOTIFICATION_LISTENER_SERVICE] =
            AndroidVersionTools.ANDROID_4_3
        PERMISSION_VERSION_MAP[Permission.BIND_VPN_SERVICE] = AndroidVersionTools.ANDROID_4_0

        PERMISSION_VERSION_MAP[Permission.READ_MEDIA_VISUAL_USER_SELECTED] =
            AndroidVersionTools.ANDROID_14
        PERMISSION_VERSION_MAP[Permission.POST_NOTIFICATIONS] = AndroidVersionTools.ANDROID_13
        PERMISSION_VERSION_MAP[Permission.NEARBY_WIFI_DEVICES] = AndroidVersionTools.ANDROID_13
        PERMISSION_VERSION_MAP[Permission.BODY_SENSORS_BACKGROUND] = AndroidVersionTools.ANDROID_13
        PERMISSION_VERSION_MAP[Permission.READ_MEDIA_IMAGES] = AndroidVersionTools.ANDROID_13
        PERMISSION_VERSION_MAP[Permission.READ_MEDIA_VIDEO] = AndroidVersionTools.ANDROID_13
        PERMISSION_VERSION_MAP[Permission.READ_MEDIA_AUDIO] = AndroidVersionTools.ANDROID_13
        PERMISSION_VERSION_MAP[Permission.BLUETOOTH_SCAN] = AndroidVersionTools.ANDROID_12
        PERMISSION_VERSION_MAP[Permission.BLUETOOTH_CONNECT] = AndroidVersionTools.ANDROID_12
        PERMISSION_VERSION_MAP[Permission.BLUETOOTH_ADVERTISE] = AndroidVersionTools.ANDROID_12
        PERMISSION_VERSION_MAP[Permission.ACCESS_BACKGROUND_LOCATION] =
            AndroidVersionTools.ANDROID_10
        PERMISSION_VERSION_MAP[Permission.ACTIVITY_RECOGNITION] = AndroidVersionTools.ANDROID_10
        PERMISSION_VERSION_MAP[Permission.ACCESS_MEDIA_LOCATION] = AndroidVersionTools.ANDROID_10
        PERMISSION_VERSION_MAP[Permission.ACCEPT_HANDOVER] = AndroidVersionTools.ANDROID_9
        PERMISSION_VERSION_MAP[Permission.ANSWER_PHONE_CALLS] = AndroidVersionTools.ANDROID_8
        PERMISSION_VERSION_MAP[Permission.READ_PHONE_NUMBERS] = AndroidVersionTools.ANDROID_8
        PERMISSION_VERSION_MAP[Permission.GET_INSTALLED_APPS] = AndroidVersionTools.ANDROID_4_2
        PERMISSION_VERSION_MAP[Permission.READ_EXTERNAL_STORAGE] = AndroidVersionTools.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.WRITE_EXTERNAL_STORAGE] = AndroidVersionTools.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.CAMERA] = AndroidVersionTools.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.RECORD_AUDIO] = AndroidVersionTools.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.ACCESS_FINE_LOCATION] = AndroidVersionTools.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.ACCESS_COARSE_LOCATION] = AndroidVersionTools.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.READ_CONTACTS] = AndroidVersionTools.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.WRITE_CONTACTS] = AndroidVersionTools.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.GET_ACCOUNTS] = AndroidVersionTools.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.READ_CALENDAR] = AndroidVersionTools.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.WRITE_CALENDAR] = AndroidVersionTools.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.READ_PHONE_STATE] = AndroidVersionTools.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.CALL_PHONE] = AndroidVersionTools.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.READ_CALL_LOG] = AndroidVersionTools.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.WRITE_CALL_LOG] = AndroidVersionTools.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.ADD_VOICEMAIL] = AndroidVersionTools.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.USE_SIP] = AndroidVersionTools.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.PROCESS_OUTGOING_CALLS] = AndroidVersionTools.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.BODY_SENSORS] = AndroidVersionTools.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.SEND_SMS] = AndroidVersionTools.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.RECEIVE_SMS] = AndroidVersionTools.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.READ_SMS] = AndroidVersionTools.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.RECEIVE_WAP_PUSH] = AndroidVersionTools.ANDROID_6
        PERMISSION_VERSION_MAP[Permission.RECEIVE_MMS] = AndroidVersionTools.ANDROID_6


        /* ---------------------------------------------------------------------------------------------------- */
        VIRTUAL_PERMISSION_LIST.add(Permission.NOTIFICATION_SERVICE)
        VIRTUAL_PERMISSION_LIST.add(Permission.BIND_NOTIFICATION_LISTENER_SERVICE)
        VIRTUAL_PERMISSION_LIST.add(Permission.BIND_VPN_SERVICE)
        VIRTUAL_PERMISSION_LIST.add(Permission.PICTURE_IN_PICTURE)


        /* ---------------------------------------------------------------------------------------------------- */

        // Android 13 以下开启通知栏服务，需要用到旧的通知栏权限（框架自己虚拟出来的）
        NEW_AND_OLD_PERMISSION_MAP[Permission.POST_NOTIFICATIONS] =
            arrayOf(Permission.NOTIFICATION_SERVICE)

        // Android 13 以下使用 WIFI 功能需要用到精确定位的权限
        NEW_AND_OLD_PERMISSION_MAP[Permission.NEARBY_WIFI_DEVICES] =
            arrayOf(Permission.ACCESS_FINE_LOCATION)

        // Android 13 以下访问媒体文件需要用到读取外部存储的权限
        NEW_AND_OLD_PERMISSION_MAP[Permission.READ_MEDIA_IMAGES] =
            arrayOf(Permission.READ_EXTERNAL_STORAGE)
        NEW_AND_OLD_PERMISSION_MAP[Permission.READ_MEDIA_VIDEO] =
            arrayOf(Permission.READ_EXTERNAL_STORAGE)
        NEW_AND_OLD_PERMISSION_MAP[Permission.READ_MEDIA_AUDIO] =
            arrayOf(Permission.READ_EXTERNAL_STORAGE)

        // Android 12 以下扫描蓝牙需要精确定位权限
        NEW_AND_OLD_PERMISSION_MAP[Permission.BLUETOOTH_SCAN] =
            arrayOf(Permission.ACCESS_FINE_LOCATION)

        // Android 11 以下访问完整的文件管理需要用到读写外部存储的权限
        NEW_AND_OLD_PERMISSION_MAP[Permission.MANAGE_EXTERNAL_STORAGE] =
            arrayOf(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)

        // Android 8.0 以下读取电话号码需要用到读取电话状态的权限
        NEW_AND_OLD_PERMISSION_MAP[Permission.READ_PHONE_NUMBERS] =
            arrayOf(Permission.READ_PHONE_STATE)


        /* ---------------------------------------------------------------------------------------------------- */

        // 后台定位权限
        BACKGROUND_PERMISSION_MAP.put(
            Permission.ACCESS_BACKGROUND_LOCATION,
            PermissionUtils.asArrayList(
                Permission.ACCESS_FINE_LOCATION,
                Permission.ACCESS_COARSE_LOCATION
            )
        )

        // 后台传感器权限
        BACKGROUND_PERMISSION_MAP.put(
            Permission.BODY_SENSORS_BACKGROUND,
            PermissionUtils.asArrayList(Permission.BODY_SENSORS)
        )

        // 后台权限列表（先获取，后面的代码会用到）
        val backgroundPermissions: Set<String> = BACKGROUND_PERMISSION_MAP.keys


        /* ---------------------------------------------------------------------------------------------------- */

        // 存储权限组
        val storagePermissionGroup: List<String> = PermissionUtils.asArrayList(
            Permission.READ_EXTERNAL_STORAGE,
            Permission.WRITE_EXTERNAL_STORAGE
        )
        DANGEROUS_PERMISSION_GROUP_MAP[PermissionGroupType.STORAGE] = storagePermissionGroup
        for (permission in storagePermissionGroup) {
            DANGEROUS_PERMISSION_GROUP_TYPE_MAP[permission!!] = PermissionGroupType.STORAGE
        }

        // 日历权限组
        val calendarPermissionGroup: List<String> = PermissionUtils.asArrayList(
            Permission.READ_CALENDAR,
            Permission.WRITE_CALENDAR
        )
        DANGEROUS_PERMISSION_GROUP_MAP[PermissionGroupType.CALENDAR] = calendarPermissionGroup
        for (permission in calendarPermissionGroup) {
            DANGEROUS_PERMISSION_GROUP_TYPE_MAP[permission!!] = PermissionGroupType.CALENDAR
        }

        // 联系人权限组
        val contactsPermissionGroup: List<String> = PermissionUtils.asArrayList(
            Permission.READ_CONTACTS,
            Permission.WRITE_CONTACTS
        )
        DANGEROUS_PERMISSION_GROUP_MAP[PermissionGroupType.CONTACTS] = contactsPermissionGroup
        for (permission in contactsPermissionGroup) {
            DANGEROUS_PERMISSION_GROUP_TYPE_MAP[permission!!] = PermissionGroupType.CONTACTS
        }

        // 短信权限组
        val smsPermissionGroup: List<String> = PermissionUtils.asArrayList(
            Permission.SEND_SMS,
            Permission.READ_SMS,
            Permission.RECEIVE_SMS,
            Permission.RECEIVE_WAP_PUSH,
            Permission.RECEIVE_MMS
        )
        DANGEROUS_PERMISSION_GROUP_MAP[PermissionGroupType.SMS] = smsPermissionGroup
        for (permission in smsPermissionGroup) {
            DANGEROUS_PERMISSION_GROUP_TYPE_MAP[permission!!] = PermissionGroupType.SMS
        }

        // 定位权限组
        val locationPermissionGroup: MutableList<String> = PermissionUtils.asArrayList(
            Permission.ACCESS_COARSE_LOCATION,
            Permission.ACCESS_FINE_LOCATION,
            Permission.ACCESS_BACKGROUND_LOCATION
        )

        // 蓝牙相关的权限组
        val bluetoothPermissions: List<String> = PermissionUtils.asArrayList(
            Permission.BLUETOOTH_SCAN,
            Permission.BLUETOOTH_CONNECT,
            Permission.BLUETOOTH_ADVERTISE
        )


        // WIFI 相关的权限组
        val wifiPermission = Permission.NEARBY_WIFI_DEVICES


        // 附近设备权限
        val nearbyDevicesPermissionGroup: MutableList<String> = if (AndroidVersionTools.isAndroid13()) {
            ArrayList<String>(bluetoothPermissions.size + 1)
        } else {
            ArrayList<String>(bluetoothPermissions.size)
        }


        // 注意：在 Android 12 的时候，蓝牙相关的权限已经归到附近设备的权限组了，但是在 Android 12 之前，蓝牙相关的权限归属定位权限组
        if (isAndroid12()) {
            nearbyDevicesPermissionGroup.addAll(bluetoothPermissions)
        } else {
            locationPermissionGroup.addAll(bluetoothPermissions)
        }


        // 注意：在 Android 13 的时候，WIFI 相关的权限已经归到附近设备的权限组了，但是在 Android 13 之前，WIFI 相关的权限归属定位权限组
        if (isAndroid13()) {
            nearbyDevicesPermissionGroup.add(wifiPermission)
        } else {
            locationPermissionGroup.add(wifiPermission)
        }

        if (!nearbyDevicesPermissionGroup.isEmpty()) {
            DANGEROUS_PERMISSION_GROUP_MAP[PermissionGroupType.NEARBY_DEVICES] =
                nearbyDevicesPermissionGroup
            for (permission in nearbyDevicesPermissionGroup) {
                DANGEROUS_PERMISSION_GROUP_TYPE_MAP[permission!!] =
                    PermissionGroupType.NEARBY_DEVICES
            }
        }

        DANGEROUS_PERMISSION_GROUP_MAP[PermissionGroupType.LOCATION] = locationPermissionGroup
        for (permission in locationPermissionGroup) {
            DANGEROUS_PERMISSION_GROUP_TYPE_MAP[permission!!] = PermissionGroupType.LOCATION
        }


        // 传感器权限组
        val sensorsPermissionGroup: List<String> = PermissionUtils.asArrayList(
            Permission.BODY_SENSORS,
            Permission.BODY_SENSORS_BACKGROUND
        )
        DANGEROUS_PERMISSION_GROUP_MAP[PermissionGroupType.SENSORS] = sensorsPermissionGroup
        for (permission in sensorsPermissionGroup) {
            DANGEROUS_PERMISSION_GROUP_TYPE_MAP[permission!!] = PermissionGroupType.SENSORS
        }

        // 电话权限组和通话记录权限组
        val phonePermissionGroup: List<String> = PermissionUtils.asArrayList(
            Permission.READ_PHONE_STATE,
            Permission.CALL_PHONE,
            Permission.ADD_VOICEMAIL,
            Permission.USE_SIP,
            Permission.READ_PHONE_NUMBERS,
            Permission.ANSWER_PHONE_CALLS,
            Permission.ACCEPT_HANDOVER
        )
        val callLogPermissionGroup: List<String> = PermissionUtils.asArrayList(
            Permission.READ_CALL_LOG,
            Permission.WRITE_CALL_LOG,
            Permission.PROCESS_OUTGOING_CALLS
        )


        // 注意：在 Android 9.0 的时候，读写通话记录权限已经归到一个单独的权限组了，但是在 Android 9.0 之前，读写通话记录权限归属电话权限组
        if (isAndroid9()) {
            DANGEROUS_PERMISSION_GROUP_MAP[PermissionGroupType.PHONE] = phonePermissionGroup
            for (permission in phonePermissionGroup) {
                DANGEROUS_PERMISSION_GROUP_TYPE_MAP[permission!!] = PermissionGroupType.PHONE
            }
            DANGEROUS_PERMISSION_GROUP_MAP[PermissionGroupType.CALL_LOG] = callLogPermissionGroup
            for (permission in callLogPermissionGroup) {
                DANGEROUS_PERMISSION_GROUP_TYPE_MAP[permission!!] = PermissionGroupType.CALL_LOG
            }
        } else {
            val oldPhonePermissionGroup: MutableList<String> = ArrayList()
            oldPhonePermissionGroup.addAll(phonePermissionGroup)
            oldPhonePermissionGroup.addAll(callLogPermissionGroup)
            DANGEROUS_PERMISSION_GROUP_MAP[PermissionGroupType.PHONE] = oldPhonePermissionGroup

            for (permission in oldPhonePermissionGroup) {
                DANGEROUS_PERMISSION_GROUP_TYPE_MAP[permission!!] = PermissionGroupType.PHONE
            }
        }


        // 读取照片和视频媒体文件权限组
        val imageAndVideoPermissionGroup: List<String> = PermissionUtils.asArrayList(
            Permission.READ_MEDIA_IMAGES,
            Permission.READ_MEDIA_VIDEO,
            Permission.READ_MEDIA_VISUAL_USER_SELECTED
        )
        DANGEROUS_PERMISSION_GROUP_MAP[PermissionGroupType.IMAGE_AND_VIDEO_MEDIA] =
            imageAndVideoPermissionGroup
        for (permission in imageAndVideoPermissionGroup) {
            DANGEROUS_PERMISSION_GROUP_TYPE_MAP[permission!!] =
                PermissionGroupType.IMAGE_AND_VIDEO_MEDIA
        }


        /* ---------------------------------------------------------------------------------------------------- */

        // 将后台权限定义为低等级权限
        LOW_LEVEL_PERMISSION_LIST.addAll(BACKGROUND_PERMISSION_MAP.keys)

        // 将读取图片位置权限定义为低等级权限
        LOW_LEVEL_PERMISSION_LIST.add(Permission.ACCESS_MEDIA_LOCATION)


        /* ---------------------------------------------------------------------------------------------------- */

        // 设置权限请求间隔时间
        for (permission in backgroundPermissions) {
            if (getCurrentAndroidVersionCode() < findAndroidVersionByPermission(
                    permission!!
                )
            ) {
                continue
            }
            // 经过测试，在 Android 13 设备上面，先申请前台权限，然后立马申请后台权限大概率会出现失败
            // 这里为了避免这种情况出现，所以加了一点延迟，这样就没有什么问题了
            // 为什么延迟时间是 150 毫秒？ 经过实践得出 100 还是有概率会出现失败，但是换成 150 试了很多次就都没有问题了
            PERMISSIONS_REQUEST_INTERVAL_TIME.put(permission, 150)
        }


        /* ---------------------------------------------------------------------------------------------------- */

        // 设置权限回调等待的时间
        var normalSpecialPermissionWaitTime: Int
        normalSpecialPermissionWaitTime = if (isAndroid11()) {
            200
        } else {
            300
        }

        if (isEmui() || isHarmonyOs()) {
            // 需要加长时间等待，不然某些华为机型授权了但是获取不到权限
            normalSpecialPermissionWaitTime = if (isAndroid8()) {
                300
            } else {
                500
            }
        }

        for (permission in SPECIAL_PERMISSION_LIST) {
            // 特殊权限一律需要一定的等待时间
            if (getCurrentAndroidVersionCode() < findAndroidVersionByPermission(
                    permission!!
                )
            ) {
                continue
            }
            PERMISSIONS_RESULT_WAIT_TIME.put(permission, normalSpecialPermissionWaitTime)
        }

        if (isMiui() && isAndroid11() && getCurrentAndroidVersionCode() >= findAndroidVersionByPermission(
                Permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            )
        ) {
            // 经过测试，发现小米 Android 11 及以上的版本，申请这个权限需要 1000 毫秒才能判断到（测试了 800 毫秒还不行）
            // 因为在 Android 10 的时候，这个特殊权限弹出的页面小米还是用谷歌原生的
            // 然而在 Android 11 之后的，这个权限页面被小米改成了自己定制化的页面
            // 测试了原生的模拟器和 vivo 云测并发现没有这个问题，所以断定这个 Bug 就是小米特有的
            PERMISSIONS_RESULT_WAIT_TIME.put(Permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, 1000)
        }
    }

    /**
     * 判断某个权限是否是特殊权限
     */
    fun isSpecialPermission( permission: String?): Boolean {
        return containsPermission(SPECIAL_PERMISSION_LIST, permission)
    }

    /**
     * 获取权限是从哪个 Android 版本新增的
     */
    fun findAndroidVersionByPermission( permission: String?): Int {
        val androidVersion = PERMISSION_VERSION_MAP[permission] ?: return 0
        return androidVersion
    }

    /**
     * 判断权限是否为框架自己虚拟出来的
     */
    fun isVirtualPermission( permission: String?): Boolean {
        return containsPermission(VIRTUAL_PERMISSION_LIST, permission!!)
    }

    /**
     * 通过新权限查询到对应的旧权限
     */

    fun queryOldPermissionByNewPermission( permission: String): Array<String>? {
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

    fun getDangerousPermissionGroup( permissionsGroupType: PermissionGroupType): List<String>? {
        return DANGEROUS_PERMISSION_GROUP_MAP[permissionsGroupType]
    }

    /**
     * 判断某个权限是否为后台权限
     */
    fun isBackgroundPermission(permission: String?): Boolean {
        return BACKGROUND_PERMISSION_MAP.containsKey(permission)
    }

    /**
     * 根据后台权限获得前台权限
     */

    fun queryForegroundPermissionByBackgroundPermission(permission: String): List<String>? {
        return BACKGROUND_PERMISSION_MAP[permission]
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

    /**
     * 获取低等级权限列表
     */
    
    fun getLowLevelPermissions(): List<String> {
        return LOW_LEVEL_PERMISSION_LIST
    }

    /**
     * 通过权限请求间隔时间
     */
    fun getMaxIntervalTimeByPermissions( permissions: List<String?>): Int {
        var maxWaitTime = 0
        for (permission in permissions) {
            val time = PERMISSIONS_REQUEST_INTERVAL_TIME[permission] ?: continue
            maxWaitTime = max(maxWaitTime.toDouble(), time.toDouble()).toInt()
        }
        return maxWaitTime
    }

    /**
     * 通过权限集合获取最大的回调等待时间
     */
    fun getMaxWaitTimeByPermissions(permissions: List<String>): Int {
        var maxWaitTime = 0
        for (permission in permissions) {
            val time = PERMISSIONS_RESULT_WAIT_TIME[permission] ?: continue
            maxWaitTime = max(maxWaitTime.toDouble(), time.toDouble()).toInt()
        }
        return maxWaitTime
    }
}