package com.knight.kotlin.library_share

import android.view.Gravity
import android.view.ViewGroup
import com.knight.kotlin.library_base.fragment.BaseDialogFragment
import com.knight.kotlin.library_base.util.ActivityManagerUtils
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_share.databinding.ShareDialogBinding
import com.knight.kotlin.library_share.utils.CaptureUtils


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/22 15:49
 * @descript:分享弹窗
 */
class ShareDialog : BaseDialogFragment<ShareDialogBinding, EmptyViewModel>() {
    override fun getGravity(): Int = Gravity.CENTER

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun ShareDialogBinding.initView() {
        val bitmap =  CaptureUtils.captureWindow(ActivityManagerUtils.getInstance().getTopActivity()!!,false)
        ivScreen.setImageBitmap(bitmap)
    }


    override fun onStart() {
        super.onStart()

        val dialog = dialog
        dialog?.let {
            val params = it.window?.attributes
            params?.width = ViewGroup.LayoutParams.MATCH_PARENT
            params?.height = ViewGroup.LayoutParams.WRAP_CONTENT
            params?.gravity = getGravity()
            it.window?.attributes = params
        }
    }
}