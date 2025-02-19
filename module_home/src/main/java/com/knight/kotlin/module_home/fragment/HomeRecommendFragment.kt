package com.knight.kotlin.module_home.fragment

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.flyjingfish.android_aop_core.annotations.SingleClick
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.knight.kotlin.library_aop.loginintercept.LoginCheck
import com.knight.kotlin.library_base.annotation.EventBusRegister
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.config.CacheKey
import com.knight.kotlin.library_base.entity.BaiduContent
import com.knight.kotlin.library_base.entity.LoginEntity
import com.knight.kotlin.library_base.entity.UserInfoEntity
import com.knight.kotlin.library_base.event.MessageEvent
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.ktx.getUser
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.ktx.toHtml
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.util.ArouteUtils
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.ColorUtils
import com.knight.kotlin.library_base.util.EventBusUtils
import com.knight.kotlin.library_base.util.GsonUtils
import com.knight.kotlin.library_base.util.LanguageFontSizeUtils
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_common.entity.OfficialAccountEntity
import com.knight.kotlin.library_database.entity.PushDateEntity
import com.knight.kotlin.library_permiss.XXPermissions
import com.knight.kotlin.library_permiss.listener.OnPermissionCallback
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.utils.PermissionUtils
import com.knight.kotlin.library_scan.activity.ScanCodeActivity
import com.knight.kotlin.library_scan.annoation.ScanStyle
import com.knight.kotlin.library_scan.decode.ScanCodeConfig
import com.knight.kotlin.library_util.DateUtils
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.library_util.startPage
import com.knight.kotlin.library_util.toast.ToastUtils
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.library_widget.skeleton.Skeleton
import com.knight.kotlin.library_widget.skeleton.SkeletonScreen
import com.knight.kotlin.module_home.R
import com.knight.kotlin.module_home.adapter.BaiduHotSearchAdapter
import com.knight.kotlin.module_home.adapter.HomeArticleAdapter
import com.knight.kotlin.module_home.adapter.OfficialAccountAdapter
import com.knight.kotlin.module_home.databinding.HomeRecommendFragmentBinding
import com.knight.kotlin.module_home.dialog.HomePushArticleFragment
import com.knight.kotlin.module_home.entity.BannerBean
import com.knight.kotlin.module_home.entity.EveryDayPushArticlesBean
import com.knight.kotlin.module_home.entity.HomeArticleListBean
import com.knight.kotlin.module_home.utils.HomeAnimUtils
import com.knight.kotlin.module_home.vm.HomeRecommendVm
import com.knight.library_biometric.control.BiometricControl
import com.knight.library_biometric.listener.BiometricStatusCallback
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.GoRouter
import com.wyjson.router.annotation.Route
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException


/**
 * Author:Knight
 * Time:2021/12/29 15:46
 * Description:HomeRecommendFragment
 */
@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteFragment.Home.RecommendFragment)
class HomeRecommendFragment : BaseFragment<HomeRecommendFragmentBinding, HomeRecommendVm>(),
    OnRefreshListener, OnLoadMoreListener {

    //百度热搜适配器
    private val mBaiduHotSearchAdapter : BaiduHotSearchAdapter by lazy {BaiduHotSearchAdapter()}
    //推荐文章适配器
    private val mHomeArticleAdapter: HomeArticleAdapter by lazy { HomeArticleAdapter() }

    //公众号适配器
    private val mOfficialAccountAdapter: OfficialAccountAdapter by lazy {
        OfficialAccountAdapter()
    }

    //头部View
    private val recommendHeadView: View by lazy {
        LayoutInflater.from(requireActivity()).inflate(R.layout.home_recommend_head, null)
    }

    //信息布局
    private lateinit var home_rl_message: RelativeLayout

    //未读消息
    private lateinit var home_tv_unread_message: TextView

    //公众号列表
    private lateinit var home_rv_official_account: SwipeRecyclerView

    //头部的尾部
    private val topArticleFootView: View by lazy {
        LayoutInflater.from(requireActivity()).inflate(R.layout.home_toparticle_foot, null)
    }
    /**
     * 获取推送文章
     */
    private lateinit var mEveryDayPushData: EveryDayPushArticlesBean
    //Banner布局
    private lateinit var mBanner: Banner<BannerBean, BannerImageAdapter<BannerBean>>

    //首页文章数据请求页码
    private var currentPage = 0

    //选择收藏/取消收藏的Item项
    private var selectItem = -1



    //置顶文章是否只显示三条
    private var isShowOnlythree = true

    //头部view中的recycleview
    private lateinit var home_top_article_rv: SwipeRecyclerView

    private var mSkeletonScreen: SkeletonScreen? = null

    //动画集合,用来控制动画的有序播放
    private var animatorSet: AnimatorSet? = null

    //圆的半径
    private var radius: Int = 0

    //FloatingActionButton宽度和高度，宽高一样
    private var width: Int = 0


    private var firstItemHeight:Int = 0
    private var totalHeight:Int = 0
    val home_tv_seemorearticles : TextView by lazy {
        recommendHeadView.findViewById(R.id.home_tv_seemorearticles)
    }

    /**
     * 展开后的数据
     *
     */
    private var expandRealTimeList = mutableListOf<BaiduContent>()
    /**
     * 知识标签
     */
    private var knowledgeLabelList = mutableListOf<String>()
    override fun setThemeColor(isDarkMode: Boolean) {
        if (!isDarkMode) {
            isWithStatusTheme(CacheUtils.getStatusBarIsWithTheme())
        }
    }

    override fun HomeRecommendFragmentBinding.initView() {
        mBinding.root.rotation = 180f
        homeRecommentConent.homeIconFab.imageTintList = null
        mSkeletonScreen = Skeleton.bind(mBinding.homeRecommentConent.rlHome)
            .load(R.layout.home_skeleton_activity)
            .duration(1200)
            .angle(0)
            .show()
        bindHeadView()
        initOfficialListener()
        initArticleListener()
        setOnClickListener(homeRecommentConent.homeIncludeToolbar!!.homeScanIcon,homeRecommentConent.homeIncludeToolbar.homeIvLoginname,
            homeRecommentConent.homeIncludeToolbar.homeIvEveryday,
            homeRecommentConent.homeIncludeToolbar.homeIvAdd,homeRecommentConent.homeIncludeToolbar.homeRlSearch,homeRecommentConent.homeIconFab,homeRecommentConent.homeIconCourse!!,homeRecommentConent.homeIconUtils!!,homeRecommentConent.homeIconScrollUp!!)
//        getUser()?.let {
//            homeRecommentConent.homeIncludeToolbar.homeTvLoginname.text = it.username
//        } ?: kotlin.run {
//            homeRecommentConent.homeIncludeToolbar.homeTvLoginname.text = getString(R.string.home_tv_login)
//        }
        homeRecommentConent.homeIconFab.backgroundTintList = ColorUtils.createColorStateList(themeColor, themeColor)
        homeRecommentConent.homeIconCourse.backgroundTintList = ColorUtils.createColorStateList(themeColor, themeColor)
        homeRecommentConent.homeIconUtils.backgroundTintList = ColorUtils.createColorStateList(themeColor, themeColor)
        homeRecommentConent.homeIconScrollUp.backgroundTintList =
            ColorUtils.createColorStateList(themeColor, themeColor)
        homeRecommentConent.homeRecommendArticleBody.init(
            LinearLayoutManager(requireActivity()),
            mHomeArticleAdapter,
            true
        )

        homeRecommentConent.recommendRefreshLayout.setOnLoadMoreListener(this@HomeRecommendFragment)
        homeRecommentConent.recommendRefreshLayout.setOnRefreshListener(this@HomeRecommendFragment)



        setViewVisible(false)
        radius = 80.dp2px()

    }


    override fun onResume() {
        super.onResume()
        mBinding.homeRecommentConent.homeIconFab.post {
            width = mBinding.homeRecommentConent.homeIconFab.measuredWidth
        }
    }

    override fun initRequestData() {
        currentPage = 0
        home_tv_seemorearticles.text = getString(R.string.home_toparticle_expand_foot)
        isShowOnlythree = true
        mViewModel.getEveryDayPushArticle().observerKt {
             setEveryDayPushArticle(it)
        }
        //未读信息请求
        getUser()?.let {
            mViewModel.getUnreadMessage().observerKt {
                setUnreadMessage(it)
            }
        }
        //获取置顶文章
//        mViewModel.getTopArticle().observerKt {
//            setTopArticle(it)
//        }
        //获取百度热搜
        mViewModel.getTopBaiduRealTime().observerKt {
            setTopBaiduRealTime(it.cards[0].content)
        }
        //获取banner
        mViewModel.getBanner().observerKt {
            setBanner(it)
        }
        //获取公众号
        mViewModel.getOfficialAccount().observerKt {
            setOfficialAccount(it)
        }
    }

    override fun initObserver() {

    }


    /**
     * 初始化置顶适配器
     * 点击事件
     */
//    private fun initTopAdapter() {
//        mTopArticleAdapter.run {
//            setOnItemClickListener { adapter, view, position ->
//                ArouteUtils.startWebArticle(
//                    items[position].link,
//                    items[position].title,
//                    items[position].id,
//                    items[position].collect,
//                    items[position].envelopePic,
//                    items[position].desc,
//                    items[position].chapterName,
//                    items[position].author,
//                    items[position].shareUser
//                )
//
//            }
//
//            setOnItemLongClickListener { adapter, view, position ->
//                BlurBuilderUtils.snapShotWithoutStatusBar(requireActivity())
//                startActivity(
//                    Intent(activity, HomeArticlesTabActivity::class.java)
//                        .putParcelableArrayListExtra("toparticles", ArrayList(items))
//                )
//                activity?.overridePendingTransition(
//                    com.knight.kotlin.library_base.R.anim.base_scalealpha_in,
//                    com.knight.kotlin.library_base.R.anim.base_scalealpha_slient
//                )
//                false
//            }
//        }
//    }

    /**
     *
     * 绑定头部布局
     */
    private fun bindHeadView() {
        home_rl_message = recommendHeadView.findViewById(R.id.home_rl_message)
        home_tv_unread_message = recommendHeadView.findViewById(R.id.home_tv_unread_message)
        home_top_article_rv = recommendHeadView.findViewById(R.id.home_top_article_rv)
        mBanner = recommendHeadView.findViewById(R.id.home_banner)
        home_rv_official_account = recommendHeadView.findViewById(R.id.home_rv_official_account)
        val home_iv_toparticlearrow:ImageView = recommendHeadView.findViewById(R.id.home_iv_toparticlearrow)
        home_rl_message.setOnClickListener {
            startPage(RouteActivity.Message.MessageActivity)
        }

        recommendHeadView.findViewById<TextView>(R.id.tv_real_time_more).setOnClick {
            startPage(RouteActivity.RealTime.RealTimeMainActivity)
        }
        //home_top_article_rv.init(LinearLayoutManager(requireActivity()), mTopArticleAdapter)
        home_top_article_rv.init(LinearLayoutManager(requireActivity()), mBaiduHotSearchAdapter)
        recommendHeadView.findViewById<LinearLayout>(R.id.home_ll_seemorearticles)
            .setOnClickListener {
                //展开收缩逻辑 mTopArticleAdapter
                HomeAnimUtils.setArrowAnimate(
                    mBaiduHotSearchAdapter,
                    home_iv_toparticlearrow,
                    isShowOnlythree
                )
                if (isShowOnlythree) {
                    home_tv_seemorearticles.text = getString(R.string.home_toparticle_collapse_foot)
                    if (mBaiduHotSearchAdapter.items.size < 5) {
                        mBaiduHotSearchAdapter.submitList(expandRealTimeList)
                    }
                    HomeAnimUtils.height(home_top_article_rv,3 * firstItemHeight.toFloat(),totalHeight.toFloat(),300,null)
                } else {
                    home_tv_seemorearticles.text = getString(R.string.home_toparticle_expand_foot)
                    HomeAnimUtils.height(home_top_article_rv,totalHeight.toFloat(),3 * firstItemHeight.toFloat(),300,null)

                }
                isShowOnlythree = !isShowOnlythree


            }

    }



    /**
     *
     * 登录
     */
    private fun setUserInfo(userInfoEntity: UserInfoEntity){
        CacheUtils.saveDataInfo(CacheKey.USER,userInfoEntity)
        Appconfig.user = userInfoEntity
        EventBusUtils.postEvent(MessageEvent(MessageEvent.MessageType.LoginSuccess))
    }

    /**
     * 获取推送文章具体数据
     */
    private fun setEveryDayPushArticle(data: EveryDayPushArticlesBean) {
        mEveryDayPushData = data
        if (data.pushStatus && DateUtils.isToday(data.time)) {
            //查询本地推送文章时间 进行判断
            mViewModel.queryPushDate().observerKt {
                processPushArticle(it)
            }
        }

    }

    //处理是否进行弹窗
    private fun processPushArticle(datas: List<PushDateEntity>) {
        if (datas != null && datas.isNotEmpty()) {
            var pushDateEntity = datas[0]
            if (pushDateEntity.time == mEveryDayPushData.time) {
                pushDateEntity.time = mEveryDayPushData.time
                mViewModel.updatePushArticlesDate(pushDateEntity)
                HomePushArticleFragment.newInstance(mEveryDayPushData.datas).showAllowingStateLoss(parentFragmentManager,"dialog_everydaypush")
            }
        } else {
            val pushTempDateEntity = PushDateEntity()
            pushTempDateEntity.time = mEveryDayPushData.time
            mViewModel.insertPushArticlesDate(pushTempDateEntity)
            HomePushArticleFragment.newInstance(mEveryDayPushData.datas).showAllowingStateLoss(parentFragmentManager,"dialog_everydaypush")
        }
    }

    /**
     *
     * 处理未读消息
     */
    private fun setUnreadMessage(number: Int) {
        if (number > 0) {
            var strMsg: String = ""
            home_rl_message.visibility = View.VISIBLE
            if (LanguageFontSizeUtils.isChinese()) {
                strMsg = "您有<font color=\"#EE7931\"> $number</font> 条未读消息</font>"
            } else {
                strMsg = "You have <font color=\"#EE7931\"> $number</font> Unread messages</font>"
            }

            home_tv_unread_message.setText(strMsg.toHtml())
        } else {
            home_rl_message.visibility = View.GONE
        }

    }


//    /**
//     * 获取置顶文章数据
//     */
//    private fun setTopArticle(data: MutableList<TopArticleBean>) {
//        mTopArticleAdapter.submitList(data)
//        if (data.size > 3) {
//            mTopArticleAdapter.setShowOnlyThree(true)
//        } else {
//            mTopArticleAdapter.setShowOnlyThree(false)
//        }
//
//        if (mBinding.homeRecommendArticleBody.headerCount == 0) {
//            mBinding.homeRecommendArticleBody.addHeaderView(recommendHeadView)
//        }
//    }


    /**
     * 获取置顶文章数据
     */
    private fun setTopBaiduRealTime(data: MutableList<BaiduContent>) {
        expandRealTimeList = data.subList(0,5)
        mBaiduHotSearchAdapter.submitList(data.take(3))
//        if (data.size > 3) {
//            mBaiduHotSearchAdapter.setShowOnlyThree(true)
//        } else {
//            mBaiduHotSearchAdapter.setShowOnlyThree(false)
//        }

        if (mBinding.homeRecommentConent.homeRecommendArticleBody.headerCount == 0) {
            mBinding.homeRecommentConent.homeRecommendArticleBody.addHeaderView(recommendHeadView)
        }


        home_top_article_rv.post {
            val viewFirst = home_top_article_rv.layoutManager!!.findViewByPosition(0)
            firstItemHeight = viewFirst!!.height
            totalHeight = if (totalHeight >= 5 * firstItemHeight)  totalHeight else 5 * firstItemHeight
//            val lp = home_top_article_rv.layoutParams
//            lp.height = 3 * firstItemHeight
//            home_top_article_rv.layoutParams = lp
        }
    }


    /**
     * 设置公众号数据
     *
     */
    private fun setOfficialAccount(data: MutableList<OfficialAccountEntity>) {
        mOfficialAccountAdapter.submitList(data)
    }


    /**
     *
     * 绑定公众号布局管理器
     */
    private fun initOfficialListener() {
        home_rv_official_account.init(
            StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.HORIZONTAL
            ), mOfficialAccountAdapter, true
        )

        mOfficialAccountAdapter.run {
            //Item点击事件
            setOnItemClickListener { adapter, view, position ->
                GoRouter.getInstance().build(RouteActivity.Wechat.WechatTabActivity)
                    .withParcelableArrayList("data", ArrayList(items))
                    .withInt("position", position)
                    .go()
            }
        }
    }

    /**
     *
     * 设置文章列表事件监听
     */
    private fun initArticleListener() {
        mHomeArticleAdapter.run {
            setOnItemClickListener { adapter, view, position ->
                ArouteUtils.startWebArticle(
                    mHomeArticleAdapter.items[position - 1].link,
                    mHomeArticleAdapter.items[position - 1].title,
                    mHomeArticleAdapter.items[position - 1].id,
                    mHomeArticleAdapter.items[position - 1].collect,
                    mHomeArticleAdapter.items[position - 1].envelopePic,
                    mHomeArticleAdapter.items[position - 1].desc,
                    mHomeArticleAdapter.items[position - 1].chapterName,
                    mHomeArticleAdapter.items[position - 1].author,
                    mHomeArticleAdapter.items[position - 1].shareUser
                )
            }

            addOnItemChildClickListener(R.id.home_icon_collect) { adapter, view, position ->
                when (view.id) {
                    R.id.home_icon_collect -> {
                        selectItem = position - 1
                        collectOrunCollect(
                            mHomeArticleAdapter.items[position - 1].collect,
                            mHomeArticleAdapter.items[position - 1].id
                        )
                    }
                }
            }
        }
    }


    /**
     *
     * 设置广告数据
     */
    private fun setBanner(data: MutableList<BannerBean>) {
        mBanner.setAdapter(object : BannerImageAdapter<BannerBean>(data) {
            override fun onBindView(
                holder: BannerImageHolder, data: BannerBean, position: Int, size: Int
            ) {
                ImageLoader.loadStringPhoto(
                    this@HomeRecommendFragment,
                    data.imagePath,
                    holder.imageView
                )
                holder.imageView.setOnClick {
                    ArouteUtils.startWebArticle(
                        data.url,
                        data.title,
                        data.id,
                        false,
                        data.imagePath,
                        data.desc,
                        "banner",
                        "张鸿洋",
                        "张鸿洋"
                    )
                }
            }

        }).addBannerLifecycleObserver(this).indicator = CircleIndicator(activity)
        //请求首页文章
        mViewModel.getHomeArticle(currentPage).observerKt {
            setArticles(it)
        }

    }

    /**
     *
     * 获取首页文章列表数据
     */
    private fun setArticles(data: HomeArticleListBean) {
        mSkeletonScreen?.hide()
        //这里返回的页码自己+1
        currentPage = data.curPage
        mBinding.homeRecommentConent.recommendRefreshLayout.finishLoadMore()
        mBinding.homeRecommentConent.recommendRefreshLayout.finishRefresh()
        if (currentPage > 1) {
            mHomeArticleAdapter.addAll(data.datas)
        } else {
            mHomeArticleAdapter.submitList(data.datas)
            if (mBinding.homeRecommentConent.homeRecommendArticleBody.headerCount == 0) {
                mBinding.homeRecommentConent.homeRecommendArticleBody.addHeaderView(recommendHeadView)
            }
        }
        if (data.datas.size == 0) {
            mBinding.homeRecommentConent.recommendRefreshLayout.setEnableLoadMore(false)
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        initRequestData()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mViewModel.getHomeArticle(currentPage).observerKt {
            setArticles(it)
        }
    }


    @LoginCheck
    private fun collectOrunCollect(collect: Boolean, articleId: Int) {
        if (collect) {
            mViewModel.unCollectArticle(articleId).observerKt {
                unCollectSuccess()
            }
        } else {
            mViewModel.collectArticle(articleId).observerKt {
                collectSucess()
            }
        }
    }

    /**
     *
     * 收藏成功
     */
    private fun collectSucess() {
        mHomeArticleAdapter.items[selectItem].collect = true
        mHomeArticleAdapter.notifyItemChanged(selectItem)

    }

    /**
     *
     * 取消收藏
     */
    private fun unCollectSuccess() {
        mHomeArticleAdapter.items[selectItem].collect = false
        mHomeArticleAdapter.notifyItemChanged(selectItem)

    }

    override fun reLoadData() {
        initRequestData()
    }


    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @SingleClick
    override fun onClick(v: View) {
        when (v) {
            mBinding.homeRecommentConent.homeIconFab -> {
                showAnimation()
            }

            mBinding.homeRecommentConent.homeIconUtils -> {
                startPage(RouteActivity.Utils.UtilsActivity)
            }

            mBinding.homeRecommentConent.homeIconCourse -> {
                startPage(RouteActivity.Course.CourseListActivity)
            }

            mBinding.homeRecommentConent.homeIconScrollUp -> {
                scrollTop()
            }
            mBinding.homeRecommentConent.homeIncludeToolbar!!.homeScanIcon->{
                XXPermissions.with(this@HomeRecommendFragment)
                    ?.permission(Permission.CAMERA)
                    ?.request(object : OnPermissionCallback {
                        override fun onGranted(permissions: List<String>, all: Boolean) {
                            if (all) {
                                ScanCodeConfig.Builder()
                                    .setFragment(this@HomeRecommendFragment)
                                    .setActivity(activity)
                                    .setPlayAudio(true)
                                    .setStyle(ScanStyle.FULL_SCREEN)
                                    .build().start(ScanCodeActivity::class.java)
                            }
                        }

                        override fun onDenied(permissions: List<String>, doNotAskAgain: Boolean) {
                            super.onDenied(permissions, doNotAskAgain)
                            activity?.let {
                                PermissionUtils.showPermissionSettingDialog(it,permissions,permissions,object :
                                    OnPermissionCallback {
                                    override fun onGranted(permissions: List<String>, all: Boolean) {

                                    }
                                })
                            }
                        }
                    })
            }
            mBinding.homeRecommentConent.homeIncludeToolbar!!.homeIvEveryday->{
                HomePushArticleFragment.newInstance(mEveryDayPushData.datas).showAllowingStateLoss(parentFragmentManager,"dialog_everydaypush")
            }

            mBinding.homeRecommentConent.homeIncludeToolbar!!.homeIvAdd->{
                startPage(RouteActivity.Square.SquareShareArticleActivity)
            }
            mBinding.homeRecommentConent.homeIncludeToolbar!!.homeIvLoginname -> {
//                if (mBinding.homeRecommentConent.homeIncludeToolbar!!.homeTvLoginname.text.toString().equals(getString(R.string.home_tv_login))) {
//                    if (CacheUtils.getGestureLogin()) {
//                        startPage(RouteActivity.Mine.QuickLoginActivity)
//                    } else if (CacheUtils.getFingerLogin()) {
//                        loginBiomtric()
//                    } else {
//                        startPage(RouteActivity.Mine.LoginActivity)
//                    }
//                }

                mBinding.homeSlidingMenu.openMenu()
            }

            mBinding.homeRecommentConent.homeIncludeToolbar!!.homeRlSearch ->{
                startPage(RouteActivity.Home.HomeSearchActivity)
            }

//            mBinding.homeIvLabelmore -> {
//                startPageWithStringArrayListParams(RouteActivity.Home.HomeKnowLedgeLabelActivity,"data" to ArrayList(knowledgeLabelList))
//            }
        }
    }

    fun scrollTop() {
        mBinding.homeRecommentConent.homeRecommendArticleBody.smoothScrollToPosition(0)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        when (event.type) {
            //登录成功
            MessageEvent.MessageType.LoginSuccess -> {
                initRequestData()
               // mBinding.homeRecommentConent.homeIncludeToolbar!!.homeTvLoginname.text = getUser()?.username
            }
            //退出登录
            MessageEvent.MessageType.LogoutSuccess -> {
                home_rl_message.visibility = View.GONE
                initRequestData()
                //退出登录成功
                //mBinding.homeRecommentConent.homeIncludeToolbar!!.homeTvLoginname.setText(getString(R.string.home_tv_login))
            }

            MessageEvent.MessageType.LogoutSuccess -> {

            }

            //更改标签
            MessageEvent.MessageType.ChangeLabel -> {
//                knowledgeLabelList.clear()
//                knowledgeLabelList.addAll(event.getStringList())
//                mFragments.clear()
//                for (i in knowledgeLabelList.indices) {
//                    if (i == 0) {
//                        mFragments.add(HomeRecommendFragment())
//                    } else {
//                        mFragments.add(HomeArticleFragment())
//                    }
//                }
//                mBinding.magicIndicator.navigator.notifyDataSetChanged()
//                mBinding.viewPager.adapter?.notifyDataSetChanged()
//                ViewInitUtils.setViewPager2Init(requireActivity(),mBinding.viewPager,mFragments,
//                    isOffscreenPageLimit = true,
//                    isUserInputEnabled = false
//                )
            }

            //改变状态栏颜色
            MessageEvent.MessageType.ChangeStatusTheme -> {
                isWithStatusTheme(event.getBoolean())
            }

            else -> {}
        }

    }


    /**
     *
     * 设置view是否显示与否
     */
    private fun setViewVisible(isShow: Boolean) {
        mBinding.homeRecommentConent.homeGpIconCourse?.visibility = if (isShow) View.VISIBLE else View.GONE
        mBinding.homeRecommentConent.homeGpIconUtils?.visibility = if (isShow) View.VISIBLE else View.GONE
        mBinding.homeRecommentConent.homeGpIconUp?.visibility = if (isShow) View.VISIBLE else View.GONE
    }


    private fun getValueAnimator(
        button: FloatingActionButton,
        reverse: Boolean,
        group: Group,
        angle: Int
    ): ValueAnimator {
        val valueANimator: ValueAnimator
        if (reverse) {
            valueANimator = ValueAnimator.ofFloat(1f, 0f)
        } else {
            valueANimator = ValueAnimator.ofFloat(0f, 1f)
        }
        valueANimator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator) {
                val v = animation.animatedValue as Float
                val params = button.layoutParams as ConstraintLayout.LayoutParams
                params.circleRadius = (radius * v).toInt()
                params.circleAngle = 270f + angle * v
                params.width = (width * v).toInt()
                params.height = (width * v).toInt()
                button.layoutParams = params
            }
        })

        valueANimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                group.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animator) {
                if (group == mBinding.homeRecommentConent.homeGpIconCourse && reverse) {
                    setViewVisible(false)
                }
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        valueANimator.duration = 300
        valueANimator.interpolator = DecelerateInterpolator()
        return valueANimator
    }


    private fun showAnimation() {
        if (animatorSet != null && animatorSet?.isRunning ?: false) {
            return
        }
        animatorSet = AnimatorSet()
        val utils: ValueAnimator
        val course: ValueAnimator
        val upScrollView: ValueAnimator
        if (mBinding.homeRecommentConent.homeGpIconCourse?.visibility != View.VISIBLE) {
            course =
                getValueAnimator(mBinding.homeRecommentConent.homeIconCourse!!, false, mBinding.homeRecommentConent.homeGpIconCourse!!, 0)
            utils =
                getValueAnimator(mBinding.homeRecommentConent.homeIconUtils!!, false, mBinding.homeRecommentConent.homeGpIconUtils!!, 45)
            upScrollView =
                getValueAnimator(mBinding.homeRecommentConent.homeIconScrollUp!!, false, mBinding.homeRecommentConent.homeGpIconUp!!, 90)
            animatorSet?.playSequentially(course, utils, upScrollView)
        } else {
            course =
                getValueAnimator(mBinding.homeRecommentConent.homeIconCourse!!, true, mBinding.homeRecommentConent.homeGpIconCourse!!, 0)
            utils = getValueAnimator(mBinding.homeRecommentConent.homeIconUtils!!, true, mBinding.homeRecommentConent.homeGpIconUtils!!, 45)
            upScrollView =
                getValueAnimator(mBinding.homeRecommentConent.homeIconScrollUp!!, true, mBinding.homeRecommentConent.homeGpIconUp!!, 90)
            animatorSet?.playSequentially(upScrollView, utils, course)
        }
        animatorSet?.start()
    }

    /**
     * 指纹登录
     */
    private fun loginBiomtric() {
        BiometricControl.setStatusCallback(object : BiometricStatusCallback {
            override fun onUsePassword() {
                startPage(RouteActivity.Mine.LoginActivity)
            }

            override fun onVerifySuccess(cipher: Cipher?) {
                try {
                    val text = CacheUtils.getEncryptLoginMessage()
                    val input = Base64.decode(text, Base64.URL_SAFE)
                    val bytes = cipher?.doFinal(input)

                    /**
                     * 然后这里用原密码(当然是加密过的)调登录接口
                     */
                    val loginEntity: LoginEntity? =
                        GsonUtils.get(String(bytes ?: ByteArray(0)), LoginEntity::class.java)
                    val iv = cipher?.iv

                    //走登录接口
                    loginEntity?.let {
                        mViewModel.login(it.loginName,it.loginPassword).observerKt {
                            setUserInfo(it)
                        }
                    }

                } catch (e: BadPaddingException) {
                    e.printStackTrace()
                } catch (e: IllegalBlockSizeException) {
                    e.printStackTrace()
                }
            }

            override fun onFailed() {
                startPage(RouteActivity.Mine.LoginActivity)
            }

            override fun error(code: Int, reason: String?) {
                ToastUtils.show("$code,$reason")
                startPage(RouteActivity.Mine.LoginActivity)
            }

            override fun onCancel() {
                ToastUtils.show(R.string.home_biometric_cancel)
                startPage(RouteActivity.Mine.LoginActivity)
            }
        }).loginBlomtric(requireActivity())





    }


    /**
     * 状态栏是否跟随主题色变化
     *
     */
    private fun isWithStatusTheme(statusWIthTheme:Boolean) {
        if (!CacheUtils.getNightModeStatus()) {
            val gradientDrawable = GradientDrawable()
            gradientDrawable.shape = GradientDrawable.RECTANGLE
            gradientDrawable.cornerRadius = 45.dp2px().toFloat()
            if (statusWIthTheme) {
                gradientDrawable.setColor(Color.WHITE)
                mBinding.homeRecommentConent.homeIncludeToolbar?.run {
                   // homeTvLoginname.setTextColor(Color.WHITE)
                    homeIvAdd.setBackgroundResource(R.drawable.home_icon_add_white)
                    homeIvEveryday.setBackgroundResource(R.drawable.home_icon_everyday_white)
                    toolbar.setBackgroundColor(CacheUtils.getThemeColor())
                }

            } else {
                gradientDrawable.setColor(Color.parseColor("#1f767680"))
                mBinding.homeRecommentConent.homeIncludeToolbar?.run {
                  //  homeTvLoginname.setTextColor(Color.parseColor("#333333"))
                    homeIvEveryday.setBackgroundResource(R.drawable.home_icon_everyday)
                    homeIvAdd.setBackgroundResource(R.drawable.home_icon_add)
                    toolbar.setBackgroundColor(Color.WHITE)
                }

            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mBinding.homeRecommentConent.homeIncludeToolbar!!.homeRlSearch.background = gradientDrawable
            } else {
                mBinding.homeRecommentConent.homeIncludeToolbar!!.homeRlSearch.setBackgroundDrawable(gradientDrawable)
            }


        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //接收扫码结果
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ScanCodeConfig.QUESTCODE && data != null) {
                val extras = data.extras
                if (extras != null) {
                    val result = extras.getString(ScanCodeConfig.CODE_KEY)
                    ToastUtils.show(result)
                }
            }
        }
    }



}