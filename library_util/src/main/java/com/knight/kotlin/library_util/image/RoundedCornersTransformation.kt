package com.knight.kotlin.library_util.image

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Shader
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * Author:Knight
 * Time:2024/3/4 17:50
 * Description:RoundedCornersTransformation
 */
class RoundedCornersTransformation(val radius: Float) : BitmapTransformation() {


    override fun updateDiskCacheKey(messageDigest: MessageDigest) {

    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        var result = pool.get(outWidth,outHeight,Bitmap.Config.ARGB_8888)
        if (result == null) {
            result = Bitmap.createBitmap(outWidth,outHeight,Bitmap.Config.ARGB_8888)
        }
        val canvas = Canvas(result)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.setShader(null)
        canvas.drawRoundRect(RectF(0F,0F, outWidth.toFloat(), outHeight.toFloat()),radius,radius,paint)
        val bitmapPaint = Paint()
        bitmapPaint.isAntiAlias = true
        bitmapPaint.setShader(BitmapShader(toTransform, Shader.TileMode.CLAMP,Shader.TileMode.CLAMP))
        bitmapPaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        canvas.drawBitmap(toTransform,0F,0F,bitmapPaint)
        return result
    }
}