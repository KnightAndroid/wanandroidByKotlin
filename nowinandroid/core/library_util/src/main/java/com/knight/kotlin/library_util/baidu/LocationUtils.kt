package com.knight.kotlin.library_util.baidu

import android.content.Context
import com.knight.kotlin.library_common.config.Appconfig
import com.knight.kotlin.library_common.config.CacheKey
import com.knight.kotlin.library_common.entity.LocationEntity
import com.knight.kotlin.library_common.util.CacheUtils


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/8 16:56
 * @descript:
 */
object LocationUtils {

    var mOnceLocationListener: OnceLocationListener? = null


    fun init(context: Context) {
        //这里写地图初始化逻辑

    }


    /**
     * 获取定位
     *
     * @param listener    回调
     * @param timeOut     超时时间:单位毫秒，-1表示不限时间。
     * @param forceUpdate 强制刷新
     */
    fun getLocation(listener: OnceLocationListener) {
        mOnceLocationListener = listener
        val location = LocationEntity(22.635886,114.062945,"广东省","深圳市","坂田区","深圳市坂田区大发埔社区埔东街")
        Appconfig.location = location
        CacheUtils.saveDataInfo(CacheKey.CURRENTLOCATION, Appconfig.location)
        mOnceLocationListener?.onReceiveLocation(location)
        mOnceLocationListener = null
    }






}

/**
 *
 * 立即定位监听
 */
interface OnceLocationListener {
    //获取定位地址
    fun onReceiveLocation(location: LocationEntity?)

}