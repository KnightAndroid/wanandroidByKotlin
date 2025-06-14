package com.knight.kotlin.module_video.utils

import android.text.TextUtils
import android.util.Log
import android.view.View
import com.knight.kotlin.library_widget.linkview.LinkMode
import com.knight.kotlin.library_widget.linkview.LinkOnClickListener
import com.knight.kotlin.library_widget.linkview.LinkTextView

/**
 * Author:Knight
 * Time:2024/3/18 17:03
 * Description:LinkHerfUtils
 */
object LinkHerfUtils {
    /**
     * 设置正文内容
     *
     * @param content
     */
    fun setContent(content: String?, autoLinkTextView: LinkTextView) {
        if (TextUtils.isEmpty(content)) return
        autoLinkTextView.visibility = View.VISIBLE
        autoLinkTextView.addAutoLinkMode(LinkMode.MODE_HASHTAG, LinkMode.MODE_MENTION, LinkMode.MODE_URL) //设置需要富文本的模式
        autoLinkTextView.text = content

        autoLinkTextView.setAutoLinkOnClickListener(object : LinkOnClickListener{
            override fun onAutoLinkTextClick(autoLinkMode: LinkMode?, matchedText: String?) {
                when (autoLinkMode) {
                    LinkMode.MODE_HASHTAG -> Log.i("minfo", "话题 $matchedText")
                    LinkMode.MODE_MENTION -> Log.i("minfo", "at $matchedText")
                    else -> {}
                }
            }
        })
    }
}