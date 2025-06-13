package com.knight.kotlin.module_eye_recommend.service

import com.knight.kotlin.module_eye_recommend.api.EyeRecommendApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


/**
 * @author created by luguian
 * @organize
 * @Date 2025/5/23 14:43
 * @descript:开眼推荐
 */

@Module
@InstallIn(SingletonComponent::class)
class EyeRecommendServiceModule {



    /**
     * 开眼推荐模块的[EyeRecommendService]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return HomeApiService
     */
    @Singleton
    @Provides
    fun provideEyeRecommendApiService(retrofit: Retrofit) : EyeRecommendApi {
        return retrofit.create(EyeRecommendApi::class.java)
    }
}