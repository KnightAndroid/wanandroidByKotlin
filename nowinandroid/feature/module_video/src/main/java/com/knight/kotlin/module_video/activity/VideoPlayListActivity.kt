package com.knight.kotlin.module_video.activity

import android.graphics.Color
import com.knight.kotlin.library_base.activity.BaseActivity
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.knight.kotlin.library_base.utils.StatusBarUtils
import com.core.library_base.vm.EmptyViewModel

import com.knight.kotlin.module_video.R
import com.knight.kotlin.module_video.databinding.VideoPlayListActivityBinding
import com.knight.kotlin.module_video.fragment.VideoPlayFragment
import com.wyjson.router.annotation.Param
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2024/3/5 14:34
 * Description:VideoPlayListActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Video.VideoPlayListActivity)
class VideoPlayListActivity : BaseActivity<VideoPlayListActivityBinding, EmptyViewModel>() {

    @JvmField
    @Param(name = "curPos")
    var curPos:Int = 0

    override fun setThemeColor(isDarkMode: Boolean) {
         mBinding.includeVideoToolbar.baseIvBack.setBackgroundResource(com.core.library_base.R.drawable.base_iv_white_left_arrow)
         mBinding.includeVideoToolbar.baseTvTitle.setText(R.string.video_play_toolbar_title)
         mBinding.includeVideoToolbar.baseTvTitle.setTextColor(Color.WHITE)
         mBinding.includeVideoToolbar.baseCompatToolbar.setBackgroundResource(android.R.color.transparent)

    }

    override fun setStatusBar() {
        StatusBarUtils.transparentStatusBarWithFont(this,false)
    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun VideoPlayListActivityBinding.initView() {
        supportFragmentManager.beginTransaction().add(R.id.video_framelayout,VideoPlayFragment(curPos)).commit()
        includeVideoToolbar.baseIvBack.setOnClick {
            finish()
        }
    }
}