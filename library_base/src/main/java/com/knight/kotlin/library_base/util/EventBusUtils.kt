package com.knight.kotlin.library_base.util

import com.knight.kotlin.library_base.event.MessageEvent
import org.greenrobot.eventbus.EventBus


/**
 * Author:Knight
 * Time:2021/12/16 17:55
 * Description:EventBusUtils
 * EventBus工具类
 */
object EventBusUtils {

    /**
     * 订阅
     * @param subscriber 订阅者
     */
    fun register(subscriber:Any) = EventBus.getDefault().register(subscriber)

    /**
     * 解除注册
     * @param subscriber 订阅者
     *
     */
    fun unRegister(subscriber: Any) = EventBus.getDefault().unregister(subscriber)

    /**
     * 发送粘性事件
     * @param strickyEvent 粘性事件
     *
     */
    fun postStrickyEvent(strickyEvent:Any) = EventBus.getDefault().postSticky(strickyEvent)


    /**
     *
     * 发送普通事件
     */
    fun postEvent(event:MessageEvent) = EventBus.getDefault().post(event)

    /**
     * 手动获取粘性事件
     * @param stickyEventType 粘性事件
     * @param <T> 事件泛型
     * @return 返回给定事件类型的最近粘性事件
     *
     */
    fun <T> getStrickyEvent(stickyEventType :Class<T>) :T =
        EventBus.getDefault().getStickyEvent(stickyEventType)



}