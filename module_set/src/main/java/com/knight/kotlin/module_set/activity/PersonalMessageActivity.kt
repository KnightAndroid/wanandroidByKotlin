package com.knight.kotlin.module_set.activity

import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.ktx.getUser
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.util.ServiceApiFactory
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.module_set.R
import com.knight.kotlin.module_set.databinding.SetPersonalMessageActivityBinding
import com.knight.kotlin.module_set.external.MineExternalContact
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2023/5/8 16:14
 * Description:PersonalMessageActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Set.PersonalMessage)
class PersonalMessageActivity : BaseActivity<SetPersonalMessageActivityBinding, EmptyViewModel>() {
    override val mViewModel: EmptyViewModel by viewModels()
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun SetPersonalMessageActivityBinding.initView() {
        includeSetMessageToobar.baseTvTitle.setText(R.string.set_personal_message)
        getUser()?.let {
            tvUserName.text = it.username
            tvUserEamil.text = if(it.email.isNullOrEmpty()) ""  else it.email
            tvCoinCount.text = it.coinCount.toString()
        }
        val mineExternalContact = ServiceApiFactory.getInstance().getService(MineExternalContact::class.java)
        mineExternalContact?.let {
            tvRank.text = it.getUserRank()
        }
        includeSetMessageToobar.baseIvBack.setOnClickListener { finish() }



    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }
}