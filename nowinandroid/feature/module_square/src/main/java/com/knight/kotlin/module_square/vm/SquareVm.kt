package com.knight.kotlin.module_square.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.module_square.entity.SquareQuestionListBean
import com.knight.kotlin.module_square.repo.SquareRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2024/4/22 11:06
 * Description:SquareVm
 */
@HiltViewModel
class SquareVm @Inject constructor(private val mRepo: SquareRepo) : BaseViewModel() {
    /**
     * 问答列表数据
     */
    fun getQuestion(page:Int) : LiveData<SquareQuestionListBean> {
        return mRepo.getQuestions(page).asLiveData()
    }


    /**
     * 收藏文章
     *
     */
    fun collectArticle(collectArticleId:Int) : LiveData<Any> {
        return mRepo.collectArticle(collectArticleId).asLiveData()
    }

    /**
     *
     * 取消收藏文章
     */
    fun unCollectArticle(unCollectArticleId:Int) : LiveData<Any> {
        return mRepo.unCollectArticle(unCollectArticleId).asLiveData()
    }


}