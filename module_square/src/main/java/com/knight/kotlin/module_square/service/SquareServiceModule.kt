package com.knight.kotlin.module_square.service

import com.knight.kotlin.module_square.api.SquareApi
import com.knight.kotlin.module_square.api.SquareArticleApi
import com.knight.kotlin.module_square.api.SquareShareArticleApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Author:Knight
 * Time:2022/4/8 17:48
 * Description:SquareServiceModule
 */
@Module
@InstallIn(SingletonComponent::class)
class SquareServiceModule {

    /**
     * Square模块的[SquareShareArticleApi]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return SquareShareArticleApi
     */
    @Singleton
    @Provides
    fun provideSquareShareService(retrofit: Retrofit) : SquareShareArticleApi {
        return retrofit.create(SquareShareArticleApi::class.java)
    }


    /**
     * Square模块的[SquareApi]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return SquareApi
     */
    @Singleton
    @Provides
    fun provideSquareService(retrofit: Retrofit) : SquareApi {
        return retrofit.create(SquareApi::class.java)
    }


    /**
     * Square模块的[SquareArticleApi]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return SquareArticleApi
     */
    @Singleton
    @Provides
    fun provideSquarArticleService(retrofit: Retrofit) : SquareArticleApi {
        return retrofit.create(SquareArticleApi::class.java)
    }
}