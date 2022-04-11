package com.knight.kotlin.module_square.service

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
     * Web模块的[SquareShareArticleApi]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return SquareShareArticleApi
     */
    @Singleton
    @Provides
    fun provideSquareShareService(retrofit: Retrofit) : SquareShareArticleApi {
        return retrofit.create(SquareShareArticleApi::class.java)
    }
}