package com.knight.kotlin.module_eye_square.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_eye_square.databinding.EyeSquareActivityBinding
import com.wyjson.router.annotation.Param
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/11 14:15
 * @descript:开眼社区主页
 */
@AndroidEntryPoint
@Route(path = RouteActivity.EyeSquare.EyeSquareActivity)
class EyeSquareActivity : BaseActivity<EyeSquareActivityBinding,EmptyViewModel>() {

    @JvmField
    @Param(name = "tabTitle")
    var tabTitle:String = ""

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun EyeSquareActivityBinding.initView() {
        mBinding.title = tabTitle

        rvEyeSquareList.init(
            LinearLayoutManager(this@EyeDailyListActivity),
            mEyeDailyAdapter,
            true
        )
    }
}