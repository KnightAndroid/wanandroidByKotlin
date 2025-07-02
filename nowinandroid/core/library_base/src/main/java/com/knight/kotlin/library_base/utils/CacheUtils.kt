package com.knight.kotlin.library_base.utils

import android.content.Context
import android.text.TextUtils
import com.knight.kotlin.library_base.BaseApp
import com.core.library_base.util.ColorUtils
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import java.lang.reflect.Type


/**
 * Author:Knight
 * Time:2021/12/24 11:01
 * Description:CacheUtils 缓存工具
 */
object CacheUtils {
    private var mmkv: MMKV? = null

    /**
     *
     * 应用启动初始化
     * @param context
     */
    fun init(context: Context) {
        MMKV.initialize(context)
        mmkv = MMKV.defaultMMKV()
    }

    /**
     * 保存对象信息
     */
    fun <T> saveDataInfo(tag: String?, data: T) {
        if (data == null) {
            return
        }
        mmkv?.encode(tag, Gson().toJson(data))
    }


    /**
     * 获取对象信息
     *
     * @param tag
     * @param typeOfT
     * @return T
     */
    fun <T> getDataInfo(tag: String?, typeOfT: Type): T {
        val userStr = mmkv?.decodeString(tag)
        return Gson().fromJson(userStr, typeOfT)
    }

    /**
     * 是否点击协议
     *
     * @param isAgree
     */
    fun saveIsAgreeMent(isAgree: Boolean) {
        mmkv?.encode(com.knight.kotlin.library_base.config.CacheKey.AGREEMENT, isAgree)
    }

    /**
     * 返回是否同意协议状态
     *
     * @return
     */
    fun getAgreeStatus(): Boolean {
        return mmkv?.decodeBool(com.knight.kotlin.library_base.config.CacheKey.AGREEMENT, false)!!
    }

    /**
     * 清空用户信息
     */
    fun loginOut() {
        mmkv?.remove(com.knight.kotlin.library_base.config.CacheKey.USER)
    }

    /**
     * 设置是否跟随系统
     *
     * @param isFollowSystem
     * @return
     */
    fun setFollowSystem(isFollowSystem: Boolean) {
        mmkv?.encode(com.knight.kotlin.library_base.config.CacheKey.ISFOLLOWSYSTEM, isFollowSystem)
    }

    /**
     * 返回是否跟随系统 默认不跟随系统
     *
     * @return
     */
    fun getFollowSystem(): Boolean {
        return mmkv?.decodeBool(com.knight.kotlin.library_base.config.CacheKey.ISFOLLOWSYSTEM, false) ?: false
    }

    /**
     * 设置普通模式还是深色模式
     */
    fun setNormalDark(normalDark: Boolean) {
        mmkv?.encode(com.knight.kotlin.library_base.config.CacheKey.NORMALDARK, normalDark)
    }

    /**
     * 获取模式是普通还是深色模式
     * 默认返回普通
     *
     * @return true 深色模式 false  不是深色模式
     */
    fun getNormalDark(): Boolean {
        return mmkv?.decodeBool(com.knight.kotlin.library_base.config.CacheKey.NORMALDARK, false) ?: false
    }

    /**
     * 状态栏是否伴随主题着色
     *
     * @param withTheme
     */
    fun statusBarIsWithTheme(withTheme: Boolean) {
        mmkv?.encode(com.knight.kotlin.library_base.config.CacheKey.STATUSWITHTHEME, withTheme)
    }

    /**
     * 获取状态栏是否伴随着色
     *
     * @return
     */
    fun getStatusBarIsWithTheme(): Boolean {
        return mmkv?.decodeBool(com.knight.kotlin.library_base.config.CacheKey.STATUSWITHTHEME, false) ?: false
    }

    /**
     * 设置主题颜色
     *
     * @param themecolor
     */
    fun setThemeColor(themecolor: Int) {
        mmkv?.encode(com.knight.kotlin.library_base.config.CacheKey.THEMECOLOR, themecolor)
    }

    /**
     * 返回主题颜色
     *
     * @return
     */
    @JvmStatic
    fun getThemeColor(): Int {
        return mmkv?.decodeInt(com.knight.kotlin.library_base.config.CacheKey.THEMECOLOR,
            ColorUtils.convertToColorInt("55aff4")
        ) ?: ColorUtils.convertToColorInt("55aff4")
    }

    /**
     * 设置是否护眼
     */
    fun setIsEyeCare(isEyeCare: Boolean) {
        mmkv?.encode(com.knight.kotlin.library_base.config.CacheKey.EYECARE, isEyeCare)
    }

    /**
     * 返回是否护眼模式
     *
     * @return
     */
    fun getIsEyeCare(): Boolean {
        return mmkv?.decodeBool(com.knight.kotlin.library_base.config.CacheKey.EYECARE, false)!!
    }

    /**
     * 返回是什么语言模式 跟随系统 中文 英文
     */
    fun getLanguageMode(): String {
        return mmkv?.decodeString(com.knight.kotlin.library_base.config.CacheKey.LANGUAGE, "简体中文")!!
    }

    /**
     * 保存语言模式
     *
     * @param languageType
     */
    fun setLanguageType(languageType: String?) {
        mmkv?.encode(com.knight.kotlin.library_base.config.CacheKey.LANGUAGE, languageType)
    }

    /**
     *
     * 字体缩放系数
     * @param fontSizeScale
     */
    fun setSystemFontSize(fontSizeScale: Float) {
        mmkv?.encode(com.knight.kotlin.library_base.config.CacheKey.FONTSIZESCALE, fontSizeScale)
    }

    /**
     *
     * 返回字体缩放系数
     * @return
     */
    fun getSystemFontSize(): Float {
        return mmkv?.decodeFloat(com.knight.kotlin.library_base.config.CacheKey.FONTSIZESCALE, 1.0f)!!
    }

    /**
     *
     * 保存网络缓存
     * @param cacheKey
     * @param cacheValue
     */
    @Suppress("unused")
    fun saveCacheValue(cacheKey: String?, cacheValue: String?): Boolean? {
        return mmkv?.putString(cacheKey, cacheValue)?.commit()
    }

    /**
     *
     * 取出缓存值
     * @param cacheKey
     * @return
     */
    @Suppress("unused")
    fun getCacheValue(cacheKey: String?): String? {
        return mmkv?.getString(cacheKey, null)
    }

    /**
     * 返回生物识别iv向量
     */
    fun getCliperIv(): String {
        return mmkv?.decodeString(com.knight.kotlin.library_base.config.CacheKey.CLIPER_IV, "") ?: ""
    }

    /**
     * 保存生物识别iv向量
     *
     * @param cliperIv
     */
    fun setCliperIv(cliperIv: String?) {
        mmkv?.encode(
            com.knight.kotlin.library_base.config.CacheKey.CLIPER_IV,
            cliperIv
        )
    }

    /**
     * 返回Base64后的登录账号信息
     */
    fun getEncryptLoginMessage(): String? {
        return mmkv?.decodeString(com.knight.kotlin.library_base.config.CacheKey.ENCRYPT_LOGIN_MESSAGE, "")
    }

    /**
     * 保存Base64后的登录账号信息
     *
     * @param encryptloginMessage
     */
    fun setEncryptLoginMessage(encryptloginMessage: String?) {
        mmkv?.encode(com.knight.kotlin.library_base.config.CacheKey.ENCRYPT_LOGIN_MESSAGE, encryptloginMessage)
    }

    /**
     *
     * 保存明文账号密码信息
     * @return
     */
    fun setLoginMessage(loginMessage: String?) {
        mmkv?.encode(com.knight.kotlin.library_base.config.CacheKey.LOGIN_MESSAGE, loginMessage)
    }

    /**
     * 得到明文账号密码信息
     * @return
     */
    fun getLoginMessage(): String {
        return mmkv?.decodeString(com.knight.kotlin.library_base.config.CacheKey.LOGIN_MESSAGE, "") ?: ""
    }

    /**
     * 设置是否指纹登录
     */
    fun setFingerLogin(isQuickLogin: Boolean) {
        mmkv?.encode(com.knight.kotlin.library_base.config.CacheKey.FINGERLOGIN, isQuickLogin)
    }


    /**
     * 返回是否开启指纹登录
     * @return
     */
    fun getFingerLogin(): Boolean {
        return mmkv?.decodeBool(com.knight.kotlin.library_base.config.CacheKey.FINGERLOGIN, false)!!
    }

    /**
     * 存放手势密码
     *
     */
    fun setGesturePassword(gesturePassword: String?) {
        mmkv?.encode(com.knight.kotlin.library_base.config.CacheKey.GESTUREPASSEORD, gesturePassword)
    }

    /**
     *
     * 返回手势密码 是否已经创建手势密码
     * @return
     */
    fun getGesturePassword(): String? {
        return mmkv?.decodeString(com.knight.kotlin.library_base.config.CacheKey.GESTUREPASSEORD)
    }

    /**
     *
     * 返回是否开启手势密码登录
     * @return
     */
    fun getGestureLogin(): Boolean {
        return mmkv?.decodeBool(com.knight.kotlin.library_base.config.CacheKey.GESTURELOGIN, false) ?: false
    }

    /**
     *
     * 设置手势密码登录
     * @param gestureLogin
     */
    fun setGestureLogin(gestureLogin: Boolean) {
        mmkv?.encode(com.knight.kotlin.library_base.config.CacheKey.GESTURELOGIN, gestureLogin)
    }

    /**
     *
     * 获取夜间模式开启时间 时
     * @return
     */
    fun getStartNightModeHour(): String {
        return if (TextUtils.isEmpty(mmkv?.decodeString(com.knight.kotlin.library_base.config.CacheKey.STARTNIGHTHOUR))) {
            "22"
        } else {
            mmkv?.decodeString(com.knight.kotlin.library_base.config.CacheKey.STARTNIGHTHOUR) ?: "22"
        }
    }

    /**
     *
     * 设置夜间模式开启时间 时
     * @return
     */
    fun setStartNightModeHour(hour: String?) {
        mmkv?.encode(com.knight.kotlin.library_base.config.CacheKey.STARTNIGHTHOUR, hour)
    }

    /**
     *
     * 获取夜间模式开启时间 分
     * @return
     */
    fun getStartNightModeMinuter(): String {
        return if (TextUtils.isEmpty(mmkv?.decodeString(com.knight.kotlin.library_base.config.CacheKey.STARTNIGHTMINUTER))) {
            "00"
        } else {
            mmkv?.decodeString(com.knight.kotlin.library_base.config.CacheKey.STARTNIGHTMINUTER) ?: "00"
        }
    }

    /**
     *
     * 设置夜间模式开启时间 分
     * @return
     */
    fun setStartNightModeMinuter(minuter: String?) {
        mmkv?.encode(com.knight.kotlin.library_base.config.CacheKey.STARTNIGHTMINUTER, minuter)
    }

    /**
     *
     * 获取白天模式开启时间 时
     * @return
     */
    fun getStartDayModeHour(): String {
        return if (TextUtils.isEmpty(mmkv?.decodeString(com.knight.kotlin.library_base.config.CacheKey.STARTDAYHOUR))) {
            "06"
        } else {
            mmkv?.decodeString(com.knight.kotlin.library_base.config.CacheKey.STARTDAYHOUR) ?: "06"
        }
    }

    /**
     *
     * 设置白天模式开启时间 时
     * @return
     */
    fun setStartDayModeHour(hour: String?) {
        mmkv?.encode(com.knight.kotlin.library_base.config.CacheKey.STARTDAYHOUR, hour)
    }

    /**
     *
     * 获取白天模式开启时间 分
     * @return
     */
    fun getStartDayModeMinuter(): String {
        return if (TextUtils.isEmpty(mmkv?.decodeString(com.knight.kotlin.library_base.config.CacheKey.STARTDAYMINUTER))) {
            "00"
        } else {
            mmkv?.decodeString(com.knight.kotlin.library_base.config.CacheKey.STARTDAYMINUTER) ?: "00"
        }
    }

    /**
     *
     * 设置白天模式开启时间 分
     * @return
     */
    fun setStartDayModeMinuter(minuter: String?) {
        mmkv?.encode(com.knight.kotlin.library_base.config.CacheKey.STARTDAYMINUTER, minuter)
    }

    /**
     *
     * 设置开启夜间模式
     * @param nightMode
     */
    fun setOpenAutoNightMode(nightMode: Boolean) {
        mmkv?.encode(com.knight.kotlin.library_base.config.CacheKey.AUTONIGHTMODE, nightMode)
    }

    /**
     *
     * 返回夜间模式开启状态
     * @return
     */
    fun getAutoNightMode(): Boolean {
        return mmkv?.decodeBool(com.knight.kotlin.library_base.config.CacheKey.AUTONIGHTMODE, false) ?:false
    }

    /**
     *
     * 设置进入APP时是否夜间状态
     *
     */
    fun setNightModeStatus(nightMode: Boolean) {
        mmkv?.encode(com.knight.kotlin.library_base.config.CacheKey.INNIGHTMODE, nightMode)
    }

    /**
     *
     * 返回当前是否夜间模式
     * @return
     */
    fun getNightModeStatus(): Boolean {
        return mmkv?.decodeBool(com.knight.kotlin.library_base.config.CacheKey.INNIGHTMODE) ?: false
    }


    /**
     * 设置排名
     *
     */
    fun saveUserRank(rank:String) {
        mmkv?.encode(com.knight.kotlin.library_base.config.CacheKey.RANK,rank)
    }


    /**
     * 返回排名
     *
     */
    fun getUserRank() : String {
        return mmkv?.decodeString(com.knight.kotlin.library_base.config.CacheKey.RANK,"0") ?: "0"
    }


    /**
     * 设置重力传感器是否开启
     */
    fun setGravitySensorSwitch(isGravitySensorEnabled: Boolean) {
        mmkv?.encode(com.knight.kotlin.library_base.config.CacheKey.GRAVITY_SENSOR_SWITCH, isGravitySensorEnabled)
    }


    /**
     * 返回是否开启重力传感器
     * @return
     */
    fun getGravitySensorSwitch(): Boolean {
        return mmkv?.decodeBool(com.knight.kotlin.library_base.config.CacheKey.GRAVITY_SENSOR_SWITCH, false)!!
    }


    /**
     *
     * 设置颜色模型
     */
    fun setDarkMode(darkMode:String) {
        mmkv?.encode(com.knight.kotlin.library_base.config.CacheKey.DARK_MODEL, darkMode)
    }

    /**
     *
     * 返回颜色模型
     */
    fun getDarkMode() :String {
        return mmkv?.decodeString(com.knight.kotlin.library_base.config.CacheKey.DARK_MODEL,"system") ?: "system"
    }

    /**
     *
     * 设置背景动画效果模式
     */
    fun setBackgroundAnimationMode(mode:String) {
        mmkv?.encode(com.knight.kotlin.library_base.config.CacheKey.BG_ANIMAT_MODE,mode)
    }

    /**
     *
     * 返回背景动画效果模式
     */
    fun getBackgroundAnimationMode():String {
        return mmkv?.decodeString(com.knight.kotlin.library_base.config.CacheKey.BG_ANIMAT_MODE,"system") ?: "system"
    }


    /**
     *
     * icon提供
     */
    fun setIconProvider(value:String) {
        mmkv?.encode(com.knight.kotlin.library_base.config.CacheKey.ICON_PROVIDER,value)
    }

    /**
     *
     * 返回icon 提供
     */
    fun getIconProvider():String {
        return mmkv?.decodeString(com.knight.kotlin.library_base.config.CacheKey.ICON_PROVIDER, BaseApp.context.packageName) ?: ""
    }


    /**
     *
     * 获取ip
     */
    fun setIp(value:String) {
        mmkv?.encode(com.knight.kotlin.library_base.config.CacheKey.IP,value)
    }

    /**
     *
     * 获取ip
     */
    fun getIp():String {
        return mmkv?.decodeString(com.knight.kotlin.library_base.config.CacheKey.IP,"") ?: ""
    }

    /**
     *
     * 清空ip
     */
    fun clearIp() {
        mmkv?.encode(com.knight.kotlin.library_base.config.CacheKey.IP,"")
    }


    /**
     *
     * 获取天气弹窗是否弹出
     */
    fun getWeatherDialogShow():String {
        return mmkv?.decodeString(com.knight.kotlin.library_base.config.CacheKey.IS_WEATHER_HIDDEN,"") ?:""
    }

    /**
     *
     * 设置是否弹出
     */
    fun setWeatherDialogHidden(date:String){
        mmkv?.encode(com.knight.kotlin.library_base.config.CacheKey.IS_WEATHER_HIDDEN,date)
    }




}