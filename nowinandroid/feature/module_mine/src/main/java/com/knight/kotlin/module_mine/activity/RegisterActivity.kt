package com.knight.kotlin.module_mine.activity

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.TextUtils
import android.util.Base64
import com.core.library_base.ktx.appStr
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.knight.kotlin.library_base.utils.CacheUtils
import com.core.library_base.util.GsonUtils
import com.core.library_base.util.dp2px
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.entity.LoginEntity
import com.knight.kotlin.library_base.entity.UserInfoEntity
import com.knight.kotlin.library_base.ktx.showLoading
import com.knight.kotlin.library_util.SoftInputScrollUtils
import com.knight.kotlin.library_util.SystemUtils
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_mine.R
import com.knight.kotlin.module_mine.databinding.MineRegisterActivityBinding
import com.knight.kotlin.module_mine.vm.RegisterViewModel
import com.knight.library_biometric.control.BiometricControl
import com.knight.library_biometric.listener.BiometricStatusCallback
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException

/**
 * Author:Knight
 * Time:2022/4/6 9:55
 * Description:RegisterActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Mine.RegisterActivity)
class RegisterActivity : BaseActivity<MineRegisterActivityBinding, RegisterViewModel>(){

    private val mSoftInputScrollUtils: SoftInputScrollUtils by lazy{ SoftInputScrollUtils(this@RegisterActivity) }
    override fun setThemeColor(isDarkMode: Boolean) {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        gradientDrawable.setColor(CacheUtils.getThemeColor())
        gradientDrawable.cornerRadius = 45.dp2px().toFloat()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mBinding.mineTvRegister.setBackground(gradientDrawable)
        } else {
            mBinding.mineTvRegister.setBackgroundDrawable(gradientDrawable)
        }

        val cursorDrawable = GradientDrawable()
        cursorDrawable.shape = GradientDrawable.RECTANGLE
        cursorDrawable.setColor(themeColor)
        cursorDrawable.setSize(2.dp2px(), 2.dp2px())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mBinding.mineRegisterUsername.textCursorDrawable = cursorDrawable
            mBinding.mineRegisterPassword.textCursorDrawable = cursorDrawable
            mBinding.mineRegisterConfirmpassword.textCursorDrawable = cursorDrawable
        } else {
            SystemUtils.setCursorDrawableColor(mBinding.mineRegisterUsername, themeColor)
            SystemUtils.setCursorDrawableColor(mBinding.mineRegisterPassword, themeColor)
            SystemUtils.setCursorDrawableColor(mBinding.mineRegisterConfirmpassword, themeColor)
        }


    }

    override fun MineRegisterActivityBinding.initView() {
        mSoftInputScrollUtils.moveBy(mBinding.mineRegisterRoot)
        mSoftInputScrollUtils.moveWith(mBinding.mineTvRegister,mBinding.mineRegisterUsername,mBinding.mineRegisterPassword,mBinding.mineRegisterConfirmpassword)
        mBinding.mineRegisterToolbar.baseTvTitle.setText(getString(R.string.mine_register))
        mBinding.mineRegisterToolbar.baseIvBack.setOnClickListener { finish() }
        mBinding.mineTvRegister.setOnClick {
            if (validateRegisterMessage()) {
                showLoading(appStr(R.string.mine_request_login))
                mViewModel.register(mBinding.mineRegisterUsername.text.toString().trim(),mBinding.mineRegisterPassword.text.toString().trim(),mBinding.mineRegisterConfirmpassword.text.toString().trim())
                    .observerKt {
                        setUserInfo(it)
                    }
            }
        }


    }


    /**
     *
     * 验证注册信息
     * @return
     */
    private fun validateRegisterMessage(): Boolean {
        var validFlag = true
        if (TextUtils.isEmpty(mBinding.mineRegisterUsername.getText().toString().trim())) {
            mBinding.mineRegisterUsername.setError(getString(R.string.mine_emptyusername_hint))
            validFlag = false
        } else if (TextUtils.isEmpty(mBinding.mineRegisterPassword.getText().toString().trim())) {
            mBinding.mineRegisterPassword.setError(getString(R.string.mine_emptypassword_hint))
            validFlag = false
        } else if (TextUtils.isEmpty(
                mBinding.mineRegisterConfirmpassword.getText().toString().trim()
            )
        ) {
            mBinding.mineRegisterConfirmpassword.setError(getString(R.string.mine_confirmpassowrd_notempty))
            validFlag = false
        } else if (!mBinding.mineRegisterPassword.getText().toString().trim()
                .equals(mBinding.mineRegisterConfirmpassword.getText().toString().trim())
        ) {
            mBinding.mineRegisterConfirmpassword.setError(getString(R.string.mine_passworduneaual))
            validFlag = false
        }
        return validFlag
    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }


    private fun setUserInfo(userInfo: UserInfoEntity) {
        val loginMessage = GsonUtils.toJson(
            LoginEntity(
                mBinding.mineRegisterUsername.text.toString().trim(),
                mBinding.mineRegisterPassword.text.toString().trim()
            )
        )
        CacheUtils.setLoginMessage(loginMessage)
        openBlomtric(loginMessage)

    }

    /**
     *
     * 开通指纹登录
     * @param loginMessage
     */
    private fun openBlomtric(loginMessage: String) {
        BiometricControl.setStatusCallback(object : BiometricStatusCallback {
            override fun onUsePassword() {
                finish()
            }

            override fun onVerifySuccess(cipher: Cipher?) {
                val bytes: ByteArray
                try {
                    bytes = cipher?.doFinal(loginMessage.toByteArray()) ?: ByteArray(0)
                    //保存加密过后的字符串
                    CacheUtils.setEncryptLoginMessage(Base64.encodeToString(bytes, Base64.URL_SAFE))
                    val iv = cipher?.iv
                    CacheUtils.setCliperIv(Base64.encodeToString(iv, Base64.URL_SAFE))
                    //保存开启了快捷登录
                    CacheUtils.setFingerLogin(true)
                    finish()
                } catch (e: BadPaddingException) {
                    e.printStackTrace()
                } catch (e: IllegalBlockSizeException) {
                    e.printStackTrace()
                }
            }

            override fun onFailed() {
                //登录成功发送事件
                finish()
            }

            override fun error(code: Int, reason: String?) {
                toast("$code,$reason")
                finish()
            }

            override fun onCancel() {
                finish()
            }
        }).openBlomtric(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        BiometricControl.setunListener()
    }

}