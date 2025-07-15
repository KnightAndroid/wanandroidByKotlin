package com.knight.kotlin.module_mine.api

import com.knight.kotlin.library_common.entity.UserInfoEntity
import com.knight.kotlin.library_network.bean.BaseResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.kotlin.module_mine.api
 * @ClassName:      LoginApiService
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/25 4:48 下午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/25 4:48 下午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

interface LoginApiService {

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(@Field("username") userName:String, @Field("password") passWord:String): BaseResponse<UserInfoEntity>
}