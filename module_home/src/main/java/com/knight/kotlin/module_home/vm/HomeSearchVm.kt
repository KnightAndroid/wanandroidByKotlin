package com.knight.kotlin.module_home.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.entity.SearchHotKeyEntity
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_database.entity.SearchHistroyKeywordEntity
import com.knight.kotlin.library_database.repository.HistroyKeywordsRepository
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_home.repo.HomeSearchRepo
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
 * Time:2022/4/12 17:14
 * Description:HomeSearchVm
 */
@HiltViewModel
class HomeSearchVm @Inject constructor(private val mRepo: HomeSearchRepo) : BaseViewModel() {

    //热词数据
    val searchHotKeyList = MutableLiveData<MutableList<SearchHotKeyEntity>>()

    //请求热词标志位
    val requestSearchHotKeyFlag = MutableLiveData<Boolean>(true)

    //本地搜索数据
    val localSearchwords = MutableLiveData<MutableList<SearchHistroyKeywordEntity>>()

    private val repository =  HistroyKeywordsRepository()


    /**
     *
     * 查询热词
     */
    fun getHotKey() {
        viewModelScope.launch {
            mRepo.getHotKey()
                //指定线程
                .flowOn(Dispatchers.IO)
                .onStart {
                    //开始
                }
                .onEach {
                    searchHotKeyList.postValue(it)
                }
                .onCompletion {

                }
                .catch {
                    toast(it.message ?: "")
                    requestSearchHotKeyFlag.postValue(false)
                }
                .collect()

        }

    }

    /**
     * 查询本地数据
     *
     */
    fun getLocalSearchwords(){
        viewModelScope.launch {
            repository.queryHistroyKeywords()
                //指定线程
                .flowOn(Dispatchers.IO)
                .onStart {
                    //开始
                }
                .onEach {
                    localSearchwords.postValue(it)
                }
                .onCompletion {

                }
                .catch {
                    toast(it.message ?: "")
                }
                .collect()

        }
    }


}