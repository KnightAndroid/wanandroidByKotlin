package com.knight.kotlin.module_realtime.vm


import com.core.library_base.vm.BaseMviViewModel
import com.knight.kotlin.module_realtime.contract.RealTimeGameContract
import com.knight.kotlin.module_realtime.repo.RealTimeBaseRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/25 16:46
 * @descript:游戏vm
 */
@HiltViewModel
class RealTimeGameVm @Inject constructor(
    private val repo: RealTimeBaseRepo
) : BaseMviViewModel<
        RealTimeGameContract.Event,
        RealTimeGameContract.State,
        RealTimeGameContract.Effect>() {

    companion object {
        private const val PLATFORM = "pc"
        private const val TYPE = "game"
    }

    override fun initialState(): RealTimeGameContract.State {
        return RealTimeGameContract.State(
            isLoading = true
        )
    }

    override fun handleEvent(event: RealTimeGameContract.Event) {
        when (event) {

            is RealTimeGameContract.Event.Init -> {
                loadData(currentState.currentCategory)
            }

            is RealTimeGameContract.Event.SelectCategory -> {
                if (event.category == currentState.currentCategory) return

                // ✅ 更新当前分类（先更新状态）
                setState {
                    copy(currentCategory = event.category)
                }

                loadData(event.category)
            }

            is RealTimeGameContract.Event.Retry -> {
                loadData(currentState.currentCategory)
            }
        }
    }

    /**
     * 核心请求（统一入口）
     */
    private fun loadData(category: String) {

        requestFlowMvi(
            block = {
                repo.getChildDataByTab(
                    PLATFORM,
                    TYPE,
                    buildCategoryParam(category)
                )
            },

            onStart = {
                setState {
                    copy(
                        isLoading = true,
                        isError = false
                    )
                }
            },

            onEach = { data ->

                val categoryList = data.tag.firstOrNull()?.content.orEmpty()
                val gameList = data.cards.firstOrNull()?.content.orEmpty()

                setState {
                    copy(
                        isLoading = false,
                        isError = false,
                        categoryList = categoryList,
                        gameList = gameList,
                        isEmpty = gameList.isEmpty()
                    )
                }
            },

            onError = {
                setState {
                    copy(
                        isLoading = false,
                        isError = true
                    )
                }

                setEffect {
                    RealTimeGameContract.Effect.ShowToast("加载失败")
                }
            }
        )
    }

    /**
     * 参数构建（避免字符串拼接污染主逻辑）
     */
    private fun buildCategoryParam(category: String): String {
        return """{"category":"$category"}"""
    }
}