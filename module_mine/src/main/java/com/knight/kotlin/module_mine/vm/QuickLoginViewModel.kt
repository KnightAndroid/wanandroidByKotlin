package com.knight.kotlin.module_mine.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.entity.UserInfoEntity
import com.knight.kotlin.library_base.ktx.appStr
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_mine.R
import com.knight.kotlin.module_mine.repo.QuickLoginRepo
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
     *
     * 用户信息
     */
    val userInfo = MutableLiveData<UserInfoEntity>()
    /**
     * 登录
     */
    fun login(userName:String,passWord:String) {
        viewModelScope.launch {
            mRepo.login(userName, passWord)
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