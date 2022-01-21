package com.knight.kotlin.library_permiss.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AppOpsManager
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.XmlResourceParser
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.text.TextUtils
import android.view.Surface
import androidx.fragment.app.FragmentActivity
import com.knight.kotlin.library_permiss.permissions.Permission
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.lang.reflect.InvocationTargetException

/**
 * Author:Knight
 * Time:2022/1/20 15:38
 * Description:PermissionUtils
 */
object PermissionUtils {

    /**
     * 是否是 Android 12 及以上版本
     */
    fun isAndroid12(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    }

    /**
     * 是否是 Android 11 及以上版本
     */
    fun isAndroid11(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }

    /**
     * 是否是 Android 10 及以上版本
     */
    fun isAndroid10(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    /**
     * 是否是 Android 9.0 及以上版本
     */
    fun isAndroid9(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
    }

    /**
     * 是否是 Android 8.0 及以上版本
     */
    fun isAndroid8(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }

    /**
     * 是否是 Android 6.0 及以上版本
     */
    fun isAndroid6(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    /**
     * 是否是 Android 5.0 及以上版本
     */
    fun isAndroid5(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }

    /**
     * 获取 Android 属性命名空间
     */
    fun getAndroidNamespace(): String {
        return "http://schemas.android.com/apk/res/android"
    }

    /**
     * 当前是否处于 debug 模式
     */
    fun isDebugMode(context: Context): Boolean {
        return context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
    }

    /**
     * 返回应用程序在清单文件中注册的权限
     */
    fun getManifestPermissions(context: Context): HashMap<String, Int> {
        val manifestPermissions = HashMap<String, Int>()
        val parser: XmlResourceParser? =
            parseAndroidManifest(context)
        if (parser != null) {
            try {
                do {
                    // 当前节点必须为标签头部
                    if (parser.eventType != XmlResourceParser.START_TAG) {
                        continue
                    }

                    // 当前标签必须为 uses-permission
                    if ("uses-permission" != parser.name) {
                        continue
                    }
                    manifestPermissions[parser.getAttributeValue(
                        getAndroidNamespace(),
                        "name"
                    )] =
                        parser.getAttributeIntValue(
                            getAndroidNamespace(),
                            "maxSdkVersion",
                            Int.MAX_VALUE
                        )
                } while (parser.next() != XmlResourceParser.END_DOCUMENT)
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: XmlPullParserException) {
                e.printStackTrace()
            } finally {
                parser.close()
            }
        }
        if (manifestPermissions.isEmpty()) {
            try {
                // 当清单文件没有注册任何权限的时候，那么这个数组对象就是空的
                // https://github.com/getActivity/XXPermissions/issues/35
                val requestedPermissions = context.packageManager.getPackageInfo(
                    context.packageName, PackageManager.GET_PERMISSIONS
                ).requestedPermissions
                if (requestedPermissions != null) {
                    for (permission in requestedPermissions) {
                        manifestPermissions[permission] = Int.MAX_VALUE
                    }
                }
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
        }
        return manifestPermissions
    }

    /**
     * 是否有存储权限
     */
    fun isGrantedStoragePermission(context: Context): Boolean {
        return if (isAndroid11()) {
            Environment.isExternalStorageManager()
        } else isGrantedPermissions(
            context,
            asArrayList<String>(*Permission.Group.STORAGE)
        )
    }

    /**
     * 是否有安装权限
     */
    fun isGrantedInstallPermission(context: Context): Boolean {
        return if (isAndroid8()) {
            context.packageManager.canRequestPackageInstalls()
        } else true
    }

    /**
     * 是否有悬浮窗权限
     */
    fun isGrantedWindowPermission(context: Context?): Boolean {
        return if (isAndroid6()) {
            Settings.canDrawOverlays(context)
        } else true
    }

    /**
     * 是否有系统设置权限
     */
    fun isGrantedSettingPermission(context: Context?): Boolean {
        return if (isAndroid6()) {
            Settings.System.canWrite(context)
        } else true
    }

    /**
     * 是否有通知栏权限
     */
    fun isGrantedNotifyPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.getSystemService(NotificationManager::class.java)
                .areNotificationsEnabled()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 参考 Support 库中的方法： NotificationManagerCompat.from(context).areNotificationsEnabled()
            val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            return try {
                val method = appOps.javaClass.getMethod(
                    "checkOpNoThrow",
                    Integer.TYPE, Integer.TYPE, String::class.java
                )
                val field = appOps.javaClass.getDeclaredField("OP_POST_NOTIFICATION")
                val value = field[Int::class.java] as Int
                method.invoke(
                    appOps, value, context.applicationInfo.uid,
                    context.packageName
                ) as Int == AppOpsManager.MODE_ALLOWED
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
                true
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
                true
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
                true
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
                true
            } catch (e: RuntimeException) {
                e.printStackTrace()
                true
            }
        }
        return true
    }

    /**
     * 是否有读取包权限
     */
    fun isGrantedPackagePermission(context: Context): Boolean {
        if (isAndroid5()) {
            val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val mode: Int
            mode = if (isAndroid10()) {
                appOps.unsafeCheckOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    context.applicationInfo.uid, context.packageName
                )
            } else {
                appOps.checkOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    context.applicationInfo.uid, context.packageName
                )
            }
            return mode == AppOpsManager.MODE_ALLOWED
        }
        return true
    }

    /**
     * 判断某个权限集合是否包含特殊权限
     */
    fun containsSpecialPermission(permissions: List<String?>?): Boolean {
        if (permissions == null || permissions.isEmpty()) {
            return false
        }
        for (permission in permissions) {
            if (isSpecialPermission(permission)) {
                return true
            }
        }
        return false
    }

    /**
     * 判断某个权限是否是特殊权限
     */
    fun isSpecialPermission(permission: String?): Boolean {
        return Permission.MANAGE_EXTERNAL_STORAGE.equals(permission) ||
                Permission.REQUEST_INSTALL_PACKAGES.equals(permission) ||
                Permission.SYSTEM_ALERT_WINDOW.equals(permission) ||
                Permission.WRITE_SETTINGS.equals(permission) ||
                Permission.NOTIFICATION_SERVICE.equals(permission) ||
                Permission.PACKAGE_USAGE_STATS.equals(permission)
    }

    /**
     * 判断某些权限是否全部被授予
     */
    fun isGrantedPermissions(context: Context, permissions: List<String?>?): Boolean {
        if (permissions == null || permissions.isEmpty()) {
            return false
        }
        for (permission in permissions) {
            if (!isGrantedPermission(
                    context,
                    permission
                )
            ) {
                return false
            }
        }
        return true
    }

    /**
     * 获取没有授予的权限
     */
    fun getDeniedPermissions(context: Context, permissions: List<String>): List<String>? {
        val deniedPermission: MutableList<String> = ArrayList(permissions.size)

        // 如果是安卓 6.0 以下版本就默认授予
        if (!isAndroid6()) {
            return deniedPermission
        }
        for (permission in permissions) {
            if (!isGrantedPermission(
                    context,
                    permission
                )
            ) {
                deniedPermission.add(permission)
            }
        }
        return deniedPermission
    }

    /**
     * 判断某个权限是否授予
     */
    fun isGrantedPermission(context: Context, permission: String?): Boolean {
        // 检测通知栏权限
        if (Permission.NOTIFICATION_SERVICE.equals(permission)) {
            return isGrantedNotifyPermission(
                context
            )
        }

        // 检测获取读取包权限
        if (Permission.PACKAGE_USAGE_STATS.equals(permission)) {
            return isGrantedPackagePermission(
                context
            )
        }

        // 其他权限在 Android 6.0 以下版本就默认授予
        if (!isAndroid6()) {
            return true
        }

        // 检测存储权限
        if (Permission.MANAGE_EXTERNAL_STORAGE.equals(permission)) {
            return isGrantedStoragePermission(
                context
            )
        }

        // 检测安装权限
        if (Permission.REQUEST_INSTALL_PACKAGES.equals(permission)) {
            return isGrantedInstallPermission(
                context
            )
        }

        // 检测悬浮窗权限
        if (Permission.SYSTEM_ALERT_WINDOW.equals(permission)) {
            return isGrantedWindowPermission(
                context
            )
        }

        // 检测系统权限
        if (Permission.WRITE_SETTINGS.equals(permission)) {
            return isGrantedSettingPermission(
                context
            )
        }

        // 检测 Android 12 的三个新权限
        if (!isAndroid12()) {
            if (Permission.BLUETOOTH_SCAN.equals(permission)) {
                return context.checkSelfPermission(Permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED
            }
            if (Permission.BLUETOOTH_CONNECT.equals(permission) ||
                Permission.BLUETOOTH_ADVERTISE.equals(permission)
            ) {
                return true
            }
        }

        // 检测 Android 10 的三个新权限
        if (!isAndroid10()) {
            if (Permission.ACCESS_BACKGROUND_LOCATION.equals(permission)) {
                return context.checkSelfPermission(Permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED
            }
            if (Permission.ACTIVITY_RECOGNITION.equals(permission)) {
                return context.checkSelfPermission(Permission.BODY_SENSORS) ==
                        PackageManager.PERMISSION_GRANTED
            }
            if (Permission.ACCESS_MEDIA_LOCATION.equals(permission)) {
                return true
            }
        }

        // 检测 Android 9.0 的一个新权限
        if (!isAndroid9()) {
            if (Permission.ACCEPT_HANDOVER.equals(permission)) {
                return true
            }
        }

        // 检测 Android 8.0 的两个新权限
        if (!isAndroid8()) {
            if (Permission.ANSWER_PHONE_CALLS.equals(permission)) {
                return true
            }
            if (Permission.READ_PHONE_NUMBERS.equals(permission)) {
                return context.checkSelfPermission(Permission.READ_PHONE_STATE) ==
                        PackageManager.PERMISSION_GRANTED
            }
        }
        return context.checkSelfPermission(permission!!) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 优化权限回调结果
     */
    fun optimizePermissionResults(
        activity: Activity,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        for (i in permissions.indices) {
            var recheck = false
            val permission = permissions[i]

            // 如果这个权限是特殊权限，那么就重新进行权限检测
            if (isSpecialPermission(permission)) {
                recheck = true
            }

            // 重新检查 Android 12 的三个新权限
            if (!isAndroid12() &&
                (Permission.BLUETOOTH_SCAN.equals(permission) ||
                        Permission.BLUETOOTH_CONNECT.equals(permission) ||
                        Permission.BLUETOOTH_ADVERTISE.equals(permission))
            ) {
                recheck = true
            }

            // 重新检查 Android 10.0 的三个新权限
            if (!isAndroid10() &&
                (Permission.ACCESS_BACKGROUND_LOCATION.equals(permission) ||
                        Permission.ACTIVITY_RECOGNITION.equals(permission) ||
                        Permission.ACCESS_MEDIA_LOCATION.equals(permission))
            ) {
                recheck = true
            }

            // 重新检查 Android 9.0 的一个新权限
            if (!isAndroid9() &&
                Permission.ACCEPT_HANDOVER.equals(permission)
            ) {
                recheck = true
            }

            // 重新检查 Android 8.0 的两个新权限
            if (!isAndroid8() &&
                (Permission.ANSWER_PHONE_CALLS.equals(permission) ||
                        Permission.READ_PHONE_NUMBERS.equals(permission))
            ) {
                recheck = true
            }
            if (recheck) {
                grantResults[i] =
                    if (isGrantedPermission(
                            activity,
                            permission
                        )
                    ) PackageManager.PERMISSION_GRANTED else PackageManager.PERMISSION_DENIED
            }
        }
    }

    /**
     * 在权限组中检查是否有某个权限是否被永久拒绝
     *
     * @param activity              Activity对象
     * @param permissions            请求的权限
     */
    fun isPermissionPermanentDenied(activity: Activity, permissions: List<String?>): Boolean {
        for (permission in permissions) {
            if (isPermissionPermanentDenied(
                    activity,
                    permission
                )
            ) {
                return true
            }
        }
        return false
    }

    /**
     * 判断某个权限是否被永久拒绝
     *
     * @param activity              Activity对象
     * @param permission            请求的权限
     */
    fun isPermissionPermanentDenied(activity: Activity, permission: String?): Boolean {
        if (!isAndroid6()) {
            return false
        }

        // 特殊权限不算，本身申请方式和危险权限申请方式不同，因为没有永久拒绝的选项，所以这里返回 false
        if (isSpecialPermission(permission)) {
            return false
        }

        // 检测 Android 12 的三个新权限
        if (!isAndroid12()) {
            if (Permission.BLUETOOTH_SCAN.equals(permission)) {
                return !isGrantedPermission(
                    activity,
                    Permission.ACCESS_COARSE_LOCATION
                ) &&
                        !activity.shouldShowRequestPermissionRationale(Permission.ACCESS_COARSE_LOCATION)
            }
            if (Permission.BLUETOOTH_CONNECT.equals(permission) ||
                Permission.BLUETOOTH_ADVERTISE.equals(permission)
            ) {
                return false
            }
        }
        if (isAndroid10()) {

            // 重新检测后台定位权限是否永久拒绝
            if (Permission.ACCESS_BACKGROUND_LOCATION.equals(permission) &&
                !isGrantedPermission(
                    activity,
                    Permission.ACCESS_BACKGROUND_LOCATION
                ) &&
                !isGrantedPermission(
                    activity,
                    Permission.ACCESS_FINE_LOCATION
                )
            ) {
                return !activity.shouldShowRequestPermissionRationale(Permission.ACCESS_FINE_LOCATION)
            }
        }

        // 检测 Android 10 的三个新权限
        if (!isAndroid10()) {
            if (Permission.ACCESS_BACKGROUND_LOCATION.equals(permission)) {
                return !isGrantedPermission(
                    activity,
                    Permission.ACCESS_FINE_LOCATION
                ) &&
                        !activity.shouldShowRequestPermissionRationale(Permission.ACCESS_FINE_LOCATION)
            }
            if (Permission.ACTIVITY_RECOGNITION.equals(permission)) {
                return !isGrantedPermission(
                    activity,
                    Permission.BODY_SENSORS
                ) &&
                        !activity.shouldShowRequestPermissionRationale(Permission.BODY_SENSORS)
            }
            if (Permission.ACCESS_MEDIA_LOCATION.equals(permission)) {
                return false
            }
        }

        // 检测 Android 9.0 的一个新权限
        if (!isAndroid9()) {
            if (Permission.ACCEPT_HANDOVER.equals(permission)) {
                return false
            }
        }

        // 检测 Android 8.0 的两个新权限
        if (!isAndroid8()) {
            if (Permission.ANSWER_PHONE_CALLS.equals(permission)) {
                return false
            }
            if (Permission.READ_PHONE_NUMBERS.equals(permission)) {
                return !isGrantedPermission(
                    activity,
                    Permission.READ_PHONE_STATE
                ) &&
                        !activity.shouldShowRequestPermissionRationale(Permission.READ_PHONE_STATE)
            }
        }
        return !isGrantedPermission(
            activity,
            permission
        ) &&
                !activity.shouldShowRequestPermissionRationale(permission!!)
    }

    /**
     * 获取没有授予的权限
     *
     * @param permissions           需要请求的权限组
     * @param grantResults          允许结果组
     */
    fun getDeniedPermissions(permissions: List<String>, grantResults: IntArray): List<String> {
        val deniedPermissions: MutableList<String> = java.util.ArrayList()
        for (i in grantResults.indices) {
            // 把没有授予过的权限加入到集合中
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permissions[i])
            }
        }
        return deniedPermissions
    }

    /**
     * 获取已授予的权限
     *
     * @param permissions       需要请求的权限组
     * @param grantResults      允许结果组
     */
    fun getGrantedPermissions(permissions: List<String>, grantResults: IntArray): List<String> {
        val grantedPermissions: MutableList<String> = java.util.ArrayList()
        for (i in grantResults.indices) {
            // 把授予过的权限加入到集合中
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                grantedPermissions.add(permissions[i])
            }
        }
        return grantedPermissions
    }

    /**
     * 将数组转换成 ArrayList
     *
     * 这里解释一下为什么不用 Arrays.asList
     * 第一是返回的类型不是 java.util.ArrayList 而是 java.util.Arrays.ArrayList
     * 第二是返回的 ArrayList 对象是只读的，也就是不能添加任何元素，否则会抛异常
     */
    fun <T> asArrayList(vararg array: T): ArrayList<T> {
        val list = ArrayList<T>(array.size)
        if (array == null || array.size == 0) {
            return list
        }
        for (t in array) {
            list.add(t)
        }
        return list
    }

    @SafeVarargs
    fun <T> asArrayLists(vararg arrays: Array<T>): ArrayList<T> {
        val list = ArrayList<T>()
        if (arrays == null || arrays.size == 0) {
            return list
        }
        for (ts in arrays) {
            list.addAll(asArrayList(*ts)!!)
        }
        return list
    }

    /**
     * 寻找上下文中的 Activity 对象
     */
    fun findActivity(context: Context?): FragmentActivity? {
        var context = context
        do {
            context = if (context is FragmentActivity) {
                return context
            } else if (context is ContextWrapper) {
                context.baseContext
            } else {
                return null
            }
        } while (context != null)
        return null
    }

    /**
     * 获取当前应用 Apk 在 AssetManager 中的 Cookie，如果获取失败，则为 0
     */
    @SuppressLint("PrivateApi")
    fun findApkPathCookie(context: Context): Int {
        val assets = context.assets
        val apkPath = context.applicationInfo.sourceDir
        try {
            // 为什么不直接通过反射 AssetManager.findCookieForPath 方法来判断？因为这个 API 属于反射黑名单，反射执行不了
            // 为什么不直接通过反射 AssetManager.addAssetPathInternal 这个非隐藏的方法来判断？因为这个也反射不了
            val method = assets.javaClass.getDeclaredMethod(
                "addAssetPath",
                String::class.java
            )
            val cookie = method.invoke(assets, apkPath) as Int
            if (cookie != null) {
                return cookie
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        // 获取失败
        return 0
    }

    /**
     * 解析清单文件
     */
    fun parseAndroidManifest(context: Context): XmlResourceParser? {
        val cookie: Int =
            findApkPathCookie(context)
        if (cookie == 0) {
            // 如果 cookie 为 0，证明获取失败，直接 return
            return null
        }
        try {
            val parser = context.assets.openXmlResourceParser(cookie, "AndroidManifest.xml")
            do {
                // 当前节点必须为标签头部
                if (parser.eventType != XmlResourceParser.START_TAG) {
                    continue
                }
                if ("manifest" == parser.name) {
                    // 如果读取到的包名和当前应用的包名不是同一个的话，证明这个清单文件的内容不是当前应用的
                    // 具体案例：https://github.com/getActivity/XXPermissions/issues/102
                    if (TextUtils.equals(
                            context.packageName,
                            parser.getAttributeValue(null, "package")
                        )
                    ) {
                        return parser
                    }
                }
            } while (parser.next() != XmlResourceParser.END_DOCUMENT)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 判断是否适配了分区存储
     */
    fun isScopedStorage(context: Context): Boolean {
        try {
            val metaKey = "ScopedStorage"
            val metaData = context.packageManager.getApplicationInfo(
                context.packageName, PackageManager.GET_META_DATA
            ).metaData
            if (metaData != null && metaData.containsKey(metaKey)) {
                return java.lang.Boolean.parseBoolean(metaData[metaKey].toString())
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 判断 Activity 是否反方向旋转了
     */
    fun isActivityReverse(activity: Activity): Boolean {
        // 获取 Activity 旋转的角度
        val activityRotation: Int
        activityRotation = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.display!!.rotation
        } else {
            activity.windowManager.defaultDisplay.rotation
        }
        return when (activityRotation) {
            Surface.ROTATION_180, Surface.ROTATION_270 -> true
            Surface.ROTATION_0, Surface.ROTATION_90 -> false
            else -> false
        }
    }


}