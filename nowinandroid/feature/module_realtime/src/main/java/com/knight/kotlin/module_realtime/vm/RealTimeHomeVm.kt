package com.knight.kotlin.module_realtime.vm

import com.knight.kotlin.module_realtime.repo.RealTimeHomeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @Description 热搜vm
 * @Author knight
 * @Time 2024/9/28 17:28
 *
 */
@HiltViewModel
class RealTimeHomeVm @Inject constructor(mRepo: RealTimeHomeRepo) : RealTimeBaseViewModel(mRepo){


}