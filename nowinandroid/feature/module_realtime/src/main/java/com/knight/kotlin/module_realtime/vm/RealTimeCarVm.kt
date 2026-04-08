package com.knight.kotlin.module_realtime.vm

import com.core.library_base.vm.BaseMviViewModel
import com.knight.kotlin.module_realtime.contract.RealTimeCarContract
import com.knight.kotlin.module_realtime.repo.RealTimeBaseRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/25 14:43
 * @descript:排行榜CarVm
 */
@HiltViewModel
class RealTimeCarVm @Inject constructor(
    private val repo: RealTimeBaseRepo
) : BaseMviViewModel<
        RealTimeCarContract.Event,
        RealTimeCarContract.State,
        RealTimeCarContract.Effect>() {

    override fun initialState() = RealTimeCarContract.State()

    override fun handleEvent(event: RealTimeCarContract.Event) {
        when (event) {
            is RealTimeCarContract.Event.LoadData -> {
                loadData(event.category, true)
            }

            is RealTimeCarContract.Event.ChangeCategory -> {
                loadData(event.category, false)
            }
        }
    }

    private fun loadData(category: String, needCategory: Boolean) {

        setState { copy(isLoading = true) }

        requestFlowMvi(
            block = {
                repo.getChildDataByTab(
                    "pc",
                    "car",
                    "{\"category\":\"$category\"}"
                )
            },
            onEach = { data ->

                val categoryList =
                    if (needCategory) data.tag[0].content else currentState.categoryList

                val carList = data.cards[0].content

                setState {
                    copy(
                        isLoading = false,
                        categoryList = categoryList,
                        carList = carList,
                        isEmpty = carList.isEmpty()
                    )
                }
            },
            onError = {
                // 👉 这里可以加 Effect（推荐）
                setState { copy(isLoading = false) }
            }
        )
    }
}