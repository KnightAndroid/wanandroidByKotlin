package com.knight.kotlin.module_mine.dialog

import android.view.Gravity
import com.knight.kotlin.library_base.fragment.BaseDialogFragment
import com.core.library_base.ktx.toHtml
import com.core.library_base.vm.EmptyViewModel
import com.knight.kotlin.module_mine.R
import com.knight.kotlin.module_mine.databinding.MinePointRuleDialogBinding

/**
 * Author:Knight
 * Time:2022/5/11 13:51
 * Description:CoinsRuleDialog
 */
class CoinsRuleDialog : BaseDialogFragment<MinePointRuleDialogBinding, EmptyViewModel>() {



    companion object {
        fun newInstance(): CoinsRuleDialog {
            val coinsRuleDialog = CoinsRuleDialog()
            return coinsRuleDialog
        }
    }

    override fun getGravity() = Gravity.CENTER
    override fun cancelOnTouchOutSide(): Boolean {
       return true
    }

    override fun MinePointRuleDialogBinding.initView() {
        tvRuleDetailPoint.setText(getString(R.string.mine_point_detail_rule).toHtml())
    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }
}