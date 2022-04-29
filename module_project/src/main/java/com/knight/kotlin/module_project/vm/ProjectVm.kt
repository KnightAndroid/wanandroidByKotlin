package com.knight.kotlin.module_project.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_project.entity.ProjectTypeBean
import com.knight.kotlin.module_project.repo.ProjectRepo
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
 * Time:2022/4/28 18:07
 * Description:ProjectVm
 */
@HiltViewModel
class ProjectVm @Inject constructor(private val mRepo:ProjectRepo) : BaseViewModel() {

    //项目类别标题
    val projectTitles = MutableLiveData<MutableList<ProjectTypeBean>>()

    /**
     * 项目标题
     */
    fun getProjectTitle() {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.getProjectTitle()
                .onStart {

                }
                .onEach {
                    projectTitles.postValue(it)
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