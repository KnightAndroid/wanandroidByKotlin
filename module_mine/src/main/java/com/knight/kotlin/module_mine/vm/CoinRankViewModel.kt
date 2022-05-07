package com.knight.kotlin.module_mine.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_mine.entity.CoinRankListEntity
import com.knight.kotlin.module_mine.repo.CoinRankRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/7 14:00
 * Description:CoinRankViewModel
 */
@HiltViewModel
class CoinRankViewModel @Inject constructor(private val mRepo: CoinRankRepo) : BaseViewModel(){


    /**
     *
     * 积分排行榜
     */
    val coinRankData = MutableLiveData<CoinRankListEntity>()

    /**
     * 获取积分排行榜数据
     */
    fun getRankCoin(page:Int) {
        viewModelScope.launch {
            mRepo.getRankCoin(page)
                .flowOn(Dispatchers.IO)
                .onStart {
                    //开始

                }
                .onEach {
                    coinRankData.postValue(it)
                }
                .onCompletion {

                }
                .catch { toast(it.message ?: "") }
                .collect()

        }
    }
}