package com.knight.kotlin.module_wechat.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.module_wechat.entity.WechatArticleListEntity
import com.knight.kotlin.module_wechat.repo.WechatRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.kotlin.module_wechat.vm
 * @ClassName:      WechatVm
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/21 10:34 上午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/21 10:34 上午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

@HiltViewModel
class WechatVm @Inject constructor(private val mRepo:WechatRepo) : BaseViewModel() {

    /**
     *
     * 获取公众号数据
     */
    fun getWechatArticle(cid:Int,page:Int,failureCallBack:((String?) ->Unit) ?= null) : LiveData<WechatArticleListEntity> {
        return mRepo.getWechatArticle(cid,page,failureCallBack).asLiveData()
    }

    /**
     *
     * 收藏本文章
     *
     */
    fun collectArticle(collectArticleId:Int) :LiveData<Any> {
        return mRepo.collectArticle(collectArticleId).asLiveData()
    }

    /**
     *
     * 取消收藏
     *
     */
    fun unCollectArticle(unCollectArticleId:Int):LiveData<Any>  {
        return mRepo.collectArticle(unCollectArticleId).asLiveData()
    }

    /**
     *
     * 根据关键字搜索
     */
    fun getWechatArticleBykeywords(cid:Int,page:Int,keyword:String) :LiveData<WechatArticleListEntity> {
        return mRepo.getWechatArticleByKeyWords(cid, page,keyword).asLiveData()
    }
}