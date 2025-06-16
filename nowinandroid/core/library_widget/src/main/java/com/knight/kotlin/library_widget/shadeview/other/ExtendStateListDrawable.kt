package com.knight.kotlin.library_widget.shadeview.other

import android.R
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable


/**
 * @author created by luguian
 * @organize
 * @Date 2024/9/5 9:48
 * @descript:基于 StateListDrawable 类进行扩展
 */
class ExtendStateListDrawable : StateListDrawable() {
    private val mDrawableMap = HashMap<IntArray, Drawable>()

    override fun addState(stateSet: IntArray, drawable: Drawable?) {
        super.addState(stateSet, drawable)
        if (drawable == null) {
            return
        }
        mDrawableMap[stateSet] = drawable
    }

    fun setDefaultDrawable(drawable: Drawable?) {
        addState(STATE_DEFAULT, drawable)
    }

    fun getDefaultDrawable(): Drawable? {
        return mDrawableMap[STATE_DEFAULT]
    }

    fun setPressedDrawable(drawable: Drawable?) {
        addState(STATE_PRESSED, drawable)
    }

    fun getPressedDrawable(): Drawable? {
        return mDrawableMap[STATE_PRESSED]
    }

    fun setCheckDrawable(drawable: Drawable?) {
        addState(STATE_CHECKED, drawable)
    }

    fun getCheckDrawable(): Drawable? {
        return mDrawableMap[STATE_CHECKED]
    }

    fun setDisabledDrawable(drawable: Drawable?) {
        addState(STATE_DISABLED, drawable)
    }

    fun getDisabledDrawable(): Drawable? {
        return mDrawableMap[STATE_DISABLED]
    }

    fun setFocusedDrawable(drawable: Drawable?) {
        addState(STATE_FOCUSED, drawable)
    }

    fun getFocusedDrawable(): Drawable? {
        return mDrawableMap[STATE_FOCUSED]
    }

    fun setSelectDrawable(drawable: Drawable?) {
        addState(STATE_SELECTED, drawable)
    }

    fun getSelectDrawable(): Drawable? {
        return mDrawableMap[STATE_SELECTED]
    }

    companion object {
        private val STATE_DEFAULT = intArrayOf()
        private val STATE_PRESSED = intArrayOf(R.attr.state_pressed)
        private val STATE_CHECKED = intArrayOf(R.attr.state_checked)
        private val STATE_DISABLED = intArrayOf(-R.attr.state_enabled)
        private val STATE_FOCUSED = intArrayOf(R.attr.state_focused)
        private val STATE_SELECTED = intArrayOf(R.attr.state_selected)
    }
}