package com.knight.kotlin.module_square.fragment

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.flyjingfish.android_aop_core.annotations.SingleClick
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.kingja.loadsir.core.LoadService
import com.knight.kotlin.library_aop.loginintercept.LoginCheck
import com.knight.kotlin.library_base.annotation.EventBusRegister
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.entity.SearchHotKeyEntity
import com.knight.kotlin.library_base.event.MessageEvent
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.ktx.loadServiceInit
import com.knight.kotlin.library_base.ktx.observeLiveData
import com.knight.kotlin.library_base.loadsir.LoadCallBack
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.util.ArouteUtils
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.ColorUtils
import com.knight.kotlin.library_util.DataBaseUtils
import com.knight.kotlin.library_util.startPage
import com.knight.kotlin.library_util.startPageWithParams
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.library_widget.ktx.setItemChildClickListener
import com.knight.kotlin.library_widget.ktx.setItemClickListener
import com.knight.kotlin.library_widget.lottie.RightLottieAnimation
import com.knight.kotlin.library_widget.lottie.RightLottieListener
import com.knight.kotlin.module_square.R
import com.knight.kotlin.module_square.adapter.HotKeyAdapter
import com.knight.kotlin.module_square.adapter.SquareArticleAdapter
import com.knight.kotlin.module_square.adapter.SquareQuestionAdapter
import com.knight.kotlin.module_square.databinding.SquareFragmentBinding
import com.knight.kotlin.module_square.entity.SquareArticleListBean
import com.knight.kotlin.module_square.entity.SquareQuestionListBean
import com.knight.kotlin.module_square.vm.SquareVm
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Route
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Author:Knight
 * Time:2021/12/23 15:59
 * Description:SquareFragment
 */
@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteFragment.Square.SquareFragment)
class SquareFragment:BaseFragment<SquareFragmentBinding, SquareVm>(),OnLoadMoreListener,OnRefreshListener {
    //广场页码
    private var page = 0
    //问题文章页码
    private var questionPage = 1
    //热词适配器
    private val mHotKeyAdapter: HotKeyAdapter by lazy { HotKeyAdapter(arrayListOf()) }
    //点击广场文章还是问答文章点赞
    private var articleClick = true
    //点击第几项的点赞
    private var selectItem = -1

    //广场文章适配器
    private val mSquareArticleAdapter: SquareArticleAdapter by lazy { SquareArticleAdapter(arrayListOf()) }
    //问题适配器
    private val mSquareQuestionAdapter:SquareQuestionAdapter by lazy {SquareQuestionAdapter(arrayListOf())}
    val mQuestionMenu: View by lazy{ LayoutInflater.from(requireActivity()).inflate(R.layout.square_question_activity,null)}
    //问题recycleview
    val baserecycleview:SwipeRecyclerView by lazy{mQuestionMenu.findViewById(com.knight.kotlin.library_widget.R.id.base_body_rv)}
    //smartFresh布局
    val smartRefreshLayout:SmartRefreshLayout by lazy{mQuestionMenu.findViewById(R.id.include_square_question)}
    //标题
    val tv_question_title:TextView by lazy { mQuestionMenu.findViewById(R.id.square_question_tv_title) }

    private lateinit var mViewLoadService: LoadService<Any>
    private val RIPPLE_DURATION: Long = 150
    override fun SquareFragmentBinding.initView() {
        requestLoading(squareSharearticleFreshlayout)
        val flexboxLayoutManager = FlexboxLayoutManager(activity)
        //方向 主轴为水平方向,起点在左端
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        //左对齐
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
        squareSearchhotRv.init(flexboxLayoutManager,mHotKeyAdapter,false)
        squareArticleRv.init(LinearLayoutManager(requireActivity()),mSquareArticleAdapter,true)
        baserecycleview.init(LinearLayoutManager(requireActivity()),mSquareQuestionAdapter,true)
        squareSharearticleFreshlayout.setOnRefreshListener(this@SquareFragment)
        squareSharearticleFreshlayout.setOnLoadMoreListener(this@SquareFragment)
        setOnClickListener(squareFabUp,squareTvGoshare)
        tv_question_title.setText(R.string.square_question)
        smartRefreshLayout.setOnRefreshListener(object : OnRefreshListener{
            override fun onRefresh(refreshLayout: RefreshLayout) {
                questionPage = 1
                mViewModel.getQuestion(questionPage)
            }
        })
        smartRefreshLayout.setOnLoadMoreListener {
            mViewModel.getQuestion(questionPage)
        }
        squareRoot.addView(mQuestionMenu)
        mViewLoadService = loadServiceInit(mQuestionMenu.findViewById<View>(R.id.include_square_question),{

        })
        RightLottieAnimation.GuillotineBuilder(mQuestionMenu,mQuestionMenu.findViewById(R.id.square_iv_question_lefticon),squareIvQuestion,mSquareQuestionAdapter)
            .setStartDelay(RIPPLE_DURATION)
            .setActionBarViewForAnimation(squareToolbar)
            .setClosedOnStart(true)
            .setGuillotineListener(object :RightLottieListener{
                override fun onRightLottieOpened() {
                    squareFabUp.setVisibility(View.GONE)
                    mViewLoadService.showCallback(LoadCallBack::class.java)
                    mViewModel.getQuestion(questionPage)
                }

                override fun onRightLottieClosed() {
                    questionPage = 1
                    squareFabUp.visibility = View.VISIBLE
                }
            })  .build()
        initAdapterClickListener()


    }

    override fun initObserver() {
        observeLiveData(mViewModel.searchHotKeyList,::setHotKey)
        observeLiveData(mViewModel.squareArticleList,::setSquareList)
        observeLiveData(mViewModel.questionsList,::setSquareQuestionList)
        observeLiveData(mViewModel.collectArticle,::collectArticleSuccess)
        observeLiveData(mViewModel.unCollectArticle,::unCollectArticleSuccess)
    }

    override fun initRequestData() {
        mViewModel.getHotKey()
        mViewModel.getSquareArticles(page)
    }

    override fun setThemeColor(isDarkMode: Boolean) {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        gradientDrawable.setColor(CacheUtils.getThemeColor())
        gradientDrawable.cornerRadius = 4f
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mBinding.squareTvGoshare.setBackground(gradientDrawable)
        } else {
            mBinding.squareTvGoshare.setBackgroundDrawable(gradientDrawable)
        }
        mBinding.squareFabUp.setBackgroundTintList(
            ColorUtils.createColorStateList(CacheUtils.getThemeColor(), CacheUtils.getThemeColor())
        )
    }

    override fun reLoadData() {
        mViewModel.getHotKey()
        mViewModel.getSquareArticles(page)
    }

    /**
     *
     * 获取热词数据
     */
    fun setHotKey(data: MutableList<SearchHotKeyEntity>){
        mHotKeyAdapter.setNewInstance(data)
    }

    /**
     * 获取广场文章列表数据
     *
     */
    fun setSquareList(data: SquareArticleListBean){
        requestSuccess()
        mBinding.squareSharearticleFreshlayout.finishLoadMore()
        mBinding.squareSharearticleFreshlayout.finishRefresh()
        if (data.datas.size > 0) {
            if (page == 0) {
                mSquareArticleAdapter.setNewInstance(data.datas)
            } else {
                mSquareArticleAdapter.addData(data.datas)
            }
            page++
        } else {
            mBinding.squareSharearticleFreshlayout.setEnableLoadMore(false)
        }
    }


    /**
     *
     * 适配器设置监听事件
     */
    private fun initAdapterClickListener() {
        mHotKeyAdapter.run {
            setItemClickListener { adapter, view, position ->
                Appconfig.search_keyword = data.get(position).name
                DataBaseUtils.saveSearchKeyword(Appconfig.search_keyword)
                startPageWithParams(RouteActivity.Home.HomeSearchResultActivity,"keyword" to Appconfig.search_keyword)

            }

        }

        mSquareArticleAdapter.run {
            setItemClickListener { adapter, view, position ->
                ArouteUtils.startWebArticle(data.get(position).link,data.get(position).title,
                    data.get(position).id,data.get(position).collect,data.get(position).envelopePic,
                    data.get(position).desc,data.get(position).chapterName,data[position].author,data[position].shareUser)
            }
            addChildClickViewIds(R.id.square_icon_collect)
            setItemChildClickListener { adapter, view, position ->
                articleClick = true
                selectItem = position
                collectOrunCollect(data[position].collect,data[position].id)
            }


        }

        mSquareQuestionAdapter.run {
            setItemClickListener { adapter, view, position ->
                ArouteUtils.startWebArticle(data.get(position).link,data.get(position).title,
                    data.get(position).id,data.get(position).collect,data.get(position).envelopePic,
                    data.get(position).desc,data.get(position).chapterName,data[position].author,data[position].shareUser)
            }

            addChildClickViewIds(com.knight.kotlin.library_base.R.id.base_icon_collect)
            setItemChildClickListener { adapter, view, position ->
                articleClick = false
                selectItem = position
                collectOrunCollect(data[position].collect,data[position].id)

            }

        }

    }


    /**
     *
     * 获取问答文章列表数据
     *
     */
    fun setSquareQuestionList(data:SquareQuestionListBean){
        mViewLoadService.showSuccess()
        smartRefreshLayout.finishRefresh()
        smartRefreshLayout.finishLoadMore()
        if (data.datas.size > 0) {
            if (questionPage == 1) {
                mSquareQuestionAdapter.setNewInstance(data.datas)
            } else {
                mSquareQuestionAdapter.addData(data.datas)
            }
            questionPage++
        } else {
            smartRefreshLayout.setEnableLoadMore(false)
        }
    }


    /**
     *
     * 收藏文章成功
     */
    private fun collectArticleSuccess(boolean: Boolean) {
        if (articleClick) {
            mSquareArticleAdapter.data[selectItem].collect = true
            mSquareArticleAdapter.notifyItemChanged(selectItem)
        } else {
            mSquareQuestionAdapter.data[selectItem].collect = true
            mSquareQuestionAdapter.notifyItemChanged(selectItem)
        }
    }

    /**
     *
     * 取消收藏文章成功
     */
    private fun unCollectArticleSuccess(boolean: Boolean) {
        if (articleClick) {
            mSquareArticleAdapter.data[selectItem].collect = false
            mSquareArticleAdapter.notifyItemChanged(selectItem)
        } else {
            mSquareQuestionAdapter.data[selectItem].collect = false
            mSquareQuestionAdapter.notifyItemChanged(selectItem)
        }
    }




    @LoginCheck
    private fun collectOrunCollect(collect: Boolean, articleId: Int) {
        if (collect) {
            mViewModel.unCollectArticle(articleId)
        } else {
            mViewModel.collectArticle(articleId)
        }
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mViewModel.getSquareArticles(page)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        page = 0
        mBinding.squareSharearticleFreshlayout.setEnableLoadMore(true)
        mViewModel.getHotKey()
        mViewModel.getSquareArticles(page)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        when (event.type) {

            //收藏成功 分享文章成功
            MessageEvent.MessageType.CollectSuccess,MessageEvent.MessageType.ShareArticleSuccess -> {
                onRefresh(mBinding.squareSharearticleFreshlayout)
            }
            //登录成功,登出成功
            MessageEvent.MessageType.LoginSuccess,MessageEvent.MessageType.LogoutSuccess -> {
                page = 0
                mViewModel.getSquareArticles(page)
            }

            else -> {}
        }

    }

    @SingleClick
    override fun onClick(v: View) {
        when (v) {
            mBinding.squareFabUp -> {
                mBinding.squareNestedsv.fullScroll(View.FOCUS_UP)
            }
            mBinding.squareTvGoshare -> {
                startPage(RouteActivity.Square.SquareShareArticleActivity)
            }

        }
    }

}