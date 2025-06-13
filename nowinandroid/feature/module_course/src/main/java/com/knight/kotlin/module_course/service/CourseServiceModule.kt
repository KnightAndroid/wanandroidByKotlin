package com.knight.kotlin.module_course.service

import com.knight.kotlin.module_course.api.CourseDetailListApi
import com.knight.kotlin.module_course.api.CourseListApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Author:Knight
 * Time:2022/6/2 16:15
 * Description:CourseServiceModule
 */
@Module
@InstallIn(SingletonComponent::class)
class CourseServiceModule {

    /**
     * Course模块的[CourseListApi]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return SquareShareArticleApi
     */
    @Singleton
    @Provides
    fun provideCourseListService(retrofit: Retrofit) : CourseListApi {
        return retrofit.create(CourseListApi::class.java)
    }

    /**
     * Course模块的[CourseDetailListApi]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return CourseDetailListApi
     */
    @Singleton
    @Provides
    fun provideCourseDetailListService(retrofit: Retrofit) : CourseDetailListApi {
        return retrofit.create(CourseDetailListApi::class.java)
    }
}