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
}