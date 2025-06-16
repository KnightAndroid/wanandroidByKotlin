package com.knight.kotlin.library_widget.circleimagebar


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/24 9:49
 * @descript:
 */
interface  OnCircularSeekBarChangeListener {

    /**
     * Notification that the progress level has changed.
     * Clients can use the fromUser parameter to distinguish user-initiated changes
     * from those that occurred programmatically.
     *
     * @param circularBar : The CircularBar whose progress has changed
     * @param progress    : The current progress level. This will be in the range min.
     * @param fromUser    : True if the progress change was initiated by the user.
     */
    fun onProgressChanged(circularBar: CircularMusicProgressBar?, progress: Int, fromUser: Boolean)

    /**
     * User click on circular bar
     *
     * @param circularBar : The CircularBar in which click happen
     */
    fun onClick(circularBar: CircularMusicProgressBar?)

    /**
     * User long click on circular bar
     *
     * @param circularBar : The CircularBar in which long gesture happen
     */
    fun onLongPress(circularBar: CircularMusicProgressBar?)
}