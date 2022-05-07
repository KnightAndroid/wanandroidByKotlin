package com.knight.kotlin.module_navigate.service

import com.knight.kotlin.module_navigate.api.HierachyApi
import com.knight.kotlin.module_navigate.api.HierachyArticleApi
import com.knight.kotlin.module_navigate.api.NavigateApi
import com.knight.kotlin.module_navigate.api.NavigateRightTreeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Author:Knight
 * Time:2022/5/5 15:00
 * Description:NavigateServicModule
 */
@Module
@InstallIn(SingletonComponent::class)
class NavigateServicModule {

    /**
     * Navigate模块的[NavigateApi]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return NavigateApi
     */
    @Singleton
    @Provides
    fun provideNavigateService(retrofit: Retrofit) : NavigateApi {
        return retrofit.create(NavigateApi::class.java)
    }

    @Singleton
    @Provides
    fun provideNavigateRightService(retrofit: Retrofit) : NavigateRightTreeApi {
        return retrofit.create(NavigateRightTreeApi::class.java)
    }


    @Singleton
    @Provides
    fun provideHierachyService(retrofit: Retrofit) : HierachyApi {
        return retrofit.create(HierachyApi::class.java)
    }

    @Singleton
    @Provides
    fun provideHierachyArticleService(retrofit: Retrofit) : HierachyArticleApi {
        return retrofit.create(HierachyArticleApi::class.java)
    }
}