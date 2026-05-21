package com.knight.kotlin.module_navigate.vm

import com.core.library_base.vm.BaseMviViewModel
import com.knight.kotlin.module_navigate.contract.NavigateContract
import com.knight.kotlin.module_navigate.repo.NavigateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/5 15:09
 * Description:NavigateVm
 */
@HiltViewModel
class NavigateVm @Inject constructor(
    private val mRepo: NavigateRepository
) : BaseMviViewModel<
        NavigateContract.Event,
        NavigateContract.State,
        NavigateContract.Effect>() {

    override fun initialState(): NavigateContract.State {
        return NavigateContract.State()
    }

    override fun handleEvent(event: NavigateContract.Event) {

        when (event) {

            is NavigateContract.Event.LoadData -> {
                getNavigateData()
            }

            is NavigateContract.Event.SelectLeftTab -> {

                setState {
                    copy(
                        currentPosition = event.position
                    )
                }
            }
        }
    }

    /**
     * 获取导航数据
     */
    private fun getNavigateData() {

        requestFlowMvi(

            block = {
                mRepo.getNavigateData()
            },

            onStart = {

                setState {
                    copy(
                        isLoading = true
                    )
                }
            },

            onEach = { data ->

                setState {
                    copy(
                        isLoading = false,
                        list = data
                    )
                }
            },

            onError = {

                setState {
                    copy(
                        isLoading = false
                    )
                }

                setEffect {
                    NavigateContract.Effect.ShowToast(
                        it.message ?: "加载失败"
                    )
                }
            }
        )
    }
}