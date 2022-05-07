package com.knight.kotlin.module_mine.fragment

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.util.Base64
import android.view.View
import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.knight.kotlin.library_aop.clickintercept.SingleClick
import com.knight.kotlin.library_base.annotation.EventBusRegister
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.config.CacheKey
import com.knight.kotlin.library_base.entity.LoginEntity
import com.knight.kotlin.library_base.entity.UserInfoEntity
import com.knight.kotlin.library_base.event.MessageEvent
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.ktx.dismissLoading
import com.knight.kotlin.library_base.ktx.getUser
import com.knight.kotlin.library_base.ktx.observeLiveData
import com.knight.kotlin.library_base.ktx.observeLiveDataWithError
import com.knight.kotlin.library_base.ktx.showLoading
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.ColorUtils
import com.knight.kotlin.library_base.util.EventBusUtils
import com.knight.kotlin.library_base.util.GsonUtils
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.library_util.startPage
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_mine.R
import com.knight.kotlin.module_mine.activity.LoginActivity
import com.knight.kotlin.module_mine.activity.QuickLoginActivity
import com.knight.kotlin.module_mine.databinding.MineFragmentBinding
import com.knight.kotlin.module_mine.entity.UserInfoCoinEntity
import com.knight.kotlin.module_mine.vm.MineViewModel
import com.knight.library_biometric.control.BiometricControl
import com.knight.library_biometric.listener.BiometricStatusCallback
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException

/**nab
 * Author:Knight
 * Time:2021/12/23 18:12
 * Description:MineFragment
 */
@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteFragment.Mine.MineFragment)
class MineFragment: BaseFragment<MineFragmentBinding, MineViewModel>() {
    override val mViewModel: MineViewModel by viewModels()
    override fun MineFragmentBinding.initView() {
         getUser()?.let {
             mineTvUsername.text = it.username
             mineIvMessage.visibility = View.VISIBLE
         } ?: run {
             mineIvMessage.visibility = View.GONE
         }

        setOnClickListener(mineTvUsername,mineRlSetup,mineLlRank)

    }

    override fun initObserver() {
        observeLiveDataWithError(mViewModel.userInfoCoin,mViewModel.requestSuccessFlag,::setUserInfoCoin,::requestUserInfoCoinError)
        observeLiveData(mViewModel.userInfo,::setUserInfo)
    }

    override fun initRequestData() {
        getUser()?.let {
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
        dismissLoading()
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
     * 登录完信息
     */
    private fun setUserInfo(userInfo:UserInfoEntity) {

        mViewModel.getUserInfoCoin()
        mBinding.mineIvMessage.visibility = View.VISIBLE
        Appconfig.user = userInfo
        //保存用户信息
        CacheUtils.saveDataInfo(CacheKey.USER,userInfo)
        //登录成功发送事件
        EventBusUtils.postEvent(MessageEvent(MessageEvent.MessageType.LoginSuccess))
    }

    /**
     *
     * 请求用户信息接口错误
     */
    private fun requestUserInfoCoinError(){
        requestSuccess()
    }

    override fun reLoadData() {
        getUser()?.let {
            mViewModel.getUserInfoCoin()
        }
    }


    @SingleClick
    override fun onClick(v: View) {
        when (v) {
            mBinding.mineTvUsername -> {
                if (getUser() == null) {
                    if (CacheUtils.getGestureLogin() == true) {
                        //如果开启手势登陆
                        startActivity(Intent(activity,QuickLoginActivity::class.java))
                    } else if (CacheUtils.getFingerLogin()) {
                        loginBlomtric()
                    } else {
                        startActivity(Intent(activity, LoginActivity::class.java))
                    }
                }
            }
            mBinding.mineRlSetup -> {
                  ARouter.getInstance().build(RouteActivity.Set.SetActivity).navigation()
            }

            mBinding.mineLlRank -> {
                  startPage(RouteActivity.Mine.UserCoinRankActivity)
            }

        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event:MessageEvent) {
        when (event.type) {
            MessageEvent.MessageType.LoginSuccess ->{
                //登录成功 请求金币信息
                showLoading(getString(R.string.mine_request_loading))
                mViewModel.getUserInfoCoin()
                mBinding.mineIvMessage.visibility = View.VISIBLE

            }

            MessageEvent.MessageType.LogoutSuccess -> {
                mBinding.mineTvUserabbr.setText("")
                mBinding.mineTvUsername.setText(getString(R.string.mine_please_login))
                mBinding.mineTvLevel.setText(getString(R.string.mine_nodata_gradle))
                mBinding.mineTvRank.setText(getString(R.string.mine_nodata_rank))
                mBinding.mineTvPoints.setText("")
                mBinding.mineIvMessage.setVisibility(View.GONE)
                mBinding.mineIvHead.setBackground(null)
                ImageLoader.loadCircleIntLocalPhoto(
                    requireActivity(),
                    R.drawable.mine_iv_default_head,
                    mBinding.mineIvHead
                )
            }
        }

    }


    /**
     * 指纹登录
     */
    private fun loginBlomtric() {
        BiometricControl.setStatusCallback(object : BiometricStatusCallback{
            override fun onUsePassword() {
                startActivity(Intent(activity, LoginActivity::class.java))
            }

            override fun onVerifySuccess(cipher: Cipher?) {
                try {
                    val text = CacheUtils.getEncryptLoginMessage()
                    val input = Base64.decode(text, Base64.URL_SAFE)
                    val bytes = cipher?.doFinal(input)

                    /**
                     * 然后这里用原密码(当然是加密过的)调登录接口
                     */
                    val loginEntity: LoginEntity? =
                        GsonUtils.get(String(bytes ?: ByteArray(0)), LoginEntity::class.java)
                    val iv = cipher?.iv
                    loginEntity?.let {
                        mViewModel.login(it.loginName,it.loginPassword)
                    }
                } catch (e: BadPaddingException) {
                    e.printStackTrace()
                } catch (e: IllegalBlockSizeException) {
                    e.printStackTrace()
                }
            }

            override fun onFailed() {
                startActivity(Intent(activity, LoginActivity::class.java))
            }

            override fun error(code: Int, reason: String?) {
                toast("$code,$reason")
                startActivity(Intent(activity, LoginActivity::class.java))
            }

            override fun onCancel() {
                toast(R.string.mine_tv_quicklogin_cancel)
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }).loginBlomtric(requireActivity())
    }


    override fun onDestroy() {
        super.onDestroy()
        BiometricControl.setunListener()
    }


}