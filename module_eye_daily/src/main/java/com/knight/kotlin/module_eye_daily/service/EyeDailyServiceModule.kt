package com.knight.kotlin.module_eye_daily.service

import com.knight.kotlin.module_eye_daily.api.EyeDailyListApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Author:Knight
 * Time:2024/4/29 14:57
 * Description:EyeDailyServiceModule
 */

@Module
@InstallIn(SingletonComponent::class)
class EyeDailyServiceModule {
    /**
     * 开眼日常模块的[EyeDailyApiService]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return HomeApiService
     */
    @Singleton
    @Provides
    fun provideDailyListApiService(retrofit: Retrofit) : EyeDailyListApi {
        return retrofit.create(EyeDailyListApi::class.java)
    }


}