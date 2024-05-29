package com.knight.kotlin.module_eye_video_detail.activity

import android.os.Build
import android.transition.Transition
import android.transition.TransitionListenerAdapter
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.entity.EyeData
import com.knight.kotlin.library_base.ktx.fromJson
import com.knight.kotlin.library_base.ktx.showLoadingDialog
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.library_video.play.OkPlayer
import com.knight.kotlin.module_eye_video_detail.R
import com.knight.kotlin.module_eye_video_detail.databinding.EyeVideoDetailActivityBinding
import com.knight.kotlin.module_eye_video_detail.vm.EyeVideoDetailVm
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
class EyeVideoDetailActivity : BaseActivity<EyeVideoDetailActivityBinding,EyeVideoDetailVm>() {
    private var mTransition: Transition? = null

    /**
     *
     * 开眼播放视频详情实体
     */
    private lateinit var videoEyeData:EyeData

    @JvmField
    @Param(name = Appconfig.EYE_VIDEO_PARAM_KEY)
    var videoJson:String = ""
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        showLoadingDialog()
        mViewModel.getVideoDetail(videoEyeData.id).observerKt {
            toast("请求成功")
            Log.d("dsd","dsdd")
        }
    }

    override fun reLoadData() {

    }

    override fun EyeVideoDetailActivityBinding.initView() {
        videoEyeData = fromJson(videoJson)
        videoEntity = videoEyeData
        jzVideo.setUp(videoEyeData.playUrl
            , videoEyeData.title)
        jzVideo.startVideo()
        initTransition()
        onBackPressed(true){
            OkPlayer.backPress()
            finish()
        }
    }

    private fun initTransition() {
        //因为进入视频详情页面后还需请求数据，所以在过渡动画完成后在请求数据
        //延迟动画执行
        postponeEnterTransition()
        //设置共用元素对应的View
        ViewCompat.setTransitionName(mBinding.jzVideo, getString(R.string.eye_video_share_image))
        //获取共享元素进入转场对象
        mTransition = window.sharedElementEnterTransition
        //设置共享元素动画执行完成的回调事件
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mTransition?.addListener(object : TransitionListenerAdapter() {
                override fun onTransitionEnd(transition: Transition?) {
                   // getRelateVideoList()
                    //移除共享元素动画监听事件
                    mTransition?.removeListener(this)
                }
            })
        }
        //开始动画执行
        startPostponedEnterTransition()
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
}