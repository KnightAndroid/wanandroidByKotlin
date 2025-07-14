package com.knight.kotlin.module_eye_discover.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
import com.knight.kotlin.library_base.activity.BaseActivity
import com.core.library_base.ktx.init
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.core.library_common.dp2px
import com.knight.kotlin.library_video.play.OkPlayer
import com.knight.kotlin.library_video.play.OkPlayerStd
import com.knight.kotlin.library_widget.RecyclerItemDecoration
import com.knight.kotlin.module_eye_discover.R
import com.knight.kotlin.module_eye_discover.adapter.EyeDiscoverSpecialTopicDetailAdapter
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverSpecialTopicActivityBinding
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverSpecialTopicHeaderBinding
import com.knight.kotlin.module_eye_discover.entity.EyeSpecialTopicDetailEntity
import com.knight.kotlin.module_eye_discover.okplaystd.ViewAttr
import com.knight.kotlin.module_eye_discover.utils.AutoPlayUtils
import com.knight.kotlin.module_eye_discover.vm.EyeDiscoverSpecialTopicVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Param
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/19 11:04
 * @descript:专题详细页面
 */
@AndroidEntryPoint
@Route(path = RouteActivity.EyeDiscover.EyeSpecialTopicDetailActivity)
class EyeSpecialTopicDetailActivity : BaseActivity<EyeDiscoverSpecialTopicActivityBinding, EyeDiscoverSpecialTopicVm>(),EyeDiscoverSpecialTopicDetailAdapter.OnVideoClick,
    OnRefreshListener {

    @JvmField
    @Param(name = "id")
    var id:Long = 0


    //专题页面适配器
    private val mEyeDiscoverSpecialTopicDetailAdapter: EyeDiscoverSpecialTopicDetailAdapter by lazy { EyeDiscoverSpecialTopicDetailAdapter(
        this,
        this) }


    private lateinit var mHeaderBinding: EyeDiscoverSpecialTopicHeaderBinding
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        getTopicDetail()
    }

    override fun reLoadData() {

    }

    override fun EyeDiscoverSpecialTopicActivityBinding.initView() {
        specialTopicRefreshLayout.setOnRefreshListener(this@EyeSpecialTopicDetailActivity)
        initRecyclerView()
        onBackPressed(true){
            OkPlayer.backPress()
            finishAfterTransition()
        }
        specialTopicDetailToolbar.baseIvBack.setOnClick {
            OkPlayer.backPress()
            finishAfterTransition()
        }


    }

    override fun videoClick(focusView: ViewGroup, viewAttr: ViewAttr, position: Int) {

    }


    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this@EyeSpecialTopicDetailActivity)
        mBinding.rvSpecialTopicVideo.init(
            layoutManager,
            mEyeDiscoverSpecialTopicDetailAdapter,
            true
        )

        mBinding.rvSpecialTopicVideo.addItemDecoration(
            RecyclerItemDecoration(
            0,
            0, 0, 0,  ContextCompat.getColor(this, com.core.library_base.R.color.base_line_color),5.dp2px(),0
        )
        )
        mBinding.rvSpecialTopicVideo.addOnChildAttachStateChangeListener(object :
            OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {}
            override fun onChildViewDetachedFromWindow(view: View) {
                //当Item视图不可见时，暂停视频播放
                val jzvd = view.findViewById<OkPlayer>(R.id.eye_discover_jzvdplayer)
                if (jzvd != null && OkPlayer.CURRENT_JZVD != null && jzvd.jzDataSource.containsTheUrl(
                        OkPlayer.CURRENT_JZVD.jzDataSource.currentUrl
                    )
                ) {
                    if (OkPlayer.CURRENT_JZVD != null && OkPlayer.CURRENT_JZVD.screen != OkPlayer.SCREEN_FULLSCREEN) {
                        OkPlayer.releaseAllVideos()
                    }
                }
            }
        })

        mBinding.rvSpecialTopicVideo.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //当列表停止滚动后，获取第一个可见Item以及最后一个可见Item的下标
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    AutoPlayUtils.onScrollPlayVideo(
                        recyclerView,
                        R.id.cl_topic,
                        R.id.eye_discover_jzvdplayer,
                        layoutManager.findFirstVisibleItemPosition(),
                        layoutManager.findLastVisibleItemPosition()
                    )
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy != 0) {
                    AutoPlayUtils.onScrollReleaseAllVideos(
                        layoutManager.findFirstVisibleItemPosition(),
                        layoutManager.findLastVisibleItemPosition(),
                        0.2f
                    )
                }
            }
        })
    }


    /**
     * 手势返回
     *
     *
     * @param isEnabled
     * @param callback
     */
    fun AppCompatActivity.onBackPressed(isEnabled: Boolean, callback: () -> Unit) {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(isEnabled) {
            override fun handleOnBackPressed() {
                callback()

            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        AutoPlayUtils.positionInList = -1
        OkPlayerStd.releaseAllVideos()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        getTopicDetail()
    }

    private fun initHeaderView(topicDetailModel: EyeSpecialTopicDetailEntity?) {
        if (mBinding.rvSpecialTopicVideo.headerCount == 0) {
            if (!::mHeaderBinding.isInitialized) {
                topicDetailModel?.let { model ->
                    mBinding.title = model.brief

                        mHeaderBinding =
                            EyeDiscoverSpecialTopicHeaderBinding.inflate(LayoutInflater.from(this))
                        mHeaderBinding!!.model = model
                        mBinding.rvSpecialTopicVideo.addHeaderView(mHeaderBinding!!.root)
                }
            } else {
                mHeaderBinding?.model = topicDetailModel
            }
        }


    }
    private fun getTopicDetail() {
        mViewModel.getDiscoverSpecialTopicDetail(id).observerKt { topicDetailModel ->
            mBinding.specialTopicRefreshLayout.finishRefresh()
            initHeaderView(topicDetailModel)
            mEyeDiscoverSpecialTopicDetailAdapter.submitList(topicDetailModel.itemList)
        }
    }
}