package com.knight.kotlin.library_base.util

import com.knight.kotlin.library_base.entity.WebDataEntity
import com.knight.kotlin.library_base.route.RouteActivity
import com.wyjson.router.GoRouter

/**
 * Author:Knight
 * Time:2022/3/7 17:34
 * Description:ArouteUtils
 */
class ArouteUtils {
    companion object {
        fun startWebArticle(link:String,title:String,id:Int,collect:Boolean,envelopePic:String? ="",desc:String?="",chapterName:String?="",author:String?="",shareUser:String?="") {
            val mWebDataEntity = WebDataEntity(
                link,
                title,
                id,
                collect,
                envelopePic ?:"",
                desc?:"" ,
                chapterName?:"",
                author
                    ?: shareUser ?: "")
            GoRouter.getInstance().build(RouteActivity.Web.WebArticlePager)
                .withParcelable("webDataEntity", mWebDataEntity).go()
        }

    }
}