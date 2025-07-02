package com.knight.kotlin.module_mine.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.core.library_base.vm.BaseViewModel
import com.knight.kotlin.library_base.entity.UserInfoEntity
import com.knight.kotlin.module_mine.repo.LoginRepo
import dagger.hilt.android.lifecycle.HiltViewModel
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
     * 登录
     */
    fun login(userName:String,passWord:String,failureCallBack:((String?) ->Unit) ?= null): LiveData<UserInfoEntity> {
        return mRepo.login(userName, passWord,failureCallBack).asLiveData()
    }
}