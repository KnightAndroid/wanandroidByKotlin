package com.knight.kotlin.module_set.vm


import com.core.library_base.vm.BaseMviViewModel
import com.knight.kotlin.module_set.contract.AboutContract
import com.knight.kotlin.module_set.repo.AboutRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/8/25 15:03
 * Description:AboutVm
 */
@HiltViewModel
class AboutVm @Inject constructor(
    private val mRepo: AboutRepo
) : BaseMviViewModel<
        AboutContract.Event,
        AboutContract.State,
        AboutContract.Effect>() {

    override fun initialState() = AboutContract.State()

    override fun handleEvent(event: AboutContract.Event) {
        when (event) {
            AboutContract.Event.CheckAppUpdate -> {
                checkAppUpdate()
            }
        }
    }

    private fun checkAppUpdate() {
        requestFlowMvi(
            block = { mRepo.checkAppUpdateMessage() },

            onStart = {
                setState { copy(isLoading = true) }
            },

            onEach = { data ->
                setState {
                    copy(
                        isLoading = false,
                        updateBean = data
                    )
                }
            },

            onError = {
                setState { copy(isLoading = false) }
                setEffect {
                    AboutContract.Effect.ShowError("请求失败")
                }
            },

            onCompletion = {
                setState { copy(isLoading = false) }
            }
        )
    }
}