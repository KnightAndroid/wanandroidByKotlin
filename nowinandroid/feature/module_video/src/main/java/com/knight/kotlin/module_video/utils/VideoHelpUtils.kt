package com.knight.kotlin.module_video.utils

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.os.Build
import android.os.CancellationSignal
import android.provider.MediaStore
import android.util.Size
import com.knight.library.cryption.AesUtils
import java.io.File
import java.util.Hashtable


/**
 * Author:Knight
 * Time:2024/3/4 17:30
 * Description:VideoHelpUtils
 */
object VideoHelpUtils {

    /**
     * 移除前缀并且解密(暂时不用)
     * @param cipherUrl 加密的字符串
     * @return
     */
    fun removePrefixToDecry(cipherUrl:String) :String {
        val  prefixToRemove = "ftp://" // 要移除的前缀
        if (cipherUrl.startsWith(prefixToRemove)) {
            val startIndex = prefixToRemove.length
            val result = cipherUrl.substring (startIndex)
            return AesUtils.decrypt(result) ?: ""
        } else {
            return cipherUrl
        }

    }

    /**
     *
     *
     * 通过本地路径获取视频第一帧
     *
     * @param videoPath
     * @param width
     * @param height
     * @param kind
     * @return
     */
    fun getVideoFirstFrameByLocaL(videoPath : String, width : Int, height : Int, kind : Int):Bitmap? {
        var bitmap: Bitmap? = null
        // 获取视频的缩略图
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                bitmap = ThumbnailUtils.createVideoThumbnail(
                    File(videoPath),
                    Size(512, 384),
                    CancellationSignal()
                )
        } else {
            bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        }
        //调用ThumbnailUtils类的静态方法createVideoThumbnail获取视频的截图；
        if (bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(
                bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT
            ) //调用ThumbnailUtils类的静态方法extractThumbnail将原图片（即上方截取的图片）转化为指定大小；
        }
        return bitmap
    }


    /**
     * 获取视频第一帧
     *
     *
     * @param filePath
     * @param kind
     * @return
     */
    fun createVideoThumbnail(filePath: String, kind: Int): Bitmap? {
        var bitmap: Bitmap? = null
        val retriever = MediaMetadataRetriever()
        try {
            if (filePath.startsWith("http://")
                || filePath.startsWith("https://")
                || filePath.startsWith("widevine://")
            ) {
                retriever.setDataSource(filePath, Hashtable())
            } else {
                retriever.setDataSource(filePath)
            }
            bitmap = retriever.getFrameAtTime(
                0,
                MediaMetadataRetriever.OPTION_CLOSEST_SYNC
            ) //retriever.getFrameAtTime(-1);
        } catch (ex: IllegalArgumentException) {
            // Assume this is a corrupt video file
            ex.printStackTrace()
        } catch (ex: RuntimeException) {
            // Assume this is a corrupt video file.
            ex.printStackTrace()
        } finally {
            try {
                retriever.release()
            } catch (ex: RuntimeException) {
                // Ignore failures while cleaning up.
                ex.printStackTrace()
            }
        }
        if (bitmap == null) {
            return null
        }
        if (kind == MediaStore.Images.Thumbnails.MINI_KIND) { //压缩图片 开始处
            // Scale down the bitmap if it's too large.
            val width = bitmap.width
            val height = bitmap.height
            val max = Math.max(width, height)
            if (max > 512) {
                val scale = 512f / max
                val w = Math.round(scale * width)
                val h = Math.round(scale * height)
                bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true)
            } //压缩图片 结束处
        } else if (kind == MediaStore.Images.Thumbnails.MICRO_KIND) {
            bitmap = ThumbnailUtils.extractThumbnail(
                bitmap,
                96,
                96,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT
            )
        }
        return bitmap
    }

}