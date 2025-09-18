package com.knight.kotlin.library_permiss






import android.content.Context
import android.os.Build
import android.text.TextUtils
import androidx.annotation.IdRes
import com.knight.kotlin.library_permiss.permission.PermissionGroups
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.base.IPermission


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

    init
    {
        PERMISSION_NAME_MAP[PermissionGroups.STORAGE] = R.string.permission_storage
        PERMISSION_DESCRIPTION_MAP[R.string.permission_storage] = R.string.permission_storage_description

        PERMISSION_NAME_MAP[PermissionGroups.IMAGE_AND_VIDEO_MEDIA] = R.string.permission_image_and_video
        PERMISSION_DESCRIPTION_MAP[R.string.permission_image_and_video] = R.string.permission_image_and_video_description

        PERMISSION_NAME_MAP[PermissionNames.READ_MEDIA_AUDIO] = R.string.permission_music_and_audio
        PERMISSION_DESCRIPTION_MAP[R.string.permission_music_and_audio] = R.string.permission_music_and_audio_description

        PERMISSION_NAME_MAP[PermissionNames.CAMERA] = R.string.permission_camera
        PERMISSION_DESCRIPTION_MAP[R.string.permission_camera] = R.string.permission_camera_description

        PERMISSION_NAME_MAP[PermissionNames.RECORD_AUDIO] = R.string.permission_microphone
        PERMISSION_DESCRIPTION_MAP[R.string.permission_microphone] = R.string.permission_microphone_description

        PERMISSION_NAME_MAP[PermissionGroups.NEARBY_DEVICES] = R.string.permission_nearby_devices

        // 注意：在 Android 13 的时候，WIFI 相关的权限已经归到附近设备的权限组了，但是在 Android 13 之前，WIFI 相关的权限归属定位权限组
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // 需要填充文案：蓝牙权限描述 + WIFI 权限描述
            PERMISSION_DESCRIPTION_MAP[R.string.permission_nearby_devices] = R.string.permission_nearby_devices_description
        } else {
            // 需要填充文案：蓝牙权限描述
            PERMISSION_DESCRIPTION_MAP[R.string.permission_nearby_devices] = R.string.permission_nearby_devices_description
        }

        PERMISSION_NAME_MAP[PermissionGroups.LOCATION] = R.string.permission_location


        // 注意：在 Android 12 的时候，蓝牙相关的权限已经归到附近设备的权限组了，但是在 Android 12 之前，蓝牙相关的权限归属定位权限组
        // 注意：在 Android 13 的时候，WIFI 相关的权限已经归到附近设备的权限组了，但是在 Android 13 之前，WIFI 相关的权限归属定位权限组
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // 需要填充文案：前台定位权限描述
            PERMISSION_DESCRIPTION_MAP[R.string.permission_location] = R.string.permission_location_description
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // 需要填充文案：前台定位权限描述 + WIFI 权限描述
            PERMISSION_DESCRIPTION_MAP[R.string.permission_location] = R.string.permission_location_description
        } else {
            // 需要填充文案：前台定位权限描述 + 蓝牙权限描述 + WIFI 权限描述
            PERMISSION_DESCRIPTION_MAP[R.string.permission_location] = R.string.permission_location_description
        }


        // 后台定位权限虽然属于定位权限组，但是只要是属于后台权限，都有独属于自己的一套规则
        PERMISSION_NAME_MAP[PermissionNames.ACCESS_BACKGROUND_LOCATION] = R.string.permission_location_background
        PERMISSION_DESCRIPTION_MAP[R.string.permission_location_background] = R.string.permission_location_background_description

        val sensorsPermissionNameStringId: Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA) {
            sensorsPermissionNameStringId = R.string.permission_health_data
        } else {
            sensorsPermissionNameStringId = R.string.permission_body_sensors
        }
        PERMISSION_NAME_MAP[PermissionGroups.SENSORS] = sensorsPermissionNameStringId
        PERMISSION_DESCRIPTION_MAP[sensorsPermissionNameStringId] = R.string.permission_body_sensors_description


        // 后台传感器权限虽然属于传感器权限组，但是只要是属于后台权限，都有独属于自己的一套规则
        val bodySensorsBackgroundPermissionNameStringId: Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA) {
            bodySensorsBackgroundPermissionNameStringId = R.string.permission_health_data_background
        } else {
            bodySensorsBackgroundPermissionNameStringId = R.string.permission_body_sensors_background
        }
        PERMISSION_NAME_MAP[PermissionNames.BODY_SENSORS_BACKGROUND] = bodySensorsBackgroundPermissionNameStringId
        PERMISSION_DESCRIPTION_MAP[bodySensorsBackgroundPermissionNameStringId] = R.string.permission_body_sensors_background_description


        // Android 16 这个版本开始，传感器权限被进行了精细化拆分，拆分成了无数个健康权限
        PERMISSION_NAME_MAP[PermissionGroups.HEALTH] = R.string.permission_health_data
        PERMISSION_DESCRIPTION_MAP[R.string.permission_health_data] = R.string.permission_health_data_description

        PERMISSION_NAME_MAP[PermissionNames.READ_HEALTH_DATA_IN_BACKGROUND] = R.string.permission_health_data_background
        PERMISSION_DESCRIPTION_MAP[R.string.permission_health_data_background] = R.string.permission_health_data_background_description

        PERMISSION_NAME_MAP[PermissionNames.READ_HEALTH_DATA_HISTORY] = R.string.permission_health_data_past
        PERMISSION_DESCRIPTION_MAP[R.string.permission_health_data_past] = R.string.permission_health_data_past_description

        PERMISSION_NAME_MAP[PermissionGroups.CALL_LOG] = R.string.permission_call_logs
        PERMISSION_DESCRIPTION_MAP[R.string.permission_call_logs] = R.string.permission_call_logs_description

        PERMISSION_NAME_MAP[PermissionGroups.PHONE] = R.string.permission_phone

        // 注意：在 Android 9.0 的时候，读写通话记录权限已经归到一个单独的权限组了，但是在 Android 9.0 之前，读写通话记录权限归属电话权限组
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // 需要填充文案：电话权限描述
            PERMISSION_DESCRIPTION_MAP[R.string.permission_phone] = R.string.permission_phone_description
        } else {
            // 需要填充文案：电话权限描述 + 通话记录权限描述
            PERMISSION_DESCRIPTION_MAP[R.string.permission_phone] = R.string.permission_phone_description
        }

        PERMISSION_NAME_MAP[PermissionGroups.CONTACTS] = R.string.permission_contacts
        PERMISSION_DESCRIPTION_MAP[R.string.permission_contacts] = R.string.permission_contacts_description

        PERMISSION_NAME_MAP[PermissionGroups.CALENDAR] = R.string.permission_calendar
        PERMISSION_DESCRIPTION_MAP[R.string.permission_calendar] = R.string.permission_calendar_description


        // 注意：在 Android 10 的版本，这个权限的名称为《健身运动权限》，但是到了 Android 11 的时候，这个权限的名称被修改成了《身体活动权限》
        // 没错就改了一下权限的叫法，其他的一切没有变，Google 产品经理真的是闲的蛋疼，但是吐槽归吐槽，框架也要灵活应对一下，避免小白用户跳转到设置页找不到对应的选项
        val activityRecognitionPermissionNameStringId: Int =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) R.string.permission_activity_recognition_api30 else R.string.permission_activity_recognition_api29
        PERMISSION_NAME_MAP[PermissionNames.ACTIVITY_RECOGNITION] = activityRecognitionPermissionNameStringId
        PERMISSION_DESCRIPTION_MAP[activityRecognitionPermissionNameStringId] = R.string.permission_activity_recognition_description

        PERMISSION_NAME_MAP[PermissionNames.ACCESS_MEDIA_LOCATION] = R.string.permission_access_media_location_information
        PERMISSION_DESCRIPTION_MAP[R.string.permission_access_media_location_information] =
            R.string.permission_access_media_location_information_description

        PERMISSION_NAME_MAP[PermissionGroups.SMS] = R.string.permission_sms
        PERMISSION_DESCRIPTION_MAP[R.string.permission_sms] = R.string.permission_sms_description

        PERMISSION_NAME_MAP[PermissionNames.GET_INSTALLED_APPS] = R.string.permission_get_installed_apps
        PERMISSION_DESCRIPTION_MAP[R.string.permission_get_installed_apps] = R.string.permission_get_installed_apps_description

        PERMISSION_NAME_MAP[PermissionNames.MANAGE_EXTERNAL_STORAGE] = R.string.permission_all_file_access
        PERMISSION_DESCRIPTION_MAP[R.string.permission_all_file_access] = R.string.permission_all_file_access_description

        PERMISSION_NAME_MAP[PermissionNames.REQUEST_INSTALL_PACKAGES] = R.string.permission_install_unknown_apps
        PERMISSION_DESCRIPTION_MAP[R.string.permission_install_unknown_apps] = R.string.permission_install_unknown_apps_description

        PERMISSION_NAME_MAP[PermissionNames.SYSTEM_ALERT_WINDOW] = R.string.permission_display_over_other_apps
        PERMISSION_DESCRIPTION_MAP[R.string.permission_display_over_other_apps] = R.string.permission_display_over_other_apps_description

        PERMISSION_NAME_MAP[PermissionNames.WRITE_SETTINGS] = R.string.permission_modify_system_settings
        PERMISSION_DESCRIPTION_MAP[R.string.permission_modify_system_settings] = R.string.permission_modify_system_settings_description

        PERMISSION_NAME_MAP[PermissionNames.NOTIFICATION_SERVICE] = R.string.permission_allow_notifications
        PERMISSION_DESCRIPTION_MAP[R.string.permission_allow_notifications] = R.string.permission_allow_notifications_description

        PERMISSION_NAME_MAP[PermissionNames.POST_NOTIFICATIONS] = R.string.permission_post_notifications
        PERMISSION_DESCRIPTION_MAP[R.string.permission_post_notifications] = R.string.permission_post_notifications_description

        PERMISSION_NAME_MAP[PermissionNames.BIND_NOTIFICATION_LISTENER_SERVICE] = R.string.permission_allow_notifications_access
        PERMISSION_DESCRIPTION_MAP[R.string.permission_allow_notifications_access] = R.string.permission_allow_notifications_access_description

        PERMISSION_NAME_MAP[PermissionNames.PACKAGE_USAGE_STATS] = R.string.permission_apps_with_usage_access
        PERMISSION_DESCRIPTION_MAP[R.string.permission_apps_with_usage_access] = R.string.permission_apps_with_usage_access_description

        PERMISSION_NAME_MAP[PermissionNames.SCHEDULE_EXACT_ALARM] = R.string.permission_alarms_reminders
        PERMISSION_DESCRIPTION_MAP[R.string.permission_alarms_reminders] = R.string.permission_alarms_reminders_description

        PERMISSION_NAME_MAP[PermissionNames.ACCESS_NOTIFICATION_POLICY] = R.string.permission_do_not_disturb_access
        PERMISSION_DESCRIPTION_MAP[R.string.permission_do_not_disturb_access] = R.string.permission_do_not_disturb_access_description

        PERMISSION_NAME_MAP[PermissionNames.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS] = R.string.permission_ignore_battery_optimize
        PERMISSION_DESCRIPTION_MAP[R.string.permission_ignore_battery_optimize] = R.string.permission_ignore_battery_optimize_description

        PERMISSION_NAME_MAP[PermissionNames.BIND_VPN_SERVICE] = R.string.permission_vpn
        PERMISSION_DESCRIPTION_MAP[R.string.permission_vpn] = R.string.permission_vpn_description

        PERMISSION_NAME_MAP[PermissionNames.PICTURE_IN_PICTURE] = R.string.permission_picture_in_picture
        PERMISSION_DESCRIPTION_MAP[R.string.permission_picture_in_picture] = R.string.permission_picture_in_picture_description

        PERMISSION_NAME_MAP[PermissionNames.USE_FULL_SCREEN_INTENT] = R.string.permission_full_screen_notifications
        PERMISSION_DESCRIPTION_MAP[R.string.permission_full_screen_notifications] = R.string.permission_full_screen_notifications_description

        PERMISSION_NAME_MAP[PermissionNames.BIND_DEVICE_ADMIN] = R.string.permission_device_admin
        PERMISSION_DESCRIPTION_MAP[R.string.permission_device_admin] = R.string.permission_device_admin_description

        PERMISSION_NAME_MAP[PermissionNames.BIND_ACCESSIBILITY_SERVICE] = R.string.permission_accessibility_service
        PERMISSION_DESCRIPTION_MAP[R.string.permission_accessibility_service] = R.string.permission_accessibility_service_description
    }


    /**
     * 通过权限获得名称
     */
    
    fun getNickNamesByPermissions( context: Context,  permissions: List<IPermission>): String {
        val permissionNameList = getNickNameListByPermissions(context, permissions, true)

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

    
    fun getNickNameListByPermissions( context: Context,  permissions: List<IPermission>, filterHighVersionPermissions: Boolean): List<String> {
        val permissionNickNameList: MutableList<String> = ArrayList()
        for (permission in permissions) {
            // 如果当前设置了过滤高版本权限，并且这个权限是高版本系统才出现的权限，则不继续往下执行
            // 避免出现在低版本上面执行拒绝权限后，连带高版本的名称也一起显示出来，但是在低版本上面是没有这个权限的
            if (filterHighVersionPermissions && permission.getFromAndroidVersion(context) > Build.VERSION.SDK_INT) {
                continue
            }
            val permissionName = getNickNameByPermission(context, permission)
            if (TextUtils.isEmpty(permissionName)) {
                continue
            }
            if (permissionNickNameList.contains(permissionName)) {
                continue
            }
            permissionNickNameList.add(permissionName)
        }
        return permissionNickNameList
    }

    fun getNickNameByPermission( context: Context,  permission: IPermission): String {
        val permissionNameStringId = getPermissionNickNameStringId(context, permission)
        if (permissionNameStringId == null || permissionNameStringId == 0) {
            return ""
        }
        return context.getString(permissionNameStringId)
    }

    /**
     * 通过权限获得描述
     */
    
    fun getDescriptionsByPermissions( context: Context,  permissions: List<IPermission>): String {
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


    fun getDescriptionListByPermissions( context: Context, permissions: List<IPermission>): List<String> {
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
    
    fun getDescriptionByPermission( context: Context,  permission: IPermission): String {
        val permissionNameStringId = getPermissionNickNameStringId(context, permission)
        if (permissionNameStringId == null || permissionNameStringId == 0) {
            return ""
        }
        val permissionNickName = context.getString(permissionNameStringId)
        val permissionDescriptionStringId = getPermissionDescriptionStringId(permissionNameStringId)
        val permissionDescription = if (permissionDescriptionStringId == null || permissionDescriptionStringId == 0) {
            ""
        } else {
            context.getString(permissionDescriptionStringId)
        }
        return permissionNickName + context.getString(R.string.permission_colon) + permissionDescription
    }

    /**
     * 获取这个权限对应的别名 StringId
     */
    
    fun getPermissionNickNameStringId( context: Context, permission: IPermission): Int? {
        val permissionName = permission.getPermissionName()
        val permissionGroup = permission.getPermissionGroup(context)
        val permissionNameStringId = if (!permission.isBackgroundPermission(context) && !TextUtils.isEmpty(permissionGroup)) {
            // 这个权限必须有组别，并且不是后台权限，才能用组别来获取
            PERMISSION_NAME_MAP[permissionGroup]
        } else {
            PERMISSION_NAME_MAP[permissionName]
        }
        return permissionNameStringId
    }

    /**
     * 获取这个权限对应的描述 StringId
     */
    
    fun getPermissionDescriptionStringId(@IdRes permissionNickNameStringId: Int): Int? {
        return PERMISSION_DESCRIPTION_MAP[permissionNickNameStringId]
    }
}