package com.knight.kotlin.module_home.fragment

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.FloatEvaluator
import android.animation.TypeEvaluator
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
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.annotation.Size
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.baidu.location.BDLocation
import com.flyjingfish.android_aop_core.annotations.SingleClick
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.reflect.TypeToken
import com.knight.kotlin.library_aop.loginintercept.LoginCheck
import com.knight.kotlin.library_base.annotation.EventBusRegister
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.config.CacheKey
import com.knight.kotlin.library_base.entity.BaiduContent
import com.knight.kotlin.library_base.entity.LoginEntity
import com.knight.kotlin.library_base.entity.UserInfoEntity
import com.knight.kotlin.library_base.entity.WeatherIndexItem
import com.knight.kotlin.library_base.enum.BackgroundAnimationMode
import com.knight.kotlin.library_base.enum.PollutantIndex
import com.knight.kotlin.library_base.event.MessageEvent
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.ktx.SettingsManager
import com.knight.kotlin.library_base.ktx.fromJson
import com.knight.kotlin.library_base.ktx.getLocation
import com.knight.kotlin.library_base.ktx.getUser
import com.knight.kotlin.library_base.ktx.init
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.ktx.toHtml
import com.knight.kotlin.library_base.ktx.toJson
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.util.ArouteUtils
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.ColorUtils
import com.knight.kotlin.library_base.util.EventBusUtils
import com.knight.kotlin.library_base.util.GsonUtils
import com.knight.kotlin.library_base.util.LanguageFontSizeUtils
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_base.util.isMotionReduced
import com.knight.kotlin.library_common.entity.AppUpdateBean
import com.knight.kotlin.library_common.entity.OfficialAccountEntity
import com.knight.kotlin.library_common.fragment.UpdateAppDialogFragment
import com.knight.kotlin.library_database.entity.CityBean
import com.knight.kotlin.library_database.entity.PushDateEntity
import com.knight.kotlin.library_permiss.XXPermissions
import com.knight.kotlin.library_permiss.listener.OnPermissionCallback
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_scan.activity.ScanCodeActivity
import com.knight.kotlin.library_scan.annoation.ScanStyle
import com.knight.kotlin.library_scan.decode.ScanCodeConfig
import com.knight.kotlin.library_util.Coordtransform
import com.knight.kotlin.library_util.DateUtils
import com.knight.kotlin.library_util.ResourceProvider
import com.knight.kotlin.library_util.SystemUtils
import com.knight.kotlin.library_util.TimeUtils
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.library_util.baidu.GeoCodeUtils
import com.knight.kotlin.library_util.baidu.LocationUtils
import com.knight.kotlin.library_util.baidu.OnceLocationListener
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.library_util.startPage
import com.knight.kotlin.library_util.startPageWithRightAnimate
import com.knight.kotlin.library_util.toast.ToastUtils
import com.knight.kotlin.library_widget.SpacesItemDecoration
import com.knight.kotlin.library_widget.ZzWeatherView
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.library_widget.ktx.setSafeOnItemChildClickListener
import com.knight.kotlin.library_widget.ktx.setSafeOnItemClickListener
import com.knight.kotlin.library_widget.skeleton.Skeleton
import com.knight.kotlin.library_widget.skeleton.SkeletonScreen
import com.knight.kotlin.library_widget.slidinglayout.SlidingLayout
import com.knight.kotlin.library_widget.utils.WeatherUtils
import com.knight.kotlin.library_widget.weatherview.ArcProgress
import com.knight.kotlin.library_widget.weatherview.ThemeManager
import com.knight.kotlin.library_widget.weatherview.WeatherView
import com.knight.kotlin.library_widget.weatherview.sunmoon.SunMoonView
import com.knight.kotlin.module_home.R
import com.knight.kotlin.module_home.adapter.BaiduHotSearchAdapter
import com.knight.kotlin.module_home.adapter.HeadHourWeatherAdapter
import com.knight.kotlin.module_home.adapter.HomeArticleAdapter
import com.knight.kotlin.module_home.adapter.HourWeatherAdapter
import com.knight.kotlin.module_home.adapter.OfficialAccountAdapter
import com.knight.kotlin.module_home.adapter.WeatherIndexAdapter
import com.knight.kotlin.module_home.adapter.WeatherPullutantAdapter
import com.knight.kotlin.module_home.databinding.HomeRecommendFragmentBinding
import com.knight.kotlin.module_home.dialog.HomeCityGroupFragment
import com.knight.kotlin.module_home.dialog.HomePushArticleFragment
import com.knight.kotlin.module_home.dialog.HomeWeatherNewsFragment
import com.knight.kotlin.module_home.entity.BannerBean
import com.knight.kotlin.module_home.entity.EveryDayPushArticlesBean
import com.knight.kotlin.module_home.entity.HomeArticleListBean
import com.knight.kotlin.module_home.entity.PollutantBean
import com.knight.kotlin.module_home.utils.HomeAnimUtils
import com.knight.kotlin.module_home.utils.MoonRiseSetUtils
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
import java.time.LocalDate
import java.util.Calendar
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import kotlin.math.max
import kotlin.math.min


/**
 * Author:Knight
 * Time:2021/12/29 15:46
 * Description:HomeRecommendFragment
 */
@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteFragment.Home.RecommendFragment)
class HomeRecommendFragment : BaseFragment<HomeRecommendFragmentBinding, HomeRecommendVm>(),
    OnRefreshListener, OnLoadMoreListener, SlidingLayout.MenuStatusListener,HomeCityGroupFragment.OnChooseCityListener {
    //天气背景
    private lateinit var weatherView: WeatherView
    private var resourceProvider: ResourceProvider? = null
    //百度热搜适配器
    private val mBaiduHotSearchAdapter : BaiduHotSearchAdapter by lazy {BaiduHotSearchAdapter()}
    //推荐文章适配器
    private val mHomeArticleAdapter: HomeArticleAdapter by lazy { HomeArticleAdapter() }

    //公众号适配器
    private val mOfficialAccountAdapter: OfficialAccountAdapter by lazy {
        OfficialAccountAdapter()
    }

    //今日天气头部适配器
    private val mHourWeatherHeadAdapter:HeadHourWeatherAdapter by lazy {
                HeadHourWeatherAdapter()
    }

    //今日小时天气横向布局
    private val mHourWeatherAdapter:HourWeatherAdapter by lazy {
        HourWeatherAdapter()
    }

    //今日提示
    private val mWeatherIndexAdapter:WeatherIndexAdapter by lazy {
        WeatherIndexAdapter()

    }



    //今日提示
    private val mWeatherPullutantAdapter:WeatherPullutantAdapter by lazy {
        WeatherPullutantAdapter()

    }
    //private lateinit var mHourWeatherHeaderBinding: HomeHourWeatherHeadBinding

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

    var mSlideMenuOpenListener :SlideMenuOpenListener? = null

    /**
     *
     * 日出日落
     */
    @Size(2)
    private var mStartTimes: LongArray = LongArray(2)

    /**
     *
     * 月出月落
     */
    @Size(2)
    private var mEndTimes: LongArray = LongArray(2)

    /**
     *
     * 当前时间
     */
    @Size(2)
    private var mCurrentTimes: LongArray = LongArray(2)

    @Size(2)
    private var mAnimCurrentTimes: LongArray = LongArray(2)
    @Size(3)
    private val mAttachAnimatorSets: Array<AnimatorSet?> = arrayOf(null, null, null)

    private var mAttachAnimatorSet: AnimatorSet? = null

    /**
     *
     * 空气质量指数
     */
    private var mAqiQuality:Int = 0
    private var mAqiQualityLevel:Int = 0
    override fun setThemeColor(isDarkMode: Boolean) {
        if (!isDarkMode) {
            isWithStatusTheme(CacheUtils.getStatusBarIsWithTheme())
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
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
        setOnClickListener(homeRecommentConent.homeIncludeToolbar!!.homeScanIcon,homeRecommentMenu.homeBtnLoginName,
            homeRecommentConent.homeIncludeToolbar.homeIvMoreMenu,
            homeRecommentConent.homeIncludeToolbar.homeIvEveryday,
            homeRecommentConent.homeIncludeToolbar.homeIvAdd,
            homeRecommentConent.homeIncludeToolbar.homeRlSearch,homeRecommentConent.homeIconFab,
            homeRecommentConent.homeIconCourse!!,homeRecommentConent.homeIconUtils!!,
            homeRecommentConent.homeIconScrollUp!!,mBinding.homeRecommentMenu.tvZaobaoDayTip,
            mBinding.homeRecommentMenu.homeTvLocation)
        getUser()?.let {
            homeRecommentMenu.homeBtnLoginName.text = it.username
        } ?: kotlin.run {
            homeRecommentMenu.homeBtnLoginName.text = getString(R.string.home_tv_login)
        }
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
        // attach weather view.
        weatherView = ThemeManager
            .getInstance(requireContext())
            .weatherThemeDelegate
            .getWeatherView(requireContext())

        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            0
        )

        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID

        (mBinding.homeRecommentMenu.clSlidmenu as ConstraintLayout).addView(
            weatherView as View,
            0,
             layoutParams
        )


        homeSlidingMenu.menuStatusListener = this@HomeRecommendFragment
        mBinding.homeRecommentMenu.tvZaobaoDayTip.text = DateUtils.convertTime() + "，"+ DateUtils.getCurrentDateFormatted() + "早报内容已为你准备~"


        val concatAdapter = ConcatAdapter(mHourWeatherHeadAdapter, mHourWeatherAdapter)
       // homeRecommentMenu.rvHourWeather.adapter = concatAdapter
        // 设置横向列表的布局管理器
        homeRecommentMenu.rvHourWeather.init(
            LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false),
            concatAdapter,
            true
        )

        //设置今日提示布局管理器
        homeRecommentMenu.rvWeatherIndex.init(
             GridLayoutManager(activity, 2, RecyclerView.HORIZONTAL,false),mWeatherIndexAdapter,true
        )

        homeRecommentMenu.rvWeatherIndex.addItemDecoration(

            SpacesItemDecoration(10.dp2px())
        )

        homeRecommentMenu.sunMoonControlView.setSunDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.home_icon_sun))
        homeRecommentMenu.sunMoonControlView.setMoonDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.home_icon_moon))


        homeRecommentMenu.sunMoonControlView.setColors(
            ContextCompat.getColor(requireActivity(), com.knight.kotlin.library_base.R.color.base_color_theme),
            androidx.core.graphics.ColorUtils.setAlphaComponent(ContextCompat.getColor(requireActivity(), com.knight.kotlin.library_base.R.color.base_color_theme), (0.5 * 255).toInt()),
            androidx.core.graphics.ColorUtils.setAlphaComponent(ContextCompat.getColor(requireActivity(), com.knight.kotlin.library_base.R.color.base_color_theme), (0.2 * 255).toInt()),
            Color.WHITE,
            false
        )

        homeRecommentMenu.homeRecommendMenu.setOnScrollChangeListener { _, _, _, _, _ ->
            if (ViewInitUtils.isViewVisibleInScroll(homeRecommentMenu.homeRecommendMenu,homeRecommentMenu.sunMoonControlView) &&  homeRecommentMenu.sunMoonControlView.getDrawStatus() == SunMoonView.SunMoonDrawStatus.NOTDRAW){
                if (homeSlidingMenu.getMenuViewIsOpen()) {
                        val timeDay = ValueAnimator.ofObject(LongEvaluator(), mStartTimes[0], mCurrentTimes[0])
                        timeDay.addUpdateListener { animation: ValueAnimator ->
                            mAnimCurrentTimes[0] = animation.animatedValue as Long
                            mBinding.homeRecommentMenu.sunMoonControlView.setTime(mStartTimes, mEndTimes, mAnimCurrentTimes)
                        }
                        val totalRotationDay = 360.0 * 7 * (mCurrentTimes[0] - mStartTimes[0]) / (mEndTimes[0] - mStartTimes[0])
                        val rotateDay = ValueAnimator.ofObject(
                            FloatEvaluator(),
                            0,
                            (totalRotationDay - totalRotationDay % 360).toInt()
                        )

                        rotateDay.addListener(object : Animator.AnimatorListener {
                            override fun onAnimationStart(animation: Animator) {
                                homeRecommentMenu.sunMoonControlView.setDrawStatus(SunMoonView.SunMoonDrawStatus.DRAWING)
                            }
                            override fun onAnimationRepeat(animation: Animator) {

                            }
                            override fun onAnimationCancel(animation: Animator) {
                                homeRecommentMenu.sunMoonControlView.setDrawStatus(SunMoonView.SunMoonDrawStatus.NOTDRAW)
                            }
                            override fun onAnimationEnd(animation: Animator) {
                                homeRecommentMenu.sunMoonControlView.setDrawStatus(SunMoonView.SunMoonDrawStatus.COMPLETE)
                            }
                        })
                        rotateDay.addUpdateListener { animation: ValueAnimator ->
                            mBinding.homeRecommentMenu.sunMoonControlView.setDayIndicatorRotation((animation.animatedValue as Float))
                        }
                        mAttachAnimatorSets[0] = AnimatorSet().apply {
                            playTogether(timeDay, rotateDay)
                            interpolator = OvershootInterpolator(1f)
                            duration = getPathAnimatorDuration(0)
                        }.also { it.start() }
                        val timeNight = ValueAnimator.ofObject(LongEvaluator(), mStartTimes[1], mCurrentTimes[1])
                        timeNight.addUpdateListener { animation: ValueAnimator ->
                            mAnimCurrentTimes[1] = animation.animatedValue as Long
                            mBinding.homeRecommentMenu.sunMoonControlView.setTime(mStartTimes, mEndTimes, mAnimCurrentTimes)
                        }
                        val totalRotationNight = 360.0 * 4 * (mCurrentTimes[1] - mStartTimes[1]) / (mEndTimes[1] - mStartTimes[1])

                        val rotateNight = ValueAnimator.ofObject(
                            FloatEvaluator(),
                            0,
                            (totalRotationNight - totalRotationNight % 360).toInt()
                        )
                        rotateNight.addUpdateListener { animation: ValueAnimator ->
                            mBinding.homeRecommentMenu.sunMoonControlView.setNightIndicatorRotation(-1 * animation.animatedValue as Float)
                        }
                        mAttachAnimatorSets[1] = AnimatorSet().apply {
                            playTogether(timeNight, rotateNight)
                            interpolator = OvershootInterpolator(1f)
                            duration = getPathAnimatorDuration(1)
                        }.also { it.start() }
                    }


            }

            if (ViewInitUtils.isViewVisibleInScroll(homeRecommentMenu.homeRecommendMenu,homeRecommentMenu.rlAirAqi) &&  homeRecommentMenu.weatherMainAqiProgress.getDrawStatus() == ArcProgress.ArcProgressDrawStatus.NOTDRAW) {
                val aqiColor = WeatherUtils.getColor(homeRecommentMenu.weatherMainAqiProgress.context,mAqiQualityLevel)
                val progressColor = ValueAnimator.ofObject(
                    ArgbEvaluator(),
                    ContextCompat.getColor(requireActivity(), com.knight.kotlin.library_widget.R.color.widget_air_colorLevel_1),
                    aqiColor
                )
                progressColor.addUpdateListener { animation: ValueAnimator ->
                    homeRecommentMenu.weatherMainAqiProgress.setProgressColor(
                        animation.animatedValue as Int,
                        DateUtils.isDaytime()
                    )
                }
                val backgroundColor = ValueAnimator.ofObject(
                    ArgbEvaluator(),
                    com.google.android.material.R.attr.colorOutline,
                    androidx.core.graphics.ColorUtils.setAlphaComponent(aqiColor, (255 * 0.1).toInt())
                )
                backgroundColor.addUpdateListener { animation: ValueAnimator ->
                    (homeRecommentMenu.weatherMainAqiProgress.setArcBackgroundColor((animation.animatedValue as Int)))
                }
                val aqiNumber = ValueAnimator.ofObject(FloatEvaluator(), 0, mAqiQuality)
                aqiNumber.addUpdateListener { animation: ValueAnimator ->
                    homeRecommentMenu.weatherMainAqiProgress.apply {
                        progress = (animation.animatedValue as Float)
                        setText(String.format("%d", homeRecommentMenu.weatherMainAqiProgress.progress.toInt()))
                    }
                }
                aqiNumber.addListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator) {
                            homeRecommentMenu.weatherMainAqiProgress.setDrawStatus(ArcProgress.ArcProgressDrawStatus.DRAWING)
                        }
                        override fun onAnimationRepeat(animation: Animator) {

                        }
                        override fun onAnimationCancel(animation: Animator) {
                            homeRecommentMenu.weatherMainAqiProgress.setDrawStatus(ArcProgress.ArcProgressDrawStatus.NOTDRAW)
                        }
                        override fun onAnimationEnd(animation: Animator) {
                            homeRecommentMenu.weatherMainAqiProgress.setDrawStatus(ArcProgress.ArcProgressDrawStatus.COMPLETE)
                        }
                    })

                mAttachAnimatorSet = AnimatorSet().apply {
                    playTogether(progressColor, backgroundColor, aqiNumber)
                    interpolator = DecelerateInterpolator()
                    duration = (1500 + 1 / 400f * 1500).toLong()
                    start()
                }
                mWeatherPullutantAdapter.executeAnimation()

            }

        }


        homeRecommentMenu.weatherMainAqiProgress.apply {
            progress = 0f
            setText(String.format("%d", 0))
            setProgressColor(
                ContextCompat.getColor(context, com.knight.kotlin.library_widget.R.color.widget_air_colorLevel_1),
                DateUtils.isDaytime()
            )
            setArcBackgroundColor(
                com.google.android.material.R.attr.colorOutline
            )
        }


        homeRecommentMenu.weatherMainAqiProgress.apply {
            setTextColor(if (CacheUtils.getNormalDark()) Color.parseColor("#D3D3D3") else Color.BLACK)
            setBottomTextColor(ContextCompat.getColor(context, R.color.home_weather_arc_bottom_text_color))
            max = 400f
        }


        homeRecommentMenu.rvWeatherAqi.init(
            LinearLayoutManager(requireActivity()),
            mWeatherPullutantAdapter,
            false
        )




    }




    private fun isBackgroundAnimationEnabled() =
        when (SettingsManager.getInstance(requireContext()).backgroundAnimationMode) {
            BackgroundAnimationMode.SYSTEM -> !requireContext().isMotionReduced
            BackgroundAnimationMode.ENABLED -> true
            BackgroundAnimationMode.DISABLED -> false
        }


    override fun onResume() {
        super.onResume()
        mBinding.homeRecommentConent.homeIconFab.post {
            width = mBinding.homeRecommentConent.homeIconFab.measuredWidth
        }
        weatherView.setDrawable(!isHidden)
    }

    override fun onPause() {
        super.onPause()
        weatherView.setDrawable(false)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        weatherView.setDrawable(!hidden)
    }


    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun initRequestData() {
        requestUpdate()
        initData()
    }



    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun requestUpdate() {
        mViewModel.checkAppUpdateMessage().observerKt {
            checkAppMessage(it)
        }

    }


    private fun initData() {
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

    /**
     *
     * 根据空气质量设置背景
     */
    fun setAirLevelBackground(textView: TextView,aqiLevel:Int) {
        when(aqiLevel) {
            1 -> {
                textView.setBackgroundResource(com.knight.kotlin.library_widget.R.drawable.widget_best_level_shape)
                textView.text = "优"
            }
            2 -> {
                textView.setBackgroundResource(com.knight.kotlin.library_widget.R.drawable.widget_good_level_shape)
                textView.text = "良好"
            }
            3 -> {
                textView.text = "轻度"
                textView.setBackgroundResource(com.knight.kotlin.library_widget.R.drawable.widget_small_level_shape)
            }
            4 -> {
                textView.setBackgroundResource(com.knight.kotlin.library_widget.R.drawable.widget_mid_level_shape)
                textView.text = "中度"
            }
            5 -> {
                textView.setBackgroundResource(com.knight.kotlin.library_widget.R.drawable.widget_big_level_shape)
                textView.text = "重度"
            }
            6 -> {
                textView.setBackgroundResource(com.knight.kotlin.library_widget.R.drawable.widget_poison_level_shape)
                textView.text = "有毒"
            }
            else -> {
                textView.setBackgroundResource(com.knight.kotlin.library_widget.R.drawable.widget_best_level_shape)
                textView.text = "优"
            }
        }

    }

    override fun initObserver() {

    }


    /**
     *
     * 获取定位
     */
    private fun requestOnceLocation() {
        LocationUtils.getLocation(object : OnceLocationListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onReceiveLocation(location: BDLocation?) {
                location?.let {
                    if ((it.latitude != 4.9E-324 && it.longitude != 4.9E-324) && (it.latitude > 0 && it.longitude > 0)) {
                        getDetailWeekWeather(it)
                    }
                }
            }
        })
    }





    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDetailWeekWeatherByCity(city: CityBean, lng:Double, lat:Double) {
        mBinding.homeRecommentMenu.weatherMainAqiProgress.setDrawStatus(ArcProgress.ArcProgressDrawStatus.NOTDRAW)
        mBinding.homeRecommentMenu.sunMoonControlView.setDrawStatus(SunMoonView.SunMoonDrawStatus.NOTDRAW)
        mBinding.homeRecommentMenu.homeTvLocation.text = city.city
        val latLng = Coordtransform.BD09toWGS84(lng, lat)
        getWeather(latLng[1],latLng[0],city.province, city.city, city.city,false)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getWeather(lat: Double, lng:Double, province:String, city:String, district:String,showDialog:Boolean) {
        mViewModel.getTwoWeekDayRainFall(
            lat,
            lng,
            DateUtils.getCurrentDateFormatted("yyyy-MM-dd"),
            DateUtils.getTwoWeekDaysLater(),
            true,
            "precipitation_sum"
        ).observerKt {
            var verticalList: List<Float>
            var horizontalList: List<String>
            verticalList = ArrayList()
            horizontalList = ArrayList()

            for (i in 0..it.daily.precipitation_sum.size - 1) {
                horizontalList.add(DateUtils.getMonthDay(it.daily.time.get(i)))
                verticalList.add(it.daily.precipitation_sum.get(i))
            }
            mBinding.homeRecommentMenu.scrbarChartView.setHorizontalList(horizontalList)
            mBinding.homeRecommentMenu.scrbarChartView.setVerticalList(verticalList)
        }

        mViewModel.getDetailWeekWeather(province, city, district).observerKt {
            mBinding.homeRecommentMenu.tvSunSunriseSunsetTime.text = it.rise.get(0).sunrise + "↑\n"  + it.rise.get(0).sunset+ "↓"
            val sunriseTime = DateUtils.getTimestamp(it.rise.get(0).time,it.rise.get(0).sunrise, TimeUtils.getDefaultTimeZoneId())
            val sunsetTime = DateUtils.getTimestamp(it.rise.get(0).time,it.rise.get(0).sunset,TimeUtils.getDefaultTimeZoneId())
            mStartTimes[0] = sunriseTime
            mEndTimes[0] = sunsetTime
            val moonTimes = MoonRiseSetUtils.getMoonPeriodForNight(LocalDate.now(),lat, lng)
            var moonRiseSetTime = ""
            moonTimes.rise?.let {
                mStartTimes[1] = DateUtils.getTimeStampByZonedDateTime(it,TimeUtils.getZonId(TimeUtils.getDefaultTimeZoneId()))
                moonRiseSetTime = it.hour.toString() + ":" + paddingZeroMinuter(it.minute) + "↑\n"
            }
            moonTimes.set?.let {
                mEndTimes[1] =  DateUtils.getTimeStampByZonedDateTime(it,TimeUtils.getZonId(TimeUtils.getDefaultTimeZoneId()))
                if (it.hour < 10 && !it.hour.toString().contains("0")) {
                    moonRiseSetTime = moonRiseSetTime + "0" + it.hour.toString() + ":" + paddingZeroMinuter(it.minute) + "↓"
                } else {
                    moonRiseSetTime = moonRiseSetTime + it.hour.toString() + ":" + paddingZeroMinuter(it.minute) + "↓"
                }
            }

            mBinding.homeRecommentMenu.tvMoonSunriseSunsetTime.text = moonRiseSetTime

            val calendar = Calendar.getInstance(TimeUtils.getDefaultTimeZone())
            val currentTime = calendar.time.time
            mCurrentTimes[0] = currentTime
            mCurrentTimes[1] = currentTime
            mAnimCurrentTimes = longArrayOf(mCurrentTimes[0], mCurrentTimes[1])

            mBinding.homeRecommentMenu.sunMoonControlView.setTime(mStartTimes, mEndTimes, mStartTimes)
            mBinding.homeRecommentMenu.sunMoonControlView.setDayIndicatorRotation(0f)
            mBinding.homeRecommentMenu.sunMoonControlView.setNightIndicatorRotation(0f)
            mBinding.todayWeather = it.observe
            mBinding.airWeather = it.air
            weatherView.setWeather(
                WeatherUtils.getBackgroundByWeather(it.observe.weather),
                DateUtils.isDaytime(), null

            )
            if (showDialog) {
                if (CacheUtils.getWeatherDialogShow() != DateUtils.getCurrentDateByFormat()) {
                    HomeWeatherNewsFragment.newInstance(it.observe, it.forecast_24h.get(1).maxDegree, it.forecast_24h.get(1).minDegree)
                        .showAllowingStateLoss(parentFragmentManager, "dialog_everyday_weather")
                }

            }

            mHourWeatherHeadAdapter.setRisks(listOf(it.rise.first()))
            mHourWeatherAdapter.setWeatherEveryHour(it.forecast_1h)
            mBinding.homeRecommentMenu.tvWeatherTodayValue.text = it.forecast_24h.get(1).dayWeather
            mBinding.homeRecommentMenu.tvWeatherTomorrowValue.text = it.forecast_24h.get(2).dayWeather
            mBinding.homeRecommentMenu.tvWeatherTodayMinmaxDegree.text = it.forecast_24h.get(1).maxDegree + "/" + it.forecast_24h.get(1).minDegree + "°"
            mBinding.homeRecommentMenu.tvWeatherTomorrowMinmaxDegree.text = it.forecast_24h.get(2).maxDegree + "/" + it.forecast_24h.get(2).minDegree + "°"
            setAirLevelBackground(mBinding.homeRecommentMenu.tvTodayAirLevel, it.forecast_24h.get(1).aqiLevel)
            setAirLevelBackground(mBinding.homeRecommentMenu.tvTomorrowAirLevel, it.forecast_24h.get(2).aqiLevel)
            //画折线
            mBinding.homeRecommentMenu.weatherView.setLineType(ZzWeatherView.LINE_TYPE_DISCOUNT)


            //画曲线(已修复不圆滑问题)
            //        weatherView.setLineType(ZzWeatherView.LINE_TYPE_CURVE);

            //设置线宽，单位px
            mBinding.homeRecommentMenu.weatherView.setLineWidth(6f)


            //设置一屏幕显示几列(最少3列)
            try {
                mBinding.homeRecommentMenu.weatherView.setColumnNumber(6)
            } catch (e: Exception) {
                e.printStackTrace()
            }


            //设置白天和晚上线条的颜色
            mBinding.homeRecommentMenu.weatherView.setDayAndNightLineColor(Color.BLUE, Color.RED)
            //延迟绘制 进行渲染
            mBinding.homeRecommentMenu.weatherView.postInvalidateDelayed(200)
            //填充天气数据
            mBinding.homeRecommentMenu.weatherView.setData(it.forecast_24h)


            val indexType = object : TypeToken<Map<String, WeatherIndexItem>>() {}.type
            val indexMap: Map<String, WeatherIndexItem>? = fromJson(toJson(it.index), indexType)
            indexMap?.let {
                val indexList: List<WeatherIndexItem> = it.values.toList()
                mWeatherIndexAdapter.submitList(indexList)
            }

            mAqiQuality = it.air.aqi
            mAqiQualityLevel = it.air.aqi_level - 1
            mBinding.homeRecommentMenu.weatherMainAqiProgress.apply {
                setBottomText(WeatherUtils.getAqiToName(context,mAqiQualityLevel))
                contentDescription = mAqiQualityLevel.toString() + ", " + WeatherUtils.getAqiToName(context,mAqiQualityLevel)
            }

            val mPollutantList :MutableList<PollutantBean> = mutableListOf()
            mPollutantList.add(PollutantBean(PollutantIndex.PM25,it.air.pm10.toFloat(),getString(com.knight.kotlin.library_base.R.string.base_unit_mugpcum)))
            mPollutantList.add(PollutantBean(PollutantIndex.PM10,it.air.pm10.toFloat(),getString(com.knight.kotlin.library_base.R.string.base_unit_mugpcum)))
            mPollutantList.add(PollutantBean(PollutantIndex.O3,it.air.o3.toFloat(),getString(com.knight.kotlin.library_base.R.string.base_unit_mugpcum)))
            mPollutantList.add(PollutantBean(PollutantIndex.NO2,it.air.no2.toFloat(),getString(com.knight.kotlin.library_base.R.string.base_unit_mugpcum)))
            mPollutantList.add(PollutantBean(PollutantIndex.SO2,it.air.so2.toFloat(),getString(com.knight.kotlin.library_base.R.string.base_unit_mugpcum)))
            mPollutantList.add(PollutantBean(PollutantIndex.CO,it.air.co.toFloat(),getString(com.knight.kotlin.library_base.R.string.base_unit_mgpcum)))
            mWeatherPullutantAdapter.submitList(mPollutantList)
        }
    }

    /**
     *
     * 根据经纬度信息获取详细天气预告
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDetailWeekWeather(location: BDLocation) {
        mBinding.homeRecommentMenu.homeTvLocation.text = location.city
        val latLng = Coordtransform.BD09toWGS84(location.longitude, location.latitude)
        getWeather(latLng[1],latLng[0],location.province, location.city, location.district,true)


    }

    /**
     *
     * 补0
     */
    private fun paddingZeroMinuter(minute:Int):String {
        var moonSetMinute:String = ""
        if (minute > 0 && minute < 10) {
            moonSetMinute = "0"+ minute
        } else {
            moonSetMinute = minute.toString()
        }
        return moonSetMinute
    }
    /**
     *
     * 检查APP更新
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun checkAppMessage(data: AppUpdateBean) {
        //如果本地安装包大于远端 证明本地安装的是测试包 无需更新
        if (SystemUtils.getAppVersionCode(requireActivity())  < data.versionCode ) {
            if (data.versionName != activity?.let { SystemUtils.getAppVersionName(it) }) {
                UpdateAppDialogFragment.newInstance(data).showAllowingStateLoss(
                    parentFragmentManager, "dialog_update")

            }
        } else {
            getLocation()?.let {
                if (it.latitude != 0.0 && it.longitude !=0.0 && (it.latitude != 4.9E-324 && it.longitude != 4.9E-324)) {
                    getDetailWeekWeather(it)
                }
            } ?: run{
                val permission:List<String> = listOf(Permission.ACCESS_FINE_LOCATION,Permission.ACCESS_COARSE_LOCATION,Permission.ACCESS_BACKGROUND_LOCATION)
                if (XXPermissions.isGrantedPermissions(requireActivity(),permission)) {
                    requestOnceLocation()
                } else {
                    XXPermissions.with(this)
                        ?.permission(permission)
                        ?.request(object : OnPermissionCallback {
                            override fun onGranted(permissions: List<String>, all: Boolean) {
                                if (all) {
                                    requestOnceLocation()
                                }
                            }

                            override fun onDenied(permissions: List<String>, doNotAskAgain: Boolean) {
                                super.onDenied(permissions, doNotAskAgain)
                            }
                        })

                }
            }







        }
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
     * 初始化头像
     */
//    private fun initHourWeatherHeaderView () {
//        if (mBinding.homeRecommentMenu.rvHourWeather.headerCount == 0) {
//            if (!::mHourWeatherHeaderBinding.isInitialized) {
//                mHourWeatherHeaderBinding =
//                    HomeHourWeatherHeadBinding.inflate(LayoutInflater.from(activity))
//                mBinding.homeRecommentMenu.rvHourWeather.addHeaderView(mHourWeatherHeaderBinding.root)
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
            setSafeOnItemClickListener { adapter, view, position ->
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
            setSafeOnItemClickListener  { adapter, view, position ->
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

            setSafeOnItemChildClickListener(R.id.home_icon_collect) { adapter, view, position ->
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

        mBaiduHotSearchAdapter.run {
            setSafeOnItemClickListener{ adapter, view, position ->
                ArouteUtils.startWebArticle(
                    mBaiduHotSearchAdapter.items[position].appUrl,
                    mBaiduHotSearchAdapter.items[position].word,
                    mBaiduHotSearchAdapter.items[position].index,
                    false
                )
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
        initData()
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
        initData()
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
                        }
                    })
            }
            mBinding.homeRecommentConent.homeIncludeToolbar!!.homeIvEveryday->{
                HomePushArticleFragment.newInstance(mEveryDayPushData.datas).showAllowingStateLoss(parentFragmentManager,"dialog_everydaypush")
            }

            mBinding.homeRecommentConent.homeIncludeToolbar!!.homeIvAdd->{
                startPage(RouteActivity.Square.SquareShareArticleActivity)
            }
            mBinding.homeRecommentConent.homeIncludeToolbar!!.homeIvMoreMenu -> {


                mBinding.homeSlidingMenu.openMenu()
            }

            mBinding.homeRecommentMenu.homeBtnLoginName -> {
                if ( mBinding.homeRecommentMenu.homeBtnLoginName.text.toString().equals(getString(R.string.home_tv_login))) {
                    mBinding.homeSlidingMenu.closeMenuByCallBack {
                        if (CacheUtils.getGestureLogin()) {
                            startPage(RouteActivity.Mine.QuickLoginActivity)
                        } else if (CacheUtils.getFingerLogin()) {
                            loginBiomtric()
                        } else {
                            startPageWithRightAnimate(requireActivity(),
                                RouteActivity.Mine.LoginActivity)
                        }
                    }

                }

            }

            mBinding.homeRecommentConent.homeIncludeToolbar!!.homeRlSearch ->{
                startPage(RouteActivity.Home.HomeSearchActivity)
            }

            mBinding.homeRecommentMenu.tvZaobaoDayTip -> {
                startPage(RouteActivity.Home.HomeNewsActivty)
            }

            mBinding.homeRecommentMenu.homeTvLocation -> {
                HomeCityGroupFragment().showAllowingStateLoss(childFragmentManager, "group_city")
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
                //未读信息请求
                getUser()?.let {
                    mViewModel.getUnreadMessage().observerKt {
                        setUnreadMessage(it)
                    }
                }
                mBinding.homeRecommentMenu!!.homeBtnLoginName.text = getUser()?.username
            }
            //退出登录
            MessageEvent.MessageType.LogoutSuccess -> {
                home_rl_message.visibility = View.GONE
                home_rl_message.visibility = View.GONE
                //退出登录成功
                mBinding.homeRecommentMenu!!.homeBtnLoginName.setText(getString(R.string.home_tv_login))
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

    override fun onOpenStatus(open: Boolean) {
        mSlideMenuOpenListener?.onOpenStatus(open)
    }

    private class LongEvaluator : TypeEvaluator<Long> {
        override fun evaluate(fraction: Float, startValue: Long, endValue: Long): Long {
            return startValue + ((endValue - startValue) * fraction).toLong()
        }
    }


    private fun getPathAnimatorDuration(index: Int): Long {
        val duration = max(
            1000 + 3000.0 * (mCurrentTimes[index] - mStartTimes[index]) / (mEndTimes[index] - mStartTimes[index]),
            0.0
        ).toLong()
        return min(duration, 4000)
    }
    /**
     *
     * 侧滑菜单是否打开还是关闭状态
     */
    interface SlideMenuOpenListener {
        fun onOpenStatus(open: Boolean)
    }


    override fun onDestroy() {
        super.onDestroy()
        mAttachAnimatorSet?.let {
            if (it.isRunning) it.cancel()
        }
        mAttachAnimatorSet = null
        for (i in mAttachAnimatorSets.indices) {
            mAttachAnimatorSets[i]?.let {
                if (it.isRunning) {
                    it.cancel()
                }
            }
            mAttachAnimatorSets[i] = null
        }
        mWeatherPullutantAdapter.cancelAnimation()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onChooseCity(data: CityBean) {
        GeoCodeUtils.getGeocode(
            city = data.city,
            address = data.city,
            onSuccess = { lat, lng ->
                getDetailWeekWeatherByCity(data,lng,lat)
            },
            onFail = {

            }
        )
    }

}