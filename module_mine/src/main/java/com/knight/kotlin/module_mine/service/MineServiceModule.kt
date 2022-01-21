package com.knight.kotlin.module_mine.service

import com.knight.kotlin.module_mine.api.MineApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Author:Knight
 * Time:2022/1/18 17:33
 * Description:MineServiceModule
 * 全局作用域的Mine模块网络接口代理依赖注入模块
 */
@Module
@InstallIn(SingletonComponent::class)
class MineServiceModule {
    /**
     * Welcome模块的[MineApiService]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return MineApiService
     */
    @Singleton
    @Provides
    fun provideMineApiService(retrofit: Retrofit) : MineApiService {
        return retrofit.create(MineApiService::class.java)
    }
}