package com.knight.kotlin.module_navigate.fragment

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.core.library_base.route.RouteActivity
import com.core.library_base.route.RouteFragment
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.knight.kotlin.library_base.fragment.BaseMviFragment
import com.knight.kotlin.library_base.utils.ArouteUtils
import com.knight.kotlin.library_util.startPageWithParams
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_navigate.R
import com.knight.kotlin.module_navigate.adapter.HierachyClassifyDetailAdapter
import com.knight.kotlin.module_navigate.contract.NavigateRightTreeContract
import com.knight.kotlin.module_navigate.databinding.NavigateRightTreeFragmentBinding
import com.knight.kotlin.module_navigate.entity.HierachyListEntity
import com.knight.kotlin.module_navigate.entity.HierachyRightBeanEntity
import com.knight.kotlin.module_navigate.entity.NavigateListEntity
import com.knight.kotlin.module_navigate.listener.CheckListener
import com.knight.kotlin.module_navigate.listener.RvListener
import com.knight.kotlin.module_navigate.vm.NavigateRightTreeVm
import com.knight.kotlin.module_navigate.widget.ItemHeaderDecoration
import com.wyjson.router.GoRouter
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/5/5 15:22
 * Description:TreeRightFragment
 */
@AndroidEntryPoint
@Route(path = RouteFragment.Navigate.NavigateRightTreeFragment)
class TreeRightFragment :
    BaseMviFragment<
            NavigateRightTreeFragmentBinding,
            NavigateRightTreeVm,
            NavigateRightTreeContract.Event,
            NavigateRightTreeContract.State,
            NavigateRightTreeContract.Effect>(),
    CheckListener {

    private lateinit var mHierachyClassifyDetailAdapter:
            HierachyClassifyDetailAdapter

    private val mDatas =
        ArrayList<HierachyRightBeanEntity>()

    private lateinit var mDecoration:
            ItemHeaderDecoration

    private var move = false

    private var mIndex = 0

    private var checkListener: CheckListener? = null

    private var isNavigate = false

    private lateinit var mManager:
            FlexboxLayoutManager

    companion object {

        fun newInstance(
            isNavigate: Boolean
        ): TreeRightFragment {

            return TreeRightFragment().apply {

                arguments = Bundle().apply {

                    putBoolean(
                        "isNavigate",
                        isNavigate
                    )
                }
            }
        }
    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun NavigateRightTreeFragmentBinding.initView() {

        requestLoading(navigateLlRight)

        isNavigate =
            arguments?.getBoolean("isNavigate")
                ?: false

        treeTightRv.addOnScrollListener(
            RecyclerViewListener()
        )

        mManager = FlexboxLayoutManager(
            requireActivity()
        ).apply {

            flexDirection = FlexDirection.ROW

            justifyContent =
                JustifyContent.FLEX_START

            alignItems = AlignItems.CENTER
        }

        treeTightRv.layoutManager = mManager

        initAdapter()
    }

    override fun initObserver() {

    }

    override fun initRequestData() {

        sendEvent(
            NavigateRightTreeContract.Event.LoadData(
                isNavigate
            )
        )
    }

    override fun reLoadData() {

        sendEvent(
            NavigateRightTreeContract.Event.LoadData(
                isNavigate
            )
        )
    }

    override fun renderState(
        state: NavigateRightTreeContract.State
    ) {

        when {

            state.isLoading -> {

                requestLoading(
                    mBinding.navigateLlRight
                )
            }

            state.navigateList.isNotEmpty() -> {

                requestSuccess()

                setNavigateData(
                    state.navigateList
                )
            }

            state.hierachyList.isNotEmpty() -> {

                requestSuccess()

                setHierachyData(
                    state.hierachyList
                )
            }
        }
    }

    override fun handleEffect(
        effect: NavigateRightTreeContract.Effect
    ) {

        when (effect) {

            is NavigateRightTreeContract.Effect.ShowToast -> {

                toast(effect.msg)
            }
        }
    }

    /**
     * 初始化 Adapter
     */
    private fun initAdapter() {

        mHierachyClassifyDetailAdapter =
            HierachyClassifyDetailAdapter(
                requireActivity(),
                mDatas,
                object : RvListener {

                    override fun onItemClick(
                        id: Int,
                        position: Int
                    ) {

                        if (id == R.id.navigate_root) {

                            if (!isNavigate) {

                                GoRouter.getInstance()
                                    .build(
                                        RouteActivity.Navigate.HierachyTabActivity
                                    )
                                    .withStringArrayList(
                                        "childrenNames",
                                        ArrayList(
                                            mDatas[position].childrenName
                                        )
                                    )
                                    .withIntegerArrayList(
                                        "cids",
                                        ArrayList(
                                            mDatas[position].cid
                                        )
                                    )
                                    .withString(
                                        "titleName",
                                        mDatas[position].titleName
                                    )
                                    .go()
                            }

                        } else if (id == R.id.navigate_tv_content) {

                            if (isNavigate) {

                                ArouteUtils.startWebArticle(
                                    mDatas[position].link,
                                    mDatas[position].name,
                                    mDatas[position].id,
                                    mDatas[position].collect,
                                    "",
                                    "",
                                    mDatas[position].name,
                                    mDatas[position].name,
                                    ""
                                )

                            } else {

                                startPageWithParams(
                                    RouteActivity.Navigate.HierachyDetailActivity,
                                    "cid" to mDatas[position].id,
                                    "titleName" to mDatas[position].name
                                )
                            }
                        }
                    }
                })

        mBinding.treeTightRv.adapter =
            mHierachyClassifyDetailAdapter
    }

    fun setListener(listener: CheckListener) {
        checkListener = listener
    }

    inner class RecyclerViewListener :
        RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(
            recyclerView: RecyclerView,
            newState: Int
        ) {

            super.onScrollStateChanged(
                recyclerView,
                newState
            )

            if (move &&
                newState ==
                RecyclerView.SCROLL_STATE_IDLE
            ) {

                move = false

                val n =
                    mIndex -
                            mManager.findFirstVisibleItemPosition()

                if (n in 0 until mBinding.treeTightRv.childCount) {

                    val top =
                        mBinding.treeTightRv
                            .getChildAt(n)
                            .top

                    mBinding.treeTightRv
                        .smoothScrollBy(0, top)
                }
            }
        }
    }

    fun setData(n: Int) {

        mIndex = n

        mBinding.treeTightRv.stopScroll()

        smoothMoveToPosition(n)
    }

    private fun smoothMoveToPosition(n: Int) {

        val firstItem =
            mManager.findFirstVisibleItemPosition()

        val lastItem =
            mManager.findLastVisibleItemPosition()

        when {

            n <= firstItem -> {

                mBinding.treeTightRv.scrollToPosition(
                    if (n < firstItem - 10) {
                        n + 5
                    } else {
                        n
                    }
                )
            }

            n <= lastItem -> {

                val top =
                    mBinding.treeTightRv
                        .getChildAt(n - firstItem)
                        .top

                mBinding.treeTightRv
                    .scrollBy(0, top)
            }

            else -> {

                mBinding.treeTightRv
                    .scrollToPosition(n)

                move = true
            }
        }
    }

    /**
     * 导航数据
     */
    private fun setNavigateData(
        data: List<NavigateListEntity>
    ) {

        mDatas.clear()

        data.forEachIndexed { index, entity ->

            mDatas.add(
                HierachyRightBeanEntity().apply {

                    name = entity.name

                    isTitle = true

                    titleName = entity.name

                    tag = index.toString()
                }
            )

            entity.articles.forEach {

                mDatas.add(
                    HierachyRightBeanEntity().apply {

                        name = it.title

                        tag = index.toString()

                        titleName =
                            it.superChapterName

                        link = it.link

                        id = it.id

                        isTitle = false

                        collect = it.collect
                    }
                )
            }
        }

        updateDecoration()
    }

    /**
     * 体系数据
     */
    private fun setHierachyData(
        data: List<HierachyListEntity>
    ) {

        mDatas.clear()

        var total = 0

        data.forEachIndexed { index, entity ->

            val childNames =
                ArrayList<String>()

            val childCids =
                ArrayList<Int>()

            entity.children.forEach {

                childNames.add(it.name)

                childCids.add(it.id)
            }

            mDatas.add(
                HierachyRightBeanEntity().apply {

                    name = entity.name

                    isTitle = true

                    titleName = entity.name

                    tag = index.toString()

                    id = entity.id

                    parentName = entity.name

                    total = entity.children.size

                    position = total

                    childrenName = childNames

                    cid = childCids
                }
            )

            total++

            entity.children.forEach {

                mDatas.add(
                    HierachyRightBeanEntity().apply {

                        name = it.name

                        tag = index.toString()

                        titleName = entity.name

                        id = it.id

                        position = total
                    }
                )

                total++
            }
        }

        updateDecoration()
    }

    /**
     * 更新 Decoration
     */
    private fun updateDecoration() {

        if (!::mDecoration.isInitialized) {

            mDecoration = ItemHeaderDecoration(
                requireActivity(),
                mDatas
            )

            mBinding.treeTightRv
                .addItemDecoration(mDecoration)

            checkListener?.let {

                mDecoration.setCheckListener(it)
            }

        } else {

            mDecoration.setData(mDatas)
        }

        mHierachyClassifyDetailAdapter
            .notifyDataSetChanged()
    }

    override fun check(
        position: Int,
        isScroll: Boolean
    ) {

        checkListener?.check(
            position,
            isScroll
        )
    }
}