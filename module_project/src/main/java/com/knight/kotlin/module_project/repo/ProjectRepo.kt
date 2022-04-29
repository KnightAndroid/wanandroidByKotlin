package com.knight.kotlin.module_project.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_project.api.ProjectApi
import com.knight.kotlin.module_project.entity.ProjectTypeBean
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/4/28 18:00
 * Description:ProjectRepo
 */
class ProjectRepo @Inject constructor() : BaseRepository() {
    @Inject
    lateinit var mProjectApi: ProjectApi

    /**
     * 获取项目标题列表
     */
    suspend fun getProjectTitle() = request<MutableList<ProjectTypeBean>>{
        mProjectApi.getProjectTtle().run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }
}