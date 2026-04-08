package com.knight.kotlin.module_realtime.vm

import com.core.library_base.vm.BaseMviViewModel
import com.knight.kotlin.module_realtime.contract.RealtimeTextContract
import com.knight.kotlin.module_realtime.enum.HotListEnum
import com.knight.kotlin.module_realtime.repo.RealTimeBaseRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/19 16:40
 * @descript:
 */
@HiltViewModel
class RealTimeTextVm @Inject constructor(
    private val repo: RealTimeBaseRepo
) : BaseMviViewModel<
        RealtimeTextContract.Event,
        RealtimeTextContract.State,
        RealtimeTextContract.Effect>() {

    private var typeName: String = ""

    override fun initialState(): RealtimeTextContract.State {
        return RealtimeTextContract.State(isLoading = true)
    }

    override fun handleEvent(event: RealtimeTextContract.Event) {
        when (event) {
            is RealtimeTextContract.Event.Init -> {
                typeName = event.typeName
                requestData()
            }
        }
    }

    private fun requestData() {
        requestFlowMvi(
            block = {
                repo.getDataByTab("pc", typeName.lowercase())
            },

            onStart = {
                setState { copy(isLoading = true) }
            },

            onEach = { data ->

                val content = data.cards.getOrNull(0)?.content.orEmpty()
                val top = data.cards.getOrNull(0)?.topContent.orEmpty()

                // ✅ 不修改原数据，生成新 list
                val finalList = if (typeName == HotListEnum.REALTIME.name) {
                    top + content
                } else {
                    content
                }

                setState {
                    copy(
                        isLoading = false,
                        list = finalList,
                        isEmpty = finalList.isEmpty()
                    )
                }
            },

            onError = {
                setState { copy(isLoading = false) }
            }
        )
    }
}