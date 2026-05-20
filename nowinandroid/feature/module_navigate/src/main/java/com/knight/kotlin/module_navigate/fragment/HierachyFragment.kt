package com.knight.kotlin.module_navigate.fragment

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.library_base.route.RouteFragment
import com.knight.kotlin.library_base.fragment.BaseMviFragment
import com.knight.kotlin.module_navigate.R
import com.knight.kotlin.module_navigate.adapter.LeftBarAdapter
import com.knight.kotlin.module_navigate.contract.HierachyContract
import com.knight.kotlin.module_navigate.databinding.NavigateHierachyFragmentBinding
import com.knight.kotlin.module_navigate.entity.HierachyListEntity
import com.knight.kotlin.module_navigate.listener.CheckListener
import com.knight.kotlin.module_navigate.listener.RvListener
import com.knight.kotlin.module_navigate.vm.HierachyVm
import com.knight.kotlin.module_navigate.widget.ItemHeaderDecoration
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/5/6 14:22
 * Description:HierachyFragment
 */
@AndroidEntryPoint
@Route(path = RouteFragment.Navigate.HierachyFragment)
class HierachyFragment :
    BaseMviFragment<
            NavigateHierachyFragmentBinding,
            HierachyVm,
            HierachyContract.Event,
            HierachyContract.State,
            HierachyContract.Effect>(),
    CheckListener {

    /**
     * 左侧Adapter
     */
    private val mHierachyLeftBarAdapter by lazy {

        LeftBarAdapter(
            requireActivity(),
            mutableListOf(),
            object : RvListener {

                override fun onItemClick(
                    id: Int,
                    position: Int
                ) {

                    sendEvent(
                        HierachyContract.Event.SelectLeftTab(
                            position
                        )
                    )

                    setChecked(position, true)
                }
            })
    }

    /**
     * LayoutManager
     */
    private lateinit var mLinearLayoutManager: LinearLayoutManager

    /**
     * 数据
     */
    private var mHierachyListEntities:
            List<HierachyListEntity> = emptyList()

    /**
     * 右侧Fragment
     */
    private var mTreeRightFragment:
            TreeRightFragment? = null

    /**
     * 左侧点击位置
     */
    private var targetPosition = 0

    /**
     * 是否联动
     */
    private var isMoved = false
    private var hasCreateFragment = false
    override fun setThemeColor(
        isDarkMode: Boolean
    ) {

    }

    override fun NavigateHierachyFragmentBinding.initView() {

        mLinearLayoutManager =
            LinearLayoutManager(requireActivity())

        hierachyLeftSidebar.apply {

            layoutManager = mLinearLayoutManager

            adapter = mHierachyLeftBarAdapter

            addItemDecoration(
                DividerItemDecoration(
                    requireActivity(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }

      //  createFragment()
    }

    override fun initObserver() {

    }

    /**
     * 初始化数据
     */
    override fun initRequestData() {

        sendEvent(
            HierachyContract.Event.LoadData
        )
    }

    override fun reLoadData() {

    }

    /**
     * 状态渲染
     */
    override fun renderState(
        state: HierachyContract.State
    ) {

        if (state.isLoading) {

            requestLoading(
                mBinding.hierachyLl
            )

        } else {

            requestSuccess()
        }

        if (state.list.isNotEmpty()) {

            setHierachyData(
                state.list
            )
            if (!hasCreateFragment) {

                hasCreateFragment = true

                createFragment()
            }
        }
    }

    /**
     * Effect
     */
    override fun handleEffect(
        effect: HierachyContract.Effect
    ) {

        when (effect) {

            is HierachyContract.Effect.ShowToast -> {

            }
        }
    }

    /**
     * 设置数据
     */
    private fun setHierachyData(
        datas: List<HierachyListEntity>
    ) {

        mHierachyListEntities = datas

        val leftList = datas.map {
            it.name
        }

        /**
         * 更新Adapter数据
         *
         * 因为 LeftBarAdapter 是自定义 RvAdapter
         * 没有 setNewInstance()
         */
        mHierachyLeftBarAdapter.refreshData(leftList)
    }

    /**
     * 设置选中
     */
    private fun setChecked(
        position: Int,
        isLeft: Boolean
    ) {

        if (isLeft) {

            mHierachyLeftBarAdapter
                .setCheckedPosition(position)

            var count = 0

            for (i in 0 until position) {

                count +=
                    mHierachyListEntities[i]
                        .children.size
            }

            count += position

            mTreeRightFragment?.setData(count)

            ItemHeaderDecoration.currentTag =
                targetPosition.toString()

        } else {

            if (isMoved) {

                isMoved = false

            } else {

                mHierachyLeftBarAdapter
                    .setCheckedPosition(position)
            }

            ItemHeaderDecoration.currentTag =
                position.toString()
        }

        moveToCenter(position)
    }

    /**
     * 移动到中间
     */
    private fun moveToCenter(
        position: Int
    ) {

        val childAt =
            mBinding.hierachyLeftSidebar.getChildAt(
                position -
                        mLinearLayoutManager
                            .findFirstVisibleItemPosition()
            )

        childAt?.let {

            val y =
                it.top -
                        mBinding.hierachyLeftSidebar.height / 2

            mBinding.hierachyLeftSidebar
                .smoothScrollBy(0, y)
        }
    }

    /**
     * 右侧联动
     */
    override fun check(
        position: Int,
        isScroll: Boolean
    ) {

        setChecked(position, isScroll)
    }

    /**
     * 创建Fragment
     */
    private fun createFragment() {

        val fragment =
            childFragmentManager
                .findFragmentByTag(
                    "hierachyfragment"
                ) as? TreeRightFragment

        if (fragment != null) {

            mTreeRightFragment = fragment

            return
        }

        mTreeRightFragment =
            TreeRightFragment.newInstance(false)

        mTreeRightFragment?.setListener(this)

        childFragmentManager.beginTransaction()
            .replace(
                R.id.hierachy_right_sidebar,
                mTreeRightFragment!!,
                "hierachyfragment"
            )
            .commitNowAllowingStateLoss()
    }
}