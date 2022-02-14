package com.knight.kotlin.library_scan.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.ChecksumException
import com.google.zxing.DecodeHintType
import com.google.zxing.EncodeHintType
import com.google.zxing.FormatException
import com.google.zxing.MultiFormatWriter
import com.google.zxing.NotFoundException
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.Result
import com.google.zxing.WriterException
import com.google.zxing.common.GlobalHistogramBinarizer
import com.google.zxing.qrcode.QRCodeReader
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.io.IOException
import java.util.EnumSet
import java.util.Hashtable
import java.util.Vector
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Author:Knight
 * Time:2022/2/14 14:09
 * Description:QrCodeUtil
 */
object QrCodeUtil {
    private val DEFAULTE_SIZE = 500

    private val CHINESEPATTERN =
        Pattern.compile("[\u4E00-\u9FA5|\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】]")

    /**
     * 生成条形码
     * @param content 要生成条形码包含的内容
     * @param widthPix 条形码的宽度
     * @param heightPix 条形码的高度
     * @param isShowContent  是否显示条形码包含的内容
     * @return 返回生成条形的位图
     */
    fun createBarcode(
        content: String,
        widthPix: Int,
        heightPix: Int,
        isShowContent: Boolean
    ): Bitmap? {
        if (TextUtils.isEmpty(content)) {
            return null
        }
        if (QrCodeUtil.isContainChinese(content)) {
            return null
        }
        //配置参数
        val hints: MutableMap<EncodeHintType, Any?> = HashMap()
        hints[EncodeHintType.CHARACTER_SET] = "utf-8"
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
        val writer = MultiFormatWriter()
        try {
            // 图像数据转换，使用了矩阵转换 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
            val bitMatrix =
                writer.encode(content, BarcodeFormat.CODE_128, widthPix, heightPix, hints)
            val pixels = IntArray(widthPix * heightPix)
            for (y in 0 until heightPix) {
                for (x in 0 until widthPix) {
                    if (bitMatrix[x, y]) {
                        pixels[y * widthPix + x] = -0x1000000
                    } else {
                        pixels[y * widthPix + x] = -0x1
                    }
                }
            }
            var bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix)
            if (isShowContent) {
                bitmap = showContent(bitmap, content)
            }
            return bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 显示条形的内容
     * @param bCBitmap 已生成的条形码的位图
     * @param content  条形码包含的内容
     * @return 返回生成的新位图
     */
    private fun showContent(bCBitmap: Bitmap?, content: String): Bitmap? {
        if (TextUtils.isEmpty(content) || null == bCBitmap) {
            return null
        }
        val paint = Paint()
        paint.color = Color.BLACK
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.textSize = 20f
        //测量字符串的宽度
        val textWidth = paint.measureText(content).toInt()
        val fm = paint.fontMetrics
        //绘制字符串矩形区域的高度
        val textHeight = (fm.bottom - fm.top).toInt()
        // x 轴的缩放比率
        val scaleRateX = (bCBitmap.width / textWidth).toFloat()
        paint.textScaleX = scaleRateX
        //绘制文本的基线
        val baseLine = bCBitmap.height + textHeight
        //创建一个图层，然后在这个图层上绘制bCBitmap、content
        val bitmap = Bitmap.createBitmap(
            bCBitmap.width,
            (bCBitmap.height + 1.5 * textHeight).toInt(), Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bCBitmap, 0f, 0f, null)
        canvas.drawText(
            content,
            ((bCBitmap.width shr 1) - ((textWidth * scaleRateX).toInt() shr 1)).toFloat(),
            baseLine.toFloat(),
            paint
        )
        canvas.save()
        canvas.restore()
        return bitmap
    }


    /**
     * 字符串是否包含中文
     *
     * @param str 待校验字符串
     * @return true 包含中文字符 false 不包含中文字符
     */
    private fun isContainChinese(str: String?): Boolean {
        if ("" == str || str == null) {
            throw RuntimeException("sms context is empty!")
        }
        val m: Matcher = CHINESEPATTERN.matcher(str)
        return m.find()
    }


    /**
     * 生成二维码，默认大小为500
     *
     * @param text 需要生成二维码的文字、网址等
     * @return bitmap
     */
    fun createQRCode(text: String?): Bitmap? {
        return createQRCode(text, DEFAULTE_SIZE)
    }

    /**
     * 生成二维码
     *
     * @param text 需要生成二维码的文字、网址等
     * @param size 需要生成二维码的大小（）
     * @return bitmap
     */
    fun createQRCode(text: String?, size: Int): Bitmap? {
        return try {
            val hints = Hashtable<EncodeHintType, Any?>()
            hints[EncodeHintType.CHARACTER_SET] = "utf-8"
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
            hints[EncodeHintType.MARGIN] = 1
            val bitMatrix = QRCodeWriter().encode(
                text,
                BarcodeFormat.QR_CODE, size, size, hints
            )
            val pixels = IntArray(size * size)
            for (y in 0 until size) {
                for (x in 0 until size) {
                    if (bitMatrix[x, y]) {
                        pixels[y * size + x] = -0x1000000
                    } else {
                        pixels[y * size + x] = -0x1
                    }
                }
            }
            val bitmap = Bitmap.createBitmap(
                size, size,
                Bitmap.Config.ARGB_8888
            )
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size)
            bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }

    /** 生成带描边logo 二维码
     * @param text  文字
     * @param size   二维码大小 1 ：1
     * @param logo   logo
     * @param logoWith logo宽
     * @param logoHigh  logo高
     * @param logoRaduisX  logo x圆角
     * @param logoRaduisY  logo y圆角
     * @param storkWith    描边宽度
     * @param storkColor   描边颜色
     * @return
     */
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
        return try {
            val hints = Hashtable<EncodeHintType, Any?>()
            hints[EncodeHintType.CHARACTER_SET] = "utf-8"
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
            hints[EncodeHintType.MARGIN] = 1
            val bitMatrix = QRCodeWriter().encode(
                text,
                BarcodeFormat.QR_CODE, size, size, hints
            )
            val pixels = IntArray(size * size)
            for (y in 0 until size) {
                for (x in 0 until size) {
                    if (bitMatrix[x, y]) {
                        pixels[y * size + x] = -0x1000000
                    } else {
                        pixels[y * size + x] = -0x1
                    }
                }
            }
            val bitmap = Bitmap.createBitmap(
                size, size,
                Bitmap.Config.ARGB_8888
            )
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size)
            if (logo == null) {
                bitmap
            } else {
                QrCodeUtil.addStorkLogo(
                    bitmap,
                    logo,
                    logoWith,
                    logoHigh,
                    logoRaduisX,
                    logoRaduisY,
                    Math.min(storkWith, Math.min(logoWith, logoHigh)),
                    storkColor
                )
            }
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 在二维码中间添加Logo图案(带描边)
     * @param src         原图
     * @param logo        logo
     * @param logoWith     添加logo的宽度
     * @param logoHigh     添加logo的高度
     * @param logoRaduisX  logo圆角
     * @param logoRaduisY  logo圆角
     * @param storkWith    描边宽度
     * @param storkColor   描边颜色
     * @return
     */
    @SuppressLint("NewApi")
    fun addStorkLogo(
        src: Bitmap?,
        logo: Bitmap?,
        logoWith: Int,
        logoHigh: Int,
        logoRaduisX: Float,
        logoRaduisY: Float,
        storkWith: Int,
        storkColor: Int
    ): Bitmap? {
        if (src == null) {
            return null
        }
        if (logo == null) {
            return src
        }
        //获取图片的宽高
        val srcWidth = src.width
        val srcHeight = src.height
        val logoW = logo.width
        val logoH = logo.height
        if (srcWidth == 0 || srcHeight == 0) {
            return null
        }
        if (logoW == 0 || logoH == 0) {
            return src
        }
        val scaleW = logoWith / logoW.toFloat()
        val scaleH = logoHigh / logoH.toFloat()
        val matrix = Matrix()
        matrix.postScale(scaleW, scaleH)
        matrix.postTranslate(
            ((srcWidth shr 1) - (logoWith shr 1)).toFloat(),
            ((srcHeight shr 1) - (logoHigh shr 1)).toFloat()
        )
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val bitmapShader = BitmapShader(logo, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        bitmapShader.setLocalMatrix(matrix)
        var bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888)
        try {
            val canvas = Canvas(bitmap!!)
            canvas.drawBitmap(src, 0f, 0f, null)
            paint.color = if (storkColor == 0) Color.WHITE else storkColor
            canvas.drawRoundRect(
                RectF(
                    ((srcWidth shr 1) - (logoWith shr 1) - storkWith).toFloat(),
                    ((srcHeight shr 1) - (logoHigh shr 1) - storkWith).toFloat(),
                    ((srcWidth shr 1) + (logoWith shr 1) + storkWith).toFloat(),
                    ((srcHeight shr 1) + (logoHigh shr 1) + storkWith).toFloat()
                ), logoRaduisX, logoRaduisY, paint
            )
            paint.shader = bitmapShader
            canvas.drawRoundRect(
                RectF(
                    ((srcWidth shr 1) - (logoWith shr 1)).toFloat(),
                    ((srcHeight shr 1) - (logoHigh shr 1)).toFloat(),
                    ((srcWidth shr 1) + (logoWith shr 1)).toFloat(),
                    ((srcHeight shr 1) + (logoHigh shr 1)).toFloat()
                ), logoRaduisX, logoRaduisY, paint
            )
        } catch (e: Exception) {
            bitmap = null
            e.stackTrace
        }
        return bitmap
    }

    /**生成带logo 二维码
     * @param text  文字
     * @param logo   logo
     * @return
     */
    fun createQRcodeWithLogo(text: String?, logo: Bitmap?): Bitmap? {
        return createQRcodeWithLogo(
            text,
            DEFAULTE_SIZE,
            logo,
            DEFAULTE_SIZE / 5,
            DEFAULTE_SIZE / 5,
            0f,
            0f
        )
    }

    /** 生成带logo 二维码
     * @param text  文字
     * @param size   二维码大小 1 ：1
     * @param logo   logo
     * @param logoWith logo宽
     * @param logoHigh  logo高
     * @param logoRaduisX  logo x圆角
     * @param logoRaduisY  logo y圆角
     * @return
     */
    fun createQRcodeWithLogo(
        text: String?,
        size: Int,
        logo: Bitmap?,
        logoWith: Int,
        logoHigh: Int,
        logoRaduisX: Float,
        logoRaduisY: Float
    ): Bitmap? {
        return try {
            val hints = Hashtable<EncodeHintType, Any?>()
            hints[EncodeHintType.CHARACTER_SET] = "utf-8"
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
            hints[EncodeHintType.MARGIN] = 1
            val bitMatrix = QRCodeWriter().encode(
                text,
                BarcodeFormat.QR_CODE, size, size, hints
            )
            val pixels = IntArray(size * size)
            for (y in 0 until size) {
                for (x in 0 until size) {
                    if (bitMatrix[x, y]) {
                        pixels[y * size + x] = -0x1000000
                    } else {
                        pixels[y * size + x] = -0x1
                    }
                }
            }
            val bitmap = Bitmap.createBitmap(
                size, size,
                Bitmap.Config.ARGB_8888
            )
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size)
            if (logo == null) {
                bitmap
            } else {
                QrCodeUtil.addLogo(bitmap, logo, logoWith, logoHigh, logoRaduisX, logoRaduisY)
            }
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 在二维码中间添加Logo图案
     * @param src         原图
     * @param logo        logo
     * @param logoWith     添加logo的宽度
     * @param logoHigh     添加logo的高度
     * @param logoRaduisX  logo圆角
     * @param logoRaduisY  logo圆角
     * @return
     */
    @SuppressLint("NewApi")
    fun addLogo(
        src: Bitmap?,
        logo: Bitmap?,
        logoWith: Int,
        logoHigh: Int,
        logoRaduisX: Float,
        logoRaduisY: Float
    ): Bitmap? {
        if (src == null) {
            return null
        }
        if (logo == null) {
            return src
        }
        //获取图片的宽高
        val srcWidth = src.width
        val srcHeight = src.height
        val logoW = logo.width
        val logoH = logo.height
        if (srcWidth == 0 || srcHeight == 0) {
            return null
        }
        if (logoW == 0 || logoH == 0) {
            return src
        }
        val scaleW = logoWith / logoW.toFloat()
        val scaleH = logoHigh / logoH.toFloat()
        val matrix = Matrix()
        matrix.postScale(scaleW, scaleH)
        matrix.postTranslate(
            ((srcWidth shr 1) - (logoWith shr 1)).toFloat(),
            ((srcHeight shr 1) - (logoHigh shr 1)).toFloat()
        )
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val bitmapShader = BitmapShader(logo, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        bitmapShader.setLocalMatrix(matrix)
        paint.shader = bitmapShader
        var bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888)
        try {
            val canvas = Canvas(bitmap!!)
            canvas.drawBitmap(src, 0f, 0f, null)
            canvas.drawRoundRect(
                RectF(
                    ((srcWidth shr 1) - (logoWith shr 1)).toFloat(),
                    ((srcHeight shr 1) - (logoHigh shr 1)).toFloat(),
                    ((srcWidth shr 1) + (logoWith shr 1)).toFloat(),
                    ((srcHeight shr 1) + (logoHigh shr 1)).toFloat()
                ), logoRaduisX, logoRaduisY, paint
            )
        } catch (e: java.lang.Exception) {
            bitmap = null
            e.stackTrace
        }
        return bitmap
    }

    /**
     * 解码uri二维码图片
     * @return
     */
    fun scanningImage(mActivity: Activity, uri: Uri): String? {
        val hints = Hashtable<DecodeHintType, Any?>(2)
        val decodeFormats = Vector<BarcodeFormat>()
        if (decodeFormats.isEmpty()) {
            decodeFormats.addAll(EnumSet.of(BarcodeFormat.QR_CODE))
        }
        hints[DecodeHintType.POSSIBLE_FORMATS] = decodeFormats
        hints[DecodeHintType.CHARACTER_SET] = "UTF-8"
        var result: Result? = null
        val srcBitmap: Bitmap? = getBitmapByUri(mActivity, uri)
        val width = srcBitmap?.width ?: 0
        val height = srcBitmap?.height ?:0
        val pixels = IntArray(width * height)
        srcBitmap?.getPixels(pixels, 0, width, 0, 0, width, height)
        val source = RGBLuminanceSource(width, height, pixels)
        val binaryBitmap = BinaryBitmap(GlobalHistogramBinarizer(source))
        val reader = QRCodeReader()
        try {
            result = reader.decode(binaryBitmap, hints)
        } catch (e: NotFoundException) {
            e.printStackTrace()
        } catch (e: ChecksumException) {
            e.printStackTrace()
        } catch (e: FormatException) {
            e.printStackTrace()
        }
        return result?.text
    }

    /**
     * 解码bitmap二维码图片
     * @return
     */
    fun scanningImageByBitmap(srcBitmap: Bitmap): String? {
        val hints = Hashtable<DecodeHintType, Any?>(2)
        val decodeFormats = Vector<BarcodeFormat>()
        if (decodeFormats.isEmpty()) {
            decodeFormats.addAll(EnumSet.of(BarcodeFormat.QR_CODE))
        }
        hints[DecodeHintType.POSSIBLE_FORMATS] = decodeFormats
        hints[DecodeHintType.CHARACTER_SET] = "UTF-8"
        var result: Result? = null
        val width = srcBitmap.width
        val height = srcBitmap.height
        val pixels = IntArray(width * height)
        srcBitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        val source = RGBLuminanceSource(width, height, pixels)
        val binaryBitmap = BinaryBitmap(GlobalHistogramBinarizer(source))
        val reader = QRCodeReader()
        try {
            result = reader.decode(binaryBitmap, hints)
        } catch (e: NotFoundException) {
            e.printStackTrace()
        } catch (e: ChecksumException) {
            e.printStackTrace()
        } catch (e: FormatException) {
            e.printStackTrace()
        }
        return result?.text
    }


    private fun getBitmapByUri(mActivity: Activity, uri: Uri): Bitmap? {
        try {
            return MediaStore.Images.Media.getBitmap(mActivity.contentResolver, uri)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }


}