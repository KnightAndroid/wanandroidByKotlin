package com.knight.kotlin.module_eye_daily.activity

import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_video.play.OkPlayer
import com.knight.kotlin.module_eye_daily.databinding.EyeDailyVideoActivityBinding
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2024/5/11 16:50
 * Description:EyeDailyVideoActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.EyeDaily.DailyVideoActivity)
class EyeDailyVideoActivity: BaseActivity<EyeDailyVideoActivityBinding,EmptyViewModel>() {
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun EyeDailyVideoActivityBinding.initView() {
        jzVideo.setUp("https://vdept3.bdstatic.com/mda-qdqxj7s4d6njpv18/sc/cae_h264/1714101678770158964/mda-qdqxj7s4d6njpv18.mp4?v_from_s=hkapp-haokan-hnb&auth_key=1715433807-0-0-85b634c6d2299b728862cb308cbfe6d5&bcevod_channel=searchbox_feed&pd=1&cr=2&cd=0&pt=3&logid=1407077959&vid=12292776055750791018&klogid=1407077959&abtest="
            , "权力、统治、战争、生死——「权力的游戏」混剪")
        jzVideo.startVideo()
        onBackPressed(true){
            OkPlayer.backPress()
            finish()
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
}