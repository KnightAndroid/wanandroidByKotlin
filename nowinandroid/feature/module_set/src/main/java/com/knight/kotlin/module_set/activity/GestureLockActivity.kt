package com.knight.kotlin.module_set.activity

import android.util.Base64
import com.knight.kotlin.library_base.activity.BaseActivity
import com.core.library_base.route.RouteActivity
import com.core.library_common.util.CacheUtils
import com.core.library_base.vm.EmptyViewModel
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
class GestureLockActivity : BaseActivity<SetGesturelockActivityBinding, EmptyViewModel>() {


    private var oneGesturePassword:String = ""


    private val mOnSetPasswordListener:GestureLockView.OnSetPasswordListener = object:GestureLockView.OnSetPasswordListener{
        override fun setOneGesturePassword(onePassword: String) {
            oneGesturePassword = onePassword
        }

        override fun setTwoGesturePassword(twoPassword: String) {
            CacheUtils.setGesturePassword(Base64.encodeToString(twoPassword.toByteArray(),Base64.URL_SAFE))
            //设置手势登录
            CacheUtils.setGestureLogin(true)
            finish()
        }

        override fun setOnCheckPassword(passwd: String): Boolean {
            return oneGesturePassword.equals(passwd)
        }

        override fun setError(errorMsg: String) {
            toast(errorMsg)
        }
    }
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun SetGesturelockActivityBinding.initView() {
        customGestureLockView.setDotPressedColor(themeColor)
        customGestureLockView.setLineColor(themeColor)
        customGestureLockView.setPasswordListener(mOnSetPasswordListener)
        includeGestureToolbar.baseIvBack.setOnClickListener { finish() }
        includeGestureToolbar.baseTvTitle.setText(R.string.set_show_gesture_password)
    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }
}