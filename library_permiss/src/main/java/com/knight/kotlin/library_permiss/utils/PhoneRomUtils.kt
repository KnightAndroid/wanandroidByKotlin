package com.knight.kotlin.library_permiss.utils

import android.annotation.SuppressLint
import android.os.Build
import android.os.Environment
import android.text.TextUtils
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid10
import com.knight.kotlin.library_permiss.utils.PermissionUtils.getSystemPropertyValue
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.lang.reflect.InvocationTargetException
import java.util.Locale
import java.util.Properties


/**
 * Author:Knight
 * Time:2023/8/30 17:29
 * Description:PhoneRomUtils
 */
object PhoneRomUtils {

    private val ROM_HUAWEI: Array<String> = arrayOf("huawei")
    private
    val ROM_VIVO: Array<String> = arrayOf("vivo")
    private val ROM_XIAOMI: Array<String> = arrayOf("xiaomi")
    private val ROM_OPPO: Array<String> = arrayOf("oppo")
    private val ROM_LEECO: Array<String> = arrayOf("leeco", "letv")
    private val ROM_360: Array<String> = arrayOf("360", "qiku")
    private val ROM_ZTE: Array<String> = arrayOf("zte")
    private val ROM_ONEPLUS: Array<String> = arrayOf("oneplus")
    private val ROM_NUBIA: Array<String> = arrayOf("nubia")
    private val ROM_SAMSUNG: Array<String> = arrayOf("samsung")
    private val ROM_HONOR: Array<String> = arrayOf("honor")

    private const val ROM_NAME_MIUI: String = "ro.miui.ui.version.name"
    private const val ROM_NAME_HYPER_OS: String = "ro.mi.os.version.name"

    private const val VERSION_PROPERTY_HUAWEI: String = "ro.build.version.emui"
    private const val VERSION_PROPERTY_VIVO: String = "ro.vivo.os.build.display.id"
    private const val VERSION_PROPERTY_XIAOMI: String = "ro.build.version.incremental"
    private val VERSION_PROPERTY_OPPO: Array<String> = arrayOf("ro.build.version.opporom", "ro.build.version.oplusrom.display")
    private const val VERSION_PROPERTY_LEECO: String = "ro.letv.release.version"
    private const val VERSION_PROPERTY_360: String = "ro.build.uiversion"
    private const val VERSION_PROPERTY_ZTE: String = "ro.build.MiFavor_version"
    private const val VERSION_PROPERTY_ONEPLUS: String = "ro.rom.version"
    private const val VERSION_PROPERTY_NUBIA: String = "ro.build.rom.id"

    /**
     * 经过测试，得出以下结论
     * Magic 7.0 存放系统版本的属性是 msc.config.magic.version，
     * Magic 4.0 和 Magic 4.1 用的是 ro.build.version.magic 属性
     */
    private val VERSION_PROPERTY_MAGIC =
        arrayOf("msc.config.magic.version", "ro.build.version.magic")

    /**
     * 判断当前厂商系统是否为 emui
     */
    fun isEmui(): Boolean {
        return !TextUtils.isEmpty(getPropertyName(VERSION_PROPERTY_HUAWEI))
    }

    /**
     * 判断当前厂商系统是否为 miui
     */
    fun isMiui(): Boolean {
        return !TextUtils.isEmpty(getPropertyName(ROM_NAME_MIUI))
    }

    /**
     * 判断当前厂商系统是否为澎湃系统
     */
    fun isHyperOs(): Boolean {
        return !TextUtils.isEmpty(getSystemPropertyValue(ROM_NAME_HYPER_OS))
    }

    /**
     * 判断当前厂商系统是否为 ColorOs
     */
    fun isColorOs(): Boolean {
        for (property in VERSION_PROPERTY_OPPO) {
            val versionName = getPropertyName(property)
            if (TextUtils.isEmpty(versionName)) {
                continue
            }
            return true
        }
        return false
    }

    /**
     * 判断当前厂商系统是否为 OriginOS
     */
    fun isOriginOs(): Boolean {
        return !TextUtils.isEmpty(getPropertyName(VERSION_PROPERTY_VIVO))
    }

    /**
     * 判断当前厂商系统是否为 OneUI
     */
    @SuppressLint("PrivateApi")
    fun isOneUi(): Boolean {
        return isRightRom(getBrand(), getManufacturer(), *ROM_SAMSUNG)
        // 暂时无法通过下面的方式判断是否为 OneUI，只能通过品牌和机型来判断
        // https://stackoverflow.com/questions/60122037/how-can-i-detect-samsung-one-ui
//      try {
//         Field semPlatformIntField = Build.VERSION.class.getDeclaredField("SEM_PLATFORM_INT");
//         semPlatformIntField.setAccessible(true);
//         int semPlatformVersion = semPlatformIntField.getInt(null);
//         return semPlatformVersion >= 100000;
//      } catch (NoSuchFieldException  e) {
//         e.printStackTrace();
//         return false;
//      } catch (IllegalAccessException e) {
//         e.printStackTrace();
//         return false;
//      }
    }

    /**
     * 判断当前是否为鸿蒙系统
     */
    fun isHarmonyOs(): Boolean {
        // 鸿蒙系统没有 Android 10 以下的
        return if (!isAndroid10()) {
            false
        } else try {
            val buildExClass = Class.forName("com.huawei.system.BuildEx")
            val osBrand = buildExClass.getMethod("getOsBrand").invoke(buildExClass)
            "Harmony".equals(osBrand.toString(), ignoreCase = true)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            false
        }
    }

    /**
     * 判断当前是否为 MagicOs 系统（荣耀）
     */
    fun isMagicOs(): Boolean {
        return isRightRom(getBrand(), getManufacturer(), *ROM_HONOR)
    }

    /**
     * 判断 miui 优化开关（默认开启，关闭步骤为：开发者选项-> 启动 MIUI 优化 -> 点击关闭）
     * 需要注意的是，关闭 miui 优化后，可以跳转到小米定制的权限请求页面，但是开启权限仍然是没有效果的
     * 另外关于 miui 国际版开发者选项中是没有 miui 优化选项的，但是代码判断是有开启 miui 优化，也就是默认开启，这样是正确的
     * 相关 Github issue 地址：https://github.com/getActivity/XXPermissions/issues/38
     */
    @SuppressLint("PrivateApi")
    fun isMiuiOptimization(): Boolean {
        try {
            val clazz = Class.forName("android.os.SystemProperties")
            val getMethod = clazz.getMethod(
                "get",
                String::class.java,
                String::class.java
            )
            val ctsValue = getMethod.invoke(clazz, "ro.miui.cts", "").toString()
            val getBooleanMethod = clazz.getMethod(
                "getBoolean",
                String::class.java,
                Boolean::class.javaPrimitiveType
            )
            return java.lang.Boolean.parseBoolean(
                getBooleanMethod.invoke(
                    clazz, "persist.sys.miui_optimization",
                    "1" != ctsValue
                ).toString()
            )
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return true
    }

    /**
     * 返回厂商系统版本号
     */
    fun getRomVersionName(): String {
        val brand = getBrand()
        val manufacturer = getManufacturer()
        if (isRightRom(brand, manufacturer, *ROM_HUAWEI)) {
            val version = getPropertyName(VERSION_PROPERTY_HUAWEI)
            val temp = version.split("_".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            return if (temp.size > 1) {
                temp[1]
            } else {
                // 需要注意的是 华为畅享 5S Android 5.1 获取到的厂商版本号是 EmotionUI 3，而不是 3.1 或者 3.0 这种
                if (version.contains("EmotionUI")) {
                    version.replaceFirst("EmotionUI\\s*".toRegex(), "")
                } else version
            }
        }
        if (isRightRom(brand, manufacturer, *ROM_VIVO)) {
            // 需要注意的是 vivo iQOO 9 Pro Android 12 获取到的厂商版本号是 OriginOS Ocean
            return getSystemPropertyValue(VERSION_PROPERTY_VIVO)
        }
        if (isRightRom(brand, manufacturer, *ROM_XIAOMI)) {
            return getSystemPropertyValue(VERSION_PROPERTY_XIAOMI)
        }
        if (isRightRom(brand, manufacturer, *ROM_OPPO)) {
            for (property in VERSION_PROPERTY_OPPO) {
                val versionName = getSystemPropertyValue(property!!)
                if (TextUtils.isEmpty(property)) {
                    continue
                }
                return versionName
            }
            return ""
        }
        if (isRightRom(brand, manufacturer, *ROM_LEECO)) {
            return getSystemPropertyValue(VERSION_PROPERTY_LEECO)
        }

        if (isRightRom(brand, manufacturer, *ROM_360)) {
            return getSystemPropertyValue(VERSION_PROPERTY_360)
        }
        if (isRightRom(brand, manufacturer, *ROM_ZTE)) {
            return getSystemPropertyValue(VERSION_PROPERTY_ZTE)
        }
        if (isRightRom(brand, manufacturer, *ROM_ONEPLUS)) {
            return getSystemPropertyValue(VERSION_PROPERTY_ONEPLUS)
        }
        if (isRightRom(brand, manufacturer, *ROM_NUBIA)) {
            return getSystemPropertyValue(VERSION_PROPERTY_NUBIA)
        }
        if (isRightRom(brand, manufacturer, *ROM_HONOR)) {
            for (property in VERSION_PROPERTY_MAGIC) {
                val versionName = getSystemPropertyValue(property!!)
                if (TextUtils.isEmpty(property)) {
                    continue
                }
                return versionName
            }
            return ""
        }

        return getSystemPropertyValue("")
    }

    private fun isRightRom(brand: String, manufacturer: String, vararg names: String): Boolean {
        for (name in names) {
            if (brand.contains(name) || manufacturer.contains(name)) {
                return true
            }
        }
        return false
    }

    private fun getBrand(): String {
        return Build.BRAND.lowercase(Locale.getDefault())
    }

    private fun getManufacturer(): String {
        return Build.MANUFACTURER.lowercase(Locale.getDefault())
    }

    private fun getPropertyName(propertyName: String): String {
        var result = ""
        if (!TextUtils.isEmpty(propertyName)) {
            result = getSystemProperty(propertyName)
        }
        return result
    }

    private fun getSystemProperty(name: String): String {
        var prop = getSystemPropertyByShell(name)
        if (!TextUtils.isEmpty(prop)) {
            return prop
        }
        prop = getSystemPropertyByStream(name)
        if (!TextUtils.isEmpty(prop)) {
            return prop
        }
        return if (Build.VERSION.SDK_INT < 28) {
            getSystemPropertyByReflect(name)
        } else prop
    }

    private fun getSystemPropertyByShell(propName: String): String {
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $propName")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            val ret = input.readLine()
            if (ret != null) {
                return ret
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return ""
    }

    private fun getSystemPropertyByStream(key: String): String {
        try {
            val prop = Properties()
            val `is` = FileInputStream(
                File(Environment.getRootDirectory(), "build.prop")
            )
            prop.load(`is`)
            return prop.getProperty(key, "")
        } catch (e: FileNotFoundException) {
            // java.io.FileNotFoundException: /system/build.prop (Permission denied)
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }

    @SuppressLint("PrivateApi")
    private fun getSystemPropertyByReflect(key: String): String {
        try {
            val clz = Class.forName("android.os.SystemProperties")
            val getMethod = clz.getMethod(
                "get",
                String::class.java,
                String::class.java
            )
            return getMethod.invoke(clz, key, "") as String
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return ""
    }
}