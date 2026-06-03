package com.knight.kotlin.module_mine.vm

import com.core.library_base.vm.BaseMviViewModel
import com.knight.kotlin.module_mine.contract.MyCollectArticleContract
import com.knight.kotlin.module_mine.repo.MyCollectArticleRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/13 10:45
 * Description:MyCollectArticleViewModel
 */
@HiltViewModel
class MyCollectArticleViewModel @Inject constructor(
    private val mRepo: MyCollectArticleRepo
) : BaseMviViewModel<
        MyCollectArticleContract.Event,
        MyCollectArticleContract.State,
        MyCollectArticleContract.Effect>() {

    override fun initialState() =
        MyCollectArticleContract.State()

    override fun handleEvent(
        event: MyCollectArticleContract.Event
    ) {

        when (event) {

            MyCollectArticleContract.Event.LoadData -> {
                requestArticleList(0)
            }

            MyCollectArticleContract.Event.Refresh -> {
                requestArticleList(0)
            }

            MyCollectArticleContract.Event.LoadMore -> {
                requestArticleList(currentState.page)
            }

            is MyCollectArticleContract.Event.UnCollectArticle -> {
                unCollectArticle(
                    event.articleId,
                    event.position
                )
            }
        }
    }

    /**
     * 收藏列表
     */
    private fun requestArticleList(
        page: Int
    ) {

        requestFlowMvi(

            block = {
                mRepo.getMyCollectArticles(page)
            },

            onStart = {

                if (page == 0) {

                    setState {
                        copy(isLoading = true)
                    }
                }
            },

            onEach = { result ->

                val noMoreData =
                    result.datas.isEmpty()

                setState {

                    copy(
                        isLoading = false,
                        page = if (noMoreData) page else page + 1,
                        articleData = result,
                        noMoreData = noMoreData
                    )
                }
            },

            onError = {

                setState {
                    copy(isLoading = false)
                }

                setEffect {
                    MyCollectArticleContract.Effect.ShowToast(
                        it.message ?: "加载失败"
                    )
                }
            }
        )
    }

    /**
     * 取消收藏
     */
    private fun unCollectArticle(
        articleId: Int,
        position: Int
    ) {

        requestFlowMvi(

            block = {
                mRepo.cancelCollectArticle(articleId)
            },

            onEach = {

                setEffect {
                    MyCollectArticleContract.Effect.RemoveItem(
                        position
                    )
                }
            },

            onError = {

                setEffect {
                    MyCollectArticleContract.Effect.ShowToast(
                        it.message ?: "取消收藏失败"
                    )
                }
            }
        )
    }
}