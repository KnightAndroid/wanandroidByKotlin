package com.knight.kotlin.library_util.image

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

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
     * 加载项目内的资源文件
     * @param mFragment 上下文
     * @param resourceId 资源路径
     * @param imageView
     */
    fun loadLocalPhoto(mFragment: Fragment, @RawRes @DrawableRes resourceId: Int?, imageView: ImageView)

    /**
     *
     * 加载项目内的资源文件
     * @param mActivity 上下文
     * @param resourceId 资源路径
     * @param imageView
     */
    fun loadLocalPhoto(mActivity: FragmentActivity, @RawRes @DrawableRes resourceId: Int?, imageView: ImageView)
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
     * 通过Uri方式加载图片到ImageView
     * @param context
     * @param uri
     * @param imageView
     */
    fun loadUriPhoto(mActivity: FragmentActivity, uri: Uri, imageView: ImageView)

    /**
     *
     * 通过Uri方式加载图片到ImageView
     * @param context
     * @param uri
     * @param imageView
     */
    fun loadUriPhoto(mFragment: Fragment, uri: Uri, imageView: ImageView)


    /**
     * 通过Uri加载图片到ImageView
     *
     * @param context
     * @param uri
     * @param imageView
     * @param callback
     */
    fun loadUriPhoto(context:Context,uri:Uri,imageView : ImageView,callback:((Boolean) -> Unit) ?= null)
    /**
     *
     * 通过String方式加载图片到ImageView
     * @param context
     * @param uri
     * @param imageView
     */
    fun loadStringPhoto(context: Context, uri: String, imageView: ImageView)

    /**
     *
     * 通过String方式加载图片到ImageView
     * @param mFragment
     * @param uri
     * @param imageView
     */
    fun loadStringPhoto(mFragment: Fragment, uri: String, imageView: ImageView)

    /**
     *
     * 通过String方式加载图片到ImageView
     * @param mActivity
     * @param uri
     * @param imageView
     */
    fun loadStringPhoto(mActivity: FragmentActivity, uri: String, imageView: ImageView)


    /**
     *
     * 通过String方式加载图片到ImageView
     * @param context
     * @param uri
     * @param imageView
     */
    fun loadStringPhoto(context: Context, uri: String, imageView: ImageView,callback:((Boolean) -> Unit) ?= null)
    /**
     * 加载String方式的圆形图片
     * @param context
     * @param uri
     * @param imageView
     */
    fun loadCirCleStringPhoto(context: Context, uri: String, imageView: ImageView)

    /**
     * 加载String方式的圆形图片
     * @param mActivity
     * @param uri
     * @param imageView
     */
    fun loadCirCleStringPhoto(mActivity: FragmentActivity, uri: String, imageView: ImageView)

    /**
     * 加载String方式的圆形图片
     * @param mFragment
     * @param uri
     * @param imageView
     */
    fun loadCirCleStringPhoto(mFragment: Fragment, uri: String, imageView: ImageView)

    /**
     *
     * 加载Int方式圆形图片
     * @param context
     * @param resourceId
     * @param imageView
     */
    fun loadCircleIntLocalPhoto(context: Context, @RawRes @DrawableRes resourceId: Int?, imageView: ImageView)

    /**
     *
     * 加载Int方式圆形图片
     * @param mFragment
     * @param resourceId
     * @param imageView
     */
    fun loadCircleIntLocalPhoto(mFragment: Fragment, @RawRes @DrawableRes resourceId: Int?, imageView: ImageView)

    /**
     *
     * 加载Int方式圆形图片
     * @param mActivity
     * @param resourceId
     * @param imageView
     */
    fun loadCircleIntLocalPhoto(mActivity: FragmentActivity, @RawRes @DrawableRes resourceId: Int?, imageView: ImageView)


    /**
     *
     * 加载圆角图片
     *
     * @param context
     * @param uri
     * @param imageView
     * @param corner
     */
    fun loadRoundedCornerPhoto(context: Context,uri: String, imageView: ImageView,corner:Int)

    /**
     *
     * 自定义加载图片 根据宽度 不变 放大 高度
     */
    fun loadImageFillWidthAndHeight(imageView: ImageView, imageUrl: String)


    fun loadImageWithAdaptiveSize(imageView: ImageView, targetWidth:Int,targetHeight:Int ,imageUrl: String)

    /**
     *
     *
     * 加载视频第一帧
     * @param context
     * @param uri
     * @param imageView
     */
    fun loadVideoFirstFrame(context: Context,uri: String, imageView: ImageView)
    /**
     * 加载视频第一帧
     *
     *
     * @param context
     * @param uri
     * @param imageView
     * @param corner
     */
    fun loadVideoFirstFrameCorner(context: Context,uri: String, imageView: ImageView,corner:Int)
    /**
     * 通过uri方式加载gif图片到ImageView
     * @param context
     * @param gifUri
     * @param imageView
     */
    fun loadGifAsBitmap(context: Context, gifUri: Uri, imageView: ImageView)

    /**
     * 通过uri方式加载gif图片到ImageView
     * @param mFragment
     * @param gifUri
     * @param imageView
     */
    fun loadGifAsBitmap(mFragment: Fragment, gifUri: Uri, imageView: ImageView)
    /**
     * 通过uri方式加载gif图片到ImageView
     * @param mActivity
     * @param gifUri
     * @param imageView
     */
    fun loadGifAsBitmap(mActivity: FragmentActivity, gifUri: Uri, imageView: ImageView)
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
     * 通过Uri方式加载gif动图到ImageView
     * @param mActivity
     * @param resourceId
     * @param imageView
     */
    fun loadGif(mActivity: FragmentActivity, resourceId: Int?, imageView: ImageView)
    /**
     *
     * 通过Uri方式加载gif动图到ImageView
     * @param mFragment
     * @param resourceId
     * @param imageView
     */
    fun loadGif(mFragment: Fragment, resourceId: Int?, imageView: ImageView)

    /**
     *
     * 获取图片加载框架中的缓存bitmap
     * @return
     */
    @Throws(Exception::class)
    fun getCacheBitmap(context: Context, uri: Uri, width: Int, height: Int): Bitmap?
    /**
     *
     * 获取图片加载框架中的缓存bitmap
     * @return
     */
    @Throws(Exception::class)
    fun getCacheBitmap(mFragment: Fragment, uri: Uri, width: Int, height: Int): Bitmap?

    /**
     *
     * 获取图片加载框架中的缓存bitmap
     * @return
     */
    @Throws(Exception::class)
    fun getCacheBitmap(mActivity: FragmentActivity, uri: Uri, width: Int, height: Int): Bitmap?
}