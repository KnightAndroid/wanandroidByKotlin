package com.knight.kotlin.module_eye_square.activity

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_eye_square.adapter.EyeSquareAdapter
import com.knight.kotlin.module_eye_square.databinding.EyeSquareActivityBinding
import com.knight.kotlin.module_eye_square.vm.EyeSquareVm
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
class EyeSquareActivity : BaseActivity<EyeSquareActivityBinding,EyeSquareVm>() {

    @JvmField
    @Param(name = "tabTitle")
    var tabTitle:String = ""



    //发现适配器
    private val mEyeSquareAdapter: EyeSquareAdapter by lazy { EyeSquareAdapter(
       this,mutableListOf()) }


    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
         mViewModel.getSquareDataByTypeAndLabel("card","community").observerKt {

             mEyeSquareAdapter.submitList(it.list?.filter { it.card_data?.body?.api_request == null })
         }
    }

    override fun reLoadData() {

    }

    override fun EyeSquareActivityBinding.initView() {
        mBinding.title = tabTitle
        rvEyeSquareList.init(
            GridLayoutManager(this@EyeSquareActivity, 2).also {
                it.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return mEyeSquareAdapter.convertViewType2SpanSize(position, 2)
                    }
                }
            },
            mEyeSquareAdapter,
            true
        )
    }
}