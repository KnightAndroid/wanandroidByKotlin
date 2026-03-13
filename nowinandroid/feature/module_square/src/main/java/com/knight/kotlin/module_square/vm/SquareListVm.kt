package com.knight.kotlin.module_square.vm

import com.core.library_base.vm.BaseMviViewModel
import com.knight.kotlin.module_square.contract.SquareListContract
import com.knight.kotlin.module_square.repo.SquareShareListRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/4/27 17:05
 * Description:SquareListVm
 */
@HiltViewModel
class SquareListVm @Inject constructor(
    private val mRepo: SquareShareListRepo
) : BaseMviViewModel<
        SquareListContract.Event,
        SquareListContract.State,
        SquareListContract.Effect>() {

    override fun initialState() = SquareListContract.State()

    override fun handleEvent(event: SquareListContract.Event) {
        when (event) {
            is SquareListContract.Event.GetSquareArticles -> {
                getSquareArticles(event.page)
            }
            is SquareListContract.Event.CollectArticle -> {
                collectArticle(event.id)
            }
            is SquareListContract.Event.UnCollectArticle -> {
                unCollectArticle(event.id)
            }
        }
    }

    /**
     * 获取广场文章列表
     */
    private fun getSquareArticles(page: Int) {
        requestFlowMvi(
            block = { mRepo.getSquareArticles(page) },
            onStart = {
                setState { copy(loading = true) }
            },
            onEach = { data ->
                setState {
                    copy(
                        articleList = data,
                        loading = false
                    )
                }
            },
            onError = {
                setState { copy(loading = false) }
                setEffect {
                    SquareListContract.Effect.ShowError(it.message ?: "获取文章失败")
                }
            }
        )
    }

    /**
     * 收藏文章
     */
    private fun collectArticle(id: Int) {
        requestFlowMvi(
            block = { mRepo.collectArticle(id) },
            onEach = {
                setEffect { SquareListContract.Effect.CollectSuccess }
            },
            onError = {
                setEffect { SquareListContract.Effect.ShowError(it.message ?: "收藏失败") }
            }
        )
    }

    /**
     * 取消收藏文章
     */
    private fun unCollectArticle(id: Int) {
        requestFlowMvi(
            block = { mRepo.unCollectArticle(id) },
            onEach = {
                setEffect { SquareListContract.Effect.UnCollectSuccess }
            },
            onError = {
                setEffect { SquareListContract.Effect.ShowError(it.message ?: "取消收藏失败") }
            }
        )
    }
}