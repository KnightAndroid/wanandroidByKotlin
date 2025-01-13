package com.knight.kotlin.module_eye_discover.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.module_eye_discover.entity.EyeSearchResultEntity
import com.knight.kotlin.module_eye_discover.repo.EyeDiscoverSearchResultItemRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @Description
 * @Author knight
 * @Time 2025/1/6 20:37
 *
 */
@HiltViewModel
class EyeDiscoverSearchResultItemVm @Inject constructor(private val mRepo: EyeDiscoverSearchResultItemRepo) : BaseViewModel() {



    /**
     *
     * 获取搜索热词更多数据
     */
    fun getMoreDataSearchByQuery(url:String,params:MutableMap<String,Any>): LiveData<EyeSearchResultEntity> {
        return mRepo.getMoreDataSearchByQuery(url,params).asLiveData()
    }




}