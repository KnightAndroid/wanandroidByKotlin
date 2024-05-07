package com.knight.kotlin.module_web.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.module_web.repo.WebRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/2/22 11:26
 * Description:WebVm
 */
@HiltViewModel
class WebVm @Inject constructor (private val mRepo:WebRepo) : BaseViewModel(){
    /**
     * 收藏本文章
     *
     */
    fun collectArticle(collectArticleId:Int):LiveData<Any> {
        return mRepo.collectArticle(collectArticleId).asLiveData()
    }



}