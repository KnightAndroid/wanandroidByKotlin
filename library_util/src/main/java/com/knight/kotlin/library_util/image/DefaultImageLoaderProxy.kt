package com.knight.kotlin.library_util.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.knight.kotlin.library_base.util.dp2px

/**
 * Author:Knight
 * Time:2022/2/10 18:00
 * Description:DefaultImageLoaderProxy
 */
class DefaultImageLoaderProxy :ImageLoaderProxy {
    override fun loadLocalPhoto(context: Context, resourceId: Int?, imageView: ImageView) {
        Glide.with(context)
            .load(resourceId)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    override fun loadLocalPhoto(mFragment: Fragment, resourceId: Int?, imageView: ImageView) {
        Glide.with(mFragment)
            .load(resourceId)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    override fun loadLocalPhoto(
        mActivity: FragmentActivity,
        resourceId: Int?,
        imageView: ImageView
    ) {
        Glide.with(mActivity)
            .load(resourceId)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    override fun loadUriPhoto(context: Context, uri: Uri, imageView: ImageView) {
        Glide.with(context).load(uri).transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    override fun loadUriPhoto(mActivity: FragmentActivity, uri: Uri, imageView: ImageView) {
        Glide.with(mActivity).load(uri).transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    override fun loadUriPhoto(mFragment: Fragment, uri: Uri, imageView: ImageView) {
        Glide.with(mFragment).load(uri).transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    override fun loadUriPhoto(
        context: Context,
        uri: Uri,
        imageView: ImageView,
        callback: ((Boolean) -> Unit)?
    ) {
        Glide.with(context).load(uri)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    callback?.run {
                        false
                    }
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    callback?.run {
                        true
                    }
                    return true
                }

            })
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)







    }

    override fun loadStringPhoto(context: Context, uri: String, imageView: ImageView) {
        Glide.with(context).load(uri).transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    override fun loadStringPhoto(mFragment: Fragment, uri: String, imageView: ImageView) {
        Glide.with(mFragment).load(uri).transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    override fun loadStringPhoto(mActivity: FragmentActivity, uri: String, imageView: ImageView) {
        Glide.with(mActivity).load(uri).transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    override fun loadStringPhoto(
        context: Context,
        uri: String,
        imageView: ImageView,
        callback: ((Boolean) -> Unit)?
    ) {
        Glide.with(context).load(uri)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    callback?.run {
                        this(false)
                    }
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    callback?.run {
                        this(true)
                    }
                    imageView.setImageDrawable(resource)
                    return true
                }

            })
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)

    }

    override fun loadCirCleStringPhoto(context: Context, uri: String, imageView: ImageView) {
        Glide.with(context).load(uri).apply(
            RequestOptions.bitmapTransform(CircleCrop())
                .override(imageView.width, imageView.height)
        ).into(imageView)
    }

    override fun loadCirCleStringPhoto(
        mActivity: FragmentActivity,
        uri: String,
        imageView: ImageView
    ) {
        Glide.with(mActivity).load(uri).apply(
            RequestOptions.bitmapTransform(CircleCrop())
                .override(imageView.width, imageView.height)
        ).into(imageView)
    }

    override fun loadCirCleStringPhoto(mFragment: Fragment, uri: String, imageView: ImageView) {
        Glide.with(mFragment).load(uri).apply(
            RequestOptions.bitmapTransform(CircleCrop())
                .override(imageView.width, imageView.height)
        ).into(imageView)
    }

    override fun loadCircleIntLocalPhoto(context: Context, resourceId: Int?, imageView: ImageView) {
        Glide.with(context).load(resourceId).apply(
            RequestOptions.bitmapTransform(CircleCrop())
                .override(imageView.width, imageView.height)
        ).into(imageView)
    }

    override fun loadCircleIntLocalPhoto(
        mFragment: Fragment,
        resourceId: Int?,
        imageView: ImageView
    ) {
        Glide.with(mFragment).load(resourceId).apply(
            RequestOptions.bitmapTransform(CircleCrop())
                .override(imageView.width, imageView.height)
        ).into(imageView)
    }

    override fun loadCircleIntLocalPhoto(
        mActivity: FragmentActivity,
        resourceId: Int?,
        imageView: ImageView
    ) {
        Glide.with(mActivity).load(resourceId).apply(
            RequestOptions.bitmapTransform(CircleCrop())
                .override(imageView.width, imageView.height)
        ).into(imageView)
    }

    override fun loadRoundedCornerPhoto(
        context: Context,
        uri: String,
        imageView: ImageView,
        corner: Int
    ) {
        Glide.with(context).load(uri).apply(
            RequestOptions.bitmapTransform(RoundedCornersTransformation(corner.dp2px().toFloat()))

        ).override(imageView.width, imageView.height).into(imageView)
    }

    override fun loadImageFillWidthAndHeight(imageView: ImageView, imageUrl: String) {
        Glide.with(imageView.context).asBitmap()
            .load(imageUrl)

            .into(object : CustomViewTarget<ImageView, Bitmap>(imageView) {
                override fun onResourceCleared(placeholder: Drawable?) {
                    // 资源被清除时的回调，可留空
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    // 加载失败时，设置错误占位图
                    imageView.setImageDrawable(errorDrawable)
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val imageViewWidth = imageView.width
                    val imageViewHeight = imageView.height

                    if (imageViewWidth > 0 && imageViewHeight > 0) {
                        val imageViewWidth = imageView.width
                        val imageViewHeight = imageView.height

                        if (imageViewWidth > 0 && imageViewHeight > 0) {
                            val bitmapWidth = resource.width
                            val bitmapHeight = resource.height

                            // 1️⃣ 先按宽度缩放，保证宽度完全匹配 ImageView
                            val scaleX = imageViewWidth.toFloat() / bitmapWidth.toFloat()
                            var scaledHeight = bitmapHeight * scaleX  // 计算缩放后的高度

                            // 2️⃣ 计算高度缩放比例，是否需要额外放大或裁剪
                            val scaleY = imageViewHeight.toFloat() / scaledHeight
                            val finalScale = if (scaledHeight < imageViewHeight) scaleX * scaleY else scaleX

                            // 3️⃣ 计算新的缩放后高度
                            scaledHeight = bitmapHeight * finalScale

                            // 4️⃣ 创建 Matrix 进行缩放变换
                            val matrix = Matrix()
                            matrix.setScale(finalScale, finalScale)

                            // 5️⃣ 计算偏移，让图片垂直居中（裁剪顶部和底部）
                            val dx = 0f // 水平方向对齐 ImageView 左边（不裁剪宽度）
                            val dy = (imageViewHeight - scaledHeight) / 2 // 计算垂直方向的偏移量

                            matrix.postTranslate(dx, dy)

                            // 6️⃣ 设置 ImageView 变换
                            imageView.scaleType = ImageView.ScaleType.MATRIX
                            imageView.imageMatrix = matrix
                            imageView.setImageBitmap(resource)
                        }
                    }
                }
            })
//            .into(object : CustomTarget<Drawable>() {
//                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
//                    val originalWidth = resource.intrinsicWidth
//                    val originalHeight = resource.intrinsicHeight
//                    val imageViewWidth = imageView.width
//                    val imageViewHeight = imageView.height
//                    var scaledHeight = 0
//                    if (imageViewWidth > 0 && imageViewHeight > 0) {
//                        // 第一步：按宽度进行加载缩放（不裁剪）
//                        val scaleX = imageViewWidth.toFloat() / originalWidth.toFloat()
//                        scaledHeight = (originalHeight * scaleX).toInt()
//                        imageView.layoutParams.width = imageViewWidth // 确保宽度一致
//                        imageView.layoutParams.height = imageViewHeight // 确保高度一致
//                        // 第二步：按高度加载缩放裁剪，铺满整个图片
//                        if (scaledHeight >= imageViewHeight) {
//                            // 缩放后的高度大于等于 ImageView 的高度，使用 CENTER_CROP
//                          //  imageView.layoutParams.width = imageViewWidth
//                          //  imageView.layoutParams.height = imageViewHeight
//                            imageView.setImageDrawable(resource)
//                            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
//                        } else {
//                            // 缩放后的高度小于 ImageView 的高度，使用 MATRIX 进行放大
//                         //   imageView.layoutParams.width = imageViewWidth
//                          //  imageView.layoutParams.height = imageViewHeight
//                            imageView.setImageDrawable(resource)
//                            val scaleY = imageViewHeight.toFloat() / scaledHeight.toFloat()
//
//                            val matrix = Matrix()
//                            matrix.setScale(1f, scaleY, 0f, 0f) // 水平方向不缩放，垂直方向放大 scaleY 倍
//
//                            imageView.imageMatrix = matrix
//                            imageView.scaleType = ImageView.ScaleType.MATRIX
//                        }
//                    } else {
//                        // imageView 的尺寸尚未计算出来，使用 ViewTreeObserver 监听布局变化
//                        imageView.viewTreeObserver.addOnGlobalLayoutListener(object : android.view.ViewTreeObserver.OnGlobalLayoutListener {
//                            override fun onGlobalLayout() {
//                                imageView.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                                loadImageFillWidthAndHeight(imageView, imageUrl)
//                            }
//                        })
//                    }
//                }
//
//                override fun onLoadCleared(placeholder: Drawable?) {
//                    // 清除资源
//                }
//            })
    }

    override fun loadImageWithAdaptiveSize(imageView: ImageView, targetWidth: Int, targetHeight: Int, imageUrl: String) {
        Glide.with(imageView.context)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val bitmapWidth = resource.width
                    val bitmapHeight = resource.height

                    // 计算 ImageView 高度，保持图片比例
                    val targetHeight = (targetWidth.toFloat() / bitmapWidth * bitmapHeight).toInt()

                    // 设置 ImageView 宽高
                    val params = imageView.layoutParams
                    params.width = targetWidth
                    params.height = targetHeight
                    imageView.layoutParams = params

                    // 设置图片
                    imageView.setImageBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // 清除资源
                }
            })
    }

    override fun loadVideoFirstFrame(context: Context, uri: String, imageView: ImageView) {
        Glide.with(context).asBitmap().load(uri).apply(RequestOptions.frameOf(0))
                .override(imageView.width, imageView.height).into(imageView)
    }

    override fun loadVideoFirstFrameCorner(
        context: Context,
        uri: String,
        imageView: ImageView,
        corner: Int
    ) {
        Glide.with(context).load(uri).apply(
            RequestOptions.bitmapTransform(RoundedCornersTransformation(corner.dp2px().toFloat())).apply(RequestOptions.frameOf(0))
                .override(imageView.width, imageView.height)
        ).into(imageView)
    }

    override fun loadGifAsBitmap(context: Context, gifUri: Uri, imageView: ImageView) {
        Glide.with(context).asBitmap().load(gifUri).into(imageView)
    }

    override fun loadGifAsBitmap(mFragment: Fragment, gifUri: Uri, imageView: ImageView) {
        Glide.with(mFragment).asBitmap().load(gifUri).into(imageView)
    }

    override fun loadGifAsBitmap(mActivity: FragmentActivity, gifUri: Uri, imageView: ImageView) {
        Glide.with(mActivity).asBitmap().load(gifUri).into(imageView)
    }

    override fun loadGif(context: Context, resourceId: Int?, imageView: ImageView) {
        Glide.with(context).asGif().load(resourceId)
            .listener(object : RequestListener<GifDrawable> {
                override fun onLoadFailed(e: GlideException?, model: Any, target: Target<GifDrawable>, isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(resource: GifDrawable, model: Any, target: Target<GifDrawable>, dataSource: DataSource, isFirstResource: Boolean
                ): Boolean {
                    //设置循环次数
                    resource.setLoopCount(2)
                    resource.registerAnimationCallback(object :
                        Animatable2Compat.AnimationCallback() {
                        override fun onAnimationStart(drawable: Drawable) {
                            super.onAnimationStart(drawable)
                            //播放开始
                            imageView.visibility = View.VISIBLE
                        }

                        override fun onAnimationEnd(drawable: Drawable) {
                            super.onAnimationEnd(drawable)
                            //播放完成
                            imageView.visibility = View.GONE
                        }
                    })
                    return false
                }
            }).transition(DrawableTransitionOptions.withCrossFade()).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(imageView)
    }

    override fun loadGif(mActivity: FragmentActivity, resourceId: Int?, imageView: ImageView) {
        Glide.with(mActivity).asGif().load(resourceId)
            .listener(object : RequestListener<GifDrawable> {
                override fun onLoadFailed(e: GlideException?, model: Any, target: Target<GifDrawable>, isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(resource: GifDrawable, model: Any, target: Target<GifDrawable>, dataSource: DataSource, isFirstResource: Boolean
                ): Boolean {
                    //设置循环次数
                    resource.setLoopCount(2)
                    resource.registerAnimationCallback(object :
                        Animatable2Compat.AnimationCallback() {
                        override fun onAnimationStart(drawable: Drawable) {
                            super.onAnimationStart(drawable)
                            //播放开始
                            imageView.visibility = View.VISIBLE
                        }

                        override fun onAnimationEnd(drawable: Drawable) {
                            super.onAnimationEnd(drawable)
                            //播放完成
                            imageView.visibility = View.GONE
                        }
                    })
                    return false
                }
            }).transition(DrawableTransitionOptions.withCrossFade()).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(imageView)
    }

    override fun loadGif(mFragment: Fragment, resourceId: Int?, imageView: ImageView) {
        Glide.with(mFragment).asGif().load(resourceId)
            .listener(object : RequestListener<GifDrawable> {
                override fun onLoadFailed(e: GlideException?, model: Any, target: Target<GifDrawable>, isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(resource: GifDrawable, model: Any, target: Target<GifDrawable>, dataSource: DataSource, isFirstResource: Boolean
                ): Boolean {
                    //设置循环次数
                    resource.setLoopCount(2)
                    resource.registerAnimationCallback(object :
                        Animatable2Compat.AnimationCallback() {
                        override fun onAnimationStart(drawable: Drawable) {
                            super.onAnimationStart(drawable)
                            //播放开始
                            imageView.visibility = View.VISIBLE
                        }

                        override fun onAnimationEnd(drawable: Drawable) {
                            super.onAnimationEnd(drawable)
                            //播放完成
                            imageView.visibility = View.GONE
                        }
                    })
                    return false
                }
            }).transition(DrawableTransitionOptions.withCrossFade()).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(imageView)
    }

    override fun getCacheBitmap(context: Context, uri: Uri, width: Int, height: Int): Bitmap? {
        return Glide.with(context).asBitmap().load(uri).submit(width, height).get()
    }

    override fun getCacheBitmap(mFragment: Fragment, uri: Uri, width: Int, height: Int): Bitmap? {
        return Glide.with(mFragment).asBitmap().load(uri).submit(width, height).get()
    }

    override fun getCacheBitmap(
        mActivity: FragmentActivity,
        uri: Uri,
        width: Int,
        height: Int
    ): Bitmap? {
        return Glide.with(mActivity).asBitmap().load(uri).submit(width, height).get()
    }
}