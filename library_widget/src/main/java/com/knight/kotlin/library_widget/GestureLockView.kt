package com.knight.kotlin.library_widget

import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.knight.kotlin.library_util.toast

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.kotlin.library_widget
 * @ClassName:      GestureLockView
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/23 11:10 上午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/23 11:10 上午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

class GestureLockView : View {
    private val DEFAULT_ROW = 3

    private val DEFAULT_COL = 3
    private val PORTRAIT = 1
    private val LANDSCAPE = 0
    private var mContext: Context = context
    private var dotPaint: Paint? = null
    private var getPaddingLeft = 0
    private var contentWidth = 0
    private var contentHeight = 0
    private var row = 0
    private var col = 0
    private var dotColor = 0
    private var dotRadius = 0
    private var dotPressedColor = 0
    private var lineColor = 0
    private var lineWidth = 0
    private var widthHeightOffset = 0
    private lateinit var dotsRegion: Array<Array<Region>>
    private var globalRegion: Region? = null
    private lateinit var dotsStatus: Array<IntArray>
    private var tmpPath: Path? = null
    private lateinit var dotsPos: Array<Array<Point>>
    private var currentPoint: Point? = null

    /**
     * 安全模式,不显示按下的点和手指移动路径
     */
    private var securityMode = false
    private var linePaint: Paint? = null
    private var linePath: Path? = null

    private var lastPoint: Point? = null
    private var password: StringBuilder? = null
    private var dotPressedRadius = 0
    private var vibrator: Vibrator? = null
    private var isTouching = false
    private var minLength = 0
    private var onCheckPasswordListener: OnCheckPasswordListener? =
        null
    private var mOnSetPasswordListener: OnSetPasswordListener? =
        null
    private val ERROR = 1
    private val NORMAL = 2
    private var realLineColor = 0
    private var realDotPressedColor = 0
    private var action: Runnable? = null
    private var STATUS = NORMAL
    private var setOneGesturePassword = false
    private var setTwoGesturePassword = false

    interface OnSetPasswordListener {
        fun setOneGesturePassword(onePassword: String?)
        fun setTwoGesturePassword(twoPassword: String?)
        fun setOnCheckPassword(passwd: String?): Boolean
        fun setError(errorMsg: String?)
    }
    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet? = null,defAttrStyle: Int = 0)
            : super(context, attributeSet,defAttrStyle) {
        init(attributeSet,defAttrStyle)
    }

    /**
     * 初始化一些属性
     *
     */
    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.GestureLockView, defStyle, 0
        )
        col = a.getInt(R.styleable.GestureLockView_row, DEFAULT_ROW)
        row = a.getInt(R.styleable.GestureLockView_col, DEFAULT_COL)
        dotColor = a.getColor(
            R.styleable.GestureLockView_dot_color,
            ContextCompat.getColor(mContext!!, R.color.widget_gesturelock_dot_color)
        )
        dotRadius = a.getDimensionPixelSize(
            R.styleable.GestureLockView_dot_radius,
            resources.getDimensionPixelSize(R.dimen.widget_gesture_default_dot_radius)
        )
        dotPressedColor = a.getColor(
            R.styleable.GestureLockView_dot_color_pressed,
            ContextCompat.getColor(mContext, R.color.widget_gesturelock_dot_pressed_color)
        )
        lineColor = a.getColor(
            R.styleable.GestureLockView_line_color,
            ContextCompat.getColor(mContext, R.color.widget_gesturelock_line_color)
        )
        lineWidth = a.getDimensionPixelSize(
            R.styleable.GestureLockView_line_width,
            resources.getDimensionPixelSize(R.dimen.widget_gesture_default_line_width)
        )
        securityMode = a.getBoolean(R.styleable.GestureLockView_security_mode, false)
        dotPressedRadius = a.getDimensionPixelSize(
            R.styleable.GestureLockView_dot_pressed_radius,
            resources.getDimensionPixelSize(R.dimen.widget_gesture_default_dot_pressed_radius)
        )
        val vibrateable = a.getBoolean(R.styleable.GestureLockView_vibrate, true)
        minLength = a.getInt(R.styleable.GestureLockView_min_length, 4)
        a.recycle()
        if (vibrateable) {
            vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        dotPaint = Paint()
        dotPaint?.let {
            it.setFlags(Paint.ANTI_ALIAS_FLAG)
            it.setAntiAlias(true)
            it.setStyle(Paint.Style.FILL)
        }

        linePaint = Paint()
        linePaint?.run {
            color = lineColor
            style = Paint.Style.STROKE
            strokeWidth = lineWidth.toFloat()
            isAntiAlias = true
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        }

        dotsRegion = Array<Array<Region>>(row){
            Array(col,init = {
                Region()
            })
        }

        dotsPos = Array<Array<Point>>(row) {
            Array(col,init = {
                Point(0, 0)
            })
        }

//        for (i in 0 until row) {
//            for (j in 0 until col) {
//                dotsRegion[i][j] = Region()
//                dotsPos[i][j] = Point(0, 0)
//            }
//        }
        dotsStatus = Array(row) { IntArray(col) }
        tmpPath = Path()
        currentPoint = Point()
        linePath = Path()
        lastPoint = Point(0, 0)
        password = StringBuilder()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        getPaddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom
        contentWidth = width - paddingLeft - paddingRight
        contentHeight = height - paddingTop - paddingBottom
        widthHeightOffset = Math.abs(contentHeight - contentWidth)
        globalRegion = Region(0, 0, width, bottom)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = 400
        val desiredHeight = 400
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val width: Int = when (widthMode) {
            MeasureSpec.EXACTLY -> {
                widthSize
            }
            MeasureSpec.AT_MOST -> {
                Math.min(desiredWidth, widthSize)
            }
            else -> {
                desiredWidth
            }
        }
        val height: Int = when (heightMode) {
            MeasureSpec.EXACTLY -> {
                heightSize
            }
            MeasureSpec.AT_MOST -> {
                Math.min(desiredHeight, heightSize)
            }
            else -> {
                desiredHeight
            }
        }
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //<editor-fold desc="画最后一个点和手指触摸点之间的连线">
        if ((lastPoint?.x != 0 || lastPoint?.y != 0) //不是初始点
            && !securityMode //不是安全模式
            && password?.length!! > 0
        ) {
            canvas.drawLine(
                lastPoint?.x?.toFloat() ?: 0f,
                lastPoint?.y?.toFloat() ?: 0f,
                currentPoint?.x?.toFloat() ?: 0f,
                currentPoint?.y?.toFloat() ?: 0f,
                linePaint ?: Paint()
            )
        }
        //</editor-fold>
        //<editor-fold desc="画点与点之间的轨迹线">
        if (!securityMode || STATUS == ERROR) {
            canvas.drawPath(linePath ?: Path(), linePaint ?: Paint())
        }
        //</editor-fold>
        for (i in 1..row) {
            for (j in 1..col) {
                //<editor-fold desc="画点">
                var x: Float =
                    getMiniViewSize() / row * i - getMiniViewSize() / row / 2f + paddingLeft
                var y: Float =
                    getMiniViewSize() / col * j - getMiniViewSize() / col / 2f + paddingLeft
                //<editor-fold desc="判断横竖View,保证点的间距相等">
                if (getViewOrientation() == PORTRAIT) {
                    y += widthHeightOffset / 2f
                } else {
                    x += widthHeightOffset / 2f
                }
                //</editor-fold>
                var realDotRadius: Int
                if (dotsStatus[i - 1][j - 1] == 1) {
                    if (!securityMode || STATUS == ERROR) {
                        dotPaint!!.color = realDotPressedColor
                    }
                    realDotRadius =
                        if (dotPressedRadius > getTouchAreaMimiRadius()) getTouchAreaMimiRadius() - 16 else dotPressedRadius
                } else {
                    dotPaint!!.color = dotColor
                    realDotRadius = dotRadius
                }
                canvas.drawCircle(x, y, realDotRadius.toFloat(), dotPaint!!)
                //</editor-fold>
                //<editor-fold desc="存储每个点的坐标">
                dotsPos[i - 1][j - 1]!![x.toInt()] = y.toInt()
                //</editor-fold>
                //<editor-fold desc="设置每个点的触摸区域">
                tmpPath?.reset()
                tmpPath?.addCircle(x, y, getTouchAreaMimiRadius().toFloat(), Path.Direction.CW)
                dotsRegion[i - 1][j - 1]!!.setPath(tmpPath!!, globalRegion!!)
                //</editor-fold>
            }
        }
    }

    /**
     * 获得最小触摸范围的半径
     * <br></br>为了防止触摸范围重叠
     *
     * @return 最小接触范围半径
     */
    private fun getTouchAreaMimiRadius(): Int {
        val max = Math.max(col, row)
        return getMiniViewSize() / max / 2
    }

    /**
     * 获取长宽的最小值
     *
     * @return 获取最小值
     */
    private fun getMiniViewSize(): Int {
        return Math.min(contentHeight, contentWidth)
    }

    /**
     * 获得view形状,是竖屏形状还是横屏形状
     *
     * @return 形状
     */
    private fun getViewOrientation(): Int {
        return if (contentWidth > contentHeight) {
            LANDSCAPE
        } else {
            PORTRAIT
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                dealDown(event)
                true
            }
            MotionEvent.ACTION_MOVE -> {
                dealMove(event)
                true
            }
            MotionEvent.ACTION_UP -> {
                resetStatus()
                true
            }
            else -> true
        }
    }

    /**
     * 处理Action Down事件
     *
     * @param event 事件
     */
    private fun dealDown(event: MotionEvent) {
        action?.let { removeCallbacks(it) }
        reset()
        setStatus(NORMAL)
        for (i in 0 until row) {
            for (j in 0 until col) {
                if (dotsRegion[i][j]!!.contains(event.x.toInt(), event.y.toInt())) {
                    if (dotsStatus[i][j] == 0) {
                        linePath!!.moveTo(dotsPos[i][j]!!.x.toFloat(), dotsPos[i][j]!!.y.toFloat())
                        lastPoint!![dotsPos[i][j]!!.x] = dotsPos[i][j]!!.y
                        vibrator()
                        val posString: String = ((j * row + i + 1 + 96).toChar()).toString()
                        if (password!!.indexOf(posString) == -1) {
                            password!!.append(posString)
                        }
                    }
                    dotsStatus[i][j] = 1
                }
            }
        }
    }

    /**
     * 处理Action Move事件
     *
     * @param event 事件
     */
    private fun dealMove(event: MotionEvent) {
        currentPoint!![event.x.toInt()] = event.y.toInt()
        for (i in 0 until row) {
            for (j in 0 until col) {
                if (dotsRegion[i][j]!!.contains(event.x.toInt(), event.y.toInt())) {
                    if (dotsStatus[i][j] == 0) {
                        //只有这个点没被选中过,才能链接到这个点
                        linePath?.lineTo(dotsPos[i][j]?.x?.toFloat() ?: 0f, dotsPos[i][j]?.y?.toFloat() ?: 0f)
                        lastPoint!![dotsPos[i][j]!!.x] = dotsPos[i][j]!!.y
                        vibrator()
                        isTouching = true
                        val posString: String = ((j * row + i + 1 + 96).toChar()).toString()
                        if (password?.indexOf(posString) == -1) {
                            password?.append(posString)
                        }
                    }
                    dotsStatus[i][j] = 1
                } else {
                    isTouching = false
                }
            }
        }
        invalidate()
    }

    /**
     * 恢复状态
     */
    private fun resetStatus() {
        currentPoint!![lastPoint!!.x] = lastPoint!!.y
        if (password.toString().length < minLength) {
            //密码长度小于最小长度
            setStatus(ERROR)
            onCheckPasswordListener?.onError(mContext.getString(R.string.widget_min_gesture_password))
            mOnSetPasswordListener?.setError(mContext.getString(R.string.widget_min_gesture_password))
        } else {
            //这里判断是录入还是校验密码
            if (mOnSetPasswordListener != null) {
                if (setOneGesturePassword && mOnSetPasswordListener?.setOnCheckPassword(password.toString()) == true) {
                    setTwoGesturePassword = true
                    mOnSetPasswordListener?.setTwoGesturePassword(password.toString())
                    toast(R.string.widget_gesture_password_success)
                } else {
                    if (setOneGesturePassword) {
                        mOnSetPasswordListener?.setError(mContext.getString(R.string.widget_gesture_password_inequality))
                    }
                }
                if (!setOneGesturePassword) {
                    setOneGesturePassword = true
                    mOnSetPasswordListener?.setOneGesturePassword(password.toString())
                    toast(R.string.widget_gesture_password_again)
                }
            } else {
                //密码长度符合要求
                if (onCheckPasswordListener != null && onCheckPasswordListener?.onCheckPassword(
                        password.toString()) == true
                ) {

                        onCheckPasswordListener?.onSuccess()

                } else {
                    setStatus(ERROR)

                        onCheckPasswordListener?.onError(mContext.getString(R.string.widget_gesture_password_error))

                }
            }
        }
        password?.delete(0, password!!.length)
        invalidate()
        //抬起手指后过一秒钟再清除输入轨迹
        action = Runnable()
        //抬起手指后过一秒钟再清除输入轨迹
        {
            reset()
            invalidate()
        }
        postDelayed(action, 1000)
    }

    /**
     * 重置一些状态
     */
    private fun reset() {
        for (i in 0 until row) {
            for (j in 0 until col) {
                dotsStatus[i][j] = 0
            }
        }
        linePath?.rewind()
        linePath?.moveTo(currentPoint?.x?.toFloat() ?:0f, currentPoint?.y?.toFloat() ?: 0f)
        password?.delete(0, password?.length ?: 0)
    }

    /**
     * 设置当前状态<br></br>
     * `NORMAL` 正常输入状态<br></br>
     * `SUCCESS` 密码正确状态<br></br>
     * `ERROR` 密码输入错误状态
     *
     * @param i 状态
     */
    private fun setStatus(i: Int) {
        if (ERROR == i) {
            realLineColor = Color.parseColor("#66FF0000")
            realDotPressedColor = Color.parseColor("#FF0000")
        } else if (NORMAL == i) {
            realLineColor = lineColor
            realDotPressedColor = dotPressedColor
        }
        STATUS = i
        linePaint?.color = realLineColor
    }

    /**
     * 震动,只有第一次进入点的触摸范围时才震动
     */
    private fun vibrator() {
        if (!isTouching) {
            if (vibrator != null) {
                if (Build.VERSION.SDK_INT >= 26) {
                    vibrator?.vibrate(VibrationEffect.createOneShot(30, 10))
                } else {
                    vibrator?.vibrate(30)
                }
            }
        }
    }

    fun getRow(): Int {
        return row
    }

    fun setRow(row: Int) {
        this.row = row
        invalidate()
    }

    fun getCol(): Int {
        return col
    }

    fun setCol(col: Int) {
        this.col = col
        invalidate()
    }

    fun getDotColor(): Int {
        return dotColor
    }

    fun setDotColor(dotColor: Int) {
        this.dotColor = dotColor
        invalidate()
    }

    fun getDotRadius(): Int {
        return dotRadius
    }

    fun setDotRadius(dotRadius: Int) {
        this.dotRadius = dotRadius
        invalidate()
    }

    fun getDotPressedColor(): Int {
        return dotPressedColor
    }

    fun setDotPressedColor(dotPressedColor: Int) {
        this.dotPressedColor = dotPressedColor
        invalidate()
    }

    fun getLineColor(): Int {
        return lineColor
    }

    fun setLineColor(lineColor: Int) {
        this.lineColor = lineColor
        invalidate()
    }

    fun getLineWidth(): Int {
        return lineWidth
    }

    fun setLineWidth(lineWidth: Int) {
        this.lineWidth = lineWidth
        invalidate()
    }

    fun isSecurityMode(): Boolean {
        return securityMode
    }

    fun setSecurityMode(securityMode: Boolean) {
        this.securityMode = securityMode
        invalidate()
    }

    fun getDotPressedRadius(): Int {
        return dotPressedRadius
    }

    fun setDotPressedRadius(dotPressedRadius: Int) {
        this.dotPressedRadius = dotPressedRadius
        invalidate()
    }

    fun getMinLength(): Int {
        return minLength
    }

    fun setMinLength(minLength: Int) {
        this.minLength = minLength
        invalidate()
    }

    fun getOnCheckPasswordListener(): OnCheckPasswordListener? {
        return onCheckPasswordListener
    }

    fun setOnCheckPasswordListener(onCheckPasswordListener: OnCheckPasswordListener?) {
        this.onCheckPasswordListener = onCheckPasswordListener
    }


    fun setPasswordListener(mOnSetPasswordListener: OnSetPasswordListener?) {
        this.mOnSetPasswordListener = mOnSetPasswordListener
    }

    /**
     *
     * 检查密码
     */
    interface OnCheckPasswordListener {
        /**
         * 手势密码输入完成时回调,验证密码
         * <br></br>这里只做密码校验
         *
         * @param passwd 输入完成的手势密码
         * @return `true`:输入的手势密码和存储在本地的密码一致
         * <br></br>反之`false`
         */
        fun onCheckPassword(passwd: String?): Boolean

        /**
         * 当密码校验成功时的回调
         */
        fun onSuccess()

        /**
         * 当密码校验失败时的回调
         */
        fun onError(errorMsg: String?)
    }

}