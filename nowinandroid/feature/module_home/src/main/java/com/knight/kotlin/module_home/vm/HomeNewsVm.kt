package com.knight.kotlin.module_home.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.core.library_base.vm.BaseViewModel
import com.knight.kotlin.module_home.entity.ZaoBaoAudioBean
import com.knight.kotlin.module_home.entity.ZaoBaoBean
import com.knight.kotlin.module_home.repo.HomeNewsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/11 15:41
 * @descript:
 */
@HiltViewModel
class HomeNewsVm @Inject constructor(private val mRepo: HomeNewsRepo) : BaseViewModel() {


    /**
     * 获取新闻
     */
    fun getNews(type:String): LiveData<ZaoBaoBean> {
        return mRepo.getNews(type).asLiveData()
    }


    /**
     * 获取新闻
     */
    fun getAudio(): LiveData<ZaoBaoAudioBean> {
        return mRepo.getAudio().asLiveData()
    }





}