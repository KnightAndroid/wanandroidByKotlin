package com.knight.kotlin.library_util.baidu

import android.content.Context
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.SDKInitializer
import com.knight.kotlin.library_common.config.Appconfig
import com.knight.kotlin.library_common.config.CacheKey
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.library_util.HandlerUtils
import com.knight.kotlin.library_util.toast


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/8 16:56
 * @descript:
 */
object LocationUtils {



    /**
     *
     * 百度地图客户端
     */
    var mLocationClient: LocationClient? = null
    var mOnceLocationListener: OnceLocationListener? = null
    private val myListener = MyLocationListener()

    fun init(context: Context) {
        //百度地图初始化
        SDKInitializer.setAgreePrivacy(context.applicationContext, true)
        LocationClient.setAgreePrivacy(true)
        SDKInitializer.initialize(context.applicationContext)
        mLocationClient = LocationClient(context)
        mLocationClient?.registerLocationListener(myListener)

        val option = LocationClientOption()

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy)
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；
        //LocationMode.Fuzzy_Locating, 模糊定位模式；v9.2.8版本开始支持，可以降低API的调用频率，但同时也会降低定位精度；

        option.setCoorType("bd09ll")
        //可选，设置返回经纬度坐标类型，默认gcj02
        //gcj02：国测局坐标；
        //bd09ll：百度经纬度坐标；
        //bd09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标

        option.setFirstLocType(LocationClientOption.FirstLocType.SPEED_IN_FIRST_LOC)
        //可选，首次定位时可以选择定位的返回是准确性优先还是速度优先，默认为速度优先
        //可以搭配setOnceLocation(Boolean isOnceLocation)单次定位接口使用，当设置为单次定位时，setFirstLocType接口中设置的类型即为单次定位使用的类型
        //FirstLocType.SPEED_IN_FIRST_LOC:速度优先，首次定位时会降低定位准确性，提升定位速度；
        //FirstLocType.ACCUARACY_IN_FIRST_LOC:准确性优先，首次定位时会降低速度，提升定位准确性；

        option.setScanSpan(0)
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效

        //option.setOpenGnss(true)
        //可选，设置是否使用卫星定位，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true

        //option.setLocationNotify(true);
        //可选，设置是否当卫星定位有效时按照1S/1次频率输出卫星定位结果，默认false

        option.setIgnoreKillProcess(false)
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false)
        //可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5*60*1000)
        //可选，V7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGnss(false)
        //可选，设置是否需要过滤卫星定位仿真结果，默认需要，即参数为false
        option.setIsNeedAddress(true)
        option.setNeedNewVersionRgc(true)
        //可选，设置是否需要最新版本的地址信息。默认需要，即参数为true

        mLocationClient?.let {
            it.setLocOption(option)
        }


        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
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
        mLocationClient?.start()
    }

    class MyLocationListener: BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation?) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            location?.run {
                if (locType != 61 || locType != 161) {
                    toast(locTypeDescription)
                }
                Appconfig.location = this
                mOnceLocationListener?.let {
                    //这里因为百度会多次回调，因此屏蔽 只回调一次
                    var cancelRunnable: Runnable = Runnable { } // 初始化为一个空 Runnable
                    cancelRunnable = HandlerUtils.postDelayed(Runnable {
                        CacheUtils.saveDataInfo(CacheKey.CURRENTLOCATION, Appconfig.location)
                        it.onReceiveLocation(location)
                        // 方法执行完成后，取消监听
                        cancelRunnable.run()
                        mOnceLocationListener = null
                    }, 500) // 500毫秒延迟

                }
                val latitude = getLatitude()    //获取纬度信息
                val longitude = getLongitude()    //获取经度信息

            }

        }


        override fun onLocDiagnosticMessage(resultCode: Int, locationType: Int, strategy: String?) {
            super.onLocDiagnosticMessage(resultCode, locationType, strategy)
            if (resultCode != 161) {
                strategy?.let {
                    toast(it)
                }

            }
        }
    }




}

/**
 *
 * 立即定位监听
 */
interface OnceLocationListener {

    //获取定位地址
    fun onReceiveLocation(location: BDLocation?)

}