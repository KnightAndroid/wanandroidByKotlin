package com.knight.kotlin.module_video.utils.precache

import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.text.TextUtils
import com.knight.kotlin.module_video.entity.VideoPreLoaderEntity
import java.net.HttpURLConnection
import java.net.URL


/**
 * Author:Knight
 * Time:2024/4/7 10:34
 * Description:VideoPreLoader
 */
class VideoPreLoader {

    private var handler: Handler
    private var handlerThread: HandlerThread? = null
    private val cancelList: MutableList<String> = ArrayList()
    private var instance: VideoPreLoader? = null
    init {
        handlerThread = HandlerThread("VideoPreLoader_HandlerThread")
        handlerThread!!.start()
        handler = object : Handler(handlerThread!!.looper) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
            }
        }
    }



    @Synchronized
    fun getInstance(): VideoPreLoader? {
        if (instance == null) {
            instance = VideoPreLoader()
        }
        return instance
    }


    /**
     * 当前正在播放的url
     */
    private var currentUrl: String? = null

    /**
     * 添加预加载路径
     *
     * @param data 预加载数据
     */
    fun addPreLoadUrl(data: VideoPreLoaderEntity?) {
        handler.post(Runnable { realPreload(data) })
    }

    /**
     * 取消置顶的预加载url
     *
     * @param url
     */
    fun cancelPreLoadURLIfNeeded(url: String) {
        cancelList.add(url)
    }

    /**
     * 设置当前正在播放的url
     *
     * @param url 当前正在播放的url
     */
    fun setCurrentUrl(url: String?) {
        currentUrl = url
    }

    /**
     * 取消所有的预加载url
     */
    fun cancelAnyPreLoads() {
        handler.removeCallbacksAndMessages(null)
        cancelList.clear()
    }

    /**
     * 开始预加载
     *
     * @param data
     */
    private fun realPreload(data: VideoPreLoaderEntity?) {
        //如果原始url为空则停止向下执行
        if (data == null || isCancel(data.originalUrl)) {
            return
        }
        var conn: HttpURLConnection? = null
        try {
            //此处为连接本地代理服务，不会执行真正的预加载动作。真正的预加载动作在AndroidVideoCache中执行
            val mUrl = URL(data.proxyUrl)
            conn = mUrl.openConnection() as HttpURLConnection
            conn.connect()
            val `is` = conn!!.inputStream
            val buffer = ByteArray(1024)
            var downloadSize = 0
            //此处有可能会出现两个下载路径同时下载数据到缓存文件中的情况，视频会播放异常，此时必须停止一个
            do {
//                String endWidth = "http://127.0.0.1:36403/";
//                if (currentUrl != null && data.getProxyUrl().endsWith(currentUrl.substring(endWidth.length()))) {//如果视频正在播放就break
//                    Log.e("VideoPreLoader", "当前正在播放此url,为防止冲突，请立马取消此链接的缓存");
//                    break;
//                }
                val numRead = `is`.read(buffer)
                downloadSize += numRead
                if ( /*downloadSize >= data.getPreLoadBytes() || */numRead == -1) {
                    break
                }
            } while (true)
            `is`.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (conn != null) {
//                    conn.disconnect();
                }
            } catch (e: Exception) {
            }
        }
    }

    /**
     * @description url是否已经被取消了。
     * @date: 2021/3/24 10:57
     * @author: wei.yang
     */
    private fun isCancel(url: String): Boolean {
        if (TextUtils.isEmpty(url)) {
            return true
        }
        for (cancelUrl in cancelList) {
            if (cancelUrl == url) {
                return true
            }
        }
        return false
    }

}