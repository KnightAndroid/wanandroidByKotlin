package com.knight.kotlin.module_vedio.service

import com.knight.kotlin.module_vedio.api.VedioApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Author:Knight
 * Time:2024/2/26 14:11
 * Description:VedioServiceModule
 */

@Module
@InstallIn(SingletonComponent::class)
class VedioServiceModule {


    @Singleton
    @Provides
    fun provideVedioApiService(retrofit: Retrofit) : VedioApiService {
        return retrofit.create(VedioApiService::class.java)
    }
}

