package com.knight.kotlin.module_home.fragment

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.flyjingfish.android_aop_core.annotations.SingleClick
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.common.reflect.TypeToken
import com.knight.kotlin.library_aop.loginintercept.LoginCheck
import com.knight.kotlin.library_base.annotation.EventBusRegister
import com.knight.kotlin.library_base.event.MessageEvent
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.ktx.getUser
import com.knight.kotlin.library_base.ktx.observeLiveData
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.ktx.toHtml
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.util.ArouteUtils
import com.knight.kotlin.library_base.util.ColorUtils
import com.knight.kotlin.library_base.util.GsonUtils
import com.knight.kotlin.library_base.util.LanguageFontSizeUtils
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_common.entity.OfficialAccountEntity
import com.knight.kotlin.library_util.BlurBuilderUtils
import com.knight.kotlin.library_util.JsonUtils
import com.knight.kotlin.library_util.SystemUtils
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.library_util.startPage
import com.knight.kotlin.library_util.toast.ToastUtils
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.library_widget.ktx.setItemChildClickListener
import com.knight.kotlin.library_widget.ktx.setItemClickListener
import com.knight.kotlin.library_widget.skeleton.Skeleton
import com.knight.kotlin.library_widget.skeleton.SkeletonScreen
import com.knight.kotlin.module_home.R
import com.knight.kotlin.module_home.activity.HomeArticlesTabActivity
import com.knight.kotlin.module_home.adapter.HomeArticleAdapter
import com.knight.kotlin.module_home.adapter.OfficialAccountAdapter
import com.knight.kotlin.module_home.adapter.OpenSourceAdapter
import com.knight.kotlin.module_home.adapter.TopArticleAdapter
import com.knight.kotlin.module_home.databinding.HomeRecommendFragmentBinding
import com.knight.kotlin.module_home.entity.BannerBean
import com.knight.kotlin.module_home.entity.HomeArticleListBean
import com.knight.kotlin.module_home.entity.OpenSourceBean
import com.knight.kotlin.module_home.entity.TopArticleBean
import com.knight.kotlin.module_home.utils.HomeAnimUtils
import com.knight.kotlin.module_home.vm.HomeRecommendVm
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.scwang.smart.refresh.layout.simple.SimpleMultiListener
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

    //开源库适配器
    private val mOpenSourceAdapter: OpenSourceAdapter by lazy { OpenSourceAdapter(arrayListOf()) }

    //置顶文章适配器
    private val mTopArticleAdapter: TopArticleAdapter by lazy { TopArticleAdapter(arrayListOf()) }

    //推荐文章适配器
    private val mHomeArticleAdapter: HomeArticleAdapter by lazy { HomeArticleAdapter(arrayListOf()) }

    //公众号适配器
    private val mOfficialAccountAdapter: OfficialAccountAdapter by lazy {
        OfficialAccountAdapter(
            arrayListOf()
        )
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

    //Banner布局
    private lateinit var mBanner: Banner<BannerBean, BannerImageAdapter<BannerBean>>

    //首页文章数据请求页码
    private var currentPage = 0

    //选择收藏/取消收藏的Item项
    private var selectItem = -1

    //是否打开了二楼
    private var openTwoLevel = false

    //置顶文章是否展开
    private var isShowOnlythree = false

    //头部view中的recycleview
    private lateinit var home_top_article_rv: SwipeRecyclerView

    private var mSkeletonScreen: SkeletonScreen? = null

    //动画集合,用来控制动画的有序播放
    private var animatorSet: AnimatorSet? = null

    //圆的半径
    private var radius: Int = 0

    //FloatingActionButton宽度和高度，宽高一样
    private var width: Int = 0

    override fun setThemeColor(isDarkMode: Boolean) {
    }

    override fun HomeRecommendFragmentBinding.initView() {
        mSkeletonScreen = Skeleton.bind(mBinding.rlHome)
            .load(R.layout.home_skeleton_activity)
            .duration(1200)
            .angle(0)
            .show()
        bindHeadView()
        initTopAdapter()
        initOfficialListener()
        initArticleListener()
        initTwoLevel()
        setOnClickListener(homeIconFab,homeIconCourse!!,homeIconUtils!!,homeIconScrollUp!!)
        homeIconFab.backgroundTintList = ColorUtils.createColorStateList(themeColor, themeColor)
        homeIconCourse.backgroundTintList = ColorUtils.createColorStateList(themeColor, themeColor)
        homeIconUtils.backgroundTintList = ColorUtils.createColorStateList(themeColor, themeColor)
        homeIconScrollUp.backgroundTintList =
            ColorUtils.createColorStateList(themeColor, themeColor)
        homeRecommendArticleBody.init(
            LinearLayoutManager(requireActivity()),
            mHomeArticleAdapter,
            true
        )
        homeTwoLevelHeader.setEnablePullToCloseTwoLevel(false)
        recommendRefreshLayout.setOnLoadMoreListener(this@HomeRecommendFragment)
        recommendRefreshLayout.setOnRefreshListener(this@HomeRecommendFragment)
        recommendRefreshLayout.setOnMultiListener(object : SimpleMultiListener() {
            override fun onHeaderMoving(
                header: RefreshHeader?,
                isDragging: Boolean,
                percent: Float,
                offset: Int,
                headerHeight: Int,
                maxDragHeight: Int
            ) {
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                //initRequestData()
            }

            override fun onStateChanged(
                refreshLayout: RefreshLayout,
                oldState: RefreshState,
                newState: RefreshState
            ) {
                when (oldState) {
                    RefreshState.TwoLevel -> {
                        homeTwoLevelContent.animate().alpha(0f).setDuration(0)
                    }
                    RefreshState.TwoLevelReleased -> {
                        openTwoLevel = true
                        homeIconFab.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireActivity(),
                                com.knight.kotlin.library_base.R.drawable.base_icon_bottom
                            )
                        )
                    }
                    RefreshState.TwoLevelFinish -> {
                        openTwoLevel = false
                        homeIconFab.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireActivity(),
                                R.drawable.home_icon_show_icon
                            )
                        )
                    }

                    else -> {}
                }
            }
        })

        homeTwoLevelHeader.setOnTwoLevelListener {
            homeTwoLevelContent.animate().alpha(1f).duration = 1000
            true
        }


        setViewVisible(false)
        radius = 80.dp2px()

    }


    override fun onResume() {
        super.onResume()
        mBinding.homeIconFab.post {
            width = mBinding.homeIconFab.measuredWidth
        }
    }

    override fun initRequestData() {
        currentPage = 0
        //未读信息请求
        getUser()?.let {
            mViewModel.getUnreadMessage()
        }
        //获取置顶文章
        mViewModel.getTopArticle()
        //获取banner
        mViewModel.getBanner()
        //获取公众号
        mViewModel.getOfficialAccount()
    }

    override fun initObserver() {
        observeLiveData(mViewModel.topArticles, ::setTopArticle)
        observeLiveData(mViewModel.articleList, ::setArticles)
        observeLiveData(mViewModel.bannerList, ::setBanner)
        observeLiveData(mViewModel.collectArticle, ::collectSucess)
        observeLiveData(mViewModel.unCollectArticle, ::unCollectSuccess)
        observeLiveData(mViewModel.unReadMessageNumber, ::setUnreadMessage)
        observeLiveData(mViewModel.officialAccountList, ::setOfficialAccount)

    }


    /**
     * 初始化置顶适配器
     * 点击事件
     */
    private fun initTopAdapter() {
        mTopArticleAdapter.run {
            setItemClickListener { adapter, view, position ->
                ArouteUtils.startWebArticle(
                    data[position].link,
                    data[position].title,
                    data[position].id,
                    data[position].collect,
                    data[position].envelopePic,
                    data[position].desc,
                    data[position].chapterName,
                    data[position].author,
                    data[position].shareUser
                )

            }

            setOnItemLongClickListener { adapter, view, position ->
                BlurBuilderUtils.snapShotWithoutStatusBar(requireActivity())
                startActivity(
                    Intent(activity, HomeArticlesTabActivity::class.java)
                        .putParcelableArrayListExtra("toparticles", ArrayList(data))
                )
                activity?.overridePendingTransition(
                    com.knight.kotlin.library_base.R.anim.base_scalealpha_in,
                    com.knight.kotlin.library_base.R.anim.base_scalealpha_slient
                )
                false
            }
        }
    }

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

        home_rl_message.setOnClickListener {
            startPage(RouteActivity.Message.MessageActivity)
        }
        home_top_article_rv.init(LinearLayoutManager(requireActivity()), mTopArticleAdapter)
        topArticleFootView.findViewById<LinearLayout>(R.id.home_ll_seemorearticles)
            .setOnClickListener {
                //展开收缩逻辑
                HomeAnimUtils.setArrowAnimate(
                    mTopArticleAdapter,
                    topArticleFootView.findViewById(R.id.home_iv_toparticlearrow),
                    isShowOnlythree
                )
                isShowOnlythree = !isShowOnlythree
            }

    }

    /**
     *
     * 初始化二楼
     */
    private fun initTwoLevel() {
        mBinding.secondOpenframeRv.init(LinearLayoutManager(requireActivity()), mOpenSourceAdapter)
        //初始化标签
        val type = object : TypeToken<List<OpenSourceBean>>() {}.type
        val jsonData: String = JsonUtils.getJson(requireActivity(), "opensourceproject.json")
        val mDataList: MutableList<OpenSourceBean> = GsonUtils.getList(jsonData, type)
        mOpenSourceAdapter.setNewInstance(mDataList)

        mOpenSourceAdapter.run {
            //子view点击事件
            addChildClickViewIds(
                R.id.home_opensource_abroadlink,
                R.id.home_opensource_internallink,
                R.id.home_iv_abroadcopy,
                R.id.home_iv_internalcopy
            )
            setItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.home_opensource_abroadlink -> {
                        GoRouter.getInstance().build(RouteActivity.Web.WebPager)
                            .withString("webUrl", mOpenSourceAdapter.data[position].abroadlink)
                            .withString("webTitle", mOpenSourceAdapter.data[position].name)
                            .go()
                    }

                    R.id.home_opensource_internallink -> {
                        GoRouter.getInstance().build(RouteActivity.Web.WebPager)
                            .withString("webUrl", mOpenSourceAdapter.data[position].internallink)
                            .withString("webTitle", mOpenSourceAdapter.data[position].name)
                            .go()
                    }

                    R.id.home_iv_abroadcopy -> {
                        SystemUtils.copyContent(
                            requireActivity(),
                            mOpenSourceAdapter.data[position].abroadlink
                        )
                        ToastUtils.show(com.knight.kotlin.library_base.R.string.base_success_copylink)
                    }
                    else -> {
                        SystemUtils.copyContent(
                            requireActivity(),
                            mOpenSourceAdapter.data[position].internallink
                        )
                        ToastUtils.show(com.knight.kotlin.library_base.R.string.base_success_copylink)
                    }

                }


            }

            //Item点击事件
            setItemClickListener { adapter, view, position ->
                //跳到webview
                GoRouter.getInstance().build(RouteActivity.Web.WebPager)
                    .withString("webUrl", mOpenSourceAdapter.data[position].abroadlink)
                    .withString("webTitle", mOpenSourceAdapter.data[position].name)
                    .go()

            }
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


    /**
     * 获取置顶文章数据
     */
    private fun setTopArticle(data: MutableList<TopArticleBean>) {
        mTopArticleAdapter.setNewInstance(data)
        if (data.size > 3) {
            mTopArticleAdapter.setShowOnlyThree(true)
        } else {
            mTopArticleAdapter.setShowOnlyThree(false)
        }

        if (home_top_article_rv.footerCount == 0 && data.size > 3) {
            home_top_article_rv.addFooterView(topArticleFootView)
        }

        if (mBinding.homeRecommendArticleBody.headerCount == 0) {
            mBinding.homeRecommendArticleBody.addHeaderView(recommendHeadView)
        }
    }


    /**
     * 设置公众号数据
     *
     */
    private fun setOfficialAccount(data: MutableList<OfficialAccountEntity>) {
        mOfficialAccountAdapter.setNewInstance(data)
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
            setItemClickListener { adapter, view, position ->
                GoRouter.getInstance().build(RouteActivity.Wechat.WechatTabActivity)
                    .withParcelableArrayList("data", ArrayList(data))
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
            setItemClickListener { adapter, view, position ->
                ArouteUtils.startWebArticle(
                    mHomeArticleAdapter.data[position - 1].link,
                    mHomeArticleAdapter.data[position - 1].title,
                    mHomeArticleAdapter.data[position - 1].id,
                    mHomeArticleAdapter.data[position - 1].collect,
                    mHomeArticleAdapter.data[position - 1].envelopePic,
                    mHomeArticleAdapter.data[position - 1].desc,
                    mHomeArticleAdapter.data[position - 1].chapterName,
                    mHomeArticleAdapter.data[position - 1].author,
                    mHomeArticleAdapter.data[position - 1].shareUser
                )
            }
            addChildClickViewIds(R.id.home_icon_collect)
            setItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.home_icon_collect -> {
                        selectItem = position - 1
                        collectOrunCollect(
                            mHomeArticleAdapter.data[position - 1].collect,
                            mHomeArticleAdapter.data[position - 1].id
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
        mViewModel.getHomeArticle(currentPage)
    }

    /**
     *
     * 获取首页文章列表数据
     */
    private fun setArticles(data: HomeArticleListBean) {
        mSkeletonScreen?.hide()
        //这里返回的页码自己+1
        currentPage = data.curPage
        mBinding.recommendRefreshLayout.finishLoadMore()
        mBinding.recommendRefreshLayout.finishRefresh()
        if (currentPage > 1) {
            mHomeArticleAdapter.addData(data.datas)
        } else {
            mHomeArticleAdapter.setNewInstance(data.datas)
            if (mBinding.homeRecommendArticleBody.headerCount == 0) {
                mBinding.homeRecommendArticleBody.addHeaderView(recommendHeadView)
            }
        }
        if (data.datas.size == 0) {
            mBinding.recommendRefreshLayout.setEnableLoadMore(false)
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        initRequestData()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mViewModel.getHomeArticle(currentPage)
    }


    @LoginCheck
    private fun collectOrunCollect(collect: Boolean, articleId: Int) {
        if (collect) {
            mViewModel.unCollectArticle(articleId)
        } else {
            mViewModel.collectArticle(articleId)
        }
    }

    /**
     *
     * 收藏成功
     */
    private fun collectSucess(data: Boolean) {
        mHomeArticleAdapter.data[selectItem].collect = true
        mHomeArticleAdapter.notifyItemChanged(selectItem)

    }

    /**
     *
     * 取消收藏
     */
    private fun unCollectSuccess(data: Boolean) {
        mHomeArticleAdapter.data[selectItem].collect = false
        mHomeArticleAdapter.notifyItemChanged(selectItem)

    }

    override fun reLoadData() {
        initRequestData()
    }


    @SingleClick
    override fun onClick(v: View) {
        when (v) {
            mBinding.homeIconFab -> {
                if (openTwoLevel) {
                    mBinding.homeTwoLevelHeader.finishTwoLevel()
                } else {
                    showAnimation()
                }
            }

            mBinding.homeIconUtils -> {
                startPage(RouteActivity.Utils.UtilsActivity)
            }

            mBinding.homeIconCourse -> {
                startPage(RouteActivity.Course.CourseListActivity)
            }

            mBinding.homeIconScrollUp -> {
                scrollTop()
            }


        }
    }

    fun scrollTop() {
        mBinding.homeRecommendArticleBody.smoothScrollToPosition(0)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        when (event.type) {
            //登录成功
            MessageEvent.MessageType.LoginSuccess -> {
                initRequestData()
            }
            //登录失败
            MessageEvent.MessageType.LogoutSuccess -> {
                home_rl_message.visibility = View.GONE
                initRequestData()
            }

            else -> {}
        }

    }


    /**
     *
     * 设置view是否显示与否
     */
    private fun setViewVisible(isShow: Boolean) {
        mBinding.homeGpIconCourse?.visibility = if (isShow) View.VISIBLE else View.GONE
        mBinding.homeGpIconUtils?.visibility = if (isShow) View.VISIBLE else View.GONE
        mBinding.homeGpIconUp?.visibility = if (isShow) View.VISIBLE else View.GONE
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
                if (group == mBinding.homeGpIconCourse && reverse) {
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
        if (mBinding.homeGpIconCourse?.visibility != View.VISIBLE) {
            course =
                getValueAnimator(mBinding.homeIconCourse!!, false, mBinding.homeGpIconCourse!!, 0)
            utils =
                getValueAnimator(mBinding.homeIconUtils!!, false, mBinding.homeGpIconUtils!!, 45)
            upScrollView =
                getValueAnimator(mBinding.homeIconScrollUp!!, false, mBinding.homeGpIconUp!!, 90)
            animatorSet?.playSequentially(course, utils, upScrollView)
        } else {
            course =
                getValueAnimator(mBinding.homeIconCourse!!, true, mBinding.homeGpIconCourse!!, 0)
            utils = getValueAnimator(mBinding.homeIconUtils!!, true, mBinding.homeGpIconUtils!!, 45)
            upScrollView =
                getValueAnimator(mBinding.homeIconScrollUp!!, true, mBinding.homeGpIconUp!!, 90)
            animatorSet?.playSequentially(upScrollView, utils, course)
        }
        animatorSet?.start()
    }


}