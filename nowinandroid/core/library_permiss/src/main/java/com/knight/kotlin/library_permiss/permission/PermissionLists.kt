package com.knight.kotlin.library_permiss.permission

import android.accessibilityservice.AccessibilityService
import android.app.admin.DeviceAdminReceiver
import android.service.notification.NotificationListenerService
import android.util.LruCache
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.permission.common.StandardDangerousPermission
import com.knight.kotlin.library_permiss.permission.dangerous.AccessBackgroundLocationPermission
import com.knight.kotlin.library_permiss.permission.dangerous.AccessMediaLocationPermission
import com.knight.kotlin.library_permiss.permission.dangerous.BluetoothAdvertisePermission
import com.knight.kotlin.library_permiss.permission.dangerous.BluetoothConnectPermission
import com.knight.kotlin.library_permiss.permission.dangerous.BluetoothScanPermission
import com.knight.kotlin.library_permiss.permission.dangerous.BodySensorsBackgroundPermission
import com.knight.kotlin.library_permiss.permission.dangerous.GetInstalledAppsPermission
import com.knight.kotlin.library_permiss.permission.dangerous.NearbyWifiDevicesPermission
import com.knight.kotlin.library_permiss.permission.dangerous.PostNotificationsPermission
import com.knight.kotlin.library_permiss.permission.dangerous.ReadExternalStoragePermission
import com.knight.kotlin.library_permiss.permission.dangerous.ReadMediaAudioPermission
import com.knight.kotlin.library_permiss.permission.dangerous.ReadMediaImagesPermission
import com.knight.kotlin.library_permiss.permission.dangerous.ReadMediaVideoPermission
import com.knight.kotlin.library_permiss.permission.dangerous.ReadMediaVisualUserSelectedPermission
import com.knight.kotlin.library_permiss.permission.dangerous.ReadPhoneNumbersPermission
import com.knight.kotlin.library_permiss.permission.dangerous.WriteExternalStoragePermission
import com.knight.kotlin.library_permiss.permission.special.AccessNotificationPolicyPermission
import com.knight.kotlin.library_permiss.permission.special.BindAccessibilityServicePermission
import com.knight.kotlin.library_permiss.permission.special.BindDeviceAdminPermission
import com.knight.kotlin.library_permiss.permission.special.BindNotificationListenerServicePermission
import com.knight.kotlin.library_permiss.permission.special.BindVpnServicePermission
import com.knight.kotlin.library_permiss.permission.special.ManageExternalStoragePermission
import com.knight.kotlin.library_permiss.permission.special.NotificationServicePermission
import com.knight.kotlin.library_permiss.permission.special.PackageUsageStatsPermission
import com.knight.kotlin.library_permiss.permission.special.PictureInPicturePermission
import com.knight.kotlin.library_permiss.permission.special.RequestIgnoreBatteryOptimizationsPermission
import com.knight.kotlin.library_permiss.permission.special.RequestInstallPackagesPermission
import com.knight.kotlin.library_permiss.permission.special.ScheduleExactAlarmPermission
import com.knight.kotlin.library_permiss.permission.special.SystemAlertWindowPermission
import com.knight.kotlin.library_permiss.permission.special.UseFullScreenIntentPermission
import com.knight.kotlin.library_permiss.permission.special.WriteSettingsPermission
import com.knight.kotlin.library_permiss.tools.PermissionVersion


/**
 * @author created by luguian
 * @organize
 * @Date 2025/7/11 9:25
 * @descript:危险权限和特殊权限清单，参考 {@link Manifest.permission}
 * https://developer.android.google.cn/reference/android/Manifest.permission?hl=zh_cn
 * https://developer.android.google.cn/guide/topics/permissions/overview?hl=zh-cn#normal-dangerous
 * http://www.taf.org.cn/upload/AssociationStandard/TTAF%20004-2017%20Android%E6%9D%83%E9%99%90%E8%B0%83%E7%94%A8%E5%BC%80%E5%8F%91%E8%80%85%E6%8C%87%E5%8D%97.pdf
 * 
 * 
 */
object PermissionLists {
    /** 权限数量  */
    private const val PERMISSION_COUNT = 54

    /**
     * 权限对象缓存集合
     *
     * 这里解释一下为什么将 IPermission 对象缓存到集合中？而不是定义成静态变量或者常量？有几个原因：
     *
     * 1. 如果直接定义成常量或静态变量，会有一个问题，如果项目开启混淆模式（minifyEnabled = true）情况下，
     * 未使用到的常量或者静态变量仍然会被保留，不知道 Android Studio 为什么要那么做，但是问题终归还是个问题
     * 目前我能找到的最好的解决方式是定义成静态方法，后续如果静态方法没有被调用，后续代码混淆的时候就会被剔除掉。
     * 2. 如果直接定义成常量或静态变量，还有另外一个问题，就是一旦有谁第一次访问到本类，就会初始化很多对象，
     * 不管这个权限有没有用到，都会在第一次访问的时候初始化完，这样对性能其实不太好的，虽然这点性能微不足道，
     * 但是本着能省一点是一点的原则，所以搞了一个静态集合来存放这些权限对象，调用的时候发现没有再去创建。
     */
    private val PERMISSION_CACHE_MAP: LruCache<String, IPermission> = LruCache(PERMISSION_COUNT)

    /**
     * 获取缓存的权限对象
     *
     * @param permissionName            权限名称
     */
    
    private fun getCachePermission( permissionName: String): IPermission? {
        return PERMISSION_CACHE_MAP.get(permissionName)
    }

    /**
     * 添加权限对象到缓存中
     *
     * @param permission                权限对象
     */
    private fun putCachePermission( permission: IPermission): IPermission {
        PERMISSION_CACHE_MAP.put(permission.getPermissionName(), permission)
        return permission
    }

    /**
     * 读取应用列表权限（危险权限，电信终端产业协会联合各大中国手机厂商搞的一个权限）
     *
     * Github issue 地址：https://github.com/getActivity/XXPermissions/issues/175
     * 移动终端应用软件列表权限实施指南：http://www.taf.org.cn/StdDetail.aspx?uid=3A7D6656-43B8-4C46-8871-E379A3EA1D48&stdType=TAF
     *
     * 需要注意的是：
     * 1. 需要在清单文件中注册 QUERY_ALL_PACKAGES 权限，否则在 Android 11 上面就算申请成功也是获取不到第三方安装列表信息的
     * 2. 这个权限在有的手机上面是授予状态，在有的手机上面是还没有授予，在有的手机上面是无法申请，能支持申请该权限的的厂商系统版本有：
     * 华为：Harmony 3.0.0 及以上版本，Harmony 2.0.1 实测不行
     * 荣耀：Magic UI 6.0 及以上版本，Magic UI 5.0 实测不行
     * 小米：Miui 13 及以上版本，Miui 12 实测不行，经过验证 miui 上面默认会授予此权限
     * OPPO：(ColorOs 12 及以上版本 && Android 11+) || (ColorOs 11.1 及以上版本 && Android 12+)
     * VIVO：虽然没有申请这个权限的通道，但是读取已安装第三方应用列表是没有问题的，没有任何限制
     * 真我：realme UI 3.0 及以上版本，realme UI 2.0 实测不行
     */
    
    fun getGetInstalledAppsPermission(): IPermission {
        val permission = getCachePermission(GetInstalledAppsPermission.PERMISSION_NAME)
        if (permission != null) {
            return permission
        }
        return putCachePermission(GetInstalledAppsPermission())
    }

    /**
     * 全屏通知权限（特殊权限，Android 14 新增的权限）
     *
     * 需要注意的是，如果你的应用需要上架 GooglePlay，请慎重添加此权限，相关文档介绍如下：
     * 1. 了解前台服务和全屏 intent 要求：https://support.google.com/googleplay/android-developer/answer/13392821?hl=zh-Hans
     * 2. Google Play 对 Android 14 全屏 Intent 的要求：https://orangeoma.zendesk.com/hc/en-us/articles/14126775576988-Google-Play-requirements-on-Full-screen-intent-for-Android-14
     */
    
    fun getUseFullScreenIntentPermission(): IPermission {
        val permission = getCachePermission(UseFullScreenIntentPermission.PERMISSION_NAME)
        if (permission != null) {
            return permission
        }
        return putCachePermission(UseFullScreenIntentPermission())
    }

    /**
     * 闹钟权限（特殊权限，Android 12 新增的权限）
     *
     * 需要注意的是：这个权限和其他特殊权限不同的是，默认已经是授予状态，用户也可以手动撤销授权
     * 官方文档介绍：https://developer.android.google.cn/about/versions/12/behavior-changes-12?hl=zh_cn#exact-alarm-permission
     */
    
    fun getScheduleExactAlarmPermission(): IPermission {
        val permission = getCachePermission(ScheduleExactAlarmPermission.PERMISSION_NAME)
        if (permission != null) {
            return permission
        }
        return putCachePermission(ScheduleExactAlarmPermission())
    }

    /**
     * 所有文件访问权限（特殊权限，Android 11 新增的权限）
     *
     * 为了兼容 Android 11 以下版本，需要在清单文件中注册
     * [PermissionNames.READ_EXTERNAL_STORAGE] 和 [PermissionNames.WRITE_EXTERNAL_STORAGE] 权限
     *
     * 如果你的应用需要上架 GooglePlay，那么需要详细阅读谷歌应用商店的政策：
     * https://support.google.com/googleplay/android-developer/answer/9956427
     */
    
    fun getManageExternalStoragePermission(): IPermission {
        val permission = getCachePermission(ManageExternalStoragePermission.PERMISSION_NAME)
        if (permission != null) {
            return permission
        }
        return putCachePermission(ManageExternalStoragePermission())
    }

    /**
     * 安装应用权限（特殊权限，Android 8.0 新增的权限）
     *
     * Android 11 特性调整，安装外部来源应用需要重启 App：https://cloud.tencent.com/developer/news/637591
     * 经过实践，Android 12 已经修复了此问题，授权或者取消授权后应用并不会重启
     */
    
    fun getRequestInstallPackagesPermission(): IPermission {
        val permission = getCachePermission(RequestInstallPackagesPermission.PERMISSION_NAME)
        if (permission != null) {
            return permission
        }
        return putCachePermission(RequestInstallPackagesPermission())
    }

    /**
     * 画中画权限（特殊权限，Android 8.0 新增的权限，注意此权限不需要在清单文件中注册也能申请）
     *
     * 需要注意的是：这个权限和其他特殊权限不同的是，默认已经是授予状态，用户也可以手动撤销授权
     */
    
    fun getPictureInPicturePermission(): IPermission {
        val permission = getCachePermission(PictureInPicturePermission.PERMISSION_NAME)
        if (permission != null) {
            return permission
        }
        return putCachePermission(PictureInPicturePermission())
    }

    /**
     * 悬浮窗权限（特殊权限，Android 6.0 新增的权限，但是有些国产的厂商在 Android 6.0 之前的设备就兼容了）
     *
     * 在 Android 10 及之前的版本能跳转到应用悬浮窗设置页面，而在 Android 11 及之后的版本只能跳转到系统设置悬浮窗管理列表了
     * 官方解释：https://developer.android.google.cn/reference/android/provider/Settings#ACTION_MANAGE_OVERLAY_PERMISSION
     */
    
    fun getSystemAlertWindowPermission(): IPermission {
        val permission = getCachePermission(SystemAlertWindowPermission.PERMISSION_NAME)
        if (permission != null) {
            return permission
        }
        return putCachePermission(SystemAlertWindowPermission())
    }

    /**
     * 写入系统设置权限（特殊权限，Android 6.0 新增的权限）
     */
    @Suppress("unused")
    
    fun getWriteSettingsPermission(): IPermission {
        val permission = getCachePermission(WriteSettingsPermission.PERMISSION_NAME)
        if (permission != null) {
            return permission
        }
        return putCachePermission(WriteSettingsPermission())
    }

    /**
     * 请求忽略电池优化选项权限（特殊权限，Android 6.0 新增的权限）
     */
    
    fun getRequestIgnoreBatteryOptimizationsPermission(): IPermission {
        val permission = getCachePermission(RequestIgnoreBatteryOptimizationsPermission.PERMISSION_NAME)
        if (permission != null) {
            return permission
        }
        return putCachePermission(RequestIgnoreBatteryOptimizationsPermission())
    }

    /**
     * 勿扰权限，可控制手机响铃模式【静音，震动】（特殊权限，Android 6.0 新增的权限）
     */
    
    fun getAccessNotificationPolicyPermission(): IPermission {
        val permission = getCachePermission(AccessNotificationPolicyPermission.PERMISSION_NAME)
        if (permission != null) {
            return permission
        }
        return putCachePermission(AccessNotificationPolicyPermission())
    }

    /**
     * 查看应用使用情况权限，简称使用统计权限（特殊权限，Android 5.0 新增的权限）
     */
    
    fun getPackageUsageStatsPermission(): IPermission {
        val permission = getCachePermission(PackageUsageStatsPermission.PERMISSION_NAME)
        if (permission != null) {
            return permission
        }
        return putCachePermission(PackageUsageStatsPermission())
    }

    /**
     * 通知栏监听权限（特殊权限，Android 4.3 新增的权限，注意此权限不需要在清单文件中注册也能申请）
     *
     * @param notificationListenerServiceClass             通知监听器的 Service 类型
     */
    
    fun getBindNotificationListenerServicePermission( notificationListenerServiceClass: Class<out NotificationListenerService?>): IPermission {
        // 该对象不会纳入到缓存的集合中，这是它携带了具体的参数，只有无参的才能丢到缓存的集合中
        return BindNotificationListenerServicePermission(notificationListenerServiceClass.name)
    }

    /**
     * VPN 权限（特殊权限，Android 4.0 新增的权限，注意此权限不需要在清单文件中注册也能申请）
     */
    
    fun getBindVpnServicePermission(): IPermission {
        val permission = getCachePermission(BindVpnServicePermission.PERMISSION_NAME)
        if (permission != null) {
            return permission
        }
        return putCachePermission(BindVpnServicePermission())
    }

    /**
     * 通知栏权限（特殊权限，只有 Android 4.4 及以上设备才能判断到权限状态，注意此权限不需要在清单文件中注册也能申请）
     *
     * @param channelId         通知渠道 id
     */
    
    fun getNotificationServicePermission( channelId: String?): IPermission {
        // 该对象不会纳入到缓存的集合中，这是它携带了具体的参数，只有无参的才能丢到缓存的集合中
        return NotificationServicePermission(channelId)
    }

    /**
     * 同上
     */
    
    fun getNotificationServicePermission(): IPermission {
        val permission = getCachePermission(NotificationServicePermission.PERMISSION_NAME)
        if (permission != null) {
            return permission
        }
        return putCachePermission(NotificationServicePermission())
    }

    /**
     * 无障碍服务权限（特殊权限，Android 4.1 新增的权限，注意此权限不需要在清单文件中注册也能申请）
     *
     * @param accessibilityServiceClass                                 无障碍 Service 类
     */
    
    fun getBindAccessibilityServicePermission( accessibilityServiceClass: Class<out AccessibilityService?>): IPermission {
        return BindAccessibilityServicePermission(accessibilityServiceClass)
    }

    /**
     * 设备管理权限（特殊权限，Android 2.2 新增的权限，注意此权限不需要在清单文件中注册也能申请）
     *
     * @param deviceAdminReceiverClass              设备管理器的 BroadcastReceiver 类
     * @param extraAddExplanation                   申请设备管理器权限的附加说明
     */
    
    fun getBindDeviceAdminPermission( deviceAdminReceiverClass: Class<out DeviceAdminReceiver?>,  extraAddExplanation: String?): IPermission {
        return BindDeviceAdminPermission(deviceAdminReceiverClass, extraAddExplanation)
    }

    /**
     * 同上
     */
    
    fun getBindDeviceAdminPermission( deviceAdminReceiverClass: Class<out DeviceAdminReceiver?>): IPermission {
        return BindDeviceAdminPermission(deviceAdminReceiverClass, null)
    }

    /* ------------------------------------ 我是一条华丽的分割线 ------------------------------------ */
    /**
     * 访问部分照片和视频的权限（Android 14.0 新增的权限）
     */
    
    fun getReadMediaVisualUserSelectedPermission(): IPermission {
        val permission = getCachePermission(ReadMediaVisualUserSelectedPermission.PERMISSION_NAME)
        if (permission != null) {
            return permission
        }
        return putCachePermission(ReadMediaVisualUserSelectedPermission())
    }

    /**
     * 发送通知权限（Android 13.0 新增的权限）
     *
     * 为了兼容 Android 13 以下版本，框架会自动申请 [PermissionNames.NOTIFICATION_SERVICE] 权限
     */
    
    fun getPostNotificationsPermission(): IPermission {
        val permission = getCachePermission(PostNotificationsPermission.PERMISSION_NAME)
        if (permission != null) {
            return permission
        }
        return putCachePermission(PostNotificationsPermission())
    }

    /**
     * WIFI 权限（Android 13.0 新增的权限）
     *
     * 需要在清单文件中加入 android:usesPermissionFlags="neverForLocation" 属性（表示不推导设备地理位置）
     * 否则就会导致在没有定位权限的情况下扫描不到附近的 WIFI 设备，这个是经过测试的，下面是清单权限注册案例，请参考以下进行注册
     * <uses-permission android:name="android.permission.NEARBY_WIFI_DEVICES" android:usesPermissionFlags="neverForLocation" tools:targetApi="s"></uses-permission>
     *
     * 为了兼容 Android 13 以下版本，需要清单文件中注册 [PermissionNames.ACCESS_FINE_LOCATION] 权限
     * 还有 Android 13 以下设备，使用 WIFI 需要 [PermissionNames.ACCESS_FINE_LOCATION] 权限，框架会自动在旧的安卓设备上自动添加此权限进行动态申请
     */
    
    fun getNearbyWifiDevicesPermission(): IPermission {
        val permission = getCachePermission(NearbyWifiDevicesPermission.PERMISSION_NAME)
        if (permission != null) {
            return permission
        }
        return putCachePermission(NearbyWifiDevicesPermission())
    }

    /**
     * 后台传感器权限（Android 13.0 新增的权限）
     *
     * 需要注意的是：
     * 1. 一旦你申请了该权限，在授权的时候，需要选择《始终允许》，而不能选择《仅在使用中允许》
     * 2. 如果你的 App 只在前台状态下使用传感器功能，请不要申请该权限（后台传感器权限）
     */
    
    fun getBodySensorsBackgroundPermission(): IPermission {
        val permission = getCachePermission(BodySensorsBackgroundPermission.PERMISSION_NAME)
        if (permission != null) {
            return permission
        }
        return putCachePermission(BodySensorsBackgroundPermission())
    }

    /**
     * 读取图片权限（Android 13.0 新增的权限）
     *
     * 为了兼容 Android 13 以下版本，需要在清单文件中注册 [PermissionNames.READ_EXTERNAL_STORAGE] 权限
     */
    
    fun getReadMediaImagesPermission(): IPermission {
        val permission = getCachePermission(ReadMediaImagesPermission.PERMISSION_NAME)
        if (permission != null) {
            return permission
        }
        return putCachePermission(ReadMediaImagesPermission())
    }

    /**
     * 读取视频权限（Android 13.0 新增的权限）
     *
     * 为了兼容 Android 13 以下版本，需要在清单文件中注册 [PermissionNames.READ_EXTERNAL_STORAGE] 权限
     */
    
    fun getReadMediaVideoPermission(): IPermission {
        val permission = getCachePermission(ReadMediaVideoPermission.PERMISSION_NAME)
        if (permission != null) {
            return permission
        }
        return putCachePermission(ReadMediaVideoPermission())
    }

    /**
     * 读取音频权限（Android 13.0 新增的权限）
     *
     * 为了兼容 Android 13 以下版本，需要在清单文件中注册 [PermissionNames.READ_EXTERNAL_STORAGE] 权限
     */
    
    fun getReadMediaAudioPermission(): IPermission {
        val permission = getCachePermission(ReadMediaAudioPermission.PERMISSION_NAME)
        if (permission != null) {
            return permission
        }
        return putCachePermission(ReadMediaAudioPermission())
    }

    /**
     * 蓝牙扫描权限（Android 12.0 新增的权限）
     *
     * 需要在清单文件中加入 android:usesPermissionFlags="neverForLocation" 属性（表示不推导设备地理位置）
     * 否则就会导致在没有定位权限的情况下扫描不到附近的蓝牙设备，这个是经过测试的，下面是清单权限注册案例，请参考以下进行注册
     * <uses-permission android:name="android.permission.BLUETOOTH_SCAN" android:usesPermissionFlags="neverForLocation" tools:targetApi="s"></uses-permission>
     *
     * 为了兼容 Android 12 以下版本，需要清单文件中注册 [Manifest.permission.BLUETOOTH_ADMIN] 权限
     * 还有 Android 12 以下设备，获取蓝牙扫描结果需要 [PermissionNames.ACCESS_FINE_LOCATION] 权限，框架会自动在旧的安卓设备上自动添加此权限进行动态申请
     */
    
    fun getBluetoothScanPermission(): IPermission {
        val permission = getCachePermission(BluetoothScanPermission.PERMISSION_NAME)
        if (permission != null) {
            return permission
        }
        return putCachePermission(BluetoothScanPermission())
    }

    /**
     * 蓝牙连接权限（Android 12.0 新增的权限）
     *
     * 为了兼容 Android 12 以下版本，需要在清单文件中注册 [Manifest.permission.BLUETOOTH] 权限
     */
    
    fun getBluetoothConnectPermission(): IPermission {
        val permission = getCachePermission(BluetoothConnectPermission.PERMISSION_NAME)
        if (permission != null) {
            return permission
        }
        return putCachePermission(BluetoothConnectPermission())
    }

    /**
     * 蓝牙广播权限（Android 12.0 新增的权限）
     *
     * 将当前设备的蓝牙进行广播，供其他设备扫描时需要用到该权限
     * 为了兼容 Android 12 以下版本，需要在清单文件中注册 [Manifest.permission.BLUETOOTH_ADMIN] 权限
     */
    
    fun getBluetoothAdvertisePermission(): IPermission {
        val permission = getCachePermission(BluetoothAdvertisePermission.PERMISSION_NAME)
        if (permission != null) {
            return permission
        }
        return putCachePermission(BluetoothAdvertisePermission())
    }

    /**
     * 在后台获取位置权限（Android 10.0 新增的权限）
     *
     * 需要注意的是：
     * 1. 一旦你申请了该权限，在授权的时候，需要选择《始终允许》，而不能选择《仅在使用中允许》
     * 2. 如果你的 App 只在前台状态下使用定位功能，没有在后台使用的场景，请不要申请该权限
     */
    
    fun getAccessBackgroundLocationPermission(): IPermission {
        val permission = getCachePermission(AccessBackgroundLocationPermission.PERMISSION_NAME)
        if (permission != null) {
            return permission
        }
        return putCachePermission(AccessBackgroundLocationPermission())
    }

    /**
     * 获取活动步数权限（Android 10.0 新增的权限）
     *
     * 需要注意的是：Android 10 以下不需要传感器（BODY_SENSORS）权限也能获取到步数
     * Github issue 地址：https://github.com/getActivity/XXPermissions/issues/150
     */
    
    fun getActivityRecognitionPermission(): IPermission {
        val permissionName: String = PermissionNames.ACTIVITY_RECOGNITION
        val permission = getCachePermission(permissionName)
        if (permission != null) {
            return permission
        }
        return putCachePermission(StandardDangerousPermission(permissionName, PermissionVersion.ANDROID_10))
    }

    /**
     * 访问媒体的位置信息权限（Android 10.0 新增的权限）
     *
     * 需要注意的是：如果这个权限申请成功了但是不能正常读取照片的地理信息，那么需要先申请存储权限，具体可分别下面两种情况：
     *
     * 1. 如果适配了分区存储的情况下：
     * 1) 如果项目 targetSdkVersion <= 32 需要申请 [PermissionNames.READ_EXTERNAL_STORAGE]
     * 2) 如果项目 targetSdkVersion >= 33 需要申请 [PermissionNames.READ_MEDIA_IMAGES] 或 [PermissionNames.READ_MEDIA_VIDEO]，并且需要全部授予，不能部分授予
     *
     * 2. 如果没有适配分区存储的情况下：
     * 1) 如果项目 targetSdkVersion <= 29 需要申请 [PermissionNames.READ_EXTERNAL_STORAGE]
     * 2) 如果项目 targetSdkVersion >= 30 需要申请 [PermissionNames.MANAGE_EXTERNAL_STORAGE]
     */
    
    fun getAccessMediaLocationPermission(): IPermission {
        val permission = getCachePermission(AccessMediaLocationPermission.PERMISSION_NAME)
        if (permission != null) {
            return permission
        }
        return putCachePermission(AccessMediaLocationPermission())
    }

    /**
     * 允许呼叫应用继续在另一个应用中启动的呼叫权限（Android 9.0 新增的权限）
     *
     * 需要注意：此权限在一些无法拨打电话的设备（例如：小米平板 5）上面申请，系统会直接回调失败，如有进行申请，请留意处理权限申请失败的情况
     */
    
    fun getAcceptHandoverPermission(): IPermission {
        val permissionName: String = PermissionNames.ACCEPT_HANDOVER
        val permission = getCachePermission(permissionName)
        if (permission != null) {
            return permission
        }
        return putCachePermission(StandardDangerousPermission(permissionName, PermissionGroups.PHONE, PermissionVersion.ANDROID_9))
    }

    /**
     * 读取手机号码权限（Android 8.0 新增的权限）
     *
     * 需要注意：此权限在一些无法拨打电话的设备（例如：小米平板 5）上面申请，系统会直接回调成功，但是这非必然，如有进行申请，还需留意处理权限申请失败的情况
     *
     * 为了兼容 Android 8.0 以下版本，需要在清单文件中注册 [PermissionNames.READ_PHONE_STATE] 权限
     */
    
    fun getReadPhoneNumbersPermission(): IPermission {
        val permission = getCachePermission(ReadPhoneNumbersPermission.PERMISSION_NAME)
        if (permission != null) {
            return permission
        }
        return putCachePermission(ReadPhoneNumbersPermission())
    }

    /**
     * 接听电话权限（Android 8.0 新增的权限，Android 8.0 以下可以采用模拟耳机按键事件来实现接听电话，这种方式不需要权限）
     *
     * 需要注意：此权限在一些无法拨打电话的设备（例如：小米平板 5）上面申请，系统会直接回调失败，如有进行申请，请留意处理权限申请失败的情况
     */
    
    fun getAnswerPhoneCallsPermission(): IPermission {
        val permissionName: String = PermissionNames.ANSWER_PHONE_CALLS
        val permission = getCachePermission(permissionName)
        if (permission != null) {
            return permission
        }
        return putCachePermission(StandardDangerousPermission(permissionName, PermissionGroups.PHONE, PermissionVersion.ANDROID_8))
    }

    /**
     * 读取外部存储权限
     */
    
    fun getReadExternalStoragePermission(): IPermission {
        val permission = getCachePermission(ReadExternalStoragePermission.PERMISSION_NAME)
        if (permission != null) {
            return permission
        }
        return putCachePermission(ReadExternalStoragePermission())
    }

    /**
     * 写入外部存储权限（注意：这个权限在 targetSdk >= Android 11 并且 Android 11 及以上的设备上面不起作用，请适配分区存储特性代替权限申请）
     */
    
    fun getWriteExternalStoragePermission(): IPermission {
        val permission = getCachePermission(WriteExternalStoragePermission.PERMISSION_NAME)
        if (permission != null) {
            return permission
        }
        return putCachePermission(WriteExternalStoragePermission())
    }

    /**
     * 相机权限
     */
    
    fun getCameraPermission(): IPermission {
        val permissionName: String = PermissionNames.CAMERA
        val permission = getCachePermission(permissionName)
        if (permission != null) {
            return permission
        }
        return putCachePermission(StandardDangerousPermission(permissionName, PermissionVersion.ANDROID_6))
    }

    /**
     * 麦克风权限
     */
    
    fun getRecordAudioPermission(): IPermission {
        val permissionName: String = PermissionNames.RECORD_AUDIO
        val permission = getCachePermission(permissionName)
        if (permission != null) {
            return permission
        }
        return putCachePermission(StandardDangerousPermission(permissionName, PermissionVersion.ANDROID_6))
    }

    /**
     * 获取精确位置权限
     */
    
    fun getAccessFineLocationPermission(): IPermission {
        val permissionName: String = PermissionNames.ACCESS_FINE_LOCATION
        val permission = getCachePermission(permissionName)
        if (permission != null) {
            return permission
        }
        return putCachePermission(StandardDangerousPermission(permissionName, PermissionGroups.LOCATION, PermissionVersion.ANDROID_6))
    }

    /**
     * 获取粗略位置权限
     */
    
    fun getAccessCoarseLocationPermission(): IPermission {
        val permissionName: String = PermissionNames.ACCESS_COARSE_LOCATION
        val permission = getCachePermission(permissionName)
        if (permission != null) {
            return permission
        }
        return putCachePermission(StandardDangerousPermission(permissionName, PermissionGroups.LOCATION, PermissionVersion.ANDROID_6))
    }

    /**
     * 读取联系人权限
     */
    
    fun getReadContactsPermission(): IPermission {
        val permissionName: String = PermissionNames.READ_CONTACTS
        val permission = getCachePermission(permissionName)
        if (permission != null) {
            return permission
        }
        return putCachePermission(StandardDangerousPermission(permissionName, PermissionGroups.CONTACTS, PermissionVersion.ANDROID_6))
    }

    /**
     * 修改联系人权限
     */
    
    fun getWriteContactsPermission(): IPermission {
        val permissionName: String = PermissionNames.WRITE_CONTACTS
        val permission = getCachePermission(permissionName)
        if (permission != null) {
            return permission
        }
        return putCachePermission(StandardDangerousPermission(permissionName, PermissionGroups.CONTACTS, PermissionVersion.ANDROID_6))
    }

    /**
     * 访问账户列表权限
     */
    
    fun getGetAccountsPermission(): IPermission {
        val permissionName: String = PermissionNames.GET_ACCOUNTS
        val permission = getCachePermission(permissionName)
        if (permission != null) {
            return permission
        }
        return putCachePermission(StandardDangerousPermission(permissionName, PermissionGroups.CONTACTS, PermissionVersion.ANDROID_6))
    }

    /**
     * 读取日历权限
     */
    
    fun getReadCalendarPermission(): IPermission {
        val permissionName: String = PermissionNames.READ_CALENDAR
        val permission = getCachePermission(permissionName)
        if (permission != null) {
            return permission
        }
        return putCachePermission(StandardDangerousPermission(permissionName, PermissionGroups.CALENDAR, PermissionVersion.ANDROID_6))
    }

    /**
     * 修改日历权限
     */
    
    fun getWriteCalendarPermission(): IPermission {
        val permissionName: String = PermissionNames.WRITE_CALENDAR
        val permission = getCachePermission(permissionName)
        if (permission != null) {
            return permission
        }
        return putCachePermission(StandardDangerousPermission(permissionName, PermissionGroups.CALENDAR, PermissionVersion.ANDROID_6))
    }

    /**
     * 读取电话状态权限，需要注意的是：
     *
     * 1. 这个权限在某些手机上面是没办法获取到的，因为某些系统禁止应用获得该权限
     * 所以你要是申请了这个权限之后没有弹授权框，而是直接回调授权失败方法
     * 请不要惊慌，这个不是 Bug、不是 Bug、不是 Bug，而是正常现象
     * 后续情况汇报：有人反馈在 iQOO 手机上面获取不到该权限，在清单文件加入下面这个权限就可以了（这里只是做记录，并不代表这种方式就一定有效果）
     * <uses-permission android:name="android.permission.READ_PRIVILEGED_PHONE_STATE"></uses-permission>
     *
     * 2. 这个权限在某些手机上面申请是直接通过的，但是系统没有弹出授权对话框，实际上也是没有授权
     * 这个也不是 Bug，而是系统故意就是这么做的，你要问我怎么办，我只能说胳膊拗不过大腿
     * Github issue 地址：https://github.com/getActivity/XXPermissions/issues/369
     */
    
    fun getReadPhoneStatePermission(): IPermission {
        val permissionName: String = PermissionNames.READ_PHONE_STATE
        val permission = getCachePermission(permissionName)
        if (permission != null) {
            return permission
        }
        return putCachePermission(StandardDangerousPermission(permissionName, PermissionGroups.PHONE, PermissionVersion.ANDROID_6))
    }

    /**
     * 拨打电话权限
     *
     * 需要注意：此权限在一些无法拨打电话的设备（例如：小米平板 5）上面申请，系统会直接回调失败，如有进行申请，请留意处理权限申请失败的情况
     */
    
    fun getCallPhonePermission(): IPermission {
        val permissionName: String = PermissionNames.CALL_PHONE
        val permission = getCachePermission(permissionName)
        if (permission != null) {
            return permission
        }
        return putCachePermission(StandardDangerousPermission(permissionName, PermissionGroups.PHONE, PermissionVersion.ANDROID_6))
    }

    /**
     * 读取通话记录权限
     *
     * 需要注意：此权限在一些无法拨打电话的设备（例如：小米平板 5）上面申请，系统会直接回调失败，如有进行申请，请留意处理权限申请失败的情况
     */
    
    fun getReadCallLogPermission(): IPermission {
        val permissionName: String = PermissionNames.READ_CALL_LOG
        val permission = getCachePermission(permissionName)
        if (permission != null) {
            return permission
        }
        // 注意：在 Android 9.0 的时候，通话记录相关的权限已经归到一个单独的权限组了，但是在 Android 9.0 之前，读写通话记录权限归属电话权限组
        val permissionGroup = if (PermissionVersion.isAndroid9()) PermissionGroups.CALL_LOG else PermissionGroups.PHONE
        return putCachePermission(StandardDangerousPermission(permissionName, permissionGroup, PermissionVersion.ANDROID_6))
    }

    /**
     * 修改通话记录权限
     *
     * 需要注意：此权限在一些无法拨打电话的设备（例如：小米平板 5）上面申请，系统会直接回调失败，如有进行申请，请留意处理权限申请失败的情况
     */
    
    fun getWriteCallLogPermission(): IPermission {
        val permissionName: String = PermissionNames.WRITE_CALL_LOG
        val permission = getCachePermission(permissionName)
        if (permission != null) {
            return permission
        }
        // 注意：在 Android 9.0 的时候，通话记录相关的权限已经归到一个单独的权限组了，但是在 Android 9.0 之前，读写通话记录权限归属电话权限组
        val permissionGroup = if (PermissionVersion.isAndroid9()) PermissionGroups.CALL_LOG else PermissionGroups.PHONE
        return putCachePermission(StandardDangerousPermission(permissionName, permissionGroup, PermissionVersion.ANDROID_6))
    }

    /**
     * 添加语音邮件权限
     */
    
    fun getAddVoicemailPermission(): IPermission {
        val permissionName: String = PermissionNames.ADD_VOICEMAIL
        val permission = getCachePermission(permissionName)
        if (permission != null) {
            return permission
        }
        return putCachePermission(StandardDangerousPermission(permissionName, PermissionGroups.PHONE, PermissionVersion.ANDROID_6))
    }

    /**
     * 使用 SIP 视频权限
     */
    
    fun getUseSipPermission(): IPermission {
        val permissionName: String = PermissionNames.USE_SIP
        val permission = getCachePermission(permissionName)
        if (permission != null) {
            return permission
        }
        return putCachePermission(StandardDangerousPermission(permissionName, PermissionGroups.PHONE, PermissionVersion.ANDROID_6))
    }

    /**
     * 处理拨出电话权限
     *
     * 需要注意：此权限在一些无法拨打电话的设备（例如：小米平板 5）上面申请，系统会直接回调失败，如有进行申请，请留意处理权限申请失败的情况
     *
     */
    
    @Deprecated("在 Android 10 已经过时，请见：https://developer.android.google.cn/reference/android/Manifest.permission?hl=zh_cn#PROCESS_OUTGOING_CALLS")
    fun getProcessOutgoingCallsPermission(): IPermission {
        val permissionName: String = PermissionNames.PROCESS_OUTGOING_CALLS
        val permission = getCachePermission(permissionName)
        if (permission != null) {
            return permission
        }
        // 注意：在 Android 9.0 的时候，通话记录相关的权限已经归到一个单独的权限组了，但是在 Android 9.0 之前，读写通话记录权限归属电话权限组
        val permissionGroup = if (PermissionVersion.isAndroid9()) PermissionGroups.CALL_LOG else PermissionGroups.PHONE
        return putCachePermission(StandardDangerousPermission(permissionName, permissionGroup, PermissionVersion.ANDROID_6))
    }

    /**
     * 使用传感器权限
     */
    
    fun getBodySensorsPermission(): IPermission {
        val permissionName: String = PermissionNames.BODY_SENSORS
        val permission = getCachePermission(permissionName)
        if (permission != null) {
            return permission
        }
        return putCachePermission(StandardDangerousPermission(permissionName, PermissionGroups.SENSORS, PermissionVersion.ANDROID_6))
    }

    /**
     * 发送短信权限
     *
     * 需要注意：此权限在一些无法发送短信的设备（例如：小米平板 5）上面申请，系统会直接回调失败，如有进行申请，请留意处理权限申请失败的情况
     */
    
    fun getSendSmsPermission(): IPermission {
        val permissionName: String = PermissionNames.SEND_SMS
        val permission = getCachePermission(permissionName)
        if (permission != null) {
            return permission
        }
        return putCachePermission(StandardDangerousPermission(permissionName, PermissionGroups.SMS, PermissionVersion.ANDROID_6))
    }

    /**
     * 接收短信权限
     *
     * 需要注意：此权限在一些无法发送短信的设备（例如：小米平板 5）上面申请，系统会直接回调失败，如有进行申请，请留意处理权限申请失败的情况
     */
    
    fun getReceiveSmsPermission(): IPermission {
        val permissionName: String = PermissionNames.RECEIVE_SMS
        val permission = getCachePermission(permissionName)
        if (permission != null) {
            return permission
        }
        return putCachePermission(StandardDangerousPermission(permissionName, PermissionGroups.SMS, PermissionVersion.ANDROID_6))
    }

    /**
     * 读取短信权限
     *
     * 需要注意：此权限在一些无法发送短信的设备（例如：小米平板 5）上面申请，系统会直接回调失败，如有进行申请，请留意处理权限申请失败的情况
     */
    
    fun getReadSmsPermission(): IPermission {
        val permissionName: String = PermissionNames.READ_SMS
        val permission = getCachePermission(permissionName)
        if (permission != null) {
            return permission
        }
        return putCachePermission(StandardDangerousPermission(permissionName, PermissionGroups.SMS, PermissionVersion.ANDROID_6))
    }

    /**
     * 接收 WAP 推送消息权限
     *
     * 需要注意：此权限在一些无法发送短信的设备（例如：小米平板 5）上面申请，系统会直接回调成功，但是这非必然，如有进行申请，还需留意处理权限申请失败的情况
     */
    
    fun getReceiveWapPushPermission(): IPermission {
        val permissionName: String = PermissionNames.RECEIVE_WAP_PUSH
        val permission = getCachePermission(permissionName)
        if (permission != null) {
            return permission
        }
        return putCachePermission(StandardDangerousPermission(permissionName, PermissionGroups.SMS, PermissionVersion.ANDROID_6))
    }

    /**
     * 接收彩信权限
     *
     * 需要注意：此权限在一些无法发送短信的设备（例如：小米平板 5）上面申请，系统会直接回调成功，但是这非必然，如有进行申请，还需留意处理权限申请失败的情况
     */
    
    fun getReceiveMmsPermission(): IPermission {
        val permissionName: String = PermissionNames.RECEIVE_MMS
        val permission = getCachePermission(permissionName)
        if (permission != null) {
            return permission
        }
        return putCachePermission(StandardDangerousPermission(permissionName, PermissionGroups.SMS, PermissionVersion.ANDROID_6))
    }
}