package com.knight.kotlin.module_mine.activity

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.TextUtils
import android.util.Base64
import androidx.activity.viewModels
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.config.CacheKey
import com.knight.kotlin.library_base.entity.LoginEntity
import com.knight.kotlin.library_base.entity.UserInfoEntity
import com.knight.kotlin.library_base.event.MessageEvent
import com.knight.kotlin.library_base.ktx.observeLiveData
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.EventBusUtils
import com.knight.kotlin.library_base.util.GsonUtils
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_util.SoftInputScrollUtils
import com.knight.kotlin.library_util.SystemUtils
import com.knight.kotlin.library_util.startPage
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_mine.R
import com.knight.kotlin.module_mine.databinding.MineLoginActivityBinding
import com.knight.kotlin.module_mine.vm.LoginViewModel
import com.knight.library_biometric.control.BiometricControl
import com.knight.library_biometric.listener.BiometricStatusCallback
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.kotlin.module_mine.activity
 * @ClassName:      LoginActivity
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/25 4:46 下午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/25 4:46 下午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

@AndroidEntryPoint
@Route(path = RouteActivity.Mine.LoginActivity)
class LoginActivity : BaseActivity<MineLoginActivityBinding,LoginViewModel>(){
    override val mViewModel: LoginViewModel by viewModels()

    private val mSoftInputScrollUtils: SoftInputScrollUtils by lazy{ SoftInputScrollUtils(this@LoginActivity) }
    private var mBiometricStatusCallback:BiometricStatusCallback?=null


    override fun setThemeColor(isDarkMode: Boolean) {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        gradientDrawable.setColor(CacheUtils.getThemeColor())
        gradientDrawable.cornerRadius = 45.dp2px().toFloat()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mBinding.mineTvLogin.setBackground(gradientDrawable)
        } else {
            mBinding.mineTvLogin.setBackgroundDrawable(gradientDrawable)
        }
        val cursorDrawable = GradientDrawable()
        cursorDrawable.shape = GradientDrawable.RECTANGLE
        cursorDrawable.setColor(themeColor)
        cursorDrawable.setSize(2.dp2px(), 2.dp2px())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mBinding.mineLoginUsername.textCursorDrawable = cursorDrawable
            mBinding.mineLoginPassword.textCursorDrawable = cursorDrawable
        } else {
            SystemUtils.setCursorDrawableColor(mBinding.mineLoginUsername, themeColor)
            SystemUtils.setCursorDrawableColor(mBinding.mineLoginPassword, themeColor)
        }

    }

    override fun MineLoginActivityBinding.initView() {
        mSoftInputScrollUtils.moveBy(mineLoginRoot)
        mSoftInputScrollUtils.moveWith(mineTvLogin,mineLoginUsername,mineLoginPassword)
        mineLoginToolbar.baseTvTitle.text = getString(R.string.mine_tv_login)
        mineLoginToolbar.baseIvBack.setOnClick {
            finish()
        }
        mineTvLogin.setOnClick {
            if (validateLoginMessage()) {
                mViewModel.login(mineLoginUsername.text.toString().trim(),mineLoginPassword.text.toString().trim())
            }

        }

        mineTvRegister.setOnClick {
            //跳到注册页面
            startPage(RouteActivity.Mine.RegisterActivity)
        }

    }



    private fun validateLoginMessage():Boolean{
        var validFlag = true
        if (TextUtils.isEmpty(mBinding.mineLoginUsername.text.toString().trim())) {
            mBinding.mineLoginUsername.setError(getString(R.string.mine_emptyusername_hint))
            validFlag = false
        } else if (TextUtils.isEmpty(mBinding.mineLoginPassword.text.toString().trim())) {
            mBinding.mineLoginPassword.setError(getString(R.string.mine_emptypassword_hint))
            validFlag = false
        }
        return validFlag
    }

    override fun initObserver() {
        observeLiveData(mViewModel.userInfo,::setUserInfo)
    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }


    private fun setUserInfo(userInfo:UserInfoEntity){
        //登录成功发送事件
        Appconfig.user = userInfo
        //保存用户信息
        CacheUtils.saveDataInfo(CacheKey.USER, userInfo)
        val loginMessage = GsonUtils.toJson(LoginEntity(mBinding.mineLoginUsername.text.toString().trim(),mBinding.mineLoginPassword.text.toString().trim()))
        if (!CacheUtils.getFingerLogin()) {
            //没开通就开通快捷登录
            openBlomtric(loginMessage)
        } else {
            //开通了 但是 对应存储的信息和输入账号信息不一致
            val localLoginMessage = CacheUtils.getLoginMessage()
            CacheUtils.setLoginMessage(loginMessage)
            if (localLoginMessage != localLoginMessage) {
                //判断本地存在的信息是否和页面信息一致 不一致为当前账号开启快捷登录
                openBlomtric(loginMessage)
            } else {
                EventBusUtils.postEvent(MessageEvent(MessageEvent.MessageType.LoginSuccess))
                finish()
            }
        }
    }
    /**
     * 开通指纹登录
     * @param loginMessage
     */
    private fun openBlomtric(loginMessage: String) {
        BiometricControl.setStatusCallback(object : BiometricStatusCallback{
            override fun onUsePassword() {
                CacheUtils.setLoginMessage(loginMessage)
                //登录成功发送事件
                EventBusUtils.postEvent(MessageEvent(MessageEvent.MessageType.LoginSuccess))
                finish()
            }

            override fun onVerifySuccess(cipher: Cipher?) {
                val bytes: ByteArray
                try {
                    bytes = cipher?.doFinal(loginMessage.toByteArray()) ?: ByteArray(0)
                    CacheUtils.setEncryptLoginMessage(Base64.encodeToString(bytes, Base64.URL_SAFE))
                    val iv = cipher?.iv
                    CacheUtils.setCliperIv(Base64.encodeToString(iv, Base64.URL_SAFE))
                    CacheUtils.setLoginMessage(loginMessage)
                    //保存开启了指纹登录
                    CacheUtils.setFingerLogin(true)
                    //登录成功发送事件
                    EventBusUtils.postEvent(MessageEvent(MessageEvent.MessageType.LoginSuccess))
                    finish()
                } catch (e: BadPaddingException) {
                    e.printStackTrace()
                } catch (e: IllegalBlockSizeException) {
                    e.printStackTrace()
                }
            }

            override fun onFailed() {
                //登录成功发送事件
                CacheUtils.setLoginMessage(loginMessage)
                EventBusUtils.postEvent(MessageEvent(MessageEvent.MessageType.LoginSuccess))
                finish()
            }

            override fun error(code: Int, reason: String?) {
                toast("$code,$reason")
                CacheUtils.setLoginMessage(loginMessage)
                //登录成功发送事件
                EventBusUtils.postEvent(MessageEvent(MessageEvent.MessageType.LoginSuccess))
                finish()
            }

            override fun onCancel() {
                toast(R.string.mine_tv_quicklogin_cancel)
                CacheUtils.setLoginMessage(loginMessage)
                //登录成功发送事件
                EventBusUtils.postEvent(MessageEvent(MessageEvent.MessageType.LoginSuccess))
                finish()
            }
        }).openBlomtric(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        BiometricControl.setunListener()
    }
}