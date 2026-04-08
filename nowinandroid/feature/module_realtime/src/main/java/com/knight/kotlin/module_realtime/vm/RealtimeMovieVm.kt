package com.knight.kotlin.module_realtime.vm

import com.core.library_base.vm.BaseMviViewModel
import com.knight.kotlin.module_realtime.contract.RealtimeMovieContract
import com.knight.kotlin.module_realtime.repo.RealTimeBaseRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/24 11:03
 * @descript:热搜电影vm
 */
@HiltViewModel
class RealtimeMovieVm @Inject constructor(
    private val repo: RealTimeBaseRepo
) : BaseMviViewModel<
        RealtimeMovieContract.Event,
        RealtimeMovieContract.State,
        RealtimeMovieContract.Effect>() {

    private var category = "全部类型"
    private var country = "全部地区"

    // ✅ 这里改成 initialState（适配你的基类）
    override fun initialState(): RealtimeMovieContract.State {
        return RealtimeMovieContract.State(
            isLoading = true
        )
    }

    override fun handleEvent(event: RealtimeMovieContract.Event) {
        when (event) {

            is RealtimeMovieContract.Event.Init -> {
                requestData(isFirst = true)
            }

            is RealtimeMovieContract.Event.FilterChanged -> {
                category = event.category
                country = event.country
                requestData(isFirst = false)
            }
        }
    }

    // =========================
    // 核心请求（🔥用你的 requestFlowMvi）
    // =========================
    private fun requestData(isFirst: Boolean) {

        requestFlowMvi(
            block = {
                repo.getChildDataByTab(
                    "pc",
                    "movie",
                    """{"category":"$category","country":"$country"}"""
                )
            },

            onStart = {
                setState {
                    copy(isLoading = true)
                }
            },

            onEach = { data ->

                val movieList = data.cards.getOrNull(0)?.content.orEmpty()

                setState {
                    copy(
                        isLoading = false,
                        categoryList = if (isFirst)
                            data.tag.getOrNull(0)?.content.orEmpty()
                        else categoryList,

                        countryList = if (isFirst)
                            data.tag.getOrNull(1)?.content.orEmpty()
                        else countryList,

                        movieList = movieList,
                        isEmpty = movieList.isEmpty()
                    )
                }
            },

            onError = {
                setState {
                    copy(isLoading = false)
                }
                // 👉 如果你有错误UI，可以发 Effect
                // setEffect { ShowError(it.message) }
            }
        )
    }
}