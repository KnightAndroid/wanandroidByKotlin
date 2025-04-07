package com.knight.kotlin.module_welcome.fragment

import android.annotation.SuppressLint
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.view.Gravity
import com.knight.kotlin.library_base.BaseApp
import com.knight.kotlin.library_base.fragment.BaseDialogFragment
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.util.ActivityManagerUtils
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_util.TextClickUtils
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.library_util.startPage
import com.knight.kotlin.library_util.startPageWithParams
import com.knight.kotlin.module_welcome.databinding.WelcomePrivacyAgreeFragmentBinding
import java.util.regex.Pattern
import kotlin.system.exitProcess


/**
 * Author:Knight
 * Time:2021/12/30 16:22
 * Description:WelcomePrivacyAgreeFragment
 */
class WelcomePrivacyAgreeFragment : BaseDialogFragment<WelcomePrivacyAgreeFragmentBinding,EmptyViewModel>() {

    private lateinit var spannable: SpannableStringBuilder

    override fun cancelOnTouchOutSide(): Boolean {
        return false
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





        // 使用正则表达式提取所有《》内的内容
        val pattern = Pattern.compile("《(.*?)》") // 匹配《》之间的内容
        val matcher = pattern.matcher(appPrivacyTip.text.toString())
        mBinding.appPrivacyTip.movementMethod = LinkMovementMethod.getInstance()
        while (matcher.find()) {
            val startIndex = matcher.start() // 获取每个《》的起始位置
            val endIndex = matcher.end() // 获取每个《》的结束位置
            val target = matcher.group(1) // 获取《》中的内容

            // 创建点击事件
            val span = TextClickUtils().setOnClickWebListener(object : TextClickUtils.OnClickToWebListener {
                override fun goWeb() {

                    when(target) {
                        "用户协议","User Agreement"->{
                            startPageWithParams(RouteActivity.Web.WebPager,
                                "webUrl" to "file:android_asset/wanandroid_useragree.html",
                                "webTitle" to "用户协议")
                        }
                        "隐私政策","Privacy Policy"->{
                            startPageWithParams(RouteActivity.Web.WebPager,
                                "webUrl" to "file:android_asset/wanandroid_userprivacy.html",
                                "webTitle" to "隐私政策")
                        }
                        "ShareSdk隐私政策","ShareSdk Privacy Policy"->{
                            startPageWithParams(RouteActivity.Web.WebPager,
                                "webUrl" to "file:android_asset/wanandroid_sharesdk_userprivacy.html",
                                "webTitle" to "ShareSdk隐私政策")
                        }
                    }
                }
            })
            // 为该部分设置点击事件
            spannable.setSpan(span, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        }
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