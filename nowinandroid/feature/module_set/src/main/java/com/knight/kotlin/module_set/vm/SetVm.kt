package com.knight.kotlin.module_set.vm

import com.core.library_base.vm.BaseMviViewModel
import com.knight.kotlin.library_common.ktx.getUser
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.module_set.contract.SetContract
import com.knight.kotlin.module_set.repo.SetRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/3/30 15:35
 * Description:SetVm
 */
@HiltViewModel
class SetVm @Inject constructor(
    private val repo: SetRepo
) : BaseMviViewModel<
        SetContract.Event,
        SetContract.State,
        SetContract.Effect>() {

    override fun initialState() = SetContract.State(
        isLogin = checkLogin(),
        cacheSize = ""
    )

    init {
        loadCacheSize()
    }

    private fun loadCacheSize() {
        val size = repo.getCacheSize()
        setState { copy(cacheSize = size) }
    }

    override fun handleEvent(event: SetContract.Event) {
        when (event) {
            SetContract.Event.Logout -> logout()
            SetContract.Event.ClearCache -> clearCache()
            is SetContract.Event.ChangeTheme -> changeTheme(event.color)
        }
    }

    /**
     * ================= 退出登录 =================
     */
    private fun logout() {
        requestFlowMvi(
            block = { repo.logout() },

            onStart = {
                setState { copy(isLoading = true) }
            },

            onEach = {
                setEffect { SetContract.Effect.LogoutSuccess }
                setState {
                    copy(
                        isLoading = false,
                        isLogin = false
                    )
                }
            },

            onError = {
                setState { copy(isLoading = false) }
                setEffect {
                    SetContract.Effect.ShowError(it.message ?: "退出失败")
                }
            },

            onCompletion = {
                setState { copy(isLoading = false) }
            }
        )
    }

    /**
     * ================= 清缓存 =================
     */
    private fun clearCache() {

        // ✅ 交给 Repo（核心）
        repo.clearCache()

        val newSize = repo.getCacheSize()

        setState {
            copy(cacheSize = newSize)
        }

        setEffect { SetContract.Effect.CacheCleared }
    }

    /**
     * ================= 修改主题 =================
     */
    private fun changeTheme(color: Int) {
        CacheUtils.setThemeColor(color)
        setEffect { SetContract.Effect.ThemeChanged }
    }

    /**
     * ================= 工具 =================
     */

    private fun checkLogin(): Boolean {
        return getUser() != null
    }
}