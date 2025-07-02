package com.knight.kotlin.module_set.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.core.library_base.vm.BaseViewModel
import com.knight.kotlin.module_set.entity.VersionRecordListEntity
import com.knight.kotlin.module_set.repo.AppUpdateRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/8/26 11:13
 * Description:AppUpdateRecordVm
 */
@HiltViewModel
class AppUpdateRecordVm @Inject constructor(private val mRepo:AppUpdateRepo) : BaseViewModel(){
    /**
     * 检查APP版本更新
     */
    fun checkAppUpdateMessage() : LiveData<VersionRecordListEntity> {
        return mRepo.checkAppUpdateRecord().asLiveData()
    }
}