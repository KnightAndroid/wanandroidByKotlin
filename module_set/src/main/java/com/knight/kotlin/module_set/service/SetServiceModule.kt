package com.knight.kotlin.module_set.service

import com.knight.kotlin.module_set.api.SetApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Author:Knight
 * Time:2022/3/30 15:31
 * Description:SetServiceModule
 */
@Module
@InstallIn(SingletonComponent::class)
class SetServiceModule {
    /**
     * Wechat模块的[SetApiService]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return SetApiService
     */
    @Singleton
    @Provides
    fun provideSetApiService(retrofit: Retrofit) : SetApiService {
        return retrofit.create(SetApiService::class.java)
    }
}