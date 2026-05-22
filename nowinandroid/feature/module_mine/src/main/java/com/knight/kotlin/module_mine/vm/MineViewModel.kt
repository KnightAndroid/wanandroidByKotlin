package com.knight.kotlin.module_mine.vm

import com.core.library_base.vm.BaseMviViewModel
import com.knight.kotlin.module_mine.contract.MineContract
import com.knight.kotlin.module_mine.repo.MineRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/1/18 17:40
 * Description:MineViewModel
 */
@HiltViewModel
class MineViewModel @Inject constructor(
    private val mRepo: MineRepo
) : BaseMviViewModel<
        MineContract.Event,
        MineContract.State,
        MineContract.Effect>() {

    override fun initialState(): MineContract.State {
        return MineContract.State()
    }

    override fun handleEvent(event: MineContract.Event) {
        when (event) {

            MineContract.Event.Init -> {
                getUserInfoCoin()
            }

            MineContract.Event.RefreshUserInfo -> {
                getUserInfoCoin()
            }

            is MineContract.Event.Login -> {
                login(event.userName, event.passWord)
            }

            MineContract.Event.Logout -> {
                setState {
                    copy(
                        isLogin = false,
                        userInfo = null,
                        userCoinInfo = null
                    )
                }
            }
        }
    }

    /**
     * 获取用户金币
     */
    private fun getUserInfoCoin() {

        requestFlowMvi(
            block = {
                mRepo.getUserInfoCoin()
            },
            onStart = {
                setState {
                    copy(isLoading = true)
                }
            },
            onEach = { data ->

                setState {
                    copy(
                        isLoading = false,
                        userCoinInfo = data,
                        isLogin = true

                    )
                }
            },
            onError = {

                setState {
                    copy(
                        isLoading = false
                    )
                }
            },
            onCompletion = {
                setState {
                    copy(isLoading = false)
                }
            }
        )
    }

    /**
     * 登录
     */
    private fun login(userName: String, passWord: String) {

        requestFlowMvi(
            block = {
                mRepo.login(userName, passWord)
            },
            onStart = {
                setState {
                    copy(isLoading = true)
                }
            },
            onEach = { userInfo ->

                setState {
                    copy(
                        isLoading = false,
                        userInfo = userInfo,
                        isLogin = true
                    )
                }

                setEffect {
                    MineContract.Effect.LoginSuccess
                }
            },
            onError = {

                setState {
                    copy(isLoading = false)
                }

                setEffect {
                    MineContract.Effect.LoginFailure(it.message)
                }
            },
            onCompletion = {
                setState {
                    copy(isLoading = false)
                }
            }
        )
    }
}