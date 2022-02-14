package com.knight.kotlin.library_util.image

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView

/**
 * Author:Knight
 * Time:2022/2/11 10:00
 * Description:ImageLoader
 */
class ImageLoader {
    companion object{
        private val mImageLoaderProxy: ImageLoaderProxy = DefaultImageLoaderProxy()

        fun getInstance(): ImageLoaderProxy {
            return mImageLoaderProxy
        }

        /**
         *
         * 加载项目内的资源文件
         * @param context 上下文
         * @param resourceId 资源路径
         * @param imageView
         */
        fun loadLocalPhoto(context: Context, resourceId: Int?, imageView: ImageView) {
            mImageLoaderProxy.loadLocalPhoto(context, resourceId, imageView)
        }

        /**
         * 通过uri加载到ImageView
         *
         * @param context
         * @param uri
         * @param imageView
         */
        fun loadUriPhoto(context: Context, uri: Uri, imageView: ImageView) {
            mImageLoaderProxy.loadUriPhoto(context, uri, imageView)
        }

        /**
         *
         * 通过String方式加载图片到ImageView
         * @param context
         * @param uri
         * @param imageView
         */
        fun loadStringPhoto(context: Context, uri: String, imageView: ImageView) {
            mImageLoaderProxy.loadStringPhoto(context, uri, imageView)
        }

        /**
         * 加载String方式的圆形图片
         * @param context
         * @param uri
         * @param imageView
         */
        fun loadCirCleStringPhoto(context: Context, uri: String, imageView: ImageView) {
            mImageLoaderProxy.loadCirCleStringPhoto(context, uri, imageView)
        }

        /**
         *
         * 加载Int方式圆形图片
         * @param context
         * @param resourceId
         * @param imageView
         */
        fun loadCircleIntLocalPhoto(context: Context, resourceId: Int?, imageView: ImageView) {
            mImageLoaderProxy.loadCircleIntLocalPhoto(context, resourceId, imageView)
        }

        /**
         * 通过gif转为bitmap加载到imageView
         *
         * @param context
         * @param gifUri
         * @param imageView
         */
        fun loadGifAsBitmap(context: Context, gifUri: Uri, imageView: ImageView) {
            mImageLoaderProxy.loadGifAsBitmap(context, gifUri, imageView)
        }

        /**
         * 通过Uri方式加载gif
         *
         * @param context
         * @param resourceId
         * @param imageView
         */
        fun loadGif(context: Context, resourceId: Int?, imageView: ImageView) {
            mImageLoaderProxy.loadGif(context, resourceId, imageView)
        }

        /**
         * 获取图片加载框架中的缓存bitmap
         *
         * @param context
         * @param uri
         * @param width
         * @param height
         * @return
         * @throws Exception
         */
        @Throws(Exception::class)
        fun getCacheBitmap(context: Context, uri: Uri, width: Int, height: Int): Bitmap? {
            return mImageLoaderProxy.getCacheBitmap(context, uri, width, height)
        }
    }
}