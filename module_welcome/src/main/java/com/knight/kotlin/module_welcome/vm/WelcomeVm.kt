package com.knight.kotlin.module_welcome.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_welcome.entity.AppThemeBean
import com.knight.kotlin.module_welcome.repo.WelcomeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2021/12/21 18:12
 * Description:WelcomeVm
 * 闪屏模块VM层
 */
@HiltViewModel
class WelcomeVm @Inject constructor(private val mRepo:WelcomeRepo) :BaseViewModel(){

    val themeData = MutableLiveData<AppThemeBean>()


    /**
     *
     * 获取App Theme
     *
     */
    fun getAppTheme() {

       viewModelScope.launch(Dispatchers.IO) {
           mRepo.getAppTheme()
               .catch {
                   toast(it.message ?: "")
                   requestSuccessFlag.postValue(false)
               }
               .collect {
                   themeData.postValue(it)

               }
       }
    }
}