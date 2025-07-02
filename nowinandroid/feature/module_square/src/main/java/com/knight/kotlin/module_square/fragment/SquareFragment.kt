package com.knight.kotlin.module_square.fragment

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.library_base.annotation.EventBusRegister
import com.core.library_base.event.MessageEvent
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.core.library_base.ktx.loadServiceInit
import com.core.library_base.route.RouteActivity
import com.core.library_base.route.RouteFragment
import com.knight.kotlin.library_base.utils.ArouteUtils
import com.knight.kotlin.library_base.utils.CacheUtils
import com.core.library_base.util.GsonUtils
import com.flyjingfish.android_aop_core.annotations.SingleClick
import com.google.gson.reflect.TypeToken
import com.kingja.loadsir.core.LoadService
import com.knight.kotlin.library_aop.loginintercept.LoginCheck
import com.knight.kotlin.library_util.JsonUtils.getJson
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.library_util.bindViewPager2
import com.knight.kotlin.library_util.startPage
import com.knight.kotlin.library_util.startPageWithStringArrayListParams
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.library_widget.ktx.setSafeOnItemChildClickListener
import com.knight.kotlin.library_widget.ktx.setSafeOnItemClickListener
import com.knight.kotlin.library_widget.lottie.RightLottieAnimation
import com.knight.kotlin.library_widget.lottie.RightLottieListener
import com.knight.kotlin.module_square.R
import com.knight.kotlin.module_square.adapter.SquareQuestionAdapter
import com.knight.kotlin.module_square.constants.SquareConstants
import com.knight.kotlin.module_square.databinding.SquareFragmentBinding
import com.knight.kotlin.module_square.entity.SquareQuestionListBean
import com.knight.kotlin.module_square.vm.SquareVm
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Route
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.reflect.Type

/**
 * Author:Knight
 * Time:2024/4/22 10:27
 * Description:SquareFragment 广场页面
 */
@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteFragment.Square.SquareFragment)
class SquareFragment : BaseFragment<SquareFragmentBinding, SquareVm>() {

    //问题文章页码
    private var questionPage = 1
    //点击第几项的点赞
    private var selectItem = -1
    private val RIPPLE_DURATION: Long = 150
    val mQuestionMenu: View by lazy{ LayoutInflater.from(requireActivity()).inflate(R.layout.square_question_activity,null)}


    //问题recycleview
    val baserecycleview: SwipeRecyclerView by lazy{mQuestionMenu.findViewById(com.knight.kotlin.library_widget.R.id.base_body_rv)}
    //smartFresh布局
    val smartRefreshLayout: SmartRefreshLayout by lazy{mQuestionMenu.findViewById(R.id.include_square_question)}
    //标题
    val tv_question_title: TextView by lazy { mQuestionMenu.findViewById(R.id.square_question_tv_title) }

    //问题适配器
    private val mSquareQuestionAdapter: SquareQuestionAdapter by lazy { SquareQuestionAdapter() }


    private lateinit var mViewLoadService: LoadService<Any>
    /**
     * 广场Fragment
     */
    private var mFragments = mutableListOf<Fragment>()
    /**
     * 知识标签
     */
    private var knowledgeLabelList = mutableListOf<String>()
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
    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun SquareFragmentBinding.initView() {
        baserecycleview.init(LinearLayoutManager(requireActivity()),mSquareQuestionAdapter,true)
        setOnClickListener(squareTvGoshare,mBinding.squareIvLabelmore)
        tv_question_title.setText(R.string.square_question)
        smartRefreshLayout.setOnRefreshListener(object : OnRefreshListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                questionPage = 1
                mViewModel.getQuestion(questionPage).observerKt {
                    setSquareQuestionList(it)
                }
            }
        })
        smartRefreshLayout.setOnLoadMoreListener {
            mViewModel.getQuestion(questionPage).observerKt {
                setSquareQuestionList(it)
            }
        }
        squareRoot.addView(mQuestionMenu)
        mViewLoadService = loadServiceInit(mQuestionMenu.findViewById<View>(R.id.include_square_question),{

        })

        RightLottieAnimation.GuillotineBuilder(mQuestionMenu,mQuestionMenu.findViewById(R.id.square_iv_question_lefticon),squareIvQuestion,mSquareQuestionAdapter)
            .setStartDelay(RIPPLE_DURATION)
            .setActionBarViewForAnimation(squareToolbar)
            .setClosedOnStart(true)
            .setGuillotineListener(object : RightLottieListener {
                override fun onRightLottieOpened() {
                    mViewLoadService.showCallback(com.core.library_base.loadsir.LoadCallBack::class.java)
                    mViewModel.getQuestion(questionPage).observerKt {
                        setSquareQuestionList(it)
                    }
                }

                override fun onRightLottieClosed() {
                    questionPage = 1
                }
            })  .build()
        initQuestionAdapterClickEvent()
        initSquareFragments()
    }


    /**
     *
     * 获取问答文章列表数据
     *
     */
    fun setSquareQuestionList(data: SquareQuestionListBean){
        mViewLoadService.showSuccess()
        smartRefreshLayout.finishRefresh()
        smartRefreshLayout.finishLoadMore()
        if (data.datas.size > 0) {
            if (questionPage == 1) {
                mSquareQuestionAdapter.submitList(data.datas)
            } else {
                mSquareQuestionAdapter.addAll(data.datas)
            }
            questionPage++
        } else {
            smartRefreshLayout.setEnableLoadMore(false)
        }
    }
    private fun initQuestionAdapterClickEvent() {
        mSquareQuestionAdapter.run {
            setSafeOnItemClickListener { adapter, view, position ->
                ArouteUtils.startWebArticle(items.get(position).link,items.get(position).title,
                    items.get(position).id,items.get(position).collect,items.get(position).envelopePic,
                    items.get(position).desc,items.get(position).chapterName,items[position].author,items[position].shareUser)
            }


            setSafeOnItemChildClickListener(com.core.library_base.R.id.base_icon_collect) { adapter, view, position ->
                selectItem = position
                collectOrunCollect(items[position].collect,items[position].id)

            }

        }
    }

    @LoginCheck
    private fun collectOrunCollect(collect: Boolean, articleId: Int) {
        if (collect) {
            mViewModel.unCollectArticle(articleId).observerKt {
                unCollectQuestionArticleSuccess()
            }
        } else {
            mViewModel.collectArticle(articleId).observerKt {
                collectQuestionArticleSuccess()
            }
        }
    }


    /**
     *
     * 收藏文章成功
     */
    private fun collectQuestionArticleSuccess() {
        mSquareQuestionAdapter.items[selectItem].collect = true
        mSquareQuestionAdapter.notifyItemChanged(selectItem)


    }

    /**
     *
     * 取消收藏文章成功
     */
    private fun unCollectQuestionArticleSuccess() {
        mSquareQuestionAdapter.items[selectItem].collect = false
        mSquareQuestionAdapter.notifyItemChanged(selectItem)

    }



    /**
     * 初始化指示器
     */
    private fun initSquareFragments() {
        knowledgeLabelList = CacheUtils.getDataInfo(
            "knowledgeLabel",
            object : TypeToken<List<String>>() {}.type
        )
        if (knowledgeLabelList.isNullOrEmpty()) {
            val type: Type = object : TypeToken<List<String>>() {}.type
            val jsonData = getJson(requireActivity(), "searchkeywords.json")
            knowledgeLabelList = GsonUtils.getList(jsonData, type)
        }
        mFragments.clear()
        for (i in knowledgeLabelList.indices) {
            if (i == 0) {
                mFragments.add(SquareShareListFragment())
            } else {
                mFragments.add(SquareArticleFragment())
            }
        }

        ViewInitUtils.setViewPager2Init(requireActivity(),mBinding.squareArticleVp,mFragments,
            isOffscreenPageLimit = false,
            isUserInputEnabled = false
        )

        mBinding.magicIndicator.bindViewPager2(mBinding.squareArticleVp,knowledgeLabelList) {
            SquareConstants.ARTICLE_TYPE = knowledgeLabelList[it]
        }




    }
    @SingleClick
    override fun onClick(v: View) {
        when (v) {
            mBinding.squareTvGoshare -> {
                startPage(RouteActivity.Square.SquareShareArticleActivity)
            }
            mBinding.squareIvLabelmore -> {
                startPageWithStringArrayListParams(RouteActivity.Home.HomeKnowLedgeLabelActivity,"data" to ArrayList(knowledgeLabelList))
            }

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        when (event.type) {
            //更改标签
            MessageEvent.MessageType.ChangeLabel -> {
                knowledgeLabelList.clear()
                knowledgeLabelList.addAll(event.getStringList())
                mFragments.clear()
                for (i in knowledgeLabelList.indices) {
                    if (i == 0) {
                        mFragments.add(SquareShareListFragment())
                    } else {
                        mFragments.add(SquareArticleFragment())
                    }
                }
                mBinding.magicIndicator.navigator.notifyDataSetChanged()
                mBinding.squareArticleVp.adapter?.notifyDataSetChanged()
                ViewInitUtils.setViewPager2Init(requireActivity(),mBinding.squareArticleVp,mFragments,
                    isOffscreenPageLimit = true,
                    isUserInputEnabled = false
                )
            }
            else ->{}


        }

    }

}