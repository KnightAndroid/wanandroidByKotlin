package com.knight.kotlin.module_realtime.vm

import com.core.library_base.vm.BaseMviViewModel
import com.knight.kotlin.module_realtime.contract.RealTimeHomeContract
import com.knight.kotlin.module_realtime.repo.RealTimeBaseRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @Description 热搜vm
 * @Author knight
 * @Time 2024/9/28 17:28
 *
 */
@HiltViewModel
class RealTimeHomeVm @Inject constructor(
    private val repo: RealTimeBaseRepo
) : BaseMviViewModel<
        RealTimeHomeContract.Event,
        RealTimeHomeContract.State,
        RealTimeHomeContract.Effect>() {

    override fun initialState() =
        RealTimeHomeContract.State(isLoading = true)

    override fun handleEvent(event: RealTimeHomeContract.Event) {
        when (event) {
            RealTimeHomeContract.Event.Init -> loadData()
        }
    }

    private fun loadData() {
        requestFlowMvi(
            block = {
                repo.getDataByTab("pc", "homepage")
            },
            onStart = {
                setState { copy(isLoading = true, isError = false) }
            },
            onEach = {
                setState {
                    copy(
                        isLoading = false,
                        tabList = it.tabBoard
                    )
                }
            },
            onError = {
                setState { copy(isLoading = false, isError = true) }
            }
        )
    }
}