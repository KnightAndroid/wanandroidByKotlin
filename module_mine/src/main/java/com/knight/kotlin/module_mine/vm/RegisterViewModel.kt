package com.knight.kotlin.module_mine.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.entity.UserInfoEntity
import com.knight.kotlin.library_base.ktx.appStr
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_mine.R
import com.knight.kotlin.module_mine.repo.RegisterRepo
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
        viewModelScope.launch {
            mRepo.regist(userName, passWord, repassword)
                .flowOn(Dispatchers.IO)
                .onStart {
                    //开始
                    showLoading(appStr(R.string.mine_request_login))
                }
                .onEach {
                    userInfo.postValue(it)
                }
                .onCompletion {
                    //结束
                    dimissLoading()
                }
                .catch { toast(it.message ?: "") }
                .collect()

        }


    }
}