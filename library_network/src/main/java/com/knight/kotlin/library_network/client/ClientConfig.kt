package com.knight.kotlin.library_network.client

import com.knight.kotlin.library_network.BuildConfig
import com.knight.kotlin.library_network.config.BaseUrlConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 *
 * Author:Knight
 * Time:2021/12/15 10:35
 * Description:ClientConfig
 *
 */
@Module
@InstallIn(SingletonComponent::class)
class ClientConfig {
    /**
     * [OkHttpClient]依赖提供方法
     * @return OkHttpClient
     */
    fun okHttpClientConfConfig():OkHttpClient {
        //拦截日志
        val level = if (BuildConfig.TYPE != "MASTER") HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(level)

        return OkHttpClient.Builder()
            .connectTimeout(15L * 1000L, TimeUnit.MILLISECONDS)
            .readTimeout(20L * 1000L,TimeUnit.MILLISECONDS)
            .addInterceptor(loggingInterceptor)
            .retryOnConnectionFailure(true)
            .build()
    }

    /**
     * 项目主要服务器地址的[Retrofit]依赖提供方法
     * @param okHttpClient Okhttp客户端
     * @return Retrofit
     */
    @Singleton
    @Provides
    fun retrofitBuild(okHttpClient: OkHttpClient) :Retrofit {
        return Retrofit.Builder()
            .baseUrl(BaseUrlConfig.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
}



