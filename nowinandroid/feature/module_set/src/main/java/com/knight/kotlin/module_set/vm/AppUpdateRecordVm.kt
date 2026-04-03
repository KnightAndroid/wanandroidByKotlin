package com.knight.kotlin.module_set.vm

import com.core.library_base.vm.BaseMviViewModel
import com.knight.kotlin.module_set.contract.AppUpdateRecordContract
import com.knight.kotlin.module_set.repo.AppUpdateRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/8/26 11:13
 * Description:AppUpdateRecordVm
 */
@HiltViewModel
class AppUpdateRecordVm @Inject constructor(
    private val mRepo: AppUpdateRepo
) : BaseMviViewModel<
        AppUpdateRecordContract.Event,
        AppUpdateRecordContract.State,
        AppUpdateRecordContract.Effect>() {

    override fun initialState() = AppUpdateRecordContract.State()

    override fun handleEvent(event: AppUpdateRecordContract.Event) {
        when (event) {

            AppUpdateRecordContract.Event.LoadData -> {
                loadData(isRefresh = false)
            }

            AppUpdateRecordContract.Event.Refresh -> {
                loadData(isRefresh = true)
            }
        }
    }

    /**
     * 加载数据（统一入口）
     */
    private fun loadData(isRefresh: Boolean) {

        requestFlowMvi(
            block = { mRepo.checkAppUpdateRecord() },

            onStart = {
                setState {
                    copy(
                        isLoading = !isRefresh,
                        isRefreshing = isRefresh
                    )
                }
            },

            onEach = { data ->

                val list = data.datas ?: emptyList()

                setState {
                    copy(
                        isLoading = false,
                        isRefreshing = false,
                        list = list,
                        isEmpty = list.isEmpty()
                    )
                }

                // ✅ 停止刷新（一次性行为）
                if (isRefresh) {
                    setEffect {
                        AppUpdateRecordContract.Effect.StopRefresh
                    }
                }
            },

            onError = {

                setState {
                    copy(
                        isLoading = false,
                        isRefreshing = false
                    )
                }

                // ✅ 停止刷新
                if (isRefresh) {
                    setEffect {
                        AppUpdateRecordContract.Effect.StopRefresh
                    }
                }

                setEffect {
                    AppUpdateRecordContract.Effect.ShowError(
                        it.message ?: "加载失败"
                    )
                }
            }
        )
    }
}