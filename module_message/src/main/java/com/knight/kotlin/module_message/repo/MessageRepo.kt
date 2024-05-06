package com.knight.kotlin.module_message.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_message.api.MessageApiService
import com.knight.kotlin.module_message.entity.MessageListEntity
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2023/5/16 16:21
 * Description:MessageRepo
 */
class MessageRepo @Inject constructor(): BaseRepository()  {
    @Inject
    lateinit var mMessageApiService: MessageApiService


    /**
     * 获取已读信息
     *
     */
    fun getMessageReaded(page:Int) = request<MessageListEntity> ({
        mMessageApiService.getMessageByReaded(page).run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }){
        it?.run {
            toast(it)
        }
    }

    /**
     *
     * 获取未读消息
     */
    fun getMessageByUnRead(page:Int) = request<MessageListEntity> ({
        mMessageApiService.getMessageByUnReaded(page).run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }){
        it?.run {
            toast(it)
        }
    }
}