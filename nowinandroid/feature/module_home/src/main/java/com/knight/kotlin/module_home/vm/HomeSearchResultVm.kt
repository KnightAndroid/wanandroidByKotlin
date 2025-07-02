package com.knight.kotlin.module_home.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.core.library_base.vm.BaseViewModel
import com.knight.kotlin.module_home.entity.HomeArticleListBean
import com.knight.kotlin.module_home.repo.HomeSearchResultRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/4/20 11:21
 * Description:HomeSearchResultVm
 */
@HiltViewModel
class HomeSearchResultVm @Inject constructor(private val mRepo: HomeSearchResultRepo) : BaseViewModel(){

    /**
     *
     * 通过关键字搜索列表文章
     */
    fun searchArticleByKeyword(page:Int,keywords:String): LiveData<HomeArticleListBean> {
        return mRepo.searchArticleByKeyword(page,keywords).asLiveData()
    }



    /**
     * 收藏文章
     *
     */
    fun collectArticle(collectArticleId:Int) :LiveData<Any>{
        return mRepo.collectArticle(collectArticleId).asLiveData()
    }

    /**
     *
     * 取消收藏文章
     */
    fun unCollectArticle(unCollectArticleId:Int):LiveData<Any> {
        return mRepo.unCollectArticle(unCollectArticleId).asLiveData()
    }


}