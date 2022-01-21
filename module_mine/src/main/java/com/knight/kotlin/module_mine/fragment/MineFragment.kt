package com.knight.kotlin.module_mine.fragment

import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.knight.kotlin.library_base.BaseApp
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.ktx.observeLiveDataWithError
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.util.ColorUtils
import com.knight.kotlin.module_mine.R
import com.knight.kotlin.module_mine.databinding.MineFragmentBinding
import com.knight.kotlin.module_mine.entity.UserInfoCoinEntity
import com.knight.kotlin.module_mine.vm.MineViewModel
import dagger.hilt.android.AndroidEntryPoint

/**nab
 * Author:Knight
 * Time:2021/12/23 18:12
 * Description:MineFragment
 */
@AndroidEntryPoint
@Route(path = RouteFragment.Mine.MineFragment)
class MineFragment: BaseFragment<MineFragmentBinding, MineViewModel>() {
    override val mViewModel: MineViewModel by viewModels()
    override fun MineFragmentBinding.initView() {
         BaseApp.user?.let {
             mineTvUsername.text = it.username
             mineIvMessage.visibility = View.VISIBLE
         } ?: run {
             mineIvMessage.visibility = View.GONE
         }
    }

    override fun initObserver() {
        observeLiveDataWithError(mViewModel.userInfoCoin,mViewModel.requestSuccessFlag,::setUserInfoCoin,::requestUserInfoCoinError)
    }

    override fun initRequestData() {
        if (BaseApp.user != null) {
            requestLoading(mBinding.mineSl)
            mViewModel.getUserInfoCoin()
        }
    }

    override fun setThemeColor(isDarkMode: Boolean) {
        themeColor?.let {
            mBinding.mineTvUsername.setTextColor(it)
            mBinding.mineTvLevel.setTextColor(it)
            mBinding.mineTvRank.setTextColor(it)
            mBinding.mineCv.setCardBackgroundColor(it)
        }
    }


    /**
     *
     * 设置用户金币，排名
     */
    private fun setUserInfoCoin(userInfoCoinEntity: UserInfoCoinEntity) {
        requestSuccess()
        //设置头像
        val gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.OVAL
        gradientDrawable.setColor(ColorUtils.getRandColorCode())
        mBinding.mineIvHead.background = gradientDrawable
        mBinding.mineTvUserabbr.text = userInfoCoinEntity.username.substring(0,1)
        mBinding.mineTvUsername.text = userInfoCoinEntity.username
        mBinding.mineTvLevel.text = getString(R.string.mine_gradle) + userInfoCoinEntity.level
        mBinding.mineTvRank.text = getString(R.string.mine_rank) + userInfoCoinEntity.rank
        mBinding.mineTvPoints.text = userInfoCoinEntity.coinCount.toString()
    }

    /**
     *
     * 请求用户信息接口错误
     */
    private fun requestUserInfoCoinError(){
        requestSuccess()
    }
}