package com.knight.kotlin.module_eye_video_detail.fragment

import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.core.library_base.route.RouteFragment

import com.knight.kotlin.library_util.RecyclerResizeHelper
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_eye_video_detail.adapter.EyeVideoCommentAdapter
import com.knight.kotlin.module_eye_video_detail.databinding.EyeVideoCommentFragmentBinding
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
class EyeVideoCommentFragment: BaseFragment<EyeVideoCommentFragmentBinding, EyeVideoCommentVm>() {
    private lateinit var recyclerResizeHelper: RecyclerResizeHelper
    private var videoId: Long = 0L
    private val mEyeVideoCommentAdapter : EyeVideoCommentAdapter by lazy { EyeVideoCommentAdapter(
        )
    }
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        videoId = arguments?.getLong("videoId")?:0L
        mViewModel.getVideoDetail(videoId,"pgc_video","hot").observerKt {
            mEyeVideoCommentAdapter.submitList(it.result?.item_list)
        }
    }

    override fun reLoadData() {

    }

    override fun EyeVideoCommentFragmentBinding.initView() {
        recyclerResizeHelper = RecyclerResizeHelper(
            activity = requireActivity(),
            rootView = clRootVideoComment,
            recyclerView = rvCommentVideo,
            anchorView = rlVideoComment // 键盘上方的输入栏
        )


        rvCommentVideo.init(
            LinearLayoutManager(requireActivity()),
            mEyeVideoCommentAdapter,
            false
        )


            etVideoComment.setOnEditorActionListener { _, _, event: KeyEvent? ->
                if (event?.keyCode == KEYCODE_ENTER) {
                    toast(getString(com.core.library_base.R.string.base_unavailable_tip))
                }
                false
            }

    }


    override fun onDestroy() {
        super.onDestroy()
        recyclerResizeHelper.detach()
    }
}