package com.knight.kotlin.module_web.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.knight.kotlin.module_web.R

/**
 * @author created by luguian
 * @organize
 * @Date 2025/11/7 15:31
 * @descript:
 */
class AppProgressLoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr) {
    private var mProgressBar: ProgressBar
    private var mTvLoading: TextView
    private var isShowLoading: Boolean

    init {
        View.inflate(context, R.layout.web_progress_bar_loading, this)
        mProgressBar = findViewById(R.id.progress_circular)
        mTvLoading = findViewById(R.id.tv_loading)
        isShowLoading = true
    }

    fun isShowLoading(): Boolean {
        return isShowLoading
    }

    fun hideShowLoading() {
        isShowLoading = false
    }

}