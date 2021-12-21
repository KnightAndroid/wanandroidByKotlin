package com.knight.kotlin.library_network.client

import android.util.Log
import androidx.annotation.Nullable
import com.knight.kotlin.library_network.BuildConfig
import com.knight.kotlin.library_network.config.BaseUrlConfig
import com.knight.kotlin.library_network.util.ReplaceUrlCallFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
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
    @Singleton
    @Provides
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
            .callFactory(object :
                ReplaceUrlCallFactory(okHttpClient) {
                @Nullable
                override fun getNewUrl(baseUrlName: String?, request: Request?): HttpUrl? {

                    if (baseUrlName == "gitee") {
                        val oldUrl: String = request?.url.toString()
                        val newUrl =
                            oldUrl.replace(BaseUrlConfig.URL, "https://gitee.com/")
                        return newUrl.toHttpUrl()
                    }
                    return null
                }
            })
            .addConverterFactory(GsonConverterFactory.create())
            //.client(okHttpClient)
            .build()

    }
}



