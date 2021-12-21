package com.knight.kotlin.library_network.util

import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.Request


/**
 * Author:Knight
 * Time:2021/12/21 17:19
 * Description:ReplaceUrlCallFactory
 */
abstract class ReplaceUrlCallFactory(@NonNull delegate: Call.Factory) :
    CallFactoryProxy(delegate) {


    override fun newCall(request: Request): Call {
        /*
         * @Headers("BaseUrlName:xxx") for method, or
         * method(@Header("BaseUrlName") String name) for parameter
         */

        val baseUrlName: String? = request.header(BASE_URL_NAME)
        Log.d("222",baseUrlName.toString())
        if (baseUrlName != null) {
            val newHttpUrl = getNewUrl(baseUrlName, request)
            if (newHttpUrl != null) {
                val newRequest: Request = request.newBuilder().url(newHttpUrl).build()
                return delegate.newCall(newRequest)
            } else {
                Log.w("wanandroid-->>Request", "getNewUrl() return null when baseUrlName==$baseUrlName")
            }
        }
        return delegate.newCall(request)
    }

    /**
     * @param baseUrlName name to sign baseUrl
     * @return new httpUrl, if null use old httpUrl
     */
    @Nullable
    protected abstract fun getNewUrl(baseUrlName: String?, request: Request?): HttpUrl?

    companion object {
        private const val BASE_URL_NAME = "BaseUrlName"
    }
}