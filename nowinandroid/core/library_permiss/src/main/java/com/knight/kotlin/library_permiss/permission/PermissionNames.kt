package com.knight.kotlin.library_permiss.permission


/**
 * @author created by luguian
 * @organize
 * @Date 2025/7/11 9:27
 * @descript:危险权限和特殊权限的名称常量集
 */
object PermissionNames {
    /**
     * 读取应用列表权限字符串常量，如需权限对象请调用 [PermissionLists.getGetInstalledAppsPermission] 获取
     */
    
    const val GET_INSTALLED_APPS: String = "com.android.permission.GET_INSTALLED_APPS"

    /**
     * 全屏通知权限字符串常量，如需权限对象请调用 [PermissionLists.getUseFullScreenIntentPermission] 获取
     */
    
    const val USE_FULL_SCREEN_INTENT: String = "android.permission.USE_FULL_SCREEN_INTENT"

    /**
     * 闹钟权限字符串常量，如需权限对象请调用 [PermissionLists.getScheduleExactAlarmPermission] 获取
     */
    
    const val SCHEDULE_EXACT_ALARM: String = "android.permission.SCHEDULE_EXACT_ALARM"

    /**
     * 所有文件访问权限字符串常量，如需权限对象请调用 [PermissionLists.getManageExternalStoragePermission] 获取
     */
    
    const val MANAGE_EXTERNAL_STORAGE: String = "android.permission.MANAGE_EXTERNAL_STORAGE"

    /**
     * 安装应用权限字符串常量，如需权限对象请调用 [PermissionLists.getRequestInstallPackagesPermission] 获取
     */
    
    const val REQUEST_INSTALL_PACKAGES: String = "android.permission.REQUEST_INSTALL_PACKAGES"

    /**
     * 画中画权限字符串常量，如需权限对象请调用 [PermissionLists.getPictureInPicturePermission] 获取
     */
    
    const val PICTURE_IN_PICTURE: String = "android.permission.PICTURE_IN_PICTURE"

    /**
     * 悬浮窗权限字符串常量，如需权限对象请调用 [PermissionLists.getSystemAlertWindowPermission] 获取
     */
    
    const val SYSTEM_ALERT_WINDOW: String = "android.permission.SYSTEM_ALERT_WINDOW"

    /**
     * 写入系统设置权限字符串常量，如需权限对象请调用 [PermissionLists.getWriteSettingsPermission] 获取
     */
    
    const val WRITE_SETTINGS: String = "android.permission.WRITE_SETTINGS"

    /**
     * 请求忽略电池优化选项权限字符串常量，如需权限对象请调用 [PermissionLists.getRequestIgnoreBatteryOptimizationsPermission] 获取
     */
    
    const val REQUEST_IGNORE_BATTERY_OPTIMIZATIONS: String = "android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS"

    /**
     * 勿扰权限字符串常量，如需权限对象请调用 [PermissionLists.getAccessNotificationPolicyPermission] 获取
     */
    
    const val ACCESS_NOTIFICATION_POLICY: String = "android.permission.ACCESS_NOTIFICATION_POLICY"

    /**
     * 查看应用使用情况权限字符串常量，如需权限对象请调用 [PermissionLists.getPackageUsageStatsPermission] 获取
     */
    
    const val PACKAGE_USAGE_STATS: String = "android.permission.PACKAGE_USAGE_STATS"

    /**
     * 通知栏监听权限字符串常量，如需权限对象请调用 [PermissionLists.getBindNotificationListenerServicePermission] 获取
     */
    
    const val BIND_NOTIFICATION_LISTENER_SERVICE: String = "android.permission.BIND_NOTIFICATION_LISTENER_SERVICE"

    /**
     * VPN 权限字符串常量，如需权限对象请调用 [PermissionLists.getBindVpnServicePermission] 获取
     */
    
    const val BIND_VPN_SERVICE: String = "android.permission.BIND_VPN_SERVICE"

    /**
     * 通知栏权限字符串常量，如需权限对象请调用 [PermissionLists.getNotificationServicePermission] 获取
     */
    
    const val NOTIFICATION_SERVICE: String = "android.permission.NOTIFICATION_SERVICE"

    /**
     * 无障碍服务权限字符串常量，如需权限对象请调用 [PermissionLists.getBindAccessibilityServicePermission] 获取
     */
    
    const val BIND_ACCESSIBILITY_SERVICE: String = "android.permission.BIND_ACCESSIBILITY_SERVICE"

    /**
     * 设备管理器权限字符串常量，如需权限对象请调用 [PermissionLists.getBindDeviceAdminPermission] 获取
     */
    
    const val BIND_DEVICE_ADMIN: String = "android.permission.BIND_DEVICE_ADMIN"


    /* ------------------------------------ 我是一条华丽的分割线 ------------------------------------ */
    /**
     * 访问部分照片和视频的权限字符串常量，如需权限对象请调用 [PermissionLists.getReadMediaVisualUserSelectedPermission] 获取
     */
    
    const val READ_MEDIA_VISUAL_USER_SELECTED: String = "android.permission.READ_MEDIA_VISUAL_USER_SELECTED"

    /**
     * 发送通知权限字符串常量，如需权限对象请调用 [PermissionLists.getPostNotificationsPermission] 获取
     */
    
    const val POST_NOTIFICATIONS: String = "android.permission.POST_NOTIFICATIONS"

    /**
     * WIFI 权限字符串常量，如需权限对象请调用 [PermissionLists.getNearbyWifiDevicesPermission] 获取
     */
    
    const val NEARBY_WIFI_DEVICES: String = "android.permission.NEARBY_WIFI_DEVICES"

    /**
     * 后台传感器权限字符串常量，如需权限对象请调用 [PermissionLists.getBodySensorsBackgroundPermission] 获取
     */
    
    const val BODY_SENSORS_BACKGROUND: String = "android.permission.BODY_SENSORS_BACKGROUND"

    /**
     * 读取图片权限字符串常量，如需权限对象请调用 [PermissionLists.getReadMediaImagesPermission] 获取
     */
    
    const val READ_MEDIA_IMAGES: String = "android.permission.READ_MEDIA_IMAGES"

    /**
     * 读取视频权限字符串常量，如需权限对象请调用 [PermissionLists.getReadMediaVideoPermission] 获取
     */
    
    const val READ_MEDIA_VIDEO: String = "android.permission.READ_MEDIA_VIDEO"

    /**
     * 读取音频权限字符串常量，如需权限对象请调用 [PermissionLists.getReadMediaAudioPermission] 获取
     */
    
    const val READ_MEDIA_AUDIO: String = "android.permission.READ_MEDIA_AUDIO"

    /**
     * 蓝牙扫描权限字符串常量，如需权限对象请调用 [PermissionLists.getBluetoothScanPermission] 获取
     */
    
    const val BLUETOOTH_SCAN: String = "android.permission.BLUETOOTH_SCAN"

    /**
     * 蓝牙连接权限字符串常量，如需权限对象请调用 [PermissionLists.getBluetoothConnectPermission] 获取
     */
    
    const val BLUETOOTH_CONNECT: String = "android.permission.BLUETOOTH_CONNECT"

    /**
     * 蓝牙广播权限字符串常量，如需权限对象请调用 [PermissionLists.getBluetoothAdvertisePermission] 获取
     */
    
    const val BLUETOOTH_ADVERTISE: String = "android.permission.BLUETOOTH_ADVERTISE"

    /**
     * 在后台获取位置权限字符串常量，如需权限对象请调用 [PermissionLists.getAccessBackgroundLocationPermission] 获取
     */
    
    const val ACCESS_BACKGROUND_LOCATION: String = "android.permission.ACCESS_BACKGROUND_LOCATION"

    /**
     * 获取活动步数权限字符串常量，如需权限对象请调用 [PermissionLists.getActivityRecognitionPermission] 获取
     */
    
    const val ACTIVITY_RECOGNITION: String = "android.permission.ACTIVITY_RECOGNITION"

    /**
     * 访问媒体的位置信息权限字符串常量，如需权限对象请调用 [PermissionLists.getAccessMediaLocationPermission] 获取
     */
    
    const val ACCESS_MEDIA_LOCATION: String = "android.permission.ACCESS_MEDIA_LOCATION"

    /**
     * 允许呼叫应用继续在另一个应用中启动的呼叫权限字符串常量，如需权限对象请调用 [PermissionLists.getAcceptHandoverPermission] 获取
     */
    
    const val ACCEPT_HANDOVER: String = "android.permission.ACCEPT_HANDOVER"

    /**
     * 读取手机号码权限字符串常量，如需权限对象请调用 [PermissionLists.getReadPhoneNumbersPermission] 获取
     */
    
    const val READ_PHONE_NUMBERS: String = "android.permission.READ_PHONE_NUMBERS"

    /**
     * 接听电话权限字符串常量，如需权限对象请调用 [PermissionLists.getAnswerPhoneCallsPermission] 获取
     */
    
    const val ANSWER_PHONE_CALLS: String = "android.permission.ANSWER_PHONE_CALLS"

    /**
     * 读取外部存储权限字符串常量，如需权限对象请调用 [PermissionLists.getReadExternalStoragePermission] 获取
     */
    
    const val READ_EXTERNAL_STORAGE: String = "android.permission.READ_EXTERNAL_STORAGE"

    /**
     * 写入外部存储权限字符串常量，如需权限对象请调用 [PermissionLists.getWriteExternalStoragePermission] 获取
     */
    
    const val WRITE_EXTERNAL_STORAGE: String = "android.permission.WRITE_EXTERNAL_STORAGE"

    /**
     * 相机权限字符串常量，如需权限对象请调用 [PermissionLists.getCameraPermission] 获取
     */
    
    const val CAMERA: String = "android.permission.CAMERA"

    /**
     * 麦克风权限字符串常量，如需权限对象请调用 [PermissionLists.getRecordAudioPermission] 获取
     */
    
    const val RECORD_AUDIO: String = "android.permission.RECORD_AUDIO"

    /**
     * 获取精确位置权限字符串常量，如需权限对象请调用 [PermissionLists.getAccessFineLocationPermission] 获取
     */
    
    const val ACCESS_FINE_LOCATION: String = "android.permission.ACCESS_FINE_LOCATION"

    /**
     * 获取粗略位置权限字符串常量，如需权限对象请调用 [PermissionLists.getAccessCoarseLocationPermission] 获取
     */
    
    const val ACCESS_COARSE_LOCATION: String = "android.permission.ACCESS_COARSE_LOCATION"

    /**
     * 读取联系人权限字符串常量，如需权限对象请调用 [PermissionLists.getReadContactsPermission] 获取
     */
    
    const val READ_CONTACTS: String = "android.permission.READ_CONTACTS"

    /**
     * 修改联系人权限字符串常量，如需权限对象请调用 [PermissionLists.getWriteContactsPermission] 获取
     */
    
    const val WRITE_CONTACTS: String = "android.permission.WRITE_CONTACTS"

    /**
     * 访问账户列表权限字符串常量，如需权限对象请调用 [PermissionLists.getGetAccountsPermission] 获取
     */
    
    const val GET_ACCOUNTS: String = "android.permission.GET_ACCOUNTS"

    /**
     * 读取日历权限字符串常量，如需权限对象请调用 [PermissionLists.getReadCalendarPermission] 获取
     */
    
    const val READ_CALENDAR: String = "android.permission.READ_CALENDAR"

    /**
     * 修改日历权限字符串常量，如需权限对象请调用 [PermissionLists.getWriteCalendarPermission] 获取
     */
    
    const val WRITE_CALENDAR: String = "android.permission.WRITE_CALENDAR"

    /**
     * 读取电话状态权限字符串常量，如需权限对象请调用 [PermissionLists.getReadPhoneStatePermission] 获取
     */
    
    const val READ_PHONE_STATE: String = "android.permission.READ_PHONE_STATE"

    /**
     * 拨打电话权限字符串常量，如需权限对象请调用 [PermissionLists.getCallPhonePermission] 获取
     */
    
    const val CALL_PHONE: String = "android.permission.CALL_PHONE"

    /**
     * 读取通话记录权限字符串常量，如需权限对象请调用 [PermissionLists.getReadCallLogPermission] 获取
     */
    
    const val READ_CALL_LOG: String = "android.permission.READ_CALL_LOG"

    /**
     * 修改通话记录权限字符串常量，如需权限对象请调用 [PermissionLists.getWriteCallLogPermission] 获取
     */
    
    const val WRITE_CALL_LOG: String = "android.permission.WRITE_CALL_LOG"

    /**
     * 添加语音邮件权限字符串常量，如需权限对象请调用 [PermissionLists.getAddVoicemailPermission] 获取
     */
    
    const val ADD_VOICEMAIL: String = "com.android.voicemail.permission.ADD_VOICEMAIL"

    /**
     * 使用 SIP 视频权限字符串常量，如需权限对象请调用 [PermissionLists.getUseSipPermission] 获取
     */
    
    const val USE_SIP: String = "android.permission.USE_SIP"

    /**
     * 处理拨出电话权限字符串常量，如需权限对象请调用 [PermissionLists.getProcessOutgoingCallsPermission] 获取
     */
    
    const val PROCESS_OUTGOING_CALLS: String = "android.permission.PROCESS_OUTGOING_CALLS"

    /**
     * 使用传感器权限字符串常量，如需权限对象请调用 [PermissionLists.getBodySensorsPermission] 获取
     */
    
    const val BODY_SENSORS: String = "android.permission.BODY_SENSORS"

    /**
     * 发送短信权限字符串常量，如需权限对象请调用 [PermissionLists.getSendSmsPermission] 获取
     */
    
    const val SEND_SMS: String = "android.permission.SEND_SMS"

    /**
     * 接收短信权限字符串常量，如需权限对象请调用 [PermissionLists.getReceiveSmsPermission] 获取
     */
    
    const val RECEIVE_SMS: String = "android.permission.RECEIVE_SMS"

    /**
     * 读取短信权限字符串常量，如需权限对象请调用 [PermissionLists.getReadSmsPermission] ()} 获取
     */
    
    const val READ_SMS: String = "android.permission.READ_SMS"

    /**
     * 接收 WAP 推送消息权限字符串常量，如需权限对象请调用 [PermissionLists.getReceiveWapPushPermission] 获取
     */
    
    const val RECEIVE_WAP_PUSH: String = "android.permission.RECEIVE_WAP_PUSH"

    /**
     * 接收彩信权限字符串常量，如需权限对象请调用 [PermissionLists.getReceiveMmsPermission] 获取
     */
    
    const val RECEIVE_MMS: String = "android.permission.RECEIVE_MMS"


    /* ------------------------------------ 我是一条华丽的分割线 ------------------------------------ */
    /**
     * 在后台读取健康数据权限（任何类型），如需权限对象请调用 [PermissionLists.getReadHealthDataInBackgroundPermission] 获取
     */
    
    const val READ_HEALTH_DATA_IN_BACKGROUND: String = "android.permission.health.READ_HEALTH_DATA_IN_BACKGROUND"

    /**
     * 读取以往的健康数据权限（任何类型），如需权限对象请调用 [PermissionLists.getReadHealthDataHistoryPermission] 获取
     */
    
    const val READ_HEALTH_DATA_HISTORY: String = "android.permission.health.READ_HEALTH_DATA_HISTORY"

    /**
     * 读取运动消耗的卡路里数据权限，如需权限对象请调用 [PermissionLists.getReadActiveCaloriesBurnedPermission] 获取
     */
    
    const val READ_ACTIVE_CALORIES_BURNED: String = "android.permission.health.READ_ACTIVE_CALORIES_BURNED"

    /**
     * 写入运动消耗的卡路里数据权限，如需权限对象请调用 [PermissionLists.getWriteActiveCaloriesBurnedPermission] 获取
     */
    
    const val WRITE_ACTIVE_CALORIES_BURNED: String = "android.permission.health.WRITE_ACTIVE_CALORIES_BURNED"

    /**
     * 读取活动强度数据权限，如需权限对象请调用 [PermissionLists.getReadActivityIntensityPermission] 获取
     */
    
    const val READ_ACTIVITY_INTENSITY: String = "android.permission.health.READ_ACTIVITY_INTENSITY"

    /**
     * 写入活动强度数据权限，如需权限对象请调用 [PermissionLists.getWriteActivityIntensityPermission] 获取
     */
    
    const val WRITE_ACTIVITY_INTENSITY: String = "android.permission.health.WRITE_ACTIVITY_INTENSITY"

    /**
     * 读取基础体温数据权限，如需权限对象请调用 [PermissionLists.getReadBasalBodyTemperaturePermission] 获取
     */
    
    const val READ_BASAL_BODY_TEMPERATURE: String = "android.permission.health.READ_BASAL_BODY_TEMPERATURE"

    /**
     * 写入基础体温数据权限，如需权限对象请调用 [PermissionLists.getWriteBasalBodyTemperaturePermission] 获取
     */
    
    const val WRITE_BASAL_BODY_TEMPERATURE: String = "android.permission.health.WRITE_BASAL_BODY_TEMPERATURE"

    /**
     * 读取基础代谢率数据权限，如需权限对象请调用 [PermissionLists.getReadBasalMetabolicRatePermission] 获取
     */
    
    const val READ_BASAL_METABOLIC_RATE: String = "android.permission.health.READ_BASAL_METABOLIC_RATE"

    /**
     * 写入基础代谢率数据权限，如需权限对象请调用 [PermissionLists.getWriteBasalMetabolicRatePermission] 获取
     */
    
    const val WRITE_BASAL_METABOLIC_RATE: String = "android.permission.health.WRITE_BASAL_METABOLIC_RATE"

    /**
     * 读取血糖数据权限，如需权限对象请调用 [PermissionLists.getReadBloodGlucosePermission] 获取
     */
    
    const val READ_BLOOD_GLUCOSE: String = "android.permission.health.READ_BLOOD_GLUCOSE"

    /**
     * 写入血糖数据权限，如需权限对象请调用 [PermissionLists.getWriteBloodGlucosePermission] 获取
     */
    
    const val WRITE_BLOOD_GLUCOSE: String = "android.permission.health.WRITE_BLOOD_GLUCOSE"

    /**
     * 读取血压数据权限，如需权限对象请调用 [PermissionLists.getReadBloodPressurePermission] 获取
     */
    
    const val READ_BLOOD_PRESSURE: String = "android.permission.health.READ_BLOOD_PRESSURE"

    /**
     * 写入血压数据权限，如需权限对象请调用 [PermissionLists.getWriteBloodPressurePermission] 获取
     */
    
    const val WRITE_BLOOD_PRESSURE: String = "android.permission.health.WRITE_BLOOD_PRESSURE"

    /**
     * 读取体脂数据权限，如需权限对象请调用 [PermissionLists.getReadBodyFatPermission] 获取
     */
    
    const val READ_BODY_FAT: String = "android.permission.health.READ_BODY_FAT"

    /**
     * 写入体脂数据权限，如需权限对象请调用 [PermissionLists.getWriteBodyFatPermission] 获取
     */
    
    const val WRITE_BODY_FAT: String = "android.permission.health.WRITE_BODY_FAT"

    /**
     * 读取体温数据权限，如需权限对象请调用 [PermissionLists.getReadBodyTemperaturePermission] 获取
     */
    
    const val READ_BODY_TEMPERATURE: String = "android.permission.health.READ_BODY_TEMPERATURE"

    /**
     * 写入体温数据权限，如需权限对象请调用 [PermissionLists.getWriteBodyTemperaturePermission] 获取
     */
    
    const val WRITE_BODY_TEMPERATURE: String = "android.permission.health.WRITE_BODY_TEMPERATURE"

    /**
     * 读取身体含水量数据权限，如需权限对象请调用 [PermissionLists.getReadBodyWaterMassPermission] 获取
     */
    
    const val READ_BODY_WATER_MASS: String = "android.permission.health.READ_BODY_WATER_MASS"

    /**
     * 写入身体含水量数据权限，如需权限对象请调用 [PermissionLists.getWriteBodyWaterMassPermission] 获取
     */
    
    const val WRITE_BODY_WATER_MASS: String = "android.permission.health.WRITE_BODY_WATER_MASS"

    /**
     * 读取骨质密度数据权限，如需权限对象请调用 [PermissionLists.getReadBoneMassPermission] 获取
     */
    
    const val READ_BONE_MASS: String = "android.permission.health.READ_BONE_MASS"

    /**
     * 写入骨质密度数据权限，如需权限对象请调用 [PermissionLists.getWriteBoneMassPermission] 获取
     */
    
    const val WRITE_BONE_MASS: String = "android.permission.health.WRITE_BONE_MASS"

    /**
     * 读取宫颈粘液数据权限，如需权限对象请调用 [PermissionLists.getReadCervicalMucusPermission] 获取
     */
    
    const val READ_CERVICAL_MUCUS: String = "android.permission.health.READ_CERVICAL_MUCUS"

    /**
     * 写入宫颈粘液数据权限，如需权限对象请调用 [PermissionLists.getWriteCervicalMucusPermission] 获取
     */
    
    const val WRITE_CERVICAL_MUCUS: String = "android.permission.health.WRITE_CERVICAL_MUCUS"

    /**
     * 读取距离数据权限，如需权限对象请调用 [PermissionLists.getReadDistancePermission] 获取
     */
    
    const val READ_DISTANCE: String = "android.permission.health.READ_DISTANCE"

    /**
     * 写入距离数据权限，如需权限对象请调用 [PermissionLists.getWriteDistancePermission] 获取
     */
    
    const val WRITE_DISTANCE: String = "android.permission.health.WRITE_DISTANCE"

    /**
     * 读取爬升高度数据权限，如需权限对象请调用 [PermissionLists.getReadElevationGainedPermission] 获取
     */
    
    const val READ_ELEVATION_GAINED: String = "android.permission.health.READ_ELEVATION_GAINED"

    /**
     * 写入爬升高度数据权限，如需权限对象请调用 [PermissionLists.getWriteElevationGainedPermission] 获取
     */
    
    const val WRITE_ELEVATION_GAINED: String = "android.permission.health.WRITE_ELEVATION_GAINED"

    /**
     * 读取锻炼数据权限，如需权限对象请调用 [PermissionLists.getReadExercisePermission] 获取
     */
    
    const val READ_EXERCISE: String = "android.permission.health.READ_EXERCISE"

    /**
     * 写入锻炼数据权限，如需权限对象请调用 [PermissionLists.getWriteExercisePermission] 获取
     */
    
    const val WRITE_EXERCISE: String = "android.permission.health.WRITE_EXERCISE"

    /**
     * 读取锻炼路线数据权限，如需权限对象请调用 [PermissionLists.getReadExerciseRoutesPermission] 获取
     */
    
    const val READ_EXERCISE_ROUTES: String = "android.permission.health.READ_EXERCISE_ROUTES"

    /**
     * 写入锻炼路线数据权限，如需权限对象请调用 [PermissionLists.getWriteExerciseRoutePermission] 获取
     */
    
    const val WRITE_EXERCISE_ROUTE: String = "android.permission.health.WRITE_EXERCISE_ROUTE"

    /**
     * 读取爬楼层数数据权限，如需权限对象请调用 [PermissionLists.getReadFloorsClimbedPermission] 获取
     */
    
    const val READ_FLOORS_CLIMBED: String = "android.permission.health.READ_FLOORS_CLIMBED"

    /**
     * 写入爬楼层数数据权限，如需权限对象请调用 [PermissionLists.getWriteFloorsClimbedPermission] 获取
     */
    
    const val WRITE_FLOORS_CLIMBED: String = "android.permission.health.WRITE_FLOORS_CLIMBED"

    /**
     * 读取心率数据权限，如需权限对象请调用 [PermissionLists.getReadHeartRatePermission] 获取
     */
    
    const val READ_HEART_RATE: String = "android.permission.health.READ_HEART_RATE"

    /**
     * 写入心率数据权限，如需权限对象请调用 [PermissionLists.getWriteHeartRatePermission] 获取
     */
    
    const val WRITE_HEART_RATE: String = "android.permission.health.WRITE_HEART_RATE"

    /**
     * 读取心率变异性数据权限，如需权限对象请调用 [PermissionLists.getReadHeartRateVariabilityPermission] 获取
     */
    
    const val READ_HEART_RATE_VARIABILITY: String = "android.permission.health.READ_HEART_RATE_VARIABILITY"

    /**
     * 写入心率变异性数据权限，如需权限对象请调用 [PermissionLists.getWriteHeartRateVariabilityPermission] 获取
     */
    
    const val WRITE_HEART_RATE_VARIABILITY: String = "android.permission.health.WRITE_HEART_RATE_VARIABILITY"

    /**
     * 读取身高数据权限，如需权限对象请调用 [PermissionLists.getReadHeightPermission] 获取
     */
    
    const val READ_HEIGHT: String = "android.permission.health.READ_HEIGHT"

    /**
     * 写入身高数据权限，如需权限对象请调用 [PermissionLists.getWriteHeightPermission] 获取
     */
    
    const val WRITE_HEIGHT: String = "android.permission.health.WRITE_HEIGHT"

    /**
     * 读取饮水量权限，如需权限对象请调用 [PermissionLists.getReadHydrationPermission] 获取
     */
    
    const val READ_HYDRATION: String = "android.permission.health.READ_HYDRATION"

    /**
     * 写入饮水量权限，如需权限对象请调用 [PermissionLists.getWriteHydrationPermission] 获取
     */
    
    const val WRITE_HYDRATION: String = "android.permission.health.WRITE_HYDRATION"

    /**
     * 读取点状出血数据权限，如需权限对象请调用 [PermissionLists.getReadIntermenstrualBleedingPermission] 获取
     */
    
    const val READ_INTERMENSTRUAL_BLEEDING: String = "android.permission.health.READ_INTERMENSTRUAL_BLEEDING"

    /**
     * 写入点状出血数据权限，如需权限对象请调用 [PermissionLists.getWriteIntermenstrualBleedingPermission] 获取
     */
    
    const val WRITE_INTERMENSTRUAL_BLEEDING: String = "android.permission.health.WRITE_INTERMENSTRUAL_BLEEDING"

    /**
     * 读取净体重数据权限，如需权限对象请调用 [PermissionLists.getReadLeanBodyMassPermission] 获取
     */
    
    const val READ_LEAN_BODY_MASS: String = "android.permission.health.READ_LEAN_BODY_MASS"

    /**
     * 写入净体重数据权限，如需权限对象请调用 [PermissionLists.getWriteLeanBodyMassPermission] 获取
     */
    
    const val WRITE_LEAN_BODY_MASS: String = "android.permission.health.WRITE_LEAN_BODY_MASS"

    /**
     * 读取经期数据权限，如需权限对象请调用 [PermissionLists.getReadMenstruationPermission] 获取
     */
    
    const val READ_MENSTRUATION: String = "android.permission.health.READ_MENSTRUATION"

    /**
     * 写入经期数据权限，如需权限对象请调用 [PermissionLists.getWriteMenstruationPermission] 获取
     */
    
    const val WRITE_MENSTRUATION: String = "android.permission.health.WRITE_MENSTRUATION"

    /**
     * 读取正念数据权限，如需权限对象请调用 [PermissionLists.getReadMindfulnessPermission] 获取
     */
    
    const val READ_MINDFULNESS: String = "android.permission.health.READ_MINDFULNESS"

    /**
     * 写入正念数据权限，如需权限对象请调用 [PermissionLists.getWriteMindfulnessPermission] 获取
     */
    
    const val WRITE_MINDFULNESS: String = "android.permission.health.WRITE_MINDFULNESS"

    /**
     * 读取营养数据权限，如需权限对象请调用 [PermissionLists.getReadNutritionPermission] 获取
     */
    
    const val READ_NUTRITION: String = "android.permission.health.READ_NUTRITION"

    /**
     * 写入营养数据权限，如需权限对象请调用 [PermissionLists.getWriteNutritionPermission] 获取
     */
    
    const val WRITE_NUTRITION: String = "android.permission.health.WRITE_NUTRITION"

    /**
     * 读取排卵检测数据权限，如需权限对象请调用 [PermissionLists.getReadOvulationTestPermission] 获取
     */
    
    const val READ_OVULATION_TEST: String = "android.permission.health.READ_OVULATION_TEST"

    /**
     * 写入排卵检测数据权限，如需权限对象请调用 [PermissionLists.getWriteOvulationTestPermission] 获取
     */
    
    const val WRITE_OVULATION_TEST: String = "android.permission.health.WRITE_OVULATION_TEST"

    /**
     * 读取血氧饱和度数据权限，如需权限对象请调用 [PermissionLists.getReadOxygenSaturationPermission] 获取
     */
    
    const val READ_OXYGEN_SATURATION: String = "android.permission.health.READ_OXYGEN_SATURATION"

    /**
     * 写入血氧饱和度数据权限，如需权限对象请调用 [PermissionLists.getWriteOxygenSaturationPermission] 获取
     */
    
    const val WRITE_OXYGEN_SATURATION: String = "android.permission.health.WRITE_OXYGEN_SATURATION"

    /**
     * 读取训练计划数据权限，如需权限对象请调用 [PermissionLists.getReadPlannedExercisePermission] 获取
     */
    
    const val READ_PLANNED_EXERCISE: String = "android.permission.health.READ_PLANNED_EXERCISE"

    /**
     * 写入训练计划数据权限，如需权限对象请调用 [PermissionLists.getWritePlannedExercisePermission] 获取
     */
    
    const val WRITE_PLANNED_EXERCISE: String = "android.permission.health.WRITE_PLANNED_EXERCISE"

    /**
     * 读取体能数据权限，如需权限对象请调用 [PermissionLists.getReadPowerPermission] 获取
     */
    
    const val READ_POWER: String = "android.permission.health.READ_POWER"

    /**
     * 写入体能数据权限，如需权限对象请调用 [PermissionLists.getWritePowerPermission] 获取
     */
    
    const val WRITE_POWER: String = "android.permission.health.WRITE_POWER"

    /**
     * 读取呼吸频率数据权限，如需权限对象请调用 [PermissionLists.getReadRespiratoryRatePermission] 获取
     */
    
    const val READ_RESPIRATORY_RATE: String = "android.permission.health.READ_RESPIRATORY_RATE"

    /**
     * 写入呼吸频率数据权限，如需权限对象请调用 [PermissionLists.getWriteRespiratoryRatePermission] 获取
     */
    
    const val WRITE_RESPIRATORY_RATE: String = "android.permission.health.WRITE_RESPIRATORY_RATE"

    /**
     * 读取静息心率数据权限，如需权限对象请调用 [PermissionLists.getReadRestingHeartRatePermission] 获取
     */
    
    const val READ_RESTING_HEART_RATE: String = "android.permission.health.READ_RESTING_HEART_RATE"

    /**
     * 写入静息心率数据权限，如需权限对象请调用 [PermissionLists.getWriteRestingHeartRatePermission] 获取
     */
    
    const val WRITE_RESTING_HEART_RATE: String = "android.permission.health.WRITE_RESTING_HEART_RATE"

    /**
     * 读取性活动数据权限，如需权限对象请调用 [PermissionLists.getReadSexualActivityPermission] 获取
     */
    
    const val READ_SEXUAL_ACTIVITY: String = "android.permission.health.READ_SEXUAL_ACTIVITY"

    /**
     * 写入性活动数据权限，如需权限对象请调用 [PermissionLists.getWriteSexualActivityPermission] 获取
     */
    
    const val WRITE_SEXUAL_ACTIVITY: String = "android.permission.health.WRITE_SEXUAL_ACTIVITY"

    /**
     * 读取体表温度数据权限，如需权限对象请调用 [PermissionLists.getReadSkinTemperaturePermission] 获取
     */
    
    const val READ_SKIN_TEMPERATURE: String = "android.permission.health.READ_SKIN_TEMPERATURE"

    /**
     * 写入体表温度数据权限，如需权限对象请调用 [PermissionLists.getWriteSkinTemperaturePermission] 获取
     */
    
    const val WRITE_SKIN_TEMPERATURE: String = "android.permission.health.WRITE_SKIN_TEMPERATURE"

    /**
     * 读取睡眠数据权限，如需权限对象请调用 [PermissionLists.getReadSleepPermission] 获取
     */
    
    const val READ_SLEEP: String = "android.permission.health.READ_SLEEP"

    /**
     * 写入睡眠数据权限，如需权限对象请调用 [PermissionLists.getWriteSleepPermission] 获取
     */
    
    const val WRITE_SLEEP: String = "android.permission.health.WRITE_SLEEP"

    /**
     * 读取速度数据权限，如需权限对象请调用 [PermissionLists.getReadSpeedPermission] 获取
     */
    
    const val READ_SPEED: String = "android.permission.health.READ_SPEED"

    /**
     * 写入速度数据权限，如需权限对象请调用 [PermissionLists.getWriteSpeedPermission] 获取
     */
    
    const val WRITE_SPEED: String = "android.permission.health.WRITE_SPEED"

    /**
     * 读取步数数据权限，如需权限对象请调用 [PermissionLists.getReadStepsPermission] 获取
     */
    
    const val READ_STEPS: String = "android.permission.health.READ_STEPS"

    /**
     * 写入步数数据权限，如需权限对象请调用 [PermissionLists.getWriteStepsPermission] 获取
     */
    
    const val WRITE_STEPS: String = "android.permission.health.WRITE_STEPS"

    /**
     * 读取消耗的卡路里总数数据权限，如需权限对象请调用 [PermissionLists.getReadTotalCaloriesBurnedPermission] 获取
     */
    
    const val READ_TOTAL_CALORIES_BURNED: String = "android.permission.health.READ_TOTAL_CALORIES_BURNED"

    /**
     * 写入消耗的卡路里总数数据权限，如需权限对象请调用 [PermissionLists.getWriteTotalCaloriesBurnedPermission] 获取
     */
    
    const val WRITE_TOTAL_CALORIES_BURNED: String = "android.permission.health.WRITE_TOTAL_CALORIES_BURNED"

    /**
     * 读取最大摄氧量数据权限，如需权限对象请调用 [PermissionLists.getReadVo2MaxPermission] 获取
     */
    
    const val READ_VO2_MAX: String = "android.permission.health.READ_VO2_MAX"

    /**
     * 写入最大摄氧量数据权限，如需权限对象请调用 [PermissionLists.getWriteVo2MaxPermission] 获取
     */
    
    const val WRITE_VO2_MAX: String = "android.permission.health.WRITE_VO2_MAX"

    /**
     * 读取体重数据权限，如需权限对象请调用 [PermissionLists.getReadWeightPermission] 获取
     */
    
    const val READ_WEIGHT: String = "android.permission.health.READ_WEIGHT"

    /**
     * 写入体重数据权限，如需权限对象请调用 [PermissionLists.getWriteWeightPermission] 获取
     */
    
    const val WRITE_WEIGHT: String = "android.permission.health.WRITE_WEIGHT"

    /**
     * 读取推轮椅次数数据权限，如需权限对象请调用 [PermissionLists.getReadWheelchairPushesPermission] 获取
     */
    
    const val READ_WHEELCHAIR_PUSHES: String = "android.permission.health.READ_WHEELCHAIR_PUSHES"

    /**
     * 写入推轮椅次数数据权限，如需权限对象请调用 [PermissionLists.getWriteWheelchairPushesPermission] 获取
     */
    
    const val WRITE_WHEELCHAIR_PUSHES: String = "android.permission.health.WRITE_WHEELCHAIR_PUSHES"


    /* ------------------------------------ 我是一条华丽的分割线 ------------------------------------ */
    /**
     * 读取过敏反应数据权限，如需权限对象请调用 [PermissionLists.getReadMedicalDataAllergiesIntolerancesPermission] 获取
     */
    
    const val READ_MEDICAL_DATA_ALLERGIES_INTOLERANCES: String = "android.permission.health.READ_MEDICAL_DATA_ALLERGIES_INTOLERANCES"

    /**
     * 读取病症数据权限，如需权限对象请调用 [PermissionLists.getReadMedicalDataConditionsPermission] 获取
     */
    
    const val READ_MEDICAL_DATA_CONDITIONS: String = "android.permission.health.READ_MEDICAL_DATA_CONDITIONS"

    /**
     * 读取化验结果数据权限，如需权限对象请调用 [PermissionLists.getReadMedicalDataLaboratoryResultsPermission] 获取
     */
    
    const val READ_MEDICAL_DATA_LABORATORY_RESULTS: String = "android.permission.health.READ_MEDICAL_DATA_LABORATORY_RESULTS"

    /**
     * 读取用药情况数据权限，如需权限对象请调用 [PermissionLists.getReadMedicalDataMedicationsPermission] 获取
     */
    
    const val READ_MEDICAL_DATA_MEDICATIONS: String = "android.permission.health.READ_MEDICAL_DATA_MEDICATIONS"

    /**
     * 读取个人详细信息数据权限，如需权限对象请调用 [PermissionLists.getReadMedicalDataPersonalDetailsPermission] 获取
     */
    
    const val READ_MEDICAL_DATA_PERSONAL_DETAILS: String = "android.permission.health.READ_MEDICAL_DATA_PERSONAL_DETAILS"

    /**
     * 读取就医情况数据权限，如需权限对象请调用 [PermissionLists.getReadMedicalDataPractitionerDetailsPermission] 获取
     */
    
    const val READ_MEDICAL_DATA_PRACTITIONER_DETAILS: String = "android.permission.health.READ_MEDICAL_DATA_PRACTITIONER_DETAILS"

    /**
     * 读取怀孕情况数据权限，如需权限对象请调用 [PermissionLists.getReadMedicalDataPregnancyPermission] 获取
     */
    
    const val READ_MEDICAL_DATA_PREGNANCY: String = "android.permission.health.READ_MEDICAL_DATA_PREGNANCY"

    /**
     * 读取医疗程序数据权限，如需权限对象请调用 [PermissionLists.getReadMedicalDataProceduresPermission] 获取
     */
    
    const val READ_MEDICAL_DATA_PROCEDURES: String = "android.permission.health.READ_MEDICAL_DATA_PROCEDURES"

    /**
     * 读取个人生活史数据权限，如需权限对象请调用 [PermissionLists.getReadMedicalDataSocialHistoryPermission] 获取
     */
    
    const val READ_MEDICAL_DATA_SOCIAL_HISTORY: String = "android.permission.health.READ_MEDICAL_DATA_SOCIAL_HISTORY"

    /**
     * 读取疫苗接种数据权限，如需权限对象请调用 [PermissionLists.getReadMedicalDataVaccinesPermission] 获取
     */
    
    const val READ_MEDICAL_DATA_VACCINES: String = "android.permission.health.READ_MEDICAL_DATA_VACCINES"

    /**
     * 读取医师详细信息数据权限，包括地点、预约时间以及就诊组织名称等数据权限，如需权限对象请调用 [PermissionLists.getReadMedicalDataVisitsPermission] 获取
     */
    
    const val READ_MEDICAL_DATA_VISITS: String = "android.permission.health.READ_MEDICAL_DATA_VISITS"

    /**
     * 读取生命体征数据权限，如需权限对象请调用 [PermissionLists.getReadMedicalDataVitalSignsPermission] 获取
     */
    
    const val READ_MEDICAL_DATA_VITAL_SIGNS: String = "android.permission.health.READ_MEDICAL_DATA_VITAL_SIGNS"

    /**
     * 写入所有健康记录数据权限，如需权限对象请调用 [PermissionLists.getWriteMedicalDataPermission] 获取
     */
    
    const val WRITE_MEDICAL_DATA: String = "android.permission.health.WRITE_MEDICAL_DATA"
}