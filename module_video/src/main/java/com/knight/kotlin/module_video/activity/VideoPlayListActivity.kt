package com.knight.kotlin.module_video.activity

import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.module_video.R
import com.knight.kotlin.module_video.databinding.VideoPlayListActivityBinding
import com.knight.kotlin.module_video.fragment.VideoPlayFragment
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2024/3/5 14:34
 * Description:VideoPlayListActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Video.VideoPlayListActivity)
class VideoPlayListActivity : BaseActivity<VideoPlayListActivityBinding,EmptyViewModel>() {
    companion object {
        var initPos = 0
    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun VideoPlayListActivityBinding.initView() {
        supportFragmentManager.beginTransaction().add(R.id.video_framelayout,VideoPlayFragment()).commit()
    }
}