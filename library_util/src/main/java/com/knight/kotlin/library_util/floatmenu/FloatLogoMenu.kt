package com.knight.kotlin.library_util.floatmenu

import android.animation.Animator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import com.knight.kotlin.library_base.util.dp2px
import kotlin.math.abs


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/11 14:57
 * @descript:
 */
class FloatLogoMenu private constructor(builder: Builder) {
    /**
     * 记录系统状态栏的高度
     */
    private var mStatusBarHeight = 0

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
     * 带透明度动画、旋转、放大的悬浮球
     */
    private var mFloatLogo: DotImageView? = null


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

    /**
     * 手指离开屏幕后 用于恢复 悬浮球的 logo 的左右位置
     */
    private val updatePositionRunnable = Runnable {
        isDrag = true
        checkPosition()
    }

    /**
     * 这个事件不做任何事情、直接return false则 onclick 事件生效
     */
    private val mDefaultOnTouchListerner = OnTouchListener { v, event ->
        isDrag = false
        false
    }

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


    /**
     * 菜单背景颜色
     */
    private var mBackMenuColor = -0x1b1c1f

    /**
     * 是否绘制红点数字
     */
    private val mDrawRedPointNum: Boolean


    /**
     * 是否绘制圆形菜单项，false绘制方形
     */
    private val mCircleMenuBg: Boolean


    /**
     * R.drawable.yw_game_logo
     *
     * @param floatItems
     */
    private val mLogoRes: Bitmap?

    /**
     * 用于显示在 mActivity 上的 mActivity
     */
    private val mActivity: Context?

    /**
     * 菜单 点击、关闭 监听
     */
    private val mOnMenuClickListener: FloatMenuView.OnMenuClickListener


    /**
     * 停靠默认位置
     */
    private var mDefaultLocation = RIGHT


    /**
     * 悬浮窗 坐落 位置
     */
    private var mHintLocation = mDefaultLocation


    /**
     * 用于记录菜单项内容
     */
    private var mFloatItems: MutableList<FloatItem> = ArrayList()

    private var rootViewRight: LinearLayout? = null

    private var rootView: LinearLayout? = null

    private var valueAnimator: ValueAnimator? = null

    private var isExpanded = false

    private val mBackground: Drawable?


    init {
        mBackMenuColor = builder.mBackMenuColor
        mDrawRedPointNum = builder.mDrawRedPointNum
        mCircleMenuBg = builder.mCircleMenuBg
        mLogoRes = builder.mLogoRes
        mActivity = builder.mActivity
        mOnMenuClickListener = builder.mOnMenuClickListener!!
        mDefaultLocation = builder.mDefaultLocation
        mFloatItems = builder.mFloatItems
        mBackground = builder.mDrawable

        //        if (mActivity == null || mActivity.isFinishing() || mActivity.getWindowManager() == null) {
//            throw new IllegalArgumentException("Activity = null, or Activity is isFinishing ,or this Activity`s  token is bad");
//        }
        requireNotNull(mLogoRes) { "No logo found,you can setLogo/showWithLogo to set a FloatLogo " }

        require(!mFloatItems.isEmpty()) { "At least one menu item!" }

        initFloatWindow()
        initTimer()
        initFloat()
    }

    fun setFloatItemList(floatItems: MutableList<FloatItem>) {
        this.mFloatItems = floatItems
        calculateDotNum()
    }

    /**
     * 初始化悬浮球 window
     */
    private fun initFloatWindow() {
        wmParams = WindowManager.LayoutParams()
        if (mActivity is Activity) {
            val activity: Activity? = mActivity as Activity?
            wManager = activity?.getWindowManager()
            //类似dialog，寄托在activity的windows上,activity关闭时需要关闭当前float
            wmParams!!.type = WindowManager.LayoutParams.TYPE_APPLICATION
        } else {
            wManager = mActivity!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
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
        }
        mScreenWidth = wManager!!.defaultDisplay.width
        val screenHeight = wManager!!.defaultDisplay.height

        //判断状态栏是否显示 如果不显示则statusBarHeight为0
        mStatusBarHeight = 25.dp2px()

        wmParams!!.format = PixelFormat.RGBA_8888
        wmParams!!.gravity = Gravity.LEFT or Gravity.TOP
        wmParams!!.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        mHintLocation = getSetting(LOCATION_X, mDefaultLocation)
        val defaultY = ((screenHeight - mStatusBarHeight) / 2) / 3
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
     * 初始化悬浮球
     */
    private fun initFloat() {
        generateLeftLineLayout()
        generateRightLineLayout()
        mFloatLogo = DotImageView(mActivity, mLogoRes)
        mFloatLogo!!.layoutParams = WindowManager.LayoutParams(50.dp2px(), 50.dp2px())
        mFloatLogo!!.setDrawNum(mDrawRedPointNum)
        mFloatLogo!!.setBgColor(mBackMenuColor)
        mFloatLogo!!.setDrawDarkBg(true)
        calculateDotNum()
        floatBtnEvent()
        try {
            wManager!!.addView(mFloatLogo, wmParams)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun generateLeftLineLayout() {
        val floatLogo = DotImageView(mActivity, mLogoRes)
        floatLogo.layoutParams = WindowManager.LayoutParams(50.dp2px(), 50.dp2px())
        floatLogo.setDrawNum(mDrawRedPointNum)
        floatLogo.setDrawDarkBg(false)

        rootView = LinearLayout(mActivity)
        rootView!!.orientation = LinearLayout.HORIZONTAL
        rootView!!.gravity = Gravity.CENTER
        rootView!!.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 50.dp2px())

        rootView!!.setBackgroundDrawable(mBackground)


        val mFloatMenuView = FloatMenuView.Builder(mActivity!!)
            .setFloatItems(mFloatItems)
            .setBackgroundColor(Color.TRANSPARENT)
            .setCicleBg(mCircleMenuBg)
            .setStatus(FloatMenuView.STATUS_LEFT)
            .setMenuBackgroundColor(Color.TRANSPARENT)
            .drawNum(mDrawRedPointNum)
            .create()
        setMenuClickListener(mFloatMenuView)

        rootView!!.addView(floatLogo)
        rootView!!.addView(mFloatMenuView)


        floatLogo.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (isExpanded) {
                    try {
                        wManager!!.removeViewImmediate(rootView)
                        wManager!!.addView(this@FloatLogoMenu.mFloatLogo, wmParams)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    isExpanded = false
                }
            }
        })
    }

    private fun generateRightLineLayout() {
        val floatLogo = DotImageView(mActivity, mLogoRes)
        floatLogo.layoutParams = WindowManager.LayoutParams(50.dp2px(), 50.dp2px())
        floatLogo.setDrawNum(mDrawRedPointNum)
        floatLogo.setDrawDarkBg(false)

        floatLogo.setOnClickListener(object :View.OnClickListener {
            override fun onClick(v: View?) {
                if (isExpanded) {
                    try {
                        wManager!!.removeViewImmediate(rootViewRight)
                        wManager!!.addView(this@FloatLogoMenu.mFloatLogo, wmParams)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    isExpanded = false
                }
            }
        })

        rootViewRight = LinearLayout(mActivity)
        rootViewRight!!.orientation = LinearLayout.HORIZONTAL
        rootViewRight!!.gravity = Gravity.CENTER
        rootViewRight!!.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,50.dp2px())


        rootViewRight!!.setBackgroundDrawable(mBackground)


        val mFloatMenuView = FloatMenuView.Builder(mActivity!!)
            .setFloatItems(mFloatItems)
            .setBackgroundColor(Color.TRANSPARENT)
            .setCicleBg(mCircleMenuBg)
            .setStatus(FloatMenuView.STATUS_RIGHT)
            .setMenuBackgroundColor(Color.TRANSPARENT)
            .drawNum(mDrawRedPointNum)
            .create()
        setMenuClickListener(mFloatMenuView)

        rootViewRight!!.addView(mFloatMenuView)
        rootViewRight!!.addView(floatLogo)
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
                        mFloatLogo!!.setStatus(DotImageView.HIDE_LEFT)
                        mFloatLogo!!.setDrawDarkBg(true)
                    } else {
                        mFloatLogo!!.setStatus(DotImageView.HIDE_RIGHT)
                        mFloatLogo!!.setDrawDarkBg(true)
                    }
                    //                    mFloatLogo.setOnTouchListener(mDefaultOnTouchListerner);//把onClick事件分发下去，防止onclick无效
                }
            }
        }
    }


    /**
     * 用于 拦截 菜单项的 关闭事件，以方便开始 隐藏定时器
     *
     * @param mFloatMenuView
     */
    private fun setMenuClickListener(mFloatMenuView: FloatMenuView) {
        mFloatMenuView.setOnMenuClickListener(object : FloatMenuView.OnMenuClickListener {
            override fun onItemClick(position: Int, title: String?) {
                mOnMenuClickListener.onItemClick(position, title)
            }

            override fun dismiss() {
                mFloatLogo!!.setDrawDarkBg(true)
                mOnMenuClickListener.dismiss()
                mHideTimer!!.start()
            }
        })
    }


    /**
     * 悬浮窗的点击事件和touch事件的切换
     */
    private fun floatBtnEvent() {
        //这里的onClick只有 touchListener = mDefaultOnTouchListener 才会触发
//        mFloatLogo.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!isDrag) {
//                    if (mFloatLogo.getStatus() != DotImageView.NORMAL) {
//                        mFloatLogo.setBitmap(mLogoRes);
//                        mFloatLogo.setStatus(DotImageView.NORMAL);
//                        if (!mFloatLogo.mDrawDarkBg) {
//                            mFloatLogo.setDrawDarkBg(true);
//                        }
//                    }
//                    mFloatLogo.setOnTouchListener(touchListener);
//                    mHideTimer.start();
//                }
//            }
//        });

        mFloatLogo!!.setOnTouchListener(touchListener) //恢复touch事件
    }

    /**
     * 悬浮窗touch事件的 down 事件
     */
    private fun floatEventDown(event: MotionEvent) {
        isDrag = false
        mHideTimer!!.cancel()
        if (mFloatLogo!!.getStatus() != DotImageView.NORMAL) {
            mFloatLogo!!.setStatus(DotImageView.NORMAL)
        }
        if (!mFloatLogo!!.mDrawDarkBg) {
            mFloatLogo!!.setDrawDarkBg(true)
        }
        if (mFloatLogo!!.getStatus() != DotImageView.NORMAL) {
            mFloatLogo!!.setStatus(DotImageView.NORMAL)
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
        if (abs((mXInScreen - mXDownInScreen).toDouble()) > mFloatLogo!!.width / 4 || abs((mYInScreen - mYDownInScreen).toDouble()) > mFloatLogo!!.width / 4) {
            wmParams!!.x = (mXInScreen - mXInView).toInt()
            wmParams!!.y = (mYInScreen - mYinView).toInt() - mFloatLogo!!.height / 2
            updateViewPosition() // 手指移动的时候更新小悬浮窗的位置
            val a = (mScreenWidth / 2).toDouble()
            val offset = ((a - (abs(wmParams!!.x - a))) / a).toFloat()
            mFloatLogo!!.setDrag(isDrag, offset, false)
        } else {
            isDrag = false
            mFloatLogo!!.setDrag(false, 0f, true)
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
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofInt(64)
            valueAnimator?.setInterpolator(mLinearInterpolator)
            valueAnimator?.setDuration(1000)
            valueAnimator?.addUpdateListener(AnimatorUpdateListener { animation ->
                mResetLocationValue = animation.animatedValue as Int
                mHandler.post(updatePositionRunnable)
            })

            valueAnimator?.addListener(object : Animator.AnimatorListener {
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
                    mFloatLogo!!.setDrag(false, 0f, true)
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
                    mFloatLogo!!.setDrag(false, 0f, true)
                    mHideTimer!!.start()
                }

                override fun onAnimationRepeat(animation: Animator) {
                }
            })
        }
        if (!valueAnimator!!.isRunning) {
            valueAnimator!!.start()
        }

        //        //这里需要判断如果如果手指所在位置和logo所在位置在一个宽度内则不移动,
        if (abs((mXInScreen - mXDownInScreen).toDouble()) > mFloatLogo!!.width / 5 || abs((mYInScreen - mYDownInScreen).toDouble()) > mFloatLogo!!.height / 5) {
            isDrag = false
        } else {
            openMenu()
        }
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
            mFloatLogo!!.setDrag(isDrag, offset, true)
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


    /**
     * 打开菜单
     */
    private fun openMenu() {
        if (isDrag) return

        if (!isExpanded) {
            mFloatLogo!!.setDrawDarkBg(false)
            try {
                wManager!!.removeViewImmediate(mFloatLogo)
                if (mHintLocation == RIGHT) {
                    wManager!!.addView(rootViewRight, wmParams)
                } else {
                    wManager!!.addView(rootView, wmParams)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            isExpanded = true
            mHideTimer!!.cancel()
        } else {
            mFloatLogo!!.setDrawDarkBg(true)
            if (isExpanded) {
                try {
                    wManager!!.removeViewImmediate(if (mHintLocation == LEFT) rootView else rootViewRight)
                    wManager!!.addView(mFloatLogo, wmParams)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                isExpanded = false
            }
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
                if (wmParams!!.y - mFloatLogo!!.height / 2 <= 0) {
                    wmParams!!.y = mStatusBarHeight
                    isDrag = true
                }
                wManager!!.updateViewLayout(mFloatLogo, wmParams)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun show() {
        try {
            if (wManager != null && wmParams != null && mFloatLogo != null) {
                wManager!!.addView(mFloatLogo, wmParams)
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
     * 关闭菜单
     */
    fun hide() {
        destroyFloat()
    }


    /**
     * 移除所有悬浮窗 释放资源
     */
    fun destroyFloat() {
        //记录上次的位置logo的停放位置，以备下次恢复
        saveSetting(LOCATION_X, mHintLocation)
        saveSetting(LOCATION_Y, wmParams!!.y)
        mFloatLogo!!.clearAnimation()
        try {
            mHideTimer!!.cancel()
            if (isExpanded) {
                wManager!!.removeViewImmediate(if (mHintLocation == LEFT) rootView else rootViewRight)
            } else {
                wManager!!.removeViewImmediate(mFloatLogo)
            }
            isExpanded = false
            isDrag = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 计算总红点数
     */
    private fun calculateDotNum() {
        var dotNum = 0
        for (floatItem in mFloatItems) {
            if (!TextUtils.isEmpty(floatItem.dotNum)) {
                val num = floatItem.dotNum!!.toInt()
                dotNum = dotNum + num
            }
        }
        mFloatLogo!!.setDrawNum(mDrawRedPointNum)
        setDotNum(dotNum)
    }

    /**
     * 绘制悬浮球的红点
     *
     * @param dotNum d
     */
    private fun setDotNum(dotNum: Int) {
        mFloatLogo!!.setDotNum(dotNum, object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                if (!isDrag) {
                    mHideTimer!!.start()
                }
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
    }

    /**
     * 用于暴露给外部判断是否包含某个菜单项
     *
     * @param menuLabel string
     * @return boolean
     */
    fun hasMenu(menuLabel: String?): Boolean {
        for (menuItem in mFloatItems) {
            if (TextUtils.equals(menuItem.title, menuLabel)) {
                return true
            }
        }
        return false
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
            val sharedata = mActivity!!.getSharedPreferences("floatLogo", 0)
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
            val sharedata = mActivity!!.getSharedPreferences("floatLogo", 0).edit()
            sharedata.putInt(key, value)
            sharedata.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    interface OnMenuClickListener {
        fun onMenuExpended(isExpened: Boolean)
    }


    fun setValueAnimator() {
    }

    class Builder {
        internal var mBackMenuColor: Int = 0
        var mDrawRedPointNum: Boolean = false
        var mCircleMenuBg: Boolean = false
        var mLogoRes: Bitmap? = null
        var mDefaultLocation: Int = 0
        var mFloatItems: MutableList<FloatItem> = ArrayList()
        var mActivity: Context? = null
        var mOnMenuClickListener: FloatMenuView.OnMenuClickListener? = null
        var mDrawable: Drawable? = null


        fun setBgDrawable(drawable: Drawable?): Builder {
            mDrawable = drawable
            return this
        }

        fun setFloatItems(mFloatItems: MutableList<FloatItem>): Builder {
            this.mFloatItems = mFloatItems
            return this
        }

        fun addFloatItem(floatItem: FloatItem): Builder {
            mFloatItems.add(floatItem)
            return this
        }

        fun backMenuColor(backColor: Int): Builder {
            mBackMenuColor = backColor
            return this
        }

        fun drawRedPointNum(num: Boolean): Builder {
            mDrawRedPointNum = num
            return this
        }

        fun drawCicleMenuBg(circleMenuBg: Boolean): Builder {
            mCircleMenuBg = circleMenuBg
            return this
        }

        fun logo(logoRes: Bitmap?): Builder {
            mLogoRes = logoRes
            return this
        }

        fun withActivity(activity: Activity?): Builder {
            mActivity = activity
            return this
        }

        fun withContext(activity: Context?): Builder {
            mActivity = activity
            return this
        }

        fun setOnMenuItemClickListener(onMenuClickListener: FloatMenuView.OnMenuClickListener?): Builder {
            mOnMenuClickListener = onMenuClickListener
            return this
        }

        fun defaultLocation(defaultLocation: Int): Builder {
            mDefaultLocation = defaultLocation
            return this
        }

        fun showWithListener(OnMenuClickListener: FloatMenuView.OnMenuClickListener?): FloatLogoMenu {
            mOnMenuClickListener = OnMenuClickListener
            return FloatLogoMenu(this)
        }

        fun showWithLogo(bitmap: Bitmap?): FloatLogoMenu {
            mLogoRes = bitmap
            return FloatLogoMenu(this)
        }

        fun show(): FloatLogoMenu {
            return FloatLogoMenu(this)
        }
    }


    companion object {
        /**
         * 记录 logo 停放的位置，以备下次恢复
         */
        private const val LOCATION_X = "hintLocation"
        private const val LOCATION_Y = "locationY"

        /**
         * 悬浮球 坐落 左 右 标记
         */
        const val LEFT: Int = 0
        const val RIGHT: Int = 1

        /**
         * 用于记录上次菜单打开的时间，判断时间间隔
         */
        private const val DOUBLE_CLICK_TIME = 0.0

    }
}