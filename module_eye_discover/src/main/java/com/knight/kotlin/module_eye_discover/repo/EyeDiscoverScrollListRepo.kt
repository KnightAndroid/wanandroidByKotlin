package com.knight.kotlin.module_eye_discover.repo

import com.knight.kotlin.library_base.ktx.dimissLoadingDialog
import com.knight.kotlin.library_base.ktx.fromJson
import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.enum.ResponseExceptionEnum
import com.knight.kotlin.library_network.exception.ResponseException
import com.knight.kotlin.module_eye_discover.api.EyeDiscoverScollListApi
import com.knight.kotlin.module_eye_discover.entity.BaseEyeDiscoverEntity
import com.knight.kotlin.module_eye_discover.entity.EyeCategoryCardEntity
import com.knight.kotlin.module_eye_discover.entity.EyeDiscoverBriefCardEntity
import com.knight.kotlin.module_eye_discover.entity.EyeDiscoverMiddleBannerEntity
import com.knight.kotlin.module_eye_discover.entity.EyeDiscoverTopBannerEntity
import com.knight.kotlin.module_eye_discover.entity.EyeDiscoverVideoSmallCardEntity
import com.knight.kotlin.module_eye_discover.entity.EyeSubTitleEntity
import com.knight.kotlin.module_eye_discover.entity.EyeSubjectCardEntity
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/6 9:29
 * @descript:开眼发现模块 Repo
 */
class EyeDiscoverScrollListRepo @Inject constructor() : BaseRepository(){
    @Inject
    lateinit var mEyeDiscoverScollListApi : EyeDiscoverScollListApi


    /**
     * 获取发现数据
     *
     */
    fun getDiscoverData (failureCallBack:((String?) ->Unit) ?= null) = request<List<BaseEyeDiscoverEntity>>({
        mEyeDiscoverScollListApi.getDiscoverData().run {
            val jsonErrorCodeElement  =  this.get("errorCode")
            val jsonErrorMsgElement = this.get("errorMessage")
            jsonErrorCodeElement?.run {
                    throw ResponseException(
                        ResponseExceptionEnum.ERROR,
                        jsonErrorMsgElement.asString
                    )
            } ?: run {
                dimissLoadingDialog()
                val discoverLists = mutableListOf<BaseEyeDiscoverEntity>()
                val itemList = this.getAsJsonArray("itemList")
                for (i in 0 until itemList.size()) {
                    val ccurrentObject = itemList.get(i).asJsonObject
                    when (ccurrentObject.get("type").asString) {
                        "horizontalScrollCard" -> {
                            val topBannerBean: EyeDiscoverTopBannerEntity = fromJson(ccurrentObject.toString())
                            discoverLists.add(topBannerBean)
                        }

                        "specialSquareCardCollection" -> {
                            val categoryCardBean: EyeCategoryCardEntity = fromJson(ccurrentObject.toString())
                            discoverLists.add(categoryCardBean)
                        }

                        "columnCardList" -> {
                            val subjectCardBean: EyeSubjectCardEntity = fromJson(ccurrentObject.toString())
                            discoverLists.add(subjectCardBean)
                        }

                        "textCard" -> {
                            val subTitleBean: EyeSubTitleEntity = fromJson(ccurrentObject.toString())
                            discoverLists.add(subTitleBean)
                        }

                        "banner" -> {
                            val middleBannerBean : EyeDiscoverMiddleBannerEntity = fromJson(ccurrentObject.toString())
                            discoverLists.add(middleBannerBean)
                        }

                        "videoSmallCard" -> {

                            val videoSmallCardEntity : EyeDiscoverVideoSmallCardEntity = fromJson(ccurrentObject.toString())
                            discoverLists.add(videoSmallCardEntity)
                        }

                        "briefCard" -> {
                            val briefCardEntity : EyeDiscoverBriefCardEntity = fromJson(ccurrentObject.toString())
                            discoverLists.add(briefCardEntity)
                        }

                        else -> {}
                    }
                }

                emit(discoverLists)
            }

        }
    }){
        dimissLoadingDialog()
        failureCallBack?.run {
            this(it)
        }
    }

}