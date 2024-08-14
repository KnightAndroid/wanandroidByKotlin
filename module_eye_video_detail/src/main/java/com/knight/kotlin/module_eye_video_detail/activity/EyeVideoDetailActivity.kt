package com.knight.kotlin.module_eye_video_detail.activity

import android.transition.Transition
import android.view.LayoutInflater
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.entity.EyeData
import com.knight.kotlin.library_base.ktx.fromJson
import com.knight.kotlin.library_base.ktx.init
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_video.play.OkPlayer
import com.knight.kotlin.library_widget.ktx.transformShareElementConfig
import com.knight.kotlin.module_eye_video_detail.R
import com.knight.kotlin.module_eye_video_detail.adapter.EyeVideoRelateAdapter
import com.knight.kotlin.module_eye_video_detail.databinding.EyeVideoDetailActivityBinding
import com.knight.kotlin.module_eye_video_detail.databinding.EyeVideoDetailHeadBinding
import com.knight.kotlin.module_eye_video_detail.vm.EyeVideoDetailVm
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
class EyeVideoDetailActivity : BaseActivity<EyeVideoDetailActivityBinding,EyeVideoDetailVm>(),
    OnRefreshListener {
    private var mTransition: Transition? = null

    /**
     *
     * 开眼播放视频详情实体
     */
    private lateinit var videoEyeData:EyeData

    @JvmField
    @Param(name = Appconfig.EYE_VIDEO_PARAM_KEY)
    var videoJson:String = ""

    //日报适配器
    private val mEyeVideoRelateAdapter: EyeVideoRelateAdapter by lazy { EyeVideoRelateAdapter(
        mutableListOf(),
        this) }


    private lateinit var mHeaderBinding: EyeVideoDetailHeadBinding


    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun EyeVideoDetailActivityBinding.initView() {
        rvRelateVideo.init(
            LinearLayoutManager(this@EyeVideoDetailActivity),
            mEyeVideoRelateAdapter,
            true
        )
        ((rvRelateVideo.layoutManager) as LinearLayoutManager).initialPrefetchItemCount = 3
        eyeDetailRefreshLayout.setOnRefreshListener(this@EyeVideoDetailActivity)
        videoEyeData = fromJson(videoJson)
        //注入xml中
        videoEntity = videoEyeData
        jzVideo.setUp(videoEyeData.playUrl
            , videoEyeData.title)
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
            mBinding.eyeDetailRefreshLayout.autoRefresh()
        })
    }


    /**
     * 初始化头像
     */
    private fun initHeaderView () {
        if (mBinding.rvRelateVideo.headerCount == 0) {
            if (!::mHeaderBinding.isInitialized) {
                mHeaderBinding =
                    EyeVideoDetailHeadBinding.inflate(LayoutInflater.from(this@EyeVideoDetailActivity))
                mBinding.rvRelateVideo.addHeaderView(mHeaderBinding.root)
                mHeaderBinding.videoEntity = videoEyeData
            } else {
                mHeaderBinding.videoEntity = videoEyeData
            }

            mHeaderBinding.expandTextView.resetState(true)
        }
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
        getRelateVideoList()
    }

    private fun getRelateVideoList() {
        mViewModel.getVideoDetail(videoEyeData.id).observerKt {
            mBinding.eyeDetailRefreshLayout.finishRefresh()
            initHeaderView()
            mEyeVideoRelateAdapter.submitList(it.itemList)


        }
    }

}