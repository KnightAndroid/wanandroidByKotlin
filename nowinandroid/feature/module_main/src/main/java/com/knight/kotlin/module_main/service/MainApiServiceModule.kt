package com.knight.kotlin.module_main.service

import com.knight.kotlin.module_main.api.MainApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Author:Knight
 * Time:2021/12/23 19:55
 * Description:MainApiService
 */
@Module
@InstallIn(SingletonComponent::class)
class MainApiServiceModule {
    /**
     * Main模块的[MainApiService]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return HomeApiService
     */
    @Singleton
    @Provides
    fun provideMainApiService(retrofit: Retrofit) : MainApiService {
        return retrofit.create(MainApiService::class.java)
    }
}