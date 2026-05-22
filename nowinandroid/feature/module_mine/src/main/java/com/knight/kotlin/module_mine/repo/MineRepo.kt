package com.knight.kotlin.module_mine.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_common.entity.UserInfoEntity
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_mine.api.MineApiService
import com.knight.kotlin.module_mine.entity.UserInfoMessageEntity
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/1/18 17:29
 * Description:MineRepo
 */
class MineRepo @Inject constructor(
    private val api: MineApiService
) : BaseRepository() {

    /**
     * 获取用户金币信息
     */
    fun getUserInfoCoin() =
        request<UserInfoMessageEntity> {

            val res = api.getUserInfoCoin()

            responseCodeExceptionHandler(
                res.code,
                res.msg
            )

            emit(res.data)
        }

    /**
     * 登录
     */
    fun login(
        userName: String,
        passWord: String
    ) =
        request<UserInfoEntity> {

            val res = api.login(
                userName,
                passWord
            )

            responseCodeExceptionHandler(
                res.code,
                res.msg
            )

            emit(res.data)
        }
}