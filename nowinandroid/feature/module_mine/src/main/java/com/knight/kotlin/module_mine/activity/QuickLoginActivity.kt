package com.knight.kotlin.module_mine.activity

import android.content.Intent
import android.util.Base64
import com.core.library_base.event.MessageEvent
import com.core.library_base.ktx.appStr
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.core.library_common.util.CacheUtils
import com.core.library_base.util.EventBusUtils
import com.core.library_base.util.GsonUtils
import com.knight.kotlin.library_base.activity.BaseActivity
import com.core.library_common.config.Appconfig
import com.core.library_common.config.CacheKey
import com.knight.kotlin.library_base.entity.LoginEntity
import com.core.library_common.entity.UserInfoEntity
import com.knight.kotlin.library_base.ktx.showLoading
import com.knight.kotlin.library_util.DateUtils
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.library_widget.GestureLockView
import com.knight.kotlin.module_mine.R
import com.knight.kotlin.module_mine.databinding.MineQuickloginActivityBinding
import com.knight.kotlin.module_mine.dialog.QuickBottomDialog
import com.knight.kotlin.module_mine.vm.QuickLoginViewModel
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
 * @ClassName:      QuickLoginActivity
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/23 3:03 下午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/23 3:03 下午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

@AndroidEntryPoint
@Route(path = RouteActivity.Mine.QuickLoginActivity)
class QuickLoginActivity :  BaseActivity<MineQuickloginActivityBinding, QuickLoginViewModel>(),QuickBottomDialog.FingureLoginListener{



    private var mQuickBottomDialog: QuickBottomDialog? = null

    var onCheckPasswordListener: GestureLockView.OnCheckPasswordListener = object : GestureLockView.OnCheckPasswordListener {
        override fun onCheckPassword(passwd: String?): Boolean {
            val gesturePassword: ByteArray =
                Base64.decode(CacheUtils.getGesturePassword(), Base64.URL_SAFE)
            return passwd == String(gesturePassword)
        }

        override fun onSuccess() {
            //调用登录接口
            val localLoginMessage: String = CacheUtils.getLoginMessage()
            val loginEntity: LoginEntity? = GsonUtils.get(localLoginMessage, LoginEntity::class.java)
            loginEntity?.let {
                showLoading(appStr(R.string.mine_request_login))
                mViewModel.login(it.loginName,it.loginPassword).observerKt {
                    setUserInfo(it)
                }
            } ?: run{
                toast(R.string.mine_quick_gesture_login_failure)
            }


        }

        override fun onError(errorMsg: String?) {
            toast(errorMsg ?: "")
        }
    }
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun MineQuickloginActivityBinding.initView() {
        mineTvGestureTime.text = DateUtils.convertTime() + "~"
        includeQuickLoginToolbar.baseTvTitle.setText(R.string.mine_quick_login)
        mineGesturelock.setDotPressedColor(themeColor)
        mineGesturelock.setLineColor(themeColor)
        includeQuickLoginToolbar.baseIvBack.setOnClick { finish() }
        mineGesturelock.setOnCheckPasswordListener(onCheckPasswordListener)
        tvGestureLockMore.setOnClick {
            mQuickBottomDialog = QuickBottomDialog()
            mQuickBottomDialog?.setFingureLoginListener(this@QuickLoginActivity)
            mQuickBottomDialog?.showAllowingStateLoss(
                supportFragmentManager,
                "QuickBottomFragment"
            )
        }
    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun fingureQuick() {
        BiometricControl.setStatusCallback(object : BiometricStatusCallback{
            override fun onUsePassword() {
                startActivity(
                    Intent(this@QuickLoginActivity, LoginActivity::class.java)
                )
                finish()
            }

            override fun onVerifySuccess(cipher: Cipher?) {
                val text = CacheUtils.getEncryptLoginMessage()
                val input = Base64.decode(text, Base64.URL_SAFE)
                val bytes: ByteArray
                try {
                    bytes = cipher?.doFinal(input) ?: ByteArray(0)
                    /**
                     * 然后这里用原密码(当然是加密过的)调登录接口
                     */
                    val loginEntity = GsonUtils[String(bytes), LoginEntity::class.java]
                    val iv = cipher?.iv
                    loginEntity?.let {
                        showLoading(appStr(R.string.mine_request_login))
                        mViewModel.login(it.loginName,it.loginPassword).observerKt {
                            setUserInfo(it)
                        }
                    } ?: run {
                        toast(R.string.mine_quick_fingure_login_failure)
                    }


                } catch (e: BadPaddingException) {
                    e.printStackTrace()
                } catch (e: IllegalBlockSizeException) {
                    e.printStackTrace()
                }
            }

            override fun onFailed() {
                startActivity(Intent(this@QuickLoginActivity, LoginActivity::class.java))
            }

            override fun error(code: Int, reason: String?) {
                toast("$code,$reason")
            }

            override fun onCancel() {
                toast(R.string.mine_tv_quicklogin_cancel)
            }
        }).loginBlomtric(this)
    }


    private fun setUserInfo(userInfo: UserInfoEntity) {
        Appconfig.user = userInfo
        //保存用户信息
        CacheUtils.saveDataInfo(CacheKey.USER,userInfo)
        //登录成功发送事件
        EventBusUtils.postEvent(MessageEvent(MessageEvent.MessageType.LoginSuccess))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        BiometricControl.setunListener()
    }
}