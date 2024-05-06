package com.knight.kotlin.module_mine.repo

import com.knight.kotlin.library_base.entity.UserInfoEntity
import com.knight.kotlin.library_base.ktx.dimissLoadingDialog
import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_mine.api.RegisterApiService
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/4/6 14:54
 * Description:RegisterRepo
 */
class RegisterRepo @Inject constructor() : BaseRepository() {
    @Inject
    lateinit var mRegisterApiService: RegisterApiService

    /**
     *
     * 注册
     */
    fun regist(userName:String,passWord:String,repassword:String) = request<UserInfoEntity>({
        mRegisterApiService.register(userName,passWord,repassword).run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }){
        dimissLoadingDialog()
        it?.run{
            toast(it)
        }
    }
}