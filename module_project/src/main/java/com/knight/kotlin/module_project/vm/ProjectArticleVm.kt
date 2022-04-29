package com.knight.kotlin.module_project.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_project.entity.ProjectArticleListBean
import com.knight.kotlin.module_project.repo.ProjectArticleRepo
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
 * Time:2022/4/28 18:36
 * Description:ProjectArticleVm
 */
@HiltViewModel
class ProjectArticleVm @Inject constructor(private val mRepo: ProjectArticleRepo) : BaseViewModel() {

    //项目文章列表
    val projectArticle = MutableLiveData<ProjectArticleListBean>()
    //是否收藏成功
    val collectArticle = MutableLiveData<Boolean>()
    //是否取消收藏成功
    val unCollectArticle = MutableLiveData<Boolean>()

    //获取项目文章列表
    fun getProjectArticle(page:Int,cid:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.getProjectArticle(page, cid)
                .onStart {

                }
                .onEach {
                    projectArticle.postValue(it)
                }
                .onCompletion {

                }
                .catch {
                    toast(it.message ?: "")

                }
                .collect()
        }

    }

    //获取最新文章列表
    fun getNewProjectArticle(page:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.getNewProjectArticle(page)
                .onStart {

                }
                .onEach {
                    projectArticle.postValue(it)
                }
                .onCompletion {

                }
                .catch {
                    toast(it.message ?: "")

                }
                .collect()
        }
    }

    /**
     * 收藏文章
     *
     */
    fun collectArticle(collectArticleId:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.collectArticle(collectArticleId)
                .onStart {
                    //开始
                }
                .onEach {
                    collectArticle.postValue(true)
                }
                .onCompletion {
                    //结束
                }
                .catch { toast(it.message ?: "") }
                .collect()
        }
    }

    /**
     *
     * 取消收藏文章
     */
    fun unCollectArticle(unCollectArticleId:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.unCollectArticle(unCollectArticleId)
                .onStart {
                    //开始
                }
                .onEach {
                    unCollectArticle.postValue(true)
                }
                .onCompletion {
                    //结束
                }
                .catch { toast(it.message ?: "") }
                .collect()
        }


    }


}