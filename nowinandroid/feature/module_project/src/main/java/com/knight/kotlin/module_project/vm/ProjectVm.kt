package com.knight.kotlin.module_project.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.core.library_base.vm.BaseViewModel
import com.knight.kotlin.module_project.entity.ProjectTypeBean
import com.knight.kotlin.module_project.repo.ProjectRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/4/28 18:07
 * Description:ProjectVm
 */
@HiltViewModel
class ProjectVm @Inject constructor(private val mRepo:ProjectRepo) : BaseViewModel() {
    
    /**
     * 项目标题
     */
    fun getProjectTitle() : LiveData<MutableList<ProjectTypeBean>> {
        return mRepo.getProjectTitle().asLiveData()
    }
}