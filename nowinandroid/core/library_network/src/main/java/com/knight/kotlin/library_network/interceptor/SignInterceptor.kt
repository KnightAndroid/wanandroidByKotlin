package com.knight.kotlin.library_network.interceptor


import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.reflect.TypeToken
import com.knight.kotlin.library_common.config.Appconfig
import com.core.library_base.util.GsonUtils
import com.knight.kotlin.library_network.bean.AuthToken
import com.knight.kotlin.library_network.bean.EyeApiResponse
import com.knight.kotlin.library_network.bean.GetToken
import com.knight.kotlin.library_network.header.HeaderStorage
import com.knight.kotlin.library_network.util.AesUtils
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.lang.reflect.Type


class SignInterceptor(
    private val context: Context,
    private val headerStorage: HeaderStorage
) : Interceptor {

    override fun intercept(chain: Chain): Response {
        //if (chain.request().url.toString() === BaseUrlConfig.OPENEYE_URL) {
        val headers  = chain.request().headers.values("Domain-Name")
        if (headers .size == 1) {
            val headerTag = headers[0]
            if (headerTag == "eye_sub") {

                val requestBuilder = chain.request().newBuilder()
                headerStorage.header.entries.forEach {
                    requestBuilder.addHeader(it.key, it.value)
                }

                val ts: String = (System.currentTimeMillis() / 1000).toString()
                val sign: String = AesUtils.encrypt(signString("refresh_token", ts))
                val decrypt = AesUtils.decrypt(sign)


                val response = chain.proceed(requestBuilder.build())
                val refresh = tryRefreshToken(requestBuilder, response)
                return if (refresh) intercept(chain) else response
            }
        }
        return chain.proceed(chain.request())

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

            val gson = Gson()
            val type: Type = object : TypeToken<EyeApiResponse<GetToken>>() {}.getType()
            val eyeApiResponse: EyeApiResponse<GetToken> = gson.fromJson(result, type)
//            val content = eyeApiResponse.message.get("content")
//            var contentMessage:String = ""
//            if (content != null) {
//                contentMessage = content.asString
//            }
//            if (content == null) {
                if (eyeApiResponse.code in 60000..69999 && eyeApiResponse.result != null) {
                    val request = Request.Builder()
                        .headers(requestBuilder.build().headers)
                        .url("$BASE_URL?grant_type=${eyeApiResponse.result.grant_type}")
                        .post(fillBuilder(eyeApiResponse.result.grant_type))
                        .build()

                    val body = OkHttpClient.Builder().build().newCall(request).execute().body?.string()+""
                    val gson = Gson()
                    val type: Type = object : TypeToken<EyeApiResponse<AuthToken>>() {}.getType()
                    gson.fromJson<EyeApiResponse<AuthToken>?>(body, type).result?.let {
                        headerStorage.updateHeader(it)
                    }

                    return true
                }
//            } else {
//                return false
//            }

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
        return "$grantType|${com.knight.kotlin.library_common.config.Appconfig.APP_ID}|android|${com.knight.kotlin.library_common.config.Appconfig.VERSION_NAME}|${headerStorage.deviceId}|$ts"
    }

    companion object {
        private const val BASE_URL = "http://api.eyepetizer.net/v1/system/auth/token"
    }
}

