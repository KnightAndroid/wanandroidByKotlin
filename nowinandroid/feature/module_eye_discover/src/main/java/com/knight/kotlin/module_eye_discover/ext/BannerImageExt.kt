package com.knight.kotlin.module_eye_discover.ext

import androidx.databinding.BindingAdapter
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.library_util.splitUrl
import com.knight.kotlin.library_util.startPageWithParams
import com.knight.kotlin.module_eye_discover.entity.EyeDiscoverTopBannerItemListData
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import java.net.URLDecoder


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/8 10:29
 * @descript:广告图片加载
 */

@BindingAdapter(
    value = ["bannerImageUrl"]
)
fun Banner<EyeDiscoverTopBannerItemListData, BannerImageAdapter<EyeDiscoverTopBannerItemListData>>.loadUrl(itemList:MutableList<EyeDiscoverTopBannerItemListData>) {
    setAdapter(object : BannerImageAdapter<EyeDiscoverTopBannerItemListData>(itemList) {
        override fun onBindView(
            holder: BannerImageHolder, data: EyeDiscoverTopBannerItemListData, position: Int, size: Int
        ) {
            var imageUrl = data.data.image
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
            val urlList = data.data.actionUrl.splitUrl()
            var webUrl = ""
            if (urlList.size > 1) {
                webUrl = "https" + data.data.actionUrl.splitUrl().get(1)
                webUrl = URLDecoder.decode(webUrl,"utf-8")
            } else {
                webUrl = data.data.actionUrl
            }
            holder.imageView.setOnClick {
                startPageWithParams(
                    RouteActivity.Web.NewWebPager,
                    "webUrl" to webUrl,
                    "webTitle" to data.data.title)
            }
        }
    })
}