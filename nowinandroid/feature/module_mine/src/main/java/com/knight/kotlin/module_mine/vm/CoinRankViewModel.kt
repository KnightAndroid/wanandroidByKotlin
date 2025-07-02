package com.knight.kotlin.module_mine.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.core.library_base.vm.BaseViewModel
import com.knight.kotlin.module_mine.entity.CoinRankListEntity
import com.knight.kotlin.module_mine.repo.CoinRankRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/7 14:00
 * Description:CoinRankViewModel
 */
@HiltViewModel
class CoinRankViewModel @Inject constructor(private val mRepo: CoinRankRepo) : BaseViewModel(){




    /**
     * 获取积分排行榜数据
     */
    fun getRankCoin(page:Int): LiveData<CoinRankListEntity> {
        return mRepo.getRankCoin(page).asLiveData()
    }
}