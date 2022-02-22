package com.knight.kotlin.module_web.service

import com.knight.kotlin.module_web.api.WebApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Author:Knight
 * Time:2022/2/22 11:09
 * Description:WebServiceModule
 * 全局作用域的Web模块网络接口代理依赖注入模块
 */
@Module
@InstallIn(SingletonComponent::class)
class WebServiceModule {

    /**
     * Web模块的[WebApiService]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return HomeApiService
     */
    @Singleton
    @Provides
    fun provideWebApiService(retrofit: Retrofit) : WebApiService {
        return retrofit.create(WebApiService::class.java)
    }
}