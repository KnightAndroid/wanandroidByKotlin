package com.knight.kotlin.module_wechat.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_wechat.entity.WechatArticleListEntity
import com.knight.kotlin.module_wechat.repo.WechatRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
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

    //公众号数据
    val wechatArticle = MutableLiveData<WechatArticleListEntity>()

    //是否收藏成功
    val collectSucess = MutableLiveData<Boolean>()

    //是否取消收藏成功
    val uncollectSuccess = MutableLiveData<Boolean>()

    //根据关键字搜索
    val wechatArticleKeyword = MutableLiveData<WechatArticleListEntity>()

    /**
     *
     * 获取公众号数据
     */
    fun getWechatArticle(cid:Int,page:Int) {
        viewModelScope.launch {
            mRepo.getWechatArticle(cid,page)
                .flowOn(Dispatchers.IO)
                .onStart {
                    //开始

                }
                .onEach {
                    wechatArticle.postValue(it)
                }
                .onCompletion {
                    //结束
                }
                .catch {
                    requestSuccessFlag.postValue(false)
                }
                .collect()

        }

    }

    /**
     *
     * 收藏本文章
     *
     */
    fun collectArticle(collectArticleId:Int) {
        viewModelScope.launch {
            mRepo.collectArticle(collectArticleId)
                .flowOn(Dispatchers.IO)
                .onStart {
                    //开始
                }
                .onEach {
                    collectSucess.postValue(true)
                }
                .onCompletion {
                    //结束
                }
                .catch {
                    toast(it.message ?: "")
                }
                .collect()
        }
    }

    /**
     *
     * 取消收藏
     *
     */
    fun unCollectArticle(unCollectArticleId:Int) {
        viewModelScope.launch {
            mRepo.cancelCollectArticle(unCollectArticleId)
                .flowOn(Dispatchers.IO)
                .onStart {
                    //开始
                }
                .onEach {
                    collectSucess.postValue(true)
                }
                .onCompletion {
                    //结束
                }
                .catch {
                    toast(it.message ?: "")
                }
                .collect()
        }
    }

    /**
     *
     * 根据关键字搜索
     */
    fun getWechatArticleBykeywords(cid:Int,page:Int,keyword:String) {
        viewModelScope.launch {
            mRepo.getWechatArticleByKeyWords(cid,page,keyword)
                .flowOn(Dispatchers.IO)
                .onStart {
                    //开始
                }
                .onEach {
                    collectSucess.postValue(true)
                }
                .onCompletion {
                    //结束
                }
                .catch {
                    toast(it.message ?: "")
                }
                .collect()
        }

    }
}