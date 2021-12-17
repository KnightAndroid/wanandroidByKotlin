package com.knight.kotlin.library_util.toast

import android.app.Activity
import android.view.View
import android.widget.TextView
import com.knight.kotlin.library_util.toast.callback.ToastInterface


/**
 * Author:Knight
 * Time:2021/12/17 14:21
 * Description:ActivityToast
 */
class ActivityToast constructor(activity: Activity) : ToastInterface{

    /**
     * Toast 实现类
     *
     */
    private val mToastImpl:ToastImpl = ToastImpl(activity,this)

    /**
     * Toast布局
     *
     */
    private lateinit var mView:View

    /**
     * Toast 消息View
     *
     */
    private var mMessageView:TextView? = null

    /**
     * Toast显示重心
     *
     */
    private var mGravity:Int = 0

    /**
     *
     * Toast显示时长
     */
    private var mDuration:Int = 0

    /**
     *
     * 水平偏移
     */
    private var mXOffset:Int = 0

    /**
     *
     * 垂直偏移
     */
    private var mYOffset:Int = 0

    /**
     *
     * 水平间距
      */
    private var mHorizontalMargin:Float = 0f

    /**
     *
     * 垂直间距
     */
    private var mVerticalMargin:Float = 0f




    override fun show() {
        //替换成 WindowManager 来显示
        mToastImpl.show()
    }

    override fun cancel() {
        //取消 WindowManager 的显示
        mToastImpl.cancel()
    }

    override fun setText(id: Int) {
        if (mView == null) {
            return
        }
        setText(mView.resources.getString(id))
    }

    override fun setText(text: CharSequence) {
        if (mMessageView == null) {
            return
        }
        mMessageView?.text = text
    }

    override fun setView(view: View) {
        mView = view
        if (mView == null) {
            mMessageView = null
            return
        }
        mMessageView = findMessageView(view)
    }

    override fun getView(): View {
        return mView
    }

    override fun setDuriation(duration: Int) {
        mDuration = duration
    }

    override fun getDuration() :Int{
        return mDuration
    }

    override fun setGravity(gravity: Int, xOffset: Int, yOffset: Int) {
        mGravity = gravity
        mXOffset = xOffset
        mYOffset = yOffset

    }

    override fun getGravity(): Int {
        return mGravity
    }

    override fun getXOffset():Int {
        return mXOffset
    }

    override fun getYOffset():Int {
        return mYOffset
    }

    override fun setMargin(horizontalMargin: Float, verticalMargin: Float) {
        mHorizontalMargin = horizontalMargin
        mVerticalMargin = verticalMargin
    }

    override fun getHorizontalMargin(): Float {
        return mHorizontalMargin
    }

    override fun getVerticalMargin(): Float {
        return mVerticalMargin
    }
}