package com.knight.kotlin.module_realtime.vm

import com.knight.kotlin.module_realtime.repo.RealTimeCarRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/25 14:43
 * @descript:排行榜CarVm
 */
@HiltViewModel
class RealTimeCarVm @Inject constructor(mRepo: RealTimeCarRepo) : RealTimeBaseViewModel(mRepo){

}