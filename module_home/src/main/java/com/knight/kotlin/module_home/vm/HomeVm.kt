package com.knight.kotlin.module_home.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.entity.UserInfoEntity
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_common.entity.AppUpdateBean
import com.knight.kotlin.library_database.db.AppDataBase
import com.knight.kotlin.library_database.entity.PushDateEntity
import com.knight.kotlin.library_database.repository.PushArticlesDataRepository
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_home.entity.EveryDayPushArticlesBean
import com.knight.kotlin.module_home.repo.HomeRepo
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
 * Author:Knight
 * Time:2021/12/27 20:13
 * Description:HomeVm
 */
@HiltViewModel
class HomeVm @Inject constructor(private val mRepo:HomeRepo): BaseViewModel(){
    val everyDayPushArticles = MutableLiveData<EveryDayPushArticlesBean>()

    private val repository: PushArticlesDataRepository


    init {
        val mDao =  AppDataBase.getInstance()?.mPushDateDao()!!
        repository = PushArticlesDataRepository(mDao)
    }

    //获取每日推荐文章结果
    val articles = MutableLiveData<List<PushDateEntity>>()
    //获取APP版本更新
    val appUpdateMessage = MutableLiveData<AppUpdateBean>()
    //登录信息
    val userInfo = MutableLiveData<UserInfoEntity>()


    /**
     * 获取每天推送文章
     */
    fun getEveryDayPushArticle() {
        viewModelScope.launch {
            mRepo.getEveryDayPushArticle()
                //指定线程
                .flowOn(Dispatchers.IO)
                .onStart {
                    //开始
                }
                .onEach {
                    everyDayPushArticles.postValue(it)
                }
                .onCompletion {
                    //结束
                }
                .catch { toast(it.message ?: "") }
                .collect()

        }
    }


    /**
     * 检查APP版本更新
     */
    fun checkAppUpdateMessage() {
        viewModelScope.launch {
            mRepo.checkAppUpdateMessage()
                //指定线程
                .flowOn(Dispatchers.IO)
                .onStart {
                    //开始
                }
                .onEach {
                    appUpdateMessage.postValue(it)
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
     * 查询本地历史记录
     */
    fun queryPushDate(){
        viewModelScope.launch {
            repository.findPushArticlesDate()
                //指定线程
                .flowOn(Dispatchers.IO)
                .onStart {
                    //开始
                }
                .onEach {
                    articles.postValue(it)
                }
                .onCompletion {
                    //结束
                }
                .catch { toast(it.message ?: "") }
                .collect()

        }

    }


    /**
     * 更新
     */
    fun updatePushArticlesDate(pushDateEntity: PushDateEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePushArticlesDate(pushDateEntity)
        }

    }

    /**
     *
     * 插入
     */
    fun insertPushArticlesDate(pushDateEntity: PushDateEntity){
        viewModelScope.launch {
            repository.insertPushArticlesDate(pushDateEntity)
                //指定线程
                .flowOn(Dispatchers.IO)
                .onStart {
                    //开始
                }
                .onEach {
                }
                .onCompletion {
                    //结束
                }
                .catch { toast(it.message ?: "") }
                .collect()

        }
    }

    /**
     * 登录
     */
    fun login(userName:String,passWord:String) {
        viewModelScope.launch {
            mRepo.login(userName, passWord)
                //指定线程
                .flowOn(Dispatchers.IO)
                .onStart {
                    //开始
                }
                .onEach {
                    userInfo.postValue(it)
                }
                .onCompletion {
                    //结束
                }
                .catch { toast(it.message ?: "") }
                .collect()

        }

    }








}