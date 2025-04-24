package com.knight.kotlin.module_home.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.annotation.OptIn
import com.knight.kotlin.library_base.ktx.getScreenHeight
import com.knight.kotlin.library_widget.overlaymenu.EasyFloat
import com.knight.kotlin.library_widget.overlaymenu.FloatWindow
import com.knight.kotlin.module_home.R


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/23 13:58
 * @descript:悬浮窗管理
 */
object FloatMenuManager {
    var float: FloatWindow? = null
    var with:FloatWindow.With? =null



    fun hidden() {
        float?.hidden()
    }

    fun show() {
        float?.show()

    }




    fun showDesktop(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.home_float_news_menu, null)
        val close = view!!.findViewById<View>(R.id.iv_float_close)
        val play = view!!.findViewById<ImageView>(R.id.iv_float_play)
        val logo = view!!.findViewById<ImageView>(R.id.iv_float_img)
        close.setOnClickListener {

        }


        play.setOnClickListener {

        }

        float = FloatWindow.With(context, view)
            .setAutoAlign(true)  // 是否自动贴边
            .setModality(false)  // 是否模态窗口（事件是否可穿透当前窗口）
            .setMoveAble(true)   // 是否可拖动
            .setStartLocation(0, (getScreenHeight() * 0.7).toInt()) //设置起始位置
            .setDeskTopWindow(true) //桌面也要显示
            .create()
    }

    //必须显式地声明你愿意使用这个 API，否则会有警告
    @OptIn(androidx.media3.common.util.UnstableApi::class)
    fun showNormal(context: Activity, @LayoutRes layoutId: Int) {
        EasyFloat
            .layout(layoutId)
            .layoutParams(initLayoutParams())
            .listener {
                initListener(it)
            }
            .show(context)


    }




    private fun initLayoutParams(): FrameLayout.LayoutParams {
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.BOTTOM or Gravity.END
        params.setMargins(0, params.topMargin, params.rightMargin, 500)
        return params
    }

    private fun initListener(root: View?) {

    }

    fun destroyFloatMenu() {
        float?.remove()
    }

    fun getFloatWindow(): FloatWindow? {
        return float
    }


    fun animateViewWidth(
        view: View,
        targetWidth: Int,
        duration: Long = 300
    ) {
        // 防止重复动画（标记 key 可以自定义）
        val isAnimating = view.getTag(R.id.home_news_floatmenu_animating_tag) as? Boolean ?: false
        if (isAnimating) return

        val startWidth = view.width
        if (startWidth == targetWidth) return // 不需要动画

        view.setTag(R.id.home_news_floatmenu_animating_tag, true) // 设置动画标记

        val animator = ValueAnimator.ofInt(startWidth, targetWidth)
        animator.addUpdateListener { valueAnimator ->
            val newWidth = valueAnimator.animatedValue as Int
            view.layoutParams.width = newWidth
            view.requestLayout()
        }

        animator.duration = duration
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                view.setTag(R.id.home_news_floatmenu_animating_tag, false)
            }

            override fun onAnimationCancel(animation: Animator) {
                view.setTag(R.id.home_news_floatmenu_animating_tag, false)
            }
        })
        animator.start()
    }
}