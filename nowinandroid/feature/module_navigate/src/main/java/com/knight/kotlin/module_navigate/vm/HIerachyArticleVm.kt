package com.knight.kotlin.module_navigate.vm

import com.core.library_base.vm.BaseMviViewModel
import com.knight.kotlin.module_navigate.contract.HierachyArticleContract
import com.knight.kotlin.module_navigate.repo.HierachyArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/6 16:12
 * Description:HIerachyArticleVm
 */

@HiltViewModel
class HierachyArticleVm @Inject constructor(
    private val repo: HierachyArticleRepository
) : BaseMviViewModel<
        HierachyArticleContract.Event,
        HierachyArticleContract.State,
        HierachyArticleContract.Effect>() {

    override fun initialState() = HierachyArticleContract.State()

    override fun handleEvent(event: HierachyArticleContract.Event) {
        when (event) {
            is HierachyArticleContract.Event.LoadData -> loadData(event.isRefresh)
            is HierachyArticleContract.Event.Collect -> collect(event)
            is HierachyArticleContract.Event.UnCollect -> unCollect(event)
        }
    }

    private var currentCid: Int = 0

    fun setCid(cid: Int) {
        currentCid = cid
    }

    /**
     * 列表加载（分页）
     */
    private fun loadData(isRefresh: Boolean) {
        val page = if (isRefresh) 0 else currentState.page

        requestFlowMvi(
            block = { repo.getHierachyArticle(page, currentCid) },

            onStart = {
                setState { copy(isLoading = page == 0) }
            },

            onEach = { data ->

                val newList = if (page == 0) {
                    data.datas.toMutableList()
                } else {
                    (currentState.list + data.datas).toMutableList()
                }

                setState {
                    copy(
                        isLoading = false,
                        list = newList,
                        page = page + 1,
                        hasMore = data.datas.isNotEmpty()
                    )
                }

                setEffect {
                    if (isRefresh)
                        HierachyArticleContract.Effect.FinishRefresh
                    else
                        HierachyArticleContract.Effect.FinishLoadMore
                }
            },

            onError = {
                setState { copy(isLoading = false) }
                setEffect {
                    HierachyArticleContract.Effect.ShowToast(it.message ?: "error")
                }
            }
        )
    }

    /**
     * 收藏
     */
    private fun collect(event: HierachyArticleContract.Event.Collect) {
        requestFlowMvi(
            block = { repo.collectArticle(event.id) },

            onEach = {
                setEffect {
                    HierachyArticleContract.Effect.UpdateItem(event.position, true)
                }
            },

            onError = {
                setEffect {
                    HierachyArticleContract.Effect.ShowToast("收藏失败")
                }
            }
        )
    }

    /**
     * 取消收藏
     */
    private fun unCollect(event: HierachyArticleContract.Event.UnCollect) {
        requestFlowMvi(
            block = { repo.cancelCollectArticle(event.id) },

            onEach = {
                setEffect {
                    HierachyArticleContract.Effect.UpdateItem(event.position, false)
                }
            },

            onError = {
                setEffect {
                    HierachyArticleContract.Effect.ShowToast("取消收藏失败")
                }
            }
        )
    }
}