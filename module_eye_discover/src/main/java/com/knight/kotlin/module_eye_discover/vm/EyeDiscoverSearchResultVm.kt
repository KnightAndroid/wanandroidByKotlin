package com.knight.kotlin.module_eye_discover.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.knight.kotlin.library_base.entity.EyeCardEntity
import com.knight.kotlin.library_base.entity.EyeMetroCard
import com.knight.kotlin.library_base.ktx.toJson
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_network.bean.EyeApiResponse
import com.knight.kotlin.module_eye_discover.entity.EyeHotQueriesEntity
import com.knight.kotlin.module_eye_discover.entity.EyeSearchResultEntity
import com.knight.kotlin.module_eye_discover.repo.EyeDiscoverSearchRecommendRepo
import com.knight.kotlin.module_eye_discover.repo.EyeDiscoverSearchResultRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.json.JsonObject
import javax.inject.Inject

/**
 * @Description 搜索结果vm
 * @Author knight
 * @Time 2025/1/2 21:22
 *
 */
@HiltViewModel
class EyeDiscoverSearchResultVm  @Inject constructor(private val mRepo: EyeDiscoverSearchResultRepo) : BaseViewModel() {
    /**
     *
     * 获取搜索热词
     */
    fun getSearchResultByQuery(query:String): LiveData<EyeSearchResultEntity> {
        return mRepo.getSearchResultByQuery(query).asLiveData()
    }
}