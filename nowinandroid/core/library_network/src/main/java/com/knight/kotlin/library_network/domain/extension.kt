package com.knight.kotlin.library_network.domain

import okhttp3.OkHttpClient

/**
 * Author:Knight
 * Time:2024/4/16 14:31
 * Description:extension
 */

fun OkHttpClient.Builder.addDomain(baseUrl: String): OkHttpClient.Builder {
    DomainManager.useDomain(this, baseUrl)
    return this
}


fun OkHttpClient.Builder.setDomain(name: String,url:String): OkHttpClient.Builder {
    DomainManager.setDoMain(name, url)
    return this
}