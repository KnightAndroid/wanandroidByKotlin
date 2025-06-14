package com.knight.kotlin.module_project.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.module_project.entity.ProjectArticleListBean
import com.knight.kotlin.module_project.repo.ProjectArticleRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/4/28 18:36
 * Description:ProjectArticleVm
 */
@HiltViewModel
class ProjectArticleVm @Inject constructor(private val mRepo: ProjectArticleRepo) : BaseViewModel() {


    //获取项目文章列表
    fun getProjectArticle(page:Int,cid:Int) : LiveData<ProjectArticleListBean> {
        return mRepo.getProjectArticle(page, cid).asLiveData()

    }

    //获取最新文章列表
    fun getNewProjectArticle(page:Int):LiveData<ProjectArticleListBean>  {
        return mRepo.getNewProjectArticle(page).asLiveData()
    }

    /**
     * 收藏文章
     *
     */
    fun collectArticle(collectArticleId:Int) :LiveData<Any> {
        return mRepo.collectArticle(collectArticleId).asLiveData()
    }

    /**
     *
     * 取消收藏文章
     */
    fun unCollectArticle(unCollectArticleId:Int) :LiveData<Any> {
        return mRepo.unCollectArticle(unCollectArticleId).asLiveData()

    }


}