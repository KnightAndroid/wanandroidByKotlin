package com.knight.kotlin.module_eye_square.activity

import androidx.recyclerview.widget.GridLayoutManager
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.entity.EyeApiRequest
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_eye_square.adapter.EyeSquareAdapter
import com.knight.kotlin.module_eye_square.databinding.EyeSquareActivityBinding
import com.knight.kotlin.module_eye_square.vm.EyeSquareVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.wyjson.router.annotation.Param
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.jsonPrimitive


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/11 14:15
 * @descript:开眼社区主页
 */
@AndroidEntryPoint
@Route(path = RouteActivity.EyeSquare.EyeSquareActivity)
class EyeSquareActivity : BaseActivity<EyeSquareActivityBinding,EyeSquareVm>(), OnLoadMoreListener {

    @JvmField
    @Param(name = "tabTitle")
    var tabTitle:String = ""

    //下一页Url请求连接
    private  var api_request : EyeApiRequest? = null
    private lateinit var map : MutableMap<String,String>

    //发现适配器
    private val mEyeSquareAdapter: EyeSquareAdapter by lazy { EyeSquareAdapter(
       this,mutableListOf()) }


    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
         mViewModel.getSquareDataByTypeAndLabel("card","community").observerKt {
             api_request = it.list?.last()?.card_data?.body?.api_request
             map = api_request?.params?.mapValues { param ->
                 param.value.jsonPrimitive.content
             }?.toMutableMap() ?: mutableMapOf()
             mEyeSquareAdapter.submitList(it.list?.filter { it.card_data?.body?.api_request == null })

         }
    }

    override fun reLoadData() {

    }

    override fun EyeSquareActivityBinding.initView() {
        mBinding.title = tabTitle
        rvEyeSquareList.init(
            GridLayoutManager(this@EyeSquareActivity, 2).also {
                it.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return mEyeSquareAdapter.convertViewType2SpanSize(position, 2)
                    }
                }
            },
            mEyeSquareAdapter,
            true
        )
        mBinding.eyeSquareListRefreshLayout.setOnLoadMoreListener(this@EyeSquareActivity)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mViewModel.getSquareMoreData(api_request!!.url,map).observerKt {
            it.list?.let {
                mEyeSquareAdapter.addAll(it)
            }
            mBinding.eyeSquareListRefreshLayout.finishLoadMore()
            it.last_item_id.takeIf {
                it.isNotEmpty()
            }?.let {
                    lastItemId -> api_request!!.params.also {
                map["last_item_id"] = lastItemId
            }
            }

        }
    }
}