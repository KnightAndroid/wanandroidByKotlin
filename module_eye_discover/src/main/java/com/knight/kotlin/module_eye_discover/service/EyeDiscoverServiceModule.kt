package com.knight.kotlin.module_eye_discover.service

import com.knight.kotlin.module_eye_discover.api.EyeDiscoverCategoryDetailApi
import com.knight.kotlin.module_eye_discover.api.EyeDiscoverScollListApi
import com.knight.kotlin.module_eye_discover.api.EyeDiscoverSearchRecommendApi
import com.knight.kotlin.module_eye_discover.api.EyeDiscoverSearchResultApi
import com.knight.kotlin.module_eye_discover.api.EyeDiscoverSearchResultItemApi
import com.knight.kotlin.module_eye_discover.api.EyeDiscoverSpecialTopicApi
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
    fun provideDiscoverApiService(retrofit: Retrofit) : EyeDiscoverScollListApi {
        return retrofit.create(EyeDiscoverScollListApi::class.java)
    }


    @Singleton
    @Provides
    fun provideDiscoverCategoryDetailApiService(retrofit: Retrofit) : EyeDiscoverCategoryDetailApi {
        return retrofit.create(EyeDiscoverCategoryDetailApi::class.java)
    }



    @Singleton
    @Provides
    fun provideDiscoverSpecialTopicApiService(retrofit: Retrofit) : EyeDiscoverSpecialTopicApi {
        return retrofit.create(EyeDiscoverSpecialTopicApi::class.java)
    }


    @Singleton
    @Provides
    fun provideDiscoverSearchRecommendApiService(retrofit:Retrofit) : EyeDiscoverSearchRecommendApi {
        return retrofit.create(EyeDiscoverSearchRecommendApi::class.java)
    }


    @Singleton
    @Provides
    fun provideDiscoverSearchResultApiService(retrofit:Retrofit) : EyeDiscoverSearchResultApi {
        return retrofit.create(EyeDiscoverSearchResultApi::class.java)
    }


    @Singleton
    @Provides
    fun provideDiscoverSearchResultItemApiService(retrofit:Retrofit) : EyeDiscoverSearchResultItemApi {
        return retrofit.create(EyeDiscoverSearchResultItemApi::class.java)

    }


}