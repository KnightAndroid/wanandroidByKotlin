package com.knight.kotlin.module_course.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_course.entity.CourseDetailListEntity
import com.knight.kotlin.module_course.repo.CourseDetailListRepo
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
 * Time:2022/6/2 17:40
 * DBescription:CourseDetailListVm
 */
@HiltViewModel
class CourseDetailListVm @Inject constructor(private val mRepo: CourseDetailListRepo) : BaseViewModel() {
    //热词数据
    val courseDetailList = MutableLiveData<CourseDetailListEntity>()
    /**
     * 课程列表数据
     */
    fun getDetailCourses(page:Int,cid:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.getDetailCourses(page, cid)
                .onStart {

                }
                .onEach {
                    courseDetailList.postValue(it)
                }
                .onCompletion {

                }
                .catch {
                    toast(it.message ?: "")

                }
                .collect()
        }
    }
}