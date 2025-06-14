package com.knight.kotlin.module_realtime.vm

import com.knight.kotlin.module_realtime.repo.RealTimeGameRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/25 16:46
 * @descript:
 */
@HiltViewModel
class RealTimeGameVm @Inject constructor(mRepo: RealTimeGameRepo) : RealTimeBaseViewModel(mRepo) {
}