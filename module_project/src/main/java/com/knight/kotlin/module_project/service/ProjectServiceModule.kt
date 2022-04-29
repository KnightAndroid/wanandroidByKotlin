package com.knight.kotlin.module_project.service

import com.knight.kotlin.module_project.api.ProjectApi
import com.knight.kotlin.module_project.api.ProjectArticleApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Author:Knight
 * Time:2022/4/28 18:05
 * Description:ProjectServiceModule
 */
@Module
@InstallIn(SingletonComponent::class)
class ProjectServiceModule {
    /**
     * Project模块的[ProjectApi]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return SquareApi
     */
    @Singleton
    @Provides
    fun provideProjectService(retrofit: Retrofit) : ProjectApi {
        return retrofit.create(ProjectApi::class.java)
    }

    /**
     * Project模块的[ProjectArticleApi]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return ProjectArticleApi
     */
    @Singleton
    @Provides
    fun provideProjectArticleService(retrofit: Retrofit) : ProjectArticleApi {
        return retrofit.create(ProjectArticleApi::class.java)
    }
}