package com.knight.kotlin.module_eye_discover.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_network.bean.EyeApiResponse
import com.knight.kotlin.module_eye_discover.entity.EyeHotQueriesEntity
import com.knight.kotlin.module_eye_discover.repo.EyeDiscoverSearchRecommendRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2024/12/11 14:25
 * @descript:
 */
@HiltViewModel
class EyeDiscoverSearchRecommendVm @Inject constructor(private val mRepo: EyeDiscoverSearchRecommendRepo) : BaseViewModel() {

    /**
     *
     * 获取搜索热词
     */
    fun getHotQueries(): LiveData<EyeApiResponse<EyeHotQueriesEntity>> {
        return mRepo.getHotQueries().asLiveData()
    }







}