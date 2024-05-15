package com.knight.kotlin.module_eye_video_detail.activity

import android.os.Build
import android.transition.Transition
import android.transition.TransitionListenerAdapter
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.knight.kotlin.library_base.activity.BaseActivity
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


    @JvmField
    @Param(name = "videoId")
    var videoId:Long = 0L
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        showLoadingDialog()
        mViewModel.getVideoDetail(videoId).observerKt {
            toast("请求成功")
        }
    }

    override fun reLoadData() {

    }

    override fun EyeVideoDetailActivityBinding.initView() {
        jzVideo.setUp("https://vdept3.bdstatic.com/mda-qdqxj7s4d6njpv18/sc/cae_h264/1714101678770158964/mda-qdqxj7s4d6njpv18.mp4?v_from_s=hkapp-haokan-hnb&auth_key=1715433807-0-0-85b634c6d2299b728862cb308cbfe6d5&bcevod_channel=searchbox_feed&pd=1&cr=2&cd=0&pt=3&logid=1407077959&vid=12292776055750791018&klogid=1407077959&abtest="
            , "权力、统治、战争、生死——「权力的游戏」混剪")
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