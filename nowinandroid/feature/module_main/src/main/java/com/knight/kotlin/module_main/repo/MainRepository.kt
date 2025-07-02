package com.knight.kotlin.module_main.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.module_main.api.MainApiService
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2021/12/22 17:43
 * Description:MainRepository
 * 首页数据仓库
 */
class MainRepository @Inject constructor(): BaseRepository()   {
    @Inject
    lateinit var mMainApiService:MainApiService
}