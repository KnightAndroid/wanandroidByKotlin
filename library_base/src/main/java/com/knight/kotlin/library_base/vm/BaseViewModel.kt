package com.knight.kotlin.library_base.vm

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.knight.kotlin.library_base.R
import com.knight.kotlin.library_base.ktx.appStr

/**
 * Author:Knight
 * Time:2021/12/15 16:18
 * Description:BaseViewModel
 */
abstract class BaseViewModel : ViewModel() {
     val requestSuccessFlag = MutableLiveData<Boolean>(true)
     val _showLoading = MutableLiveData<String>()
     val _dismissLoading = MutableLiveData<Unit>()

     @MainThread
     fun showLoading(msg:String = appStr(R.string.base_loading)){
          _showLoading.postValue(msg)
     }

     fun dimissLoading() {
          _dismissLoading.postValue(Unit)
     }


}