package com.knight.kotlin.module_web.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_web.repo.WebRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/2/22 11:26
 * Description:WebVm
 */
@HiltViewModel
class WebVm @Inject constructor (private val mRepo:WebRepo) : BaseViewModel(){

    val collectSucess = MutableLiveData<Boolean>()


    /**
     *
     * 收藏本文章
     *
     */
    fun collectArticle(collectArticleId:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.collectArticle(collectArticleId)
                .catch {
                    toast(it.message ?: "")
                }
                .collect {
                    collectSucess.postValue(true)
                }
        }
    }



}