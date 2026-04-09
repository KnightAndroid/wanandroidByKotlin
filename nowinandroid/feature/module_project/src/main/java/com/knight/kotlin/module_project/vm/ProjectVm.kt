package com.knight.kotlin.module_project.vm

import com.core.library_base.vm.BaseMviViewModel
import com.knight.kotlin.module_project.contract.ProjectContract
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
class ProjectVm @Inject constructor(
    private val repo: ProjectRepo
) : BaseMviViewModel<
        ProjectContract.Event,
        ProjectContract.State,
        ProjectContract.Effect>() {

    override fun initialState() = ProjectContract.State()

    override fun handleEvent(event: ProjectContract.Event) {
        when (event) {
            ProjectContract.Event.LoadProjectTypes -> loadProjectTypes()
            ProjectContract.Event.Retry -> loadProjectTypes()
        }
    }

    private fun loadProjectTypes() {
        requestFlowMvi(
            block = { repo.getProjectTitle() },
            onStart = {
                setState { copy(isLoading = true, error = null) }
            },
            onEach = { list ->

                // 👉 VM 处理“最新项目”
                val newList = mutableListOf<ProjectTypeBean>()
                val latest = ProjectTypeBean().apply {
                    name = "最新项目"
                    id = 0
                }
                newList.add(latest)
                newList.addAll(list)

                setState {
                    copy(
                        isLoading = false,
                        projectTypes = newList
                    )
                }

                // ✅ 关键：只发一次 UI 初始化行为
                setEffect {
                    ProjectContract.Effect.InitViewPager(newList)
                }
            },
            onError = {
                setState { copy(isLoading = false, error = it.message) }
                setEffect {
                    ProjectContract.Effect.ShowToast(it.message ?: "加载失败")
                }
            }
        )
    }
}