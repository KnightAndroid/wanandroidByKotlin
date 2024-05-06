package com.knight.kotlin.module_course.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_course.api.CourseListApi
import com.knight.kotlin.module_course.entity.CourseEntity
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/6/2 16:39
 * Description:CourseListRepo
 */
class CourseListRepo @Inject constructor() : BaseRepository() {
    @Inject
    lateinit var mCourseListApi: CourseListApi

    /**
     * 获取课程列表
     */
    fun getCourses(failureCallBack:((String?) ->Unit) ?= null) = request<MutableList<CourseEntity>>({
        mCourseListApi.getCourses().run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }) {
        it?.let { it1 -> toast(it1) }
    }
}