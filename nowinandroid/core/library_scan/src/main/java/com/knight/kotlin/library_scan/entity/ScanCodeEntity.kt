package com.knight.kotlin.library_scan.entity

import android.app.Activity
import android.os.Parcel
import android.os.Parcelable
import androidx.fragment.app.Fragment

/**
 * Author:Knight
 * Time:2022/2/14 11:30
 * Description:ScanCodeEntity
 */
class ScanCodeEntity : Parcelable {
    var mActivity: Activity? = null
    var mFragment: Fragment? = null
    private var style = 0
    private var scanMode = 0
    private var isPlayAudio = false
    private var audioId = 0
    private var showFrame = false
    private var scanRect: ScanRectEntity? = null
    private var scanSize = 0
    private var offsetX = 0
    private var offsetY = 0
    private var usePx = false
    fun getStyle(): Int {
        return style
    }

    fun setStyle(style: Int) {
        this.style = style
    }

    fun getScanMode(): Int {
        return scanMode
    }

    fun setScanMode(scanMode: Int) {
        this.scanMode = scanMode
    }

    fun isPlayAudio(): Boolean {
        return isPlayAudio
    }

    fun setPlayAudio(playAudio: Boolean) {
        isPlayAudio = playAudio
    }

    fun getAudioId(): Int {
        return audioId
    }

    fun setAudioId(audioId: Int) {
        this.audioId = audioId
    }

    fun isShowFrame(): Boolean {
        return showFrame
    }

    fun setShowFrame(showFrame: Boolean) {
        this.showFrame = showFrame
    }

    fun getScanRect(): ScanRectEntity? {
        return scanRect
    }

    fun setScanRect(scanRect: ScanRectEntity?) {
        this.scanRect = scanRect
    }

    fun getScanSize(): Int {
        return scanSize
    }

    fun setScanSize(scanSize: Int) {
        this.scanSize = scanSize
    }

    fun getOffsetX(): Int {
        return offsetX
    }

    fun setOffsetX(offsetX: Int) {
        this.offsetX = offsetX
    }

    fun getOffsetY(): Int {
        return offsetY
    }

    fun setOffsetY(offsetY: Int) {
        this.offsetY = offsetY
    }

    fun isUsePx(): Boolean {
        return usePx
    }

    fun setUsePx(usePx: Boolean) {
        this.usePx = usePx
    }

    fun getScanDuration(): Long {
        return scanDuration
    }

    fun setScanDuration(scanDuration: Long) {
        this.scanDuration = scanDuration
    }

    fun getFrameColor(): Int {
        return frameColor
    }

    fun setFrameColor(frameColor: Int) {
        this.frameColor = frameColor
    }

    fun isShowShadow(): Boolean {
        return showShadow
    }

    fun setShowShadow(showShadow: Boolean) {
        this.showShadow = showShadow
    }

    fun getShaowColor(): Int {
        return shaowColor
    }

    fun setShaowColor(shaowColor: Int) {
        this.shaowColor = shaowColor
    }

    fun getScanBitmapId(): Int {
        return scanBitmapId
    }

    fun setScanBitmapId(scanBitmapId: Int) {
        this.scanBitmapId = scanBitmapId
    }

    fun getFrameWith(): Int {
        return frameWith
    }

    fun setFrameWith(frameWith: Int) {
        this.frameWith = frameWith
    }

    fun getFrameLenth(): Int {
        return frameLenth
    }

    fun setFrameLenth(frameLenth: Int) {
        this.frameLenth = frameLenth
    }

    fun getFrameRaduis(): Int {
        return frameRaduis
    }

    fun setFrameRaduis(frameRaduis: Int) {
        this.frameRaduis = frameRaduis
    }

    private var scanDuration: Long = 0
    private var frameColor = 0
    private var showShadow = false
    private var shaowColor = 0
    private var scanBitmapId = 0
    private var frameWith = 0
    private var frameLenth = 0
    private var frameRaduis = 0

    constructor(mActivity: Activity?) {
        this.mActivity = mActivity
    }

    constructor(mFragment: Fragment?) {
        this.mFragment = mFragment
    }

    constructor(mActivity: Activity?, mFragment: Fragment?) {
        this.mActivity = mActivity
        this.mFragment = mFragment
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(style)
        dest.writeInt(scanMode)
        dest.writeByte(if (isPlayAudio) 1.toByte() else 0.toByte())
        dest.writeInt(audioId)
        dest.writeByte(if (showFrame) 1.toByte() else 0.toByte())
        dest.writeSerializable(scanRect)
        dest.writeInt(scanSize)
        dest.writeInt(offsetX)
        dest.writeInt(offsetY)
        dest.writeByte(if (usePx) 1.toByte() else 0.toByte())
        dest.writeLong(scanDuration)
        dest.writeInt(frameColor)
        dest.writeByte(if (showShadow) 1.toByte() else 0.toByte())
        dest.writeInt(shaowColor)
        dest.writeInt(scanBitmapId)
        dest.writeInt(frameWith)
        dest.writeInt(frameLenth)
        dest.writeInt(frameRaduis)
    }

    protected constructor(`in`: Parcel) {
        style = `in`.readInt()
        scanMode = `in`.readInt()
        isPlayAudio = `in`.readByte().toInt() != 0
        audioId = `in`.readInt()
        showFrame = `in`.readByte().toInt() != 0
        scanRect = `in`.readSerializable() as ScanRectEntity?
        scanSize = `in`.readInt()
        offsetX = `in`.readInt()
        offsetY = `in`.readInt()
        usePx = `in`.readByte().toInt() != 0
        scanDuration = `in`.readLong()
        frameColor = `in`.readInt()
        showShadow = `in`.readByte().toInt() != 0
        shaowColor = `in`.readInt()
        scanBitmapId = `in`.readInt()
        frameWith = `in`.readInt()
        frameLenth = `in`.readInt()
        frameRaduis = `in`.readInt()
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ScanCodeEntity> =
            object : Parcelable.Creator<ScanCodeEntity> {
                override fun createFromParcel(source: Parcel): ScanCodeEntity {
                    return ScanCodeEntity(source)
                }

                override fun newArray(size: Int): Array<ScanCodeEntity?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
