package com.knight.kotlin.module_mine.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.entity.UserInfoEntity
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_mine.repo.LoginRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.kotlin.module_mine.vm
 * @ClassName:      LoginViewModel
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/25 4:51 下午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/25 4:51 下午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

@HiltViewModel
class LoginViewModel @Inject constructor(private val mRepo: LoginRepo) : BaseViewModel(){
    /**
     *
     * 用户信息
     */
    val userInfo = MutableLiveData<UserInfoEntity>()
    /**
     * 登录
     */
    fun login(userName:String,passWord:String) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.login(userName, passWord)
                .catch {
                    toast(it.message ?: "")
                    requestSuccessFlag.postValue(false)
                }
                .collect {
                    userInfo.postValue(it) }
        }
    }
}