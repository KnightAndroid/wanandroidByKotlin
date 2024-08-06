package com.knight.kotlin.module_eye_discover.repo

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.knight.kotlin.library_base.ktx.dimissLoadingDialog
import com.knight.kotlin.library_base.ktx.fromJson
import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.enum.ResponseExceptionEnum
import com.knight.kotlin.library_network.exception.ResponseException
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_eye_discover.api.EyeDiscoverApi
import com.knight.kotlin.module_eye_discover.entity.BaseEyeDiscoverEntity
import com.knight.kotlin.module_eye_discover.entity.EyeDiscoverBannerEntity
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/6 9:29
 * @descript:开眼发现模块 Repo
 */
class EyeDiscoverRepo @Inject constructor() : BaseRepository(){
    @Inject
    lateinit var mEyeDiscoverApi : EyeDiscoverApi


    /**
     * 获取发现数据
     *
     */
    fun getDiscoverData (failureCallBack:((String?) ->Unit) ?= null) = request<List<BaseEyeDiscoverEntity>>({
        mEyeDiscoverApi.getDiscoverData().run {
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
                //val jsonObject: JSONObject = JSONObject(this)

                val itemList = this.getAsJsonArray("itemList")

                for (i in 0 until itemList.size()) {
                    val ccurrentObject = itemList.get(i).asJsonObject
                    when (ccurrentObject.get("type").asString) {
                        "horizontalScrollCard" -> {
                            val topBannerBean: EyeDiscoverBannerEntity = fromJson(ccurrentObject.toString())
                            discoverLists.add(topBannerBean)
                        }

                        "specialSquareCardCollection" -> {
//                        val categoryCardBean: CategoryCardBean = GsonUtils.fromLocalJson(ccurrentObject.toString(), CategoryCardBean::class.java)
//                        viewModels.add(categoryCardBean)
                        }

                        "columnCardList" -> {
//                        val subjectCardBean: SubjectCardBean = GsonUtils.fromLocalJson(ccurrentObject.toString(), SubjectCardBean::class.java)
//                        viewModels.add(subjectCardBean)
                        }

                        "textCard" -> {
//                        val textCardbean: TextCardbean = GsonUtils.fromLocalJson(ccurrentObject.toString(), TextCardbean::class.java)
//                        val titleViewModel: TitleViewModel = TitleViewModel()
//                        titleViewModel.title = textCardbean.getData().getText()
//                        titleViewModel.actionTitle = textCardbean.getData().getRightText()
//                        viewModels.add(titleViewModel)
                        }

                        "banner" -> {
//                        val bannerBean: BannerBean = GsonUtils.fromLocalJson(ccurrentObject.toString(), BannerBean::class.java)
//                        val bannerViewModel: ContentBannerViewModel = ContentBannerViewModel()
//                        bannerViewModel.bannerUrl = bannerBean.getData().getImage()
//                        viewModels.add(bannerViewModel)
                        }

                        "videoSmallCard" -> {
//                        val videoSmallCardBean: VideoSmallCardBean = GsonUtils
//                            .fromLocalJson(
//                                ccurrentObject.toString(),
//                                VideoSmallCardBean::class.java
//                            )
//                        paresVideoCard(viewModels, videoSmallCardBean)
                        }

                        "briefCard" -> {
//                        val briefCard: BriefCard = GsonUtils.fromLocalJson(ccurrentObject.toString(), BriefCard::class.java)
//                        val briefCardViewModel: BriefCardViewModel = BriefCardViewModel()
//                        briefCardViewModel.coverUrl = briefCard.getData().getIcon()
//                        briefCardViewModel.title = briefCard.getData().getTitle()
//                        briefCardViewModel.description = briefCard.getData().getDescription()
//                        viewModels.add(briefCardViewModel)
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