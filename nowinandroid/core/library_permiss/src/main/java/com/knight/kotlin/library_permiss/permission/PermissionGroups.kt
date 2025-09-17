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
    
    const val STORAGE: String = "storage$SUFFIX"

    /**
     * 日历权限组，包含以下权限
     *
     * [PermissionNames.READ_CALENDAR]
     * [PermissionNames.WRITE_CALENDAR]
     */
    
    const val CALENDAR: String = "calendar$SUFFIX"

    /**
     * 通讯录权限组，包含以下权限
     *
     * [PermissionNames.READ_CONTACTS]
     * [PermissionNames.WRITE_CONTACTS]
     * [PermissionNames.GET_ACCOUNTS]
     */
    
    const val CONTACTS: String = "contacts$SUFFIX"

    /**
     * 短信权限组，包含以下权限
     *
     * [PermissionNames.SEND_SMS]
     * [PermissionNames.READ_SMS]
     * [PermissionNames.RECEIVE_SMS]
     * [PermissionNames.RECEIVE_WAP_PUSH]
     * [PermissionNames.RECEIVE_MMS]
     */
    
    const val SMS: String = "sms$SUFFIX"

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
    
    const val LOCATION: String = "location$SUFFIX"

    /**
     * 传感器权限组，包含以下权限
     *
     * [PermissionNames.BODY_SENSORS]
     * [PermissionNames.BODY_SENSORS_BACKGROUND]
     */
    
    const val SENSORS: String = "sensors$SUFFIX"

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
    
    const val PHONE: String = "phone$SUFFIX"

    /**
     * 通话记录权限组（在 Android 9.0 的时候，读写通话记录权限已经归到一个单独的权限组了，但是在 Android 9.0 之前，读写通话记录权限归属电话权限组），包含以下权限
     *
     * [PermissionNames.READ_CALL_LOG]
     * [PermissionNames.WRITE_CALL_LOG]
     * [PermissionNames.PROCESS_OUTGOING_CALLS]
     */
    
    const val CALL_LOG: String = "call_log$SUFFIX"

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
    
    const val NEARBY_DEVICES: String = "nearby_devices$SUFFIX"

    /**
     * 照片和视频权限组（注意：不包含音频权限） ，包含以下权限
     *
     * [PermissionNames.READ_MEDIA_IMAGES]
     * [PermissionNames.READ_MEDIA_VIDEO]
     * [PermissionNames.READ_MEDIA_VISUAL_USER_SELECTED]
     */
    
    const val IMAGE_AND_VIDEO_MEDIA: String = "image_and_video_media$SUFFIX"

    /**
     * 健康权限组，包含以下权限
     *
     * [PermissionNames.READ_HEALTH_DATA_IN_BACKGROUND]
     * [PermissionNames.READ_HEALTH_DATA_HISTORY]
     *
     * [PermissionNames.READ_ACTIVE_CALORIES_BURNED]
     * [PermissionNames.WRITE_ACTIVE_CALORIES_BURNED]
     * [PermissionNames.READ_ACTIVITY_INTENSITY]
     * [PermissionNames.WRITE_ACTIVITY_INTENSITY]
     * [PermissionNames.READ_BASAL_BODY_TEMPERATURE]
     * [PermissionNames.WRITE_BASAL_BODY_TEMPERATURE]
     * [PermissionNames.READ_BASAL_METABOLIC_RATE]
     * [PermissionNames.WRITE_BASAL_METABOLIC_RATE]
     * [PermissionNames.READ_BLOOD_GLUCOSE]
     * [PermissionNames.WRITE_BLOOD_GLUCOSE]
     * [PermissionNames.READ_BLOOD_PRESSURE]
     * [PermissionNames.WRITE_BLOOD_PRESSURE]
     * [PermissionNames.READ_BODY_FAT]
     * [PermissionNames.WRITE_BODY_FAT]
     * [PermissionNames.READ_BODY_TEMPERATURE]
     * [PermissionNames.WRITE_BODY_TEMPERATURE]
     * [PermissionNames.READ_BODY_WATER_MASS]
     * [PermissionNames.WRITE_BODY_WATER_MASS]
     * [PermissionNames.READ_BONE_MASS]
     * [PermissionNames.WRITE_BONE_MASS]
     * [PermissionNames.READ_CERVICAL_MUCUS]
     * [PermissionNames.WRITE_CERVICAL_MUCUS]
     * [PermissionNames.READ_DISTANCE]
     * [PermissionNames.WRITE_DISTANCE]
     * [PermissionNames.READ_ELEVATION_GAINED]
     * [PermissionNames.WRITE_ELEVATION_GAINED]
     * [PermissionNames.READ_EXERCISE]
     * [PermissionNames.WRITE_EXERCISE]
     * [PermissionNames.READ_EXERCISE_ROUTES]
     * [PermissionNames.WRITE_EXERCISE_ROUTE]
     * [PermissionNames.READ_FLOORS_CLIMBED]
     * [PermissionNames.WRITE_FLOORS_CLIMBED]
     * [PermissionNames.READ_HEART_RATE]
     * [PermissionNames.WRITE_HEART_RATE]
     * [PermissionNames.READ_HEART_RATE_VARIABILITY]
     * [PermissionNames.WRITE_HEART_RATE_VARIABILITY]
     * [PermissionNames.READ_HEIGHT]
     * [PermissionNames.WRITE_HEIGHT]
     * [PermissionNames.READ_HYDRATION]
     * [PermissionNames.WRITE_HYDRATION]
     * [PermissionNames.READ_INTERMENSTRUAL_BLEEDING]
     * [PermissionNames.WRITE_INTERMENSTRUAL_BLEEDING]
     * [PermissionNames.READ_LEAN_BODY_MASS]
     * [PermissionNames.WRITE_LEAN_BODY_MASS]
     * [PermissionNames.READ_MENSTRUATION]
     * [PermissionNames.WRITE_MENSTRUATION]
     * [PermissionNames.READ_MINDFULNESS]
     * [PermissionNames.WRITE_MINDFULNESS]
     * [PermissionNames.READ_NUTRITION]
     * [PermissionNames.WRITE_NUTRITION]
     * [PermissionNames.READ_OVULATION_TEST]
     * [PermissionNames.WRITE_OVULATION_TEST]
     * [PermissionNames.READ_OXYGEN_SATURATION]
     * [PermissionNames.WRITE_OXYGEN_SATURATION]
     * [PermissionNames.READ_PLANNED_EXERCISE]
     * [PermissionNames.WRITE_PLANNED_EXERCISE]
     * [PermissionNames.READ_POWER]
     * [PermissionNames.WRITE_POWER]
     * [PermissionNames.READ_RESPIRATORY_RATE]
     * [PermissionNames.WRITE_RESPIRATORY_RATE]
     * [PermissionNames.READ_RESTING_HEART_RATE]
     * [PermissionNames.WRITE_RESTING_HEART_RATE]
     * [PermissionNames.READ_SEXUAL_ACTIVITY]
     * [PermissionNames.WRITE_SEXUAL_ACTIVITY]
     * [PermissionNames.READ_SKIN_TEMPERATURE]
     * [PermissionNames.WRITE_SKIN_TEMPERATURE]
     * [PermissionNames.READ_SLEEP]
     * [PermissionNames.WRITE_SLEEP]
     * [PermissionNames.READ_SPEED]
     * [PermissionNames.WRITE_SPEED]
     * [PermissionNames.READ_STEPS]
     * [PermissionNames.WRITE_STEPS]
     * [PermissionNames.READ_TOTAL_CALORIES_BURNED]
     * [PermissionNames.WRITE_TOTAL_CALORIES_BURNED]
     * [PermissionNames.READ_VO2_MAX]
     * [PermissionNames.WRITE_VO2_MAX]
     * [PermissionNames.READ_WEIGHT]
     * [PermissionNames.WRITE_WEIGHT]
     * [PermissionNames.READ_WHEELCHAIR_PUSHES]
     * [PermissionNames.WRITE_WHEELCHAIR_PUSHES]
     *
     * [PermissionNames.READ_MEDICAL_DATA_ALLERGIES_INTOLERANCES]
     * [PermissionNames.READ_MEDICAL_DATA_CONDITIONS]
     * [PermissionNames.READ_MEDICAL_DATA_LABORATORY_RESULTS]
     * [PermissionNames.READ_MEDICAL_DATA_MEDICATIONS]
     * [PermissionNames.READ_MEDICAL_DATA_PERSONAL_DETAILS]
     * [PermissionNames.READ_MEDICAL_DATA_PRACTITIONER_DETAILS]
     * [PermissionNames.READ_MEDICAL_DATA_PREGNANCY]
     * [PermissionNames.READ_MEDICAL_DATA_PROCEDURES]
     * [PermissionNames.READ_MEDICAL_DATA_SOCIAL_HISTORY]
     * [PermissionNames.READ_MEDICAL_DATA_VACCINES]
     * [PermissionNames.READ_MEDICAL_DATA_VISITS]
     * [PermissionNames.READ_MEDICAL_DATA_VITAL_SIGNS]
     * [PermissionNames.WRITE_MEDICAL_DATA]
     */
    
    const val HEALTH: String = "health$SUFFIX"
}