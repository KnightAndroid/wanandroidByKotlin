package com.knight.kotlin.library_common.ktx

import com.knight.kotlin.library_common.config.Appconfig
import com.knight.kotlin.library_common.config.CacheKey
import com.knight.kotlin.library_common.entity.LocationEntity
import com.knight.kotlin.library_common.entity.UserInfoEntity
import com.knight.kotlin.library_common.util.CacheUtils


/**
 * @author created by luguian
 * @organize
 * @Date 2025/7/15 16:35
 * @descript:
 */
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
        return it.lat
    } ?:run {
        return 22.635886
    }


}

/**
 *
 * 返回经纬度信息
 */
fun getLocation(): LocationEntity? {
    Appconfig.location ?.let {
        return it
    } ?:run {
        Appconfig.location= CacheUtils.getDataInfo(CacheKey.CURRENTLOCATION, LocationEntity::class.java)
        return Appconfig.location
    }
}

/**
 * 返回经度
 */
fun getLongitude():Double {
    Appconfig.location?.let {
        return it.lng
    } ?:run {
        return 114.062945
    }
}

