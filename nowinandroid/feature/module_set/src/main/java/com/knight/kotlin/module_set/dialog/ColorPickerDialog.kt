package com.knight.kotlin.module_set.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsets
import android.widget.EditText
import android.widget.TextView
import com.knight.kotlin.library_base.util.ColorUtils
import com.knight.kotlin.library_widget.ColorPickerView
import com.knight.kotlin.module_set.R
import com.knight.kotlin.module_set.annoation.ColorStyle
import java.util.Locale

/**
 * Author:Knight
 * Time:2022/5/25 11:02
 * Description:ColorPickerDialog
 */
class ColorPickerDialog(context: Context,initialColor:Int,mColorStyle:Int,mRecoverText:String) : Dialog(context),ColorPickerView.OnColorChangedListener, View.OnClickListener {
    private var mListener: OnColorPickedListener? = null
    private lateinit var set_color_picker_view: ColorPickerView
    private lateinit var et_color: EditText
    private lateinit var view_color_panel: View
    private lateinit var tv_cancel: TextView
    private lateinit var tv_confim: TextView
    private lateinit var set_tv_recovertheme: TextView
    private val mContext: Context = context
    private val colorStyle = mColorStyle
    private val recoverText: String = mRecoverText

    interface OnColorPickedListener {
        fun onColorPicked(color: Int)
    }


    fun setOnColorChangedListener(mListener: OnColorPickedListener?) {
        this.mListener = mListener
    }

    init {
        setUp(initialColor)
    }
    private val mTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {

        }
    }
    private fun setUp(color: Int) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        @SuppressLint("InflateParams") val layout: View =
            inflater.inflate(R.layout.set_colorpicker_dialog, null)
        setContentView(layout)
        // 必须设置这两个,才能设置宽度
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.decorView.setBackgroundColor(Color.TRANSPARENT)
        val attributes = window!!.attributes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = (mContext as Activity).windowManager.currentWindowMetrics
            val insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            attributes.width =
                ((windowMetrics.bounds.width() - insets.left - insets.right) * 0.9).toInt()
        } else {
            val dd = (mContext as Activity).windowManager.defaultDisplay
            val dm = DisplayMetrics()
            dd.getMetrics(dm)
            attributes.width = (dm.widthPixels * 0.9).toInt()
        }
        set_color_picker_view =
            layout.findViewById<View>(R.id.set_color_picker_view) as ColorPickerView
        et_color = layout.findViewById(R.id.et_color)
        view_color_panel = layout.findViewById(R.id.view_color_panel)
        tv_cancel = layout.findViewById(R.id.tv_cancel)
        tv_confim = layout.findViewById(R.id.tv_confim)
        set_tv_recovertheme = layout.findViewById(R.id.set_tv_recovertheme)
        set_tv_recovertheme.setText(recoverText)
        tv_cancel.setOnClickListener(this)
        tv_confim.setOnClickListener(this)
        set_tv_recovertheme.setOnClickListener(this)
        set_color_picker_view.setOnColorChangedListener(this)
        set_color_picker_view.setColor(color, true)
        et_color.setText(ColorUtils.convertToRGB(color).toUpperCase(Locale.getDefault()))
        et_color.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                view_color_panel.setBackgroundColor(
                    ColorUtils.convertToColorInt(et_color.text.toString().trim { it <= ' ' })
                )
            }
        })
    }



    fun getColor(): Int {
        return set_color_picker_view.getColor()
    }

    override fun onColorChanged(color: Int) {
        view_color_panel.setBackgroundColor(color)
        et_color.setText(ColorUtils.convertToRGB(color).uppercase(Locale.getDefault()))

    }


    class Builder(
        private val mContext: Context,
        private val initColor: Int,
        private val colorStyle: Int,
        private val recoverText: String
    ) {
        private var mListener: OnColorPickedListener? = null
        fun setOnColorPickedListener(mListener: OnColorPickedListener?): Builder {
            this.mListener = mListener
            return this
        }

        fun build(): ColorPickerDialog {
            val dialog = ColorPickerDialog(
                mContext,
                initColor,
                colorStyle,
                recoverText
            )
            dialog.setOnColorChangedListener(mListener)
            return dialog
        }
    }
    override fun onClick(v: View?) {
        if (v?.id == R.id.tv_confim) {
            if (mListener != null) {
                if (ColorUtils.convertToColorInt(
                        et_color.text.toString().trim { it <= ' ' }) !== 0
                ) {
                    mListener?.onColorPicked((view_color_panel.background as ColorDrawable).color)
                }
            }
            dismiss()
        } else if (v?.id == R.id.set_tv_recovertheme) {
            when (colorStyle) {
                ColorStyle.THEMECOLOR -> {
                    view_color_panel.setBackgroundColor(Color.parseColor("#55aff4"))
                    et_color.setText("55aff4")
                }
                ColorStyle.TEXTCOLOR -> {
                    view_color_panel.setBackgroundColor(Color.parseColor("#333333"))
                    et_color.setText("333333")
                }
                ColorStyle.BGCOLOR -> {
                    view_color_panel.setBackgroundColor(Color.parseColor("#f9f9f9"))
                    et_color.setText("f9f9f9")
                }
                else -> {
                }
            }
        } else {
            dismiss()
        }
    }
}