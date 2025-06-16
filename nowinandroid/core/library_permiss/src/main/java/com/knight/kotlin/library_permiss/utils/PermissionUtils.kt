package com.knight.kotlin.library_permiss.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.content.pm.PackageManager.ResolveInfoFlags
import android.content.res.AssetManager
import android.net.Uri
import android.os.Environment
import android.text.TextUtils
import androidx.fragment.app.Fragment
import com.knight.kotlin.library_permiss.AndroidManifestInfo
import com.knight.kotlin.library_permiss.AndroidManifestParser.parseAndroidManifest
import com.knight.kotlin.library_permiss.AndroidVersionTools
import com.knight.kotlin.library_permiss.AndroidVersionTools.getCurrentAndroidVersionCode
import com.knight.kotlin.library_permiss.AndroidVersionTools.isAdaptationAndroidVersionNewFeatures
import com.knight.kotlin.library_permiss.AndroidVersionTools.isAndroid10
import com.knight.kotlin.library_permiss.AndroidVersionTools.isAndroid13
import com.knight.kotlin.library_permiss.AndroidVersionTools.isAndroid4_4
import com.knight.kotlin.library_permiss.AndroidVersionTools.isAndroid6
import org.xmlpull.v1.XmlPullParserException
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.Properties


/**
 * Author:Knight
 * Time:2022/1/20 15:38
 * Description:PermissionUtils
 */
object PermissionUtils {

    /**
     * 判断某个危险权限是否授予了
     */
    fun isGrantedPermission( context: Context,  permission: String?): Boolean {
        if (!isAndroid6()) {
            return true
        }
        return context.checkSelfPermission(permission!!) == PackageManager.PERMISSION_GRANTED
    }

    fun checkOpNoThrow(context: Context, opFieldName: String?, opDefaultValue: Int): Boolean {
        if (!isAndroid4_4()) {
            return true
        }
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val appInfo = context.applicationInfo
        val pkg = context.applicationContext.packageName
        val uid = appInfo.uid
        try {
            val appOpsClass = Class.forName(AppOpsManager::class.java.name)
            var opValue: Int
            try {
                val opValueField = appOpsClass.getDeclaredField(opFieldName)
                opValue = opValueField[Int::class.java] as Int
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
                opValue = opDefaultValue
            }
            val checkOpNoThrowMethod = appOpsClass.getMethod(
                "checkOpNoThrow", Integer.TYPE, Integer.TYPE,
                String::class.java
            )
            return (checkOpNoThrowMethod.invoke(
                appOps,
                opValue,
                uid,
                pkg
            ) as Int == AppOpsManager.MODE_ALLOWED)
        } catch (e: ClassNotFoundException) {
            return true
        } catch (e: NoSuchMethodException) {
            return true
        } catch (e: InvocationTargetException) {
            return true
        } catch (e: IllegalAccessException) {
            return true
        } catch (e: RuntimeException) {
            return true
        }
    }

    fun checkOpNoThrow(context: Context, opName: String?): Boolean {
        if (!isAndroid4_4()) {
            return true
        }
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (isAndroid10()) {
            appOps.unsafeCheckOpNoThrow(opName!!, context.applicationInfo.uid, context.packageName)
        } else {
            appOps.checkOpNoThrow(opName!!, context.applicationInfo.uid, context.packageName)
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    /**
     * 解决 Android 12 调用 shouldShowRequestPermissionRationale 出现内存泄漏的问题
     * Android 12L 和 Android 13 版本经过测试不会出现这个问题，证明 Google 在新版本上已经修复了这个问题
     * 但是对于 Android 12 仍是一个历史遗留问题，这是我们所有 Android App 开发者不得不面对的一个事情
     *
     * issues 地址：https://github.com/getActivity/XXPermissions/issues/133
     */
    fun shouldShowRequestPermissionRationale(
         activity: Activity,
         permission: String?
    ): Boolean {
        if (!isAndroid6()) {
            return false
        }
        if (getCurrentAndroidVersionCode() == AndroidVersionTools.ANDROID_12) {
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
     * 判断某个权限是否勾选了不再询问的选项
     */
    fun isDoNotAskAgainPermission(
         activity: Activity,
         permission: String?
    ): Boolean {
        if (!isAndroid6()) {
            return false
        }
        return !isGrantedPermission(activity, permission) &&
                !shouldShowRequestPermissionRationale(activity, permission)
    }

    /**
     * 当前是否处于 debug 模式
     */
    fun isDebugMode( context: Context): Boolean {
        return (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    }

    
    fun getAndroidManifestInfo(context: Context): AndroidManifestInfo? {
        val apkPathCookie = findApkPathCookie(context, context.applicationInfo.sourceDir)
        // 如果 cookie 为 0，证明获取失败
        if (apkPathCookie == 0) {
            return null
        }

        var androidManifestInfo: AndroidManifestInfo? = null
        try {
            androidManifestInfo = parseAndroidManifest(context, apkPathCookie)
            // 如果读取到的包名和当前应用的包名不是同一个的话，证明这个清单文件的内容不是当前应用的
            // 具体案例：https://github.com/getActivity/XXPermissions/issues/102
            if (!TextUtils.equals(context.packageName, androidManifestInfo!!.packageName)) {
                return null
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        }

        return androidManifestInfo
    }

    /**
     * 将数组转换成 ArrayList
     *
     * 这里解释一下为什么不用 Arrays.asList
     * 第一是返回的类型不是 java.util.ArrayList 而是 java.util.Arrays.ArrayList
     * 第二是返回的 ArrayList 对象是只读的，也就是不能添加任何元素，否则会抛异常
     */
    
    fun <T> asArrayList( vararg array: T): ArrayList<T> {
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
    
    fun findActivity( context: Context?): Activity? {
        var context = context
        do {
            if (context is Activity) {
                return context
            } else if (context is ContextWrapper) {
                // android.content.ContextWrapper
                // android.content.MutableContextWrapper
                // android.support.v7.view.ContextThemeWrapper
                context = context.baseContext
            } else {
                return null
            }
        } while (context != null)
        return null
    }

    /**
     * 判断 Activity 是不是不可用
     */
    fun isActivityUnavailable( activity: Activity?): Boolean {
        return activity == null || activity.isDestroyed || activity.isFinishing
    }


    /**
     * 判断 Fragment 是不是不可用（App 包版本）
     */
    @Suppress("deprecation")
    fun isFragmentUnavailable( appFragment: Fragment?): Boolean {
        return appFragment == null || !appFragment.isAdded() || appFragment.isRemoving()
    }

    /**
     * 获取当前应用 Apk 在 AssetManager 中的 Cookie，如果获取失败，则为 0
     */
    @SuppressLint("PrivateApi")
    fun findApkPathCookie( context: Context,  apkPath: String?): Int {
        val assets = context.assets
        var cookie: Int

        try {
            if (isAdaptationAndroidVersionNewFeatures(context, AndroidVersionTools.ANDROID_9) &&
                getCurrentAndroidVersionCode() < AndroidVersionTools.ANDROID_11
            ) {
                // 反射套娃操作：实测这种方式只在 Android 9.0 和 Android 10.0 有效果，在 Android 11 上面就失效了

                val metaGetDeclaredMethod = Class::class.java.getDeclaredMethod(
                    "getDeclaredMethod",
                    String::class.java,
                    arrayOf<Class<*>>()::class.java
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
    fun isScopedStorage( context: Context): Boolean {
        try {
            val metaKey = "ScopedStorage"
            val metaData = context.packageManager.getApplicationInfo(
                context.packageName, PackageManager.GET_META_DATA
            ).metaData
            if (metaData != null && metaData.containsKey(metaKey)) {
                return metaData.getBoolean(metaKey)
            }
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 判断这个意图的 Activity 是否存在
     */
    fun areActivityIntent( context: Context,  intent: Intent?): Boolean {
        if (intent == null) {
            return false
        }
        // 这里为什么不用 Intent.resolveActivity(intent) != null 来判断呢？
        // 这是因为在 OPPO R7 Plus （Android 5.0）会出现误判，明明没有这个 Activity，却返回了 ComponentName 对象
        val packageManager = context.packageManager
        if (isAndroid13()) {
            return !packageManager.queryIntentActivities(
                intent,
                ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong())
            ).isEmpty()
        }
        return !packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            .isEmpty()
    }

    /**
     * 判断两个权限字符串是否为同一个
     */
    fun equalsPermission(permission1: String,  permission2: String): Boolean {
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
    fun containsPermission(
         permissions: Collection<String?>,
         permission: String?
    ): Boolean {
        if (permissions.isEmpty()) {
            return false
        }

        if (permission == null) {
            return false
        }
        for (s in permissions) {
            // 使用 equalsPermission 来判断可以提升代码执行效率
            if (equalsPermission(s!!, permission!!)) {
                return true
            }
        }
        return false
    }

    /**
     * 获取包名 uri
     */
    fun getPackageNameUri( context: Context): Uri {
        return Uri.parse("package:" + context.packageName)
    }

    /**
     * 获取系统属性值（多种方式）
     */
    
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
            val prop = Properties()
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



}