package com.knight.kotlin.module_home.service

import com.knight.kotlin.module_home.api.HomeApiService
import com.knight.kotlin.module_home.api.HomeRecommendApiService
import com.knight.kotlin.module_home.api.HomeSearchApiService
import com.knight.kotlin.module_home.api.HomeSearchResultApiService
import com.knight.kotlin.module_home.api.HomeWeatherNewsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Author:Knight
 * Time:2021/12/27 19:59
 * Description:MainServiceModule
 * 全局作用域的Home模块网络接口代理依赖注入模块
 */
@Module
@InstallIn(SingletonComponent::class)
class MainServiceModule {
    /**
     * Home模块的[HomeApiService]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return HomeApiService
     */
    @Singleton
    @Provides
    fun provideHomeApiService(retrofit: Retrofit) :HomeApiService {
        return retrofit.create(HomeApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideHomeRecommendApiService(retrofit: Retrofit) :HomeRecommendApiService {
        return retrofit.create(HomeRecommendApiService::class.java)
    }


    @Singleton
    @Provides
    fun provideHomeSearchApiService(retrofit: Retrofit) : HomeSearchApiService {
        return retrofit.create(HomeSearchApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideHomeSearchResultApiService(retrofit: Retrofit) : HomeSearchResultApiService {
        return retrofit.create(HomeSearchResultApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideHomeWeatherNewsApiService(retrofit: Retrofit) : HomeWeatherNewsApiService {
        return retrofit.create(HomeWeatherNewsApiService::class.java)
    }








}