package com.knight.kotlin.library_util.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

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
         *
         * 加载项目内的资源文件
         * @param mFragment Fragment
         * @param resourceId 资源路径
         * @param imageView
         */
        fun loadLocalPhoto(mFragment: Fragment, resourceId: Int?, imageView: ImageView) {
            mImageLoaderProxy.loadLocalPhoto(mFragment, resourceId, imageView)
        }

        /**
         *
         * 加载项目内的资源文件
         * @param mActivity FragmentActivity
         * @param resourceId 资源路径
         * @param imageView
         */
        fun loadLocalPhoto(mActivity: FragmentActivity, resourceId: Int?, imageView: ImageView) {
            mImageLoaderProxy.loadLocalPhoto(mActivity, resourceId, imageView)
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
         * 通过uri加载到ImageView
         *
         * @param Fragment
         * @param uri
         * @param imageView
         */
        fun loadUriPhoto(mFragment: Fragment, uri: Uri, imageView: ImageView) {
            mImageLoaderProxy.loadUriPhoto(mFragment, uri, imageView)
        }

        /**
         * 通过uri加载到ImageView
         *
         * @param FragmentActivity
         * @param uri
         * @param imageView
         */
        fun loadUriPhoto(mActivity: FragmentActivity, uri: Uri, imageView: ImageView) {
            mImageLoaderProxy.loadUriPhoto(mActivity, uri, imageView)
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
         * 加载 Uri 对应的图片为 Drawable
         * @param context 上下文
         * @param uri 图片 Uri
         * @param callback 加载完成后回调 Drawable
         */
        fun loadUriPhotoAsBitmap(context: Context, uri: String,callback: (Drawable?) -> Unit) {
            mImageLoaderProxy.loadUriPhotoAsBitmap(context,uri,callback)
        }

        /**
         *
         * 通过String方式加载图片到ImageView
         * @param context
         * @param uri
         * @param imageView
         */
        fun loadStringPhoto(context: Context, uri: String, imageView: ImageView,callback: ((Boolean) -> Unit)?) {
            mImageLoaderProxy.loadStringPhoto(context, uri, imageView,callback)
        }
        /**
         *
         * 通过String方式加载图片到ImageView
         * @param mFragment
         * @param uri
         * @param imageView
         */
        fun loadStringPhoto(mFragment: Fragment, uri: String, imageView: ImageView) {
            mImageLoaderProxy.loadStringPhoto(mFragment, uri, imageView)
        }


        /**
         *
         * 通过String方式加载图片到ImageView
         * @param mActivity
         * @param uri
         * @param imageView
         */
        fun loadStringPhoto(mActivity: FragmentActivity, uri: String, imageView: ImageView) {
            mImageLoaderProxy.loadStringPhoto(mActivity, uri, imageView)
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
         * 加载String方式的圆形图片
         * @param mFragment
         * @param uri
         * @param imageView
         */
        fun loadCirCleStringPhoto(mFragment: Fragment, uri: String, imageView: ImageView) {
            mImageLoaderProxy.loadCirCleStringPhoto(mFragment, uri, imageView)
        }

        /**
         * 加载String方式的圆形图片
         * @param mActivity
         * @param uri
         * @param imageView
         */
        fun loadCirCleStringPhoto(mActivity: FragmentActivity, uri: String, imageView: ImageView) {
            mImageLoaderProxy.loadCirCleStringPhoto(mActivity, uri, imageView)
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
         *
         * 加载Int方式圆形图片
         * @param mFragment
         * @param resourceId
         * @param imageView
         */
        fun loadCircleIntLocalPhoto(mFragment: Fragment, resourceId: Int?, imageView: ImageView) {
            mImageLoaderProxy.loadCircleIntLocalPhoto(mFragment, resourceId, imageView)
        }

        /**
         *
         * 加载Int方式圆形图片
         * @param mActivity
         * @param resourceId
         * @param imageView
         */
        fun loadCircleIntLocalPhoto(mActivity: FragmentActivity, resourceId: Int?, imageView: ImageView) {
            mImageLoaderProxy.loadCircleIntLocalPhoto(mActivity, resourceId, imageView)
        }

        /**
         * 加载圆角图片
         * @param context
         * @param uri
         * @param imageView
         * @param corner
         */
        fun loadRoundedCornerPhoto(context: Context,uri: String, imageView: ImageView,corner:Int) {
            mImageLoaderProxy.loadRoundedCornerPhoto(context, uri, imageView, corner)
        }

        /**
         *  加载视频第一帧
         *
         *
         * @param context
         * @param uri
         * @param imageView
         */
       fun loadVideoFirstFrame(context: Context,uri: String, imageView: ImageView) {
           mImageLoaderProxy.loadVideoFirstFrame(context, uri, imageView)
       }

        /**
         * 通过圆角加载图片第一帧
         *
         * @param context
         * @param uri
         * @param imageView
         * @param corner
         */
        fun loadVideoFirstFrameCorner(context: Context,uri: String, imageView: ImageView,corner:Int) {
            mImageLoaderProxy.loadVideoFirstFrameCorner(context, uri, imageView, corner)
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
         * 通过gif转为bitmap加载到imageView
         *
         * @param mActivity
         * @param gifUri
         * @param imageView
         */
        fun loadGifAsBitmap(mActivity: FragmentActivity, gifUri: Uri, imageView: ImageView) {
            mImageLoaderProxy.loadGifAsBitmap(mActivity, gifUri, imageView)
        }

        /**
         * 通过gif转为bitmap加载到imageView
         *
         * @param mFragment
         * @param gifUri
         * @param imageView
         */
        fun loadGifAsBitmap(mFragment: Fragment, gifUri: Uri, imageView: ImageView) {
            mImageLoaderProxy.loadGifAsBitmap(mFragment, gifUri, imageView)
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
         * 通过Uri方式加载gif
         *
         * @param mFragment
         * @param resourceId
         * @param imageView
         */
        fun loadGif(mFragment: Fragment, resourceId: Int?, imageView: ImageView) {
            mImageLoaderProxy.loadGif(mFragment, resourceId, imageView)
        }

        /**
         * 通过Uri方式加载gif
         *
         * @param context
         * @param resourceId
         * @param imageView
         */
        fun loadGif(mActivity: FragmentActivity, resourceId: Int?, imageView: ImageView) {
            mImageLoaderProxy.loadGif(mActivity, resourceId, imageView)
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

        /**
         * 获取图片加载框架中的缓存bitmap
         *
         * @param mActivity
         * @param uri
         * @param width
         * @param height
         * @return
         * @throws Exception
         */
        @Throws(Exception::class)
        fun getCacheBitmap(mActivity: FragmentActivity, uri: Uri, width: Int, height: Int): Bitmap? {
            return mImageLoaderProxy.getCacheBitmap(mActivity, uri, width, height)
        }

        /**
         * 获取图片加载框架中的缓存bitmap
         *
         * @param mFragment
         * @param uri
         * @param width
         * @param height
         * @return
         * @throws Exception
         */
        @Throws(Exception::class)
        fun getCacheBitmap(mFragment: Fragment, uri: Uri, width: Int, height: Int): Bitmap? {
            return mImageLoaderProxy.getCacheBitmap(mFragment, uri, width, height)
        }


        /**
         * 要实现图片的宽度与 ImageView 的宽度一致，并且高度放大到 ImageView 的高
         *
         */
        @Throws(Exception::class)
        fun loadImageWithAdaptiveSize(imageView: ImageView, targetWidth:Int,targetHeight:Int ,imageUrl: String,callback: ((width: Int, height: Int) -> Unit)?=null) {
            return mImageLoaderProxy.loadImageWithAdaptiveSize(imageView, targetWidth, targetHeight, imageUrl,callback)
        }
    }
}