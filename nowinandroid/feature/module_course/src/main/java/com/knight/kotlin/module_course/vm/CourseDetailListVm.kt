package com.knight.kotlin.module_course.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.core.library_base.vm.BaseViewModel

import com.knight.kotlin.module_course.entity.CourseDetailListEntity
import com.knight.kotlin.module_course.repo.CourseDetailListRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/6/2 17:40
 * DBescription:CourseDetailListVm
 */
@HiltViewModel
class CourseDetailListVm @Inject constructor(private val mRepo: CourseDetailListRepo) : BaseViewModel() {
    /**
     * 课程列表数据
     */
    fun getDetailCourses(page:Int,cid:Int) : LiveData<CourseDetailListEntity> {
        return mRepo.getDetailCourses(page,cid).asLiveData()
    }
}