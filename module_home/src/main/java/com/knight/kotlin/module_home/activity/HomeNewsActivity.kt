package com.knight.kotlin.module_home.activity


import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.ktx.getScreenWidth
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_util.DateUtils
import com.knight.kotlin.library_util.Mp3PlayerUtils
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.library_widget.circleimagebar.CircularMusicProgressBar
import com.knight.kotlin.library_widget.circleimagebar.OnCircularSeekBarChangeListener
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.library_widget.overlaymenu.EasyFloat
import com.knight.kotlin.library_widget.overlaymenu.FloatingMagnetView
import com.knight.kotlin.module_home.R
import com.knight.kotlin.module_home.adapter.HomeNewsAdapter
import com.knight.kotlin.module_home.databinding.HomeNewsActivityBinding
import com.knight.kotlin.module_home.databinding.HomeNewsFootBinding
import com.knight.kotlin.module_home.databinding.HomeNewsHeadBinding
import com.knight.kotlin.module_home.entity.ZaoBaoBean
import com.knight.kotlin.module_home.utils.FloatMenuManager
import com.knight.kotlin.module_home.utils.FloatMenuManager.animateViewWidth
import com.knight.kotlin.module_home.vm.HomeNewsVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/11 15:18
 * @descript:新闻界面
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Home.HomeNewsActivty)
class HomeNewsActivity:BaseActivity<HomeNewsActivityBinding,HomeNewsVm>(), OnRefreshListener {


    private lateinit var mNewsHeaderBinding: HomeNewsHeadBinding
    private lateinit var mNewsFootBinding:HomeNewsFootBinding
    private val mNewsAdapter: HomeNewsAdapter by lazy { HomeNewsAdapter() }
    private lateinit var mp3Url:String
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        mViewModel.getNews().observerKt {
            mp3Url = it.audio
            mBinding.includeNews.baseFreshlayout.finishRefresh()
            requestSuccess()
            initHeaderView(it)
        }
    }

    override fun reLoadData() {

    }

    override fun HomeNewsActivityBinding.initView() {
        FloatMenuManager.showNormal(this@HomeNewsActivity, R.layout.home_float_news_menu)
        initFloatViewActions()
        mBinding.title = getString(com.knight.kotlin.module_home.R.string.home_tv_zaobao)
        requestLoading(mBinding.includeNews.baseFreshlayout)
        includeNews.baseFreshlayout.setEnableLoadMore(false)
        includeNews.baseBodyRv.init(
            LinearLayoutManager(this@HomeNewsActivity),
            mNewsAdapter,
            true
        )
        includeNews.baseFreshlayout.setOnRefreshListener(this@HomeNewsActivity)
        includeNewsToolbar.baseIvBack.setOnClick {
            finish()
        }

    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        initRequestData()
    }


    @OptIn(UnstableApi::class)
    private fun initFloatViewActions() {
        val floatMagnetView: FloatingMagnetView? = EasyFloat.getCustomView()
        val play = floatMagnetView?.findViewById<ImageView>(R.id.iv_float_play)
        val llRoot = floatMagnetView?.findViewById<LinearLayout>(R.id.ll_root_float_menu)
        val iv_float_img = floatMagnetView?.findViewById<CircularMusicProgressBar>(R.id.iv_float_img)
        //iv_float_img?.setValue(0f)

        var rootWidth = 0
        llRoot?.post {
            rootWidth = llRoot.width
        }

        play?.setOnClickListener {
            Mp3PlayerUtils.getInstance().smartTogglePlay(mp3Url, onProgress = { pos, dur ->
                iv_float_img?.setValue((pos * 100 / dur).toInt().toFloat())
                                                                              // 这里可以添加播放进度更新逻辑
            }, onStateChanged = { isPlaying ->
                if (isPlaying) {
                    play.setImageResource(R.drawable.home_news_play_icon)
                } else {
                    play.setImageResource(R.drawable.home_news_pause_icon)
                }

            })

        }

        iv_float_img?.setOnCircularBarChangeListener(object :OnCircularSeekBarChangeListener{
            override fun onProgressChanged(circularBar: CircularMusicProgressBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onClick(circularBar: CircularMusicProgressBar?) {
                if (llRoot?.width == (iv_float_img.width + 10.dp2px())) {
                    animateViewWidth(llRoot!!, rootWidth)
                } else {
                    animateViewWidth(llRoot!!, iv_float_img.width + 10.dp2px())
                }
            }

            override fun onLongPress(circularBar: CircularMusicProgressBar?) {

            }
        })

    }

    /**
     * 初始化头像
     */
    private fun initHeaderView(zaoBao: ZaoBaoBean) {
        if (mBinding.includeNews.baseBodyRv.headerCount == 0 || mBinding.includeNews.baseBodyRv.footerCount == 0) {
            if (!::mNewsHeaderBinding.isInitialized) {
                mNewsHeaderBinding =
                HomeNewsHeadBinding.inflate(LayoutInflater.from(this@HomeNewsActivity))
                ImageLoader.loadImageWithAdaptiveSize(mNewsHeaderBinding.homeNewsHeadIv, getScreenWidth(), 0,zaoBao.head_image,{
                        width,height->
                    mNewsAdapter.submitList(zaoBao.news)
                    mBinding.includeNews.baseBodyRv.addHeaderView(mNewsHeaderBinding.root)

                })
                mNewsHeaderBinding.tvNewsDate.text = DateUtils.formatDate(zaoBao.date,"yyyy-MM-dd", "yyyy年M月d日")
            }

            if (!::mNewsFootBinding.isInitialized) {
                mNewsFootBinding =
                    HomeNewsFootBinding.inflate(LayoutInflater.from(this@HomeNewsActivity))
                mBinding.includeNews.baseBodyRv.addFooterView(mNewsFootBinding.root)
                mNewsFootBinding.weiyu = zaoBao.weiyu
            }
        } else {
            mNewsAdapter.submitList(zaoBao.news)
        }
    }


}