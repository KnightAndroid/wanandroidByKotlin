package com.knight.kotlin.module_eye_video_detail.fragment

import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.module_eye_video_detail.databinding.EyeVideoDetailFragmentBinding
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2025/5/20 16:44
 * @descript:评论的fragment
 */
@AndroidEntryPoint
@Route(path = RouteFragment.EyeVideo.EyeVideoCommentFragment)
class EyeVideoCommentFragment:BaseFragment<EyeVideoDetailFragmentBinding,EmptyViewModel>() {
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun EyeVideoDetailFragmentBinding.initView() {

    }
}