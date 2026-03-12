package com.knight.kotlin.module_square.vm

import com.core.library_base.vm.BaseMviViewModel
import com.knight.kotlin.module_square.contract.SquareArticleContract
import com.knight.kotlin.module_square.repo.SquareArticleRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/4/11 11:21
 * Description:SquareArticleVm
 */
@HiltViewModel
class SquareArticleVm @Inject constructor(
    private val repo: SquareArticleRepo
) : BaseMviViewModel<
        SquareArticleContract.Event,
        SquareArticleContract.State,
        SquareArticleContract.Effect>() {

    override fun initialState() = SquareArticleContract.State()

    override fun handleEvent(event: SquareArticleContract.Event) {

        when (event) {

            is SquareArticleContract.Event.GetArticleByTag -> {
                getArticleByTag(event.page, event.keyword)
            }

            is SquareArticleContract.Event.CollectArticle -> {
                collectArticle(event.id)
            }

            is SquareArticleContract.Event.UnCollectArticle -> {
                unCollectArticle(event.id)
            }
        }
    }

    /**
     * 获取文章列表
     */
    private fun getArticleByTag(page: Int, keyword: String) {

        requestFlowMvi(

            block = { repo.getArticleByTag(page, keyword) },

            onStart = {
                setState {
                    copy(
                        loading = true
                    )
                }
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

                setState {
                    copy(
                        loading = false
                    )
                }

                setEffect {
                    SquareArticleContract.Effect.ShowError(
                        it.message ?: "获取文章失败"
                    )
                }
            }
        )
    }

    /**
     * 收藏文章
     */
    private fun collectArticle(id: Int) {

        requestFlowMvi(

            block = { repo.collectArticle(id) },

            onEach = {

                setEffect {
                    SquareArticleContract.Effect.CollectSuccess
                }
            },

            onError = {

                setEffect {
                    SquareArticleContract.Effect.ShowError(
                        it.message ?: "收藏失败"
                    )
                }
            }
        )
    }

    /**
     * 取消收藏
     */
    private fun unCollectArticle(id: Int) {

        requestFlowMvi(

            block = { repo.unCollectArticle(id) },

            onEach = {

                setEffect {
                    SquareArticleContract.Effect.UnCollectSuccess
                }
            },

            onError = {

                setEffect {
                    SquareArticleContract.Effect.ShowError(
                        it.message ?: "取消收藏失败"
                    )
                }
            }
        )
    }
}