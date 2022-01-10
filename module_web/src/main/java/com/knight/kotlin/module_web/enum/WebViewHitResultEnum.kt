package com.knight.kotlin.module_web.enum

/**
 * Author:Knight
 * Time:2022/1/6 14:28
 * Description:WebViewHitResultEnum
 */
enum class WebViewHitResultEnum(val value:Int) {
    UNKNOWN_TYPE(0),
    ANCHOR_TYPE(1),
    PHONE_TYPE(2),
    GEO_TYPE(3),
    EMAIL_TYPE(4),
    IMAGE_TYPE(5),
    IMAGE_ANCHOR_TYPE(6),
    SRC_ANCHOR_TYPE(7),
    SRC_IMAGE_ANCHOR_TYPE(8),
    EDIT_TEXT_TYPE(9)
}