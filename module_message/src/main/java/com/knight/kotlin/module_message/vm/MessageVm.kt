package com.knight.kotlin.module_message.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.module_message.entity.MessageListEntity
import com.knight.kotlin.module_message.repo.MessageRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2023/5/16 16:50
 * Description:MessageVm
 */
@HiltViewModel
class MessageVm @Inject constructor(private val mRepo:MessageRepo) : BaseViewModel(){

    //已读消息数据
    val mMessageReadedDatas = MutableLiveData<MessageListEntity>()
    //未读消息
    val mMessageUnReadDatas = MutableLiveData<MessageListEntity>()


    /**
     *
     * 获取已读消息
     */
    fun getMessageByReaded(page:Int) {
        viewModelScope.launch {
            mRepo.getMessageReaded(page)
                .flowOn(Dispatchers.IO)
                .onStart {
                    //开始

                }
                .onEach {
                    mMessageReadedDatas.postValue(it)
                }
                .onCompletion {
                    //结束
                }
                .catch {

                }
                .collect()

        }

    }

    /**
     *
     * 获取未读消息
     */
    fun getMessageByUnReaded(page:Int) {
        viewModelScope.launch {
            mRepo.getMessageByUnRead(page)
                .flowOn(Dispatchers.IO)
                .onStart {
                    //开始

                }
                .onEach {
                    mMessageUnReadDatas.postValue(it)
                }
                .onCompletion {
                    //结束
                }
                .catch {

                }
                .collect()

        }
    }
}