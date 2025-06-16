package com.knight.kotlin.library_network.domain

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response


/**
 * Author:Knight
 * Time:2024/4/16 9:46
 * Description:DomainManager 用于动态配置和切换Okhttp的baseurl
 */
object DomainManager {
    const val DOMAIN_NAME = "Domain-Name"
    const val DOMAIN_NAME_HEADER = "$DOMAIN_NAME:"

    internal var domainInterceptor : DomainInterceptor? = null
        private set

    var enable:Boolean
        @JvmStatic
        get() = domainInterceptor?.enable ?: false
        @JvmStatic
        set(value){
            domainInterceptor?.enable = value
        }


    /**
     *
     * 是否开启调试日志
     */
    @get:JvmStatic
    @set:JvmStatic
    var debuggable:Boolean = false

    /**
     *
     * 使用Domain
     *
     * @param builder
     * @param baseUrl
     */
    @JvmStatic
    fun useDomain(builder: OkHttpClient.Builder,baseUrl:String) {
        builder.addInterceptor(domainInterceptor ?: DomainInterceptor(baseUrl).also {
            domainInterceptor = it
        })
    }


    /**
     * 设置主域名
     *
     *
     * @param url
     */
    @JvmStatic
    fun setMainDomain(url:String) {
          val interceptor = domainInterceptor ?: throw RuntimeException("set main domain require call method ${DomainManager::useDomain.name} first.")
          interceptor.setMainDomain(url)
    }


    /**
     * set the domain of the specified name.
     * 设置[name]表示的域名为[url],通常是配置其他域名
     * @param name 域名的key，标识符，比如使用腾讯的域名，那么自定义一个标识符区别该域名 ,比如使用tencent
     * @param name
     * @param url
     */
    @JvmStatic
    fun setDoMain(name:String,url:String) {
        val inteceptor = domainInterceptor
            ?: throw RuntimeException("set domain require call method ${DomainManager::useDomain.name} first.")
        inteceptor.setDomain(name, url)
    }

    @JvmStatic
    @JvmOverloads
    fun addMainHeader(
        key:String,
        value:String,
        confilctStrategy:OnConflictStrategy = OnConflictStrategy.IGNORE
    ) {
        val interceptor = domainInterceptor
            ?: throw RuntimeException("set domain require call method ${DomainManager::useDomain.name} first.")
        interceptor.addMainHeader(key, value,confilctStrategy)
    }


    @JvmStatic
    @JvmOverloads
    fun addHeader(
        domainName:String,
        key:String,
        value:String,
        conflictStrategy: OnConflictStrategy = OnConflictStrategy.IGNORE
    ) {
        val interceptor = domainInterceptor
            ?: throw RuntimeException("set domain require call method ${DomainManager::useDomain.name} first.")
        interceptor.addHeader(domainName, key, value, conflictStrategy)
    }


    @JvmStatic
    fun removeHeader(domainName:String,key:String) : Pair<String,OnConflictStrategy>? {
        return domainInterceptor?.removeHeader(domainName,key)
    }



    /**
     * 域名切换以及域名对应的全局Header 拦截器
     * @param baseUrl 主域名，必须设置，后面可以修改，不能为空
     * @see enable 启用/禁用
     * @see setMainDomain 设置主域名
     * @see setDomain 设置其他域名
     * @see addMainHeader
     * @see removeMainHeader
     * @see addHeader
     * @see removeHeader
     */
    internal class DomainInterceptor(baseUrl: String) : Interceptor {

        @JvmField
        var enable: Boolean = true

        private val configs = mutableMapOf<String, DomainConfig>()

        /**
         * 设置主域名
         */
        fun setMainDomain(url: String) = setDomain(MAIN_DOMAIN, url)

        /**
         * 设置[name]表示的域名为[url],通常是配置其他域名
         * @param name 域名的key，标识符，比如使用腾讯的域名，那么自定义一个标识符区别该域名 ,比如使用tencent
         */
        fun setDomain(
            name: String,
            url: String
        ) {
            val cache = configs[name]
            if (cache != null) {
                logD {
                    "[DomainInterceptor#setDomain] the domain config (key=$name) is exists,do update"
                }
                val previous = cache.expectBaseUrl
                cache.updateBaseUrl(url)
                //从老的中移除目标地址
                cache.oldBaseUrls.remove(url)
                logD { "[DomainInterceptor#setDomain] set previous ($previous) to expect (${cache.expectBaseUrl})" }
            } else {
                configs[name] = DomainConfig(name, url)
                logD { "[DomainInterceptor#setDomain] save the new domain config (key=$name,url=$url)" }
            }
        }

        fun addMainHeader(
            key: String, value: String,
            conflictStrategy: OnConflictStrategy = OnConflictStrategy.IGNORE
        ) = addHeader(MAIN_DOMAIN, key, value, conflictStrategy)

        fun removeMainHeader(key: String): Pair<String, OnConflictStrategy>? =
            removeHeader(MAIN_DOMAIN, key)

        fun addHeader(
            domainName: String, key: String, value: String,
            conflictStrategy: OnConflictStrategy = OnConflictStrategy.IGNORE
        ): Pair<String, OnConflictStrategy>? {
            val cache = configs[domainName]
            require(cache != null) {
                "[DomainInterceptor#addHeader] the domain config named '$domainName' not found,please use call ${::setDomain} method before add header."
            }
            logD { "[DomainInterceptor#addHeader] add header to the domain config named '$domainName' (key=${key},value=$value,conflictStrategy=$conflictStrategy)" }
            return cache.addHeader(key, value, conflictStrategy)
        }

        fun removeHeader(domainName: String, key: String): Pair<String, OnConflictStrategy>? {
            return configs[domainName]?.removeHeader(key)
        }

        override fun intercept(chain: Interceptor.Chain): Response {
            logD { "[DomainInterceptor]intercept" }
            return chain.proceed(handleRequest(chain.request()))
        }

        private fun handleRequest(request: Request): Request {
            if (!enable)
                return request
            logD { "[DomainInterceptor#handleRequest] handleRequest" }
            val domainName = obtainDomainNameFromHeaders(request)
            return if (domainName.isNullOrEmpty()) {
                //没有配置domain的，都是使用主域名
                logD { "[DomainInterceptor#handleRequest] the request does not set domain name,use main domain to transform" }
                transformRequest(configs[MAIN_DOMAIN]!!, request)
            } else {
                val domainConfig = configs[domainName]
                require(domainConfig != null) {
                    "can not found the base url of the domain name(=${domainName}) ,please call setDomain($domainName,your base url) method set before use."
                }
                logD { "[DomainInterceptor#handleRequest] the domain config found ,begin transform." }
                transformRequest(domainConfig, request)
            }
        }

        /**
         * 转换请求
         */
        private fun transformRequest(domainConfig: DomainConfig, request: Request): Request {
            synchronized(domainConfig) {
                val urlValue = request.url.toString()
                val baseUrl = obtainBaseUrl(urlValue, domainConfig)
                if (baseUrl.isNullOrEmpty()) {
                    logD { "[DomainInterceptor#transformRequest] the base url is not found in config domains,ure original request(ignore base url transform and global header set)." }
                    return request
                }

                return newRequest(baseUrl, request, domainConfig)
            }
        }

        /**
         * @param baseUrl the base url of the [request].
         * @param request the original request.
         * @param config DomainConfig corresponding to [baseUrl]
         */
        private fun newRequest(baseUrl: String, request: Request, config: DomainConfig): Request {
            val isBaseUrlSame = baseUrl == config.expectBaseUrl
            if (isBaseUrlSame && config.headers.isEmpty()) {
                logD { "[DomainInterceptor#newRequest] the base url is same with current config,and the global header is empty,use the original request." }
                return request
            }

            val builder = request.newBuilder()
            if (!isBaseUrlSame) {
                val urlValue = request.url.toString()
                val newUrlValue = urlValue.replace(baseUrl, config.expectBaseUrl)
                logD { "[DomainInterceptor#transformRequest] transform success,new url is $newUrlValue (the original base url is :$baseUrl)" }
                builder.url(newUrlValue)
            }
            val originalHeaders = request.headers
            config.headers.forEach { (key, valuePair) ->
                valuePair.second.apply(originalHeaders, builder, key, valuePair.first)
            }
            return builder.build()
        }

        /**
         * 提取BaseUrl
         * find the base url of the [urlValue] from the [domainConfig]
         */
        private fun obtainBaseUrl(urlValue: String, domainConfig: DomainConfig): String? {
            if (urlValue.startsWith(domainConfig.expectBaseUrl)) {
                logD { "[DomainInterceptor#obtainBaseUrl] the original request url is start with the expect base url,return directly." }
                return domainConfig.expectBaseUrl
            }
            val baseUrl = domainConfig.oldBaseUrls.findLast {
                urlValue.startsWith(it)
            }
            if (!baseUrl.isNullOrEmpty()) {
                //如果在当前url chain中查找到
                logD { "[DomainInterceptor#obtainBaseUrl] find the base url,return." }
                return baseUrl
            }

            if (domainConfig.domainName == MAIN_DOMAIN) {
                logD { "[DomainInterceptor#obtainBaseUrl] not find base url,return directly." }
                return null
            } else {
                logD { "[DomainInterceptor#obtainBaseUrl] not find base url,try found in main domain." }
                val mainChain = configs[MAIN_DOMAIN]
                if (mainChain == null) {
                    logD { "[DomainInterceptor#obtainBaseUrl] main domain is null,return directly." }
                    return null
                }
                return obtainBaseUrl(urlValue, mainChain)
            }
        }

        /**
         * 从header中获取域名
         * obtain header form the request headers.
         */
        private fun obtainDomainNameFromHeaders(request: Request): String? {
            val headers = request.headers(DOMAIN_NAME)
            if (headers.isNullOrEmpty())
                return null
            require(headers.size == 1) { "Only one $DOMAIN_NAME in the headers" }
            return request.header(DOMAIN_NAME)
        }

        init {
            require(baseUrl.isNotEmpty()) {
                "the base url must not be empty."
            }
            setMainDomain(baseUrl)
        }
    }


    /**
     * @param domainName the name of the request
     * @param baseUrl the current base url of the domain
     */
    internal class DomainConfig(
        @JvmField val domainName: String,
        private var baseUrl: String
    ) {

        internal val oldBaseUrls = mutableListOf<String>()

        internal val headers = mutableMapOf<String, Pair<String, OnConflictStrategy>>()

        val expectBaseUrl: String get() = baseUrl

        /**
         * update the current base url
         */
        @Synchronized
        fun updateBaseUrl(url: String) {
            if (url == baseUrl) {
                logD { "[UrlChain#update] the new base url is same with current base url,ignore set. current = $baseUrl expect = $url" }
                return
            }
            val previous = baseUrl
            baseUrl = url
            oldBaseUrls.add(previous)
        }

        /**
         * add global header
         */
        fun addHeader(
            key: String,
            value: String,
            conflictStrategy: OnConflictStrategy
        ): Pair<String, OnConflictStrategy>? {
            return headers.put(key, Pair(value, conflictStrategy))
        }

        /**
         * remove global header
         */
        fun removeHeader(key: String): Pair<String, OnConflictStrategy>? {
            return headers.remove(key)
        }
    }

    private const val TAG = "RNet"
    internal const val MAIN_DOMAIN = "_MAIN_"

    /**
     * 采用这种方法（内联），在使用的时候，可以避免字符串的创建
     */
    @JvmStatic
    internal inline fun logD(msgFactory: () -> String) {
        if (debuggable)
            println("$TAG:${msgFactory.invoke()}")
    }

}