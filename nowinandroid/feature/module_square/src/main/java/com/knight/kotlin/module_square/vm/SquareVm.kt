package com.knight.kotlin.module_square.vm

import com.core.library_base.vm.BaseMviViewModel
import com.knight.kotlin.module_square.contract.SquareContract
import com.knight.kotlin.module_square.repo.SquareRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2024/4/22 11:06
 * Description:SquareVm
 */
@HiltViewModel
class SquareVm @Inject constructor(
    private val repo: SquareRepo
) : BaseMviViewModel<
        SquareContract.Event,
        SquareContract.State,
        SquareContract.Effect>() {

    override fun initialState() = SquareContract.State()

    override fun handleEvent(event: SquareContract.Event) {
        when (event) {
            is SquareContract.Event.LoadQuestions -> loadQuestions(event.page)
            is SquareContract.Event.CollectArticle -> collectArticle(event.articleId, event.position)
            is SquareContract.Event.UnCollectArticle -> unCollectArticle(event.articleId, event.position)
            is SquareContract.Event.UpdateKnowledgeLabel -> {}
        }
    }

    /** 获取问答列表 */
    private fun loadQuestions(page: Int) {
        requestFlowMvi(
            block = { repo.getQuestions(page) },
            onStart = {
                setState { copy(isLoading = true) }
            },
            onEach = { data ->
                setState { copy(questionPage = data, isLoading = false) }
            },
            onError = {
                setState { copy(isLoading = false) }
                setEffect { SquareContract.Effect.ShowError(it.message ?: "获取问答失败") }
            }
        )
    }

    /** 收藏文章 */
    private fun collectArticle(articleId: Int, position: Int) {
        requestFlowMvi(
            block = { repo.collectArticle(articleId) },
            onEach = {
                // ✅ 发 UpdateCollect
                setEffect { SquareContract.Effect.UpdateCollect(position, true) }
                setEffect { SquareContract.Effect.ShowToast("收藏成功") }
            },
            onError = {
                setEffect { SquareContract.Effect.ShowError(it.message ?: "收藏失败") }
            }
        )
    }

    /** 取消收藏 */
    private fun unCollectArticle(articleId: Int, position: Int) {
        requestFlowMvi(
            block = { repo.unCollectArticle(articleId) },
            onEach = {
                // ✅ 发 UpdateCollect
                setEffect { SquareContract.Effect.UpdateCollect(position, false) }
                setEffect { SquareContract.Effect.ShowToast("取消收藏成功") }
            },
            onError = {
                setEffect { SquareContract.Effect.ShowError(it.message ?: "取消收藏失败") }
            }
        )
    }
}