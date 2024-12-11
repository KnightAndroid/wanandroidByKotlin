package com.knight.kotlin.library_network.interceptor

import android.content.Context
import android.os.Build
import android.os.Build.VERSION
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_network.header.HeaderStorage
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


/**
 * @author created by luguian
 * @organize
 * @Date 2024/12/11 15:05
 * @descript:给开眼视频接口增加头部参数
 */
class EyeAddHeadInterceptor(context: Context,
                            private val headerStorage: HeaderStorage
) :  Interceptor{
    private val commonParams by lazy {
        mutableMapOf<String,String>(
            "udid" to headerStorage.udId,
            "deviceModel" to Build.MODEL,
            "system_version_code" to VERSION.SDK_INT.toString(),
            "vc" to Appconfig.VERSION_CODE.toString(),
            "vn" to Appconfig.VERSION_NAME
        )
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()
        when (request.method) {
            "GET" -> addCommonParamToUrl(request, requestBuilder)
            "POST" -> addCommonParamToBody(request, requestBuilder)
            else -> {
            }
        }
        return chain.proceed(requestBuilder.build())
    }

    private fun addCommonParamToUrl(
        request: Request,
        requestBuilder: Request.Builder
    ) {
        val builder = request.url.newBuilder()
        commonParams.forEach { (key, value) ->
            builder.addQueryParameter(key, value)
        }
        requestBuilder.url(builder.build())
    }

    private fun addCommonParamToBody(
        request: Request,
        requestBuilder: Request.Builder
    ) {
        val requestBodyBuilder = FormBody.Builder()
        (request.body as? FormBody)?.let { form ->
            repeat(form.size) {
                requestBodyBuilder.add(form.name(it), form.value(it))
            }
            commonParams.forEach { (key, value) ->
                requestBodyBuilder.add(key, value)
            }
        }
        requestBuilder.post(requestBodyBuilder.build())
    }
}