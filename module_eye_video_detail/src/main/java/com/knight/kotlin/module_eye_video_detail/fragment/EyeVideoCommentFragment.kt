package com.knight.kotlin.module_eye_video_detail.fragment

import com.knight.kotlin.library_base.entity.EyeVideoDetailEntity
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_util.LogUtils
import com.knight.kotlin.module_eye_video_detail.databinding.EyeVideoCommentFragmentBinding
import com.knight.kotlin.module_eye_video_detail.databinding.EyeVideoDetailFragmentBinding
import com.knight.kotlin.module_eye_video_detail.vm.EyeVideoCommentVm
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
class EyeVideoCommentFragment:BaseFragment<EyeVideoCommentFragmentBinding,EyeVideoCommentVm>() {

    private var videoId: Long = 0L

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        videoId = arguments?.getLong("videoId")?:0L
        mViewModel.getVideoDetail(videoId,"pgc_video","hot").observerKt {
            LogUtils.d("sdsd"+it.result?.item_list?.size+"")
        }
    }

    override fun reLoadData() {

    }

    override fun EyeVideoCommentFragmentBinding.initView() {

    }

}