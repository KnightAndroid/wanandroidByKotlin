package com.knight.kotlin.module_square.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.core.library_base.ktx.dimissLoadingDialog
import com.core.library_base.vm.BaseViewModel
import com.knight.kotlin.module_square.repo.SquareShareListRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/4/8 17:53
 * Description:SquareShareArticleVm
 */
@HiltViewModel
class SquareShareArticleVm @Inject constructor (private val mRepo:SquareShareListRepo) : BaseViewModel() {

    /**
     * 分享文章
     */
    fun shareArticle(title:String,link:String) : LiveData<Any> {
        return mRepo.shareArticle(title, link, failureCallBack = {
            dimissLoadingDialog()
        }).asLiveData()
    }

}