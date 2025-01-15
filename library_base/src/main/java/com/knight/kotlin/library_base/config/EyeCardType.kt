package com.knight.kotlin.library_base.config

/**
 * @Description 开眼卡片枚举类型
 * @Author knight
 * @Time 2025/1/13 21:45
 *
 */


object EyeScroll {
    const val HORIZONTAL = "h-scroll"
    const val VERTICAL = "v-scroll"
}

object EyeCardType {


    const val HEADER = "header"
    const val FOOTER = "footer"

    const val FEED_COVER_LARGE_VIDEO = "feed_cover_large_video"
    const val FEED_COVER_SMALL_VIDEO = "feed_cover_small_video"
    const val SLIDE_COVER_IMAGE_WITH_FOOTER = "slide_cover_image_with_footer"
    const val WATERFALL_COVER_SMALL_IMAGE = "waterfall_cover_small_image"
    const val WATERFALL_COVER_SMALL_VIDEO = "waterfall_cover_small_video"
    const val FEED_ITEM_DETAIL = "feed_item_detail"
    const val ICON_GRID = "icon_grid"
    const val SLIDE_COVER_IMAGE_WITH_TITLE = "slide_cover_image_with_title"
    const val STACKED_SLIDE_COVER_IMAGE = "stacked_slide_cover_image"
    const val SLIDE_COVER_IMAGE = "slide_cover_image"
    const val FEED_USER = "feed_user"
    const val SEARCH_RESULT_IMAGE = "search_result_image"
    const val FEED_COVER_DETAIL_TOPIC = "feed_cover_detail_topic"
    const val SEARCH_RESULT_TEXT = "search_result_text"
    const val FEED_COVER_LARGE_IMAGE = "feed_cover_large_image"
    const val SLIDE_USER = "slide_user"
    const val SLIDE_COVER_VIDEO_WITH_AUTHOR = "slide_cover_video_with_author"
    const val CARD_USER = "card_user"
    const val SLIDE_COVER_VIDEO = "slide_cover_video"
    const val DESCRIPTION_TEXT = "description_text"

    const val DEFAULT = ""
}

/**
 *
 * 搜索结果类型
 */
object EyeSearchResultType {
    const val VIDEO = 1 //视频
    const val PGC = 2 //作者
    const val GRAPHIC = 3 //图文
    const val TOPIC = 4 //话题
    const val UGC = 5 //用户
}