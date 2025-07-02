package com.core.library_base.vm

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.core.library_base.R
import com.core.library_base.ktx.appStr
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

/**
 * Author:Knight
 * Time:2021/12/15 16:18
 * Description:BaseViewModel
 */
abstract class BaseViewModel : ViewModel() {
     val requestSuccessFlag = MutableLiveData(true)
     val showLoading = MutableLiveData<String>()
     val dismissLoading = MutableLiveData<Unit>()

     @MainThread
     fun showLoading(msg:String = appStr(R.string.base_loading)){
          showLoading.postValue(msg)
     }

     fun dimissLoading() {
          dismissLoading.postValue(Unit)
     }

     val mStateLiveData = MutableLiveData<State>()

     fun <T> liveDataEx(block: suspend () -> T) = liveData {
          kotlin.runCatching {
             //  mStateLiveData.value = LoadState
               block()
          }.onSuccess {
               emit(it)
           //    mStateLiveData.value = SuccessState
          }.onFailure { e ->
           //    mStateLiveData.value = ErrorState(e.message)
          }
     }


     //使用Flow流式编程类似RxJava
     fun <T> flowEx(block: suspend () -> T) = flow {
          emit(block())
     }.onStart {
         // mStateLiveData.value = LoadState
     }.onCompletion {
         // mStateLiveData.value = SuccessState
     }.catch { cause ->
        //  mStateLiveData.value = ErrorState(cause.message)
     }.asLiveData()

     sealed class State

     object LoadState : State()

     object SuccessState : State()

     class ErrorState(val errorMsg: String?) : State()


}