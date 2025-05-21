package com.knight.kotlin.library_util

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView

/**
 * 软键盘弹起时，根布局整体上移 + RecyclerView 动态缩放高度辅助类
 */
class RecyclerResizeHelper(
    private val activity: Activity,
    private val rootView: View,          // 整个根布局
    private val recyclerView: RecyclerView,
    private val anchorView: View         // 输入框所在底部布局
) : ViewTreeObserver.OnGlobalLayoutListener {

    private var originalRecyclerHeight: Int = 0
    private var isKeyboardOpen = false
    private var lastTranslationY = 0f
    private var rvLastTranslationY = 0f

    init {
        rootView.viewTreeObserver.addOnGlobalLayoutListener(this)
        recyclerView.post {
            originalRecyclerHeight = recyclerView.height
        }
    }

    fun detach() {
        if (rootView.viewTreeObserver.isAlive) {
            rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
    }

    override fun onGlobalLayout() {
        val rect = Rect()
        rootView.getWindowVisibleDisplayFrame(rect)

        val screenHeight = activity.resources.displayMetrics.heightPixels
        val visibleBottom = rect.bottom  // 当前可见区域底部（即键盘顶部）
        val keyboardHeight = screenHeight - visibleBottom

        val isNowOpen = keyboardHeight > screenHeight / 4

        if (isNowOpen != isKeyboardOpen) {
            isKeyboardOpen = isNowOpen
            if (isNowOpen) {
                adjustLayout(visibleBottom,keyboardHeight)
            } else {
                restoreLayout()
            }
        }
    }

//    private fun adjustLayout(visibleBottom: Int,keyboardHeight:Int) {
//        val location = IntArray(2)
//        recyclerView.getLocationOnScreen(location)
//        val recyclerBottom = location[1] + recyclerView.height
//
//        // 计算遮挡高度：RecyclerView 底部 - 键盘顶部（可见区域底部）
//        val overlap = recyclerBottom - visibleBottom
//
//        // 只有被遮挡时才上移
//        val translationY = if (overlap > 0) -overlap.toFloat() else 0f
//
//        if (translationY != lastTranslationY) {
//            rootView.translationY = translationY
//            lastTranslationY = translationY
//        }
//    }

    private fun adjustLayout(visibleBottom: Int,keyboardHeight:Int) {
        val location = IntArray(2)
        val location2 = IntArray(2)
        anchorView.getLocationOnScreen(location)

               recyclerView.getLocationOnScreen(location2)
        val anchorBottom = location[1] + anchorView.height
        val recyclerBottom = location2[1] + recyclerView.height

        val recycleroverlap = recyclerBottom - visibleBottom


        val overlap = anchorBottom - visibleBottom
        val translationY = if (overlap > 0) -overlap.toFloat() else 0f
        val rvTranslationY = if (recycleroverlap> 0) -recycleroverlap.toFloat() else 0f
        if (translationY != lastTranslationY) {
          //  ThreadUtils.postMainDelayed({
                anchorView.translationY = translationY
                lastTranslationY = translationY
          //  },500)

        }

//        if (rvTranslationY!= rvLastTranslationY) {
//            rootView.translationY = rvTranslationY
//            rvLastTranslationY = rvTranslationY
//        }
    }

    private fun restoreLayout() {
        if (lastTranslationY != 0f) {
            anchorView.translationY = 0f
            lastTranslationY = 0f
        }

//        if (rvLastTranslationY != 0f) {
//            rootView.translationY = 0f
//            rvLastTranslationY = 0f
//        }
    }

    private fun getViewLocationY(view: View): Int {
        val loc = IntArray(2)
        view.getLocationOnScreen(loc)
        return loc[1]
    }

    private fun getStatusBarHeight(): Int {
        val resId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resId > 0) activity.resources.getDimensionPixelSize(resId) else 0
    }
}
