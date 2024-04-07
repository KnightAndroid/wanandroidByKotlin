package com.knight.kotlin.module_video.utils.precache


import com.knight.kotlin.module_video.utils.videocache.HttpProxyCacheServer
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService


/**
 * Author:Knight
 * Time:2024/4/7 10:33
 * Description:PreloadTask
 */
class PreloadTask : Runnable {
    /**
     * 原始地址
     */
    var mRawUrl: String? = null

    /**
     * 列表中的位置
     */
    var mPosition = 0

    /**
     * VideoCache服务器
     */
    var mCacheServer: HttpProxyCacheServer? = null

    /**
     * 是否被取消
     */
    private var mIsCanceled = false

    /**
     * 是否正在预加载
     */
    private var mIsExecuted = false
    override fun run() {
        if (!mIsCanceled) {
            start()
        }
        mIsExecuted = false
        mIsCanceled = false
    }

    /**
     * 开始预加载
     */
    private fun start() {
        var connection: HttpURLConnection? = null
        try {
            //获取HttpProxyCacheServer的代理地址
            val proxyUrl = mCacheServer!!.getProxyUrl(mRawUrl)
            val url = URL(proxyUrl)
            connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 10000
            connection!!.readTimeout = 10000
            val `in`: InputStream = BufferedInputStream(connection.inputStream)
            var length: Int
            var read = -1
            val bytes = ByteArray(8 * 1024)
            while (`in`.read(bytes).also { length = it } != -1) {
                read += length
                //预加载完成或者取消预加载
                if (mIsCanceled || read >= PreloadManager.PRELOAD_LENGTH) {
                    break
                }
            }
            if (read == -1) { //这种情况一般是预加载出错了，删掉缓存
                val cacheFile = mCacheServer!!.getCacheFile(mRawUrl)
                if (cacheFile.exists()) {
                    cacheFile.delete()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection?.disconnect()
        }
    }

    /**
     * 将预加载任务提交到线程池，准备执行
     */
    fun executeOn(executorService: ExecutorService) {
        if (mIsExecuted) return
        mIsExecuted = true
        executorService.submit(this)
    }

    /**
     * 取消预加载任务
     */
    fun cancel() {
        if (mIsExecuted) {
            mIsCanceled = true
        }
    }

    companion object {
        private const val TAG = "PreloadTask"
    }
}