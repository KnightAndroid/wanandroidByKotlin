package com.knight.kotlin.module_mine.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_mine.entity.MyDetailCoinListEntity
import com.knight.kotlin.module_mine.repo.MyDetailCoinRepo
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
 * Time:2022/5/11 10:53
 * Description:MyDetailCoinsViewModel
 */
@HiltViewModel
class MyDetailCoinsViewModel @Inject constructor(private val mRepo: MyDetailCoinRepo) : BaseViewModel() {
    /**
     * 用户积分明细
     */
    val myDetailCoinList = MutableLiveData<MyDetailCoinListEntity>()


    /**
     * 获取积分明细
     */
    fun getMyDetailCoin(page:Int) {
        viewModelScope.launch {
            mRepo.getMyDetailCoin(page)
                .flowOn(Dispatchers.IO)
                .onStart {
                    //开始
                }
                .onEach {
                    myDetailCoinList.postValue(it)
                }
                .onCompletion {
                    //结束
                }
                .catch {
                    toast(it.message ?: "")
                }
                .collect()
        }
    }
}