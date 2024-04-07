package com.knight.kotlin.module_video.utils.precache

import android.content.Context
import com.knight.kotlin.module_video.utils.videocache.HttpProxyCacheServer
import java.io.File
import java.util.concurrent.Executors


/**
 * Author:Knight
 * Time:2024/4/7 10:31
 * Description:PreloadManager
 */
class PreloadManager(val context:Context) {

    private val TAG = "PreloadManager"


    /**
     * 单线程池，按照添加顺序依次执行[PreloadTask]
     */
    private val mExecutorService = Executors.newSingleThreadExecutor()

    /**
     * 保存正在预加载的[PreloadTask]
     */
    private val mPreloadTasks: LinkedHashMap<String, PreloadTask> =
        LinkedHashMap<String, PreloadTask>()

    /**
     * 标识是否需要预加载
     */
    private var mIsStartPreload = true

    private var mHttpProxyCacheServer: HttpProxyCacheServer?=null

    init {
        mHttpProxyCacheServer = ProxyVideoCacheManager.getProxy(context)
    }

    companion object {
        /**
         * 预加载的大小，每个视频预加载512KB，这个参数可根据实际情况调整
         */
        val PRELOAD_LENGTH = 1024 * 1024 * 2


        @Volatile
        private var instance: PreloadManager? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: PreloadManager(context).also { instance = it }
            }
    }


    /**
     * 删除缓存文件
     *
     * @param url
     * @return
     */
    fun deleteCacheFile(url: String?): Boolean {
        try {
            val file = mHttpProxyCacheServer!!.getCacheFile(url)
            if (file.exists()) {
                file.delete()
            }
        } catch (e: Exception) {
            return false
        }
        return true
    }

    /**
     * 开始预加载
     *
     * @param rawUrl 原始视频地址
     */
    fun addPreloadTask(rawUrl: String, position: Int) {
        if (isPreloaded(rawUrl)) return
        val task = PreloadTask()
        task.mRawUrl = rawUrl
        task.mPosition = position
        task.mCacheServer = mHttpProxyCacheServer
        mPreloadTasks[rawUrl] = task
        if (mIsStartPreload) {
            //开始预加载
//            Log.i(TAG,"开始预加载: " + position);
            task.executeOn(mExecutorService)
        }
    }

    /**
     * 判断该播放地址是否已经预加载
     */
    private fun isPreloaded(rawUrl: String): Boolean {
        //先判断是否有缓存文件，如果已经存在缓存文件，并且其大小大于1KB，则表示已经预加载完成了
        val cacheFile = mHttpProxyCacheServer!!.getCacheFile(rawUrl)
        if (cacheFile.exists()) {
            return if (cacheFile.length() >= 1024) {
                true
            } else {
                //这种情况一般是缓存出错，把缓存删掉，重新缓存
                cacheFile.delete()
                false
            }
        }
        //再判断是否有临时缓存文件，如果已经存在临时缓存文件，并且临时缓存文件超过了预加载大小，则表示已经预加载完成了
        val tempCacheFile: File = mHttpProxyCacheServer!!.getTempCacheFile(rawUrl)
        return if (tempCacheFile.exists()) {
            tempCacheFile.length() >= PRELOAD_LENGTH
        } else false
    }


    /**
     * 暂停预加载
     * 根据是否反向滑动取消在position之下或之上的PreloadTask
     *
     * @param position        当前滑到的位置
     * @param isReverseScroll 列表是否反向滑动
     */
    fun pausePreload(position: Int, isReverseScroll: Boolean) {
        mIsStartPreload = false
        for (next in mPreloadTasks.entries) {
            val task: PreloadTask = next.value
            if (isReverseScroll) {
                if (task.mPosition >= position) {
                    task.cancel()
                }
            } else {
                if (task.mPosition <= position) {
                    task.cancel()
                }
            }
        }
    }

    /**
     * 恢复预加载
     * 根据是否反向滑动开始在position之下或之上的PreloadTask
     *
     * @param position        当前滑到的位置
     * @param isReverseScroll 列表是否反向滑动
     */
    fun resumePreload(position: Int, isReverseScroll: Boolean) {
        mIsStartPreload = true
        for (next in mPreloadTasks.entries) {
            val task: PreloadTask = next.value
            if (isReverseScroll) {
                if (task.mPosition < position) {
                    task.mRawUrl?.let {
                        isPreloaded(it)
                        task.executeOn(mExecutorService)
                    }

                }
            } else {
                if (task.mPosition > position) {
                    task.mRawUrl?.let {
                        isPreloaded(it)
                        task.executeOn(mExecutorService)
                    }

                }
            }
        }
    }

    /**
     * 继续执行预加载,边下载边播放
     */
    fun continuePreLoad(position: Int, isReverseScroll: Boolean) {
        mIsStartPreload = true
        for (next in mPreloadTasks.entries) {
            val task: PreloadTask = next.value
            if (isReverseScroll) {
                if (task.mPosition < position) {
                    task.mRawUrl?.let {
                        if (!isPreloaded(it)) {
                            task.executeOn(mExecutorService)
                        }
                    }

                }
            } else {
                if (task.mPosition > position) {
                    task.mRawUrl?.let {
                        if (!isPreloaded(it)) {
                            task.executeOn(mExecutorService)
                        }
                    }
                }
            }
        }
    }


    /**
     * 通过原始地址取消预加载
     *
     * @param rawUrl 原始地址
     */
    fun removePreloadTask(rawUrl: String) {
        val task: PreloadTask? = mPreloadTasks[rawUrl]
        if (task != null) {
            task.cancel()
            mPreloadTasks.remove(rawUrl)
        }
    }

    /**
     * 取消所有的预加载
     */
    fun removeAllPreloadTask() {
        val iterator: MutableIterator<Map.Entry<String, PreloadTask>> =
            mPreloadTasks.entries.iterator()
        while (iterator.hasNext()) {
            val (_, task) = iterator.next()
            task.cancel()
            iterator.remove()
        }
    }

    /**
     * 停止所有的预缓存任务
     */
    fun stopAllPreloadTask() {
        val iterator: Iterator<Map.Entry<String, PreloadTask>> = mPreloadTasks.entries.iterator()
        while (iterator.hasNext()) {
            val (_, task) = iterator.next()
            task.cancel()
        }
    }

    /**
     * 获取播放地址
     */
    fun getPlayUrl(rawUrl: String): String? {
        val task: PreloadTask? = mPreloadTasks[rawUrl]
        if (task != null) {
            task.cancel()
        }
        return if (isPreloaded(rawUrl)) {
            mHttpProxyCacheServer!!.getProxyUrl(rawUrl)
        } else {
            rawUrl
        }
    }
}