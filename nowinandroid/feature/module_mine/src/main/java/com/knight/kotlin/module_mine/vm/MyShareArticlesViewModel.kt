package com.knight.kotlin.module_mine.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.core.library_base.vm.BaseViewModel
import com.knight.kotlin.module_mine.entity.MyShareArticleEntity
import com.knight.kotlin.module_mine.repo.MyShareArticlesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/17 10:30
 * Description:MyShareArticlesViewModel
 */
@HiltViewModel
class MyShareArticlesViewModel @Inject constructor(private val mRepo: MyShareArticlesRepo) : BaseViewModel(){


    /**
     * 获取自己分享文章
     */
    fun getMyShareArticles(page:Int): LiveData<MyShareArticleEntity> {
        return mRepo.getMyShareArticles(page).asLiveData()
    }


    /**
     * 删除自己分享的文章
     */
    fun deleteMyShareArticles(articleId:Int):LiveData<Any> {
        return mRepo.deleteMyShareArticles(articleId).asLiveData()
    }
}