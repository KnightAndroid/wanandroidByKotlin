package com.knight.kotlin.module_welcome.util

import android.content.Context
import com.knight.kotlin.library_util.image.ImageLoader
import java.util.Calendar

/**
 * @author created by luguian
 * @organize
 * @Date 2025/12/11 15:50
 * @descript:预加载帮助类
 */
object WeekImagePreloadUtils {


    // 根据星期几获取 0~6 的索引
    private fun getTodayIndex(): Int {
        val calendar = Calendar.getInstance()
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> 0
            Calendar.MONDAY -> 1
            Calendar.TUESDAY -> 2
            Calendar.WEDNESDAY -> 3
            Calendar.THURSDAY -> 4
            Calendar.FRIDAY -> 5
            Calendar.SATURDAY -> 6
            else -> 0
        }
    }

    // 获取对应 URL
    fun getTodayImageUrl(): String {
        val index = getTodayIndex()
        return "https://api.suxun.site/60s/$index.png"
    }

    /**
     * 预加载今日图片
     * @param context 上下文
     * @param onFinish 可选回调（预加载立即返回，无阻塞）
     */
    fun preloadTodayImage(
        context: Context,
        onFinish: (() -> Unit)? = null
    ) {
        val url = getTodayImageUrl()

        ImageLoader.Companion.preloadUrl(context.applicationContext,url)

        // preload 是异步的，但调用结束就代表任务已进入队列
        onFinish?.let { it() }
    }
}