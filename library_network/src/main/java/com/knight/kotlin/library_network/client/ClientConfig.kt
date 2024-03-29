package com.knight.kotlin.library_network.client

import androidx.annotation.Nullable
import com.knight.kotlin.library_base.BaseApp
import com.knight.kotlin.library_network.BuildConfig
import com.knight.kotlin.library_network.config.BaseUrlConfig
import com.knight.kotlin.library_network.cookie.InMemoryCookieStore
import com.knight.kotlin.library_network.cookie.JavaNetCookieJar
import com.knight.kotlin.library_network.cookie.SharedPreferencesCookieStore
import com.knight.kotlin.library_network.interceptor.CacheInterceptor
import com.knight.kotlin.library_network.interceptor.HttpInterceptor
import com.knight.kotlin.library_network.interceptor.NetworkInterceptor
import com.knight.kotlin.library_network.log.LoggingInterceptor
import com.knight.kotlin.library_network.util.ReplaceUrlCallFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.net.CookieManager
import java.net.CookiePolicy
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




    companion object {

        lateinit var cookieManager: CookieManager
    }

    /**
     * [OkHttpClient]依赖提供方法
     * @return OkHttpClient
     */
    @Singleton
    @Provides
    fun okHttpClientConfConfig():OkHttpClient {
        //缓存目录
        val cacheFile: File = File(BaseApp.context.getCacheDir(), "knight_wanandroid")
        val cache = Cache(cacheFile, 1024 * 1024 * 50)

        cookieManager= CookieManager(createCookieStore("myCookies",true), CookiePolicy.ACCEPT_ALL)

        //设置拦截器
        val loggingInterceptor = LoggingInterceptor.Builder()
            .loggable(BuildConfig.TYPE != "MASTER")
            .request()
            .requestTag("Request")
            .response()
            .responseTag("Response")
            .build()

        return OkHttpClient.Builder()
            .connectTimeout(15L * 1000L, TimeUnit.MILLISECONDS)
            .readTimeout(20L * 1000L,TimeUnit.MILLISECONDS)
            .addInterceptor(loggingInterceptor) //日志拦截器
            .addInterceptor(CacheInterceptor()) //缓存拦截器
            .addInterceptor(NetworkInterceptor()) //配合缓存拦截器
            .addInterceptor(HttpInterceptor())//重复拦截
            .cache(cache)
            .retryOnConnectionFailure(true)
            .cookieJar(JavaNetCookieJar(cookieManager))
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
            .baseUrl(BaseUrlConfig.WANDROID_URL)
            .callFactory(object :
                ReplaceUrlCallFactory(okHttpClient) {
                @Nullable
                override fun getNewUrl(baseUrlName: String?, request: Request?): HttpUrl? {
                    //这里其实通过头部注解@Headers("BaseUrlName:gitee") 来说明这个链接使用 "https://gitee.com/"
                    if (baseUrlName == "gitee") {
                        val oldUrl: String = request?.url.toString()
                        val newUrl =
                            oldUrl.replace(BaseUrlConfig.WANDROID_URL, BaseUrlConfig.GITEE_RUL)
                        return newUrl.toHttpUrl()
                    }
                    return null
                }
            })
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }


    private fun createCookieStore(name:String,persistent:Boolean) = if (persistent) {
        SharedPreferencesCookieStore(BaseApp.context,name)
    } else {
        InMemoryCookieStore(name)
    }
}



