package com.knight.kotlin.module_course.api

import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_course.entity.CourseDetailListEntity
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Author:Knight
 * Time:2022/6/2 17:08
 * Description:CourseDetailListApi
 */
interface CourseDetailListApi {


    /**
     * 获取课程数据
     */
    @GET("article/list/{page}/json?page_size=10&order_type=1")
    suspend fun getDetailCourses(@Path("page") page:Int,@Query("cid") cid:Int): BaseResponse<CourseDetailListEntity>

}