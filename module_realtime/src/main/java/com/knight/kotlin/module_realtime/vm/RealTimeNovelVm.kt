package com.knight.kotlin.module_realtime.vm

import com.knight.kotlin.module_realtime.repo.RealTimeNovelRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/20 11:08
 * @descript:小说vm
 */
@HiltViewModel
class RealTimeNovelVm @Inject constructor(mRepo: RealTimeNovelRepo) : RealTimeBaseViewModel(mRepo){

}