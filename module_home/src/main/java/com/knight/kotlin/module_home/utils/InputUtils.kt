package com.knight.kotlin.module_home.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView

/**
 * Author:Knight
 * Time:2022/4/25 16:58
 * Description:InputUtils
 */
object InputUtils {

    fun setLinstenerInputNumber(editText: EditText,tv: TextView) {
        editText.addTextChangedListener(object:TextWatcher{
            private var temp: CharSequence? = null
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                temp = s
            }

            override fun afterTextChanged(s: Editable?) {
                tv.text = temp.toString().trim { it <= ' ' }.length.toString() + "/30"
            }
        })
    }

}