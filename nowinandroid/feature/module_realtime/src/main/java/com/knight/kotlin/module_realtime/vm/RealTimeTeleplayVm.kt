package com.knight.kotlin.module_realtime.vm

import com.knight.kotlin.module_realtime.repo.RealTimeTeleplayRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/25 9:50
 * @descript:电视榜vm
 */
@HiltViewModel
class RealTimeTeleplayVm  @Inject constructor(mRepo: RealTimeTeleplayRepo) : RealTimeBaseViewModel(mRepo){

}
