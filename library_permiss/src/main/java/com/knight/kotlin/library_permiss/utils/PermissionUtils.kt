package com.knight.kotlin.library_permiss.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.Surface
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import com.knight.kotlin.library_permiss.AndroidManifestInfo
import com.knight.kotlin.library_permiss.AndroidManifestParser
import com.knight.kotlin.library_permiss.AndroidVersion
import com.knight.kotlin.library_permiss.PermissionNameConvert
import com.knight.kotlin.library_permiss.R
import com.knight.kotlin.library_permiss.XXPermissions
import com.knight.kotlin.library_permiss.listener.OnPermissionCallback
import com.knight.kotlin.library_permiss.listener.OnPermissionPageCallback
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_util.DialogUtils
import org.xmlpull.v1.XmlPullParserException
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.Arrays
import java.util.Properties


/**
 * Author:Knight
 * Time:2022/1/20 15:38
 * Description:PermissionUtils
 */
object PermissionUtils {

    /** Handler 对象  */
    private val HANDLER: Handler = Handler(Looper.getMainLooper())


    /**
     * 判断某个危险权限是否授予了
     */
    @RequiresApi(api = AndroidVersion.ANDROID_6)
    fun checkSelfPermission( context: Context, permission: String): Boolean {
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(AndroidVersion.ANDROID_4_4)
    fun checkOpNoThrow(context: Context, opFieldName: String?, opDefaultValue: Int): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val appInfo = context.applicationInfo
        val pkg = context.applicationContext.packageName
        val uid = appInfo.uid
        return try {
            val appOpsClass = Class.forName(AppOpsManager::class.java.name)
            val opValue: Int
            opValue = try {
                val opValueField: Field = appOpsClass.getDeclaredField(opFieldName)
                opValueField.get(Int::class.java) as Int
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
                opDefaultValue
            }
            val checkOpNoThrowMethod = appOpsClass.getMethod(
                "checkOpNoThrow", Integer.TYPE,
                Integer.TYPE,
                String::class.java
            )
            (checkOpNoThrowMethod.invoke(appOps, opValue, uid, pkg) as Int
                    == AppOpsManager.MODE_ALLOWED)
        } catch (e: ClassNotFoundException) {
            true
        } catch (e: NoSuchMethodException) {
            true
        } catch (e: InvocationTargetException) {
            true
        } catch (e: IllegalAccessException) {
            true
        } catch (e: RuntimeException) {
            true
        }
    }

    @RequiresApi(AndroidVersion.ANDROID_4_4)
    fun checkOpNoThrow(context: Context, opName: String?): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode: Int
        mode = if (AndroidVersion.isAndroid10()) {
            appOps.unsafeCheckOpNoThrow(
                opName!!,
                context.applicationInfo.uid, context.packageName
            )
        } else {
            appOps.checkOpNoThrow(
                opName!!,
                context.applicationInfo.uid, context.packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    /**
     * 解决 Android 12 调用 shouldShowRequestPermissionRationale 出现内存泄漏的问题
     * Android 12L 和 Android 13 版本经过测试不会出现这个问题，证明 Google 在新版本上已经修复了这个问题
     * 但是对于 Android 12 仍是一个历史遗留问题，这是我们所有应用开发者不得不面对的一个事情
     *
     * issues 地址：https://github.com/getActivity/XXPermissions/issues/133
     */
    @RequiresApi(api = AndroidVersion.ANDROID_6)
    fun shouldShowRequestPermissionRationale(
        activity: Activity,
        permission: String
    ): Boolean {
        if (AndroidVersion.getAndroidVersionCode() === AndroidVersion.ANDROID_12) {
            try {
                val packageManager = activity.application.packageManager
                val method =
                    PackageManager::class.java.getMethod(
                        "shouldShowRequestPermissionRationale",
                        String::class.java
                    )
                return method.invoke(packageManager, permission) as Boolean
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
        return activity.shouldShowRequestPermissionRationale(permission!!)
    }

    /**
     * 延迟一段时间执行 OnActivityResult，避免有些机型明明授权了，但还是回调失败的问题
     */
    fun postActivityResult( permissions: List<String>, runnable: Runnable) {
        var delayMillis: Long
        delayMillis = if (AndroidVersion.isAndroid11()) {
            200
        } else {
            300
        }
        if (PhoneRomUtils.isEmui() || PhoneRomUtils.isHarmonyOs()) {
            // 需要加长时间等待，不然某些华为机型授权了但是获取不到权限
            delayMillis = if (AndroidVersion.isAndroid8()) {
                300
            } else {
                500
            }
        } else if (PhoneRomUtils.isMiui()) {
            // 经过测试，发现小米 Android 11 及以上的版本，申请这个权限需要 1 秒钟才能判断到
            // 因为在 Android 10 的时候，这个特殊权限弹出的页面小米还是用谷歌原生的
            // 然而在 Android 11 之后的，这个权限页面被小米改成了自己定制化的页面
            // 测试了原生的模拟器和 vivo 云测并发现没有这个问题，所以断定这个 Bug 就是小米特有的
            if (AndroidVersion.isAndroid11() &&
                containsPermission(permissions, Permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            ) {
                delayMillis = 1000
            }
        }
        postDelayed(runnable, delayMillis)
    }

    /**
     * 延迟一段时间执行
     */
    fun postDelayed(runnable: Runnable, delayMillis: Long) {
        HANDLER.postDelayed(runnable, delayMillis)
    }

    /**
     * 当前是否处于 debug 模式
     */
    fun isDebugMode( context: Context): Boolean {
        return context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
    }

    fun getAndroidManifestInfo(context: Context): AndroidManifestInfo? {
        val apkPathCookie = findApkPathCookie(context, context.applicationInfo.sourceDir)
        // 如果 cookie 为 0，证明获取失败
        if (apkPathCookie == 0) {
            return null
        }
        var androidManifestInfo: AndroidManifestInfo? = null
        try {
            androidManifestInfo = AndroidManifestParser.parseAndroidManifest(context, apkPathCookie)
            // 如果读取到的包名和当前应用的包名不是同一个的话，证明这个清单文件的内容不是当前应用的
            // 具体案例：https://github.com/getActivity/XXPermissions/issues/102
            if (androidManifestInfo != null) {
                if (!TextUtils.equals(
                        context.packageName,
                        androidManifestInfo.packageName
                    )
                ) {
                    return null
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        }
        return androidManifestInfo
    }

//    /**
//     * 优化权限回调结果
//     */
//    fun optimizePermissionResults(
//        activity: Activity,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        for (i in 0 until permissions.size) {
//            val permission = permissions[i]
//
//            // 如果这个权限是特殊权限，则需要重新检查权限的状态
//            if (PermissionApi.isSpecialPermission(permission)) {
//                grantResults[i] = getPermissionResult(activity, permission)
//                continue
//            }
//
//            // 如果是读取应用列表权限（国产权限），则需要重新检查权限的状态
//            if (equalsPermission(permission, Permission.GET_INSTALLED_APPS)) {
//                grantResults[i] = getPermissionResult(activity, permission)
//                continue
//            }
//
//            // 如果是在 Android 14 上面，并且是图片权限或者视频权限，则需要重新检查权限的状态
//            // 这是因为用户授权部分图片或者视频的时候，READ_MEDIA_VISUAL_USER_SELECTED 权限状态是授予的
//            // 但是 READ_MEDIA_IMAGES 和 READ_MEDIA_VIDEO 的权限状态是拒绝的
//            if (isAndroid14() &&
//                (equalsPermission(permission, Permission.READ_MEDIA_IMAGES) ||
//                        equalsPermission(permission, Permission.READ_MEDIA_VIDEO))
//            ) {
//                grantResults[i] = getPermissionResult(activity, permission)
//                continue
//            }
//            if (isAndroid13() && getTargetSdkVersionCode(activity) >= AndroidVersion.ANDROID_13 &&
//                equalsPermission(permission, Permission.WRITE_EXTERNAL_STORAGE)
//            ) {
//                // 在 Android 13 不能申请 WRITE_EXTERNAL_STORAGE，会被系统直接拒绝，在这里需要重新检查权限的状态
//                grantResults[i] = getPermissionResult(activity, permission)
//                continue
//            }
//            if (Permission.getDangerPermissionFromAndroidVersion(permission) > getAndroidVersionCode()) {
//                // 如果是申请了新权限，但却是旧设备上面运行的，会被系统直接拒绝，在这里需要重新检查权限的状态
//                grantResults[i] = getPermissionResult(activity, permission)
//            }
//        }
//    }

    /**
     * 将数组转换成 ArrayList
     *
     * 这里解释一下为什么不用 Arrays.asList
     * 第一是返回的类型不是 java.util.ArrayList 而是 java.util.Arrays.ArrayList
     * 第二是返回的 ArrayList 对象是只读的，也就是不能添加任何元素，否则会抛异常
     */
    fun <T> asArrayList(vararg array: T): ArrayList<T> {
        var initialCapacity = 0
        if (array != null) {
            initialCapacity = array.size
        }
        val list = ArrayList<T>(initialCapacity)
        if (array == null || array.size == 0) {
            return list
        }
        for (t in array) {
            list.add(t)
        }
        return list
    }

    @SafeVarargs
    fun <T> asArrayLists( vararg arrays: Array<T>): ArrayList<T> {
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

    fun findActivity(context: Context): FragmentActivity? {
        var context = context
        do {
            context = if (context is FragmentActivity) {
                return context
            } else if (context is ContextWrapper) {
                // android.content.ContextWrapper
                // android.content.MutableContextWrapper
                // android.support.v7.view.ContextThemeWrapper
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
    fun findApkPathCookie(context: Context, apkPath: String?): Int {
        val assets = context.assets
        var cookie: Int
        try {
            if (AndroidVersion.getTargetSdkVersionCode(context) >= AndroidVersion.ANDROID_9 && AndroidVersion.getAndroidVersionCode() >= AndroidVersion.ANDROID_9 && AndroidVersion.getAndroidVersionCode() < AndroidVersion.ANDROID_11) {

                // 反射套娃操作：实测这种方式只在 Android 9.0 和 Android 10.0 有效果，在 Android 11 上面就失效了
                val metaGetDeclaredMethod = Class::class.java.getDeclaredMethod(
                    "getDeclaredMethod", String::class.java, Array<Any>::class.java
                )
                metaGetDeclaredMethod.isAccessible = true
                // 注意 AssetManager.findCookieForPath 是 Android 9.0（API 28）的时候才添加的方法
                // 而 Android 9.0 用的是 AssetManager.addAssetPath 来获取 cookie
                // 具体可以参考 PackageParser.parseBaseApk 方法源码的实现
                val findCookieForPathMethod = metaGetDeclaredMethod.invoke(
                    AssetManager::class.java,
                    "findCookieForPath", arrayOf<Class<*>>(String::class.java)
                ) as Method
                if (findCookieForPathMethod != null) {
                    findCookieForPathMethod.isAccessible = true
                    cookie = findCookieForPathMethod.invoke(context.assets, apkPath) as Int
                    if (cookie != null) {
                        return cookie
                    }
                }
            }
            val addAssetPathMethod = assets.javaClass.getDeclaredMethod(
                "addAssetPath",
                String::class.java
            )
            cookie = addAssetPathMethod.invoke(assets, apkPath) as Int
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

        // 获取失败直接返回 0
        // 为什么不直接返回 Integer，而是返回 int 类型？
        // 去看看 AssetManager.findCookieForPath 获取失败会返回什么就知道了
        return 0
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
                return metaData.getBoolean(metaKey)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 锁定当前 Activity 的方向
     */
    @SuppressLint("SwitchIntDef")
    fun lockActivityOrientation(activity: Activity) {
        try {
            // 兼容问题：在 Android 8.0 的手机上可以固定 Activity 的方向，但是这个 Activity 不能是透明的，否则就会抛出异常
            // 复现场景：只需要给 Activity 主题设置 <item name="android:windowIsTranslucent">true</item> 属性即可
            when (activity.resources.configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> activity.requestedOrientation =
                    if (isActivityReverse(activity)) ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE else ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

                Configuration.ORIENTATION_PORTRAIT -> activity.requestedOrientation =
                    if (isActivityReverse(activity)) ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

                else -> {}
            }
        } catch (e: IllegalStateException) {
            // java.lang.IllegalStateException: Only fullscreen activities can request orientation
            e.printStackTrace()
        }
    }

    /**
     * 判断 Activity 是否反方向旋转了
     */
    fun isActivityReverse(activity: Activity): Boolean {
        // 获取 Activity 旋转的角度
        val activityRotation: Int
        activityRotation = if (AndroidVersion.isAndroid11()) {
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

    /**
     * 判断这个意图的 Activity 是否存在
     */
    fun areActivityIntent(context: Context,  intent: Intent?): Boolean {
        if (intent == null) {
            return false
        }
        // 这里为什么不用 Intent.resolveActivity(intent) != null 来判断呢？
        // 这是因为在 OPPO R7 Plus （Android 5.0）会出现误判，明明没有这个 Activity，却返回了 ComponentName 对象
        val packageManager = context.packageManager
        return if (AndroidVersion.isAndroid13()) {
            !packageManager.queryIntentActivities(
                intent,
                PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong())
            ).isEmpty()
        } else !packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            .isEmpty()
    }

//    /**
//     * 根据传入的权限自动选择最合适的权限设置页
//     *
//     * @param permissions                 请求失败的权限
//     */
//    fun getSmartPermissionIntent(
//       context: Context,
//       permissions: List<String>
//    ): Intent? {
//        // 如果失败的权限里面不包含特殊权限
//        if (permissions == null || permissions.isEmpty()) {
//            return PermissionIntentManager.getApplicationDetailsIntent(context)
//        }
//
//        // 危险权限统一处理
//        if (!PermissionApi.containsSpecialPermission(permissions)) {
//            return if (permissions.size == 1) {
//                PermissionApi.getPermissionIntent(context, permissions[0])
//            } else PermissionIntentManager.getApplicationDetailsIntent(context)
//        }
//        when (permissions.size) {
//            1 ->                 // 如果当前只有一个权限被拒绝了
//                return PermissionApi.getPermissionIntent(context, permissions[0])
//
//            2 -> if (!AndroidVersion.isAndroid13() &&
//                containsPermission(permissions, Permission.NOTIFICATION_SERVICE) &&
//                containsPermission(permissions, Permission.POST_NOTIFICATIONS)
//            ) {
//                return PermissionApi.getPermissionIntent(context, Permission.NOTIFICATION_SERVICE)
//            }
//
//            3 -> if (AndroidVersion.isAndroid11() &&
//                containsPermission(permissions, Permission.MANAGE_EXTERNAL_STORAGE) &&
//                containsPermission(permissions, Permission.READ_EXTERNAL_STORAGE) &&
//                containsPermission(permissions, Permission.WRITE_EXTERNAL_STORAGE)
//            ) {
//                return PermissionApi.getPermissionIntent(
//                    context,
//                    Permission.MANAGE_EXTERNAL_STORAGE
//                )
//            }
//
//            else -> {}
//        }
//        return PermissionIntentManager.getApplicationDetailsIntent(context)
//    }

    /**
     * 判断两个权限字符串是否为同一个
     */
    fun equalsPermission(permission1: String, permission2: String): Boolean {
        val length = permission1.length
        if (length != permission2.length) {
            return false
        }

        // 因为权限字符串都是 android.permission 开头
        // 所以从最后一个字符开始判断，可以提升 equals 的判断效率
        for (i in length - 1 downTo 0) {
            if (permission1[i] != permission2[i]) {
                return false
            }
        }
        return true
    }

    /**
     * 判断权限集合中是否包含某个权限
     */
    fun containsPermission(permissions: Array<String>,  permission: String): Boolean {
        return containsPermission(Arrays.asList(*permissions), permission)
    }

    /**
     * 判断权限集合中是否包含某个权限
     */
    fun containsPermission(
        permissions: Collection<String>,
        permission: String
    ): Boolean {
        if (permissions.isEmpty()) {
            return false
        }
        for (s in permissions) {
            // 使用 equalsPermission 来判断可以提升代码效率
            if (equalsPermission(s, permission)) {
                return true
            }
        }
        return false
    }

    /**
     * 获取包名 uri
     */
    fun getPackageNameUri(context: Context): Uri? {
        return Uri.parse("package:" + context.packageName)
    }


    fun getSystemPropertyValue(propertyName: String): String {
        var prop: String?
        try {
            prop = getSystemPropertyByReflect(propertyName)
            if (prop != null && !prop.isEmpty()) {
                return prop
            }
        } catch (ignored: Exception) {
        }

        try {
            prop = getSystemPropertyByShell(propertyName)
            if (prop != null && !prop.isEmpty()) {
                return prop
            }
        } catch (ignored: IOException) {
        }

        try {
            prop = getSystemPropertyByStream(propertyName)
            if (prop != null && !prop.isEmpty()) {
                return prop
            }
        } catch (ignored: IOException) {
        }

        return ""
    }

    /**
     * 获取系统属性值（通过反射系统类）
     */
    @SuppressLint("PrivateApi")
    @Throws(
        ClassNotFoundException::class,
        InvocationTargetException::class,
        NoSuchMethodException::class,
        IllegalAccessException::class
    )
    private fun getSystemPropertyByReflect(key: String): String {
        val clz = Class.forName("android.os.SystemProperties")
        val getMethod = clz.getMethod("get", String::class.java, String::class.java)
        return getMethod.invoke(clz, key, "") as String
    }

    /**
     * 获取系统属性值（通过 shell 命令）
     */
    @Throws(IOException::class)
    private fun getSystemPropertyByShell(propName: String): String? {
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $propName")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            val firstLine = input.readLine()
            if (firstLine != null) {
                return firstLine
            }
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (ignored: IOException) {
                }
            }
        }
        return null
    }

    /**
     * 获取系统属性值（通过读取系统文件）
     */
    @Throws(IOException::class)
    private fun getSystemPropertyByStream(key: String): String {
        var inputStream: FileInputStream? = null
        try {
            val prop: Properties = Properties()
            val file = File(Environment.getRootDirectory(), "build.prop")
            inputStream = FileInputStream(file)
            prop.load(inputStream)
            return prop.getProperty(key, "")
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (ignored: IOException) {
                }
            }
        }
    }


    /**
     *
     * 显示权限设置弹窗
     */
    fun showPermissionSettingDialog(
        activity: FragmentActivity, allPermissions: List<String>,
        deniedPermissions: List<String>, callback: OnPermissionCallback
    ) {
        if (activity == null || activity.isFinishing || Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed) {
            return
        }
        var message: String? = null
        val permissionNames: List<String> =
            PermissionNameConvert.permissionsToNames(activity, deniedPermissions)
        if (!permissionNames.isEmpty()) {
            if (deniedPermissions.size == 1) {
                val deniedPermission = deniedPermissions[0]
                if (Permission.ACCESS_BACKGROUND_LOCATION.equals(deniedPermission)) {
                    message = activity.getString(
                        R.string.permission_manual_assign_fail_background_location_hint,
                        getBackgroundPermissionOptionLabel(activity)
                    )
                } else if (Permission.BODY_SENSORS_BACKGROUND.equals(deniedPermission)) {
                    message = activity.getString(
                        R.string.permission_manual_assign_fail_background_sensors_hint,
                        getBackgroundPermissionOptionLabel(activity)
                    )
                } else {
                    message = activity.getString(R.string.permission_manual_fail_hint)
                }
            }
            if (TextUtils.isEmpty(message)) {
                message = activity.getString(
                    R.string.permission_manual_assign_fail_hint,
                    PermissionNameConvert.listToString(activity, permissionNames)
                )
            }
        } else {
            message = activity.getString(R.string.permission_manual_fail_hint)
        }

        // 这里的 Dialog 只是示例，没有用 DialogFragment 来处理 Dialog 生命周期
        DialogUtils.getConfirmDialog(activity,message,{ dialog, which ->
             //确定
            XXPermissions.startPermissionActivity(activity,
                deniedPermissions, object : OnPermissionPageCallback {
                    override fun onGranted() {
                        if (callback == null) {
                            return
                        }
                        callback!!.onGranted(allPermissions, true)
                    }

                    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
                    override fun onDenied() {
                        showPermissionSettingDialog(
                            activity, allPermissions,
                            XXPermissions.getDenied(activity, allPermissions), callback
                        )
                    }
                })
        }){
                dialog, which ->
        }
    }


    /**
     * 获取后台权限的《始终允许》选项的文案
     */

    private fun getBackgroundPermissionOptionLabel(context: Context): String {
        var backgroundPermissionOptionLabel = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            backgroundPermissionOptionLabel =
                context.packageManager.backgroundPermissionOptionLabel.toString()
        }
        if (TextUtils.isEmpty(backgroundPermissionOptionLabel)) {
            backgroundPermissionOptionLabel =
                context.getString(R.string.permission_background_default_option_label)
        }
        return backgroundPermissionOptionLabel
    }

}