package com.knight.kotlin.module_set.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_set.repo.SetRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/3/30 15:35
 * Description:SetVm
 */
@HiltViewModel
class SetVm @Inject constructor(private val mRepo:SetRepo) : BaseViewModel() {


    //是否收藏成功
    val logoutStatus = MutableLiveData<Boolean>()

    /**
     * 退出账户
     */
    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.logout()
                .catch {
                    toast(it.message ?: "")
                    logoutStatus.postValue(false)
                }
                .collect {
                    logoutStatus.postValue(true)
                }
        }
    }
}