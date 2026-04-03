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
    private val mRepo: SetRepo
) : BaseMviViewModel<SetContract.Event, SetContract.State, SetContract.Effect>() {

    //===================== 初始化 =====================//

    override fun initialState(): SetContract.State {
        return SetContract.State(
            isLogin = getUser() != null,
            cacheSize = "", // ⭐不在VM计算，由UI传入
            themeColor = CacheUtils.getThemeColor(),
            isEyeCare = CacheUtils.getIsEyeCare(),
            isStatusWithTheme = CacheUtils.getStatusBarIsWithTheme()
        )
    }

    //===================== 事件处理 =====================//

    override fun handleEvent(event: SetContract.Event) {
        when (event) {

            is SetContract.Event.Init -> {
                syncStateFromCache()
            }

            is SetContract.Event.Refresh -> {
                syncStateFromCache()
            }

            is SetContract.Event.Logout -> {
                logout()
            }

            is SetContract.Event.ChangeEyeCare -> {
                updateEyeCare(event.enable)
            }

            is SetContract.Event.ChangeStatusTheme -> {
                updateStatusTheme(event.enable)
            }

            is SetContract.Event.ChangeThemeColor -> {
                updateThemeColor(event.color)
            }

            is SetContract.Event.UpdateCacheSize -> {
                updateCacheSize(event.size)
            }
        }
    }

    //===================== 状态同步 =====================//

    /**
     * ⭐统一从缓存同步（唯一可信源）
     */
    private fun syncStateFromCache() {
        setState {
            copy(
                isLogin = getUser() != null,
                themeColor = CacheUtils.getThemeColor(),
                isEyeCare = CacheUtils.getIsEyeCare(),
                isStatusWithTheme = CacheUtils.getStatusBarIsWithTheme()
            )
        }
    }

    //===================== 状态更新封装 =====================//

    private fun updateThemeColor(color: Int) {
        CacheUtils.setThemeColor(color)
        setState { copy(themeColor = color) }
    }

    private fun updateEyeCare(enable: Boolean) {
        CacheUtils.setIsEyeCare(enable)
        setState { copy(isEyeCare = enable) }
    }

    private fun updateStatusTheme(enable: Boolean) {
        CacheUtils.statusBarIsWithTheme(enable)
        setState { copy(isStatusWithTheme = enable) }
    }

    private fun updateCacheSize(size: String) {
        setState { copy(cacheSize = size) }
    }

    //===================== 业务逻辑 =====================//

    /**
     * 退出登录（MVI）
     */
    private fun logout() {
        requestMvi(
            block = { mRepo.logout() },
            onStart = {
                setEffect { SetContract.Effect.ShowLoading }
            },
            onSuccess = {
                setEffect { SetContract.Effect.HideLoading }
                setEffect { SetContract.Effect.LogoutSuccess }

                setState {
                    copy(isLogin = false)
                }
            },
            onError = {
                setEffect { SetContract.Effect.HideLoading }
                setEffect {
                    SetContract.Effect.ShowToast(it.message ?: "error")
                }
            }
        )
    }
}