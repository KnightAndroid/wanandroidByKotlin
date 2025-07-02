package com.knight.kotlin.module_eye_discover.api

import com.knight.kotlin.library_base.entity.EyeCardListEntity
import com.knight.kotlin.library_network.bean.EyeApiResponse
import com.knight.kotlin.module_eye_discover.entity.EyeHotQueriesEntity
import retrofit2.http.GET
import retrofit2.http.Headers


/**
 * @author created by luguian
 * @organize
 * @Date 2024/12/11 11:09
 * @descript:推荐搜索
 */
interface EyeDiscoverSearchRecommendApi {


    @Headers("Domain-Name:eye_sub")
    @GET("v1/recommend/search/get_hot_queries")
    suspend fun getHotQueries(): EyeApiResponse<EyeHotQueriesEntity>

    @Headers("Domain-Name:eye_sub")
    @GET("v1/search/search/get_search_recommend_card_list")
    suspend fun getRecommendCardList():EyeApiResponse<EyeCardListEntity>



}