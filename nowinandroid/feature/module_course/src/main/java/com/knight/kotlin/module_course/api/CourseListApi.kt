package com.knight.kotlin.module_course.api

import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_course.entity.CourseEntity
import retrofit2.http.GET

/**
 * Author:Knight
 * Time:2022/6/2 16:13
 * Description:CourseListApi
 */
interface CourseListApi {

    /**
     * 课程集合
     */
    @GET("chapter/547/sublist/json")
    suspend fun getCourses(): BaseResponse<MutableList<CourseEntity>>
}