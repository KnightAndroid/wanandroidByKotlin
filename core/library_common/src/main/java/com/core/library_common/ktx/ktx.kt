package com.core.library_common.ktx

import android.content.Context
import com.baidu.location.BDLocation
import com.core.library_common.config.Appconfig
import com.core.library_common.config.CacheKey
import com.core.library_common.entity.UserInfoEntity
import com.core.library_common.util.CacheUtils

/**
 * @Description
 * @Author knight
 * @Time 2025/7/14 22:00
 *
 */

/**
 * 获取屏幕宽度
 */
val Context.screenWidth
    get() = resources.displayMetrics.widthPixels

/**
 * 获取屏幕高度
 */
val Context.screenHeight
    get() = resources.displayMetrics.heightPixels

/**
 * 返回用户信息
 */
fun getUser(): UserInfoEntity? {
    Appconfig.user?.let {
        return it
    } ?: run {
        Appconfig.user = CacheUtils.getDataInfo(CacheKey.USER, UserInfoEntity::class.java)
        return Appconfig.user
    }

}


/**
 * 返回维度
 */
fun getLatitude():Double {
    Appconfig.location?.let {
        return it.latitude
    } ?:run {
        return 4.9E-324
    }


}

/**
 *
 * 返回经纬度信息
 */
fun getLocation(): BDLocation? {
    Appconfig.location ?.let {
        return it
    } ?:run {
        Appconfig.location= CacheUtils.getDataInfo(CacheKey.CURRENTLOCATION, BDLocation::class.java)
        return Appconfig.location
    }
}

/**
 * 返回经度
 */
fun getLongitude():Double {
    Appconfig.location?.let {
        return it.longitude
    } ?:run {
        return 4.9E-324
    }
}
