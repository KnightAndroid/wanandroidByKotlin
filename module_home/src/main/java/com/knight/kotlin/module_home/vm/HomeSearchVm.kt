package com.knight.kotlin.module_home.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.knight.kotlin.library_base.entity.SearchHotKeyEntity
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_database.entity.SearchHistroyKeywordEntity
import com.knight.kotlin.library_database.repository.HistroyKeywordsRepository
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_home.repo.HomeSearchRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/4/12 17:14
 * Description:HomeSearchVm
 */
@HiltViewModel
class HomeSearchVm @Inject constructor(private val mRepo: HomeSearchRepo) : BaseViewModel() {

    private val repository =  HistroyKeywordsRepository()

    /**
     *
     * 查询热词
     */
    fun getHotKey(failureCallBack:((String?) ->Unit) ?= null): LiveData<MutableList<SearchHotKeyEntity>> {
        return mRepo.getHotKey(failureCallBack).asLiveData()
    }

    /**
     * 查询本地数据
     *
     */
    fun getLocalSearchwords():LiveData<MutableList<SearchHistroyKeywordEntity>>{
        return repository.queryHistroyKeywords(failureCallBack = {
            it?.let { it1 -> toast(it1) }
        }).asLiveData()
    }


}