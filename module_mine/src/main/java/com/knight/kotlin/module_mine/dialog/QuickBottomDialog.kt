package com.knight.kotlin.module_mine.dialog

import android.view.Gravity
import android.view.View
import androidx.fragment.app.viewModels
import com.knight.kotlin.library_base.fragment.BaseDialogFragment
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.module_mine.databinding.MineQuickloginBottomDialogBinding

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.kotlin.module_mine.dialog
 * @ClassName:      QuickBottomDialog
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/24 2:49 下午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/24 2:49 下午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

class QuickBottomDialog : BaseDialogFragment<MineQuickloginBottomDialogBinding,EmptyViewModel>() {
    override val mViewModel: EmptyViewModel by viewModels()

    interface FingureLoginListener {
        fun fingureQuick()
    }

    private var mFingureLoginListener: FingureLoginListener? = null

    fun setFingureLoginListener(mFingureLoginListener: FingureLoginListener?) {
        this.mFingureLoginListener = mFingureLoginListener
    }

    override fun getGravity() = Gravity.BOTTOM

    override fun MineQuickloginBottomDialogBinding.initView() {
         if (CacheUtils.getFingerLogin()) {
             tvFingureLogin.visibility = View.VISIBLE
         } else {
             tvFingureLogin.visibility = View.GONE
         }

        tvFingureLogin.setOnClick {
            dismiss()
            mFingureLoginListener?.fingureQuick()
        }
        tvQuickloginCancel.setOnClick {
            dismiss()
        }


    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

}