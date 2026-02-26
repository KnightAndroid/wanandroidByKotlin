package com.knight.kotlin.module_utils.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.core.library_base.ktx.appStr
import com.core.library_base.vm.BaseMviViewModel
import com.core.library_base.vm.BaseViewModel
import com.knight.kotlin.module_utils.R
import com.knight.kotlin.module_utils.contract.UtilsContract
import com.knight.kotlin.module_utils.entity.UtilsEntity
import com.knight.kotlin.module_utils.repo.UtilsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/6/2 14:56
 * Description:UtilsVm
 */
@HiltViewModel
class UtilsVm @Inject constructor(
    private val repo: UtilsRepo
) : BaseMviViewModel<
        UtilsContract.Event,
        UtilsContract.State,
        UtilsContract.Effect>() {

    override fun initialState() = UtilsContract.State()

    override fun handleEvent(event: UtilsContract.Event) {
        when (event) {
            UtilsContract.Event.LoadUtils -> loadUtils()
        }
    }

    /**
     * 加载工具列表
     */
    private fun loadUtils() {
        requestFlowMvi(
            block = { repo.getUtils() },

            onStart = {
                setState { copy(isLoading = true) }
            },

            onEach = { list ->
                setState {
                    copy(
                        isLoading = false,
                        utils = list
                    )
                }
            },

            onError = { e ->
                setState { copy(isLoading = false) }

                setEffect {
                    UtilsContract.Effect.ShowError(
                        e.message ?: appStr(R.string.utils_error_load_failed)
                    )
                }
            }
        )
    }
}