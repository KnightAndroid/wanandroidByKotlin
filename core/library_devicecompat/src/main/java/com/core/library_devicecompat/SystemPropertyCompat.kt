package com.core.library_devicecompat

import android.annotation.SuppressLint
import android.os.Environment
import android.text.TextUtils
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.lang.reflect.InvocationTargetException
import java.util.Properties


/**
 * @author created by luguian
 * @organize
 * @Date 2025/9/18 9:53
 * @descript:系统属性兼容类
 */
object SystemPropertyCompat {
    /**
     * 获取单个系统属性值
     */
    fun getSystemPropertyValue(key: String): String {
        if (key == null || key.isEmpty()) {
            return ""
        }

        var propertyValue: String? = null
        try {
            propertyValue = getSystemPropertyByReflect(key)
        } catch (ignored: Exception) {
            // default implementation ignored
        }

        if (propertyValue != null && !propertyValue.isEmpty()) {
            return propertyValue
        }

        try {
            propertyValue = getSystemPropertyByShell(key)
        } catch (ignored: IOException) {
            // default implementation ignored
        }

        if (propertyValue != null && !propertyValue.isEmpty()) {
            return propertyValue
        }

        try {
            propertyValue = getSystemPropertyByStream(key)
        } catch (ignored: IOException) {
            // default implementation ignored
        }

        if (propertyValue != null && !propertyValue.isEmpty()) {
            return propertyValue
        }

        return ""
    }

    /**
     * 获取多个系统属性值
     */
    fun getSystemPropertyValues( keys: Array<String>): Array<String?> {
        if (keys == null) {
            return emptyArray()
        }

        val propertyValues = arrayOfNulls<String>(keys.size)

        for (i in keys.indices) {
            propertyValues[i] = getSystemPropertyValue(keys[i])
        }
        return propertyValues
    }

    /**
     * 获取多个系统属性中的任一一个值
     */
    fun getSystemPropertyAnyOneValue( keys: Array<String>): String {
        if (keys == null) {
            return ""
        }

        for (key in keys) {
            val propertyValue = getSystemPropertyValue(key)
            if (!propertyValue.isEmpty()) {
                return propertyValue
            }
        }
        return ""
    }

    /**
     * 判断某个系统属性是否存在
     */
    fun isSystemPropertyExist( key: String): Boolean {
        return !TextUtils.isEmpty(getSystemPropertyValue(key))
    }

    /**
     * 判断多个系统属性是否有任一一个存在
     */
    fun isSystemPropertyAnyOneExist( keys: Array<String>): Boolean {
        if (keys == null) {
            return false
        }
        for (key in keys) {
            if (isSystemPropertyExist(key)) {
                return true
            }
        }
        return false
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
    private fun getSystemPropertyByShell(key: String): String? {
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $key")
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
                    // default implementation ignored
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
                    // default implementation ignored
                }
            }
        }
    }
}