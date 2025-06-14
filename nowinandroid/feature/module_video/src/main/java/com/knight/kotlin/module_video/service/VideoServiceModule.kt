package com.knight.kotlin.module_video.service

import com.knight.kotlin.module_video.api.VideoApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Author:Knight
 * Time:2024/2/26 14:11
 * Description:VideoServiceModule
 */

@Module
@InstallIn(SingletonComponent::class)
class VideoServiceModule {


    @Singleton
    @Provides
    fun provideVideoApiService(retrofit: Retrofit) : VideoApiService {
        return retrofit.create(VideoApiService::class.java)
    }
}

