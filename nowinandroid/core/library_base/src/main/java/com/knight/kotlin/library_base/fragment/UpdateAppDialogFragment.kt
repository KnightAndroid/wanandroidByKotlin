package com.knight.kotlin.library_base.fragment

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.core.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_common.util.CacheUtils
import com.core.library_common.util.dp2px
import com.knight.kotlin.library_base.databinding.UpdateAppDialogBinding
import com.knight.kotlin.library_base.entity.AppUpdateBean


/**
 * Author:Knight
 * Time:2022/1/11 15:34
 * Description:UpdateAppDialogFragment
 */
class UpdateAppDialogFragment: BaseDialogFragment<UpdateAppDialogBinding, EmptyViewModel>() {


    private var mAppUpdateBean: AppUpdateBean?=null

    companion object {
        fun newInstance(mAppUpdateEntity: AppUpdateBean): UpdateAppDialogFragment {
            val homePushArticleFragment = UpdateAppDialogFragment()
            val args = Bundle()
            args.putParcelable("updateEntity", mAppUpdateEntity)
            homePushArticleFragment.arguments = args
            return homePushArticleFragment
        }
    }

    override fun cancelOnTouchOutSide(): Boolean {
        return false
    }


    override fun getGravity() = Gravity.CENTER
    override fun UpdateAppDialogBinding.initView() {
        mAppUpdateBean = arguments?.getParcelable<AppUpdateBean>("updateEntity")
        mBinding.tvAppupdateVersion.text = mAppUpdateBean?.versionName
        mBinding.tvAppupdateTime.text = mAppUpdateBean?.updateTime
        mBinding.tvAppupdateContent.text = mAppUpdateBean?.updateDesc
        if (mAppUpdateBean?.forceUpdate == true) {
            mBinding.ivAppupdateClose.visibility = View .GONE
        } else {
            mBinding.ivAppupdateClose.visibility = View.VISIBLE
        }
        mBinding.tvAppupdateTitlename.setTextColor(com.knight.kotlin.library_common.util.CacheUtils.getThemeColor())
        mBinding.tvAppupdateVersion.setTextColor(com.knight.kotlin.library_common.util.CacheUtils.getThemeColor())
        val gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        gradientDrawable.setColor(com.knight.kotlin.library_common.util.CacheUtils.getThemeColor())
        gradientDrawable.cornerRadius = 45.dp2px().toFloat()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mBinding.tvConfimUpdate.background = gradientDrawable
        } else {
            mBinding.tvConfimUpdate.setBackgroundDrawable(gradientDrawable)
        }

        mBinding.tvConfimUpdate.setOnClickListener {
            dismiss()
            com.knight.kotlin.library_base.fragment.DownLoadDialogFragment.newInstance(mAppUpdateBean?.downLoadLink).showAllowingStateLoss(
                parentFragmentManager,"dialog_download")
        }

        mBinding.ivAppupdateClose.setOnClickListener {
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