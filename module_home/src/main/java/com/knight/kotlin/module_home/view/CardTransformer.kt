package com.knight.kotlin.module_home.view

import android.view.View
import androidx.viewpager2.widget.ViewPager2

/**
 * Author:Knight
 * Time:2022/1/28 15:03
 * Description:CardTransformer
 */
class CardTransformer:ViewPager2.PageTransformer {

    companion object {
        /**
         * 动画类型 =》层叠
         */
         val ANIM_TYPE_STACK = 1

        /**
         * 动画类型 =》缩放
         */
        val ANIM_TYPE_SCALE = 2

        /**
         * 动画类型 =》风车
         */
        val ANIM_TYPE_WINDMILL = 3
    }



    /**
     * 动画类型
     * 【ANIM_TYPE_STACK：层叠】
     * 【ANIM_TYPE_STACK：缩放】
     * 【ANIM_TYPE_WINDMILL：风车】
     */
    private var mTransformerType = ANIM_TYPE_STACK

    /**
     * 初始位移量
     */
    private val mTranslation = 61

    /**
     * 初始缩放比例
     */
    private val mScale = 80

    /**
     * 初始旋转角度_风车
     */
    private val mRotation = -20f

    override fun transformPage(page: View, position: Float) {
        when (mTransformerType) {
            ANIM_TYPE_STACK ->                 // 层叠
                animStack(page, position)
            ANIM_TYPE_SCALE ->                 // 缩放
                animScale(page, position)
            ANIM_TYPE_WINDMILL ->                 // 风车
                animWindmill(page, position)
        }
    }

    /**
     * 设置动画类型
     *
     * @param type 动画类型
     * 【ANIM_TYPE_STACK：层叠】
     * 【ANIM_TYPE_SCALE：缩放】
     * 【ANIM_TYPE_WINDMILL：风车】
     * @return 需要预加载的页数
     */
    fun setTransformerType(type: Int): Int {
        mTransformerType = type
        return when (type) {
            ANIM_TYPE_STACK ->                 // 层叠
                4
            ANIM_TYPE_SCALE ->                 // 缩放
                2
            ANIM_TYPE_WINDMILL ->                 // 风车
                2
            else -> 0
        }
    }

    /**
     * 层叠
     *
     * @param view
     * @param position
     */
    private fun animStack(view: View, position: Float) {
        //缩放比例
        val scale = (view.width - mScale * position) / view.width.toFloat()

        // 设置卡片背景色
        if (view.background == null) {
            view.setBackgroundColor(0xFFFFFFFF.toInt())
        }
        // 设置Z轴及阴影
        view.translationZ = scale * 20
        // 设置横向缩放
        view.scaleX = scale * 0.85f
        // 设置纵向缩放
        view.scaleY = scale * 0.85f
        if (position <= 0.0f) {         // 当前页

            //X轴偏移
            view.translationX = view.width / 3f * position

            //打开点击事件
            view.isClickable = true
        } else if (position > 0) {      // 后一页

            //X轴偏移
            view.translationX = -view.width * position + mTranslation * position

            //屏蔽点击事件
            view.isClickable = false
        }
    }

    /**
     * 缩放
     *
     * @param view
     * @param position
     */
    private fun animScale(view: View, position: Float) {
        //缩放比例
        var scale = 1f
        if (position <= 0.0f) {         // 当前页

            // 计算缩放比例
            scale = (view.width + mScale * 5 * position) / view.width.toFloat()

            //X轴偏移
            view.translationX = -(view.width / 3f * position)

            //打开点击事件
            view.isClickable = true
        } else if (position > 0) {      // 后一页

            // 计算缩放比例
            scale = (view.width - mScale * 5 * position) / view.width.toFloat()

            //X轴偏移
            view.translationX = -(view.width / 3f * position)

            //屏蔽点击事件
            view.isClickable = false
        }

        // 设置卡片背景色
        if (view.background == null) {
            view.setBackgroundColor(0xFFFFFFFF.toInt())
        }
        // 设置Z轴及阴影
        view.translationZ = scale * 20
        // 设置横向缩放
        view.scaleX = scale * 0.85f
        // 设置纵向缩放
        view.scaleY = scale * 0.85f
    }

    /**
     * 风车
     *
     * @param view
     * @param position
     */
    private fun animWindmill(view: View, position: Float) {
        if (view.background == null) {
            // 设置横向缩放
            view.scaleX = 0.85f
            // 设置纵向缩放
            view.scaleY = 0.85f
        }
        if (position <= 0.0f) {         // 当前页

            // 旋转
            view.rotation = mRotation * Math.abs(position)

            // Y轴位移
            view.translationY = -(view.height / 10f * position)

            // Z轴 阴影
            val translationZ = (view.width + mScale * 5 * position) / view.width.toFloat()
            view.translationZ = translationZ * 20

            //打开点击事件
            view.isClickable = true
        } else if (position > 0) {      // 后一页

            // 旋转
            view.rotation = -(mRotation * Math.abs(position))

            // Y轴位移
            view.translationY = view.height / 10f * position

            // Z轴 阴影
            val translationZ = (view.width - mScale * 5 * position) / view.width.toFloat()
            view.translationZ = translationZ * 20

            //屏蔽点击事件
            view.isClickable = false
        }

        // 前后两页是否露角
        val showAngle = true
        if (showAngle) {
            // X轴位移
            view.translationX = -(view.width / 10f * position)
        } else {
            // X轴位移
            view.translationX = view.width / 3f * position
        }
    }


}