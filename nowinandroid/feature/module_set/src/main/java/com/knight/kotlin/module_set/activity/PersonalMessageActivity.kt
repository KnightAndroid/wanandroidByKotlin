package com.knight.kotlin.module_set.activity

import com.core.library_base.contact.EmptyContract
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.core.library_base.util.ServiceApiFactory
import com.core.library_base.vm.EmptyMviViewModel
import com.knight.kotlin.library_base.activity.BaseMviActivity
import com.knight.kotlin.library_common.ktx.getUser
import com.knight.kotlin.module_set.R
import com.knight.kotlin.module_set.databinding.SetPersonalMessageActivityBinding
import com.knight.kotlin.module_set.entity.UserTempInfo
import com.knight.kotlin.module_set.external.MineExternalContact
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2023/5/8 16:14
 * Description:PersonalMessageActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Set.PersonalMessage)
class PersonalMessageActivity :
    BaseMviActivity<
            SetPersonalMessageActivityBinding,
            EmptyMviViewModel,
            EmptyContract.Event,
            EmptyContract.State,
            EmptyContract.Effect>() {

    override fun SetPersonalMessageActivityBinding.initView() {
        title = getString(R.string.set_personal_message)

        includeSetMessageToobar.baseTvTitle.text =
            getString(R.string.set_personal_message)

        includeSetMessageToobar.baseIvBack.setOnClick {
            finish()
        }

        renderUserInfo()
    }

    override fun initObserver() {}

    override fun initRequestData() {}

    override fun reLoadData() {}

    override fun renderState(state: EmptyContract.State) {}

    override fun handleEffect(effect: EmptyContract.Effect) {}

    override fun setThemeColor(isDarkMode: Boolean) {}

    // =========================
    // UI渲染
    // =========================

    private fun renderUserInfo() = with(mBinding) {
        val userInfo = collectUserInfo()

        tvUserName.text = userInfo.username
        tvUserEamil.text = userInfo.email
        tvCoinCount.text = userInfo.coinCount
        tvRank.text = userInfo.rank
    }

    // =========================
    // 数据获取（核心优化🔥）
    // =========================

    private fun collectUserInfo(): UserTempInfo {
        val user = getUser()

        val rank = getUserRank()

        return UserTempInfo(
            username = user?.username ?: "",
            email = user?.email.orEmpty(),
            coinCount = user?.coinCount?.toString() ?: "0",
            rank = rank
        )
    }

    private fun getUserRank(): String {
        val service =
            ServiceApiFactory.getInstance().getService(MineExternalContact::class.java)

        return service?.getUserRank().orEmpty()
    }
}
