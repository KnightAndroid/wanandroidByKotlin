package com.core.library_devicecompat

import android.annotation.SuppressLint
import android.os.Build
import android.text.TextUtils
import java.lang.reflect.Field
import java.util.Locale
import java.util.regex.Matcher
import java.util.regex.Pattern;


/**
 * @author created by luguian
 * @organize
 * @Date 2025/9/18 9:47
 * @descript:厂商系统判断
 */
object DeviceOs {
    /* ---------------------------------------- 我是一条华丽的分割线 ---------------------------------------- */
    const val REGEX_VERSION_NAME: String = "\\d+(?:\\.\\d+)+"

    const val REGEX_NUMBER: String = "\\d+"

    /* ---------------------------------------- 我是一条华丽的分割线 ---------------------------------------- */
    const val SYSTEM_PROPERTY_BUILD_VERSION_INCREMENTAL: String = "ro.build.version.incremental"
    const val SYSTEM_PROPERTY_BUILD_DISPLAY_ID: String = "ro.build.display.id"

    /* ---------------------------------------- 我是一条华丽的分割线 ---------------------------------------- */
    val OS_VERSION_NAME_UNKNOWN: Array<String> = arrayOf(
        SYSTEM_PROPERTY_BUILD_DISPLAY_ID,
        SYSTEM_PROPERTY_BUILD_VERSION_INCREMENTAL
    )

    /* ---------------------------------------- 下面是小米或者红米的系统 ---------------------------------------- */
    /**
     * 国内版本：
     * [ro.miui.build.region]: [cn]
     * [ro.miui.region]: [CN]
     * [ro.vendor.miui.region]: [CN]
     *
     * 国际版本：
     * [ro.miui.build.region]: [global]
     * [ro.miui.region]: [HK]
     * [ro.vendor.miui.region]: [HK]
     */
    val OS_REGION_MI: Array<String> = arrayOf(
        "ro.miui.build.region",
        "ro.miui.region",
        "ro.vendor.miui.region"
    )

    const val OS_NAME_HYPER_OS: String = "HyperOS"

    //[ro.mi.os.version.incremental]  [OS1.0.3.0.TKXCNXM]
    const val OS_VERSION_NAME_HYPER_OS: String = "ro.mi.os.version.incremental"

 
     // [ro.mi.os.version.incremental]: [OS1.0.3.0.TKXCNXM]
     // [ro.mi.os.version.name]: [OS1.0]
     // [ro.mi.os.version.code]: [1]
     
    val OS_CONDITIONS_HYPER_OS: Array<String> = arrayOf(
        "ro.mi.os.version.name",
        "ro.mi.os.version.code",
        OS_VERSION_NAME_HYPER_OS
    )

    val OS_REGION_HYPER_OS: Array<String> = OS_REGION_MI

    const val OS_NAME_MIUI: String = "MIUI"

 
     // [ro.build.version.incremental]: [V9.6.1.0.MHOCNFD]
     // [ro.build.description]: [kenzo-user 6.0.1 MMB29M V9.6.1.0.MHOCNFD release-keys]
     // [ro.build.fingerprint]: [Xiaomi/kenzo/kenzo:6.0.1/MMB29M| V9.6.1.0.MH0cNFD:user/release-keys]
     // [ro.bootimage.build.fingerprint]: [Xiaomi/kenzo/kenzo:6.0.1/MMB29M/ V9.6.1.0.MHOCNFD:user/release-keys]
     
    const val OS_VERSION_NAME_MIUI: String = SYSTEM_PROPERTY_BUILD_VERSION_INCREMENTAL

    /**
     * miui 9.6.1.0：[ro.miui.ui.version.name]: [V9]
     * miui 13.0.12：[ro.miui.ui.version.name]: [V130]
     *
     * miui 9.6.1：[ro.miui.ui.version.code]: [7]
     * miui 13.0.12：[ro.miui.ui.version.code]: [13]
     *
     * 如何识别小米设备/MIUI系统：https://dev.mi.com/console/doc/detail?pId=915
     */
    val OS_CONDITIONS_MIUI: Array<String> = arrayOf(
        "ro.miui.ui.version.name",
        "ro.miui.ui.version.code"
    )

    /**
     * 国内版本：
     * [ro.miui.region]: [CN]
     * [ro.vendor.miui.region]: [CN]
     * [ro.miui.build.region]: [cn]
     *
     * 国际版本：
     * [ro.miui.region]: [HK]
     * [ro.vendor.miui.region]: [HK]
     * [ro.miui.build.region]: [global]
     */
    val OS_REGION_MIUI: Array<String> = OS_REGION_MI

    /* ---------------------------------------- 下面是真我、OPPO 的系统 ---------------------------------------- */
    const val OS_NAME_REALME_UI: String = "realmeUI"

    
    //  [ro.build.version.realmeui]: [V5.0]
     
    const val OS_VERSION_NAME_REALME_UI: String = "ro.build.version.realmeui"

    
      //[ro.build.version.oplusrom]: [V13.1.0]
     // [ro.build.version.oplusrom.confidential]: [V13.1.0]
      //[ro.build.version.oplusrom.display]: [13.1]
     
    const val OS_NAME_COLOR_OS: String = "ColorOS"
    val OS_VERSION_NAME_COLOR_OS: Array<String> = arrayOf(
        "ro.build.version.opporom",
        "ro.build.version.oplusrom.confidential",
        "ro.build.version.oplusrom.display"
    )

    /* ---------------------------------------- 下面是 VIVO 的系统 ---------------------------------------- */
    /**
     * [ro.vivo.os.build.display.id]: [OriginOS 4]
     * [ro.vivo.os.build.display.id]: [OriginOS 5]
     * [ro.vivo.os.build.display.id]: [Funtouch 0S_2.5]
     */
    const val OS_CONDITIONS_VIVO_OS: String = "ro.vivo.os.build.display.id"

    const val OS_NAME_ORIGIN_OS: String = "OriginOS"

   
     // [ro.vivo.product.version]: [PD2359C_A_15.1.19.20.W10.V000L1]
    //  [ro.vivo.product.version.incremental]: [15.1.19.20.W10.V000L1]
    //  [ro.vivo.build.version.incremental]: [15.1.19.20.W10]
     // [ro.vivo.build.version]: [PD2359C_A_15.1.19.20.W10]
    //  [ro.vivo.default.version]: [PD2309_A_15.1.19.20.W10.V000L1]
    // [ro.build.display.id]: [PD2309_A_15.1.19.20.W10.V000L1]
    //  [ro.vivo.system.product.version]: [PD2309_A_15.1.19.20.W10]
   //   [ro.build.software.version]: [PD2359C_A_15.1.19.20.W10]
     
    val OS_VERSION_NAME_ORIGIN_OS: Array<String> = arrayOf(
        "ro.vivo.product.version",
        "ro.vivo.product.version.incremental",
        "ro.vivo.build.version.incremental",
        "ro.vivo.build.version",
        "ro.vivo.default.version",
        SYSTEM_PROPERTY_BUILD_DISPLAY_ID,
        "ro.vivo.system.product.version",
        "ro.build.software.version"
    )

    const val OS_NAME_FUNTOUCH_OS: String = "FuntouchOS"
    // [ro.vivo.os.name]: [Funtouch]
    // 不要用 ro.vivo.os.name 属性判断是否为 FuntouchOS 系统，因为在 FuntouchOS 和 OriginOs 系统上面获取到的值是 Funtouch
    // static final String OS_CONDITIONS_FUNTOUCH_OS = "ro.vivo.os.name";
    
     // [ro.vivo.os.version]: [2.5]
     // [ro.vivo.rom.version]: [rom_2.5]
     // [ro.vivo.rom]: [rom_ 2.5]
     // [ro.vivo.os.build.display.id]: [Funtouch OS_2.5]
     
    val OS_VERSION_NAME_FUNTOUCH_OS: Array<String> = arrayOf(
        "ro.vivo.os.version",
        "ro.vivo.rom.version",
        "ro.vivo.rom",
        OS_CONDITIONS_VIVO_OS
    )

    /* ---------------------------------------- 下面是华为或者荣耀的系统 ---------------------------------------- */
    const val OS_NAME_MAGIC_OS: String = "MagicOS"

    /**
     * 经过测试，得出以下结论
     * MagicOS 7.0 存放系统版本的属性是 msc.config.magic.version，
     * MagicOS 4.0 和 Magic 4.1 用的是 ro.build.version.magic 属性
     */
    val OS_VERSION_NAME_MAGIC_OS: Array<String> = arrayOf(
        "msc.config.magic.version",
        "ro.build.version.magic"
    )

    const val OS_NAME_HARMONY_OS: String = "HarmonyOS"

    /**
     * [ro.huawei.build.display.id]: [NOH-AN00 2.0.0.165(C00E160R6P2)]
     * [ro.build.display.id]: [NOH-AN00 2.0.0.165(C00E160R6P2)]
     * [hwouc.hwpatch.version]: [2.0.0.165(C00E160R6P2patch04)]
     * [persist.mygote.build.id]: [NOH-AN00 2.0.0.165(C00E160R6P2)]
     * [persist.sys.hiview.base_version]: [NOH-LGRP1-CHN 2.0.0.165]
     * [ro.comp.hl.product_base_version]: [NOH-LGRP1-CHN 2.0.0.165]
     */
    val OS_VERSION_NAME_HARMONY_OS: Array<String> = arrayOf(
        "hw_sc.build.platform.version",
        "ro.huawei.build.display.id",
        "hwouc.hwpatch.version",
        "persist.mygote.build.id",
        "persist.sys.hiview.base_version",
        "ro.comp.hl.product_base_version"
    )

    /**
     * 实测下面的属性在 HarmonyOS 2.0、3.0、4.0、4.2 上面都存在，但是在 4.3 上面不存在
     * [ro.build.ohos.devicetype]: [phone]
     * [ro.build.ohos.devicetype]: [tablet]
     * [persist.sys.ohos.osd.cloud.switch]: [true]
     */
    // static final String[] OS_CONDITIONS_HARMONY_OS = { "ro.build.ohos.devicetype",
    //                                                   "persist.sys.ohos.osd.cloud.switch" };
    const val OS_NAME_EMUI: String = "EMUI"

    
    // [ro.build.version.emui]: [EmotionUI_8.0.0]
     //[ro.build.version.emui]: [EmotionUI_9.1.0]
    // [ro.build.version.emui]: [EmotionUI_9.1.1]
    // [ro.build.version.emui]: [EmotionUI_10.1.1]
    //  [ro.build.version.emui]: [EmotionUI_11.1.0]
    //  [ro.build.version.emui]: [EmotionUI_13.0.0]
   //   [ro.build.version.emui]: [EmotionUI_14.0.0]
   //   [ro.build.version.emui]: [EmotionUI_14.2.0]
     
    const val OS_VERSION_NAME_EMUI: String = "ro.build.version.emui"

    /* ---------------------------------------- 下面是三星的系统 ---------------------------------------- */
    const val OS_NAME_ONE_UI: String = "OneUI"

    /**
     * OneUI 高版本
     * OneUI 8.0：[ro.build.version.oneui]: [80000]
     * OneUI 7.0： [ro.build.version.oneui]: [70000]
     * OneUI 6.1：[ro.build.version.oneui]: [60101]
     * OneUI 5.1.1：[ro.build.version.oneui]: [50101]
     */
    const val OS_VERSION_NAME_ONE_UI: String = "ro.build.version.oneui"

    /* ---------------------------------------- 下面是一加的系统 ---------------------------------------- */
    const val OS_NAME_OXYGEN_OS: String = "OxygenOS"

    /**
     * [ro.oxygen.version]: [9.0.4]
     */
    const val OS_VERSION_NAME_OXYGEN_OS: String = "ro.oxygen.version"

    const val OS_NAME_H2_OS: String = "H2OS"

    /**
     * Android 7.1.1：[ro.rom.version]: [H2OS V3.5]
     * Android 9.0：[ro.rom.version]: [9.0.11]
     * Android 11：[ro.rom.version]: [11.1.2.2]
     */
    const val OS_VERSION_NAME_H2_OS: String = "ro.rom.version"

    /* ---------------------------------------- 下面是魅族的系统 ---------------------------------------- */
    const val OS_NAME_FLYME: String = "Flyme"

    /**
     * ro.flyme.version.id 属性：
     * Android 5.1 返回：[ro.build.display.id]: [Flyme OS 5.1.3.0A]
     * Android 9 返回：[ro.flyme.version.id]: [Flyme 8.1.8.0A]
     * Android 11 返回：[ro.flyme.version.id]: [Flyme 9.3.1.0A]
     */ 
     //Android 13 返回：[ro.flyme.version.id]: [Flyme 10.5.0.1A]
     //Android 15 返回：[ro.flyme.version.id]: [AQ3A.250129.001_]（错误）
     /*
     * ro.build.display.id 属性：
     * Android 9 返回：[ro.build.display.id]: [Flyme 8.1.8.0A]
     * Android 13 返回：[ro.build.display.id]: [Flyme 10.5.0.1A]
     * Android 15 返回：[ro.build.display.id]: [Flyme 12.1.0.0A]
     *
     * 结论：优先用 ro.flyme.version.id 属性获取系统版本号，获取到的值必须包含 Flyme 字眼，否则再用 ro.build.display.id 属性获取
     */
    const val OS_VERSION_NAME_FLYME_1: String = "ro.flyme.version.id"
    const val OS_VERSION_NAME_FLYME_2: String = SYSTEM_PROPERTY_BUILD_DISPLAY_ID


    /**
     * [ro.flyme.published]: [true]
     * [ro.flyme.version.id]: [Flyme 9.3.1.0A]
     */
    val OS_CONDITIONS_FLYME: Array<String> = arrayOf(
        "ro.flyme.published",
        OS_VERSION_NAME_FLYME_1
    )

    /* ---------------------------------------- 下面是中兴或者努比亚的系统 ---------------------------------------- */
    /**
     * MyOS 系统返回：[ro.build.MiFavor_version]: [12]
     * MiFavor 系统返回：[ro.build.MiFavor_version]: [10.1]
     */
    const val OS_VERSION_ZTE_OS: String = "ro.build.MiFavor_version"

    /**
     * [ro.vendor.mifavor.custom]: [home]
     * [ro.vendor.mifavor.mfvkeyguard.type]: [2]
     * [ro.vendor.mifavor.voicetotext]: [1]
     */
    val OS_CONDITIONS_ZTE_OS: Array<String> = arrayOf(
        OS_VERSION_ZTE_OS,
        "ro.vendor.mifavor.custom",
        "ro.vendor.mifavor.mfvkeyguard.type",
        "ro.vendor.mifavor.voicetotext"
    )

 
     // RedMagicOS 返回：[ro.build.display.id]: [RedMagicOS10.0.12]
     // MyOS 返回：[ro.build.display.id]: [MyOS12.0.14_A2121]
     // MifavorUI 返回：注意不能用 [ro.build.display.id]: [ZTE_A2021_PROV1.0.2B05]（错误），应该用 ro.build.MiFavor_version
     
    const val OS_VERSION_NAME_ZTE_OS: String = SYSTEM_PROPERTY_BUILD_DISPLAY_ID

    const val OS_NAME_RED_MAGIC_OS: String = "RedMagicOS"

    const val OS_NAME_MY_OS: String = "MyOS"

    const val OS_NAME_MIFAVOR_UI: String = "MifavorUI"

    /**
     * [ro.build.MiFavor_version]: [10.1]
     * [ro.build.MiFavor_version]: [4.0]
     */
    const val OS_VERSION_NAME_MIFAVOR_UI: String = OS_VERSION_ZTE_OS

    /* ---------------------------------------- 下面是锤子的系统 ---------------------------------------- */
    const val OS_NAME_SMARTISAN_OS: String = "SmartisanOS"
    const val OS_VERSION_NAME_SMARTISAN_OS: String = "ro.smartisan.version"
    val OS_CONDITIONS_SMARTISAN_OS: Array<String> = arrayOf(
        "ro.smartisan.sa",
        OS_VERSION_NAME_SMARTISAN_OS
    )

    /* ---------------------------------------- 下面是乐视的系统 ---------------------------------------- */
    const val OS_NAME_EUI_OS: String = "EUI"

    /**
     * [ro.letv.release.version]: [6.0.030S]
     */
    const val OS_VERSION_NAME_EUI_OS: String = "ro.letv.release.version"

    /**
     * [ro.letv.release.version_date]: [5.8.001D_09093]
     * [ro.product.letv_model]: [Le X620]
     * [ro.product.letv_name]: [乐2]
     * [sys.letv.fmodelaid]: [10120]
     * [persist.sys.leui.bootreason]: [0]
     * [ro.config.leui_ringtone_slot2]: [Default.ogg]
     * [ro.leui_oem_unlock_enable]: [1]
     */
    val OS_CONDITIONS_EUI_OS: Array<String> = arrayOf(
        OS_VERSION_NAME_EUI_OS,
        "ro.letv.release.version_date",
        "ro.product.letv_model",
        "ro.product.letv_name",
        "sys.letv.fmodelaid",
        "persist.sys.leui.bootreason",
        "ro.config.leui_ringtone_slot2",
        "ro.leui_oem_unlock_enable"
    )

    /* ---------------------------------------- 下面是联想、摩托罗拉的系统 ---------------------------------------- */
    const val OS_NAME_ZUX_OS: String = "ZUXOS"

    
     // [ro.config.lgsi.fp.incremental]: [ZUXOS_1.1.350_250418_PRC]
     // [ro.config.lgsi.os.version]: [1.1]
     
    val OS_VERSION_NAME_ZUX_OS: Array<String> = arrayOf(
        "ro.config.lgsi.fp.incremental",
        "ro.config.lgsi.os.version"
    )

    /**
     * [ro.config.lgsi.os.name]: [ZUXOS]
     */
    const val OS_CONDITIONS_ZUX_OS: String = "ro.config.lgsi.os.name"


    const val OS_NAME_ZUI: String = "ZUI"

    /**
     * [ro.com.zui.version]: [3.5]
     */
    const val OS_VERSION_NAME_ZUI: String = "ro.com.zui.version"

    /**
     * [ro.zui.version.status]: [ST]
     * [ro.zui.hardware.displayid]: [H201]
     * [persist.radio.zui.feature]: [true]
     * [ro.config.zuisdk.enabled]: [true]
     */
    val OS_CONDITIONS_ZUI: Array<String> = arrayOf(
        OS_VERSION_NAME_ZUI,
        "ro.zui.version.status",
        "ro.zui.hardware.displayid",
        "persist.radio.zui.feature",
        "ro.config.zuisdk.enabled"
    )

    /* ---------------------------------------- 下面是努比亚的老系统 ---------------------------------------- */
    const val OS_NAME_NUBIA_UI: String = "nubiaUI"

    
    //  [ro.build.nubia.rom.code]: [V1.0]
    //  [ro.build.nubia.rom.code]: [V1.6]
    //  [ro.build.nubia.rom.code]: [V2.0]
    // [ro.build.nubia.rom.code]: [V3.0]
    // [ro.build.nubia.rom.code]: [V3.7]
    //  [ro.build.nubia.rom.code]: [V4.0]
    //  [ro.build.nubia.rom.code]: [V6.0]
     
    const val OS_VERSION_NAME_NUBIA_UI: String = "ro.build.nubia.rom.code"

    /**
     * [ro.build.nubia.rom.name]: [nubiaUI]
     */
    const val OS_CONDITIONS_NUBIA_UI: String = "ro.build.nubia.rom.name"

    /* ---------------------------------------- 下面是 360 的系统 ---------------------------------------- */
    const val OS_NAME_360_UI: String = "360UI"

    /**
     * Android 8.0：[ro.build.uiversion]: [360UI:V3.0]
     */
    const val OS_VERSION_NAME_360_UI: String = "ro.build.uiversion"

    
    private var sCurrentOsName: String? = null

    
    private var sCurrentOriginalOsVersionName: String? = null

    
    private var sCurrentBeautificationVersionName: String? = null

    init {
        // 需要注意的是：该逻辑需要在判断 MIUI 系统之前判断，因为在 HyperOS 系统上面判断当前设备的厂商系统是否为 MIUI 系统也会返回 true
        // 这是因为 HyperOS 系统本身就是从 MIUI 系统演变而来，有这个问题也很正常，主要是厂商为了系统兼容性而保留的
        if (SystemPropertyCompat.isSystemPropertyAnyOneExist(OS_CONDITIONS_HYPER_OS)) {
            sCurrentOsName = OS_NAME_HYPER_OS
            sCurrentOriginalOsVersionName = SystemPropertyCompat.getSystemPropertyValue(OS_VERSION_NAME_HYPER_OS)
            sCurrentBeautificationVersionName = extractVersionNameByText(sCurrentOriginalOsVersionName)
        } else if (SystemPropertyCompat.isSystemPropertyAnyOneExist(OS_CONDITIONS_MIUI)) {
            sCurrentOsName = OS_NAME_MIUI
            sCurrentOriginalOsVersionName = SystemPropertyCompat.getSystemPropertyValue(OS_VERSION_NAME_MIUI)
            sCurrentBeautificationVersionName = extractVersionNameByText(sCurrentOriginalOsVersionName)
        }

        if (sCurrentOsName == null) {
            val realmeUiVersion: String = SystemPropertyCompat.getSystemPropertyValue(OS_VERSION_NAME_REALME_UI)
            // realmeUI 一定要放在 ColorOS 之前判断，因为 realmeUI 是 ColorOS 的另外一个分支
            if (!TextUtils.isEmpty(realmeUiVersion)) {
                sCurrentOsName = OS_NAME_REALME_UI
                sCurrentOriginalOsVersionName = realmeUiVersion
                sCurrentBeautificationVersionName = extractVersionNameByText(sCurrentOriginalOsVersionName)
            } else {
                val colorOsVersion: String = SystemPropertyCompat.getSystemPropertyAnyOneValue(OS_VERSION_NAME_COLOR_OS)
                if (!TextUtils.isEmpty(colorOsVersion)) {
                    sCurrentOsName = OS_NAME_COLOR_OS
                    sCurrentOriginalOsVersionName = colorOsVersion
                    sCurrentBeautificationVersionName = extractVersionNameByText(sCurrentOriginalOsVersionName)
                }
            }
        }

        if (sCurrentOsName == null) {
            val vivoOsName: String = SystemPropertyCompat.getSystemPropertyValue(OS_CONDITIONS_VIVO_OS)
            if (!TextUtils.isEmpty(vivoOsName)) {
                if (vivoOsName.lowercase(Locale.getDefault()).contains("origin")) {
                    sCurrentOsName = OS_NAME_ORIGIN_OS
                    // OriginOS 5 获取到的版本包含 15.x.x，例如：[ro.vivo.product.version]: [PD2429_A_15.0.18.12.W10.V000L1]
                    // OriginOS 4 获取到的版本包含 14.x.x，例如：[ro.vivo.product.version]: [PD2220D_A_14.2.6.5.W10.V000L1]
                    sCurrentOriginalOsVersionName = SystemPropertyCompat.getSystemPropertyAnyOneValue(OS_VERSION_NAME_ORIGIN_OS)
                    sCurrentBeautificationVersionName = extractVersionNameByText(sCurrentOriginalOsVersionName)
                } else if (vivoOsName.lowercase(Locale.getDefault()).contains("funtouch")) {
                    sCurrentOsName = OS_NAME_FUNTOUCH_OS
                    sCurrentOriginalOsVersionName = SystemPropertyCompat.getSystemPropertyAnyOneValue(OS_VERSION_NAME_FUNTOUCH_OS)
                    sCurrentBeautificationVersionName = extractVersionNameByText(sCurrentOriginalOsVersionName)
                }
            }
        }

        if (sCurrentOsName == null) {
            val magicOsVersion: String = SystemPropertyCompat.getSystemPropertyAnyOneValue(OS_VERSION_NAME_MAGIC_OS)
            if (!TextUtils.isEmpty(magicOsVersion)) {
                sCurrentOsName = OS_NAME_MAGIC_OS
                sCurrentOriginalOsVersionName = magicOsVersion
                sCurrentBeautificationVersionName = extractVersionNameByText(sCurrentOriginalOsVersionName)
            }
        }

        if (sCurrentOsName == null) {
            try {
                val buildExClass = Class.forName("com.huawei.system.BuildEx")
                val osBrand = buildExClass.getMethod("getOsBrand").invoke(buildExClass)
                // 在 HarmonyOS 2.0、3.0 上测试，osBrand 字段的值等于 harmony，但是这里为了逻辑严谨，还是用 contains 去判断
                if (osBrand != null && osBrand.toString().lowercase(Locale.getDefault()).contains("harmony")) {
                    sCurrentOsName = OS_NAME_HARMONY_OS
                    sCurrentOriginalOsVersionName = SystemPropertyCompat.getSystemPropertyAnyOneValue(OS_VERSION_NAME_HARMONY_OS)
                    sCurrentBeautificationVersionName = extractVersionNameByText(sCurrentOriginalOsVersionName)
                }
            } catch (ignore: Exception) {
                // default implementation ignored
            }
        }

        if (sCurrentOsName == null) {
            val emuiVersion: String = SystemPropertyCompat.getSystemPropertyValue(OS_VERSION_NAME_EMUI)
            // 在 MagicUI 6.1.0 上会返回 [ro.build.version.emui]: [MagicUI_6.1.0]，这里要注意过滤掉
            if (!TextUtils.isEmpty(emuiVersion) && emuiVersion.lowercase(Locale.getDefault()).contains("emotionui")) {
                sCurrentOsName = OS_NAME_EMUI
                sCurrentOriginalOsVersionName = emuiVersion
                sCurrentBeautificationVersionName = extractVersionNameByText(sCurrentOriginalOsVersionName)
            }
        }

        if (sCurrentOsName == null) {
            val oneUiVersion: String = SystemPropertyCompat.getSystemPropertyValue(OS_VERSION_NAME_ONE_UI)
            if (!TextUtils.isEmpty(oneUiVersion)) {
                sCurrentOsName = OS_NAME_ONE_UI
                if (oneUiVersion.matches(REGEX_NUMBER.toRegex())) {
                    try {
                        // OneUI 5.1.1 获取到的值是 50101 再经过一通计算得出 5.1.1
                        val oneUiVersionCode = oneUiVersion.toInt()
                        sCurrentOriginalOsVersionName = getOneUiVersionNameByVersionCode(oneUiVersionCode)
                        sCurrentBeautificationVersionName = extractVersionNameByText(sCurrentOriginalOsVersionName)
                    } catch (e: Exception) {
                        // default implementation ignored
                    }
                } else if (oneUiVersion.matches(REGEX_VERSION_NAME.toRegex())) {
                    sCurrentOriginalOsVersionName = oneUiVersion
                    sCurrentBeautificationVersionName = extractVersionNameByText(sCurrentOriginalOsVersionName)
                }
            }

            if (sCurrentOsName == null || sCurrentOriginalOsVersionName == null) {
                try {
                    val semPlatformIntField: Field = Build.VERSION::class.java.getDeclaredField("SEM_PLATFORM_INT")
                    semPlatformIntField.setAccessible(true)
                    val semPlatformVersion: Int = semPlatformIntField.getInt(null)
                    sCurrentOsName = OS_NAME_ONE_UI
                    val superfluousValue = 90000
                    if (semPlatformVersion >= superfluousValue) {
                        // https://stackoverflow.com/questions/60122037/how-can-i-detect-samsung-one-ui
                        // OneUI 7.0 获取到的值是 160000，160000 - 90000 = 70000，70000 再经过一通计算得出 7.0 的版本号
                        // OneUI 5.1.1 获取到的值是 140500，无法通过计算得出 5.1.1 的版本号，所以这种方法不是最佳的答案
                        // OneUI 2.5 获取到的值是 110500，110500 - 90000 = 25000，20500 再经过一通计算得出 2.5 的版本号
                        val oneUiVersionCode = semPlatformVersion - superfluousValue
                        sCurrentOriginalOsVersionName = getOneUiVersionNameByVersionCode(oneUiVersionCode)
                        sCurrentBeautificationVersionName = extractVersionNameByText(sCurrentOriginalOsVersionName)
                    }
                } catch (ignore: Exception) {
                    // default implementation ignored
                }
            }
        }

        if (sCurrentOsName == null) {
            val oxygenOsVersion: String = SystemPropertyCompat.getSystemPropertyValue(OS_VERSION_NAME_OXYGEN_OS)
            if (!TextUtils.isEmpty(oxygenOsVersion)) {
                sCurrentOsName = OS_NAME_OXYGEN_OS
                sCurrentOriginalOsVersionName = oxygenOsVersion
                sCurrentBeautificationVersionName = extractVersionNameByText(sCurrentOriginalOsVersionName)
            }
        }
        if (sCurrentOsName == null) {
            val h2OsVersion: String = SystemPropertyCompat.getSystemPropertyValue(OS_VERSION_NAME_H2_OS)
            if (!TextUtils.isEmpty(h2OsVersion)) {
                sCurrentOsName = OS_NAME_H2_OS
                sCurrentOriginalOsVersionName = h2OsVersion
                sCurrentBeautificationVersionName = extractVersionNameByText(sCurrentOriginalOsVersionName)
            }
        }

        if (sCurrentOsName == null && SystemPropertyCompat.isSystemPropertyAnyOneExist(OS_CONDITIONS_FLYME)) {
            sCurrentOsName = OS_NAME_FLYME
            val flymeVersion: String = SystemPropertyCompat.getSystemPropertyValue(OS_VERSION_NAME_FLYME_1)
            if (!TextUtils.isEmpty(flymeVersion) && flymeVersion.lowercase(Locale.getDefault()).contains("flyme")) {
                sCurrentOriginalOsVersionName = flymeVersion
                sCurrentBeautificationVersionName = extractVersionNameByText(sCurrentOriginalOsVersionName)
            } else {
                sCurrentOriginalOsVersionName = SystemPropertyCompat.getSystemPropertyValue(OS_VERSION_NAME_FLYME_2)
                sCurrentBeautificationVersionName = extractVersionNameByText(sCurrentOriginalOsVersionName)
            }
        }

        if (sCurrentOsName == null && SystemPropertyCompat.isSystemPropertyAnyOneExist(OS_CONDITIONS_ZTE_OS)) {
            val osVersion: String = SystemPropertyCompat.getSystemPropertyValue(OS_VERSION_NAME_ZTE_OS)
            if (!TextUtils.isEmpty(osVersion)) {
                val lowerCaseOsVersion = osVersion.lowercase(Locale.getDefault())
                if (lowerCaseOsVersion.contains("redmagicos")) {
                    sCurrentOsName = OS_NAME_RED_MAGIC_OS
                    sCurrentOriginalOsVersionName = osVersion
                    sCurrentBeautificationVersionName = extractVersionNameByText(sCurrentOriginalOsVersionName)
                } else if (lowerCaseOsVersion.contains("myos")) {
                    sCurrentOsName = OS_NAME_MY_OS
                    sCurrentOriginalOsVersionName = osVersion
                    sCurrentBeautificationVersionName = extractVersionNameByText(sCurrentOriginalOsVersionName)
                } else if (lowerCaseOsVersion.contains("zte")) {
                    sCurrentOsName = OS_NAME_MIFAVOR_UI
                    sCurrentOriginalOsVersionName = SystemPropertyCompat.getSystemPropertyValue(OS_VERSION_NAME_MIFAVOR_UI)
                    sCurrentBeautificationVersionName = extractVersionNameByText(sCurrentOriginalOsVersionName)
                }
            }
        }

        if (sCurrentOsName == null && SystemPropertyCompat.isSystemPropertyAnyOneExist(OS_CONDITIONS_SMARTISAN_OS)) {
            sCurrentOsName = OS_NAME_SMARTISAN_OS
            sCurrentOriginalOsVersionName = SystemPropertyCompat.getSystemPropertyValue(OS_VERSION_NAME_SMARTISAN_OS)
            sCurrentBeautificationVersionName = extractVersionNameByText(sCurrentOriginalOsVersionName)
        }

        if (sCurrentOsName == null && SystemPropertyCompat.isSystemPropertyAnyOneExist(OS_CONDITIONS_EUI_OS)) {
            sCurrentOsName = OS_NAME_EUI_OS
            sCurrentOriginalOsVersionName = SystemPropertyCompat.getSystemPropertyValue(OS_VERSION_NAME_EUI_OS)
            sCurrentBeautificationVersionName = extractVersionNameByText(sCurrentOriginalOsVersionName)
        }

        if (sCurrentOsName == null) {
            val zuxOsName: String = SystemPropertyCompat.getSystemPropertyValue(OS_CONDITIONS_ZUX_OS)
            // ZUXOS 一定要放在 ZUI 之前判断，因为 ZUXOS 是 ZUI 的另外一个分支
            if (!TextUtils.isEmpty(zuxOsName) && zuxOsName.lowercase(Locale.getDefault()).contains("zuxos")) {
                sCurrentOsName = OS_NAME_ZUX_OS
                sCurrentOriginalOsVersionName = SystemPropertyCompat.getSystemPropertyAnyOneValue(OS_VERSION_NAME_ZUX_OS)
                sCurrentBeautificationVersionName = extractVersionNameByText(sCurrentOriginalOsVersionName)
            } else if (SystemPropertyCompat.isSystemPropertyAnyOneExist(OS_CONDITIONS_ZUI)) {
                sCurrentOsName = OS_NAME_ZUI
                sCurrentOriginalOsVersionName = SystemPropertyCompat.getSystemPropertyValue(OS_VERSION_NAME_ZUI)
                sCurrentBeautificationVersionName = extractVersionNameByText(sCurrentOriginalOsVersionName)
            }
        }

        if (sCurrentOsName == null) {
            val osName: String = SystemPropertyCompat.getSystemPropertyValue(OS_CONDITIONS_NUBIA_UI)
            if (!TextUtils.isEmpty(osName) && osName.lowercase(Locale.getDefault()).contains("nubiaui")) {
                sCurrentOsName = OS_NAME_NUBIA_UI
                sCurrentOriginalOsVersionName = SystemPropertyCompat.getSystemPropertyValue(OS_VERSION_NAME_NUBIA_UI)
                sCurrentBeautificationVersionName = extractVersionNameByText(sCurrentOriginalOsVersionName)
            }
        }

        if (sCurrentOsName == null) {
            val osVersion: String = SystemPropertyCompat.getSystemPropertyValue(OS_VERSION_NAME_360_UI)
            if (!TextUtils.isEmpty(osVersion) && osVersion.lowercase(Locale.getDefault()).contains("360ui")) {
                sCurrentOsName = OS_NAME_360_UI
                sCurrentOriginalOsVersionName = osVersion
                sCurrentBeautificationVersionName = extractVersionNameByText(sCurrentOriginalOsVersionName)
            }
        }

        if (sCurrentOsName == null) {
            sCurrentOsName = ""
        }

        if (sCurrentOriginalOsVersionName == null) {
            sCurrentOriginalOsVersionName = ""
        }

        if (sCurrentBeautificationVersionName == null) {
            sCurrentBeautificationVersionName = ""
        }
    }

    /**
     * 判断当前设备的厂商系统是否为 HyperOS（小米手机、红米手机的系统）
     */
    fun isHyperOs(): Boolean {
        return TextUtils.equals(sCurrentOsName, OS_NAME_HYPER_OS)
    }

    /**
     * 判断当前设备的厂商系统是否为国内版本的 HyperOS
     */
    fun isHyperOsByChina(): Boolean {
        if (!isHyperOs()) {
            return false
        }
        val propertyValues: Array<String?> = SystemPropertyCompat.getSystemPropertyValues(OS_REGION_HYPER_OS)
        for (propertyValue in propertyValues) {
            if (propertyValue?.equals("cn", ignoreCase = true) == true) {
                return true
            }
        }
        return false
    }

    /**
     * 判断当前设备的厂商系统是否为国际版本的 HyperOS
     */
    fun isHyperOsByGlobal(): Boolean {
        if (!isHyperOs()) {
            return false
        }
        val propertyValues: Array<String?> = SystemPropertyCompat.getSystemPropertyValues(OS_REGION_HYPER_OS)
        for (propertyValue in propertyValues) {
            if (propertyValue?.equals("global", ignoreCase = true) == true) {
                return true
            }
        }
        return false
    }

    /**
     * 判断当前设备的厂商系统开启了 HyperOS 的系统优化选项
     */
    fun isHyperOsOptimization(): Boolean {
        return isXiaoMiSystemOptimization()
    }

    /**
     * 判断当前设备的厂商系统是否为 MIUI（小米手机、红米手机的老系统）
     */
    fun isMiui(): Boolean {
        return TextUtils.equals(sCurrentOsName, OS_NAME_MIUI)
    }

    /**
     * 判断当前设备的厂商系统是否为国内版本的 MIUI
     */
    fun isMiuiByChina(): Boolean {
        if (!isMiui()) {
            return false
        }
        val propertyValues: Array<String?> = SystemPropertyCompat.getSystemPropertyValues(OS_REGION_MIUI)
        for (propertyValue in propertyValues) {
            if (propertyValue?.equals("cn", ignoreCase = true)== true) {
                return true
            }
        }
        return false
    }

    /**
     * 判断当前设备的厂商系统是否为国际版本的 MIUI
     */
    fun isMiuiByGlobal(): Boolean {
        // https://github.com/getActivity/XXPermissions/issues/398#issuecomment-3181978796
        // https://xiaomi.eu/community/threads/how-to-enable-the-region-option-in-settings-for-eu-roms.56303/
        // https://github.com/search?q=+ro.miui.region+&type=code
        // https://c.mi.com/global/post/600955
        if (!isMiui()) {
            return false
        }
        val propertyValues: Array<String?> = SystemPropertyCompat.getSystemPropertyValues(OS_REGION_MIUI)
        for (propertyValue in propertyValues) {
            if (propertyValue?.equals("global", ignoreCase = true) == true) {
                return true
            }
        }
        return false
    }

    /**
     * 判断当前设备的厂商系统是否开启了 MIUI 优化选项
     */
    fun isMiuiOptimization(): Boolean {
        return isXiaoMiSystemOptimization()
    }

    /**
     * 判断小米手机是否开启了系统优化（默认开启）
     *
     * MIUI 关闭步骤为：开发者选项-> 启动 MIUI 优化 -> 点击关闭
     * HyperOS 的关闭步骤为：开发者选项-> 启用系统优化 -> 点击关闭
     *
     * 需要注意的是，关闭优化后，可以跳转到小米定制的权限请求页面，但是开启权限仍然是没有效果的
     * 另外关于 MIUI 国际版开发者选项中是没有优化选项的，但是代码判断是有开启优化选项，也就是默认开启，这样是正确的
     * 相关 Github issue 地址：https://github.com/getActivity/XXPermissions/issues/38
     */
    @SuppressLint("PrivateApi")
    private fun isXiaoMiSystemOptimization(): Boolean {
        try {
            val clazz = Class.forName("android.os.SystemProperties")
            val getMethod = clazz.getMethod("get", String::class.java, String::class.java)
            val ctsValue = getMethod.invoke(clazz, "ro.miui.cts", "").toString()
            val getBooleanMethod = clazz.getMethod("getBoolean", String::class.java, Boolean::class.javaPrimitiveType)
            return getBooleanMethod.invoke(clazz, "persist.sys.miui_optimization", "1" != ctsValue).toString().toBoolean()
        } catch (ignored: Exception) {
            // default implementation ignored
        }
        return true
    }

    /**
     * 判断当前是否为 realmeUI（真我手机的系统）
     */
    fun isRealmeUi(): Boolean {
        return TextUtils.equals(sCurrentOsName, OS_NAME_REALME_UI)
    }

    /**
     * 判断当前设备的厂商系统是否为 ColorOS（ OPPO、一加手机的系统）
     */
    fun isColorOs(): Boolean {
        return TextUtils.equals(sCurrentOsName, OS_NAME_COLOR_OS)
    }

    /**
     * 判断当前设备的厂商系统是否为 OriginOS（ vivo 手机的系统）
     */
    fun isOriginOs(): Boolean {
        return TextUtils.equals(sCurrentOsName, OS_NAME_ORIGIN_OS)
    }

    /**
     * 判断当前设备的厂商系统是否为 FuntouchOS（vivo 手机的老系统）
     */
    fun isFuntouchOs(): Boolean {
        return TextUtils.equals(sCurrentOsName, OS_NAME_FUNTOUCH_OS)
    }

    /**
     * 判断当前是否为 MagicOs 或者 MagicUI（荣耀手机的系统）
     */
    fun isMagicOs(): Boolean {
        return TextUtils.equals(sCurrentOsName, OS_NAME_MAGIC_OS)
    }

    /**
     * 判断当前设备的厂商系统是否为 HarmonyOS（华为手机、荣耀手机的系统）
     */
    fun isHarmonyOs(): Boolean {
        return TextUtils.equals(sCurrentOsName, OS_NAME_HARMONY_OS)
    }

    /**
     * 判断当前设备的厂商系统是否为 EMUI 或者 EmotionUI（华为手机、荣耀手机的老系统）
     */
    fun isEmui(): Boolean {
        return TextUtils.equals(sCurrentOsName, OS_NAME_EMUI)
    }

    /**
     * 判断当前设备的厂商系统是否为 OneUI（三星手机的系统）
     */
    fun isOneUi(): Boolean {
        return TextUtils.equals(sCurrentOsName, OS_NAME_ONE_UI)
    }

    /**
     * 判断当前设备的厂商系统是否为 OxygenOS（一加手机的老系统，相当于 H2OS 的海外版）
     */
    fun isOxygenOs(): Boolean {
        return TextUtils.equals(sCurrentOsName, OS_NAME_OXYGEN_OS)
    }

    /**
     * 判断当前设备的厂商系统是否为 H2OS（一加手机的老系统，相当于 OxygenOS 的国内版）
     */
    fun isH2Os(): Boolean {
        return TextUtils.equals(sCurrentOsName, OS_NAME_H2_OS)
    }

    /**
     * 判断当前设备的厂商系统是否为 Flyme（魅族手机的系统）
     */
    fun isFlyme(): Boolean {
        return TextUtils.equals(sCurrentOsName, OS_NAME_FLYME)
    }

    /**
     * 判断当前设备的厂商系统是否为 RedMagicOS（努比亚红魔手机的系统，努比亚红魔是中兴旗下的子品牌）
     */
    fun isRedMagicOs(): Boolean {
        return TextUtils.equals(sCurrentOsName, OS_NAME_RED_MAGIC_OS)
    }

    /**
     * 判断当前设备的厂商系统是否为 MyOS（中兴手机、努比亚手机的系统）
     */
    fun isMyOs(): Boolean {
        return TextUtils.equals(sCurrentOsName, OS_NAME_MY_OS)
    }

    /**
     * 判断当前设备的厂商系统是否为 MifavorUI（中兴手机的老系统）
     */
    fun isMifavorUi(): Boolean {
        return TextUtils.equals(sCurrentOsName, OS_NAME_MIFAVOR_UI)
    }

    /**
     * 判断当前设备的厂商系统是否为 SmartisanOS（锤子手机的系统）
     */
    fun isSmartisanOs(): Boolean {
        return TextUtils.equals(sCurrentOsName, OS_NAME_SMARTISAN_OS)
    }

    /**
     * 判断当前设备的厂商系统是否为 EUI（乐视手机的系统）
     */
    fun isEui(): Boolean {
        return TextUtils.equals(sCurrentOsName, OS_NAME_EUI_OS)
    }

    /**
     * 判断当前设备的厂商系统是否为 ZUXOS（联想手机、摩托罗拉手机的系统）
     */
    fun isZuxOs(): Boolean {
        return TextUtils.equals(sCurrentOsName, OS_NAME_ZUX_OS)
    }

    /**
     * 判断当前设备的厂商系统是否为 ZUI（联想手机、摩托罗拉手机的老系统）
     */
    fun isZui(): Boolean {
        return TextUtils.equals(sCurrentOsName, OS_NAME_ZUI)
    }

    /**
     * 判断当前设备的厂商系统是否为 nubiaUI（努比亚手机的老系统）
     */
    fun isNubiaUi(): Boolean {
        return TextUtils.equals(sCurrentOsName, OS_NAME_NUBIA_UI)
    }

    /**
     * 判断当前设备的厂商系统是否为 360UI（360 手机的系统）
     */
    fun is360Ui(): Boolean {
        return TextUtils.equals(sCurrentOsName, OS_NAME_360_UI)
    }

    /**
     * 获取当前设备的厂商系统名称
     *
     * @return               如果获取不到则返回空字符串
     */
    fun getOsName(): String {
        return if (sCurrentOsName != null) sCurrentOsName!! else ""
    }

    /**
     * 获取经过美化的厂商系统版本名称
     */
    fun getOsVersionName(): String {
        return if (sCurrentBeautificationVersionName != null) sCurrentBeautificationVersionName!! else ""
    }

    /**
     * 获取原始的厂商系统版本名称（没有经过美化的）
     */
    fun getOriginalOsVersionName(): String {
        return if (sCurrentOriginalOsVersionName != null) sCurrentOriginalOsVersionName!! else ""
    }

    /**
     * 获取厂商系统版本的大版本号
     *
     * @return               如果获取不到则返回 -1
     */
    fun getOsBigVersionCode(): Int {
        val osVersionName = getOsVersionName()
        if (TextUtils.isEmpty(osVersionName)) {
            return 0
        }
        val array = osVersionName.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (array.size == 0) {
            return 0
        }
        return try {
            array[0].toInt()
        } catch (e: Exception) {
            // java.lang.NumberFormatException: Invalid int: "0 "
            -1
        }
    }

    /**
     * 根据 OneUI 的版本号计算出来 OneUI 的版本号
     */
    private fun getOneUiVersionNameByVersionCode(oneUiVersionCode: Int): String {
        // OneUI 8.0：[ro.build.version.oneui]: [80000]
        // OneUI 7.0：[ro.build.version.oneui]: [70000]
        // OneUI 6.1：[ro.build.version.oneui]: [60101]
        // OneUI 5.1.1：[ro.build.version.oneui]: [50101]
        val oneVersion = oneUiVersionCode / 10000
        val twoVersion = oneUiVersionCode % 10000
        val threeVersion = oneUiVersionCode % 100
        return if (threeVersion > 0) {
            // OneUI 5.1.1 的版本号是 50101，计算出来的结果是 5.1.1
            // OneUI 6.1 的版本号是 60101，计算出来的结果是 6.1.1，虽然不太准但也是没有办法
            oneVersion.toString() + "." + (twoVersion / 100) + "." + threeVersion
        } else {
            // OneUI 8.0 的版本号是 80000，计算出来的结果是 8.0
            oneVersion.toString() + "." + (twoVersion / 100)
        }
    }

    /**
     * 从文本提取版本号（只保留数字和点号）
     */
    private fun extractVersionNameByText( text: String?): String {
        if (TextUtils.isEmpty(text)) {
            return ""
        }

        // 使用正则表达式匹配数字和点号组成的版本号
        // 这里需要注意：因为是获取正则表达式的分组，所以需要在 Pattern.compile 时加上括号
        // Github 地址：https://github.com/getActivity/DeviceCompat/pull/3
        var pattern: Pattern = Pattern.compile("(" + REGEX_VERSION_NAME + ")")
        var matcher: Matcher = pattern.matcher(text)

        if (matcher.find() && matcher.groupCount() > 0) {
            val result = matcher.group(1)
            if (result != null) {
                return result
            }
        }

        // 需要注意的是 华为畅享 5S Android 5.1 获取到的厂商版本号是 EmotionUI_3，而不是 3.1 或者 3.0 这种
        // 这里需要注意：因为是获取正则表达式的分组，所以需要在 Pattern.compile 时加上括号
        // Github 地址：https://github.com/getActivity/DeviceCompat/pull/3
        pattern = Pattern.compile("(" + REGEX_NUMBER + ")")
        matcher = pattern.matcher(text)

        if (matcher.find() && matcher.groupCount() > 0) {
            val result = matcher.group(1)
            if (result != null) {
                return "$result.0"
            }
        }
        return ""
    }
}