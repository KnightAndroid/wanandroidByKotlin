package com.knight.kotlin.module_main.vm

import com.core.library_base.vm.BaseViewModel
import com.knight.kotlin.module_main.repo.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2021/12/22 17:36
 * Description:MainViewModel
 * 首页VM层
 */
@HiltViewModel
class MainViewModel @Inject constructor(private val mRepository:MainRepository): BaseViewModel() {



}