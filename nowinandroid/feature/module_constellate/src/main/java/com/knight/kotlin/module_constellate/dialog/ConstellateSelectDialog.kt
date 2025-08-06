package com.knight.kotlin.module_constellate.dialog

import android.view.Gravity
import com.core.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_base.fragment.BaseDialogFragment
import com.knight.kotlin.module_constellate.databinding.ConstellateSelectDialogBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description
 * @Author knight
 * @Time 2025/8/6 21:48
 *
 */
@AndroidEntryPoint
class ConstellateSelectDialog :BaseDialogFragment<ConstellateSelectDialogBinding,EmptyViewModel>(){
    override fun ConstellateSelectDialogBinding.initView() {

    }

    override fun getGravity() = Gravity.BOTTOM

    override fun cancelOnTouchOutSide() = true

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }
}