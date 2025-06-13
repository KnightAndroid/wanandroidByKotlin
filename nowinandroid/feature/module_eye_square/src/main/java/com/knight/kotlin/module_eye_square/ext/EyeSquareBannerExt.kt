package com.knight.kotlin.module_eye_square.ext

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.knight.kotlin.library_base.entity.eye_type.EyeBannerImageList
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.library_util.splitUrl
import com.knight.kotlin.library_util.startPageWithParams
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import java.net.URLDecoder

/**
 * @Description
 * @Author knight
 * @Time 2025/2/11 20:18
 *
 */

@BindingAdapter(
    value = ["bannerUrl"]
)
fun Banner<EyeBannerImageList, BannerImageAdapter<EyeBannerImageList>>.loadUrl(itemList:MutableList<EyeBannerImageList>) {
    setAdapter(object : BannerImageAdapter<EyeBannerImageList>(itemList) {
        override fun onBindView(
            holder: BannerImageHolder, data: EyeBannerImageList, position: Int, size: Int
        ) {
            var imageUrl = data.cover?.url ?: ""
//            if (!imageUrl.contains("http://ali-")) {
//                imageUrl = imageUrl.replace("http://","http://ali-")
//            }

            if (!imageUrl.contains("//ali")) {
                if (imageUrl.contains("img.kaiyanapp.com")) {
                    if (imageUrl.startsWith("http://")) {
                        imageUrl = imageUrl.replace("http://","http://ali-")
                    } else {
                        imageUrl = imageUrl.replace("https://","https://ali-")
                    }
                }
            }
            ImageLoader.loadStringPhoto(
                context,
                imageUrl,
                holder.imageView
            )
//            val urlList = data.data.actionUrl.splitUrl()
//            var webUrl = ""
//            if (urlList.size > 1) {
//                webUrl = "https" + data.data.actionUrl.splitUrl().get(1)
//                webUrl = URLDecoder.decode(webUrl,"utf-8")
//            } else {
//                webUrl = data.data.actionUrl
//            }
//            holder.imageView.setOnClick {
//                startPageWithParams(
//                    RouteActivity.Web.WebPager,
//                    "webUrl" to webUrl,
//                    "webTitle" to data.data.title)
//            }
        }
    })
}