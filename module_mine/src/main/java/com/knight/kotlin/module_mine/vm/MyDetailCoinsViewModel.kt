package com.knight.kotlin.module_mine.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.module_mine.entity.MyDetailCoinListEntity
import com.knight.kotlin.module_mine.repo.MyDetailCoinRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/11 10:53
 * Description:MyDetailCoinsViewModel
 */
@HiltViewModel
class MyDetailCoinsViewModel @Inject constructor(private val mRepo: MyDetailCoinRepo) : BaseViewModel() {

    /**
     * 获取积分明细
     */
    fun getMyDetailCoin(page:Int): LiveData<MyDetailCoinListEntity> {
        return mRepo.getMyDetailCoin(page).asLiveData()
    }
}