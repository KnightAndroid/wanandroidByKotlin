package com.knight.kotlin.module_eye_discover.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.core.library_base.ktx.showLoadingDialog
import com.core.library_base.vm.BaseViewModel
import com.knight.kotlin.module_eye_discover.entity.EyeSpecialTopicDetailEntity
import com.knight.kotlin.module_eye_discover.repo.EyeDiscoverSpecialTopicRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/19 15:39
 * @descript:专题vm
 */
@HiltViewModel
class EyeDiscoverSpecialTopicVm @Inject constructor(private val mRepo: EyeDiscoverSpecialTopicRepo) : BaseViewModel(){


    /**
     *
     * 获取发现分类详细数据
     */
    fun getDiscoverSpecialTopicDetail(id: Long): LiveData<EyeSpecialTopicDetailEntity> {
        showLoadingDialog()
        return mRepo.getDiscoverSpecialTopicalDetail(id).asLiveData()

    }

}