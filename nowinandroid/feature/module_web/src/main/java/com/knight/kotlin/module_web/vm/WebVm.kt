package com.knight.kotlin.module_web.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.core.library_base.vm.BaseMviViewModel
import com.core.library_base.vm.BaseViewModel
import com.knight.kotlin.module_web.contact.WebContract

import com.knight.kotlin.module_web.repo.WebRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/2/22 11:26
 * Description:WebVm
 */
@HiltViewModel
class WebVm @Inject constructor(
    private val repo: WebRepo
) : BaseMviViewModel<
        WebContract.Event,
        WebContract.State,
        WebContract.Effect>() {

    override fun initialState() = WebContract.State()

    override fun handleEvent(event: WebContract.Event) {
        when (event) {
            is WebContract.Event.CollectArticle ->
                collectArticle(event)
        }
    }

    // ========================
    // 收藏文章
    // ========================
    private fun collectArticle(event: WebContract.Event.CollectArticle) {
        requestFlowMvi(
            block = {
                repo.collectArticle(event.articleId)
            },
            onEach = {
                setEffect {
                    WebContract.Effect.CollectSuccess
                }
            },
            onError = { e->
                setEffect {
                    WebContract.Effect.ShowToast(e.message ?: "收藏失败")
                }
            }
        )
    }
}