package com.knight.kotlin.library_permiss.tools

import android.annotation.SuppressLint
import android.os.Build
import android.text.TextUtils
import com.knight.kotlin.library_permiss.tools.PermissionUtils.getSystemPropertyValue
import java.lang.reflect.InvocationTargetException
import java.util.Locale
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * Author:Knight
 * Time:2023/8/30 17:29
 * Description:PhoneRomUtils
 */
object PhoneRomUtils {
    private val ROM_HUAWEI = arrayOf("huawei")
    private val ROM_VIVO = arrayOf("vivo")
    private val ROM_XIAOMI = arrayOf("xiaomi")
    private val ROM_OPPO = arrayOf("oppo")
    private val ROM_LEECO = arrayOf("leeco", "letv")
    private val ROM_360 = arrayOf("360", "qiku")
    private val ROM_ZTE = arrayOf("zte")
    private val ROM_ONEPLUS = arrayOf("oneplus")
    private val ROM_NUBIA = arrayOf("nubia")
    private val ROM_SAMSUNG = arrayOf("samsung")
    private val ROM_HONOR = arrayOf("honor")
    private val ROM_SMARTISAN = arrayOf("smartisan")

    private const val ROM_NAME_MIUI = "ro.miui.ui.version.name"
    private const val ROM_NAME_HYPER_OS = "ro.mi.os.version.name"

    private const val VERSION_PROPERTY_HUAWEI = "ro.build.version.emui"
    private const val VERSION_PROPERTY_VIVO = "ro.vivo.os.build.display.id"

    //[ro.mi.os.version.incremental]: [OS1.0.7.0.UOQCNXM]
    //[ro.build.version.incremental]: [V13.0.12.0.SLCCNXM]

    //1. 澎湃 1.0 会返回 [ro.build.version.incremental]: [V816.0.7.0.UOQCNXM]
    //2. 澎湃 2.0 会返回 [ro.build.version.incremental]: [OS2.0.112.0.VNCCNXM]
    /**
     * 小米手机有两种 Os 系统，一种是澎湃，另外一种是 miui
     *
     * 系统为了兼容，澎湃有的属性，miui 肯定有，反过来就没有，所以这里要把澎湃的版本号属性放在首位
     *
     *
     *
     *
     * 切记不要拿 ro.build.version.incremental 属性来获取澎湃的系统版本，否则有问题，
     *
     *
     */
    private val VERSION_PROPERTY_XIAOMI = arrayOf("ro.mi.os.version.incremental", "ro.build.version.incremental")
    private val VERSION_PROPERTY_OPPO = arrayOf("ro.build.version.opporom", "ro.build.version.oplusrom.display")
    private const val VERSION_PROPERTY_LEECO = "ro.letv.release.version"
    private const val VERSION_PROPERTY_360 = "ro.build.uiversion"
    private const val VERSION_PROPERTY_ZTE = "ro.build.MiFavor_version"
    private const val VERSION_PROPERTY_ONEPLUS = "ro.rom.version"
    private const val VERSION_PROPERTY_NUBIA = "ro.build.rom.id"

    /**
     * 经过测试，得出以下结论
     * Magic 7.0 存放系统版本的属性是 msc.config.magic.version，
     * Magic 4.0 和 Magic 4.1 用的是 ro.build.version.magic 属性
     */
    private val VERSION_PROPERTY_MAGIC = arrayOf("msc.config.magic.version", "ro.build.version.magic")

    /**
     * 判断当前厂商系统是否为 emui
     */
    fun isEmui(): Boolean {
        return !TextUtils.isEmpty(getSystemPropertyValue(VERSION_PROPERTY_HUAWEI))
    }

    /**
     * 判断当前厂商系统是否为澎湃系统
     */
    fun isHyperOs(): Boolean {
        return !TextUtils.isEmpty(getSystemPropertyValue(ROM_NAME_HYPER_OS))
    }

    /**
     * 判断当前厂商系统是否为 miui
     */
    fun isMiui(): Boolean {
        if (isHyperOs()) {
            // 需要注意的是：该逻辑需要在判断 miui 系统之前判断，因为在 HyperOs 系统上面判断当前系统是否为 miui 系统也会返回 true
            // 这是因为 HyperOs 系统本身就是从 miui 系统演变而来，有这个问题也很正常，主要是厂商为了系统兼容性而保留的
            return false
        }
        return !TextUtils.isEmpty(getSystemPropertyValue(ROM_NAME_MIUI))
    }

    /**
     * 判断当前厂商系统是否为 ColorOs
     */
    fun isColorOs(): Boolean {
        for (property in VERSION_PROPERTY_OPPO) {
            val versionName = getSystemPropertyValue(property)
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
        return !TextUtils.isEmpty(getSystemPropertyValue(VERSION_PROPERTY_VIVO))
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
        if (!PermissionVersion.isAndroid10()) {
            return false
        }
        try {
            val buildExClass = Class.forName("com.huawei.system.BuildEx")
            val osBrand = buildExClass.getMethod("getOsBrand").invoke(buildExClass)
            return "Harmony".equals(osBrand.toString(), ignoreCase = true)
        } catch (ignore: ClassNotFoundException) {
            // 如果是类找不到的问题，就不打印日志，否则会影响看 Logcat 的体验
            // 相关 Github issue 地址：https://github.com/getActivity/XXPermissions/issues/368
            return false
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            return false
        }
    }

    /**
     * 判断当前是否为 MagicOs 系统（荣耀）
     */
    fun isMagicOs(): Boolean {
        return isRightRom(getBrand(), getManufacturer(), *ROM_HONOR)
    }

    /**
     * 判断当前是否为 SmartisanOS 系统（锤子手机的系统）
     */
    fun isSmartisanOS(): Boolean {
        return isRightRom(getBrand(), getManufacturer(), *ROM_SMARTISAN)
    }

    /**
     * 判断小米是否开启了系统优化（默认开启）
     *
     * Miui 关闭步骤为：开发者选项-> 启动 MIUI 优化 -> 点击关闭
     * 澎湃的关闭步骤为：开发者选项-> 启用系统优化 -> 点击关闭
     *
     * 需要注意的是，关闭优化后，可以跳转到小米定制的权限请求页面，但是开启权限仍然是没有效果的
     * 另外关于 miui 国际版开发者选项中是没有优化选项的，但是代码判断是有开启优化选项，也就是默认开启，这样是正确的
     * 相关 Github issue 地址：https://github.com/getActivity/XXPermissions/issues/38
     */
    @SuppressLint("PrivateApi")
    fun isXiaomiSystemOptimization(): Boolean {
        try {
            val clazz = Class.forName("android.os.SystemProperties")
            val getMethod = clazz.getMethod("get", String::class.java, String::class.java)
            val ctsValue = getMethod.invoke(clazz, "ro.miui.cts", "").toString()
            val getBooleanMethod = clazz.getMethod("getBoolean", String::class.java, Boolean::class.javaPrimitiveType)
            return getBooleanMethod.invoke(clazz, "persist.sys.miui_optimization", "1" != ctsValue).toString().toBoolean()
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
     * 获取厂商系统版本的大版本号
     *
     * @return               如果获取不到则返回 0
     */
    fun getRomBigVersionCode(): Int {
        val romVersionName = getRomVersionName() ?: return 0
        val array = romVersionName.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (array.size == 0) {
            return 0
        }
        try {
            return array[0].toInt()
        } catch (e: Exception) {
            // java.lang.NumberFormatException: Invalid int: "0 "
            e.printStackTrace()
            return 0
        }
    }

    /**
     * 返回经过美化的厂商系统版本号
     */
    
    fun getRomVersionName(): String? {
        val originalRomVersionName = getOriginalRomVersionName()

        if (TextUtils.isEmpty(originalRomVersionName)) {
            return null
        }

        // 使用正则表达式匹配数字和点号组成的版本号
        val pattern: Pattern = Pattern.compile("(\\d+(?:\\.\\d+)+)")
        val matcher: Matcher = pattern.matcher(originalRomVersionName)

        if (matcher.find()) {
            return matcher.group(1)
        }

        return null
    }

    /**
     * 返回原始的厂商系统版本号
     */
    
    fun getOriginalRomVersionName(): String {
        val brand = getBrand()
        val manufacturer = getManufacturer()
        if (isRightRom(brand, manufacturer, *ROM_HUAWEI)) {
            val version = getSystemPropertyValue(VERSION_PROPERTY_HUAWEI)
            val temp = version.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (temp.size > 1) {
                return temp[1]
            } else {
                // 需要注意的是 华为畅享 5S Android 5.1 获取到的厂商版本号是 EmotionUI 3，而不是 3.1 或者 3.0 这种
                if (version.contains("EmotionUI")) {
                    return version.replaceFirst("EmotionUI\\s*".toRegex(), "")
                }
                return version
            }
        }
        if (isRightRom(brand, manufacturer, *ROM_VIVO)) {
            // 需要注意的是 vivo iQOO 9 Pro Android 12 获取到的厂商版本号是 OriginOS Ocean
            return getSystemPropertyValue(VERSION_PROPERTY_VIVO)
        }
        if (isRightRom(brand, manufacturer, *ROM_XIAOMI)) {
            for (property in VERSION_PROPERTY_XIAOMI) {
                val versionName = getSystemPropertyValue(property)
                if (TextUtils.isEmpty(property)) {
                    continue
                }
                return versionName
            }
            return ""
        }
        if (isRightRom(brand, manufacturer, *ROM_OPPO)) {
            for (property in VERSION_PROPERTY_OPPO) {
                val versionName = getSystemPropertyValue(property)
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
                val versionName = getSystemPropertyValue(property)
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
}