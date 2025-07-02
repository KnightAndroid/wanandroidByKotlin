package com.knight.kotlin.module_video.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.knight.kotlin.library_base.adapter.BaseAdapter
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.module_video.databinding.VideoPlayItemBinding
import com.knight.kotlin.module_video.entity.VideoPlayEntity
import com.knight.kotlin.module_video.utils.VideoCache
import com.knight.kotlin.module_video.utils.precache.PreloadManager
import com.knight.kotlin.module_video.view.LikeView

/**
 * Author:Knight
 * Time:2024/3/5 14:57
 * Description:VideoPlayAdapter
 */
class VideoPlayAdapter(val context: Context, val recyclerView :RecyclerView) : BaseAdapter<VideoPlayAdapter.VideoViewHolder, VideoPlayEntity>(VideoDiff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoPlayAdapter.VideoViewHolder {
        return VideoViewHolder(VideoPlayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        //开始预加载
        PreloadManager.getInstance(context).addPreloadTask(mList[position].videoUrl, holder.absoluteAdapterPosition)
        mList[position]?.let {
            holder.binding.controller.setVideoData(it)
            ImageLoader.loadVideoFirstFrame(context, it.videoUrl, holder.binding.ivCover)
            holder.binding.videoLikeview.setOnLikeListener(object : LikeView.OnLikeListener {
                override fun onLikeListener() {
                    if (!it.isLike) {  //未点赞，会有点赞效果，否则无
                        holder.binding.controller.like()
                    }
                }
            })
            //利用预加item，提前加载缓存资源
            mList[position].mediaSource = buildMediaSource(mList[position].videoUrl)
        }

        holder.mPosition =  holder.absoluteAdapterPosition


    }

//    /**
//     * 构建一个共用缓存文件
//     */
//    val cache: SimpleCache by lazy {
//        //构建缓存文件
//        val cacheFile = context.cacheDir.resolve("tiktok_cache_file$this.hashCode()")
//        //构建simpleCache缓存实例
//        SimpleCache(cacheFile, LeastRecentlyUsedCacheEvictor(VideoPlayer.MAX_CACHE_BYTE), StandaloneDatabaseProvider(context))
//    }

    /**
     * 构建当前url视频的缓存
     */
    private fun buildMediaSource(url: String): ProgressiveMediaSource {
        //开启缓存文件
        val mediaItem = MediaItem.fromUri(url)
        //构建 DataSourceFactory
        val dataSourceFactory = CacheDataSource.Factory().setCache(VideoCache.getInstance(context)).setUpstreamDataSourceFactory(
            DefaultDataSource.Factory(context))
        //构建 MediaSource
        return ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
    }

    /**
     * 通过position获取当前item.rootview
     */
    fun getRootViewAt(position: Int): View? {
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
        return if (viewHolder != null && viewHolder is VideoViewHolder) {
            viewHolder.itemView
        } else {
            null

        }
    }

    override fun onViewDetachedFromWindow(holder: VideoPlayAdapter.VideoViewHolder) {
        super.onViewDetachedFromWindow(holder)
        //取消预加载
        PreloadManager.getInstance(context).removePreloadTask(mList.get(holder.mPosition).videoUrl)


    }

    inner class VideoViewHolder(val binding: VideoPlayItemBinding) : RecyclerView.ViewHolder(binding.root) {
          var mPosition:Int = 0
    }

}

    class VideoDiff: DiffUtil.ItemCallback<VideoPlayEntity>() {
    override fun areItemsTheSame(oldItem: VideoPlayEntity, newItem: VideoPlayEntity): Boolean {
        return (oldItem.userId == newItem.userId)
    }

    override fun areContentsTheSame(oldItem: VideoPlayEntity, newItem:VideoPlayEntity): Boolean {
        return (oldItem.videoUrl == newItem.videoUrl && oldItem.userId == newItem.userId)
    }



//
//        @Override
//        public void onViewDetachedFromWindow(@NonNull TiktokViewHolder holder) {
//            super.onViewDetachedFromWindow(holder);
//            NewsFeedAdatperBean item = mData.get(holder.mPosition);
//            ImageInfo videoUrlInfo = item.myNewsResources.get(1);
//
//            //取消预加载
//            VideoCachePreloadManager.getInstance(mContext).removePreloadTask(videoUrlInfo.bigImageUrl);
//        }


}

