package com.knight.kotlin.library_widget

import android.R.color.transparent
import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.knight.kotlin.library_util.LogUtils


/**
 * Author:Knight
 * Time:2024/7/22 16:59
 * Description:SlowShowTextView 逐字显示
 */
class SlowShowTextView : androidx.appcompat.widget.AppCompatTextView {
    /**
     * default duration
     * 5ms
     */
    private val DEFAULT_DURATION = 5

    /**
     * duration one by one
     * ms
     */
    private var mDuration = 0

    /**
     * handler
     */
    private lateinit var mHandler: BeanShowingHandler

    private var mOriginalTextColor = 0

    private var mOriginalText: String = ""

    private var mStringList: Array<String> = arrayOf()

    private var mCurrentIndex = 0

    private var mCurrentText: String? = null

    /**
     * handler event
     */
    private val EVENT_BEAN_START = 0
    private val EVENT_BEAN_SHOW = 1
    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet? = null, defAttrStyle: Int = 0)
            : super(context, attributeSet, defAttrStyle) {
                init()
    }


    @SuppressLint("ResourceAsColor")
    private fun init() {
        // set transparent first
        mOriginalTextColor = currentTextColor
        mOriginalText = getText() as String
        mStringList =
            mOriginalText.split("".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        mCurrentIndex = 0
        //super.setTextColor(transparent)
        mDuration = DEFAULT_DURATION
        mHandler = BeanShowingHandler()
     //   mHandler.sendEmptyMessage(EVENT_BEAN_START)
    }


    inner class BeanShowingHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                EVENT_BEAN_START -> {
                    mCurrentText = ""
                    mCurrentIndex = 0
                    setTextColor(mOriginalTextColor)
                    sendEmptyMessage(EVENT_BEAN_SHOW)
                }

                EVENT_BEAN_SHOW -> doBeanShowText()
                else -> {}
            }
        }
    }


    private fun doBeanShowText() {
        mCurrentIndex++
        if (mCurrentIndex > mOriginalText!!.length) {
            return
        }
        mCurrentText = mCurrentText + mStringList[mCurrentIndex]
        super.setText(mCurrentText)
        mHandler.sendEmptyMessageDelayed(EVENT_BEAN_SHOW, DEFAULT_DURATION.toLong())
    }

    /**
     * set text
     * @param text
     */
    fun setText(text: String) {
        mHandler.removeMessages(EVENT_BEAN_SHOW)
        mHandler.removeMessages(EVENT_BEAN_START)
        mOriginalTextColor = currentTextColor
        setTextColor(ContextCompat.getColor(context, transparent))
        mOriginalText = text
        mStringList =
            mOriginalText.split("".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        mHandler.sendEmptyMessage(EVENT_BEAN_START)
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mHandler.removeMessages(EVENT_BEAN_SHOW)
        mHandler.removeMessages(EVENT_BEAN_START)
    }
}