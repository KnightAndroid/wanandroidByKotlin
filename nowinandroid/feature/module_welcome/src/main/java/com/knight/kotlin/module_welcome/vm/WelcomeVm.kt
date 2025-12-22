package com.knight.kotlin.module_welcome.vm

import com.core.library_base.ktx.appStr
import com.core.library_base.vm.BaseMviViewModel
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.module_welcome.R
import com.knight.kotlin.module_welcome.contract.WelcomeContract
import com.knight.kotlin.module_welcome.repo.WelcomeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author created by luguian
 * @organize
 * @Date 2025/12/22 9:35
 * @descript:
 */
@HiltViewModel
class WelcomeVm @Inject constructor(
    private val repo: WelcomeRepo
) : BaseMviViewModel<
        WelcomeContract.Event,
        WelcomeContract.State,
        WelcomeContract.Effect>() {

    override fun initialState() = WelcomeContract.State()

    override fun handleEvent(event: WelcomeContract.Event) {
        when (event) {

            WelcomeContract.Event.LoadAppTheme -> {
                loadAppTheme()
            }

            WelcomeContract.Event.LogoAnimFinished -> {
                checkPrivacyAndJump()
            }
        }
    }

    private fun loadAppTheme() {
        requestFlowMvi(
            block = { repo.getAppTheme() },
            onEach = { theme ->
                setState {
                    copy(
                        theme = theme
                    )
                }
            },
            onError = { e ->
                setEffect {
                    WelcomeContract.Effect.ShowError(
                        e.message ?: appStr(R.string.welcome_error_get_theme_failed)
                    )
                }
            }
        )
    }

    private fun checkPrivacyAndJump() {
        if (CacheUtils.getAgreeStatus()) {
            setEffect { WelcomeContract.Effect.GoMain }
        } else {
            setEffect { WelcomeContract.Effect.ShowPrivacyDialog }
        }
    }
}