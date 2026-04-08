package com.knight.kotlin.module_realtime.vm

import com.core.library_base.vm.BaseMviViewModel
import com.knight.kotlin.module_realtime.contract.RealTimeMainContract
import com.knight.kotlin.module_realtime.repo.RealTimeBaseRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author created by luguian
 * @organize
 * @Date 2026/4/8 17:06
 * @descript:热搜MainVm
 */
@HiltViewModel
class RealTimeMainVm @Inject constructor(
    private val repo: RealTimeBaseRepo
) : BaseMviViewModel<
        RealTimeMainContract.Event,
        RealTimeMainContract.State,
        RealTimeMainContract.Effect>() {

    override fun initialState() =
        RealTimeMainContract.State(isLoading = true)

    override fun handleEvent(event: RealTimeMainContract.Event) {
        when (event) {
            RealTimeMainContract.Event.Init -> loadData()
        }
    }

    private fun loadData() {
        requestFlowMvi(
            block = { repo.getDataByTab("pc", "homepage") },
            onStart = {
                setState { copy(isLoading = true, isError = false) }
            },
            onEach = { data ->

                val hot = data.cards.getOrNull(0)?.apply {
                    content.addAll(0, topContent)
                }

                setState {
                    copy(
                        isLoading = false,
                        hotList = hot?.content ?: emptyList(),
                        novelList = data.cards.getOrNull(1)?.content ?: emptyList(),
                        movieList = data.cards.getOrNull(2)?.content ?: emptyList()
                    )
                }
            },
            onError = {
                setState { copy(isLoading = false, isError = true) }
            }
        )
    }
}