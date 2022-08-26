package com.knight.kotlin.module_set.service

import com.knight.kotlin.module_set.api.AboutApiService
import com.knight.kotlin.module_set.api.AppUpdateRecordService
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
     * Set模块的[SetApiService]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return SetApiService
     */
    @Singleton
    @Provides
    fun provideSetApiService(retrofit: Retrofit) : SetApiService {
        return retrofit.create(SetApiService::class.java)
    }


    /**
     * Set模块的[AboutApiService]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return SetApiService
     */
    @Singleton
    @Provides
    fun provideAboutApiService(retrofit: Retrofit) : AboutApiService {
        return retrofit.create(AboutApiService::class.java)
    }


    /**
     * Set模块的[AppUpdateRecordService]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return SetApiService
     */
    @Singleton
    @Provides
    fun provideAppUpdateRecordService(retrofit: Retrofit) : AppUpdateRecordService {
        return retrofit.create(AppUpdateRecordService::class.java)
    }






}