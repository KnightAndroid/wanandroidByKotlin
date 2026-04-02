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
                loadData()
            }
        }
    }

    private fun loadData() {
        requestFlowMvi(
            block = { mRepo.checkAppUpdateRecord() },

            onStart = {
                setState { copy(isLoading = true) }
            },

            onEach = { data ->
                setState {
                    copy(
                        isLoading = false,
                        listData = data
                    )
                }
            },

            onError = {
                setState { copy(isLoading = false) }
                setEffect {
                    AppUpdateRecordContract.Effect.ShowError(
                        it.message ?: "加载失败"
                    )
                }
            },

            onCompletion = {
                setState { copy(isLoading = false) }
            }
        )
    }
}