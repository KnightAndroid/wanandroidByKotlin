package com.knight.kotlin.module_vedio.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.module_vedio.entity.VedioListEntity
import com.knight.kotlin.module_vedio.repo.VedioRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2024/2/26 14:18
 * Description:VedioVm
 */
@HiltViewModel
class VedioVm @Inject constructor(private val mVedioRepo : VedioRepo) : BaseViewModel(){

    val vedios = MutableLiveData<MutableList<VedioListEntity>>()


    /**
     *
     * 获取推荐视频
     */
    fun getDouyinVedios() {
        viewModelScope.launch(Dispatchers.IO) {
            mVedioRepo.getDouyinVedios()
                .catch {

                }
                .collect{
                    vedios.postValue(it)
                }


        }
    }
}