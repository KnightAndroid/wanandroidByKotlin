package com.knight.kotlin.library_widget.linkview

/**
 * Author:Knight
 * Time:2024/3/18 15:36
 * Description:LinkOnClickListener
 */
interface LinkOnClickListener {
    fun onAutoLinkTextClick(autoLinkMode: LinkMode?, matchedText: String?)

}