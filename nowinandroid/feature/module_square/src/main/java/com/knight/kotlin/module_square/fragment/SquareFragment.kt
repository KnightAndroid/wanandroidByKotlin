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
import com.core.library_base.ktx.loadServiceInit
import com.core.library_base.route.RouteActivity
import com.core.library_base.route.RouteFragment
import com.core.library_base.util.GsonUtils
import com.flyjingfish.android_aop_core.annotations.SingleClick
import com.google.gson.reflect.TypeToken
import com.kingja.loadsir.core.LoadService
import com.knight.kotlin.library_base.fragment.BaseMviFragment
import com.knight.kotlin.library_base.utils.ArouteUtils
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.library_util.JsonUtils.getJson
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.library_util.bindViewPager2
import com.knight.kotlin.library_util.startPage
import com.knight.kotlin.library_util.startPageWithStringArrayListParams
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.library_widget.ktx.setSafeOnItemChildClickListener
import com.knight.kotlin.library_widget.ktx.setSafeOnItemClickListener
import com.knight.kotlin.library_widget.lottie.RightLottieAnimation
import com.knight.kotlin.library_widget.lottie.RightLottieListener
import com.knight.kotlin.module_square.R
import com.knight.kotlin.module_square.adapter.SquareQuestionAdapter
import com.knight.kotlin.module_square.constants.SquareConstants
import com.knight.kotlin.module_square.contract.SquareContract
import com.knight.kotlin.module_square.databinding.SquareFragmentBinding
import com.knight.kotlin.module_square.vm.SquareVm
import com.scwang.smart.refresh.layout.SmartRefreshLayout
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
class SquareFragment :
    BaseMviFragment<
            SquareFragmentBinding,
            SquareVm,
            SquareContract.Event,
            SquareContract.State,
            SquareContract.Effect>() {

    private var questionPage = 1
    private var selectItem = -1
    private val RIPPLE_DURATION: Long = 150

    private val mQuestionMenu: View by lazy {
        LayoutInflater.from(requireActivity())
            .inflate(R.layout.square_question_activity, mBinding.squareRoot,false)
    }

    private val baserecycleview: SwipeRecyclerView by lazy {
        mQuestionMenu.findViewById(com.knight.kotlin.library_widget.R.id.base_body_rv)
    }

    private val smartRefreshLayout: SmartRefreshLayout by lazy {
        mQuestionMenu.findViewById(R.id.include_square_question)
    }

    private val tv_question_title: TextView by lazy {
        mQuestionMenu.findViewById(R.id.square_question_tv_title)
    }

    private val mSquareQuestionAdapter by lazy { SquareQuestionAdapter() }

    private lateinit var mViewLoadService: LoadService<Any>

    private val mFragments = mutableListOf<Fragment>()
    private var knowledgeLabelList = mutableListOf<String>()

    override fun SquareFragmentBinding.initView() {
        // RecyclerView
        baserecycleview.init(
            LinearLayoutManager(requireActivity()),
            mSquareQuestionAdapter,
            true
        )

        tv_question_title.setText(R.string.square_question)
        squareRoot.addView(mQuestionMenu)
        mViewLoadService = loadServiceInit(mQuestionMenu.findViewById(R.id.include_square_question)) {
            reLoadData()
        }

        // 下拉刷新
        smartRefreshLayout.setOnRefreshListener {
            questionPage = 1
            sendEvent(SquareContract.Event.LoadQuestions(page = questionPage))
        }

        // 加载更多
        smartRefreshLayout.setOnLoadMoreListener {
            sendEvent(SquareContract.Event.LoadQuestions(page = questionPage))
        }

        initQuestionAdapterClickEvent()
        initSquareFragments()
        initMenuAnimation()
        setOnClickListener(mBinding.squareTvGoshare,mBinding.squareIvLabelmore)
    }

    private fun initQuestionAdapterClickEvent() {
        mSquareQuestionAdapter.run {
            setSafeOnItemClickListener { _, _, position ->
                val item = items[position]
                ArouteUtils.startWebArticle(
                    item.link,
                    item.title,
                    item.id,
                    item.collect,
                    item.envelopePic,
                    item.desc,
                    item.chapterName,
                    item.author,
                    item.shareUser
                )
            }

            setSafeOnItemChildClickListener(com.core.library_base.R.id.base_icon_collect) { _, _, position ->
                selectItem = position
                val item = items[position]
                sendEvent(
                    if (item.collect) {
                        SquareContract.Event.UnCollectArticle(item.id, position)
                    } else {
                        SquareContract.Event.CollectArticle(item.id, position)
                    }
                )
            }
        }
    }

    override fun initObserver() {
        // MVI 观察 State/Effect 都在 BaseMviFragment 已实现
    }

    override fun initRequestData() {
        sendEvent(SquareContract.Event.LoadQuestions(page = questionPage))
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
    }

    override fun renderState(state: SquareContract.State) {
        // Loading 状态
        if (state.isLoading && state.questionPage == null) {
            requestLoading(smartRefreshLayout)
            return
        }

       // mViewLoadService.showSuccess()
        smartRefreshLayout.finishRefresh()
        smartRefreshLayout.finishLoadMore()

        state.questionPage?.let { data ->
            if (data.datas.isEmpty()) {
                requestEmptyData()
                return
            }

            if (questionPage == 1) {
                mSquareQuestionAdapter.submitList(data.datas.toMutableList())
            } else {
                mSquareQuestionAdapter.addAll(data.datas)
            }

            questionPage++
            smartRefreshLayout.setEnableLoadMore(!data.over)
        }
    }

    override fun handleEffect(effect: SquareContract.Effect) {
        when (effect) {
            is SquareContract.Effect.ShowError -> toast(effect.msg)

            // ✅ 统一处理收藏/取消收藏
            is SquareContract.Effect.UpdateCollect -> {
                mSquareQuestionAdapter.items[effect.position].collect = effect.collect
                mSquareQuestionAdapter.notifyItemChanged(effect.position)
            }

            is SquareContract.Effect.ShowToast -> toast(effect.msg)
            is SquareContract.Effect.LoadMoreSuccess -> {
                // 可选：加载更多成功提示
                smartRefreshLayout.finishLoadMore()
            }

            is SquareContract.Effect.RefreshSuccess -> {
                // 可选：刷新成功提示
                smartRefreshLayout.finishRefresh()
            }
        }
    }

    private fun initMenuAnimation() {
        RightLottieAnimation.GuillotineBuilder(
            mQuestionMenu,
            mQuestionMenu.findViewById(R.id.square_iv_question_lefticon),
            mBinding.squareIvQuestion,
            mSquareQuestionAdapter
        )
            .setStartDelay(RIPPLE_DURATION)
            .setActionBarViewForAnimation(mBinding.squareToolbar)
            .setClosedOnStart(true)
            .setGuillotineListener(object : RightLottieListener {
                override fun onRightLottieOpened() {
                  //  mViewLoadService.showCallback(com.core.library_base.loadsir.LoadCallBack::class.java)
                    sendEvent(SquareContract.Event.LoadQuestions(page = questionPage))
                }

                override fun onRightLottieClosed() {
                    questionPage = 1
                }
            })
            .build()
    }

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
        knowledgeLabelList.forEachIndexed { index, _ ->
            if (index == 0) mFragments.add(SquareShareListFragment())
            else mFragments.add(SquareArticleFragment())
        }

        ViewInitUtils.setViewPager2Init(requireActivity(), mBinding.squareArticleVp, mFragments,
            isOffscreenPageLimit = false,
            isUserInputEnabled = false
        )

        mBinding.magicIndicator.bindViewPager2(mBinding.squareArticleVp, knowledgeLabelList) {
            SquareConstants.ARTICLE_TYPE = knowledgeLabelList[it]
        }
    }

    override fun reLoadData() {
        sendEvent(SquareContract.Event.LoadQuestions(page = 1))
    }

    @SingleClick
    override fun onClick(v: View) {
        when (v) {
            mBinding.squareTvGoshare -> startPage(RouteActivity.Square.SquareShareArticleActivity)
            mBinding.squareIvLabelmore -> startPageWithStringArrayListParams(
                RouteActivity.Home.HomeKnowLedgeLabelActivity,
                "data" to ArrayList(knowledgeLabelList)
            )
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        when (event.type) {
            MessageEvent.MessageType.ChangeLabel -> {
                knowledgeLabelList.clear()
                knowledgeLabelList.addAll(event.getStringList())
                mFragments.clear()
                knowledgeLabelList.forEachIndexed { index, _ ->
                    if (index == 0) mFragments.add(SquareShareListFragment())
                    else mFragments.add(SquareArticleFragment())
                }

                mBinding.magicIndicator.navigator.notifyDataSetChanged()
                mBinding.squareArticleVp.adapter?.notifyDataSetChanged()

                ViewInitUtils.setViewPager2Init(
                    requireActivity(),
                    mBinding.squareArticleVp,
                    mFragments,
                    isOffscreenPageLimit = true,
                    isUserInputEnabled = false
                )
            }
            else ->{}
        }
    }

    override fun onDestroyView() {
        mBinding.squareArticleVp.adapter = null
        mFragments.clear()
        super.onDestroyView()
    }
}