package com.knight.kotlin.module_eye_video_detail.fragment

import android.os.Build
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.library_base.entity.EyeVideoDetailEntity
import com.core.library_base.fragment.BaseFragment
import com.core.library_base.route.RouteFragment
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_eye_video_detail.adapter.EyeVideoRelateAdapter
import com.knight.kotlin.module_eye_video_detail.databinding.EyeVideoDetailFragmentBinding
import com.knight.kotlin.module_eye_video_detail.databinding.EyeVideoDetailHeadBinding
import com.knight.kotlin.module_eye_video_detail.vm.EyeVideoDetailVm
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2025/5/20 16:31
 * @descript:视频详情fragment
 */
@AndroidEntryPoint
@Route(path = RouteFragment.EyeVideo.EyeVideoDetailFragment)
class EyeVideoDetailFragment: BaseFragment<EyeVideoDetailFragmentBinding, EyeVideoDetailVm>() {



    private lateinit var mHeaderBinding: EyeVideoDetailHeadBinding
    //视频关联详情适配器
    private val mEyeVideoRelateAdapter: EyeVideoRelateAdapter by lazy { EyeVideoRelateAdapter(
        mutableListOf(),
        requireActivity()) }
    /**
     *
     * 详细实体
     */
    private lateinit var videoDetailData: EyeVideoDetailEntity
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            videoDetailData = arguments?.getParcelable("videoDetailData",EyeVideoDetailEntity::class.java)!!
        } else {
            videoDetailData = arguments?.getParcelable("videoDetailData")!!
        }

        getRelateVideoList()
    }

    override fun reLoadData() {

    }

    override fun EyeVideoDetailFragmentBinding.initView() {
        rvRelateVideo.init(
            LinearLayoutManager(requireActivity()),
            mEyeVideoRelateAdapter,
            false
        )
      //  ((rvRelateVideo.layoutManager) as LinearLayoutManager).initialPrefetchItemCount = 3
    }



    /**
     * 初始化头像
     */
    private fun initHeaderView () {
        if (mBinding.rvRelateVideo.headerCount == 0) {
            if (!::mHeaderBinding.isInitialized) {
                mHeaderBinding =
                    EyeVideoDetailHeadBinding.inflate(LayoutInflater.from(requireActivity()))
                mBinding.rvRelateVideo.addHeaderView(mHeaderBinding.root)
                mHeaderBinding.videoEntity = videoDetailData
            } else {
                mHeaderBinding.videoEntity = videoDetailData
            }
        }
    }


    private fun getRelateVideoList() {
        mViewModel.getVideoDetail(videoDetailData.videoId).observerKt {
            initHeaderView()
            mEyeVideoRelateAdapter.submitList(it.itemList)
        }
    }
}