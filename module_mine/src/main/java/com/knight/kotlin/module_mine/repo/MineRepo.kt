package com.knight.kotlin.module_mine.repo

import com.knight.kotlin.library_base.entity.UserInfoEntity
import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_mine.api.MineApiService
import com.knight.kotlin.module_mine.entity.UserInfoMessageEntity
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/1/18 17:29
 * Description:MineRepo
 */
class MineRepo @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mMineApiService:MineApiService

    suspend fun getUserInfoCoin() = request<UserInfoMessageEntity>{
        mMineApiService.getUserInfoCoin().run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }

    /**
     *
     * 登录
     */
    suspend fun login(userName:String,passWord:String) = request<UserInfoEntity>{
        mMineApiService.login(userName,passWord).run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }
    }






}