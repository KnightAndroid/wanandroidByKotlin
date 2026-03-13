package com.knight.kotlin.module_square.vm

import com.core.library_base.vm.BaseMviViewModel
import com.knight.kotlin.module_square.contract.SquareShareArticleContract
import com.knight.kotlin.module_square.repo.SquareShareListRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/4/8 17:53
 * Description:SquareShareArticleVm
 */
@HiltViewModel
class SquareShareArticleVm @Inject constructor(
    private val repo: SquareShareListRepo
) : BaseMviViewModel<
        SquareShareArticleContract.Event,
        SquareShareArticleContract.State,
        SquareShareArticleContract.Effect>() {

    /**
     * 初始化 State
     */
    override fun initialState(): SquareShareArticleContract.State {
        return SquareShareArticleContract.State()
    }

    /**
     * 处理 Event
     */
    override fun handleEvent(event: SquareShareArticleContract.Event) {

        when (event) {

            is SquareShareArticleContract.Event.ShareArticle -> {
                shareArticle(event.title, event.link)
            }
        }
    }

    /**
     * 分享文章
     */
    private fun shareArticle(title: String, link: String) {

        requestFlowMvi(

            block = { repo.shareArticle(title, link) },

            onStart = {
                setState { copy(loading = true) }
            },

            onEach = {

                setEffect {
                    SquareShareArticleContract.Effect.ShareSuccess
                }
            },

            onError = {

                setEffect {
                    SquareShareArticleContract.Effect.ShowError(
                        it.message ?: "分享失败"
                    )
                }
            },

            onCompletion = {
                setState { copy(loading = false) }
            }
        )
    }
}