package com.knight.kotlin.module_eye_discover.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.core.library_base.ktx.showLoadingDialog
import com.core.library_base.vm.BaseViewModel
import com.knight.kotlin.library_network.bean.EyeApiResponse
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_eye_discover.entity.BaseEyeDiscoverEntity
import com.knight.kotlin.module_eye_discover.entity.EyeDiscoverNavEntity
import com.knight.kotlin.module_eye_discover.repo.EyeDiscoverScrollListRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/5 17:54
 * @descript:
 */
@HiltViewModel
class EyeDiscoverScrollListVm @Inject constructor(private val mRepo:EyeDiscoverScrollListRepo) : BaseViewModel(){


    /**
     *
     * 获取发现数据
     */
    fun getDiscoverData():LiveData<List<BaseEyeDiscoverEntity>> {
        showLoadingDialog()
        return mRepo.getDiscoverData(failureCallBack = {
            it?.let { it1 -> toast(it1) }
        }).asLiveData()
    }

    /**
     *
     * 获取发现导航数据
     */
    fun getNav(tabel:String):LiveData<EyeApiResponse<EyeDiscoverNavEntity>> {
        return mRepo.getNav(tabel).asLiveData()
    }





}