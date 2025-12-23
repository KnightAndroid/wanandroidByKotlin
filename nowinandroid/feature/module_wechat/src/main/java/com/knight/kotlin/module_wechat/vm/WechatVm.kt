package com.knight.kotlin.module_wechat.vm

import com.core.library_base.vm.BaseMviViewModel
import com.knight.kotlin.module_wechat.contact.WechatContract
import com.knight.kotlin.module_wechat.repo.WechatRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.kotlin.module_wechat.vm
 * @ClassName:      WechatVm
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/21 10:34 上午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/21 10:34 上午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

@HiltViewModel
class WechatVm @Inject constructor(
    private val repo: WechatRepo
) : BaseMviViewModel<
        WechatContract.Event,
        WechatContract.State,
        WechatContract.Effect>() {

    override fun initialState() = WechatContract.State()

    override fun handleEvent(event: WechatContract.Event) {
        when (event) {
            is WechatContract.Event.LoadArticles ->
                loadArticles(event)

            is WechatContract.Event.CollectArticle ->
                collectArticle(event)

            is WechatContract.Event.UnCollectArticle ->
                unCollectArticle(event)
        }
    }

    // ========================
    // 加载文章列表
    // ========================
    private fun loadArticles(event: WechatContract.Event.LoadArticles) {
        requestFlowMvi(
            block = {
                if (event.keyword.isNullOrEmpty()) {
                    repo.getWechatArticle(event.cid, event.page)
                } else {
                    repo.getWechatArticleByKeyWords(
                        event.cid,
                        event.page,
                        event.keyword
                    )
                }
            },
            onStart = {
                setState {
                    copy(
                        isLoading = !event.isRefresh,
                        isRefreshing = event.isRefresh
                    )
                }
            },
            onEach = { pageData ->
                setState {
                    copy(
                        articlePage =
                            if (event.page == 1 || articlePage == null) {
                                pageData
                            } else {
                                pageData.copy(
                                    datas = (articlePage!!.datas + pageData.datas)
                                        .toMutableList()
                                )
                            },
                        isLoading = false,
                        isRefreshing = false
                    )
                }
            },
            onError = {
                setState {
                    copy(
                        isLoading = false,
                        isRefreshing = false,
                        isError = true
                    )
                }
                setEffect {
                    WechatContract.Effect.ShowToast("获取公众号文章失败")
                }
            }
        )
    }

    // ========================
    // 收藏
    // ========================
    private fun collectArticle(event: WechatContract.Event.CollectArticle) {
        requestFlowMvi(
            block = { repo.collectArticle(event.articleId) },
            onEach = {
                setEffect {
                    WechatContract.Effect.UpdateCollect(
                        event.position,
                        true
                    )
                }
            },
            onError = {
                setEffect {
                    WechatContract.Effect.ShowToast("收藏失败")
                }
            }
        )
    }

    // ========================
    // 取消收藏
    // ========================
    private fun unCollectArticle(event: WechatContract.Event.UnCollectArticle) {
        requestFlowMvi(
            block = { repo.unCollectArticle(event.articleId) },
            onEach = {
                setEffect {
                    WechatContract.Effect.UpdateCollect(
                        event.position,
                        false
                    )
                }
            },
            onError = {
                setEffect {
                    WechatContract.Effect.ShowToast("取消收藏失败")
                }
            }
        )
    }
}