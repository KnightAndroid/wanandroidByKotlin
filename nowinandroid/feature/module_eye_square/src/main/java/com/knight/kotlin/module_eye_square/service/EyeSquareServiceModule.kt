package com.knight.kotlin.module_eye_square.service

import com.knight.kotlin.module_eye_square.api.EyeSquareApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * @Description
 * @Author knight
 * @Time 2025/2/11 20:23
 * EyeSquareServiceModule
 */
@Module
@InstallIn(SingletonComponent::class)
class EyeSquareServiceModule {


    /**
     * 开眼发现模块的[EyeSquareService]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return HomeApiService
     */
    @Singleton
    @Provides
    fun provideSquareApiService(retrofit: Retrofit) : EyeSquareApi {
        return retrofit.create(EyeSquareApi::class.java)
    }
}