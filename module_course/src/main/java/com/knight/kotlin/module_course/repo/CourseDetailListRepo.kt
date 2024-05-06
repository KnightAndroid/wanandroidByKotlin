package com.knight.kotlin.module_course.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_course.api.CourseDetailListApi
import com.knight.kotlin.module_course.entity.CourseDetailListEntity
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/6/2 17:40
 * Description:CourseDetailListRepo
 */
class CourseDetailListRepo @Inject constructor() : BaseRepository() {
    @Inject
    lateinit var mCourseDetailListApi: CourseDetailListApi

    /**
     * 获取课程列表
     */
    fun getDetailCourses(page:Int,cid:Int,failureCallBack:((String?) ->Unit) ?= null) = request<CourseDetailListEntity>({
        mCourseDetailListApi.getDetailCourses(page, cid).run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }){
        it?.let { it1 -> toast(it1) }
    }

}