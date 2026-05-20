package com.knight.kotlin.module_navigate.vm

import com.core.library_base.vm.BaseMviViewModel
import com.knight.kotlin.module_navigate.contract.HierachyContract
import com.knight.kotlin.module_navigate.repo.HierachyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/6 14:01
 * Description:HierachyVm
 */
@HiltViewModel
class HierachyVm @Inject constructor(
    private val mRepo: HierachyRepository
) : BaseMviViewModel<
        HierachyContract.Event,
        HierachyContract.State,
        HierachyContract.Effect>() {

    override fun initialState(): HierachyContract.State {
        return HierachyContract.State()
    }

    override fun handleEvent(event: HierachyContract.Event) {

        when (event) {

            HierachyContract.Event.LoadData -> {
                loadData()
            }

            is HierachyContract.Event.SelectLeftTab -> {

                setState {
                    copy(
                        currentPosition = event.position
                    )
                }
            }
        }
    }

    /**
     * 加载数据
     */
    private fun loadData() {

        requestFlowMvi(

            block = {
                mRepo.getHierachyData()
            },

            onStart = {

                setState {
                    copy(
                        isLoading = true
                    )
                }
            },

            onEach = { list ->

                setState {
                    copy(
                        isLoading = false,
                        list = list
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
                    HierachyContract.Effect.ShowToast(
                        it.message ?: "加载失败"
                    )
                }
            }
        )
    }
}