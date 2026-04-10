package com.knight.kotlin.library_util.baidu


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/16 17:33
 * @descript:地理逆编码工具
 */
object GeoCodeUtils {


    // 用来接收临时回调函数
    private var onSuccessCallback: ((Double, Double) -> Unit)? = null
    private var onFailCallback: (() -> Unit)? = null
    fun getGeocode(
        city: String,
        address: String,
        onSuccess: (lat: Double, lng: Double) -> Unit,
        onFail: (() -> Unit)? = null
    ) {

       // https://nominatim.openstreetmap.org/search?q=%E5%8C%97%E4%BA%AC&format=json 后面用这个接口
       // 这是国外接口 调不了
        // 保存回调
        onSuccessCallback = onSuccess
        onSuccessCallback?.invoke(22.635886,114.062945)
        onFailCallback = onFail

    }

    fun destroy() {
        onSuccessCallback = null
        onFailCallback = null
    }
}