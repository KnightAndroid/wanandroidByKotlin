package com.knight.kotlin.module_message.activity

import androidx.fragment.app.Fragment
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.library_util.bindWechatViewPager2
import com.knight.kotlin.module_message.R
import com.knight.kotlin.module_message.databinding.MessageActivityBinding
import com.knight.kotlin.module_message.fragment.MessageFragment
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Route(path = RouteActivity.Message.MessageActivity)
class MessageActivity : BaseActivity<MessageActivityBinding,EmptyViewModel>() {
    val titleDatas by lazy {mutableListOf<String>(getString(R.string.message_readed),getString(R.string.message_unread))}
    val messageFragments by lazy{mutableListOf<Fragment>(MessageFragment.newInstance(true),MessageFragment.newInstance(false))}
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun MessageActivityBinding.initView() {
        includeMessageToolbar.baseTvTitle.setText(R.string.message_center)
        includeMessageToolbar.baseIvBack.setOnClick { finish() }
        ViewInitUtils.setViewPager2Init(this@MessageActivity,mBinding.messageViewPager,messageFragments,
            isOffscreenPageLimit = true,
            isUserInputEnabled = false
        )
        mBinding.messageIndicator.bindWechatViewPager2(mBinding.messageViewPager,titleDatas)
    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }
}