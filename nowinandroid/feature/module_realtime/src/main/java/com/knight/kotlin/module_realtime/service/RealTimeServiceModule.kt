package com.knight.kotlin.module_realtime.service

import com.knight.kotlin.module_realtime.api.RealTimeApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RealTimeServiceModule {



    /**
     * realtime模块的[HomeApiService]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return HomeApiService
     */
    @Singleton
    @Provides
    fun provideRealTimeApiService(retrofit: Retrofit) : RealTimeApiService {
        return retrofit.create(RealTimeApiService::class.java)
    }
}