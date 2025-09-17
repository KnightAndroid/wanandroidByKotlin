package com.knight.kotlin.library_permiss.tools

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.content.pm.PackageManager.ResolveInfoFlags
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import com.knight.kotlin.library_permiss.permission.base.IPermission
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.lang.reflect.InvocationTargetException
import java.util.Properties


/**
 * Author:Knight
 * Time:2022/1/20 15:38
 * Description:PermissionUtils
 */
object PermissionUtils {
    /**
     * 当前是否处于 debug 模式
     */
    fun isDebugMode( context: Context): Boolean {
        return (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
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
        val list: ArrayList<T> = ArrayList(initialCapacity)
        if (array == null || array.size == 0) {
            return list
        }
        for (t in array) {
            list.add(t)
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
        return appFragment == null || !appFragment.isAdded || appFragment.isRemoving
    }

    /**
     * 通过 MetaData 获得布尔值
     *
     * @param metaKey               Meta Key 值
     * @param defaultValue          当获取不到时返回的默认值
     */
    fun getBooleanByMetaData( context: Context,  metaKey: String?, defaultValue: Boolean): Boolean {
        try {
            val metaData: Bundle? = context.packageManager.getApplicationInfo(
                context.packageName, PackageManager.GET_META_DATA
            ).metaData
            if (metaData != null && metaData.containsKey(metaKey)) {
                return metaData.getBoolean(metaKey)
            }
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
        }
        return defaultValue
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
        val packageManager = context.packageManager ?: return false
        if (PermissionVersion.isAndroid13()) {
            return !packageManager.queryIntentActivities(
                intent,
                ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong())
            ).isEmpty()
        }
        return !packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).isEmpty()
    }

    /**
     * 比较字符串是否相等（从第一个字符串开始比较）
     */
    fun equalsString( s1: String?,  s2: String?): Boolean {
        if (s1 == null || s2 == null) {
            return false
        }
        if (s1.hashCode() == s2.hashCode()) {
            return true
        }
        val length = s1.length
        if (length != s2.length) {
            return false
        }

        for (i in 0..<length) {
            if (s1[i] != s2[i]) {
                return false
            }
        }
        return true
    }

    /**
     * 比较字符串是否相等（从最后一个字符串开始比较）
     */
    fun reverseEqualsString( s1: String?,  s2: String?): Boolean {
        if (s1 == null || s2 == null) {
            return false
        }
        if (s1.hashCode() == s2.hashCode()) {
            return true
        }
        val length = s1.length
        if (length != s2.length) {
            return false
        }

        for (i in length - 1 downTo 0) {
            if (s1[i] != s2[i]) {
                return false
            }
        }
        return true
    }

    /**
     * 判断两个权限是否为同一个
     */
    fun equalsPermission( permission1: String?,  permission2: String?): Boolean {
        // 因为权限字符串大多数都是以 android.permission 开头
        // 所以从最后一个字符开始判断，可以大大提升 equals 的判断效率
        return reverseEqualsString(permission1, permission2)
    }

    /**
     * 判断两个权限是否为同一个
     */
    fun equalsPermission( permission1: IPermission,  permission2: String?): Boolean {
        // 因为权限字符串大多数都是以 android.permission 开头
        // 所以从最后一个字符开始判断，可以大大提升 equals 的判断效率
        return reverseEqualsString(permission1.getPermissionName(), permission2)
    }

    fun equalsPermission( permission1: IPermission,  permission2: IPermission): Boolean {
        // 因为权限字符串大多数都是以 android.permission 开头
        // 所以从最后一个字符开始判断，可以大大提升 equals 的判断效率
        return reverseEqualsString(permission1.getPermissionName(), permission2.getPermissionName())
    }

    /**
     * 判断权限集合中是否包含某个权限
     */
    fun containsPermission( permissions: Collection<IPermission>,  permission: IPermission): Boolean {
        if (permissions.isEmpty()) {
            return false
        }
        for (item in permissions) {
            // 使用 equalsPermission 来判断可以提升代码执行效率
            if (equalsPermission(permission, item.getPermissionName())) {
                return true
            }
        }
        return false
    }

    /**
     * 判断权限集合中是否包含某个权限
     */
    fun containsPermission( permissions: Collection<IPermission>,  permissionName: String): Boolean {
        if (permissions.isEmpty()) {
            return false
        }
        for (item in permissions) {
            // 使用 equalsPermission 来判断可以提升代码执行效率
            if (equalsPermission(item.getPermissionName(), permissionName)) {
                return true
            }
        }
        return false
    }

    /**
     * 将 List<IPermission> 转换成 List<String> 对象
    </String></IPermission> */

    fun convertPermissionList(permissions: List<IPermission>?): List<String> {
        val list = mutableListOf<String>()
        if (permissions.isNullOrEmpty()) {
            return list
        }
        for (permission in permissions) {
            list.add(permission.getPermissionName())
        }
        return list
    }


    fun convertPermissionList(permissions: Array<IPermission>?): List<String> {
        val list = mutableListOf<String>()
        if (permissions == null) {
            return list
        }
        for (permission in permissions) {
            list.add(permission.getPermissionName())
        }
        return list
    }

    /**
     * 将 List<IPermission> 转换成 String[] 对象
    </IPermission> */

    fun convertPermissionArray(permissions: List<IPermission>): Array<String> {
        if (permissions.isNullOrEmpty()) {
            return emptyArray()
        }
        return Array(permissions.size) { i -> permissions[i].getPermissionName() }
    }

    /**
     * 将 List<IPermission> 转换成 String[] 对象
    </IPermission> */

    fun convertPermissionArray(context: Context, permissions: List<IPermission>): Array<String> {
        if (permissions.isEmpty()) {
            return emptyArray()
        }
        return permissions.map { it.getRequestPermissionName(context) }.toTypedArray()
    }



    /**
     * 获取包名 uri
     */
    fun getPackageNameUri( context: Context): Uri {
        return Uri.parse("package:" + context.packageName)
    }

    /**
     * 判断某个类的类名是否存在
     */
    fun isClassExist(className: String): Boolean {
        if (className == null) {
            return false
        }
        if (className.isEmpty()) {
            return false
        }
        try {
            // 判断这个类有是否存在，如果存在的话，证明是有效的
            // 如果不存在的话，证明无效的，也是需要重新授权的
            Class.forName(className)
            return true
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            return false
        }
    }

    /**
     * 比较两个 Intent 列表的内容是否一致
     */
    fun equalsIntentList(intentList1: List<Intent>, intentList2: List<Intent>): Boolean {
        if (intentList1.size != intentList2.size) {
            return false
        }

        for (i in intentList1.indices) {
            if (!intentList1[i].filterEquals(intentList2[i])) {
                return false
            }
        }

        return true
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