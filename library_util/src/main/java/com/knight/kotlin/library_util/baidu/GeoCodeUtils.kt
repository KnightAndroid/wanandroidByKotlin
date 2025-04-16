package com.knight.kotlin.library_util.baidu

import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.geocode.GeoCodeOption
import com.baidu.mapapi.search.geocode.GeoCodeResult
import com.baidu.mapapi.search.geocode.GeoCoder
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/16 17:33
 * @descript:地理逆编码工具
 */
object GeoCodeUtils {

    private var mCoder: GeoCoder? = null

    // 用来接收临时回调函数
    private var onSuccessCallback: ((Double, Double) -> Unit)? = null
    private var onFailCallback: (() -> Unit)? = null

    private val geoCoderListener = object : OnGetGeoCoderResultListener {
        override fun onGetGeoCodeResult(result: GeoCodeResult?) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                onFailCallback?.invoke()
                return
            }

            result.location?.let {
                onSuccessCallback?.invoke(it.latitude, it.longitude)
            } ?: onFailCallback?.invoke()
        }

        override fun onGetReverseGeoCodeResult(result: ReverseGeoCodeResult?) {
            // 可选实现
        }
    }

    fun getGeocode(
        city: String,
        address: String,
        onSuccess: (lat: Double, lng: Double) -> Unit,
        onFail: (() -> Unit)? = null
    ) {
        if (mCoder == null) {
            mCoder = GeoCoder.newInstance().apply {
                setOnGetGeoCodeResultListener(geoCoderListener)
            }
        }

        // 保存回调
        onSuccessCallback = onSuccess
        onFailCallback = onFail

        // 发起请求
        mCoder?.geocode(GeoCodeOption().city(city).address(address))
    }

    fun destroy() {
        mCoder?.destroy()
        mCoder = null
        onSuccessCallback = null
        onFailCallback = null
    }
}