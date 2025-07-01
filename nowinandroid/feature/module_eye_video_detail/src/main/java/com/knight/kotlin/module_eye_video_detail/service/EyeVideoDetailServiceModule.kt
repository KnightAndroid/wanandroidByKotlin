package com.knight.kotlin.module_eye_video_detail.service

import com.knight.kotlin.module_eye_video_detail.api.EyeVideoCommentCommentApi
import com.knight.kotlin.module_eye_video_detail.api.EyeVideoDetailApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class EyeVideoDetailServiceModule {
    /**
     *
     * 开眼视频模块的[EyeVideoDetailApi] 依赖提供的方法
     * @param retrofit Retrofit
     * @return EyeVideoDetailApi
     *
     */
    @Singleton
    @Provides
    fun provideVideoDetailApiService(retrofit: Retrofit) :EyeVideoDetailApi {
        return retrofit.create(EyeVideoDetailApi::class.java)
    }

    /**
     * 视频评论
     *
     */
    @Singleton
    @Provides
    fun provideVideoCommentApiService(retrofit: Retrofit) :EyeVideoCommentCommentApi {
        return retrofit.create(EyeVideoCommentCommentApi::class.java)
    }
}