package com.knight.kotlin.module_mine.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.core.library_base.vm.BaseViewModel
import com.knight.kotlin.library_common.entity.UserInfoEntity
import com.knight.kotlin.module_mine.entity.UserInfoMessageEntity
import com.knight.kotlin.module_mine.repo.MineRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/1/18 17:40
 * Description:MineViewModel
 */
@HiltViewModel
class MineViewModel @Inject constructor(private val mRepo:MineRepo) : BaseViewModel(){

    /**
     * 获取用户金币
     */
    fun getUserInfoCoin(failureCallBack:((String?) ->Unit) ?= null): LiveData<UserInfoMessageEntity> {
        return mRepo.getUserInfoCoin(failureCallBack).asLiveData()
    }

    /**
     * 登录
     */
    fun login(userName:String,passWord:String):LiveData<UserInfoEntity> {
        return mRepo.login(userName, passWord).asLiveData()
    }
}