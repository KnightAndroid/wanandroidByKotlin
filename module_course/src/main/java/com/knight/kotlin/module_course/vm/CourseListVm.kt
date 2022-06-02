package com.knight.kotlin.module_course.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_course.entity.CourseEntity
import com.knight.kotlin.module_course.repo.CourseListRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/6/2 16:38
 * Description:CourseListVm
 */
@HiltViewModel
class CourseListVm @Inject constructor(private val mRepo:CourseListRepo) : BaseViewModel() {


    //热词数据
    val courseLists = MutableLiveData<MutableList<CourseEntity>>()
    /**
     * 课程列表数据
     */
    fun getCourses() {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.getCourses()
                .onStart {

                }
                .onEach {
                    courseLists.postValue(it) }
                .onCompletion {

                }
                .catch {
                    toast(it.message ?: "")

                }
                .collect()
        }
    }
}