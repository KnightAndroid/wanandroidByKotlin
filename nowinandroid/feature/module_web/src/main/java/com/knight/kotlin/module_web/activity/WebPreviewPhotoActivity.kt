package com.knight.kotlin.module_web.activity
import com.knight.kotlin.library_base.activity.BaseActivity
import com.core.library_base.route.RouteActivity
import com.core.library_base.vm.EmptyViewModel
import com.knight.kotlin.module_web.R
import com.knight.kotlin.module_web.databinding.WebPreviewphotoActivityBinding
import com.wyjson.router.annotation.Param
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/1/6 17:04
 * Description:WebPreviewPhotoActivity
 */

@AndroidEntryPoint
@Route(path = RouteActivity.Web.WebPreviewPhotoPager)
class WebPreviewPhotoActivity: BaseActivity<WebPreviewphotoActivityBinding, EmptyViewModel>() {


    /**
     * 文章里的图片
     */
    @JvmField
    @Param(name = "photoUri")
    var photoUri:String = ""


    override fun WebPreviewphotoActivityBinding.initView() {
        webPreviewIv.isZoomEnabled = true
        webPreviewIv.displayImage(photoUri)
        webPhotoCloseIv.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.web_fade_out_anim, R.anim.web_fade_in_anim);
        }
    }

    override fun initObserver() {

    }

    override fun initRequestData() {
    }

    override fun reLoadData() {

    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }
}