package com.knight.kotlin.module_mine.vm

import com.core.library_base.vm.BaseMviViewModel
import com.knight.kotlin.module_mine.contract.MyShareArticlesContract
import com.knight.kotlin.module_mine.repo.MyShareArticlesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/17 10:30
 * Description:MyShareArticlesViewModel
 */
@HiltViewModel
class MyShareArticlesViewModel @Inject constructor(
    private val mRepo: MyShareArticlesRepo
) : BaseMviViewModel<
        MyShareArticlesContract.Event,
        MyShareArticlesContract.State,
        MyShareArticlesContract.Effect>() {

    override fun initialState() =
        MyShareArticlesContract.State()

    override fun handleEvent(
        event: MyShareArticlesContract.Event
    ) {

        when (event) {

            MyShareArticlesContract.Event.GetMyShareArticles -> {

                requestShareArticles(1)
            }

            MyShareArticlesContract.Event.Refresh -> {

                requestShareArticles(1)
            }

            MyShareArticlesContract.Event.LoadMore -> {

                requestShareArticles(
                    currentState.page
                )
            }

            is MyShareArticlesContract.Event.DeleteMyShareArticle -> {

                deleteArticle(
                    event.articleId,
                    event.position
                )
            }
        }
    }

    /**
     * 获取我的分享文章
     */
    private fun requestShareArticles(
        page: Int
    ) {

        requestFlowMvi(

            block = {
                mRepo.getMyShareArticles(page)
            },

            onStart = {

                if (page == 1) {

                    setState {
                        copy(
                            isLoading = true
                        )
                    }
                }
            },

            onEach = { result ->

                val list = if (page == 1) {

                    result.shareArticles.datas

                } else {

                    currentState.articleList +
                            result.shareArticles.datas
                }

                val hasMore =
                    result.shareArticles.datas.isNotEmpty()

                setState {

                    copy(

                        isLoading = false,

                        page = if (hasMore)
                            page + 1
                        else
                            page,

                        articleList = list,

                        hasMore = hasMore,

                        requestSuccess = true,

                        isEmpty = list.isEmpty(),

                        refreshSuccess = page == 1,

                        loadMoreSuccess = page != 1
                    )
                }
            },

            onError = {

                setState {

                    copy(
                        isLoading = false
                    )
                }

                setEffect {

                    MyShareArticlesContract.Effect.ShowToast(

                        it.message ?: "获取分享文章失败"

                    )
                }
            }
        )
    }

    /**
     * 删除分享文章
     */
    private fun deleteArticle(
        articleId: Int,
        position: Int
    ) {

        requestFlowMvi(

            block = {
                mRepo.deleteMyShareArticles(
                    articleId
                )
            },

            onEach = {

                setEffect {

                    MyShareArticlesContract.Effect.DeleteSuccess(
                        position
                    )
                }
            },

            onError = {

                setEffect {

                    MyShareArticlesContract.Effect.ShowToast(

                        it.message ?: "删除失败"

                    )
                }
            }
        )
    }
}