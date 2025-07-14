package com.knight.kotlin.module_mine.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.core.library_base.vm.BaseViewModel
import com.core.library_common.entity.UserInfoEntity
import com.knight.kotlin.module_mine.repo.QuickLoginRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.kotlin.module_mine.vm
 * @ClassName:      QuickLoginViewModel
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/24 11:56 上午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/24 11:56 上午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

@HiltViewModel
class QuickLoginViewModel @Inject constructor(private val mRepo: QuickLoginRepo) : BaseViewModel(){

    /**
     * 登录
     */
    fun login(userName:String,passWord:String): LiveData<UserInfoEntity> {
        return mRepo.login(userName,passWord).asLiveData()
    }
}