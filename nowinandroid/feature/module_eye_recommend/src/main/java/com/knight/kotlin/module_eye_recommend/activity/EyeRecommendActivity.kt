package com.knight.kotlin.module_eye_recommend.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.entity.EyeApiRequest
import com.knight.kotlin.library_base.entity.EyeCardDataEntity
import com.knight.kotlin.library_base.entity.EyeCardEntity
import com.knight.kotlin.library_base.entity.EyeMetroCard
import com.knight.kotlin.library_base.entity.EyeMetroList
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_eye_recommend.R
import com.knight.kotlin.module_eye_recommend.adapter.EyeRecommendAdapter
import com.knight.kotlin.module_eye_recommend.databinding.EyeRecommendActivityBinding
import com.knight.kotlin.module_eye_recommend.vm.EyeRecommendVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive


/**
 * @author created by luguian
 * @organize 
 * @Date 2025/5/22 10:56
 * @descript:开眼推荐主页面
 */
@AndroidEntryPoint
@Route(path = RouteActivity.EyeRecommend.EyeRecommendActivity)
class EyeRecommendActivity: BaseActivity<EyeRecommendActivityBinding, EyeRecommendVm>(), OnRefreshListener, OnLoadMoreListener {

    //下一页Url请求连接
    private  var api_request : EyeApiRequest? = null
    private lateinit var map : MutableMap<String,String>
    //适配器
    private val mEyeRecommendAdater: EyeRecommendAdapter by lazy {
        EyeRecommendAdapter(this,mutableListOf())
    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
         mBinding.eyeRecommendRefreshLayout.baseFreshlayout.autoRefresh()
         mViewModel.getRecommendData("card","recommend").observerKt {
             mBinding.eyeRecommendRefreshLayout.baseFreshlayout.finishRefresh()
             //后续处理 要筛选掉广告，因为广告图片打不开
               api_request = it.list?.last()?.card_data?.body?.api_request
             map = api_request?.params?.mapValues { param ->
                 param.value.jsonPrimitive.content
             }?.toMutableMap() ?: mutableMapOf()
             mEyeRecommendAdater.submitList(it.list?.filter { it.card_data?.body?.api_request == null })
         }
    }

    override fun reLoadData() {

    }

    override fun EyeRecommendActivityBinding.initView() {
        mBinding.title = getString(R.string.eye_recommend_video_title)
        eyeCommendToolbar.baseIvBack.setOnClick { finish() }
        eyeRecommendRefreshLayout.baseBodyRv.init(
                LinearLayoutManager(this@EyeRecommendActivity),
        mEyeRecommendAdater,
        true
        )
        eyeRecommendRefreshLayout.baseFreshlayout.setOnRefreshListener(this@EyeRecommendActivity)
        eyeRecommendRefreshLayout.baseFreshlayout.setOnLoadMoreListener(this@EyeRecommendActivity)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        initRequestData()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mViewModel.getEyeRecommendMoreData(api_request!!.url,map).observerKt {
            it.list?.let {
                //获取更多的推荐视频需要特殊处理 因为模型不一样
                val resultList : MutableList<EyeMetroCard<JsonObject>> = mutableListOf()
                resultList.addAll(it)

                val eyeMetroList: EyeMetroList = EyeMetroList()
                eyeMetroList.metro_list = resultList

                val eyeCardDataEntity: EyeCardDataEntity = EyeCardDataEntity()
                eyeCardDataEntity.body = eyeMetroList

                val eyeCardEntity : EyeCardEntity = EyeCardEntity()
                eyeCardEntity.card_data = eyeCardDataEntity
                val smallVideoLists  = mutableListOf<EyeCardEntity>()
                smallVideoLists.add(eyeCardEntity)
                mEyeRecommendAdater.addAll(smallVideoLists)
            }
            mBinding.eyeRecommendRefreshLayout.baseFreshlayout.finishLoadMore()
            it.last_item_id.takeIf {
                it.isNotEmpty()
            } ?.let { lastItemId ->
                api_request!!.params.also {
                    map["last_item_id"] = lastItemId
                }
            }
        }
    }
}