package com.knight.kotlin.module_wechat.service

import com.knight.kotlin.module_wechat.api.WechatApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.kotlin.module_wechat.service
 * @ClassName:      WechatServiceModule
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/22 8:53 下午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/22 8:53 下午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@Module
@InstallIn(SingletonComponent::class)
class WechatServiceModule {
    /**
     * Wechat模块的[WechatApiService]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return WechatApiService
     */
    @Singleton
    @Provides
    fun provideWechatApiService(retrofit: Retrofit) :WechatApiService {
        return retrofit.create(WechatApiService::class.java)
    }
}