package com.knight.kotlin.module_mine.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.entity.UserInfoEntity
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_mine.repo.RegisterRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/4/6 15:07
 * Description:RegisterViewModel
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(private val mRepo: RegisterRepo) : BaseViewModel() {


    /**
     *
     * 用户信息
     */
    val userInfo = MutableLiveData<UserInfoEntity>()

    /**
     * 注册
     */
    fun register(userName: String, passWord: String, repassword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.regist(userName, passWord, repassword)
                .catch {
                    toast(it.message ?: "")
                    requestSuccessFlag.postValue(false)
                }
                .collect {
                    userInfo.postValue(it)
                }
        }
    }
}