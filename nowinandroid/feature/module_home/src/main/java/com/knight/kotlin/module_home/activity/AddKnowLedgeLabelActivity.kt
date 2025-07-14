package com.knight.kotlin.module_home.activity

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import com.knight.kotlin.library_base.activity.BaseActivity
import com.core.library_base.ktx.ActivityMessenger
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.core.library_common.util.CacheUtils
import com.core.library_common.dp2px
import com.core.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_util.SystemUtils
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_home.R
import com.knight.kotlin.module_home.databinding.HomeAddlabelActivityBinding
import com.knight.kotlin.module_home.utils.InputUtils
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/4/25 16:08
 * Description:AddKnowLedgeLabelActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Home.AddKnowLedgeLableActivity)
class AddKnowLedgeLabelActivity : BaseActivity<HomeAddlabelActivityBinding, EmptyViewModel>() {

    override fun setThemeColor(isDarkMode: Boolean) {
        val cursorDrawable = GradientDrawable()
        cursorDrawable.shape = GradientDrawable.RECTANGLE
        cursorDrawable.setColor(themeColor)
        cursorDrawable.setSize(2.dp2px(), 2.dp2px())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mBinding.homeInputlabelEt.setTextCursorDrawable(cursorDrawable)
        } else {
            SystemUtils.setCursorDrawableColor(mBinding.homeInputlabelEt, themeColor)
        }
    }

    override fun HomeAddlabelActivityBinding.initView() {
        includeAddLabel.baseTvTitle.setText(getString(R.string.home_addlabel))
        includeAddLabel.baseIvBack.setOnClick {
            finish()
        }
        homeInputlabelBottomView.setBackgroundColor(CacheUtils.getThemeColor())
        includeAddLabel.baseTvRight.visibility = View.VISIBLE
        includeAddLabel.baseTvRight.setText(R.string.home_save_add_label)
        includeAddLabel.baseTvRight.setOnClick {
            //返回上个界面
            if (!homeInputlabelEt.text.toString().trim().isNullOrEmpty()) {
                ActivityMessenger.finish(this@AddKnowLedgeLabelActivity, "label_data" to homeInputlabelEt.text.toString().trim())
            } else {
                toast(R.string.home_input_label_empty)
            }

        }
        InputUtils.setLinstenerInputNumber(homeInputlabelEt,homeTvLabelnumber)
    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

}