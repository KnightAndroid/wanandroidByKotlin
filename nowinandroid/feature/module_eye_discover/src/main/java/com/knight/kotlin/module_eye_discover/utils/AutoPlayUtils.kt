package com.knight.kotlin.module_eye_discover.utils

import android.graphics.Rect
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.knight.kotlin.library_video.play.OkPlayer
import com.knight.kotlin.library_video.utils.VideoUtils


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/20 16:00
 * @descript:
 */
object AutoPlayUtils {
    var positionInList: Int = -1 //记录当前播放列表位置


    /**
     * @param firstVisiblePosition 首个可见item位置
     * @param lastVisiblePosition  最后一个可见item位置
     */
    fun onScrollPlayVideo(recyclerView: RecyclerView, viewId: Int, jzvdId: Int, firstVisiblePosition: Int, lastVisiblePosition: Int) {
        if (VideoUtils.isWifiConnected(recyclerView.context)) {
            for (i in 0..lastVisiblePosition - firstVisiblePosition) {
                val child = recyclerView.getChildAt(i)
                val view = child.findViewById<View>(viewId)
                if (view is ConstraintLayout) {
                    val player: OkPlayer = view.findViewById(jzvdId)
                    if (getViewVisiblePercent(player) == 1f) {
                        if (positionInList != i + firstVisiblePosition) {
                            player.startButton.performClick()
                        }
                        break
                    }
                }
            }
        }
    }
    /**
     * @param firstVisiblePosition 首个可见item位置
     * @param lastVisiblePosition  最后一个可见item位置
     * @param percent              当item被遮挡percent/1时释放,percent取值0-1
     */
    fun onScrollReleaseAllVideos(firstVisiblePosition: Int, lastVisiblePosition: Int, percent: Float) {
        if (OkPlayer.CURRENT_JZVD == null) return
        if (positionInList >= 0) {
            if ((positionInList <= firstVisiblePosition || positionInList >= lastVisiblePosition - 1)) {
                if (getViewVisiblePercent(OkPlayer.CURRENT_JZVD) < percent) {
                    OkPlayer.releaseAllVideos()
                }
            }
        }
    }

    /**
     * @param view
     * @return 当前视图可见比列
     */
    fun getViewVisiblePercent(view: View?): Float {
        if (view == null) {
            return 0f
        }
        val height = view.height.toFloat()
        val rect = Rect()
        if (!view.getLocalVisibleRect(rect)) {
            return 0f
        }
        val visibleHeight = (rect.bottom - rect.top).toFloat()
        return visibleHeight / height
    }
}