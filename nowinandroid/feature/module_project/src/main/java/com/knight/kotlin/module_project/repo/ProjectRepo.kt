package com.knight.kotlin.module_project.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_project.api.ProjectApi
import com.knight.kotlin.module_project.entity.ProjectTypeBean
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/4/28 18:00
 * Description:ProjectRepo
 */
class ProjectRepo @Inject constructor(
    private val api: ProjectApi
) : BaseRepository() {

    fun getProjectTitle() :Flow<List<ProjectTypeBean>> = request {
        val response = api.getProjectTtle()

        // 👉 统一异常抛出
        responseCodeExceptionHandler(response.code, response.msg)

        emit(response.data ?: emptyList())
    }
}