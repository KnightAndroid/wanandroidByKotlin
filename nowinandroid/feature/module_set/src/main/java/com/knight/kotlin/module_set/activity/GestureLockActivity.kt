package com.knight.kotlin.module_set.activity

import android.util.Base64
import com.core.library_base.contact.EmptyContract
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.core.library_base.vm.EmptyMviViewModel
import com.knight.kotlin.library_base.activity.BaseMviActivity
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.library_widget.GestureLockView
import com.knight.kotlin.module_set.R
import com.knight.kotlin.module_set.databinding.SetGesturelockActivityBinding
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.kotlin.module_set.activity
 * @ClassName:      GestureLockActivity
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/6/27 9:06 下午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/6/27 9:06 下午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Set.SetGestureLockActivity)
class GestureLockActivity :
    BaseMviActivity<
            SetGesturelockActivityBinding,
            EmptyMviViewModel,
            EmptyContract.Event,
            EmptyContract.State,
            EmptyContract.Effect>() {

    /**
     * 第一次输入的手势
     */
    private var firstPassword: String = ""

    override fun SetGesturelockActivityBinding.initView() {
        title = getString(R.string.set_show_gesture_password)

        includeGestureToolbar.baseIvBack.setOnClick { finish() }
        includeGestureToolbar.baseTvTitle.text =
            getString(R.string.set_show_gesture_password)

        initGestureView()
    }

    override fun initObserver() {}

    override fun initRequestData() {}

    override fun reLoadData() {}

    override fun renderState(state: EmptyContract.State) {}

    override fun handleEffect(effect: EmptyContract.Effect) {}

    override fun setThemeColor(isDarkMode: Boolean) {}

    // =========================
    // 初始化
    // =========================

    private fun initGestureView() = with(mBinding.customGestureLockView) {
        setDotPressedColor(themeColor)
        setLineColor(themeColor)
        setPasswordListener(createGestureListener())
    }

    // =========================
    // 手势逻辑（核心🔥）
    // =========================

    private fun createGestureListener(): GestureLockView.OnSetPasswordListener {
        return object : GestureLockView.OnSetPasswordListener {

            override fun setOneGesturePassword(onePassword: String) {
                firstPassword = onePassword
            }

            override fun setTwoGesturePassword(twoPassword: String) {
                saveGesturePassword(twoPassword)
                finish()
            }

            override fun setOnCheckPassword(passwd: String): Boolean {
                return passwd == firstPassword
            }

            override fun setError(errorMsg: String) {
                toast(errorMsg)
            }
        }
    }

    // =========================
    // 业务逻辑
    // =========================

    private fun saveGesturePassword(password: String) {
        val encoded = encodePassword(password)

        CacheUtils.setGesturePassword(encoded)
        CacheUtils.setGestureLogin(true)
    }

    /**
     * 密码加密（可扩展🔥）
     */
    private fun encodePassword(password: String): String {
        return Base64.encodeToString(
            password.toByteArray(),
            Base64.URL_SAFE
        )
    }
}