package com.knight.kotlin.module_mine.repo

import com.knight.kotlin.library_base.entity.UserInfoEntity
import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_mine.api.LoginApiService
import javax.inject.Inject

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.kotlin.module_mine.repo
 * @ClassName:      LoginRepo
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/25 4:49 下午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/25 4:49 下午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

class LoginRepo @Inject constructor() : BaseRepository() {
    @Inject
    lateinit var mLoginApiService: LoginApiService

    /**
     *
     * 登录
     */
    suspend fun login(userName:String,passWord:String) = request<UserInfoEntity>{
        mLoginApiService.login(userName,passWord).run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }
    }
}