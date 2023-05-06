package com.knight.kotlin.library_base.config

/**
 * Author:Knight
 * Time:2021/12/24 11:11
 * Description:CacheKey
 */
object CacheKey {

    /**
     *
     * 用户信息Key
     */
    const val USER = "user"

    /**
     * 是否同意用户协议
     */
    const val AGREEMENT = "agreement"

    /**
     *
     * 是否跟随系统
     */
    const val ISFOLLOWSYSTEM = "isFollow"

    /**
     *
     * 普通模式还是深色模式
     */
    const val NORMALDARK = "normalDark"

    /**
     * 主题颜色
     */
    const val THEMECOLOR = "themecolor"

    /**
     * 导航栏是否伴随主题色变化
     */
    const val STATUSWITHTHEME = "statuswiththeme"

    /**
     * 护眼模式
     */
    const val EYECARE = "eyecare"

    /**
     * 语言选择
     */
    const val LANGUAGE = "language"

    /**
     * 字体缩放系数
     */
    const val FONTSIZESCALE = "fontSizeScale"

    /**
     * 是否开启快捷登录---> 指纹识别
     *
     */
    const val FINGERLOGIN = "openFingerLogin"

    /**
     *
     * 生物识别iv 向量key
     */
    const val CLIPER_IV = "cliper_iv"

    /**
     * Base64后的登录账号密码信息
     */
    const val ENCRYPT_LOGIN_MESSAGE = "encryptLoginMessage"

    /**
     * 明文的账号密码信息
     */
    const val LOGIN_MESSAGE = "loginMessage"

    /**
     * 手势密码
     */
    const val GESTUREPASSEORD = "gesturePassword"

    /**
     *
     * 是否开启手势密码登录
     */
    const val GESTURELOGIN = "gestureLogin"

    /**
     * 晚间模式开启时间时
     */
    const val STARTNIGHTHOUR = "nighthour"

    /**
     * 晚间模式开启时间分
     *
     */
    const val STARTNIGHTMINUTER = "nightminuter"

    /**
     * 白天模式开启时间时
     *
     */
    const val STARTDAYHOUR = "dayhour"

    /**
     * 白天模式开启时间分
     */
    const val STARTDAYMINUTER = "dayminuter"

    /**
     * 是否开启自动切换夜间模式
     */
    const val AUTONIGHTMODE = "autoChangeNight"

    /**
     *
     * 是否正在夜间模式
     */
    const val INNIGHTMODE = "inNightMode"

    /**
     *
     * 设置排名
     *
     */
    const val RANK = "rank"

}