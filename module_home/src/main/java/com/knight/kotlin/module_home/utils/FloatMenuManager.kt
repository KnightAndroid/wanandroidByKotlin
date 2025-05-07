package com.knight.kotlin.module_home.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import com.knight.kotlin.library_base.ktx.getScreenHeight
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
        float?.let {
            it.hidden()
        }

    }

    fun show() {
        float?.let {
            it.show()
        }


    }


    /**
     *
     * 开启悬浮窗
     */
    fun initFloatMenu(context: Context,view:View,showDesktop:Boolean) {
        float = FloatWindow.With(context, view)
            .setAutoAlign(true)  // 是否自动贴边
            .setModality(false)  // 是否模态窗口（事件是否可穿透当前窗口）
            .setMoveAble(true)   // 是否可拖动
            .setStartLocation(0, (getScreenHeight() * 0.7).toInt()) //设置起始位置
            .setDeskTopWindow(showDesktop) //桌面也要显示
            .create()

    }




    fun destroyFloatMenu() {
        float?.remove()
        float = null
    }

    fun checkInitialized():Boolean {
        return float == null
    }


    fun getFloatWindow(): FloatWindow? {
        return float
    }


    fun animateViewWidth(
        view: View,
        expand: Boolean,
        targetWidth: Int,
        duration: Long = 300
    ) {
        // 防止重复动画（标记 key 可以自定义）
        val isAnimating = view.getTag(R.id.home_news_floatmenu_animating_tag) as? Boolean ?: false
        if (isAnimating) return

        val startWidth= if (expand) 0 else targetWidth
        val targetWidth = if (expand) targetWidth else 0


        view.setTag(R.id.home_news_floatmenu_animating_tag, true) // 设置动画标记

        val animator = ValueAnimator.ofInt(startWidth, targetWidth)
        animator.addUpdateListener { valueAnimator ->
//            val newWidth = valueAnimator.animatedValue as Int
//            view.layoutParams.width = newWidth
//            view.requestLayout()


            val newWidth = valueAnimator.animatedValue as Int
            val lp = view.layoutParams
            lp.width = newWidth
            view.layoutParams = lp


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