package com.knight.kotlin.module_project.vm

import com.core.library_base.vm.BaseMviViewModel
import com.knight.kotlin.module_project.contract.ProjectArticleContract
import com.knight.kotlin.module_project.repo.ProjectArticleRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/4/28 18:36
 * Description:ProjectArticleVm
 */
@HiltViewModel
class ProjectArticleVm @Inject constructor(
    private val repo: ProjectArticleRepo
) : BaseMviViewModel<
        ProjectArticleContract.Event,
        ProjectArticleContract.State,
        ProjectArticleContract.Effect>() {

    private var cid: Int = 0
    private var isNew: Boolean = false

    override fun initialState() = ProjectArticleContract.State()

    fun init(cid: Int, isNew: Boolean) {
        this.cid = cid
        this.isNew = isNew
    }

    override fun handleEvent(event: ProjectArticleContract.Event) {
        when (event) {
            ProjectArticleContract.Event.Refresh -> load(true)
            ProjectArticleContract.Event.LoadMore -> load(false)
            is ProjectArticleContract.Event.Collect -> collect(event)
            is ProjectArticleContract.Event.UnCollect -> unCollect(event)
        }
    }

    private fun load(isRefresh: Boolean) {
        val page = if (isRefresh) {
            if (isNew) 0 else 1
        } else {
            currentState.page
        }

        requestFlowMvi(
            block = {
                if (isNew) repo.getNewProjectArticle(page)
                else repo.getProjectArticle(page, cid)
            },
            onStart = {
                setState { copy(isLoading = isRefresh, isRefresh = isRefresh) }
            },
            onEach = { data ->
                val newList = if (isRefresh) {
                    data.datas
                } else {
                    currentState.list + data.datas
                }

                setState {
                    copy(
                        list = newList,
                        page = page + 1,
                        hasMore = data.datas.isNotEmpty(),
                        isLoading = false
                    )
                }
            },
            onError = {
                setState { copy(isLoading = false, error = it.message) }
                setEffect { ProjectArticleContract.Effect.Toast(it.message ?: "加载失败") }
            }
        )
    }

    private fun collect(event: ProjectArticleContract.Event.Collect) {
        requestFlowMvi(
            block = { repo.collectArticle(event.id) },
            onEach = {
                updateCollectState(event.position, true)
            },
            onError = {
                setEffect { ProjectArticleContract.Effect.Toast("收藏失败") }
            }
        )
    }

    private fun unCollect(event: ProjectArticleContract.Event.UnCollect) {
        requestFlowMvi(
            block = { repo.unCollectArticle(event.id) },
            onEach = {
                updateCollectState(event.position, false)
            },
            onError = {
                setEffect { ProjectArticleContract.Effect.Toast("取消收藏失败") }
            }
        )
    }

    private fun updateCollectState(position: Int, collect: Boolean) {
        val newList = currentState.list.toMutableList()

        val oldItem = newList[position]

        // ✅ 创建新对象（关键）
        newList[position] = oldItem.copy(collect = collect)

        setState { copy(list = newList) }
    }
}