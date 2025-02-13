package com.knight.kotlin.library_util.toast

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.res.Resources
import android.widget.Toast
import com.knight.kotlin.library_util.ActivityStack
import com.knight.kotlin.library_util.toast.callback.IToastInterceptor
import com.knight.kotlin.library_util.toast.callback.IToastStrategy
import com.knight.kotlin.library_util.toast.callback.IToastStyle
import com.knight.kotlin.library_util.toast.style.BlackToastStyle
import com.knight.kotlin.library_util.toast.style.CustomToastStyle
import com.knight.kotlin.library_util.toast.style.LocationToastStyle
import com.knight.kotlin.library_util.toast.style.WhiteToastStyle


/**
 * Author:Knight
 * Time:2021/12/20 14:51
 * Description:ToastUtils
 * 吐司工具类
 */
object ToastUtils {

    /** Application 对象  */

    var sApplication: Application? = null

    /** Toast 处理策略  */
    var sToastStrategy: IToastStrategy? = null

    /** Toast 样式  */
    var sToastStyle: IToastStyle<*>? = null

    /** Toast 拦截器（可空）  */
    var sToastInterceptor: IToastInterceptor? = null

    /** 调试模式  */
    var sDebugMode: Boolean? = null

    /**
     * 不允许被外部实例化
     */
    private fun Toaster() {}

    /**
     * 初始化 Toast，需要在 Application.create 中初始化
     *
     * @param application       应用的上下文
     */
    fun init(application: Application) {
        init(application, sToastStyle)
    }

    fun init(application: Application, strategy: IToastStrategy?) {
        init(application, strategy, null)
    }

    fun init(application: Application, style: IToastStyle<*>?) {
        init(application, null, style)
    }

    /**
     * 初始化 Toast
     *
     * @param application       应用的上下文
     * @param strategy          Toast 策略
     * @param style             Toast 样式
     */
    fun init(application: Application, strategy: IToastStrategy?, style: IToastStyle<*>?) {
        // 如果当前已经初始化过了，就不要再重复初始化了
        var strategy = strategy
        var style = style
        if (isInit()) {
            return
        }

        sApplication = application
        ActivityStack.register(application)

        // 初始化 Toast 策略
        if (strategy == null) {
            strategy = ToastStrategy()
        }
        setStrategy(strategy)

        // 设置 Toast 样式
        if (style == null) {
            style = BlackToastStyle()
        }
        setStyle(style)
    }

    /**
     * 判断当前框架是否已经初始化
     */
    fun isInit(): Boolean {
        return sApplication != null && sToastStrategy != null && sToastStyle != null
    }

    /**
     * 延迟显示 Toast
     */
    fun delayedShow(id: Int, delayMillis: Long) {
        delayedShow(stringIdToCharSequence(id), delayMillis)
    }

    fun delayedShow(`object`: Any?, delayMillis: Long) {
        delayedShow(objectToCharSequence(`object`), delayMillis)
    }

    fun delayedShow(text: CharSequence?, delayMillis: Long) {
        val params = ToastParams()
        params.text = text
        params.delayMillis = delayMillis
        show(params)
    }

    /**
     * debug 模式下显示 Toast
     */
    fun debugShow(id: Int) {
        debugShow(stringIdToCharSequence(id))
    }

    fun debugShow(`object`: Any?) {
        debugShow(objectToCharSequence(`object`))
    }

    fun debugShow(text: CharSequence?) {
        if (!isDebugMode()) {
            return
        }
        val params = ToastParams()
        params.text = text
        show(params)
    }

    /**
     * 显示一个短 Toast
     */
    fun showShort(id: Int) {
        showShort(stringIdToCharSequence(id))
    }

    fun showShort(`object`: Any?) {
        showShort(objectToCharSequence(`object`))
    }

    fun showShort(text: CharSequence?) {
        val params = ToastParams()
        params.text = text
        params.duration = Toast.LENGTH_SHORT
        show(params)
    }

    /**
     * 显示一个长 Toast
     */
    fun showLong(id: Int) {
        showLong(stringIdToCharSequence(id))
    }

    fun showLong(`object`: Any?) {
        showLong(objectToCharSequence(`object`))
    }

    fun showLong(text: CharSequence?) {
        val params = ToastParams()
        params.text = text
        params.duration = Toast.LENGTH_LONG
        show(params)
    }

    /**
     * 显示 Toast
     */
    fun show(id: Int) {
        show(stringIdToCharSequence(id))
    }

    fun show(`object`: Any?) {
        show(objectToCharSequence(`object`))
    }

    fun show(text: CharSequence?) {
        val params = ToastParams()
        params.text = text
        show(params)
    }

    fun show(params: ToastParams) {
        checkInitStatus()

        // 如果是空对象或者空文本就不显示
        if (params.text == null || params.text!!.length == 0) {
            return
        }

        if (params.strategy == null) {
            params.strategy = sToastStrategy
        }

        if (params.interceptor == null) {
            if (sToastInterceptor == null) {
                sToastInterceptor = ToastLogInterceptor()
            }
            params.interceptor = sToastInterceptor
        }

        if (params.style == null) {
            params.style = sToastStyle
        }

        if (params.interceptor!!.intercept(params)) {
            return
        }

        if (params.duration == -1) {
            params.duration = params.strategy!!.computeShowDuration(params.text!!)
        }

        params.strategy!!.showToast(params)
    }

    /**
     * 取消吐司的显示
     */
    fun cancel() {
        sToastStrategy!!.cancelToast()
    }

    /**
     * 设置吐司的位置
     *
     * @param gravity           重心
     */
    fun setGravity(gravity: Int) {
        setGravity(gravity, 0, 0)
    }

    fun setGravity(gravity: Int, xOffset: Int, yOffset: Int) {
        setGravity(gravity, xOffset, yOffset, 0f, 0f)
    }

    fun setGravity(gravity: Int, xOffset: Int, yOffset: Int, horizontalMargin: Float, verticalMargin: Float) {
        sToastStyle = LocationToastStyle(sToastStyle, gravity, xOffset, yOffset, horizontalMargin, verticalMargin)
    }

    /**
     * 给当前 Toast 设置新的布局
     */
    fun setView(id: Int) {
        if (id <= 0) {
            return
        }
        if (sToastStyle == null) {
            return
        }
        setStyle(
            CustomToastStyle(
                id, sToastStyle!!.getGravity(),
                sToastStyle!!.getXOffset(), sToastStyle!!.getYOffset(),
                sToastStyle!!.getHorizontalMargin(), sToastStyle!!.getVerticalMargin()
            )
        )
    }

    /**
     * 初始化全局的 Toast 样式
     *
     * @param style         样式实现类，框架已经实现两种不同的样式
     * 黑色样式：[BlackToastStyle]
     * 白色样式：[WhiteToastStyle]
     */
    fun setStyle(style: IToastStyle<*>?) {
        if (style == null) {
            return
        }
        sToastStyle = style
    }

    fun getStyle(): IToastStyle<*>? {
        return sToastStyle
    }

    /**
     * 设置 Toast 显示策略
     */
    fun setStrategy(strategy: IToastStrategy?) {
        if (strategy == null) {
            return
        }
        sToastStrategy = strategy
        sToastStrategy!!.registerStrategy(sApplication!!)
    }

    fun getStrategy(): IToastStrategy? {
        return sToastStrategy
    }

    /**
     * 设置 Toast 拦截器（可以根据显示的内容决定是否拦截这个Toast）
     * 场景：打印 Toast 内容日志、根据 Toast 内容是否包含敏感字来动态切换其他方式显示（这里可以使用我的另外一套框架 EasyWindow）
     */
    fun setInterceptor(interceptor: IToastInterceptor?) {
        sToastInterceptor = interceptor
    }

    fun getInterceptor(): IToastInterceptor? {
        return sToastInterceptor
    }

    /**
     * 是否为调试模式
     */
    fun setDebugMode(debug: Boolean) {
        sDebugMode = debug
    }

    /**
     * 检查框架初始化状态，如果未初始化请先调用[Toaster.init]
     */
    private fun checkInitStatus() {
        // 框架当前还没有被初始化，必须要先调用 init 方法进行初始化
        checkNotNull(sApplication) { "Toaster has not been initialized" }
    }

    fun isDebugMode(): Boolean {
        if (sDebugMode == null) {
            checkInitStatus()
            sDebugMode = (sApplication!!.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        }
        return sDebugMode!!
    }

    private fun stringIdToCharSequence(id: Int): CharSequence {
        checkInitStatus()
        return try {
            // 如果这是一个资源 id
            sApplication!!.resources.getText(id)
        } catch (ignored: Resources.NotFoundException) {
            // 如果这是一个 int 整数
            id.toString()
        }
    }

    private fun objectToCharSequence(`object`: Any?): CharSequence {
        return `object`?.toString() ?: "null"
    }

}