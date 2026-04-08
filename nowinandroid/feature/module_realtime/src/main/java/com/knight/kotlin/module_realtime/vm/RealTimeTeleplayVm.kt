package com.knight.kotlin.module_realtime.vm

import com.core.library_base.vm.BaseMviViewModel
import com.knight.kotlin.module_realtime.contract.RealtimeTeleplayContract
import com.knight.kotlin.module_realtime.repo.RealTimeBaseRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/25 9:50
 * @descript:电视榜vm
 */
@HiltViewModel
class RealTimeTeleplayVm @Inject constructor(
    private val repo: RealTimeBaseRepo
) : BaseMviViewModel<
        RealtimeTeleplayContract.Event,
        RealtimeTeleplayContract.State,
        RealtimeTeleplayContract.Effect>() {

    private var category = "全部类型"
    private var country = "中国大陆"

    override fun initialState(): RealtimeTeleplayContract.State {
        return RealtimeTeleplayContract.State(isLoading = true)
    }

    override fun handleEvent(event: RealtimeTeleplayContract.Event) {
        when (event) {
            is RealtimeTeleplayContract.Event.Init -> {
                requestData(true)
            }

            is RealtimeTeleplayContract.Event.FilterChanged -> {
                category = event.category
                country = event.country
                requestData(false)
            }
        }
    }

    private fun requestData(isFirst: Boolean) {
        requestFlowMvi(
            block = {
                repo.getChildDataByTab(
                    "pc",
                    "teleplay",
                    """{"category":"$category","country":"$country"}"""
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
                        categoryList = if (isFirst)
                            data.tag.getOrNull(0)?.content.orEmpty()
                        else categoryList,

                        countryList = if (isFirst)
                            data.tag.getOrNull(1)?.content.orEmpty()
                        else countryList,

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
