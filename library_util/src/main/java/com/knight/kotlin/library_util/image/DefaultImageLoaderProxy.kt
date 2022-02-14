package com.knight.kotlin.library_util.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
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
import com.bumptech.glide.request.target.Target

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

    override fun loadUriPhoto(context: Context, uri: Uri, imageView: ImageView) {
        Glide.with(context).load(uri).transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    override fun loadStringPhoto(context: Context, uri: String, imageView: ImageView) {
        Glide.with(context).load(uri).transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    override fun loadCirCleStringPhoto(context: Context, uri: String, imageView: ImageView) {
        Glide.with(context).load(uri).apply(
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

    override fun loadGifAsBitmap(context: Context, gifUri: Uri, imageView: ImageView) {
        Glide.with(context).asBitmap().load(gifUri).into(imageView)
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

    override fun getCacheBitmap(context: Context, uri: Uri, width: Int, height: Int): Bitmap? {
        return Glide.with(context).asBitmap().load(uri).submit(width, height).get()
    }
}