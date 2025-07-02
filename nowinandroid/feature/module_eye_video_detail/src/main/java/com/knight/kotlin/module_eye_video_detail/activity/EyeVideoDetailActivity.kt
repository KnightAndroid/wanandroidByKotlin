package com.knight.kotlin.module_eye_video_detail.activity

import android.transition.Transition
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.core.library_base.ktx.fromJson
import com.core.library_base.route.RouteActivity
import com.core.library_base.vm.EmptyViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.entity.EyeVideoDetailEntity
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.library_video.play.OkPlayer
import com.knight.kotlin.library_widget.ktx.transformShareElementConfig
import com.knight.kotlin.module_eye_video_detail.R
import com.knight.kotlin.module_eye_video_detail.databinding.EyeVideoDetailActivityBinding
import com.knight.kotlin.module_eye_video_detail.fragment.EyeVideoCommentFragment
import com.knight.kotlin.module_eye_video_detail.fragment.EyeVideoDetailFragment
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Param
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2024/5/13 9:28
 * Description:EyeVideoDetailActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.EyeVideo.EyeVideoDetail)
class EyeVideoDetailActivity : BaseActivity<EyeVideoDetailActivityBinding, EmptyViewModel>(),
    OnRefreshListener {
    private var mTransition: Transition? = null


    private val mTitleDatas = mutableListOf<String>("简介","评论")
    /**
     *
     * 详细实体
     */
    private lateinit var videoDetailData: EyeVideoDetailEntity

    @JvmField
    @Param(name = Appconfig.EYE_VIDEO_PARAM_KEY)
    var videoJson:String = ""




    private val mFragments = mutableListOf<Fragment>()

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun EyeVideoDetailActivityBinding.initView() {
        videoDetailData = fromJson(videoJson)
        mFragments.add(EyeVideoDetailFragment().also { fragment ->
            fragment.arguments = bundleOf("videoDetailData" to videoDetailData)
        })

        mFragments.add(EyeVideoCommentFragment().also { fragment ->
            fragment.arguments = bundleOf("videoId" to videoDetailData.videoId)
        })

        if (mFragments.size > 0) {
            ViewInitUtils.setViewPager2Init(this@EyeVideoDetailActivity,mBinding.eyeVideoDetailViewPager,mFragments,
                isOffscreenPageLimit = true,
                isUserInputEnabled = false
            )

            TabLayoutMediator(mBinding.eyeVideoDetailTabLayout, mBinding.eyeVideoDetailViewPager) { tab, pos ->
                tab.text = mTitleDatas[pos]
            }.attach()
        }



        eyeDetailRefreshLayout.setOnRefreshListener(this@EyeVideoDetailActivity)

        //注入xml中
        videoEntity = videoDetailData
        jzVideo.setUp(videoDetailData.videoUrl
            , videoDetailData.videoTitle)
        jzVideo.startVideo()
        jzVideo.setNormalBackListener(object : OkPlayer.NormalBackListener{
            override fun backFinifsh() {
                finishAfterTransition()
            }

        })
        initTransition()
        onBackPressed(true){
            OkPlayer.backPress()
            finishAfterTransition()
        }
    }

    private fun initTransition() {
        transformShareElementConfig(mBinding.jzVideo, getString(R.string.eye_video_share_image),{
           // mBinding.eyeDetailRefreshLayout.autoRefresh()
        })
    }





    /**
     * 手势返回
     *
     *
     * @param isEnabled
     * @param callback
     */
    fun AppCompatActivity.onBackPressed(isEnabled: Boolean, callback: () -> Unit) {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(isEnabled) {
            override fun handleOnBackPressed() {
                callback()

            }
        })

    }

    override fun onPause() {
        super.onPause()
        OkPlayer.releaseAllVideos()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mBinding.eyeDetailRefreshLayout.finishRefresh()

    }



}