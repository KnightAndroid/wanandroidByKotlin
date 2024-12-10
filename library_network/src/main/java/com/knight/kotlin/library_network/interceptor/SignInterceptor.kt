package com.knight.kotlin.library_network.interceptor


import android.content.Context
import android.util.Log
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.ktx.fromJson
import com.knight.kotlin.library_base.ktx.versionName
import com.knight.kotlin.library_base.util.GsonUtils
import com.knight.kotlin.library_network.bean.ApiResponse
import com.knight.kotlin.library_network.bean.AuthToken
import com.knight.kotlin.library_network.bean.GetToken
import com.knight.kotlin.library_network.header.HeaderStorage
import com.knight.kotlin.library_network.util.AesUtils
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class SignInterceptor(
    private val context: Context,
    private val headerStorage: HeaderStorage
) : Interceptor {

    override fun intercept(chain: Chain): Response {
        //if (chain.request().url.toString() === BaseUrlConfig.OPENEYE_URL) {
            val requestBuilder = chain.request().newBuilder()
            headerStorage.header.entries.forEach {
                requestBuilder.addHeader(it.key, it.value)
            }

        val ts: String = (System.currentTimeMillis() / 1000).toString()
        val sign: String = AesUtils.encrypt(signString("refresh_token", ts))
        val decrypt = AesUtils.decrypt(sign)
        Log.e("huqiang1", "fillBuilder: $decrypt")
        Log.e("huqiang2", "fillBuilder: "+sign+"")

            val response = chain.proceed(requestBuilder.build())
            val refresh = tryRefreshToken(requestBuilder, response)
            return if (refresh) intercept(chain) else response
//        } else {
//            return chain.proceed(chain.request())
//        }

    }

    private fun tryRefreshToken(
        requestBuilder: Request.Builder,
        response: Response
    ): Boolean {
        if (response.code == 400) {
            val result = response.peekBody(Long.MAX_VALUE).string()
            val apiResponse =
                fromJson<ApiResponse<GetToken>>(result)
               // json.decodeFromString(ApiResponse.serializer(GetToken.serializer()), result)
            if (apiResponse.code in 60000..69999 && apiResponse.result != null) {
                val request = Request.Builder()
                    .headers(requestBuilder.build().headers)
                    .url("$BASE_URL?grant_type=${apiResponse.result.grant_type}")
                    .post(fillBuilder(apiResponse.result.grant_type))
                    .build()

                fromJson<ApiResponse<AuthToken>>(     OkHttpClient.Builder().build().newCall(request).execute().body?.string() ?: "")
                    .result?.let {
                        headerStorage.updateHeader(it)
                    }
//                json.decodeFromString(
//                    ApiResponse.serializer(AuthToken.serializer()),
//                    OkHttpClient.Builder().build().newCall(request).execute().body()?.string() ?: ""
//                )
                return true
            }
        }
        return false
    }

    private fun fillBuilder(grantType: String): FormBody {
        val paramBuilder = FormBody.Builder()
        val ts: String = (System.currentTimeMillis() / 1000).toString()
        val sign: String = AesUtils.encrypt(signString(grantType, ts))
        val decrypt = AesUtils.decrypt(sign)
        Log.e("huqiang", "fillBuilder: $decrypt")
        val builder = paramBuilder.add("grant_type", grantType)
            .add("sign", sign)
            .add("ts", ts)
            .add("device_info", deviceInfo())
        if (grantType == "refresh_token") {
            builder.add("refresh_token", headerStorage.refreshToken)
        }
        return builder.build()
    }

    private fun deviceInfo(): String {
        val map = mapOf<String, JsonElement>(
            "imei" to JsonPrimitive("android"),
            "udid" to JsonPrimitive(headerStorage.udId),
            "mac" to JsonPrimitive("02:00:00:00:00:00"),
            "imsi" to JsonPrimitive("0"),
            "android_id" to JsonPrimitive("42f05f4b31c3bc18"),
            "screen" to JsonPrimitive(headerStorage.screen)
        )
        return GsonUtils.toJson(map)
      //  return JsonObject(map).toString()
    }

    private fun signString(
        grantType: String,
        ts: String
    ): String {
        return "$grantType|${Appconfig.APP_ID}|android|${context.versionName}|${headerStorage.deviceId}|$ts"
    }

    companion object {
        private const val BASE_URL = "http://api.eyepetizer.net/v1/system/auth/token"
    }
}

