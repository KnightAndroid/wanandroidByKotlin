package com.knight.kotlin.module_home.dialog

import android.view.Gravity
import com.knight.kotlin.library_base.fragment.BaseDialogFragment
import com.knight.kotlin.library_base.util.StatusBarUtils
import com.knight.kotlin.library_widget.citypicker.view.SideIndexBar
import com.knight.kotlin.module_home.databinding.HomeCityDialogPickerBinding
import com.knight.kotlin.module_home.vm.HomeCityGroupVm
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/17 16:56
 * @descript:选择城市列表弹窗
 */
@AndroidEntryPoint
class HomeCityGroupFragment:BaseDialogFragment<HomeCityDialogPickerBinding,HomeCityGroupVm>(),SideIndexBar.OnIndexTouchedChangedListener {
    override fun getGravity()= Gravity.BOTTOM
    override fun cancelOnTouchOutSide() = false

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun HomeCityDialogPickerBinding.initView() {
        sideIndexBar.setNavigationBarHeight(StatusBarUtils.getStatusBarHeight(requireActivity()))
        sideIndexBar.setOverlayTextView(indexOverlay)
            .setOnIndexChangedListener(this@HomeCityGroupFragment)
    }

    override fun onIndexChanged(index: String?, position: Int) {

    }
}