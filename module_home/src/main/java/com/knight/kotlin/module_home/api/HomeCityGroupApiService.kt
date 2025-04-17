package com.knight.kotlin.module_home.api

import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_home.entity.GroupCityListBean
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/17 16:40
 * @descript:城市分组获取接口
 */
interface HomeCityGroupApiService {

    /**
     *
     * 获取一周详细天气
     */
    @Headers("Domain-Name:weather")
    @GET("city/group")
    suspend fun getCityGroupData(
        @Query("source") source: String
    ): BaseResponse<List<GroupCityListBean>>
}