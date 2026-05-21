package com.knight.kotlin.module_navigate.vm

import com.core.library_base.vm.BaseMviViewModel
import com.knight.kotlin.module_navigate.contract.NavigateRightTreeContract
import com.knight.kotlin.module_navigate.repo.NavigateTreeRightRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/5 15:47
 * Description:NavigateRightTreeVm
 */
@HiltViewModel
class NavigateRightTreeVm @Inject constructor(
    private val mRepo: NavigateTreeRightRepository
) : BaseMviViewModel<
        NavigateRightTreeContract.Event,
        NavigateRightTreeContract.State,
        NavigateRightTreeContract.Effect>() {

    override fun initialState(): NavigateRightTreeContract.State {
        return NavigateRightTreeContract.State()
    }

    override fun handleEvent(
        event: NavigateRightTreeContract.Event
    ) {

        when (event) {

            is NavigateRightTreeContract.Event.LoadData -> {

                if (event.isNavigate) {

                    getTreeNavigateData()

                } else {

                    getTreeHierachyData()
                }
            }
        }
    }

    /**
     * 获取导航数据
     */
    private fun getTreeNavigateData() {

        requestFlowMvi(

            block = {
                mRepo.getTreeNavigateData()
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
                        navigateList = data
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
                    NavigateRightTreeContract.Effect.ShowToast(
                        it.message ?: "加载失败"
                    )
                }
            }
        )
    }

    /**
     * 获取体系数据
     */
    private fun getTreeHierachyData() {

        requestFlowMvi(

            block = {
                mRepo.getTreeHierachyData()
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
                        hierachyList = data
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
                    NavigateRightTreeContract.Effect.ShowToast(
                        it.message ?: "加载失败"
                    )
                }
            }
        )
    }
}