package com.knight.kotlin.module_welcome.service

import com.knight.kotlin.module_welcome.api.WelcomeApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Author:Knight
 * Time:2021/12/21 18:32
 * Description:WelcomeServiceModule
 * 全局作用域的Welcome模块网络接口代理依赖注入模块
 */
@Module
@InstallIn(SingletonComponent::class)
class WelcomeServiceModule {


    /**
     * Welcome模块的[WelcomeApiService]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return HomeApiService
     */
    @Singleton
    @Provides
    fun provideWelcomeApiService(retrofit: Retrofit) :WelcomeApiService {
        return retrofit.create(WelcomeApiService::class.java)
    }
}