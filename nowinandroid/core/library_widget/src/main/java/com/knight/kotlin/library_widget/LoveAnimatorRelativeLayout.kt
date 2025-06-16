package com.knight.kotlin.library_widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import java.util.Random

/**
 * Author:Knight
 * Time:2022/2/21 17:43
 * Description:LoveAnimatorRelativeLayout
 */
class LoveAnimatorRelativeLayout : RelativeLayout {
    private var mContext: Context? = null
    val num = floatArrayOf(-30f, -20f, 0f, 20f, 30f) // 随机心形图片的角度

    interface onCollectListener {
        fun onCollect()
    }


    private var mOnCollectListener: onCollectListener? = null

    fun setOnCollectListener(mOnCollectListener: onCollectListener?) {
        this.mOnCollectListener = mOnCollectListener
    }


    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet? = null, defAttrStyle: Int = 0)
            : super(context, attributeSet, defAttrStyle) {
        mContext = context
    }

    /** 判断触摸时间派发间隔  */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            if (isFastDoubleClick()) {
                // 首先，我们需要在触摸事件中做监听，当有触摸时，创建一个展示心形图片的 ImageView
                val imageView = ImageView(mContext)
                // 设置图片展示的位置，需要在手指触摸的位置上方，即触摸点是心形的下方角的位置。所以我们需要将 ImageView 设置到手指的位置
                val params = LayoutParams(300, 300)
                params.leftMargin = ev.x.toInt() - 150
                params.topMargin = ev.y.toInt() - 300
                imageView.setImageDrawable(mContext?.getDrawable(R.drawable.widget_like_icon))
                imageView.layoutParams = params
                addView(imageView)

                // 设置 imageView 动画
                val animatorSet = AnimatorSet()
                animatorSet.play(
                    scale(imageView, "scaleX", 2f, 0.9f, 100, 0)) // 缩放动画，X轴2倍缩小至0.9倍
                    .with(scale(imageView, "scaleY", 2f, 0.9f, 100, 0)) // 缩放动画，Y轴2倍缩小至0.9倍
                    .with(rotation(imageView, 0, 0, num[Random().nextInt(4)])) // 旋转动画，随机旋转角度num={-30.-20，0，20，30}
                    .with(alpha(imageView, 0f, 1f, 100, 0)) // 渐变透明度动画，透明度从0-1.
                    .with(scale(imageView, "scaleX", 0.9f, 1f, 50, 150)) // 缩放动画，X轴0.9倍缩小至1倍
                    .with(scale(imageView, "scaleY", 0.9f, 1f, 50, 150)) // 缩放动画，Y轴0.9倍缩小至1倍
                    .with(translationY(imageView, 0f, -600f, 800, 400)) // 平移动画，Y轴从0向上移动600单位
                    .with(alpha(imageView, 1f, 0f, 300, 400)) // 透明度动画，从1-0
                    .with(scale(imageView, "scaleX", 1f, 3f, 700, 400)) // 缩放动画，X轴1倍放大至3倍
                    .with(scale(imageView, "scaleY", 1f, 3f, 700, 400)) // 缩放动画，Y轴1倍放大至3倍
                animatorSet.start()

                // 我们不可能无限制的增加 view，在 view 消失之后，需要手动的移除改 imageView
                animatorSet.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        removeViewInLayout(imageView)
                    }
                })
                //回调出去
                mOnCollectListener?.onCollect()
                return true
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
        // return gestureDetector.onTouchEvent(event);
    }

    fun scale(view: View?, propertyName: String?, from: Float, to: Float, time: Long, delayTime: Long
    ): ObjectAnimator? {
        val translation = ObjectAnimator.ofFloat(
            view, propertyName, from, to
        )
        translation.interpolator = LinearInterpolator()
        translation.startDelay = delayTime
        translation.duration = time
        return translation
    }

    fun translationX(view: View?, from: Float, to: Float, time: Long, delayTime: Long
    ): ObjectAnimator? {
        val translation = ObjectAnimator.ofFloat(
            view, "translationX", from, to
        )
        translation.interpolator = LinearInterpolator()
        translation.startDelay = delayTime
        translation.duration = time
        return translation
    }

    fun translationY(view: View?, from: Float, to: Float, time: Long, delayTime: Long
    ): ObjectAnimator? {
        val translation = ObjectAnimator.ofFloat(
            view, "translationY", from, to
        )
        translation.interpolator = LinearInterpolator()
        translation.startDelay = delayTime
        translation.duration = time
        return translation
    }

    fun alpha(view: View?, from: Float, to: Float, time: Long, delayTime: Long): ObjectAnimator? {
        val translation = ObjectAnimator.ofFloat(
            view, "alpha", from, to
        )
        translation.interpolator = LinearInterpolator()
        translation.startDelay = delayTime
        translation.duration = time
        return translation
    }

    fun rotation(view: View?, time: Long, delayTime: Long, vararg values: Float): ObjectAnimator? {
        val rotation = ObjectAnimator.ofFloat(view, "rotation", *values)
        rotation.duration = time
        rotation.startDelay = delayTime
        rotation.interpolator = TimeInterpolator { input -> input }
        return rotation
    }


    /** 判断是否是快速点击  */
    private var lastClickTime: Long = 0
    fun isFastDoubleClick(): Boolean {
        val time = System.currentTimeMillis()
        val timeD = time - lastClickTime
        if (0 < timeD && timeD < 200) {
            return true
        }
        lastClickTime = time
        return false
    }
}