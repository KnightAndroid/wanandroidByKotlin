package com.knight.kotlin.library_widget.linkview

/**
 * Author:Knight
 * Time:2024/3/18 15:26
 * Description:LinkMode
 */
enum class LinkMode(val description:String) {

    MODE_HASHTAG("Hashtag"),
    MODE_MENTION("Mention"),
    MODE_URL("Url"),
    MODE_PHONE("Phone"),
    MODE_EMAIL("Email"),
    MODE_CUSTOM("Custom");


}