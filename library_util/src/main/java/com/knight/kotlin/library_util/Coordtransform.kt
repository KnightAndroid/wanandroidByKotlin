package com.knight.kotlin.library_util

import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/5 16:20
 * @descript:
 *
 *
 *
 *
 *
 */
/**
 *
 * -----------------------------------------------------------------------------------------
 * 坐标系    |解释                                                              |使用地图
 * -----------------------------------------------------------------------------------------
 * WGS84    |地球坐标系，国际上通用的坐标系。设备一般包含GPS芯片或者北斗芯片获取          |GPS/谷歌地图卫星
 *          |的经纬度为WGS84地理坐标系,最基础的坐标，谷歌地图在非中国地区使用的坐标系     |
 * -----------------------------------------------------------------------------------------
 * GCJ02    |火星坐标系，是由中国国家测绘局制订的地理信息系统的坐标系统。                 |腾讯(搜搜)地图，
 *          |并要求在中国使用的地图产品使用的都必须是加密后的坐标，                      |阿里云地图，高德地图，
 *          |而这套WGS84加密后的坐标就是gcj02。                                    |谷歌国内地图
 * -----------------------------------------------------------------------------------------
 * BD09     |百度坐标系，百度在GCJ02的基础上进行了二次加密，                           |百度地图
 *          |官方解释是为了进一步保护用户隐私（我差点就信了）                           |
 * -----------------------------------------------------------------------------------------
 * 小众坐标系 |类似于百度地图，在GCJ02基础上使用自己的加密算法进行二次加密的坐标系           |搜狗地图、图吧地图 等
 * -----------------------------------------------------------------------------------------
 * 墨卡托坐标  |墨卡托投影以整个世界范围，赤道作为标准纬线，本初子午线作为中央经线，
 *           |两者交点为坐标原点，向东向北为正，向西向南为负。
 *           |南北极在地图的正下、上方，而东西方向处于地图的正右、左。
 *
 * 你可以通过这个工具类将上述坐标系进行互相转换。
 *
 * 百度地图地图投影采用的依然是Web Mercator投影，地图瓦片的切片规则遵循TMS标准，瓦片坐标原点在经纬度为0的附近，
 * 但却做了一定的偏移处理，经测算此偏移量约为（-865，15850），
 * 即地图瓦片（0, 0）是从Web Mercator投影坐标系的（-865，15850）点开始的。
 *
 * 顺便提供百度地图的地图等级从18级到1级
 * 18级，1个像素代表1米，17级，1个像素代表2米，16级代表4米，依此类推
 * Author: kong
 */
object Coordtransform {



    const val baiduChange: Double = (Math.PI * 3000.0) / 180.0
    const val ee: Double = 0.00669342162296594323 //偏心率平方
    const val a: Double = 6378245.0 //  # 长半轴

    /**
     * 百度坐标系(BD-09)转火星坐标系(GCJ-02)
     * 百度——>谷歌、高德
     *
     * @param lng_BD 百度坐标经度
     * @param lat_BD 百度坐标纬度
     * @return 转换后的坐标列表形式
     */
    fun BD09toGCJ02(lng_BD: Double, lat_BD: Double): DoubleArray {
        val GCJ02 = DoubleArray(2)

        val x = lng_BD - 0.0065
        val y = lat_BD - 0.006
        val z = sqrt(x * x + y * y) - 0.00002 * sin(y * baiduChange)
        val theta = atan2(y, x) - 0.000003 * cos(x * baiduChange)
        val gg_lng = z * cos(theta)
        val gg_lat = z * sin(theta)
        GCJ02[0] = gg_lng
        GCJ02[1] = gg_lat

        return GCJ02
    }


    /**
     * 百度坐标系(BD-09)转WGS坐标
     *
     * @param lng 百度坐标纬度
     * @param lat 百度坐标经度
     * @return WGS84坐标数组
     */
    fun BD09toWGS84(lng: Double, lat: Double): DoubleArray {
        val gcj: DoubleArray = BD09toGCJ02(lng, lat)
        val wgs84: DoubleArray = GCJ02toWGS84(gcj[0], gcj[1])
        return wgs84
    }

    /**
     * 火星坐标系(GCJ-02)转百度坐标系(BD-09)
     * 谷歌、高德——>百度
     * @param lng_GCJ
     * @param lat_GCJ
     * @return 转换后的坐标列表形式
     */
    fun GCJ02toBD09(lng_GCJ: Double, lat_GCJ: Double): DoubleArray {
        val BD09 = DoubleArray(2)
        //        """
//                实现GCJ02向BD09坐标系的转换
//                :param lng: GCJ02坐标系下的经度
//                :param lat: GCJ02坐标系下的纬度
//                :return: 转换后的BD09下经纬度
//                """
        val z = sqrt(lng_GCJ * lng_GCJ + lat_GCJ * lat_GCJ) + 0.00002 * sin(lat_GCJ * Math.PI)
        val theta = atan2(lat_GCJ, lng_GCJ) + 0.000003 * cos(lng_GCJ * Math.PI)
        val bd_lng = z * cos(theta) + 0.0065
        val bd_lat = z * sin(theta) + 0.006
        BD09[0] = bd_lng
        BD09[1] = bd_lat
        return BD09
    }

    /**
     * GCJ02(火星坐标系)转GPS84
     * @param lng_gcj 火星坐标系的经度
     * @param lat_gcj 火星坐标系纬度
     * @return 转换后的坐标列表形式
     */
    fun GCJ02toWGS84(lng_gcj: Double, lat_gcj: Double): DoubleArray {
        val wgs84 = DoubleArray(2)
        if (outOfChina(lng_gcj, lat_gcj)) {
            return doubleArrayOf(lng_gcj, lat_gcj)
        }
        //       if out_of_china(lng, lat):
//       return [lng, lat]
        var dlat = transformlat(lng_gcj - 105.0, lat_gcj - 35.0)
        var dlng = transformlng(lng_gcj - 105.0, lat_gcj - 35.0)
        val radlat = lat_gcj / 180.0 * Math.PI
        var magic = sin(radlat)
        magic = 1 - ee * magic * magic
        val sqrtmagic = sqrt(magic)
        dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * Math.PI)
        dlng = (dlng * 180.0) / (a / sqrtmagic * cos(radlat) * Math.PI)
        val mglat = lat_gcj + dlat
        val mglng = lng_gcj + dlng
        return doubleArrayOf(lng_gcj * 2 - mglng, lat_gcj * 2 - mglat)
    }

    /**
     * GPS84转GCJ02(火星坐标系)
     * @param lng_wgs WGS84坐标系的经度
     * @param lat_wgs WGS84坐标系纬度
     * @return 转换后的GCJ02下经纬度
     */
    fun WGS84toGCJ02(lng_wgs: Double, lat_wgs: Double): DoubleArray {
        if (outOfChina(lng_wgs, lat_wgs)) {
            return doubleArrayOf(lng_wgs, lat_wgs)
        }
        val GCJ02 = DoubleArray(2)

        var dlat = transformlat(lng_wgs - 105.0, lat_wgs - 35.0)
        var dlng = transformlng(lng_wgs - 105.0, lat_wgs - 35.0)
        val radlat = lat_wgs / 180.0 * Math.PI
        var magic = sin(radlat)
        magic = 1 - ee * magic * magic
        val sqrtmagic = sqrt(magic)
        dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * Math.PI)
        dlng = (dlng * 180.0) / (a / sqrtmagic * cos(radlat) * Math.PI)
        val gcj_lng = lat_wgs + dlng
        val gcj_lat = lng_wgs + dlat
        GCJ02[0] = gcj_lng
        GCJ02[1] = gcj_lat
        return GCJ02
    }


    /**
     * WGS坐标转百度坐标系(BD-09)
     *
     * @param lng WGS84坐标系的经度
     * @param lat WGS84坐标系的纬度
     * @return 百度坐标数组
     */
    fun WGS84toBD09(lng: Double, lat: Double): DoubleArray {
        val gcj: DoubleArray = WGS84toGCJ02(lng, lat)
        val bd09: DoubleArray = GCJ02toBD09(gcj[0], gcj[1])
        return bd09
    }

    /**
     * GPS84 转 墨卡托坐标
     * @param lng GPS84的经度
     * @param lat GPS84纬度
     * @return 转换后的坐标列表形式
     */
    fun WGS84toMercator(lng: Double, lat: Double): DoubleArray {
        val x = lng * 20037508.342789 / 180
        var y = ln(tan((90 + lat) * Math.PI / 360)) / (Math.PI / 180)
        y = y * 20037508.342789 / 180
        return doubleArrayOf(x, y)
    }


    /**
     * 百度坐标系转成通用墨卡托坐标
     *
     * @param lng
     * @param lat
     * @return
     */
    fun bdtoMercator(lng: Double, lat: Double): DoubleArray {
        val gcj02 = BD09toGCJ02(lng, lat)
        val wgs84 = GCJ02toWGS84(gcj02[0], gcj02[1])
        val mercator = WGS84toMercator(wgs84[0], wgs84[1])
        return mercator
    }


    private fun transformlat(lng: Double, lat: Double): Double {
        var ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * sqrt(abs(lng))
        ret += (20.0 * sin(6.0 * lng * Math.PI) + 20.0 * sin(2.0 * lng * Math.PI)) * 2.0 / 3.0
        ret += (20.0 * sin(lat * Math.PI) + 40.0 * sin(lat / 3.0 * Math.PI)) * 2.0 / 3.0
        ret += (160.0 * sin(lat / 12.0 * Math.PI) + 320 * sin(lat * Math.PI / 30.0)) * 2.0 / 3.0
        return ret
    }

    private fun transformlng(lng: Double, lat: Double): Double {
        var ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * sqrt(abs(lng))
        ret += (20.0 * sin(6.0 * lng * Math.PI) + 20.0 * sin(2.0 * lng * Math.PI)) * 2.0 / 3.0
        ret += (20.0 * sin(lng * Math.PI) + 40.0 * sin(lng / 3.0 * Math.PI)) * 2.0 / 3.0
        ret += (150.0 * sin(lng / 12.0 * Math.PI) + 300.0 * sin(lng / 30.0 * Math.PI)) * 2.0 / 3.0
        return ret
    }

    /**
     * 判断是否在国内，不在国内不做偏移
     * @param lng
     * @param lat
     * @return
     */
    private fun outOfChina(lng: Double, lat: Double): Boolean {
        return !(lng > 73.66 && lng < 135.05 && lat > 3.86 && lat < 53.55)
    }

}