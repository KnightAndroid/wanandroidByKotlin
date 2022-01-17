package com.knight.kotlin.library_common.fragment

import android.app.Dialog
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.viewModels
import com.knight.kotlin.library_base.fragment.BaseDialogFragment
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_common.databinding.UpdateAppDialogBinding
import com.knight.kotlin.library_common.entity.AppUpdateBean


/**
 * Author:Knight
 * Time:2022/1/11 15:34
 * Description:UpdateAppDialogFragment
 */
class UpdateAppDialogFragment: BaseDialogFragment<UpdateAppDialogBinding,EmptyViewModel>() {

    override val mViewModel: EmptyViewModel by viewModels()
    private var mAppUpdateBean:AppUpdateBean?=null

    companion object {
        fun newInstance(mAppUpdateEntity: AppUpdateBean): UpdateAppDialogFragment {
            val homePushArticleFragment = UpdateAppDialogFragment()
            val args = Bundle()
            args.putParcelable("updateEntity", mAppUpdateEntity)
            homePushArticleFragment.arguments = args
            return homePushArticleFragment
        }
    }

    @NonNull
    override fun onCreateDialog(@Nullable savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setOnKeyListener { dialog, keyCode, event ->
            keyCode == KeyEvent.KEYCODE_BACK
        }
        return dialog
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
        mBinding.tvAppupdateTitlename.setTextColor(CacheUtils.getThemeColor())
        mBinding.tvAppupdateVersion.setTextColor(CacheUtils.getThemeColor())
        val gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        gradientDrawable.setColor(CacheUtils.getThemeColor())
        gradientDrawable.cornerRadius = 45.dp2px().toFloat()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mBinding.tvConfimUpdate.background = gradientDrawable
        } else {
            mBinding.tvConfimUpdate.setBackgroundDrawable(gradientDrawable)
        }

        mBinding.tvConfimUpdate.setOnClickListener {
            dismiss()
            DownLoadDialogFragment.newInstance(mAppUpdateBean?.downLoadLink).showAllowingStateLoss(
                parentFragmentManager,"dialog_downlaod")
        }
    }

    override fun initObserver() {

    }

    override fun initRequestData() {
    }
}