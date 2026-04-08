package com.knight.kotlin.module_realtime.vm

import com.core.library_base.vm.BaseMviViewModel
import com.knight.kotlin.module_realtime.contract.RealtimeChildNovelContract
import com.knight.kotlin.module_realtime.repo.RealTimeBaseRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2026/4/8 17:41
 * @descript:小说二级契约
 */
@HiltViewModel
class RealTimeChildNovelVm @Inject constructor(
    private val repo: RealTimeBaseRepo
) : BaseMviViewModel<
        RealtimeChildNovelContract.Event,
        RealtimeChildNovelContract.State,
        RealtimeChildNovelContract.Effect>() {

    private var category: String = ""

    override fun initialState(): RealtimeChildNovelContract.State {
        return RealtimeChildNovelContract.State(isLoading = true)
    }

    override fun handleEvent(event: RealtimeChildNovelContract.Event) {
        when (event) {
            is RealtimeChildNovelContract.Event.Init -> {
                category = event.category
                requestData()
            }
        }
    }

    private fun requestData() {
        requestFlowMvi(
            block = {
                repo.getChildDataByTab(
                    "pc",
                    "novel",
                    """{"category":"$category"}"""
                )
            },

            onStart = {
                setState { copy(isLoading = true) }
            },

            onEach = { data ->
                val list = data.cards.getOrNull(0)?.content.orEmpty()

                setState {
                    copy(
                        isLoading = false,
                        list = list,
                        isEmpty = list.isEmpty()
                    )
                }
            },

            onError = {
                setState { copy(isLoading = false) }
            }
        )
    }
}