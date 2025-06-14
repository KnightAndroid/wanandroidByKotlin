package com.knight.kotlin.module_realtime.vm

import com.knight.kotlin.module_realtime.repo.RealtimeMovieRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/24 11:03
 * @descript:热搜电影vm
 */
@HiltViewModel
class RealtimeMovieVm @Inject constructor(mRepo: RealtimeMovieRepo) : RealTimeBaseViewModel(mRepo){
}