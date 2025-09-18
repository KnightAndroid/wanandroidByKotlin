package com.core.library_devicecompat

import android.os.Build
import android.text.TextUtils
import java.util.Locale


/**
 * @author created by luguian
 * @organize
 * @Date 2025/9/18 9:43
 * @descript: 厂商品牌判断
 */
object DeviceBrand {
    private const val BRAND_NAME_HUAWEI = "HuaWei"
    private val BRAND_ID_LOWER_CASE_HUAWEI = arrayOf("huawei")

    private const val BRAND_NAME_VIVO = "Vivo"
    private val BRAND_ID_LOWER_CASE_VIVO = arrayOf("vivo")

    private const val BRAND_NAME_XIAOMI = "XiaoMi"
    private val BRAND_ID_LOWER_CASE_XIAOMI = arrayOf("xiaomi")

    private const val BRAND_NAME_REDMI = "RedMi"
    private val BRAND_ID_LOWER_CASE_REDMI = arrayOf("redmi")

    private const val BRAND_NAME_OPPO = "Oppo"
    private val BRAND_ID_LOWER_CASE_OPPO = arrayOf("oppo")

    private const val BRAND_NAME_REALME = "RealMe"
    private val BRAND_ID_LOWER_CASE_REALME = arrayOf("realme")

    private const val BRAND_NAME_LEECO = "LeEco"
    private val BRAND_ID_LOWER_CASE_LEECO = arrayOf("leeco", "letv")

    private const val BRAND_NAME_360 = "360"
    private val BRAND_ID_LOWER_CASE_360 = arrayOf("360", "qiku")

    private const val BRAND_NAME_ZTE = "ZTE"
    private val BRAND_ID_LOWER_CASE_ZTE = arrayOf("zte")

    private const val BRAND_NAME_ONEPLUS = "OnePlus"
    private val BRAND_ID_LOWER_CASE_ONEPLUS = arrayOf("oneplus")

    private const val BRAND_NAME_NUBIA = "Nubia"
    private val BRAND_ID_LOWER_CASE_NUBIA = arrayOf("nubia")

    private const val BRAND_NAME_SAMSUNG = "Samsung"
    private val BRAND_ID_LOWER_CASE_SAMSUNG = arrayOf("samsung")

    private const val BRAND_NAME_HONOR = "Honor"
    private val BRAND_ID_LOWER_CASE_HONOR = arrayOf("honor")

    private const val BRAND_NAME_SMARTISAN = "Smartisan"
    private val BRAND_ID_LOWER_CASE_SMARTISAN = arrayOf("smartisan", "deltainno")

    private const val BRAND_NAME_COOLPAD = "CoolPad"
    private val BRAND_ID_LOWER_CASE_COOLPAD = arrayOf("coolpad", "yulong", "cp")

    private const val BRAND_NAME_LG = "LG"
    private val BRAND_ID_LOWER_CASE_LG = arrayOf("lg", "lge")

    private const val BRAND_NAME_GOOGLE = "Google"
    private val BRAND_ID_LOWER_CASE_GOOGLE = arrayOf("google")

    private const val BRAND_NAME_MEIZU = "MeiZu"
    private val BRAND_ID_LOWER_CASE_MEIZU = arrayOf("meizu")

    private const val BRAND_NAME_LENOVO = "Lenovo"
    private val BRAND_ID_LOWER_CASE_LENOVO = arrayOf("lenovo", "zuk")

    private const val BRAND_NAME_HTC = "HTC"
    private val BRAND_ID_LOWER_CASE_HTC = arrayOf("htc")

    private const val BRAND_NAME_SONY = "Sony"
    private val BRAND_ID_LOWER_CASE_SONY = arrayOf("sony")

    private const val BRAND_NAME_GIONEE = "Gionee"
    private val BRAND_ID_LOWER_CASE_GIONEE = arrayOf("gionee", "amigo")

    private const val BRAND_NAME_MOTOROLA = "Motorola"
    private val BRAND_ID_LOWER_CASE_MOTOROLA = arrayOf("motorola")

    private const val BRAND_NAME_ASUS = "Asus"
    private val BRAND_ID_LOWER_CASE_ASUS = arrayOf("asus")

    private const val BRAND_NAME_TRANSSION = "Transsion"
    private val BRAND_ID_LOWER_CASE_TRANSSION = arrayOf("infinix mobility limited", "itel", "tecno")

    private const val BRAND_NAME_DOOV = "DOOV"
    private val BRAND_ID_LOWER_CASE_DOOV = arrayOf("DOOV")

    private const val BRAND_NAME_PHILIPS = "Philips"
    private val BRAND_ID_LOWER_CASE_PHILIPS = arrayOf("philips")

    private const val BRAND_NAME_BLACKSHARK = "BlackShark"
    private val BRAND_ID_LOWER_CASE_BLACKSHARK = arrayOf("blackshark")

    private const val BRAND_NAME_HISENSE = "Hisense"
    private val BRAND_ID_LOWER_CASE_HISENSE = arrayOf("hisense")

    private const val BRAND_NAME_KTOUCH = "K-Touch"
    private val BRAND_ID_LOWER_CASE_KTOUCH = arrayOf("k-touch", "ktouch")

    private const val BRAND_NAME_MEITU = "MeiTu"
    private val BRAND_ID_LOWER_CASE_MEITU = arrayOf("meitu")

    private const val BRAND_NAME_NOKIA = "Nokia"
    private val BRAND_ID_LOWER_CASE_NOKIA = arrayOf("nokia")

    private var CURRENT_BRAND_NAME: String? = null

    init {
        val brand = Build.BRAND.lowercase(Locale.getDefault())
        val manufacturer = Build.MANUFACTURER.lowercase(Locale.getDefault())

        if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_HUAWEI)) {
            CURRENT_BRAND_NAME = BRAND_NAME_HUAWEI
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_VIVO)) {
            CURRENT_BRAND_NAME = BRAND_NAME_VIVO
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_XIAOMI)) {
            CURRENT_BRAND_NAME = BRAND_NAME_XIAOMI
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_REDMI)) {
            CURRENT_BRAND_NAME = BRAND_NAME_REDMI
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_OPPO)) {
            CURRENT_BRAND_NAME = BRAND_NAME_OPPO
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_REALME)) {
            CURRENT_BRAND_NAME = BRAND_NAME_REALME
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_LEECO)) {
            CURRENT_BRAND_NAME = BRAND_NAME_LEECO
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_360)) {
            CURRENT_BRAND_NAME = BRAND_NAME_360
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_ZTE)) {
            CURRENT_BRAND_NAME = BRAND_NAME_ZTE
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_ONEPLUS)) {
            CURRENT_BRAND_NAME = BRAND_NAME_ONEPLUS
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_NUBIA)) {
            CURRENT_BRAND_NAME = BRAND_NAME_NUBIA
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_SAMSUNG)) {
            CURRENT_BRAND_NAME = BRAND_NAME_SAMSUNG
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_HONOR)) {
            CURRENT_BRAND_NAME = BRAND_NAME_HONOR
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_SMARTISAN)) {
            CURRENT_BRAND_NAME = BRAND_NAME_SMARTISAN
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_COOLPAD)) {
            CURRENT_BRAND_NAME = BRAND_NAME_COOLPAD
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_LG)) {
            CURRENT_BRAND_NAME = BRAND_NAME_LG
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_GOOGLE)) {
            CURRENT_BRAND_NAME = BRAND_NAME_GOOGLE
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_MEIZU)) {
            CURRENT_BRAND_NAME = BRAND_NAME_MEIZU
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_LENOVO)) {
            CURRENT_BRAND_NAME = BRAND_NAME_LENOVO
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_HTC)) {
            CURRENT_BRAND_NAME = BRAND_NAME_HTC
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_SONY)) {
            CURRENT_BRAND_NAME = BRAND_NAME_SONY
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_GIONEE)) {
            CURRENT_BRAND_NAME = BRAND_NAME_GIONEE
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_MOTOROLA)) {
            CURRENT_BRAND_NAME = BRAND_NAME_MOTOROLA
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_ASUS)) {
            CURRENT_BRAND_NAME = BRAND_NAME_ASUS
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_TRANSSION)) {
            CURRENT_BRAND_NAME = BRAND_NAME_TRANSSION
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_DOOV)) {
            CURRENT_BRAND_NAME = BRAND_NAME_DOOV
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_PHILIPS)) {
            CURRENT_BRAND_NAME = BRAND_NAME_PHILIPS
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_BLACKSHARK)) {
            CURRENT_BRAND_NAME = BRAND_NAME_BLACKSHARK
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_HISENSE)) {
            CURRENT_BRAND_NAME = BRAND_NAME_HISENSE
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_KTOUCH)) {
            CURRENT_BRAND_NAME = BRAND_NAME_KTOUCH
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_MEITU)) {
            CURRENT_BRAND_NAME = BRAND_NAME_MEITU
        } else if (compareBrand(brand, manufacturer, *BRAND_ID_LOWER_CASE_NOKIA)) {
            CURRENT_BRAND_NAME = BRAND_NAME_NOKIA
        } else {
            if (!TextUtils.isEmpty(brand)) {
                CURRENT_BRAND_NAME = brand
            } else if (!TextUtils.isEmpty(manufacturer)) {
                CURRENT_BRAND_NAME = manufacturer
            } else {
                CURRENT_BRAND_NAME = "Unknown"
            }
        }
    }

    /**
     * 判断当前设备的品牌是否为华为
     */
    fun isHuaWei(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_HUAWEI)
    }

    /**
     * 判断当前设备的品牌是否为荣耀
     */
    fun isHonor(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_HONOR)
    }

    /**
     * 判断当前设备的品牌是否为 vivo
     */
    fun isVivo(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_VIVO)
    }

    /**
     * 判断当前设备的品牌是否为小米
     */
    fun isXiaoMi(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_XIAOMI)
    }

    /**
     * 判断当前设备的品牌是否为红米
     */
    fun isRedMi(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_REDMI)
    }

    /**
     * 判断当前设备的品牌是否为 oppo
     */
    fun isOppo(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_OPPO)
    }

    /**
     * 判断当前设备的品牌是否为真我
     */
    fun isRealMe(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_REALME)
    }

    /**
     * 判断当前设备的品牌是否为乐视
     */
    fun isLeEco(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_LEECO)
    }

    /**
     * 判断当前设备的品牌是否为 360
     */
    fun is360(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_360)
    }

    /**
     * 判断当前设备的品牌是否为中兴
     */
    fun isZte(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_ZTE)
    }

    /**
     * 判断当前设备的品牌是否为一加
     */
    fun isOnePlus(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_ONEPLUS)
    }

    /**
     * 判断当前设备的品牌是否为努比亚
     */
    fun isNubia(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_NUBIA)
    }

    /**
     * 判断当前设备的品牌是否为酷派
     */
    fun isCoolPad(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_COOLPAD)
    }

    /**
     * 判断当前设备的品牌是否为 LG
     */
    fun isLg(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_LG)
    }

    /**
     * 判断当前设备的品牌是否为 Google
     */
    fun isGoogle(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_GOOGLE)
    }

    /**
     * 判断当前设备的品牌是否为三星
     */
    fun isSamsung(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_SAMSUNG)
    }

    /**
     * 判断当前设备的品牌是否为魅族
     */
    fun isMeiZu(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_MEIZU)
    }

    /**
     * 判断当前设备的品牌是否为联想
     */
    fun isLenovo(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_LENOVO)
    }

    /**
     * 判断当前设备的品牌是否为锤子
     */
    fun isSmartisan(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_SMARTISAN)
    }

    /**
     * 判断当前设备的品牌是否为 HTC
     */
    fun isHtc(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_HTC)
    }

    /**
     * 判断当前设备的品牌是否为索尼
     */
    fun isSony(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_SONY)
    }

    /**
     * 判断当前设备的品牌是否为金立
     */
    fun isGionee(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_GIONEE)
    }

    /**
     * 判断当前设备的品牌是否为摩托罗拉
     */
    fun isMotorola(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_MOTOROLA)
    }

    /**
     * 判断当前设备的品牌是否为传音
     */
    fun isTranssion(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_TRANSSION)
    }

    /**
     * 判断当前设备的品牌是否为朵唯
     */
    fun isDoov(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_DOOV)
    }

    /**
     * 判断当前设备的品牌是否为飞利浦
     */
    fun isPhilips(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_PHILIPS)
    }

    /**
     * 判断当前设备的品牌是否为黑鲨
     */
    fun isBlackShark(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_BLACKSHARK)
    }

    /**
     * 判断当前设备的品牌是否为海信
     */
    fun isHisense(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_HISENSE)
    }

    /**
     * 判断当前设备的品牌是否为天语
     */
    fun isKTouch(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_KTOUCH)
    }


    /**
     * 判断当前设备的品牌是否为美图
     */
    fun isMeiTu(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_MEITU)
    }

    /**
     * 判断当前设备的品牌是否为诺基亚
     */
    fun isNokia(): Boolean {
        return TextUtils.equals(CURRENT_BRAND_NAME, BRAND_NAME_NOKIA)
    }

    /**
     * 获取当前设备品牌的名称
     */
    fun getBrandName(): String {
        return CURRENT_BRAND_NAME!!
    }

    /**
     * 比较品牌或者制造商名称是否包含指定的名称
     */
    private fun compareBrand(brand: String, manufacturer: String, vararg names: String): Boolean {
        for (name in names) {
            if (brand.contains(name) || manufacturer.contains(name)) {
                return true
            }
        }
        return false
    }
}