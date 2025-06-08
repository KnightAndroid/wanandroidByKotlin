package com.knight.kotlin.library_permiss


import android.content.Context
import android.os.Build
import android.text.TextUtils
import com.knight.kotlin.library_permiss.permissions.Permission


/**
 * @Description
 * @Author knight
 * @Time 2025/6/8 22:29
 *
 */

internal object PermissionConverter {
    /** 权限名称映射（为了适配多语种，这里存储的是 StringId，而不是 String）  */
    private val PERMISSION_NAME_MAP: MutableMap<String, Int> = HashMap()

    /** 权限描述映射（为了适配多语种，这里存储的是 StringId，而不是 String）  */
    private val PERMISSION_DESCRIPTION_MAP: MutableMap<Int, Int> = HashMap()

    init {
        PERMISSION_NAME_MAP[Permission.READ_EXTERNAL_STORAGE] = R.string.permission_storage
        PERMISSION_NAME_MAP[Permission.WRITE_EXTERNAL_STORAGE] = R.string.permission_storage
        PERMISSION_DESCRIPTION_MAP[R.string.permission_storage] =
            R.string.permission_description_demo

        PERMISSION_NAME_MAP[Permission.READ_MEDIA_IMAGES] =
            R.string.permission_image_and_video
        PERMISSION_NAME_MAP[Permission.READ_MEDIA_VIDEO] =
            R.string.permission_image_and_video
        PERMISSION_NAME_MAP[Permission.READ_MEDIA_VISUAL_USER_SELECTED] =
            R.string.permission_image_and_video
        PERMISSION_DESCRIPTION_MAP[R.string.permission_image_and_video] =
            R.string.permission_description_demo

        PERMISSION_NAME_MAP[Permission.READ_MEDIA_AUDIO] =
            R.string.permission_music_and_audio
        PERMISSION_DESCRIPTION_MAP[R.string.permission_music_and_audio] =
            R.string.permission_description_demo

        PERMISSION_NAME_MAP[Permission.CAMERA] = R.string.permission_camera
        PERMISSION_DESCRIPTION_MAP[R.string.permission_camera] =
            R.string.permission_description_demo

        PERMISSION_NAME_MAP[Permission.RECORD_AUDIO] = R.string.permission_microphone
        PERMISSION_DESCRIPTION_MAP[R.string.permission_microphone] =
            R.string.permission_description_demo

        PERMISSION_NAME_MAP[Permission.ACCESS_FINE_LOCATION] = R.string.permission_location
        PERMISSION_NAME_MAP[Permission.ACCESS_COARSE_LOCATION] = R.string.permission_location

        // 注意：在 Android 12 的时候，蓝牙相关的权限已经归到附近设备的权限组了，但是在 Android 12 之前，蓝牙相关的权限归属定位权限组
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PERMISSION_NAME_MAP[Permission.BLUETOOTH_SCAN] =
                R.string.permission_nearby_devices
            PERMISSION_NAME_MAP[Permission.BLUETOOTH_CONNECT] =
                R.string.permission_nearby_devices
            PERMISSION_NAME_MAP[Permission.BLUETOOTH_ADVERTISE] =
                R.string.permission_nearby_devices
            // 注意：在 Android 13 的时候，WIFI 相关的权限已经归到附近设备的权限组了，但是在 Android 13 之前，WIFI 相关的权限归属定位权限组
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                PERMISSION_NAME_MAP[Permission.NEARBY_WIFI_DEVICES] =
                    R.string.permission_nearby_devices
                PERMISSION_DESCRIPTION_MAP[R.string.permission_nearby_devices] =
                    R.string.permission_description_demo
                PERMISSION_DESCRIPTION_MAP[R.string.permission_location] =
                    R.string.permission_description_demo
            } else {
                PERMISSION_NAME_MAP[Permission.NEARBY_WIFI_DEVICES] =
                    R.string.permission_location
                PERMISSION_DESCRIPTION_MAP[R.string.permission_nearby_devices] =
                    R.string.permission_description_demo
                PERMISSION_DESCRIPTION_MAP[R.string.permission_location] =
                    R.string.permission_description_demo
            }
        } else {
            PERMISSION_NAME_MAP[Permission.BLUETOOTH_SCAN] = R.string.permission_location
            PERMISSION_NAME_MAP[Permission.BLUETOOTH_CONNECT] = R.string.permission_location
            PERMISSION_NAME_MAP[Permission.BLUETOOTH_ADVERTISE] =
                R.string.permission_location
            PERMISSION_NAME_MAP[Permission.NEARBY_WIFI_DEVICES] =
                R.string.permission_location
            PERMISSION_DESCRIPTION_MAP[R.string.permission_location] =
                R.string.permission_description_demo
        }

        PERMISSION_NAME_MAP[Permission.ACCESS_BACKGROUND_LOCATION] =
            R.string.permission_location_background
        PERMISSION_DESCRIPTION_MAP[R.string.permission_location_background] =
            R.string.permission_description_demo

        PERMISSION_NAME_MAP[Permission.BODY_SENSORS] = R.string.permission_body_sensors
        PERMISSION_DESCRIPTION_MAP[R.string.permission_body_sensors] =
            R.string.permission_description_demo

        PERMISSION_NAME_MAP[Permission.BODY_SENSORS_BACKGROUND] =
            R.string.permission_body_sensors_background
        PERMISSION_DESCRIPTION_MAP[R.string.permission_body_sensors_background] =
            R.string.permission_description_demo

        PERMISSION_NAME_MAP[Permission.READ_PHONE_STATE] = R.string.permission_phone
        PERMISSION_NAME_MAP[Permission.CALL_PHONE] = R.string.permission_phone
        PERMISSION_NAME_MAP[Permission.ADD_VOICEMAIL] = R.string.permission_phone
        PERMISSION_NAME_MAP[Permission.USE_SIP] = R.string.permission_phone
        PERMISSION_NAME_MAP[Permission.READ_PHONE_NUMBERS] = R.string.permission_phone
        PERMISSION_NAME_MAP[Permission.ANSWER_PHONE_CALLS] = R.string.permission_phone
        PERMISSION_NAME_MAP[Permission.ACCEPT_HANDOVER] = R.string.permission_phone
        // 注意：在 Android 9.0 的时候，读写通话记录权限已经归到一个单独的权限组了，但是在 Android 9.0 之前，读写通话记录权限归属电话权限组
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            PERMISSION_NAME_MAP[Permission.READ_CALL_LOG] = R.string.permission_call_logs
            PERMISSION_NAME_MAP[Permission.WRITE_CALL_LOG] = R.string.permission_call_logs
            PERMISSION_NAME_MAP[Permission.PROCESS_OUTGOING_CALLS] =
                R.string.permission_call_logs
            PERMISSION_DESCRIPTION_MAP[R.string.permission_call_logs] =
                R.string.permission_description_demo
            PERMISSION_DESCRIPTION_MAP[R.string.permission_phone] =
                R.string.permission_description_demo
        } else {
            PERMISSION_NAME_MAP[Permission.READ_CALL_LOG] = R.string.permission_phone
            PERMISSION_NAME_MAP[Permission.WRITE_CALL_LOG] = R.string.permission_phone
            PERMISSION_NAME_MAP[Permission.PROCESS_OUTGOING_CALLS] =
                R.string.permission_phone
            // 需要注意：这里的电话权限需要补充一下前面三个通话记录权限的用途
            PERMISSION_DESCRIPTION_MAP[R.string.permission_phone] =
                R.string.permission_description_demo
        }

        PERMISSION_NAME_MAP[Permission.GET_ACCOUNTS] = R.string.permission_contacts
        PERMISSION_NAME_MAP[Permission.READ_CONTACTS] = R.string.permission_contacts
        PERMISSION_NAME_MAP[Permission.WRITE_CONTACTS] = R.string.permission_contacts
        PERMISSION_DESCRIPTION_MAP[R.string.permission_contacts] =
            R.string.permission_description_demo

        PERMISSION_NAME_MAP[Permission.READ_CALENDAR] = R.string.permission_calendar
        PERMISSION_NAME_MAP[Permission.WRITE_CALENDAR] = R.string.permission_calendar
        PERMISSION_DESCRIPTION_MAP[R.string.permission_calendar] =
            R.string.permission_description_demo

        // 注意：在 Android 10 的版本，这个权限的名称为《健身运动权限》，但是到了 Android 11 的时候，这个权限的名称被修改成了《身体活动权限》
        // 没错就改了一下权限的叫法，其他的一切没有变，Google 产品经理真的是闲的蛋疼，但是吐槽归吐槽，框架也要灵活应对一下，避免小白用户跳转到设置页找不到对应的选项
        val activityRecognitionPermissionNameStringId: Int =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) R.string.permission_activity_recognition_api30 else R.string.permission_activity_recognition_api29
        PERMISSION_NAME_MAP[Permission.ACTIVITY_RECOGNITION] =
            activityRecognitionPermissionNameStringId
        PERMISSION_DESCRIPTION_MAP[activityRecognitionPermissionNameStringId] =
            R.string.permission_description_demo

        PERMISSION_NAME_MAP[Permission.ACCESS_MEDIA_LOCATION] =
            R.string.permission_access_media_location
        PERMISSION_DESCRIPTION_MAP[R.string.permission_access_media_location] =
            R.string.permission_description_demo

        PERMISSION_NAME_MAP[Permission.SEND_SMS] = R.string.permission_sms
        PERMISSION_NAME_MAP[Permission.RECEIVE_SMS] = R.string.permission_sms
        PERMISSION_NAME_MAP[Permission.READ_SMS] = R.string.permission_sms
        PERMISSION_NAME_MAP[Permission.RECEIVE_WAP_PUSH] = R.string.permission_sms
        PERMISSION_NAME_MAP[Permission.RECEIVE_MMS] = R.string.permission_sms
        PERMISSION_DESCRIPTION_MAP[R.string.permission_sms] =
            R.string.permission_description_demo

        PERMISSION_NAME_MAP[Permission.GET_INSTALLED_APPS] =
            R.string.permission_get_installed_apps
        PERMISSION_DESCRIPTION_MAP[R.string.permission_get_installed_apps] =
            R.string.permission_description_demo

        PERMISSION_NAME_MAP[Permission.MANAGE_EXTERNAL_STORAGE] =
            R.string.permission_all_file_access
        PERMISSION_DESCRIPTION_MAP[R.string.permission_all_file_access] =
            R.string.permission_description_demo

        PERMISSION_NAME_MAP[Permission.REQUEST_INSTALL_PACKAGES] =
            R.string.permission_install_unknown_apps
        PERMISSION_DESCRIPTION_MAP[R.string.permission_install_unknown_apps] =
            R.string.permission_description_demo

        PERMISSION_NAME_MAP[Permission.SYSTEM_ALERT_WINDOW] =
            R.string.permission_display_over_other_apps
        PERMISSION_DESCRIPTION_MAP[R.string.permission_display_over_other_apps] =
            R.string.permission_description_demo

        PERMISSION_NAME_MAP[Permission.WRITE_SETTINGS] =
            R.string.permission_modify_system_settings
        PERMISSION_DESCRIPTION_MAP[R.string.permission_modify_system_settings] =
            R.string.permission_description_demo

        PERMISSION_NAME_MAP[Permission.NOTIFICATION_SERVICE] =
            R.string.permission_allow_notifications
        PERMISSION_DESCRIPTION_MAP[R.string.permission_allow_notifications] =
            R.string.permission_description_demo

        PERMISSION_NAME_MAP[Permission.POST_NOTIFICATIONS] =
            R.string.permission_post_notifications
        PERMISSION_DESCRIPTION_MAP[R.string.permission_post_notifications] =
            R.string.permission_description_demo

        PERMISSION_NAME_MAP[Permission.BIND_NOTIFICATION_LISTENER_SERVICE] =
            R.string.permission_allow_notifications_access
        PERMISSION_DESCRIPTION_MAP[R.string.permission_allow_notifications_access] =
            R.string.permission_description_demo

        PERMISSION_NAME_MAP[Permission.PACKAGE_USAGE_STATS] =
            R.string.permission_apps_with_usage_access
        PERMISSION_DESCRIPTION_MAP[R.string.permission_apps_with_usage_access] =
            R.string.permission_description_demo

        PERMISSION_NAME_MAP[Permission.SCHEDULE_EXACT_ALARM] =
            R.string.permission_alarms_reminders
        PERMISSION_DESCRIPTION_MAP[R.string.permission_alarms_reminders] =
            R.string.permission_description_demo

        PERMISSION_NAME_MAP[Permission.ACCESS_NOTIFICATION_POLICY] =
            R.string.permission_do_not_disturb_access
        PERMISSION_DESCRIPTION_MAP[R.string.permission_do_not_disturb_access] =
            R.string.permission_description_demo

        PERMISSION_NAME_MAP[Permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS] =
            R.string.permission_ignore_battery_optimize
        PERMISSION_DESCRIPTION_MAP[R.string.permission_ignore_battery_optimize] =
            R.string.permission_description_demo

        PERMISSION_NAME_MAP[Permission.BIND_VPN_SERVICE] = R.string.permission_vpn
        PERMISSION_DESCRIPTION_MAP[R.string.permission_vpn] =
            R.string.permission_description_demo

        PERMISSION_NAME_MAP[Permission.PICTURE_IN_PICTURE] =
            R.string.permission_picture_in_picture
        PERMISSION_DESCRIPTION_MAP[R.string.permission_picture_in_picture] =
            R.string.permission_description_demo
    }

    /**
     * 通过权限获得名称
     */
    
    fun getNamesByPermissions(
         context: Context,
         permissions: List<String>
    ): String {
        val permissionNameList = getNameListByPermissions(context, permissions, true)

        val builder = StringBuilder()
        for (permissionName in permissionNameList) {
            if (TextUtils.isEmpty(permissionName)) {
                continue
            }
            if (builder.length == 0) {
                builder.append(permissionName)
            } else {
                builder.append(context.getString(R.string.permission_comma))
                    .append(permissionName)
            }
        }
        if (builder.length == 0) {
            // 如果没有获得到任何信息，则返回一个默认的文本
            return context.getString(R.string.permission_unknown)
        }
        return builder.toString()
    }

    
    fun getNameListByPermissions(
         context: Context,
         permissions: List<String>,
        filterHighVersionPermissions: Boolean
    ): List<String> {
        val permissionNameList: MutableList<String> = ArrayList()
        for (permission in permissions) {
            // 如果当前设置了过滤高版本权限，并且这个权限是高版本系统才出现的权限，则不继续往下执行
            // 避免出现在低版本上面执行拒绝权限后，连带高版本的名称也一起显示出来，但是在低版本上面是没有这个权限的
            if (filterHighVersionPermissions && XXPermissions.isHighVersionPermission(permission)) {
                continue
            }
            val permissionName = getNameByPermission(context, permission)
            if (TextUtils.isEmpty(permissionName)) {
                continue
            }
            if (permissionNameList.contains(permissionName)) {
                continue
            }
            permissionNameList.add(permissionName)
        }
        return permissionNameList
    }

    fun getNameByPermission( context: Context,  permission: String): String {
        val permissionNameStringId = PERMISSION_NAME_MAP[permission]
        if (permissionNameStringId == null || permissionNameStringId == 0) {
            return ""
        }
        return context.getString(permissionNameStringId)
    }

    /**
     * 通过权限获得描述
     */
    
    fun getDescriptionsByPermissions(
         context: Context,
         permissions: List<String?>
    ): String {
        val descriptionList = getDescriptionListByPermissions(context, permissions)

        val builder = StringBuilder()
        for (description in descriptionList) {
            if (TextUtils.isEmpty(description)) {
                continue
            }
            if (builder.length == 0) {
                builder.append(description)
            } else {
                builder.append("\n")
                    .append(description)
            }
        }
        return builder.toString()
    }

    
    fun getDescriptionListByPermissions(
         context: Context,
         permissions: List<String?>
    ): List<String> {
        val descriptionList: MutableList<String> = ArrayList()
        for (permission in permissions) {
            val permissionDescription = getDescriptionByPermission(context, permission)
            if (TextUtils.isEmpty(permissionDescription)) {
                continue
            }
            if (descriptionList.contains(permissionDescription)) {
                continue
            }
            descriptionList.add(permissionDescription)
        }
        return descriptionList
    }

    /**
     * 通过权限获得描述
     */
    
    fun getDescriptionByPermission( context: Context,  permission: String?): String {
        val permissionNameStringId = PERMISSION_NAME_MAP[permission]
        if (permissionNameStringId == null || permissionNameStringId == 0) {
            return ""
        }
        val permissionName = context.getString(permissionNameStringId)
        val permissionDescriptionStringId = PERMISSION_DESCRIPTION_MAP[permissionNameStringId]
        val permissionDescription =
            if (permissionDescriptionStringId == null || permissionDescriptionStringId == 0) {
                ""
            } else {
                context.getString(permissionDescriptionStringId)
            }
        return permissionName + context.getString(R.string.permission_colon) + permissionDescription
    }
}