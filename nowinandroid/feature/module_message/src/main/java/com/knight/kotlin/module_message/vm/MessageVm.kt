package com.knight.kotlin.module_message.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.module_message.entity.MessageListEntity
import com.knight.kotlin.module_message.repo.MessageRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2023/5/16 16:50
 * Description:MessageVm
 */
@HiltViewModel
class MessageVm @Inject constructor(private val mRepo:MessageRepo) : BaseViewModel(){




    /**
     *
     * 获取已读消息
     */
    fun getMessageByReaded(page:Int): LiveData<MessageListEntity> {
        return mRepo.getMessageReaded(page).asLiveData()
    }

    /**
     *
     * 获取未读消息
     */
    fun getMessageByUnReaded(page:Int):LiveData<MessageListEntity>  {
        return mRepo.getMessageByUnRead(page).asLiveData()
    }
}