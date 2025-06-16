package com.knight.kotlin.module_set.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.knight.kotlin.library_base.R
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_common.entity.AppUpdateBean
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_set.repo.AboutRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/8/25 15:03
 * Description:AboutVm
 */
@HiltViewModel
class AboutVm @Inject constructor(private val mRepo: AboutRepo) : BaseViewModel() {
    /**
     * 检查APP版本更新
     */
    fun checkAppUpdateMessage() :LiveData<AppUpdateBean> {
        return mRepo.checkAppUpdateMessage(failureCallBack = {
            toast(R.string.base_request_failure)
        }).asLiveData()
    }


}