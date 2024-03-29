package com.knight.kotlin.module_video.view

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.widget.ImageView
import android.widget.RelativeLayout
import com.knight.kotlin.library_util.AnimUtils
import java.util.Random


/**
 * Author:Knight
 * Time:2024/3/29 15:36
 * Description:LikeView
 */
class LikeView : RelativeLayout {

    private var gestureDetector : GestureDetector? = null

    /** 图片大小 */
    private val likeViewSize = 330
    private val angles = intArrayOf(-30,0,30)
    /**单击是否有点赞效果 */
    private val canSingleTabShow = false
    private var onPlayPauseListener:OnPlayPauseListener? = null
    private var onLikeListener:OnLikeListener? = null




    constructor(context: Context) : super(context) {
        init()
    }


    constructor(context: Context,attrs: AttributeSet) :super(context, attrs) {
         init()
    }



    private fun init() {
        gestureDetector = GestureDetector(context,object:SimpleOnGestureListener(){
            override fun onDoubleTap(e: MotionEvent): Boolean {
                addLikeView(e)
                onLikeListener!!.onLikeListener()
                return true
            }

            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                if (onPlayPauseListener != null) {
                    onPlayPauseListener!!.onPlayOrPause()
                }
                return true
            }
        })

        setOnTouchListener { v, event ->
            gestureDetector?.onTouchEvent(event)
            true
        }


    }

    private fun addLikeView(e:MotionEvent) {
        val imageView = ImageView(context)
        imageView.setImageResource(com.knight.kotlin.library_widget.R.drawable.widget_like_icon)
        addView(imageView)
        val layoutParams = LayoutParams(likeViewSize, likeViewSize)
        layoutParams.leftMargin = e.x.toInt() - likeViewSize / 2
        layoutParams.topMargin = e.y.toInt() - likeViewSize
        imageView.layoutParams = layoutParams
        playAnim(imageView)
    }


    private fun playAnim(view: View) {
        val animationSet = AnimationSet(true)
        val degrees = angles[Random().nextInt(3)]
        animationSet.addAnimation(AnimUtils.rotateAnim(0, 0, degrees.toFloat()))
        animationSet.addAnimation(AnimUtils.scaleAnim(100, 2f, 1f, 0))
        animationSet.addAnimation(AnimUtils.alphaAnim(0f, 1f, 100, 0))
        animationSet.addAnimation(AnimUtils.scaleAnim(500, 1f, 1.8f, 300))
        animationSet.addAnimation(AnimUtils.alphaAnim(1f, 0f, 500, 300))
        animationSet.addAnimation(AnimUtils.translationAnim(500, 0f, 0f, 0f, -400f, 300))
        animationSet.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                Handler().post { removeView(view) }
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        view.startAnimation(animationSet)
    }


    interface OnPlayPauseListener {
         fun onPlayOrPause()
    }

    interface OnLikeListener {
        fun onLikeListener()
    }


    /**
     * 设置播放器暂停事件
     *
     *
     * @param onPlayPauseListener
     */
    fun setOnPlayPauseListener(onPlayPauseListener:OnPlayPauseListener) {
         this.onPlayPauseListener = onPlayPauseListener
    }


    /**
     * 设置双击点赞事件
     *
     * @param onLikeListener
     */
    fun setOnLikeListener(onLikeListener:OnLikeListener) {
        this.onLikeListener = onLikeListener

    }





}