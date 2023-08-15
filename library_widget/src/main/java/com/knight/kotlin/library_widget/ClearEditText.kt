package com.knight.kotlin.library_widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.kotlin.library_widget
 * @ClassName:      ClearEditText
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/18 3:12 下午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/18 3:12 下午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

class ClearEditText:androidx.appcompat.widget.AppCompatEditText,View.OnFocusChangeListener,TextWatcher{

    private lateinit var mClearDrawable: Drawable

    private var hasFocus = false

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet? = null,defAttrStyle: Int = androidx.appcompat.R.attr.editTextStyle)
            : super(context, attributeSet, defAttrStyle) {
        init()
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun init(){
       // mClearDrawable = compoundDrawables[2]
     //   if (mClearDrawable == null) {
            mClearDrawable = resources.getDrawable(R.drawable.widget_deleteicon_selector, null)
      //  }

        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight()
        )
        //默认设置隐藏图标
        setClearIconVisible(false)
        //设置焦点改变的监听?
        onFocusChangeListener = this
        //设置输入框里面内容发生改变的监听
        addTextChangedListener(this)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            if (compoundDrawables[2] != null) {
                val touchable = (event.x > width - totalPaddingRight
                        && event.x < width - paddingRight)
                if (touchable) {
                    this.setText("")
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        this.hasFocus = hasFocus
        if (hasFocus) {
            setClearIconVisible(text?.length ?: 0 > 0)
        } else {
            setClearIconVisible(false)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (hasFocus) {
            setClearIconVisible(s.isNotEmpty())
        }
    }
    override fun afterTextChanged(s: Editable) {

    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     * @param visible
     */
    protected fun setClearIconVisible(visible: Boolean) {
        val right = if (visible) mClearDrawable else null
        setCompoundDrawables(
            compoundDrawables[0],
            compoundDrawables[1], right, compoundDrawables[3]
        )
    }
}