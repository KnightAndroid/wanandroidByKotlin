package com.knight.kotlin.module_utils.service

import com.knight.kotlin.module_utils.api.UtilsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Author:Knight
 * Time:2022/6/2 14:54
 * Description:UtilsServiceModule
 */
@Module
@InstallIn(SingletonComponent::class)
class UtilsServiceModule {
    /**
     * Utils模块的[UtilsApi]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return UtilsApi
     */
    @Singleton
    @Provides
    fun provideUtilsService(retrofit: Retrofit) : UtilsApi {
        return retrofit.create(UtilsApi::class.java)
    }

}