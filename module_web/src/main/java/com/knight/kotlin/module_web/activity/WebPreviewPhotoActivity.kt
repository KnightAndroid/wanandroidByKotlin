package com.knight.kotlin.module_web.activity
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.module_web.R
import com.knight.kotlin.module_web.databinding.WebPreviewphotoActivityBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/1/6 17:04
 * Description:WebPreviewPhotoActivity
 */

@AndroidEntryPoint
@Route(path = RouteActivity.Web.WebPreviewPhotoPager)
class WebPreviewPhotoActivity:BaseActivity<WebPreviewphotoActivityBinding,EmptyViewModel>() {


    /**
     * 文章里的图片
     */
    @JvmField
    @Autowired(name = "photoUri")
    var photoUri:String = ""

    override val mViewModel: EmptyViewModel by viewModels()
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
}