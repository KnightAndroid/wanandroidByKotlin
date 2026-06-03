package com.knight.kotlin.module_mine.vm

import com.core.library_base.vm.BaseMviViewModel
import com.knight.kotlin.module_mine.contract.MyDetailCoinsContract
import com.knight.kotlin.module_mine.repo.MyDetailCoinRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/11 10:53
 * Description:MyDetailCoinsViewModel
 */
@HiltViewModel
class MyDetailCoinsViewModel @Inject constructor(
    private val mRepo: MyDetailCoinRepo
) : BaseMviViewModel<
        MyDetailCoinsContract.Event,
        MyDetailCoinsContract.State,
        MyDetailCoinsContract.Effect>() {

    override fun initialState() =
        MyDetailCoinsContract.State()

    override fun handleEvent(
        event: MyDetailCoinsContract.Event
    ) {

        when (event) {

            MyDetailCoinsContract.Event.LoadData -> {
                requestCoinList(1)
            }

            MyDetailCoinsContract.Event.Refresh -> {
                requestCoinList(1)
            }

            MyDetailCoinsContract.Event.LoadMore -> {
                requestCoinList(currentState.page)
            }
        }
    }

    private fun requestCoinList(
        page: Int
    ) {

        requestFlowMvi(

            block = {
                mRepo.getMyDetailCoin(page)
            },

            onStart = {

                if (page == 1) {

                    setState {
                        copy(
                            isLoading = true
                        )
                    }
                }
            },

            onEach = { result ->

                val noMoreData =
                    result.datas.isEmpty()

                setState {

                    copy(
                        isLoading = false,
                        page = if (noMoreData) {
                            page
                        } else {
                            page + 1
                        },
                        coinData = result,
                        noMoreData = noMoreData
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
                    MyDetailCoinsContract.Effect.ShowToast(
                        it.message ?: "加载失败"
                    )
                }
            }
        )
    }
}