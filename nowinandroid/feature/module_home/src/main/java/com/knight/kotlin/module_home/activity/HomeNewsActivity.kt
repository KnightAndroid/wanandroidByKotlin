package com.knight.kotlin.module_home.activity


import XXPermissions
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.ktx.getScreenWidth
import com.knight.kotlin.library_permiss.OnPermissionCallback
import com.knight.kotlin.library_permiss.permission.PermissionLists
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_util.DateUtils
import com.knight.kotlin.library_util.DialogUtils
import com.knight.kotlin.library_util.Mp3PlayerUtils
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.library_widget.circleimagebar.CircularMusicProgressBar
import com.knight.kotlin.library_widget.ktx.init
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
class HomeNewsActivity: BaseActivity<HomeNewsActivityBinding, HomeNewsVm>(), OnRefreshListener {


    private lateinit var mNewsHeaderBinding: HomeNewsHeadBinding
    private lateinit var mNewsFootBinding:HomeNewsFootBinding
    private val mNewsAdapter: HomeNewsAdapter by lazy { HomeNewsAdapter() }
    private lateinit var mp3Url:String
    private var showFloatMenu:Boolean = false

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        mViewModel.getNews("json").observerKt {

            mBinding.includeNews.baseFreshlayout.finishRefresh()
            requestSuccess()
            initHeaderView(it)
        }

        mViewModel.getAudio().observerKt {
            mp3Url = it.audio
        }
    }

    override fun reLoadData() {

    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun HomeNewsActivityBinding.initView() {
        includeNewsToolbar.baseIvRight.setImageResource(R.drawable.home_news_float_menu_icon)
        includeNewsToolbar.baseIvRight.visibility = View.VISIBLE
            // FloatMenuManager.initNormal(this@HomeNewsActivity, R.layout.home_float_news_menu)
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
        includeNewsToolbar.baseIvRight.setOnClick {
            if (showFloatMenu) {
                FloatMenuManager.hidden()
            } else {
                if (FloatMenuManager.checkInitialized()) {
                    DialogUtils.getConfirmDialog(this@HomeNewsActivity,getString(R.string.home_open_floatmenu_title),
                        getString(R.string.home_open_floatmenu_message),getString(R.string.home_in_app_floating_window),
                        getString(R.string.home_out_of_app_floating_window),
                        { dialog, which ->
                            FloatMenuManager.initFloatMenu(this@HomeNewsActivity,initFloatViewActions(),false)
                            FloatMenuManager.show()
                            showFloatMenu = !showFloatMenu
                        }) {
                            dialog, which ->

                        val permission: List<IPermission> = listOf(PermissionLists.getSystemAlertWindowPermission())
                        if (XXPermissions.isGrantedPermissions(this@HomeNewsActivity,permission)) {
                            FloatMenuManager.initFloatMenu(this@HomeNewsActivity,initFloatViewActions(),true)
                            FloatMenuManager.show()
                        } else {
                            XXPermissions.with(this@HomeNewsActivity)
                                .permission(PermissionLists.getSystemAlertWindowPermission())
                                .request(object : OnPermissionCallback {


                                    override fun onDenied(permissions: List<IPermission>, doNotAskAgain: Boolean) {
                                        if (doNotAskAgain) {
                                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                                            XXPermissions.startPermissionActivity(this@HomeNewsActivity, permissions)
                                        } else {
                                            toast(getString(R.string.home_miss_float_window))
                                        }

                                    }

                                    override fun onGranted(permissions: List<IPermission>, allGranted: Boolean) {
                                        if (allGranted) {
                                            FloatMenuManager.initFloatMenu(this@HomeNewsActivity,initFloatViewActions(),true)
                                            FloatMenuManager.show()
                                            showFloatMenu = !showFloatMenu
                                        }
                                    }
                                })

                        }

                    }

                } else {
                    FloatMenuManager.show()
                }
            }

        }



    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        initRequestData()
    }


    @OptIn(UnstableApi::class)
    private fun initFloatViewActions():View {
        val view = LayoutInflater.from(this@HomeNewsActivity).inflate(R.layout.home_float_news_menu, null)
        val close = view!!.findViewById<View>(R.id.iv_float_close)
        val play = view!!.findViewById<ImageView>(R.id.iv_float_play)
        val llRoot = view!!.findViewById<LinearLayout>(R.id.ll_float_menu)
        val iv_float_img = view!!.findViewById<CircularMusicProgressBar>(R.id.iv_float_img)
        close.setOnClickListener {
            FloatMenuManager.hidden()
        }

        var rootWidth = 0
        llRoot?.post {
            rootWidth = llRoot.width
        }

        play?.setOnClickListener {
            if (::mp3Url.isInitialized) {
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
            } else {
                toast(R.string.home_mp3url_empty_hint)
            }


        }

        iv_float_img?.setOnClick {
            if (llRoot?.width == 0) {
                animateViewWidth(llRoot!!, true,rootWidth)
            } else {
                animateViewWidth(llRoot!!, false,rootWidth)
            }
        }

        return view
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



    override fun onDestroy() {
        FloatMenuManager.getFloatWindow()?.let {
            if (!it.getDesktopWindow()) {
                FloatMenuManager.destroyFloatMenu()
            }
        }
        super.onDestroy()
    }


}