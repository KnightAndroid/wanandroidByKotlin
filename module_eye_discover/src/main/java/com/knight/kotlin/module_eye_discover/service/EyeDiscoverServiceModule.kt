package com.knight.kotlin.module_eye_discover.service

import com.knight.kotlin.module_eye_discover.api.EyeDiscoverApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/6 10:03
 * @descript:EyeDiscoverServiceModule
 */
@Module
@InstallIn(SingletonComponent::class)
class EyeDiscoverServiceModule {
    /**
     * 开眼发现模块的[EyeDiscoverService]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return HomeApiService
     */
    @Singleton
    @Provides
    fun provideDiscoverApiService(retrofit: Retrofit) : EyeDiscoverApi {
        return retrofit.create(EyeDiscoverApi::class.java)
    }

}