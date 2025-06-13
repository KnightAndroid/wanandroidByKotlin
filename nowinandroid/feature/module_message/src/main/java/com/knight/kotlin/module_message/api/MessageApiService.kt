package com.knight.kotlin.module_message.api

import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_message.entity.MessageListEntity
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Author:Knight
 * Time:2023/5/16 16:24
 * Description:MessageApiService
 */
interface MessageApiService {

    /**
     * 获取已读消息记录
     */
    @GET("message/lg/readed_list/{page}/json?page_size=10")
    suspend fun getMessageByReaded(@Path("page") page:Int): BaseResponse<MessageListEntity>


    /**
     * 获取未读消息记录
     */
    @GET("message/lg/unread_list/{page}/json?page_size=10")
    suspend fun getMessageByUnReaded(@Path("page") page:Int): BaseResponse<MessageListEntity>
}