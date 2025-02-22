package com.knight.kotlin.module_welcome.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.view.Gravity
import android.view.KeyEvent
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.knight.kotlin.library_base.BaseApp
import com.knight.kotlin.library_base.fragment.BaseDialogFragment
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.util.ActivityManagerUtils
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_util.TextClickUtils
import com.knight.kotlin.library_util.TextClickUtils.OnClickToWebListener
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.library_util.startPage
import com.knight.kotlin.library_util.startPageWithParams
import com.knight.kotlin.module_welcome.databinding.WelcomePrivacyAgreeFragmentBinding
import kotlin.system.exitProcess


/**
 * Author:Knight
 * Time:2021/12/30 16:22
 * Description:WelcomePrivacyAgreeFragment
 */
class WelcomePrivacyAgreeFragment : BaseDialogFragment<WelcomePrivacyAgreeFragmentBinding,EmptyViewModel>() {

    private lateinit var spannable: SpannableStringBuilder

    @NonNull
    override fun onCreateDialog(@Nullable savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setOnKeyListener(DialogInterface.OnKeyListener { _, keyCode, _ ->
            keyCode == KeyEvent.KEYCODE_BACK
        })
        return dialog
    }
    override fun getGravity(): Int = Gravity.CENTER

    /**
     * 同意后跳到主页页面
     */
    private fun goAgreeToMain() {
       //同意隐私政策
       CacheUtils.saveIsAgreeMent(true)
       //初始化危险类sdk
       BaseApp.application.initDangrousSdk()
       dismiss()
       startPage(RouteActivity.Main.MainActivity)
       activity?.finish()
    }

    /**
     * 不同意退出APP
     */
    private fun disAgreeExitApp() {
        dismiss()
        ActivityManagerUtils.getInstance()?.finishAllActivity()
        exitProcess(0)
    }

    @SuppressLint("NewApi")
    override fun WelcomePrivacyAgreeFragmentBinding.initView() {
        spannable = SpannableStringBuilder(mBinding.appPrivacyTip.text.toString())
        mBinding.appPrivacyTip.movementMethod = LinkMovementMethod.getInstance()
        spannable.setSpan(TextClickUtils().setOnClickWebListener(object : OnClickToWebListener {
            override fun goWeb() {

                startPageWithParams(RouteActivity.Web.WebPager,
                    "webUrl" to "file:android_asset/wanandroid_useragree.html",
                            "webTitle" to "用户协议")
            }
        }), 8, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(TextClickUtils().setOnClickWebListener(object : OnClickToWebListener {
            override fun goWeb() {
                startPageWithParams(RouteActivity.Web.WebPager,
                            "webUrl" to "file:android_asset/wanandroid_userprivacy.html",
                                    "webTitle" to "隐私政策")
            }

        }), 15, 21, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        mBinding.appPrivacyTip.text = spannable
        ViewInitUtils.avoidHintColor(mBinding.appPrivacyTip)
        setOnClick(mBinding.tvConfimPrivacy,::goAgreeToMain)
        setOnClick(mBinding.tvDisagreePrivacy,::disAgreeExitApp)
    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }


}