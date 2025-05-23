package com.knight.kotlin.library_widget

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView


/**
 * @author created by luguian
 * @organize
 * @Date 2025/5/23 11:06
 * @descript:追加特定文字到末尾
 */
class AppendEndTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var mainText: String = ""
    private var locationText: String = ""

    fun setContentWithEndMessage(content: String, appendContent: String?,appendColor:Int) {
        mainText = content
        locationText = appendContent ?: ""

        val builder = SpannableStringBuilder()
        builder.append(mainText)

        if (locationText.isNotEmpty()) {
            builder.append(" ")
            val start = builder.length
            builder.append(locationText)

            // 设置颜色
            builder.setSpan(ForegroundColorSpan(appendColor), start, builder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            // 设置字体大小为 12sp（true 表示单位为 sp）
            builder.setSpan(
                AbsoluteSizeSpan(11, true),
                start,
                builder.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        text = builder
    }
}