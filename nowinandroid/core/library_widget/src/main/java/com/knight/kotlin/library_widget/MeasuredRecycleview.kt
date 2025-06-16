package com.knight.kotlin.library_widget

import android.content.Context
import android.util.AttributeSet
import com.yanzhenjie.recyclerview.SwipeRecyclerView


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/17 16:47
 * @descript:
 */
class MeasuredRecycleview @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SwipeRecyclerView(context, attrs, defStyleAttr) {


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
       val expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2,MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, expandSpec)
    }
}