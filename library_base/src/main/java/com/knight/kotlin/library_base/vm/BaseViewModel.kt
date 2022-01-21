package com.knight.kotlin.library_base.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Author:Knight
 * Time:2021/12/15 16:18
 * Description:BaseViewModel
 */
abstract class BaseViewModel : ViewModel() {
     val requestSuccessFlag = MutableLiveData<Boolean>(true)
}