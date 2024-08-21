package com.knight.kotlin.module_eye_discover.okplaystd

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.knight.kotlin.library_video.play.OkPlayer
import com.knight.kotlin.library_video.play.OkPlayerStd


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/20 16:08
 * @descript:播放帮助类
 */
class OkPlayerRv: OkPlayerStd {

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    private var clickUi: ClickUi? = null
    var isAtList = false
        set(value) {
            field = value
            if (field) bottomContainer.visibility = GONE
        }



    companion object {
        @JvmStatic
        fun getPlayer() : OkPlayer?{
            return CURRENT_JZVD
        }
    }
    fun setClickUi(clickUi: ClickUi?) {
        this.clickUi = clickUi
    }

//    fun isAtList(): Boolean {
//        return isAtList
//    }

//    fun setAtList(atList: Boolean) {
//        isAtList = atList
//        if (isAtList) bottomContainer.visibility = GONE
//    }



    override fun onClick(v: View) {
        if (isAtList) bottomContainer.visibility = GONE
        if (v.id == com.knight.kotlin.library_video.R.id.surface_container &&
            (state === STATE_PLAYING ||
                    state === STATE_PAUSE)
        ) {
             clickUi?.onClickUiToggle()
        }
        super.onClick(v)
    }

    override fun startVideo() {
        super.startVideo()
        clickUi?.onClickStart()
    }

    override fun setScreenNormal() {
        super.setScreenNormal()
        backButton.setVisibility(GONE)
    }

    interface ClickUi {
        fun onClickUiToggle()

        fun onClickStart()
    }
}