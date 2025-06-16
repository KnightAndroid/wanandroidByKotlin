package com.knight.kotlin.library_util

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.Window
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.kotlin.library_util
 * @ClassName:      SoftInputScrollUtils
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/24 4:26 下午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/24 4:26 下午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

class SoftInputScrollUtils constructor(activity:Activity) : OnGlobalLayoutListener, OnGlobalFocusChangeListener {

    private var window: Window = activity.window
    private var rootView: View = window.getDecorView().rootView

    private var duration: Long = 300
    private var moveView: View? = null
    private val focusBottomMap: HashMap<View, View> = HashMap<View,View>()
    private var onSoftInputListener: OnSoftInputListener? =
        null

    private var moveWithScroll = false

    private var isOpened = false
    private var moveHeight = 0
    private var isFocusChange = false

    private val moveRunnable = Runnable { calcToMove() }

    companion object {
        fun attach(activity: Activity): SoftInputScrollUtils {
            return SoftInputScrollUtils(activity)
        }
    }

    init {
        val observer = rootView.getViewTreeObserver()
        observer?.addOnGlobalLayoutListener(this)
        observer?.addOnGlobalFocusChangeListener(this)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

    }

    fun detach() {
        if (rootView.viewTreeObserver.isAlive) {
            rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            rootView.viewTreeObserver.removeOnGlobalFocusChangeListener(this)
        }
    }

    fun moveBy(moveView: View?): SoftInputScrollUtils {
        this.moveView = moveView
        return this
    }

    fun moveWith(bottomView: View, vararg focusViews: View): SoftInputScrollUtils {
        for (focusView in focusViews) {
            focusBottomMap.put(focusView, bottomView)
        }
        return this
    }

    fun listener(onSoftInputListener: OnSoftInputListener?): SoftInputScrollUtils {
        this.onSoftInputListener = onSoftInputListener
        return this
    }

    fun duration(duration: Long): SoftInputScrollUtils? {
        this.duration = duration
        return this
    }

    /**
     * 设置moveView移动以ScrollY属性滚动内容
     *
     * @return SoftInputHelper
     */
    fun moveWithScroll(): SoftInputScrollUtils {
        moveWithScroll = true
        return this
    }

    /**
     * 设置moveView移动以TranslationY属性移动位置
     *
     * @return SoftInputHelper
     */
    fun moveWithTranslation(): SoftInputScrollUtils {
        moveWithScroll = false
        return this
    }
    override fun onGlobalLayout() {
        val isOpen: Boolean = isSoftOpen()
        if (isOpen) {
            if (!isOpened) {
                isOpened = true
                onSoftInputListener?.onOpen()

            }
            if (moveView != null) {
                if (isFocusChange) {
                    isFocusChange = false
                    rootView.removeCallbacks(moveRunnable)
                }
                calcToMove()
            }
        } else {
            if (isOpened) {
                isOpened = false
                onSoftInputListener?.onClose()

            }
            if (moveView != null) {
                moveHeight = 0
                move()
            }
        }
    }

    private fun calcToMove() {
        val focusView: View? = isViewFocus()
        if (focusView != null) {
            val bottomView = focusBottomMap[focusView]
            if (bottomView != null) {
                val rect: Rect? = getRootViewRect()
                val bottomViewY: Int = getBottomViewY(bottomView)
                if (bottomViewY > rect?.bottom ?: 0) {
                    val offHeight = bottomViewY - (rect?.bottom?:0)
                    moveHeight += offHeight
                    move()
                } else if (bottomViewY < rect?.bottom?: 0) {
                    val offHeight = -(bottomViewY - (rect?.bottom?:0))
                    if (moveHeight > 0) {
                        if (moveHeight >= offHeight) {
                            moveHeight -= offHeight
                        } else {
                            moveHeight = 0
                        }
                        move()
                    }
                }
            }
        } else {
            moveHeight = 0
            move()
        }
    }

    override fun onGlobalFocusChanged(oldFocus: View?, newFocus: View?) {
        if (isOpened) {
            if (moveView != null) {
                isFocusChange = true
                rootView.postDelayed(moveRunnable, 100)
            }
        }
    }

    private fun getBottomViewY(bottomView: View): Int {
        val bottomLocation = IntArray(2)
        bottomView.getLocationOnScreen(bottomLocation)
        return bottomLocation[1] + bottomView.height
    }

    private fun getRootViewRect(): Rect {
        val rect = Rect()
        rootView.getWindowVisibleDisplayFrame(rect)
        return rect
    }

    private fun move() {
        if (moveWithScroll) {
            scrollTo(moveHeight)
        } else {
            translationTo(-moveHeight)
        }
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun translationTo(to: Int) {
        val translationY = moveView?.translationY
        if (translationY == to.toFloat()) {
            return
        }
        val anim = ObjectAnimator.ofFloat(moveView, "translationY", translationY ?: 0f, to.toFloat())
        anim.interpolator = DecelerateInterpolator()
        anim.duration = duration
        anim.start()
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun scrollTo(to: Int) {
        val scrollY = moveView?.scrollY
        if (scrollY == to) {
            return
        }
        val anim = ObjectAnimator.ofInt(moveView, "scrollY", scrollY ?:0, to)
        anim.interpolator = DecelerateInterpolator()
        anim.duration = duration
        anim.start()
    }

    /**
     * 判断软键盘打开状态的阈值
     * 此处以用户可用高度变化值大于1/4总高度时作为判断依据。
     *
     * @return boolean
     */
    private fun isSoftOpen(): Boolean {
        val rect = getRootViewRect()
        val usableHeightNow = rect!!.bottom - rect.top
        val usableHeightSansKeyboard = rootView.height
        val heightDifference = usableHeightSansKeyboard - usableHeightNow
        return heightDifference > (usableHeightSansKeyboard / 4)
    }

    private fun isViewFocus(): View? {
        val focusView = window.currentFocus
        for (view in focusBottomMap.keys) {
            if (focusView === view) {
                return view
            }
        }
        return null
    }

    interface OnSoftInputListener {
        /**
         * 软键盘由关闭变为打开时调用
         */
        fun onOpen()

        /**
         * 软键盘由打开变为关闭时调用
         */
        fun onClose()
    }

}