package com.knight.kotlin.module_constellate.service

import com.knight.kotlin.module_constellate.api.ConstellateFortuneApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


/**
 * @author created by luguian
 * @organize
 * @Date 2025/8/6 15:16
 * @descript:ConstellateServiceModule
 */
@Module
@InstallIn(SingletonComponent::class)
class ConstellateServiceModule {

    /**
     * 星座运势模块
     * @param retrofit Retrofit
     * @return SquareShareArticleApi
     */
    @Singleton
    @Provides
    fun provideConstellateFortuneService(retrofit: Retrofit): ConstellateFortuneApi {
        return retrofit.create(ConstellateFortuneApi::class.java)
    }
}