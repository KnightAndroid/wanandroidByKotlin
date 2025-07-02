package com.knight.kotlin.library_scan.decode

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.fragment.app.Fragment
import com.knight.kotlin.library_scan.entity.ScanCodeEntity
import com.knight.kotlin.library_scan.utils.QrCodeUtil

/**
 * Author:Knight
 * Time:2022/2/14 14:27
 * Description:ScanCodeConfig
 */
class ScanCodeConfig(builder: Builder) {


    companion object {
        val QUESTCODE = 0x001
        val CODE_KEY = "code"
        val MODEL_KEY = "model"
    }


    private var mActivity: Activity? = null
    private var mFragment: Fragment? = null
    private var model: ScanCodeEntity? = null

    init {
        mActivity = builder.mActivity
        mFragment = builder.mFragment
        model = ScanCodeEntity(builder.mActivity, builder.mFragment)
        model?.setPlayAudio(builder.playAudio)
        model?.setStyle(builder.style)
    }



    fun create(mActivity: Activity?): ScanCodeEntity? {
        return ScanCodeEntity(mActivity)
    }

    fun create(mFragment: Fragment?): ScanCodeEntity? {
        return ScanCodeEntity(mFragment)
    }


      class Builder {
        internal var style = 0
        internal var playAudio = false
        internal var mActivity: Activity? = null
        internal var mFragment: Fragment? = null
        fun setStyle(style: Int): Builder {
            this.style = style
            return this
        }

        fun setPlayAudio(playAudio: Boolean): Builder {
            this.playAudio = playAudio
            return this
        }

        fun setActivity(mActivity: Activity?): Builder {
            this.mActivity = mActivity
            return this
        }

        fun setFragment(fragment: Fragment?): Builder {
            mFragment = fragment
            return this
        }

        fun build(): ScanCodeConfig {
            return ScanCodeConfig(this)
        }
    }

    fun create(mActivity: Activity?, mFragment: Fragment?): ScanCodeEntity? {
        return ScanCodeEntity(mActivity, mFragment)
    }

    fun start(mClass: Class<*>?) {
        if (mFragment != null) {
            val intent = Intent(mActivity, mClass)
            intent.putExtra(MODEL_KEY, model)
            mFragment?.startActivityForResult(intent, QUESTCODE)
            mActivity?.overridePendingTransition(com.core.library_base.R.anim.base_bottom_in, com.core.library_base.R.anim.base_bottom_slient)
        } else {
            val intent = Intent(mActivity, mClass)
            intent.putExtra(MODEL_KEY, model)
            mActivity?.startActivityForResult(intent, QUESTCODE)
        }
    }

    fun createQRCode(text: String?): Bitmap? {
        return QrCodeUtil.createQRCode(text)
    }

    fun createQRCode(text: String?, size: Int): Bitmap? {
        return QrCodeUtil.createQRCode(text, size)
    }

    fun createQRcodeWithLogo(text: String?, logo: Bitmap?): Bitmap? {
        return QrCodeUtil.createQRcodeWithLogo(text, logo)
    }

    fun createQRcodeWithLogo(
        text: String?,
        size: Int,
        logo: Bitmap?,
        logoWith: Int,
        logoHigh: Int,
        logoRaduisX: Float,
        logoRaduisY: Float
    ): Bitmap? {
        return QrCodeUtil.createQRcodeWithLogo(
            text,
            size,
            logo,
            logoWith,
            logoHigh,
            logoRaduisX,
            logoRaduisY
        )
    }

    fun createQRcodeWithStrokLogo(
        text: String?,
        size: Int,
        logo: Bitmap?,
        logoWith: Int,
        logoHigh: Int,
        logoRaduisX: Float,
        logoRaduisY: Float,
        storkWith: Int,
        storkColor: Int
    ): Bitmap? {
        return QrCodeUtil.createQRcodeWithStrokLogo(
            text,
            size,
            logo,
            logoWith,
            logoHigh,
            logoRaduisX,
            logoRaduisY,
            storkWith,
            storkColor
        )
    }

    fun createBarcode(
        content: String,
        widthPix: Int,
        heightPix: Int,
        isShowContent: Boolean
    ): Bitmap? {
        return QrCodeUtil.createBarcode(content, widthPix, heightPix, isShowContent)
    }

    fun scanningImage(mActivity: Activity, uri: Uri): String? {
        return QrCodeUtil.scanningImage(mActivity, uri)
    }

    fun scanningImageByBitmap(bitmap: Bitmap): String? {
        return QrCodeUtil.scanningImageByBitmap(bitmap)
    }
}