package com.knight.kotlin.module_mine.service

import com.knight.kotlin.module_mine.api.LoginApiService
import com.knight.kotlin.module_mine.api.MineApiService
import com.knight.kotlin.module_mine.api.QuickLoginApiService
import com.knight.kotlin.module_mine.api.RegisterApiService
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
     * Mine模块的[MineApiService]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return MineApiService
     */
    @Singleton
    @Provides
    fun provideMineApiService(retrofit: Retrofit) : MineApiService {
        return retrofit.create(MineApiService::class.java)
    }

    /**
     * Mine模块的[QuickLoginApiService]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return QuickLoginApiService
     */
    @Singleton
    @Provides
    fun provideQuickLoginApiService(retrofit: Retrofit):QuickLoginApiService {
        return retrofit.create(QuickLoginApiService::class.java)
    }


    /**
     * Mine模块的[LoginApiService]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return LoginApiService
     */
    @Singleton
    @Provides
    fun provideLoginApiService(retrofit: Retrofit):LoginApiService {
        return retrofit.create(LoginApiService::class.java)
    }

    /**
     * Mine模块的[RegisterApiService]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return RegisterApiService
     */
    @Singleton
    @Provides
    fun provideRegisterApiService(retrofit: Retrofit): RegisterApiService {
        return retrofit.create(RegisterApiService::class.java)
    }







}