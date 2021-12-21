package com.knight.kotlin.library_util.toast

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.res.Resources
import com.knight.kotlin.library_base.util.ActivityManagerUtils
import com.knight.kotlin.library_util.toast.callback.ToastInterceptorInterface
import com.knight.kotlin.library_util.toast.callback.ToastStrategyInterface
import com.knight.kotlin.library_util.toast.callback.ToastStyleInterface
import com.knight.kotlin.library_util.toast.style.BlackToastStyle
import com.knight.kotlin.library_util.toast.style.LocationToastStyle
import com.knight.kotlin.library_util.toast.style.ViewToastStyle


/**
 * Author:Knight
 * Time:2021/12/20 14:51
 * Description:ToastUtils
 * 吐司工具类
 */
object ToastUtils {

    /**
     *
     * Application 对象
     */
    private lateinit var sApplication:Application

    /**
     *
     * Toast处理策略
     */
    private var sToastStrategy: ToastStrategyInterface?=null

    /**
     *
     * Toast样式
     */
    private var sToastStyle: ToastStyleInterface<*>?=null

    /**
     *
     *
     */
    private lateinit var sToastInterceptor:ToastInterceptorInterface
    /**
     *
     * 调试模式
     */
    private var mDebugMode:Boolean = true

    /**
     * 初始化 Toast，需要在 Application.create 中初始化
     *
     * @param application       应用的上下文
     */
    fun init(application:Application) {
        init(application, sToastStyle)
    }

    fun init(application: Application,style:ToastStyleInterface<*>?) {
        sApplication = application

        // 初始化 Toast 显示处理器
        if (sToastStrategy == null) {
            setStrategy(ToastStrategy())
        }
        if (style == null) {
            var mStyle = BlackToastStyle()
            // 设置 Toast 样式
            setStyle(mStyle)
        } else {
            // 设置 Toast 样式
            setStyle(style)
        }

    }

    /**
     * 判断当前框架是否已经初始化
     */
    fun isInit(): Boolean {
        return sApplication != null && sToastStrategy != null && sToastStyle != null
    }

    /**
     * 显示一个对象的吐司
     *
     * @param object      对象
     */
    fun show(`object`: Any?) {
        show(`object`?.toString() ?: "null")
    }

    fun debugShow(`object`: Any?) {
        if (!isDebugMode()) {
            return
        }
        show(`object`)
    }

    /**
     * 显示一个吐司
     *
     * @param id 如果传入的是正确的 string id 就显示对应字符串
     * 如果不是则显示一个整数的string
     */
    fun show(id: Int) {
        try {
            // TODO
            // 如果这是一个资源 id 为了适配中英文提示 ActivityManagerUtils.getInstance().getTopActivity() 代替 sApplication
            show(ActivityManagerUtils.getInstance()?.getTopActivity()?.getResources()?.getText(id))
        } catch (ignored: Resources.NotFoundException) {
            // 如果这是一个 int 整数
            show(id.toString())
        }
    }

    fun debugShow(id: Int) {
        if (!isDebugMode()) {
            return
        }
        show(id)
    }

    /**
     * 显示一个吐司
     *
     * @param text      需要显示的文本
     */
    fun show(text: CharSequence?) {
        // 如果是空对象或者空文本就不显示
        if (text == null || text.length == 0) {
            return
        }
        if (sToastInterceptor != null && sToastInterceptor.intercept(text)) {
            return
        }
        sToastStrategy?.showToast(text)
    }

    fun debugShow(text: CharSequence?) {
        if (!isDebugMode()) {
            return
        }
        show(text)
    }

    /**
     * 取消吐司的显示
     */
    fun cancel() {
        sToastStrategy?.cancelToast()
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

    fun setGravity(
        gravity: Int,
        xOffset: Int,
        yOffset: Int,
        horizontalMargin: Float,
        verticalMargin: Float
    ) {
        sToastStrategy?.bindStyle(
            LocationToastStyle(
                sToastStyle,
                gravity,
                xOffset,
                yOffset,
                horizontalMargin,
                verticalMargin
            )
        )
    }

    /**
     * 给当前 Toast 设置新的布局
     */
    fun setView(id: Int) {
        if (id <= 0) {
            return
        }
        setStyle(ViewToastStyle(id, sToastStyle))
    }

    /**
     * 初始化全局的 Toast 样式
     *
     * @param style         样式实现类，框架已经实现两种不同的样式
     * 黑色样式：[BlackToastStyle]
     * 白色样式：[com.knight.wanandroid.library_util.toast.style.WhiteToastStyle]
     */
    fun setStyle(style: ToastStyleInterface<*>) {
        sToastStyle = style
        sToastStrategy?.bindStyle(style)
    }

    fun getStyle(): ToastStyleInterface<*>? {
        return sToastStyle
    }

    /**
     * 设置 Toast 显示策略
     */
    fun setStrategy(strategy: ToastStrategyInterface) {
        sToastStrategy = strategy
        sToastStrategy?.registerStrategy(sApplication)
    }

    fun getStrategy(): ToastStrategyInterface? {
        return sToastStrategy
    }

    /**
     * 设置 Toast 拦截器（可以根据显示的内容决定是否拦截这个Toast）
     * 场景：打印 Toast 内容日志、根据 Toast 内容是否包含敏感字来动态切换其他方式显示（这里可以使用我的另外一套框架 XToast）
     */
    fun setInterceptor(interceptor: ToastInterceptorInterface) {
        sToastInterceptor = interceptor
    }

    fun getInterceptor(): ToastInterceptorInterface? {
        return sToastInterceptor
    }

    /**
     * 是否为调试模式
     */
    fun setDebugMode(debug: Boolean) {
        mDebugMode = debug
    }

    private fun isDebugMode(): Boolean {
        if (mDebugMode == null) {
            mDebugMode = sApplication.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        }
        return mDebugMode
    }


}