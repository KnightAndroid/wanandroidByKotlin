package com.knight.kotlin.module_home.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.module_home.entity.GroupCityListBean
import com.knight.kotlin.module_home.repo.HomeCityGroupRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/17 16:53
 * @descript:城市列表vm
 */
@HiltViewModel
class HomeCityGroupVm @Inject constructor(private val mRepo: HomeCityGroupRepo) : BaseViewModel() {


    /**
     * 获取城市列表
     */
    fun getCityGroupData(source:String): LiveData<List<GroupCityListBean>> {
        return mRepo.getCityGroupData(source).asLiveData()
    }
}