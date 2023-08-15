package com.knight.library_biometric.dialog

import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.view.Gravity
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.knight.kotlin.library_base.fragment.BaseDialogFragment
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.library_biometric.R
import com.knight.library_biometric.databinding.BiometricLoginDialogBinding

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.library_biometric.dialog
 * @ClassName:      BiometricPromptDialog
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/23 4:47 下午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/23 4:47 下午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

class BiometricPromptDialog: BaseDialogFragment<BiometricLoginDialogBinding,EmptyViewModel>(){
    override val mViewModel: EmptyViewModel by viewModels()

    private var mDialogActionCallback: OnBiometricPromptDialogActionCallback? = null
    override fun getGravity() = Gravity.CENTER


    companion object {
        const val STATE_NORMAL = 1
        const val STATE_FAILED = 2
        const val STATE_ERROR = 3
        const val STATE_SUCCEED = 4
        fun newInstance(): BiometricPromptDialog? {
            return BiometricPromptDialog()
        }
    }
    interface OnBiometricPromptDialogActionCallback {
        fun onDialogDismiss()
        fun onCancel()
    }

    fun setOnBiometricPromptDialogActionCallback(callback: OnBiometricPromptDialogActionCallback) {
        mDialogActionCallback = callback
    }
    override fun BiometricLoginDialogBinding.initView() {
        fingureTvCancel.setOnClick {
            mDialogActionCallback?.let {
                it.onCancel()
            }
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        mDialogActionCallback?.let {
            it.onDialogDismiss()
        }
        mDialogActionCallback = null

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun setState(state: Int) {
        when (state) {
            STATE_NORMAL -> {
                mBinding.fingureTvStatus.setTextColor(Color.BLACK)
                mBinding.fingureTvStatus.setText(getString(R.string.biometric_touch_sensor))
                mBinding.fingureTvCancel.setVisibility(View.VISIBLE)
            }
            STATE_FAILED -> {
                mBinding.fingureTvStatus.setTextColor(Color.RED)
                mBinding.fingureTvStatus.setText(getString(R.string.biometric_touch_verify_failure))
                mBinding.fingureTvCancel.setVisibility(View.VISIBLE)
            }
            STATE_ERROR -> {
                mBinding.fingureTvStatus.setTextColor(Color.RED)
                mBinding.fingureTvStatus.setText(getString(R.string.biometric_touch_verify_error))
                mBinding.fingureTvCancel.setVisibility(View.VISIBLE)
            }
            STATE_SUCCEED -> {
                mBinding.fingureTvStatus.setTextColor(activity?.getColor(com.knight.kotlin.library_base.R.color.base_color_theme) ?: Color.parseColor("#55aff4"))
                mBinding.fingureTvStatus.setText(getString(R.string.biometric_touchverify_succeeded))
                mBinding.fingureTvCancel.setVisibility(View.VISIBLE)
                mBinding.fingureTvStatus.postDelayed(Runnable { dismiss() }, 500)
            }
            else -> {
            }
        }
    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }
}