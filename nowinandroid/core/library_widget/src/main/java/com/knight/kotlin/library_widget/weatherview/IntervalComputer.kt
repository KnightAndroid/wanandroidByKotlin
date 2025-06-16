package com.knight.kotlin.library_widget.weatherview


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/26 9:40
 * @descript:
 */
class IntervalComputer {
    private var mCurrentTime: Long = 0
    private var mLastTime: Long = 0
    var interval = 0.0
        private set

    init {
        reset()
    }

    fun reset() {
        mCurrentTime = -1
        mLastTime = -1
        interval = 0.0
    }

    fun invalidate() {
        mCurrentTime = System.currentTimeMillis()
        interval = (if (mLastTime == -1L) 0 else mCurrentTime - mLastTime).toDouble()
        mLastTime = mCurrentTime
    }
}