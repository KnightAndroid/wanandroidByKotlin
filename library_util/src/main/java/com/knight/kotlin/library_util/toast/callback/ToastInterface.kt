package com.knight.kotlin.library_util.toast.callback

import android.R
import android.view.View
import android.widget.TextView


/**
 * Author:Knight
 * Time:2021/12/17 10:27
 * Description:ToastInterface
 */
interface ToastInterface {

    /**
     * 显示
     */
    fun show()

    /**
     *
     * 取消
     */
    fun cancel()

    /**
     *
     * 设置资源文件中的文本
     */
    fun setText(id:Int)

    /**
     * 设置普通文本
     *
     */
    fun setText(text : CharSequence)

    /**
     * 设置布局
     */
    fun setView(view : View)

    /**
     * 获取布局
     */
    fun getView():View?


    /**
     * 设置显示时长
     */
    fun setDuration(duration : Int)

    /**
     * 获取显示时长
     *
     */
    fun getDuration():Int

    /**
     *
     * 设置重心偏移
     */
    fun setGravity(gravity:Int,xOffset : Int,yOffset:Int)

    /**
     * 获取显示重心
     */
    fun getGravity():Int

    /**
     *
     * 获取水平偏移
     */
    fun getXOffset():Int

    /**
     *
     * 获取垂直偏移
     */
    fun getYOffset():Int

    /**
     * 设置屏幕间距
     */
    fun setMargin(horizontalMargin :Float,verticalMargin:Float)

    /**
     *
     * 设置水平间距
     */
    fun getHorizontalMargin():Float

    /**
     * 设置垂直间距
     */
    fun getVerticalMargin():Float

    /**
     * 智能获取用于显示消息的 TextView
     */
    fun findMessageView(view: View): TextView? {
        if (view is TextView) {
            if (view.getId() == View.NO_ID) {
                view.setId(R.id.message)
            } else require(view.getId() == R.id.message) {
                // 必须将 TextView 的 id 值设置成 android.R.id.message
                "You must set the ID value of TextView to android.R.id.message"
            }
            return view
        }
        if (view.findViewById<View>(R.id.message) is TextView) {
            return view.findViewById<View>(R.id.message) as TextView
        }
        throw IllegalArgumentException("You must include a TextView with an ID value of android.R.id.message")
    }



}