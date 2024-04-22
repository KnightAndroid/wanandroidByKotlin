package com.knight.kotlin.module_square.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.ktx.appStr
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_square.R
import com.knight.kotlin.module_square.repo.SquareShareListRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/4/8 17:53
 * Description:SquareShareArticleVm
 */
@HiltViewModel
class SquareShareArticleVm @Inject constructor (private val mRepo:SquareShareListRepo) : BaseViewModel() {

    val shareArticleSuccess = MutableLiveData<Boolean>()

    /**
     * 分享文章
     */
    fun shareArticle(title:String,link:String) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.shareArticle(title, link)
                .onStart {
                    showLoading(appStr(R.string.square_share_article_loading))
                }
                .onEach { shareArticleSuccess.postValue(true) }
                .onCompletion {
                    dimissLoading()
                }
                .catch {
                    toast(it.message ?: "")

                }
                .collect()
        }
    }

}