package com.core.library_base.widget.loadcircleview

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.core.library_base.R
import com.core.library_base.ktx.appStr
import com.core.library_common.util.dp2px

/**
 * Author:Knight
 * Time:2022/3/28 10:39
 * Description:ProgressHud
 */
class ProgressHud constructor(context: Context) {
    private var mProgressHudDialog: ProgressHudDialog? =null
    private val mDimAmount = 0f
    private var mWindowColor = 0
    private var mCornerRadius = 0f
    private var mContext: Context = context

    init {
        mProgressHudDialog =
            ProgressHudDialog(context)
        mWindowColor = ContextCompat.getColor(context, R.color.base_progress_color)
        mCornerRadius = 10f
        //转圈圈的圆形view
        val view: View = CirView(mContext)
        mProgressHudDialog?.setView(view)

    }

    fun setSize(width:Int,height:Int): ProgressHud {
        mProgressHudDialog?.setSize(width, height)
        return this
    }

    //   设置矩形的圆角程度
    fun setCornerRadius(radius: Float): ProgressHud {
        mCornerRadius = radius
        return this
    }

    //展示
    fun show(msg:String = appStr(R.string.base_loading)): ProgressHud {
        if (!isShowing()) {
            mProgressHudDialog?.let {
                it.show()
                it.setShowMessage(msg)
            }
        }
        return this
    }

    /**
     *
     * 更换展示文字
     */
    fun setText(msg:String) : ProgressHud {
        if (isShowing()) {
            mProgressHudDialog?.let {
                it.setShowMessage(msg)
            }
        } else {
            mProgressHudDialog?.let {
                it.show()
                it.setShowMessage(msg)
            }
        }
        return this
    }



    private fun isShowing(): Boolean {
        return mProgressHudDialog != null && mProgressHudDialog?.isShowing ?: false
    }

    //隐藏
    fun dismiss() {
        if (mProgressHudDialog != null && mProgressHudDialog?.isShowing == true) {
            mProgressHudDialog?.dismiss()
        }
    }



    private inner class ProgressHudDialog(context: Context) : Dialog(context) {
        private var mView: View? = null

        private var mCustomViewContainer: FrameLayout? = null
        private var mBackgroundLayout: BackgroundLayout? = null
        private var tvLoadingShow: TextView? = null
        private var mWidth = 0
        private var mHeight = 0


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.base_hud)
            val window = window
            window?.setBackgroundDrawable(ColorDrawable(0))
            window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            val layoutParams = window?.attributes
            layoutParams?.dimAmount = mDimAmount
            layoutParams?.gravity = Gravity.CENTER
            window?.attributes = layoutParams
            setCanceledOnTouchOutside(false)
            initViews()
        }

        private fun initViews() {
            mBackgroundLayout = findViewById<View>(R.id.background) as BackgroundLayout
            mBackgroundLayout?.setBaseColor(mWindowColor)
            mBackgroundLayout?.setCornerRadius(mCornerRadius.toInt())
            if (mWidth != 0) {
                updateBackgroundSize()
            }
            mCustomViewContainer = findViewById<View>(R.id.container) as FrameLayout
            tvLoadingShow = findViewById<View>(R.id.tv_loading_show) as TextView

            addViewToFrame(mView)
        }

        private fun addViewToFrame(view: View?) {
            if (view == null) {
                return
            }
            val wrapParam = ViewGroup.LayoutParams.WRAP_CONTENT
            val params = ViewGroup.LayoutParams(wrapParam, wrapParam)
            mCustomViewContainer!!.addView(view, params)
        }

        private fun updateBackgroundSize() {
            val params = mBackgroundLayout?.layoutParams
            params?.width = mWidth.dp2px()
            params?.height = mHeight.dp2px()
            mBackgroundLayout?.layoutParams = params
        }

        fun setView(view: View?) {
            if (view != null) {
                mView = view
                if (isShowing) {
                    mCustomViewContainer?.removeAllViews()
                    addViewToFrame(view)
                }
            }
        }


        fun setShowMessage(message:String):ProgressHud {
            tvLoadingShow?.text = message
            return this@ProgressHud
        }

        fun setSize(width: Int, height: Int) {
            mWidth = width
            mHeight = height
            if (mBackgroundLayout != null) {
                updateBackgroundSize()
            }
        }


    }


}