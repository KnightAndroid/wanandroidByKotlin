package com.knight.kotlin.module_realtime.vm

import com.core.library_base.vm.BaseMviViewModel
import com.knight.kotlin.module_realtime.contract.RealtimeNovelContract
import com.knight.kotlin.module_realtime.repo.RealTimeBaseRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/20 11:08
 * @descript:小说vm
 */
@HiltViewModel
class RealTimeNovelVm @Inject constructor(
    private val repo: RealTimeBaseRepo
) : BaseMviViewModel<
        RealtimeNovelContract.Event,
        RealtimeNovelContract.State,
        RealtimeNovelContract.Effect>() {

    override fun initialState(): RealtimeNovelContract.State {
        return RealtimeNovelContract.State(isLoading = true)
    }

    override fun handleEvent(event: RealtimeNovelContract.Event) {
        when (event) {
            RealtimeNovelContract.Event.Init -> requestData()
        }
    }

    private fun requestData() {
        requestFlowMvi(
            block = { repo.getDataByTab("pc", "novel") },

            onStart = {
                setState { copy(isLoading = true) }
            },

            onEach = { data ->
                val tabs = data.tag.getOrNull(0)?.content.orEmpty()

                setState {
                    copy(
                        isLoading = false,
                        tabs = tabs
                    )
                }
            },

            onError = {
                setState { copy(isLoading = false) }
            }
        )
    }
}