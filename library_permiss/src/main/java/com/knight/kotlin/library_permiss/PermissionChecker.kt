package com.knight.kotlin.library_permiss

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.res.XmlResourceParser
import android.os.Build
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.utils.PermissionUtils
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
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
            require(!checkMode) {
                // Context 的实例必须是 Activity 对象
                "The instance of the context must be an activity object"
            }
            return false
        }
        if (activity.isFinishing) {
            check(!checkMode) {
                // 这个 Activity 对象当前不能是关闭状态，这种情况常出现在执行异步请求后申请权限
                // 请自行在外层判断 Activity 状态是否正常之后再进入权限申请
                "The activity has been finishing, " +
                        "please manually determine the status of the activity"
            }
            return false
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed) {
            check(!checkMode) {
                // 这个 Activity 对象当前不能是销毁状态，这种情况常出现在执行异步请求后申请权限
                // 请自行在外层判断 Activity 状态是否正常之后再进入权限申请
                "The activity has been destroyed, " +
                        "please manually determine the status of the activity"
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
    fun checkPermissionArgument(requestPermissions: List<String>, checkMode: Boolean): Boolean {
        if (requestPermissions == null || requestPermissions.isEmpty()) {
            if (checkMode) {
                // 不传权限，就想申请权限？
                throw IllegalArgumentException("The requested permission cannot be empty")
            }
            return false
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
                if (!allPermissions.contains(permission)) {
                    // 请不要申请危险权限和特殊权限之外的权限
                    throw IllegalArgumentException(
                        "The " + permission +
                                " is not a dangerous permission or special permission, " +
                                "please do not apply dynamically"
                    )
                }
            }
        }
        return true
    }

    /**
     * 检查存储权限
     *
     * @param requestPermissions        请求的权限组
     */
    fun checkStoragePermission(context: Context, requestPermissions: List<String?>) {
        // 如果请求的权限中没有包含外部存储相关的权限，那么就直接返回
        if (!requestPermissions.contains(Permission.MANAGE_EXTERNAL_STORAGE) &&
            !requestPermissions.contains(Permission.READ_EXTERNAL_STORAGE) &&
            !requestPermissions.contains(Permission.WRITE_EXTERNAL_STORAGE)
        ) {
            return
        }

        // 是否适配了分区存储
        val scopedStorage: Boolean = PermissionUtils.isScopedStorage(context)
        val parser: XmlResourceParser = PermissionUtils.parseAndroidManifest(context) ?: return
        try {
            do {
                // 当前节点必须为标签头部
                if (parser.eventType != XmlResourceParser.START_TAG) {
                    continue
                }

                // 当前标签必须为 application
                if ("application" != parser.name) {
                    continue
                }
                val targetSdkVersion = context.applicationInfo.targetSdkVersion
                val requestLegacyExternalStorage = parser.getAttributeBooleanValue(
                    PermissionUtils.getAndroidNamespace(),
                    "requestLegacyExternalStorage", false
                )
                // 如果在已经适配 Android 10 的情况下
                check(
                    !(targetSdkVersion >= Build.VERSION_CODES.Q && !requestLegacyExternalStorage &&
                            (requestPermissions.contains(Permission.MANAGE_EXTERNAL_STORAGE) || !scopedStorage))
                ) {
                    // 请在清单文件 Application 节点中注册 android:requestLegacyExternalStorage="true" 属性
                    // 否则就算申请了权限，也无法在 Android 10 的设备上正常读写外部存储上的文件
                    // 如果你的项目已经全面适配了分区存储，请在清单文件中注册一个 meta-data 属性
                    // <meta-data android:name="ScopedStorage" android:value="true" /> 来跳过该检查
                    "Please register the android:requestLegacyExternalStorage=\"true\" " +
                            "attribute in the AndroidManifest.xml file, otherwise it will cause incompatibility with the old version"
                }

                // 如果在已经适配 Android 11 的情况下
                require(
                    !(targetSdkVersion >= Build.VERSION_CODES.R &&
                            !requestPermissions.contains(Permission.MANAGE_EXTERNAL_STORAGE) && !scopedStorage)
                ) {
                    // 1. 适配分区存储的特性，并在清单文件中注册一个 meta-data 属性
                    // <meta-data android:name="ScopedStorage" android:value="true" />
                    // 2. 如果不想适配分区存储，则需要使用 Permission.MANAGE_EXTERNAL_STORAGE 来申请权限
                    // 上面两种方式需要二选一，否则无法在 Android 11 的设备上正常读写外部存储上的文件
                    // 如果不知道该怎么选择，可以看文档：https://github.com/getActivity/XXPermissions/blob/master/HelpDoc
                    "The storage permission application is abnormal. If you have adapted the scope storage, " +
                            "please register the <meta-data android:name=\"ScopedStorage\" android:value=\"true\" /> attribute in the AndroidManifest.xml file. " +
                            "If there is no adaptation scope storage, please use MANAGE_EXTERNAL_STORAGE to apply for permission"
                }

                // 终止循环
                break
            } while (parser.next() != XmlResourceParser.END_DOCUMENT)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } finally {
            parser.close()
        }
    }

    /**
     * 检查定位权限
     *
     * @param requestPermissions        请求的权限组
     */
    fun checkLocationPermission(context: Context, requestPermissions: List<String?>) {
        if (context.applicationInfo.targetSdkVersion >= Build.VERSION_CODES.S) {
            if (requestPermissions.contains(Permission.ACCESS_FINE_LOCATION) &&
                !requestPermissions.contains(Permission.ACCESS_COARSE_LOCATION)
            ) {
                // 如果您的应用以 Android 12 为目标平台并且您请求 ACCESS_FINE_LOCATION 权限
                // 则还必须请求 ACCESS_COARSE_LOCATION 权限。您必须在单个运行时请求中包含这两项权限
                // 如果您尝试仅请求 ACCESS_FINE_LOCATION，则系统会忽略该请求并在 Logcat 中记录以下错误消息：
                // ACCESS_FINE_LOCATION must be requested with ACCESS_COARSE_LOCATION
                // 官方适配文档：https://developer.android.google.cn/about/versions/12/approximate-location
                throw java.lang.IllegalArgumentException(
                    "If your app targets Android 12 or higher " +
                            "and requests the ACCESS_FINE_LOCATION runtime permission, " +
                            "you must also request the ACCESS_COARSE_LOCATION permission. " +
                            "You must include both permissions in a single runtime request."
                )
            }
        }

        // 判断是否包含后台定位权限
        if (!requestPermissions.contains(Permission.ACCESS_BACKGROUND_LOCATION)) {
            return
        }
        if (requestPermissions.contains(Permission.ACCESS_COARSE_LOCATION) &&
            !requestPermissions.contains(Permission.ACCESS_FINE_LOCATION)
        ) {
            // 申请后台定位权限可以不包含模糊定位权限，但是一定要包含精确定位权限，否则后台定位权限会无法申请
            // 也就是会导致无法弹出授权弹窗，经过实践，在 Android 12 上这个问题已经被解决了
            // 但是为了兼容 Android 12 以下的设备还是要那么做，否则在 Android 11 及以下设备会出现异常
            throw java.lang.IllegalArgumentException(
                "The application for background location permissions " +
                        "must include precise location permissions"
            )
        }
        for (permission: String? in requestPermissions) {
            if ((Permission.ACCESS_FINE_LOCATION.equals(permission)
                        || Permission.ACCESS_COARSE_LOCATION.equals(permission)
                        || Permission.ACCESS_BACKGROUND_LOCATION.equals(permission))
            ) {
                continue
            }
            throw java.lang.IllegalArgumentException(
                "Because it includes background location permissions, " +
                        "do not apply for permissions unrelated to location"
            )
        }
    }

    /**
     * 检查targetSdkVersion 是否符合要求
     *
     * @param requestPermissions            请求的权限组
     */
    fun checkTargetSdkVersion(context: Context, requestPermissions: List<String?>) {
        // targetSdk 最低版本要求
        val targetSdkMinVersion: Int
        targetSdkMinVersion = if (requestPermissions.contains(Permission.BLUETOOTH_SCAN) ||
            requestPermissions.contains(Permission.BLUETOOTH_CONNECT) ||
            requestPermissions.contains(Permission.BLUETOOTH_ADVERTISE)
        ) {
            Build.VERSION_CODES.S
        } else if (requestPermissions.contains(Permission.MANAGE_EXTERNAL_STORAGE)) {
            // 必须设置 targetSdkVersion >= 30 才能正常检测权限，否则请使用 Permission.Group.STORAGE 来申请存储权限
            Build.VERSION_CODES.R
        } else if (requestPermissions.contains(Permission.ACCEPT_HANDOVER)) {
            Build.VERSION_CODES.P
        } else if (requestPermissions.contains(Permission.ACCESS_BACKGROUND_LOCATION) ||
            requestPermissions.contains(Permission.ACTIVITY_RECOGNITION) ||
            requestPermissions.contains(Permission.ACCESS_MEDIA_LOCATION)
        ) {
            Build.VERSION_CODES.Q
        } else if (requestPermissions.contains(Permission.REQUEST_INSTALL_PACKAGES) ||
            requestPermissions.contains(Permission.ANSWER_PHONE_CALLS) ||
            requestPermissions.contains(Permission.READ_PHONE_NUMBERS)
        ) {
            Build.VERSION_CODES.O
        } else {
            Build.VERSION_CODES.M
        }

        // 必须设置正确的 targetSdkVersion 才能正常检测权限
        if (context.applicationInfo.targetSdkVersion < targetSdkMinVersion) {
            throw RuntimeException(
                "The targetSdkVersion SDK must be " + targetSdkMinVersion +
                        " or more, if you do not want to upgrade targetSdkVersion, " +
                        "please apply with the old permissions"
            )
        }
    }

    /**
     * 检查清单文件中所注册的权限是否正常
     *
     * @param requestPermissions            请求的权限组
     */
    fun checkManifestPermissions(context: Context, requestPermissions: List<String?>) {
        val manifestPermissions: HashMap<String, Int> =
            PermissionUtils.getManifestPermissions(context)
        check(!manifestPermissions.isEmpty()) { "No permissions are registered in the AndroidManifest.xml file" }
        val minSdkVersion =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) context.applicationInfo.minSdkVersion else Build.VERSION_CODES.M
        for (permission in requestPermissions) {
            if (Permission.NOTIFICATION_SERVICE.equals(permission)) {
                // 不检测通知栏权限有没有在清单文件中注册，因为这个权限是框架虚拟出来的，有没有在清单文件中注册都没关系
                continue
            }
            if (minSdkVersion < Build.VERSION_CODES.S) {
                if (Permission.BLUETOOTH_SCAN.equals(permission)) {
                    PermissionChecker.checkManifestPermission(
                        manifestPermissions,
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Build.VERSION_CODES.R
                    )
                    // 这是 Android 12 之前遗留的问题，获取扫描蓝牙的结果需要定位的权限
                    PermissionChecker.checkManifestPermission(
                        manifestPermissions,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Build.VERSION_CODES.R
                    )
                }
                if (Permission.BLUETOOTH_CONNECT.equals(permission)) {
                    PermissionChecker.checkManifestPermission(
                        manifestPermissions,
                        Manifest.permission.BLUETOOTH,
                        Build.VERSION_CODES.R
                    )
                }
                if (Permission.BLUETOOTH_ADVERTISE.equals(permission)) {
                    PermissionChecker.checkManifestPermission(
                        manifestPermissions,
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Build.VERSION_CODES.R
                    )
                }
            }
            if (minSdkVersion < Build.VERSION_CODES.R) {
                if (Permission.MANAGE_EXTERNAL_STORAGE.equals(permission)) {
                    PermissionChecker.checkManifestPermission(
                        manifestPermissions,
                        Permission.READ_EXTERNAL_STORAGE,
                        Build.VERSION_CODES.Q
                    )
                    PermissionChecker.checkManifestPermission(
                        manifestPermissions,
                        Permission.WRITE_EXTERNAL_STORAGE,
                        Build.VERSION_CODES.Q
                    )
                }
            }
            if (minSdkVersion < Build.VERSION_CODES.Q) {
                if (Permission.ACTIVITY_RECOGNITION.equals(permission)) {
                    PermissionChecker.checkManifestPermission(
                        manifestPermissions,
                        Permission.BODY_SENSORS,
                        Build.VERSION_CODES.O
                    )
                }
            }
            if (minSdkVersion < Build.VERSION_CODES.O) {
                if (Permission.READ_PHONE_NUMBERS.equals(permission)) {
                    checkManifestPermission(
                        manifestPermissions,
                        Permission.READ_PHONE_STATE,
                        Build.VERSION_CODES.N_MR1
                    )
                }
            }
            checkManifestPermission(
                manifestPermissions,
                permission,
                Int.MAX_VALUE
            )
        }
    }

    /**
     * 检查某个权限注册是否正常，如果是则会抛出异常
     *
     * @param manifestPermissions       清单权限组
     * @param checkPermission           被检查的权限
     * @param maxSdkVersion             最低要求的 maxSdkVersion
     */
    fun checkManifestPermission(
        manifestPermissions: HashMap<String, Int>,
        checkPermission: String?, maxSdkVersion: Int
    ) {
        if (!manifestPermissions.containsKey(checkPermission)) {
            // 动态申请的权限没有在清单文件中注册，分为以下两种情况：
            // 1. 如果你的项目没有在清单文件中注册这个权限，请直接在清单文件中注册一下即可
            // 2. 如果你的项目明明已注册这个权限，可以检查一下编译完成的 apk 包中是否包含该权限，如果里面没有，证明框架的判断是没有问题的
            //    一般是第三方 sdk 或者框架在清单文件中注册了 <uses-permission android:name="xxx" tools:node="remove"/> 导致的
            //    解决方式也很简单，通过在项目中注册 <uses-permission android:name="xxx" tools:node="replace"/> 即可替换掉原先的配置
            // 具体案例：https://github.com/getActivity/XXPermissions/issues/98
            throw IllegalStateException(
                "Please register permissions in the AndroidManifest.xml file " +
                        "<uses-permission android:name=\"" + checkPermission + "\" />"
            )
        }
        val manifestMaxSdkVersion = manifestPermissions.get(checkPermission) ?: return
        if (manifestMaxSdkVersion < maxSdkVersion) {
            // 清单文件中所注册的权限 maxSdkVersion 大小不符合最低要求，分为以下两种情况：
            // 1. 如果你的项目中注册了该属性，请根据报错提示修改 maxSdkVersion 属性值或者删除 maxSdkVersion 属性
            // 2. 如果你明明没有注册过 maxSdkVersion 属性，可以检查一下编译完成的 apk 包中是否有该属性，如果里面存在，证明框架的判断是没有问题的
            //    一般是第三方 sdk 或者框架在清单文件中注册了 <uses-permission android:name="xxx" android:maxSdkVersion="xx"/> 导致的
            //    解决方式也很简单，通过在项目中注册 <uses-permission android:name="xxx" tools:node="replace"/> 即可替换掉原先的配置
            // 具体案例：https://github.com/getActivity/XXPermissions/issues/98
            throw java.lang.IllegalArgumentException(
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
        if (!PermissionUtils.isAndroid12() &&
            requestPermissions.contains(Permission.BLUETOOTH_SCAN) &&
            !requestPermissions.contains(Permission.ACCESS_COARSE_LOCATION)
        ) {
            // 自动添加定位权限，因为在低版本下获取蓝牙扫描的结果需要此权限
            requestPermissions.add(Permission.ACCESS_COARSE_LOCATION)
        }

        // 如果本次申请包含了 Android 12 蓝牙扫描权限
        if (!PermissionUtils.isAndroid12() && requestPermissions.contains(Permission.BLUETOOTH_SCAN)) {
            // 这是 Android 12 之前遗留的问题，扫描蓝牙需要定位的权限
            requestPermissions.add(Permission.ACCESS_COARSE_LOCATION)
        }

        // 如果本次申请包含了 Android 11 存储权限
        if (requestPermissions.contains(Permission.MANAGE_EXTERNAL_STORAGE)) {
            require(
                !(requestPermissions.contains(Permission.READ_EXTERNAL_STORAGE) ||
                        requestPermissions.contains(Permission.WRITE_EXTERNAL_STORAGE))
            ) {
                // 检测是否有旧版的存储权限，有的话直接抛出异常，请不要自己动态申请这两个权限
                "If you have applied for MANAGE_EXTERNAL_STORAGE permissions, " +
                        "do not apply for the READ_EXTERNAL_STORAGE and WRITE_EXTERNAL_STORAGE permissions"
            }
            if (!PermissionUtils.isAndroid11()) {
                // 自动添加旧版的存储权限，因为旧版的系统不支持申请新版的存储权限
                requestPermissions.add(Permission.READ_EXTERNAL_STORAGE)
                requestPermissions.add(Permission.WRITE_EXTERNAL_STORAGE)
            }
        }
        if (!PermissionUtils.isAndroid8() &&
            requestPermissions.contains(Permission.READ_PHONE_NUMBERS) &&
            !requestPermissions.contains(Permission.READ_PHONE_STATE)
        ) {
            // 自动添加旧版的读取电话号码权限，因为旧版的系统不支持申请新版的权限
            requestPermissions.add(Permission.READ_PHONE_STATE)
        }
        if (!PermissionUtils.isAndroid10() &&
            requestPermissions.contains(Permission.ACTIVITY_RECOGNITION) &&
            !requestPermissions.contains(Permission.BODY_SENSORS)
        ) {
            // 自动添加传感器权限，因为这个权限是从 Android 10 开始才从传感器权限中剥离成独立权限
            requestPermissions.add(Permission.BODY_SENSORS)
        }
    }


}