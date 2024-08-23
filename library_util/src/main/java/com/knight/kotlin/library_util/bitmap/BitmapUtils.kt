package com.knight.kotlin.library_util.bitmap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.media.MediaScannerConnection
import android.os.Build
import android.view.View
import com.knight.kotlin.library_base.BaseApp
import com.knight.kotlin.library_util.FileUtils
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/23 8:58
 * @descript:Bitmap 相关工具类
 */
object BitmapUtils {



    /**
     *
     * 将view转成bitmap
     */
    fun getViewBitmap(view: View): Bitmap {
        val bitmap: Bitmap
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            // 对于Android API 28以下版本，使用 isDrawingCacheEnabled
            view.isDrawingCacheEnabled = true
            view.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH;
            bitmap = Bitmap.createBitmap(view.drawingCache)
            view.destroyDrawingCache()
            view.isDrawingCacheEnabled = false

        } else {
            // 创建一个和给定View相同大小的Bitmap
            bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            // 将View绘制在Bitmap上
            view.draw(canvas)
            return bitmap
        }

        return bitmap
    }


    /**
     * 拼接Bitmap
     *
     * @param bitmapList bitmapList
     */
    fun puzzleBitmap(bitmapList: List<Bitmap>): Bitmap {
        // 计算画布宽高
        var width = 0
        var height = 0
        for (bitmap in bitmapList) {
            width = max(width.toDouble(), bitmap.width.toDouble()).toInt()
            height += bitmap.height
        }

        // 创建画布
        val resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resultBitmap)

        // 拼接图片
        var top = 0
        for (bitmap in bitmapList) {
            val bitmapWidth = bitmap.width
            val bitmapHeight = bitmap.height

            val left = (width - bitmapWidth) / 2.0f
            canvas.drawBitmap(bitmap, left, top.toFloat(), null)

            top += bitmapHeight
        }

        return resultBitmap
    }

    /**
     * 保存Bitmap到外部私有文件
     *
     * @param context    context
     * @param bitmap     bitmap
     * @param callback   回调
     */
    fun saveBitmapToFile(context: Context, bitmap: Bitmap, filePrefix:String,callback: SaveBitmapCallback) {
        if (bitmap == null) {
            callback.onFail(NullPointerException("Bitmap不能为null"))
            return
        }

        // 创建保存路径
        val dirFile: File = FileUtils.getCachePhotoDir(context)
        val mkdirs = dirFile.mkdirs()
        // 创建保存文件
        val outFile = File(dirFile, FileUtils.getDateName(filePrefix) + ".jpg")
        saveBitmapToFile(bitmap, outFile, callback)
    }

    /**
     * 保存Bitmap到文件
     *
     * @param bitmap   bitmap
     * @param outFile  outFile
     * @param callback 回调
     */
    fun saveBitmapToFile(bitmap: Bitmap, outFile: File, callback: SaveBitmapCallback) {
        if (bitmap == null) {
            callback.onFail(NullPointerException("Bitmap不能为null"))
            return
        }

        // 保存文件
        try {
            FileOutputStream(outFile).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.flush()
                callback.onSuccess(outFile!!)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            callback.onFail(e)
        }
    }


    /**
     * 拍照后通知相册刷新
     * @param context
     * @param file
     */
    fun scanAlbum(file: File) {
        MediaScannerConnection.scanFile(
            BaseApp.context, arrayOf(file.absolutePath), null
        ) { path, uri -> }
    }


    /**
     * 图片按比例大小压缩方法
     * @param srcPath （根据路径获取图片并压缩）
     * @return
     */
    fun getimage(srcPath: String): Bitmap? {
        val newOpts = BitmapFactory.Options()
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true
        var bitmap = BitmapFactory.decodeFile(srcPath, newOpts) // 此时返回bm为空
        newOpts.inJustDecodeBounds = false
        val w = newOpts.outWidth
        val h = newOpts.outHeight
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        val hh = 800f // 这里设置高度为800f
        val ww = 480f // 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        var be = 1 // be=1表示不缩放
        if (w > h && w > ww) { // 如果宽度大的话根据宽度固定大小缩放
            be = (newOpts.outWidth / ww).toInt()
        } else if (w < h && h > hh) { // 如果高度高的话根据宽度固定大小缩放
            be = (newOpts.outHeight / hh).toInt()
        }
        if (be <= 0) be = 1
        newOpts.inSampleSize = be // 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts)
        return compressImage(bitmap) // 压缩好比例大小后再进行质量压缩
    }


    /**
     * 图片按比例大小压缩方法
     * @param image （根据Bitmap图片压缩）
     * @return
     */
    fun compressScale(image: Bitmap): Bitmap? {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        // 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
        if (baos.toByteArray().size / 1024 > 1024) {
            baos.reset() // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 80, baos) // 这里压缩50%，把压缩后的数据存放到baos中
        }
        val newOpts = BitmapFactory.Options()
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true
        newOpts.inJustDecodeBounds = false
        val w = newOpts.outWidth
        val h = newOpts.outHeight
        val hh = 512f
        val ww = 512f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        var be = 1 // be=1表示不缩放
        if (w > h && w > ww) { // 如果宽度大的话根据宽度固定大小缩放
            be = (newOpts.outWidth / ww).toInt()
        } else if (w < h && h > hh) { // 如果高度高的话根据高度固定大小缩放
            be = (newOpts.outHeight / hh).toInt()
        }
        if (be <= 0) be = 1
        newOpts.inSampleSize = be // 设置缩放比例
        // newOpts.inPreferredConfig = Config.RGB_565;//降低图片从ARGB888到RGB565
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        val isBm = ByteArrayInputStream(baos.toByteArray())
        val bitmap = BitmapFactory.decodeStream(isBm, null, newOpts)
        return compressImage(bitmap!!) // 压缩好比例大小后再进行质量压缩
    }


    /**
     * 质量压缩方法
     * @param image
     * @return
     */
    fun compressImage(image: Bitmap): Bitmap? {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos) // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        var options = 90
        while (baos.toByteArray().size / 1024 > 300) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset() // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos) // 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10 // 每次都减少10
        }
        val isBm = ByteArrayInputStream(baos.toByteArray()) // 把压缩后的数据baos存放到ByteArrayInputStream中
        val bitmap = BitmapFactory.decodeStream(isBm, null, null) // 把ByteArrayInputStream数据生成图片
        return bitmap
    }


}