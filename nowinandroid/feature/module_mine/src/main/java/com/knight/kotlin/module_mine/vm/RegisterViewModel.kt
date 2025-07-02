package com.knight.kotlin.module_mine.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.core.library_base.vm.BaseViewModel
import com.knight.kotlin.library_base.entity.UserInfoEntity
import com.knight.kotlin.module_mine.repo.RegisterRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/4/6 15:07
 * Description:RegisterViewModel
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(private val mRepo: RegisterRepo) : BaseViewModel() {


    /**
     * 注册
     */
    fun register(userName: String, passWord: String, repassword: String): LiveData<UserInfoEntity> {
        return mRepo.regist(userName, passWord, repassword).asLiveData()
    }
}