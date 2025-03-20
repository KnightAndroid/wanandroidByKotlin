package com.knight.kotlin.module_realtime.vm

import com.knight.kotlin.module_realtime.repo.RealTimeTextRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/19 16:40
 * @descript:
 */
@HiltViewModel
class RealTimeTextVm @Inject constructor(mRepo: RealTimeTextRepo) : RealTimeBaseViewModel(mRepo){

}