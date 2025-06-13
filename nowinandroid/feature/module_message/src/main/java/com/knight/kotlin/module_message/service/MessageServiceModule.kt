package com.knight.kotlin.module_message.service

import com.knight.kotlin.module_message.api.MessageApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Author:Knight
 * Time:2023/5/16 16:46
 * Description:MessageServiceModule
 */
@Module
@InstallIn(SingletonComponent::class)
class MessageServiceModule {

    /**
     * Wechat模块的[MessageApiService]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return WechatApiService
     */
    @Singleton
    @Provides
    fun provideMessageApiService(retrofit: Retrofit) :MessageApiService {
        return retrofit.create(MessageApiService::class.java)
    }
}