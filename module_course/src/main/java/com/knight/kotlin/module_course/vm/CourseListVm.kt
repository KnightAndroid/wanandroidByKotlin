package com.knight.kotlin.module_course.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.module_course.entity.CourseEntity
import com.knight.kotlin.module_course.repo.CourseListRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/6/2 16:38
 * Description:CourseListVm
 */
@HiltViewModel
class CourseListVm @Inject constructor(private val mRepo:CourseListRepo) : BaseViewModel() {


    //热词数据
//    val courseLists = MutableLiveData<MutableList<CourseEntity>>()
    /**
     * 课程列表数据
     */
    fun getCourses() : LiveData<MutableList<CourseEntity>> {
        return mRepo.getCourses().asLiveData()
    }
}