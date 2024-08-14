package com.knight.kotlin.module_eye_discover.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.ktx.init
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_widget.ktx.transformShareElementConfig
import com.knight.kotlin.module_eye_discover.R
import com.knight.kotlin.module_eye_discover.adapter.EyeDiscoverCategoryDetailAdapter
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverCategoryDetailActivityBinding
import com.knight.kotlin.module_eye_discover.vm.EyeDiscoverVm
import com.wyjson.router.annotation.Param
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/13 17:28
 * @descript:分类详细界面
 */
@AndroidEntryPoint
@Route(path = RouteActivity.EyeDiscover.EyeCategoryDetailActivity)
class EyeCategoryDetailActivity : BaseActivity<EyeDiscoverCategoryDetailActivityBinding, EyeDiscoverVm>(){

    @JvmField
    @Param(name = "id")
    var id:Long = 0

    @JvmField
    @Param(name = "name")
    var name:String = ""


    @JvmField
    @Param(name = "headImage")
    var headImage:String = ""

    private val mEyeDiscoverCategoryDetailAdapter: EyeDiscoverCategoryDetailAdapter by lazy { EyeDiscoverCategoryDetailAdapter(
        ) }

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun EyeDiscoverCategoryDetailActivityBinding.initView() {
        title = name
        headerImage = headImage
        transformShareElementConfig(discoverDetailImg,getString(R.string.eye_discover_share_element_container))
        discoverCategoryDetailRv.init(
            LinearLayoutManager(this@EyeCategoryDetailActivity),
            mEyeDiscoverCategoryDetailAdapter,
            true
        )
    }

}