package com.knight.kotlin.module_home.dialog

import android.view.Gravity
import com.knight.kotlin.library_base.fragment.BaseDialogFragment
import com.knight.kotlin.module_home.databinding.HomeTodayWeatherNewsDialogBinding
import com.knight.kotlin.module_home.vm.HomeWeatherNewVm
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/27 15:04
 * @descript:天气弹窗
 */
@AndroidEntryPoint
class HomeWeatherNewsFragment:BaseDialogFragment<HomeTodayWeatherNewsDialogBinding,HomeWeatherNewVm>() {
    override fun getGravity() = Gravity.CENTER

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun HomeTodayWeatherNewsDialogBinding.initView() {

    }
}