package com.knight.kotlin.module_project.api

import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_project.entity.ProjectTypeBean
import retrofit2.http.GET

/**
 * Author:Knight
 * Time:2022/4/28 17:27
 * Description:ProjectApi
 */
interface ProjectApi {

    /**
     * 获取问答文章数据
     */
    @GET("project/tree/json")
    suspend fun getProjectTtle(): BaseResponse<MutableList<ProjectTypeBean>>
}