package com.knight.kotlin.library_permiss

import android.Manifest
import android.app.Activity
import android.content.Context
import android.text.TextUtils
import com.knight.kotlin.library_permiss.AndroidVersion.getAndroidVersionCode
import com.knight.kotlin.library_permiss.AndroidVersion.getTargetSdkVersionCode
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid10
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid11
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid12
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid13
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid4_2
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid7
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid8
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.utils.PermissionUtils.containsPermission
import com.knight.kotlin.library_permiss.utils.PermissionUtils.equalsPermission
import com.knight.kotlin.library_permiss.utils.PermissionUtils.isScopedStorage
import java.lang.reflect.Field


/**
 * Author:Knight
 * Time:2022/1/20 15:32
 * Description:PermissionChecker
 */
object PermissionChecker {

    /**
     * 检查 Activity 的状态是否正常
     *
     * @param checkMode         是否是检查模式
     * @return                  是否检查通过
     */
    fun checkActivityStatus(activity: Activity?, checkMode: Boolean): Boolean {
        // 检查当前 Activity 状态是否是正常的，如果不是则不请求权限
        if (activity == null) {
            if (checkMode) {
                // Context 的实例必须是 Activity 对象
                throw IllegalArgumentException("The instance of the context must be an activity object")
            }
            return false
        }
        if (activity.isFinishing) {
            if (checkMode) {
                // 这个 Activity 对象当前不能是关闭状态，这种情况常出现在执行异步请求后申请权限
                // 请自行在外层判断 Activity 状态是否正常之后再进入权限申请
                throw IllegalStateException(
                    "The activity has been finishing, " +
                            "please manually determine the status of the activity"
                )
            }
            return false
        }
        if (isAndroid4_2() && activity.isDestroyed) {
            if (checkMode) {
                // 这个 Activity 对象当前不能是销毁状态，这种情况常出现在执行异步请求后申请权限
                // 请自行在外层判断 Activity 状态是否正常之后再进入权限申请
                throw IllegalStateException(
                    "The activity has been destroyed, " +
                            "please manually determine the status of the activity"
                )
            }
            return false
        }
        return true
    }

    /**
     * 检查传入的权限是否符合要求
     *
     * @param requestPermissions        请求的权限组
     * @param checkMode                 是否是检查模式
     * @return                          是否检查通过
     */
    fun checkPermissionArgument(
        requestPermissions: List<String>?,
        checkMode: Boolean
    ): Boolean {
        if (requestPermissions == null || requestPermissions.isEmpty()) {
            if (checkMode) {
                // 不传任何权限，就想动态申请权限？
                throw IllegalArgumentException("The requested permission cannot be empty")
            }
            return false
        }
        if (getAndroidVersionCode() > AndroidVersion.ANDROID_13) {
            // 如果是 Android 13 后面的版本，则不进行检查
            return true
        }
        if (checkMode) {
            val allPermissions: MutableList<String> = ArrayList()
            val fields = Permission::class.java.declaredFields
            // 在开启代码混淆之后，反射 Permission 类中的字段会得到空的字段数组
            // 这个是因为编译后常量会在代码中直接引用，所以 Permission 常量字段在混淆的时候会被移除掉
            if (fields.size == 0) {
                return true
            }
            for (field: Field in fields) {
                if (String::class.java != field.type) {
                    continue
                }
                try {
                    allPermissions.add(field[null] as String)
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
            }
            for (permission: String in requestPermissions) {
                if (containsPermission(allPermissions, permission)) {
                    continue
                }
                throw IllegalArgumentException(
                    "The " + permission +
                            " is not a dangerous permission or special permission, " +
                            "please do not request dynamically"
                )
            }
        }
        return true
    }

    /**
     * 检查读取媒体位置权限
     */
    fun checkMediaLocationPermission(
        context: Context?,
        requestPermissions: List<String>
    ) {
        // 如果请求的权限中没有包含外部读取媒体位置权限，那么就不符合条件，停止检查
        if (!containsPermission(requestPermissions, Permission.ACCESS_MEDIA_LOCATION)) {
            return
        }
        for (permission: String? in requestPermissions) {
            if ((equalsPermission((permission)!!, Permission.ACCESS_MEDIA_LOCATION)
                        || equalsPermission((permission), Permission.READ_MEDIA_IMAGES)
                        || equalsPermission((permission),Permission.READ_MEDIA_VIDEO)
                        || equalsPermission((permission), Permission.READ_EXTERNAL_STORAGE)
                        || equalsPermission((permission), Permission.WRITE_EXTERNAL_STORAGE)
                        || equalsPermission((permission), Permission.MANAGE_EXTERNAL_STORAGE))
            ) {
                continue
            }
            throw IllegalArgumentException(
                "Because it includes access media location permissions, " +
                        "do not apply for permissions unrelated to access media location"
            )
        }
        if (getTargetSdkVersionCode((context)!!) >= AndroidVersion.ANDROID_13) {
            if (!containsPermission(requestPermissions, Permission.READ_MEDIA_IMAGES) &&
                !containsPermission(requestPermissions, Permission.READ_MEDIA_VIDEO) &&
                !containsPermission(requestPermissions, Permission.MANAGE_EXTERNAL_STORAGE)
            ) {
                // 你需要在外层手动添加 READ_MEDIA_IMAGES 、READ_MEDIA_VIDEO、 MANAGE_EXTERNAL_STORAGE 才可以申请 ACCESS_MEDIA_LOCATION 权限
                throw IllegalArgumentException(
                    (((("You must add " + Permission.READ_MEDIA_IMAGES).toString() + " or " +
                            Permission.MANAGE_EXTERNAL_STORAGE).toString() + " rights to apply for " + Permission.ACCESS_MEDIA_LOCATION).toString() + " rights")
                )
            }
        } else {
            if (!containsPermission(requestPermissions, Permission.READ_EXTERNAL_STORAGE) &&
                !containsPermission(requestPermissions, Permission.MANAGE_EXTERNAL_STORAGE)
            ) {
                // 你需要在外层手动添加 READ_EXTERNAL_STORAGE 或者 MANAGE_EXTERNAL_STORAGE 才可以申请 ACCESS_MEDIA_LOCATION 权限
                throw IllegalArgumentException(
                    (((("You must add " + Permission.READ_EXTERNAL_STORAGE).toString() + " or " +
                            Permission.MANAGE_EXTERNAL_STORAGE).toString() + " rights to apply for " + Permission.ACCESS_MEDIA_LOCATION).toString() + " rights")
                )
            }
        }
    }

    /**
     * 检查存储权限
     */
    fun checkStoragePermission(
        context: Context?,  requestPermissions: List<String>,
        androidManifestInfo: AndroidManifestInfo?
    ) {
        // 如果请求的权限中没有包含外部存储相关的权限，那么就不符合条件，停止检查
        if ((!containsPermission(requestPermissions, Permission.READ_MEDIA_IMAGES) &&
                    !containsPermission(requestPermissions, Permission.READ_MEDIA_VIDEO) &&
                    !containsPermission(requestPermissions, Permission.READ_MEDIA_AUDIO) &&
                    !containsPermission(requestPermissions, Permission.MANAGE_EXTERNAL_STORAGE) &&
                    !containsPermission(requestPermissions, Permission.READ_EXTERNAL_STORAGE) &&
                    !containsPermission(requestPermissions, Permission.WRITE_EXTERNAL_STORAGE))
        ) {
            return
        }
        if (getTargetSdkVersionCode((context)!!) >= AndroidVersion.ANDROID_13 &&
            containsPermission(requestPermissions, Permission.READ_EXTERNAL_STORAGE)
        ) {
            // 当 targetSdkVersion >= 33 应该使用 READ_MEDIA_IMAGES、READ_MEDIA_VIDEO、READ_MEDIA_AUDIO 来代替 READ_EXTERNAL_STORAGE
            // 因为经过测试，如果当 targetSdkVersion >= 33 申请 READ_EXTERNAL_STORAGE 或者 WRITE_EXTERNAL_STORAGE 会被系统直接拒绝，不会弹出任何授权框
            throw IllegalArgumentException(
                (((("When targetSdkVersion >= 33 should use " +
                        Permission.READ_MEDIA_IMAGES).toString() + ", " + Permission.READ_MEDIA_VIDEO).toString() + ", " + Permission.READ_MEDIA_AUDIO).toString() +
                        " instead of " + Permission.READ_EXTERNAL_STORAGE)
            )
        }

        // 如果申请的是 Android 13 读取照片权限，则绕过本次检查
        if (containsPermission(requestPermissions, Permission.READ_MEDIA_IMAGES) ||
            containsPermission(requestPermissions,Permission.READ_MEDIA_VIDEO) ||
            containsPermission(requestPermissions,Permission.READ_MEDIA_AUDIO)) {

            if (containsPermission(requestPermissions, Permission.READ_EXTERNAL_STORAGE)) {
                // 检测是否有旧版的存储权限，有的话直接抛出异常，请不要自己动态申请这个权限
                // 框架会在 Android 13 以下的版本上自动添加并申请这两个权限
                throw IllegalArgumentException("If you have applied for media permissions, " +
                        "do not apply for the READ_EXTERNAL_STORAGE and WRITE_EXTERNAL_STORAGE permissions");
            }

            if (containsPermission(requestPermissions, Permission.MANAGE_EXTERNAL_STORAGE)) {
                // 因为 MANAGE_EXTERNAL_STORAGE 权限范围很大，有了它就可以读取媒体文件，不需要再叠加申请媒体权限
                throw IllegalArgumentException("Because the MANAGE_EXTERNAL_STORAGE permission range is very large, "
                        + "you can read media files with it, and there is no need to apply for additional media permissions.");
            }

            // 到此结束，不需要往下走是否有分区存储的判断
            return;
        }

        if (containsPermission(requestPermissions, Permission.MANAGE_EXTERNAL_STORAGE)) {

            if (containsPermission(requestPermissions, Permission.READ_EXTERNAL_STORAGE) ||
                containsPermission(requestPermissions, Permission.WRITE_EXTERNAL_STORAGE)) {
                // 检测是否有旧版的存储权限，有的话直接抛出异常，请不要自己动态申请这两个权限
                // 框架会在 Android 10 以下的版本上自动添加并申请这两个权限
                throw IllegalArgumentException("If you have applied for MANAGE_EXTERNAL_STORAGE permissions, " +
                        "do not apply for the READ_EXTERNAL_STORAGE and WRITE_EXTERNAL_STORAGE permissions");
            }
        }

        // 如果申请的是 Android 10 获取媒体位置权限，则绕过本次检查
        if (containsPermission(requestPermissions, Permission.ACCESS_MEDIA_LOCATION)) {
            return
        }
        if (androidManifestInfo == null) {
            return
        }
        val applicationInfo = androidManifestInfo.applicationInfo ?: return

        // 是否适配了分区存储
        val scopedStorage = isScopedStorage(
            (context)
        )
        val targetSdkVersion = getTargetSdkVersionCode((context)!!)
        val requestLegacyExternalStorage = applicationInfo.requestLegacyExternalStorage
        // 如果在已经适配 Android 10 的情况下
        if (((targetSdkVersion >= AndroidVersion.ANDROID_10) && !requestLegacyExternalStorage &&
                    (containsPermission(
                        requestPermissions,
                        Permission.MANAGE_EXTERNAL_STORAGE
                    ) || !scopedStorage))
        ) {
            // 请在清单文件 Application 节点中注册 android:requestLegacyExternalStorage="true" 属性
            // 否则就算申请了权限，也无法在 Android 10 的设备上正常读写外部存储上的文件
            // 如果你的项目已经全面适配了分区存储，请在清单文件中注册一个 meta-data 属性
            // <meta-data android:name="ScopedStorage" android:value="true" /> 来跳过该检查
            throw IllegalStateException(
                "Please register the android:requestLegacyExternalStorage=\"true\" " +
                        "attribute in the AndroidManifest.xml file, otherwise it will cause incompatibility with the old version"
            )
        }

        // 如果在已经适配 Android 11 的情况下
        if (((targetSdkVersion >= AndroidVersion.ANDROID_11) &&
                    !containsPermission(
                        requestPermissions,
                        Permission.MANAGE_EXTERNAL_STORAGE
                    ) && !scopedStorage)
        ) {
            // 1. 适配分区存储的特性，并在清单文件中注册一个 meta-data 属性
            // <meta-data android:name="ScopedStorage" android:value="true" />
            // 2. 如果不想适配分区存储，则需要使用 Permission.MANAGE_EXTERNAL_STORAGE 来申请权限
            // 上面两种方式需要二选一，否则无法在 Android 11 的设备上正常读写外部存储上的文件
            // 如果不知道该怎么选择，可以看文档：https://github.com/getActivity/XXPermissions/blob/master/HelpDoc
            throw IllegalArgumentException(
                (("The storage permission application is abnormal. If you have adapted the scope storage, " +
                        "please register the <meta-data android:name=\"ScopedStorage\" android:value=\"true\" /> attribute in the AndroidManifest.xml file. " +
                        "If there is no adaptation scope storage, please use " + Permission.MANAGE_EXTERNAL_STORAGE).toString() + " to apply for permission")
            )
        }
    }

    /**
     * 检查传感器权限
     */
    fun checkBodySensorsPermission(requestPermissions: List<String>) {
        // 判断是否包含后台传感器权限
        if (!containsPermission(requestPermissions, Permission.BODY_SENSORS_BACKGROUND)) {
            return
        }
        if (containsPermission(requestPermissions, Permission.BODY_SENSORS_BACKGROUND) &&
            !containsPermission(requestPermissions, Permission.BODY_SENSORS)
        ) {
            // 必须要申请前台传感器权限才能申请后台传感器权限
            throw IllegalArgumentException("Applying for background sensor permissions must contain " + Permission.BODY_SENSORS)
        }
        for (permission: String? in requestPermissions) {
            if (equalsPermission((permission)!!, Permission.ACCESS_BACKGROUND_LOCATION)) {
                // 不支持同时申请后台传感器权限和后台定位权限
                throw IllegalArgumentException(
                    ((("Applying for permissions " + Permission.BODY_SENSORS_BACKGROUND).toString() +
                            " and " + Permission.ACCESS_BACKGROUND_LOCATION).toString() + " at the same time is not supported")
                )
            }
            if (equalsPermission((permission), Permission.ACCESS_MEDIA_LOCATION)) {
                // 不支持同时申请后台传感器权限和获取媒体位置权限
                throw IllegalArgumentException(
                    ((("Applying for permissions " + Permission.BODY_SENSORS_BACKGROUND).toString() +
                            " and " + Permission.ACCESS_MEDIA_LOCATION).toString() + " at the same time is not supported")
                )
            }
        }
    }

    /**
     * 检查定位权限
     */
    fun checkLocationPermission(requestPermissions: List<String>) {
        /*
        为什么要注释这段代码，因为经过测试，没有官方说得那么严重，我用 Android 模拟器做测试
        愣是没测出来只申请 ACCESS_FINE_LOCATION 会有什么异常，估计是 Google 将代码改回去了，但是文档忘记改了
        总结出来：耳听为虚，眼见不一定为实，要自己动手实践，实践出真理，光说不练假把式
        if (AndroidVersion.getTargetSdkVersionCode(context) >= AndroidVersion.ANDROID_12) {
            if (PermissionUtils.containsPermission(requestPermissions, Permission.ACCESS_FINE_LOCATION) &&
                    !PermissionUtils.containsPermission(requestPermissions, Permission.ACCESS_COARSE_LOCATION) ) {
                // 如果您的应用以 Android 12 为目标平台并且您请求 ACCESS_FINE_LOCATION 权限
                // 则还必须请求 ACCESS_COARSE_LOCATION 权限。您必须在单个运行时请求中包含这两项权限
                // 如果您尝试仅请求 ACCESS_FINE_LOCATION，则系统会忽略该请求并在 Logcat 中记录以下错误消息：
                // ACCESS_FINE_LOCATION must be requested with ACCESS_COARSE_LOCATION
                // 官方适配文档：https://developer.android.google.cn/about/versions/12/approximate-location
                throw new IllegalArgumentException("If your app targets Android 12 or higher " +
                        "and requests the ACCESS_FINE_LOCATION runtime permission, " +
                        "you must also request the ACCESS_COARSE_LOCATION permission. " +
                        "You must include both permissions in a single runtime request.");
            }
        }
        */

        // 判断是否包含后台定位权限
        if (!containsPermission(requestPermissions, Permission.ACCESS_BACKGROUND_LOCATION)) {
            return
        }

        // 申请后台定位权限可以不包含模糊定位权限，但是一定要包含精确定位权限，否则后台定位权限会无法申请
        // 也就是会导致无法弹出授权弹窗，经过实践，在 Android 12 上这个问题已经被解决了
        // 但是为了兼容 Android 12 以下的设备还是要那么做，否则在 Android 11 及以下设备会出现异常
        if (containsPermission(requestPermissions, Permission.ACCESS_COARSE_LOCATION) &&
            !containsPermission(requestPermissions, Permission.ACCESS_FINE_LOCATION)
        ) {
            throw IllegalArgumentException(
                "Applying for background positioning permissions must include " +
                        Permission.ACCESS_FINE_LOCATION
            )
        }
        for (permission: String? in requestPermissions) {
            if ((equalsPermission((permission)!!, Permission.ACCESS_FINE_LOCATION)
                        || equalsPermission((permission), Permission.ACCESS_COARSE_LOCATION)
                        || equalsPermission((permission), Permission.ACCESS_BACKGROUND_LOCATION))
            ) {
                continue
            }
            throw IllegalArgumentException(
                "Because it includes background location permissions, " +
                        "do not apply for permissions unrelated to location"
            )
        }
    }


    /**
     * 检查蓝牙和 WIFI 权限申请是否符合规范
     */
    fun checkNearbyDevicesPermission(
        requestPermissions: List<String>,
        androidManifestInfo: AndroidManifestInfo?
    ) {
        // 如果请求的权限中没有蓝牙权限并且 WIFI 权限，那么就不符合条件，停止检查
        if (!containsPermission(requestPermissions, Permission.BLUETOOTH_SCAN) &&
            !containsPermission(requestPermissions, Permission.NEARBY_WIFI_DEVICES)
        ) {
            return
        }

        // 如果请求的权限已经包含了精确定位权限，那么就需要检查了
        if (containsPermission(requestPermissions, Permission.ACCESS_FINE_LOCATION)) {
            return
        }
        if (androidManifestInfo == null) {
            return
        }
        val permissionInfoList: List<AndroidManifestInfo.PermissionInfo> =
            androidManifestInfo.permissionInfoList
        for (permissionInfo: AndroidManifestInfo.PermissionInfo in permissionInfoList) {
            if (!equalsPermission((permissionInfo.name)!!, Permission.BLUETOOTH_SCAN) &&
                !equalsPermission((permissionInfo.name)!!, Permission.NEARBY_WIFI_DEVICES)
            ) {
                continue
            }
            if (!permissionInfo.neverForLocation()) {
                // WIFI 权限：https://developer.android.google.cn/about/versions/13/features/nearby-wifi-devices-permission?hl=zh-cn#assert-never-for-location
                // 在以 Android 13 为目标平台时，请考虑您的应用是否会通过 WIFI API 推导物理位置，如果不会，则应坚定声明此情况。
                // 如需做出此声明，请在应用的清单文件中将 usesPermissionFlags 属性设为 neverForLocation

                // 蓝牙权限：https://developer.android.google.cn/guide/topics/connectivity/bluetooth/permissions?hl=zh-cn#assert-never-for-location
                // 如果您的应用不使用蓝牙扫描结果来获取物理位置，则您可以断言您的应用从不使用蓝牙权限来获取物理位置。为此，请完成以下步骤：
                // 将该属性添加 android:usesPermissionFlags 到您的 BLUETOOTH_SCAN 权限声明中，并将该属性的值设置为 neverForLocation
                val maxSdkVersionString =
                    if ((permissionInfo.maxSdkVersion != Int.MAX_VALUE)) "android:maxSdkVersion=\"" + permissionInfo.maxSdkVersion + "\" " else ""
                throw IllegalArgumentException(
                    (("If your app doesn't use " + permissionInfo.name +
                            " to get physical location, " + "please change the <uses-permission android:name=\"" +
                            permissionInfo.name + "\" " + maxSdkVersionString + "/> node in the " +
                            "manifest file to <uses-permission android:name=\"" + permissionInfo.name +
                            "\" android:usesPermissionFlags=\"neverForLocation\" " + maxSdkVersionString + "/> node, " +
                            "if your app need use " + permissionInfo.name + " to get physical location, " +
                            "also need to add " + Permission.ACCESS_FINE_LOCATION).toString() + " permissions")
                )
            }
        }
    }

    /**
     * 检查画中画权限
     */
    fun checkNotificationListenerPermission(
        requestPermissions: List<String>,
        androidManifestInfo: AndroidManifestInfo?
    ) {
        // 如果请求的权限中没有通知栏监听权限，那么就不符合条件，停止检查
        if (!containsPermission(
                requestPermissions,
                Permission.BIND_NOTIFICATION_LISTENER_SERVICE
            )
        ) {
            return
        }
        if (androidManifestInfo == null) {
            return
        }
        val serviceInfoList: List<AndroidManifestInfo.ServiceInfo> =
            androidManifestInfo.serviceInfoList
        for (i in serviceInfoList.indices) {
            val permission = serviceInfoList[i].permission
            if (TextUtils.equals(permission, Permission.BIND_NOTIFICATION_LISTENER_SERVICE)) {
                // 终止循环并返回
                return
            }
        }
        throw IllegalArgumentException(
            (("No service registered permission attribute, " +
                    "please register <service android:permission=\"" +
                    Permission.BIND_NOTIFICATION_LISTENER_SERVICE).toString() + "\" > in AndroidManifest.xml")
        )
    }

    /**
     * 检查通知栏监听权限
     */
    fun checkPictureInPicturePermission(
        activity: Activity, requestPermissions: List<String>,
        androidManifestInfo: AndroidManifestInfo?
    ) {
        // 如果请求的权限中没有画中画权限，那么就不符合条件，停止检查
        if (!containsPermission(requestPermissions, Permission.PICTURE_IN_PICTURE)) {
            return
        }
        if (androidManifestInfo == null) {
            return
        }
        val activityInfoList: List<AndroidManifestInfo.ActivityInfo> =
            androidManifestInfo.activityInfoList
        for (i in activityInfoList.indices) {
            val supportsPictureInPicture = activityInfoList[i].supportsPictureInPicture
            if (supportsPictureInPicture) {
                // 终止循环并返回
                return
            }
        }
        val activityName = activity.javaClass.name.replace(activity.packageName, "")
        throw IllegalArgumentException(
            ("No activity registered supportsPictureInPicture attribute, please register \n" +
                    "<activity android:name=\"" + activityName + "\" android:supportsPictureInPicture=\"true\" > in AndroidManifest.xml")
        )
    }


    /**
     * 检查对照片和视频的部分访问权限申请是否符合规范
     */
    fun checkReadMediaVisualUserSelectedPermission( requestPermissions: List<String>) {
        // 如果请求的权限中没有对照片和视频的部分访问权限，那么就不符合条件，停止检查
        if (!containsPermission(requestPermissions, Permission.READ_MEDIA_VISUAL_USER_SELECTED)) {
            return
        }
        if (containsPermission(requestPermissions, Permission.READ_MEDIA_IMAGES) ||
            containsPermission(requestPermissions, Permission.READ_MEDIA_VIDEO)
        ) {
            return
        }
        throw java.lang.IllegalArgumentException(
            ("You cannot request the " + Permission.READ_MEDIA_VISUAL_USER_SELECTED + " permission alone. "
                    + "must add either " + Permission.READ_MEDIA_IMAGES) + " or " + Permission.READ_MEDIA_VIDEO + " permission, or maybe both"
        )
    }

    /**
     * 检查 targetSdkVersion 是否符合要求
     *
     * @param requestPermissions            请求的权限组
     */
    fun checkTargetSdkVersion(
        context: Context,
        requestPermissions: List<String>
    ) {
        for (permission in requestPermissions) {
            // targetSdk 最低版本要求
            var targetSdkMinVersion: Int =
                if (equalsPermission(permission, Permission.READ_MEDIA_VISUAL_USER_SELECTED)) {
                    // 授予对照片和视频的部分访问权限：https://developer.android.google.cn/about/versions/14/changes/partial-photo-video-access?hl=zh-cn
                    // READ_MEDIA_VISUAL_USER_SELECTED 这个权限比较特殊，不需要调高 targetSdk 的版本才能申请，但是需要和 READ_MEDIA_IMAGES 和 READ_MEDIA_VIDEO 组合使用
                    // 这个权限不能单独申请，只能和 READ_MEDIA_IMAGES、READ_MEDIA_VIDEO 一起申请，否则会有问题，所以这个权限的 targetSdk 最低要求为 33 及以上
                    AndroidVersion.ANDROID_13
                }  else if (containsPermission(arrayOf(
                        Permission.BLUETOOTH_SCAN,
                        Permission.BLUETOOTH_CONNECT,
                        Permission.BLUETOOTH_ADVERTISE
                    ).toList(), permission)) {
                    // 部分厂商修改了蓝牙权限机制，在 targetSdk 不满足条件的情况下（小于 31），仍需要让应用申请这个权限
                    // 相关的 issue 地址：
                    // 1. https://github.com/getActivity/XXPermissions/issues/123
                    // 2. https://github.com/getActivity/XXPermissions/issues/302
                    AndroidVersion.ANDROID_6
                } else {
                    PermissionHelper.findAndroidVersionByPermission(permission);
                }

            // 必须设置正确的 targetSdkVersion 才能正常检测权限
            if (getTargetSdkVersionCode(context) >= targetSdkMinVersion) {
                continue
            }
            throw java.lang.IllegalStateException(
                "Request " + permission + " permission, " +
                        "The targetSdkVersion SDK must be " + targetSdkMinVersion +
                        " or more, if you do not want to upgrade targetSdkVersion, " +
                        "please apply with the old permission"
            )
        }
    }

    /**
     * 检查清单文件中所注册的权限是否正常
     *
     * @param requestPermissions            请求的权限组
     */
    fun checkManifestPermissions(
        context: Context, requestPermissions: List<String>,
        androidManifestInfo: AndroidManifestInfo?
    ) {
        if (androidManifestInfo == null) {
            return
        }
        val permissionInfoList: List<AndroidManifestInfo.PermissionInfo> =
            androidManifestInfo.permissionInfoList
        if (permissionInfoList.isEmpty()) {
            throw IllegalStateException("No permissions are registered in the AndroidManifest.xml file")
        }
        val minSdkVersion: Int
        if (isAndroid7()) {
            minSdkVersion = context.applicationInfo.minSdkVersion
        } else {
            if (androidManifestInfo.usesSdkInfo != null) {
                minSdkVersion = androidManifestInfo.usesSdkInfo!!.minSdkVersion
            } else {
                minSdkVersion = AndroidVersion.ANDROID_4_0
            }
        }
        for (permission in requestPermissions) {
            if (PermissionHelper.isVirtualPermission(permission)) {
                // 不检测这些权限有没有在清单文件中注册，因为这几个权限是框架虚拟出来的，有没有在清单文件中注册都没关系
                continue;
            }

            // 检查这个权限有没有在清单文件中注册
            checkManifestPermission(permissionInfoList, permission!!)
            if (equalsPermission(permission!!, Permission.BODY_SENSORS_BACKGROUND)) {
                // 申请后台的传感器权限必须要先注册前台的传感器权限
                checkManifestPermission(permissionInfoList, Permission.BODY_SENSORS)
                continue
            }
            if (equalsPermission(permission!!, Permission.ACCESS_BACKGROUND_LOCATION)) {
                // 在 Android 11 及之前的版本，申请后台定位权限需要精确定位权限
                // 在 Android 12 及之后的版本，申请后台定位权限即可以用精确定位权限也可以用模糊定位权限
                if (getTargetSdkVersionCode(context) >= AndroidVersion.ANDROID_12) {
                    checkManifestPermission(
                        permissionInfoList,
                        Permission.ACCESS_FINE_LOCATION,
                        AndroidVersion.ANDROID_11
                    )
                    checkManifestPermission(permissionInfoList, Permission.ACCESS_COARSE_LOCATION)
                } else {
                    checkManifestPermission(permissionInfoList, Permission.ACCESS_FINE_LOCATION)
                }
                continue
            }

            // 其他的
            if (equalsPermission(permission!!, Permission.GET_INSTALLED_APPS)) {
                // 申请读取应用列表权限需要在清单文件中注册 QUERY_ALL_PACKAGES 权限
                // 否则就算申请 GET_INSTALLED_APPS 权限成功也是白搭，也是获取不到第三方安装列表信息的
                // Manifest.permission.QUERY_ALL_PACKAGES
                checkManifestPermission(permissionInfoList, "android.permission.QUERY_ALL_PACKAGES")
                continue
            }

            // 如果 minSdkVersion 已经大于等于权限出现的版本，则不需要做向下兼容
            if (minSdkVersion >= PermissionHelper.findAndroidVersionByPermission(permission)) {
                return;
            }

            // Android 13
            if (equalsPermission(permission!!, Permission.READ_MEDIA_IMAGES) ||
                equalsPermission(permission!!, Permission.READ_MEDIA_VIDEO) ||
                equalsPermission(permission!!, Permission.READ_MEDIA_AUDIO)
            ) {
                checkManifestPermission(
                    permissionInfoList,
                    Permission.READ_EXTERNAL_STORAGE,
                    AndroidVersion.ANDROID_12_L
                )
                continue
            }
            if (equalsPermission(permission!!, Permission.NEARBY_WIFI_DEVICES)) {
                checkManifestPermission(
                    permissionInfoList,
                    Permission.ACCESS_FINE_LOCATION,
                    AndroidVersion.ANDROID_12_L
                )
                continue
            }

            // Android 12
            if (equalsPermission(permission!!, Permission.BLUETOOTH_SCAN)) {
                checkManifestPermission(
                    permissionInfoList,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    AndroidVersion.ANDROID_11
                )
                // 这是 Android 12 之前遗留的问题，获取扫描蓝牙的结果需要精确定位权限
                checkManifestPermission(
                    permissionInfoList,
                    Permission.ACCESS_FINE_LOCATION,
                    AndroidVersion.ANDROID_11
                )
                continue
            }
            if (equalsPermission(permission!!, Permission.BLUETOOTH_CONNECT)) {
                checkManifestPermission(
                    permissionInfoList,
                    Manifest.permission.BLUETOOTH,
                    AndroidVersion.ANDROID_11
                )
                continue
            }
            if (equalsPermission(permission!!, Permission.BLUETOOTH_ADVERTISE)) {
                checkManifestPermission(
                    permissionInfoList,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    AndroidVersion.ANDROID_11
                )
                continue
            }

            // Android 11
            if (equalsPermission(permission!!, Permission.MANAGE_EXTERNAL_STORAGE)) {
                checkManifestPermission(
                    permissionInfoList,
                    Permission.READ_EXTERNAL_STORAGE,
                    AndroidVersion.ANDROID_10
                )
                checkManifestPermission(
                    permissionInfoList,
                    Permission.WRITE_EXTERNAL_STORAGE,
                    AndroidVersion.ANDROID_10
                )
                continue
            }

            // Android 8.0
            if (equalsPermission(permission!!, Permission.READ_PHONE_NUMBERS)) {
                checkManifestPermission(
                    permissionInfoList,
                    Permission.READ_PHONE_STATE,
                    AndroidVersion.ANDROID_7_1
                )
            }
        }
    }

    fun checkManifestPermission(
        permissionInfoList: List<AndroidManifestInfo.PermissionInfo>,
        checkPermission: String
    ) {
        checkManifestPermission(permissionInfoList, checkPermission, Int.MAX_VALUE)
    }

    /**
     * 检查某个权限注册是否正常，如果是则会抛出异常
     *
     * @param permissionInfoList        清单权限组
     * @param checkPermission           被检查的权限
     * @param maxSdkVersion             最低要求的 maxSdkVersion
     */
    fun checkManifestPermission(
        permissionInfoList: List<AndroidManifestInfo.PermissionInfo>,
        checkPermission: String, maxSdkVersion: Int
    ) {
        var permissionInfo: AndroidManifestInfo.PermissionInfo? = null
        for (info: AndroidManifestInfo.PermissionInfo in permissionInfoList) {
            if (TextUtils.equals(info.name, checkPermission)) {
                permissionInfo = info
                break
            }
        }
        if (permissionInfo == null) {
            // 动态申请的权限没有在清单文件中注册，分为以下两种情况：
            // 1. 如果你的项目没有在清单文件中注册这个权限，请直接在清单文件中注册一下即可
            // 2. 如果你的项目明明已注册这个权限，可以检查一下编译完成的 apk 包中是否包含该权限，如果里面没有，证明框架的判断是没有问题的
            //    一般是第三方 sdk 或者框架在清单文件中注册了 <uses-permission android:name="xxx" tools:node="remove"/> 导致的
            //    解决方式也很简单，通过在项目中注册 <uses-permission android:name="xxx" tools:node="replace"/> 即可替换掉原先的配置
            // 具体案例：https://github.com/getActivity/XXPermissions/issues/98
            throw IllegalStateException(
                ("Please register permissions in the AndroidManifest.xml file " +
                        "<uses-permission android:name=\"" + checkPermission + "\" />")
            )
        }
        val manifestMaxSdkVersion = permissionInfo.maxSdkVersion
        if (manifestMaxSdkVersion < maxSdkVersion) {
            // 清单文件中所注册的权限 maxSdkVersion 大小不符合最低要求，分为以下两种情况：
            // 1. 如果你的项目中注册了该属性，请根据报错提示修改 maxSdkVersion 属性值或者删除 maxSdkVersion 属性
            // 2. 如果你明明没有注册过 maxSdkVersion 属性，可以检查一下编译完成的 apk 包中是否有该属性，如果里面存在，证明框架的判断是没有问题的
            //    一般是第三方 sdk 或者框架在清单文件中注册了 <uses-permission android:name="xxx" android:maxSdkVersion="xx"/> 导致的
            //    解决方式也很简单，通过在项目中注册 <uses-permission android:name="xxx" tools:node="replace"/> 即可替换掉原先的配置
            throw IllegalArgumentException(
                ("The AndroidManifest.xml file " +
                        "<uses-permission android:name=\"" + checkPermission +
                        "\" android:maxSdkVersion=\"" + manifestMaxSdkVersion +
                        "\" /> does not meet the requirements, " +
                        (if (maxSdkVersion != Int.MAX_VALUE) "the minimum requirement for maxSdkVersion is $maxSdkVersion" else "please delete the android:maxSdkVersion=\"$manifestMaxSdkVersion\" attribute"))
            )
        }
    }

    /**
     * 处理和优化已经过时的权限
     *
     * @param requestPermissions            请求的权限组
     */
    fun optimizeDeprecatedPermission(requestPermissions: MutableList<String>) {
        // 如果本次申请包含了 Android 13 WIFI 权限
        if (!isAndroid13()) {
            if (containsPermission(requestPermissions, Permission.POST_NOTIFICATIONS) &&
                !containsPermission(requestPermissions, Permission.NOTIFICATION_SERVICE)
            ) {
                // 添加旧版的通知权限
                requestPermissions.add(Permission.NOTIFICATION_SERVICE)
            }
            if (containsPermission(requestPermissions, Permission.NEARBY_WIFI_DEVICES) &&
                !containsPermission(requestPermissions, Permission.ACCESS_FINE_LOCATION)
            ) {
                // 这是 Android 13 之前遗留的问题，使用 WIFI 需要精确定位权限
                requestPermissions.add(Permission.ACCESS_FINE_LOCATION)
            }
            if (((containsPermission(requestPermissions, Permission.READ_MEDIA_IMAGES) ||
                        containsPermission(requestPermissions, Permission.READ_MEDIA_VIDEO) ||
                        containsPermission(requestPermissions, Permission.READ_MEDIA_AUDIO)))
            ) {

                // 添加旧版的读取外部存储权限
                if (!containsPermission(requestPermissions, Permission.READ_EXTERNAL_STORAGE)) {
                    requestPermissions.add(Permission.READ_EXTERNAL_STORAGE)
                }
            }
        }

        // 如果本次申请包含了 Android 12 蓝牙扫描权限
        if ((!isAndroid12() &&
                    containsPermission(requestPermissions, Permission.BLUETOOTH_SCAN) &&
                    !containsPermission(requestPermissions, Permission.ACCESS_FINE_LOCATION))
        ) {
            // 这是 Android 12 之前遗留的问题，扫描蓝牙需要精确定位权限
            requestPermissions.add(Permission.ACCESS_FINE_LOCATION)
        }

        // 如果本次申请包含了 Android 11 存储权限
        if (containsPermission(requestPermissions, Permission.MANAGE_EXTERNAL_STORAGE)) {
            if (containsPermission(requestPermissions, Permission.READ_EXTERNAL_STORAGE) ||
                containsPermission(requestPermissions, Permission.WRITE_EXTERNAL_STORAGE)
            ) {
                // 检测是否有旧版的存储权限，有的话直接抛出异常，请不要自己动态申请这两个权限
                // 框架会在 Android 10 以下的版本上自动添加并申请这两个权限
                throw IllegalArgumentException(
                    "If you have applied for MANAGE_EXTERNAL_STORAGE permissions, " +
                            "do not apply for the READ_EXTERNAL_STORAGE and WRITE_EXTERNAL_STORAGE permissions"
                )
            }
            if (!isAndroid11()) {
                // 自动添加旧版的存储权限，因为旧版的系统不支持申请新版的存储权限
                requestPermissions.add(Permission.READ_EXTERNAL_STORAGE)
                requestPermissions.add(Permission.WRITE_EXTERNAL_STORAGE)
            }
        }
        if ((!isAndroid10() &&
                    containsPermission(requestPermissions, Permission.ACTIVITY_RECOGNITION) &&
                    !containsPermission(requestPermissions, Permission.BODY_SENSORS))
        ) {
            // 自动添加传感器权限，因为 ACTIVITY_RECOGNITION 是从 Android 10 开始才从传感器权限中剥离成独立权限
            requestPermissions.add(Permission.BODY_SENSORS)
        }
        if ((!isAndroid8() &&
                    containsPermission(requestPermissions, Permission.READ_PHONE_NUMBERS) &&
                    !containsPermission(requestPermissions, Permission.READ_PHONE_STATE))
        ) {
            // 自动添加旧版的读取电话号码权限，因为旧版的系统不支持申请新版的权限
            requestPermissions.add(Permission.READ_PHONE_STATE)
        }
    }


}