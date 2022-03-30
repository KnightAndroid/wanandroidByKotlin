package com.knight.kotlin.module_mine.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.entity.UserInfoEntity
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_mine.entity.UserInfoCoinEntity
import com.knight.kotlin.module_mine.repo.MineRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/1/18 17:40
 * Description:MineViewModel
 */
@HiltViewModel
class MineViewModel @Inject constructor(private val mRepo:MineRepo) : BaseViewModel(){

    /**
     * 用户金币
     */
    val userInfoCoin = MutableLiveData<UserInfoCoinEntity>()
    /**
     * 用户信息
     */
    val userInfo = MutableLiveData<UserInfoEntity>()


    /**
     * 获取用户金币
     */
    fun getUserInfoCoin() {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.getUserInfoCoin()
                .catch {
                    toast(it.message ?: "")
                    requestSuccessFlag.postValue(false)
                }
                .collect {
                    userInfoCoin.postValue(it)
                }
        }
    }

    /**
     * 登录
     */
    fun login(userName:String,passWord:String) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.login(userName, passWord)
                .catch {
                    toast(it.message ?: "")
                }
                .collect {
                    userInfo.postValue(it) }
        }
    }
}