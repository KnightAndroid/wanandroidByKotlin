package com.knight.kotlin.library_util.image

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes

/**
 * Author:Knight
 * Time:2022/2/10 17:47
 * Description:ImageLoaderProxy
 */
interface ImageLoaderProxy {

    /**
     *
     * 加载项目内的资源文件
     * @param context 上下文
     * @param resourceId 资源路径
     * @param imageView
     */
    fun loadLocalPhoto(context: Context, @RawRes @DrawableRes resourceId: Int?, imageView: ImageView)

    /**
     *
     * 通过Uri方式加载图片到ImageView
     * @param context
     * @param uri
     * @param imageView
     */
    fun loadUriPhoto(context: Context, uri: Uri, imageView: ImageView)


    /**
     *
     * 通过String方式加载图片到ImageView
     * @param context
     * @param uri
     * @param imageView
     */
    fun loadStringPhoto(context: Context, uri: String, imageView: ImageView)


    /**
     * 加载String方式的圆形图片
     * @param context
     * @param uri
     * @param imageView
     */
    fun loadCirCleStringPhoto(context: Context, uri: String, imageView: ImageView)

    /**
     *
     * 加载Int方式圆形图片
     * @param context
     * @param resourceId
     * @param imageView
     */
    fun loadCircleIntLocalPhoto(context: Context, @RawRes @DrawableRes resourceId: Int?, imageView: ImageView)

    /**
     * 通过uri方式加载gif图片到ImageView
     * @param context
     * @param gifUri
     * @param imageView
     */
    fun loadGifAsBitmap(context: Context, gifUri: Uri, imageView: ImageView)

    /**
     *
     * 通过Uri方式加载gif动图到ImageView
     * @param context
     * @param resourceId
     * @param imageView
     */
    fun loadGif(context: Context, resourceId: Int?, imageView: ImageView)


    /**
     *
     * 获取图片加载框架中的缓存bitmap
     * @return
     */
    @Throws(Exception::class)
    fun getCacheBitmap(context: Context, uri: Uri, width: Int, height: Int): Bitmap?
}