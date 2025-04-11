package com.knight.kotlin.library_util.floatmenu.customfloat

import android.animation.Animator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.app.Activity
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowManager
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import kotlin.math.abs


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/11 14:42
 * @descript:
 */
abstract class BaseFloatDialog protected constructor(context: Context) {
    /**
     * 停靠默认位置
     */
    private val mDefaultLocation = RIGHT


    /**
     * 悬浮窗 坐落 位置
     */
    private var mHintLocation = mDefaultLocation


    /**
     * 记录当前手指位置在屏幕上的横坐标值
     */
    private var mXInScreen = 0f

    /**
     * 记录当前手指位置在屏幕上的纵坐标值
     */
    private var mYInScreen = 0f

    /**
     * 记录手指按下时在屏幕上的横坐标的值
     */
    private var mXDownInScreen = 0f

    /**
     * 记录手指按下时在屏幕上的纵坐标的值
     */
    private var mYDownInScreen = 0f

    /**
     * 记录手指按下时在小悬浮窗的View上的横坐标的值
     */
    private var mXInView = 0f

    /**
     * 记录手指按下时在小悬浮窗的View上的纵坐标的值
     */
    private var mYinView = 0f

    /**
     * 记录屏幕的宽度
     */
    private var mScreenWidth = 0

    /**
     * 来自 activity 的 wManager
     */
    private var wManager: WindowManager? = null


    /**
     * 为 wManager 设置 LayoutParams
     */
    private var wmParams: WindowManager.LayoutParams? = null


    /**
     * 用于显示在 mActivity 上的 mActivity
     */
    private val mActivity = context


    /**
     * 用于 定时 隐藏 logo的定时器
     */
    private var mHideTimer: CountDownTimer? = null


    /**
     * float menu的高度
     */
    private val mHandler: Handler = Handler(Looper.getMainLooper())


    /**
     * 悬浮窗左右移动到默认位置 动画的 加速器
     */
    private val mLinearInterpolator: Interpolator = LinearInterpolator()

    /**
     * 标记是否拖动中
     */
    private var isDrag = false

    /**
     * 用于恢复悬浮球的location的属性动画值
     */
    private var mResetLocationValue = 0

    fun isApplicationDialog(): Boolean {
        return isApplicationDialog
    }

    private var isApplicationDialog = false

    /**
     * 这个事件用于处理移动、自定义点击或者其它事情，return true可以保证onclick事件失效
     */
    private val touchListener = OnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> floatEventDown(event)
            MotionEvent.ACTION_MOVE -> floatEventMove(event)
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> floatEventUp()
        }
        true
    }

    var valueAnimator: ValueAnimator? = null
    private var isExpanded = false

    private var logoView: View? = null
    private var rightView: View? = null
    private var leftView: View? = null

    private var mGetViewCallback: GetViewCallback? = null


    fun getContext(): Context {
        return mActivity
    }

    class FloatDialogImp : BaseFloatDialog {
        constructor(context: Context, getViewCallback: GetViewCallback?) : super(context, getViewCallback)

        constructor(context: Context) : super(context)

        override fun getLeftView(inflater: LayoutInflater?, touchListener: OnTouchListener?): View? {
            return null
        }

        override fun getRightView(inflater: LayoutInflater?, touchListener: OnTouchListener?): View? {
            return null
        }

        override fun getLogoView(inflater: LayoutInflater?): View? {
            return null
        }

        override fun resetLogoViewSize(hintLocation: Int, logoView: View?) {
        }

        override fun dragLogoViewOffset(logoView: View?, isDraging: Boolean, isResetPosition: Boolean, offset: Float) {
        }

        override fun shrinkLeftLogoView(logoView: View?) {
        }

        override fun shrinkRightLogoView(logoView: View?) {
        }

        override fun leftViewOpened(leftView: View?) {
        }

        override fun rightViewOpened(rightView: View?) {
        }

        override fun leftOrRightViewClosed(logoView: View?) {
        }

        override fun onDestroyed() {
        }
    }

    protected constructor(context: Context, getViewCallback: GetViewCallback?) : this(context) {
        this.mGetViewCallback = getViewCallback
        requireNotNull(mGetViewCallback) { "GetViewCallback cound not be null!" }
    }

    private fun initFloatView() {
        val inflater = LayoutInflater.from(mActivity)
        logoView = if (mGetViewCallback == null) getLogoView(inflater) else mGetViewCallback!!.getLogoView(inflater)
        leftView = if (mGetViewCallback == null) getLeftView(inflater, touchListener) else mGetViewCallback!!.getLeftView(inflater, touchListener)
        rightView = if (mGetViewCallback == null) getRightView(inflater, touchListener) else mGetViewCallback!!.getRightView(inflater, touchListener)

        requireNotNull(logoView) { "Must impl GetViewCallback or impl " + javaClass.simpleName + "and make getLogoView() not return null !" }

        logoView!!.setOnTouchListener(touchListener) //恢复touch事件
    }

    /**
     * 初始化 隐藏悬浮球的定时器
     */
    private fun initTimer() {
        mHideTimer = object : CountDownTimer(2000, 10) {
            //悬浮窗超过5秒没有操作的话会自动隐藏
            override fun onTick(millisUntilFinished: Long) {
                if (isExpanded) {
                    mHideTimer!!.cancel()
                }
            }

            override fun onFinish() {
                if (isExpanded) {
                    mHideTimer!!.cancel()
                    return
                }
                if (!isDrag) {
                    if (mHintLocation == LEFT) {
                        if (mGetViewCallback == null) {
                            shrinkLeftLogoView(logoView)
                        } else {
                            mGetViewCallback!!.shrinkLeftLogoView(logoView)
                        }
                    } else {
                        if (mGetViewCallback == null) {
                            shrinkRightLogoView(logoView)
                        } else {
                            mGetViewCallback!!.shrinkRightLogoView(logoView)
                        }
                    }
                }
            }
        }
    }


    /**
     * 初始化悬浮球 window
     */
    private fun initFloatWindow() {
        wmParams = WindowManager.LayoutParams()
        if (mActivity is Activity) {
            val activity: Activity = mActivity as Activity
            wManager = activity.getWindowManager()
            //类似dialog，寄托在activity的windows上,activity关闭时需要关闭当前float
            wmParams!!.type = WindowManager.LayoutParams.TYPE_APPLICATION
            isApplicationDialog = true
        } else {
            wManager = mActivity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            //判断状态栏是否显示 如果不显示则statusBarHeight为0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //在android7.1以上系统需要使用TYPE_PHONE类型 配合运行时权限
                wmParams!!.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //在android7.1以上系统需要使用TYPE_PHONE类型 配合运行时权限
                wmParams!!.type = WindowManager.LayoutParams.TYPE_PHONE
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                wmParams!!.type = WindowManager.LayoutParams.TYPE_TOAST
            } else {
                wmParams!!.type = WindowManager.LayoutParams.TYPE_PHONE
            }
            isApplicationDialog = false
        }
        mScreenWidth = wManager!!.defaultDisplay.width
        val screenHeight = wManager!!.defaultDisplay.height
        wmParams!!.format = PixelFormat.RGBA_8888
        wmParams!!.gravity = Gravity.LEFT or Gravity.TOP
        wmParams!!.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        mHintLocation = getSetting(LOCATION_X, mDefaultLocation)
        val defaultY = ((screenHeight) / 2) / 3
        val y = getSetting(LOCATION_Y, defaultY)
        if (mHintLocation == LEFT) {
            wmParams!!.x = 0
        } else {
            wmParams!!.x = mScreenWidth
        }
        if (y != 0 && y != defaultY) {
            wmParams!!.y = y
        } else {
            wmParams!!.y = defaultY
        }
        wmParams!!.alpha = 1f
        wmParams!!.width = WindowManager.LayoutParams.WRAP_CONTENT
        wmParams!!.height = WindowManager.LayoutParams.WRAP_CONTENT
    }

    /**
     * 悬浮窗touch事件的 down 事件
     */
    private fun floatEventDown(event: MotionEvent) {
        isDrag = false
        mHideTimer!!.cancel()

        if (mGetViewCallback == null) {
            resetLogoViewSize(mHintLocation, logoView)
        } else {
            mGetViewCallback!!.resetLogoViewSize(mHintLocation, logoView)
        }

        mXInView = event.x
        mYinView = event.y
        mXDownInScreen = event.rawX
        mYDownInScreen = event.rawY
        mXInScreen = event.rawX
        mYInScreen = event.rawY
    }


    /**
     * 悬浮窗touch事件的 move 事件
     */
    private fun floatEventMove(event: MotionEvent) {
        mXInScreen = event.rawX
        mYInScreen = event.rawY


        //连续移动的距离超过3则更新一次视图位置
        if (abs((mXInScreen - mXDownInScreen).toDouble()) > logoView!!.width / 4 || abs((mYInScreen - mYDownInScreen).toDouble()) > logoView!!.width / 4) {
            wmParams!!.x = (mXInScreen - mXInView).toInt()
            wmParams!!.y = (mYInScreen - mYinView).toInt() - logoView!!.height / 2
            updateViewPosition() // 手指移动的时候更新小悬浮窗的位置
            val a = (mScreenWidth / 2).toDouble()
            val offset = ((a - (abs(wmParams!!.x - a))) / a).toFloat()
            if (mGetViewCallback == null) {
                dragLogoViewOffset(logoView, isDrag, false, offset)
            } else {
                mGetViewCallback!!.dragingLogoViewOffset(logoView, isDrag, false, offset)
            }
        } else {
            isDrag = false
            //            logoView.setDrag(false, 0, true);
            if (mGetViewCallback == null) {
                dragLogoViewOffset(logoView, false, true, 0f)
            } else {
                mGetViewCallback!!.dragingLogoViewOffset(logoView, false, true, 0f)
            }
        }
    }

    /**
     * 悬浮窗touch事件的 up 事件
     */
    private fun floatEventUp() {
        mHintLocation = if (mXInScreen < mScreenWidth / 2) {   //在左边
            LEFT
        } else {                   //在右边
            RIGHT
        }


        valueAnimator = ValueAnimator.ofInt(64)
        valueAnimator?.run {
            setInterpolator(mLinearInterpolator)
            setDuration(1000)
            addUpdateListener(AnimatorUpdateListener { animation ->
                mResetLocationValue = animation.animatedValue as Int
                mHandler.post(updatePositionRunnable)
            })

            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                }

                override fun onAnimationEnd(animation: Animator) {
                    if (abs(wmParams!!.x.toDouble()) < 0) {
                        wmParams!!.x = 0
                    } else if (abs(wmParams!!.x.toDouble()) > mScreenWidth) {
                        wmParams!!.x = mScreenWidth
                    }
                    updateViewPosition()
                    isDrag = false
                    if (mGetViewCallback == null) {
                        dragLogoViewOffset(logoView, false, true, 0f)
                    } else {
                        mGetViewCallback!!.dragingLogoViewOffset(logoView, false, true, 0f)
                    }
                    mHideTimer!!.start()
                }

                override fun onAnimationCancel(animation: Animator) {
                    if (abs(wmParams!!.x.toDouble()) < 0) {
                        wmParams!!.x = 0
                    } else if (abs(wmParams!!.x.toDouble()) > mScreenWidth) {
                        wmParams!!.x = mScreenWidth
                    }

                    updateViewPosition()
                    isDrag = false
                    if (mGetViewCallback == null) {
                        dragLogoViewOffset(logoView, false, true, 0f)
                    } else {
                        mGetViewCallback!!.dragingLogoViewOffset(logoView, false, true, 0f)
                    }
                    mHideTimer!!.start()
                }

                override fun onAnimationRepeat(animation: Animator) {
                }
            })
            if (!isRunning()) {
                start()
            }
        }


        //        //这里需要判断如果如果手指所在位置和logo所在位置在一个宽度内则不移动,
        if (abs((mXInScreen - mXDownInScreen).toDouble()) > logoView!!.width / 5 || abs((mYInScreen - mYDownInScreen).toDouble()) > logoView!!.height / 5) {
            isDrag = false
        } else {
            openMenu()
        }
    }

    /**
     * 手指离开屏幕后 用于恢复 悬浮球的 logo 的左右位置
     */
    private val updatePositionRunnable = Runnable {
        isDrag = true
        checkPosition()
    }


    init {
        initFloatWindow()
        initTimer()
        initFloatView()
    }


    /**
     * 用于检查并更新悬浮球的位置
     */
    private fun checkPosition() {
        if (wmParams!!.x > 0 && wmParams!!.x < mScreenWidth) {
            if (mHintLocation == LEFT) {
                wmParams!!.x = wmParams!!.x - mResetLocationValue
            } else {
                wmParams!!.x = wmParams!!.x + mResetLocationValue
            }
            updateViewPosition()
            val a = (mScreenWidth / 2).toDouble()
            val offset = ((a - (abs(wmParams!!.x - a))) / a).toFloat()
            //            logoView.setDrag(isDrag, offset, true);
            if (mGetViewCallback == null) {
                dragLogoViewOffset(logoView, false, true, 0f)
            } else {
                mGetViewCallback!!.dragingLogoViewOffset(logoView, isDrag, true, offset)
            }

            return
        }


        if (abs(wmParams!!.x.toDouble()) < 0) {
            wmParams!!.x = 0
        } else if (abs(wmParams!!.x.toDouble()) > mScreenWidth) {
            wmParams!!.x = mScreenWidth
        }
        if (valueAnimator!!.isRunning) {
            valueAnimator!!.cancel()
        }


        updateViewPosition()
        isDrag = false
    }

    fun show() {
        try {
            if (wManager != null && wmParams != null && logoView != null) {
                wManager!!.addView(logoView, wmParams)
            }
            if (mHideTimer != null) {
                mHideTimer!!.start()
            } else {
                initTimer()
                mHideTimer!!.start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 打开菜单
     */
    private fun openMenu() {
        if (isDrag) return

        if (!isExpanded) {
//            logoView.setDrawDarkBg(false);
            try {
                wManager!!.removeViewImmediate(logoView)
                if (mHintLocation == RIGHT) {
                    wManager!!.addView(rightView, wmParams)
                    if (mGetViewCallback == null) {
                        rightViewOpened(rightView)
                    } else {
                        mGetViewCallback!!.rightViewOpened(rightView)
                    }
                } else {
                    wManager!!.addView(leftView, wmParams)
                    if (mGetViewCallback == null) {
                        leftViewOpened(leftView)
                    } else {
                        mGetViewCallback!!.leftViewOpened(leftView)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            isExpanded = true
            mHideTimer!!.cancel()
        } else {
//            logoView.setDrawDarkBg(true);
            try {
                wManager!!.removeViewImmediate(if (mHintLocation == LEFT) leftView else rightView)
                wManager!!.addView(logoView, wmParams)
                if (mGetViewCallback == null) {
                    leftOrRightViewClosed(logoView)
                } else {
                    mGetViewCallback!!.leftOrRightViewClosed(logoView)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            isExpanded = false
            mHideTimer!!.start()
        }
    }


    /**
     * 更新悬浮窗在屏幕中的位置。
     */
    private fun updateViewPosition() {
        isDrag = true
        try {
            if (!isExpanded) {
                if (wmParams!!.y - logoView!!.height / 2 <= 0) {
                    wmParams!!.y = 25
                    isDrag = true
                }
                wManager!!.updateViewLayout(logoView, wmParams)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 移除所有悬浮窗 释放资源
     */
    fun dismiss() {
        //记录上次的位置logo的停放位置，以备下次恢复
        saveSetting(LOCATION_X, mHintLocation)
        saveSetting(LOCATION_Y, wmParams!!.y)
        logoView!!.clearAnimation()
        try {
            mHideTimer!!.cancel()
            if (isExpanded) {
                wManager!!.removeViewImmediate(if (mHintLocation == LEFT) leftView else rightView)
            } else {
                wManager!!.removeViewImmediate(logoView)
            }
            isExpanded = false
            isDrag = false
            if (mGetViewCallback == null) {
                onDestroyed()
            } else {
                mGetViewCallback!!.onDestoryed()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected abstract fun getLeftView(inflater: LayoutInflater?, touchListener: OnTouchListener?): View?

    protected abstract fun getRightView(inflater: LayoutInflater?, touchListener: OnTouchListener?): View?

    protected abstract fun getLogoView(inflater: LayoutInflater?): View?

    protected abstract fun resetLogoViewSize(hintLocation: Int, logoView: View?) //logo恢复原始大小

    protected abstract fun dragLogoViewOffset(logoView: View?, isDraging: Boolean, isResetPosition: Boolean, offset: Float)

    protected abstract fun shrinkLeftLogoView(logoView: View?) //logo左边收缩

    protected abstract fun shrinkRightLogoView(logoView: View?) //logo右边收缩

    protected abstract fun leftViewOpened(leftView: View?) //左菜单打开

    protected abstract fun rightViewOpened(rightView: View?) //右菜单打开

    protected abstract fun leftOrRightViewClosed(logoView: View?)

    protected abstract fun onDestroyed()

    interface GetViewCallback {
        fun getLeftView(inflater: LayoutInflater?, touchListener: OnTouchListener?): View?

        fun getRightView(inflater: LayoutInflater?, touchListener: OnTouchListener?): View?

        fun getLogoView(inflater: LayoutInflater?): View?


        fun resetLogoViewSize(hintLocation: Int, logoView: View?) //logo恢复原始大小

        fun dragingLogoViewOffset(logoView: View?, isDraging: Boolean, isResetPosition: Boolean, offset: Float) //logo正被拖动，或真在恢复原位

        fun shrinkLeftLogoView(logoView: View?) //logo左边收缩

        fun shrinkRightLogoView(logoView: View?) //logo右边收缩

        fun leftViewOpened(leftView: View?) //左菜单打开

        fun rightViewOpened(rightView: View?) //右菜单打开

        fun leftOrRightViewClosed(logoView: View?)

        fun onDestoryed()
    }

    /**
     * 用于保存悬浮球的位置记录
     *
     * @param key          String
     * @param defaultValue int
     * @return int
     */
    private fun getSetting(key: String, defaultValue: Int): Int {
        try {
            val sharedata = mActivity.getSharedPreferences("floatLogo", 0)
            return sharedata.getInt(key, defaultValue)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return defaultValue
    }

    /**
     * 用于保存悬浮球的位置记录
     *
     * @param key   String
     * @param value int
     */
    fun saveSetting(key: String?, value: Int) {
        try {
            val sharedata = mActivity.getSharedPreferences("floatLogo", 0).edit()
            sharedata.putInt(key, value)
            sharedata.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        /**
         * 悬浮球 坐落 左 右 标记
         */
        const val LEFT: Int = 0
        const val RIGHT: Int = 1

        /**
         * 记录 logo 停放的位置，以备下次恢复
         */
        private const val LOCATION_X = "hintLocation"
        private const val LOCATION_Y = "locationY"
    }
}