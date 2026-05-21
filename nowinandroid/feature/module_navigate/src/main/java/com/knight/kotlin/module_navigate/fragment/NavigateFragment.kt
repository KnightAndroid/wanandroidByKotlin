package com.knight.kotlin.module_navigate.fragment

import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.library_base.route.RouteFragment
import com.knight.kotlin.library_base.fragment.BaseMviFragment
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_navigate.R
import com.knight.kotlin.module_navigate.adapter.LeftBarAdapter
import com.knight.kotlin.module_navigate.contract.NavigateContract
import com.knight.kotlin.module_navigate.databinding.NavigateFragmentBinding
import com.knight.kotlin.module_navigate.entity.NavigateListEntity
import com.knight.kotlin.module_navigate.listener.CheckListener
import com.knight.kotlin.module_navigate.listener.RvListener
import com.knight.kotlin.module_navigate.vm.NavigateVm
import com.knight.kotlin.module_navigate.widget.ItemHeaderDecoration
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/5/5 13:57
 * Description:NavigateFragment
 * 导航页面
 */
@AndroidEntryPoint
@Route(path = RouteFragment.Navigate.NavigateFragment)
class NavigateFragment :
    BaseMviFragment<
            NavigateFragmentBinding,
            NavigateVm,
            NavigateContract.Event,
            NavigateContract.State,
            NavigateContract.Effect>(),
    CheckListener {

    /**
     * 左侧 Adapter
     */
    private var mNavigateLeftBarAdapter: LeftBarAdapter? = null

    /**
     * 左侧 LayoutManager
     */
    private lateinit var mLinearLayoutManager: LinearLayoutManager

    /**
     * 导航数据
     */
    private var mNavigateListEntities: List<NavigateListEntity> = emptyList()

    /**
     * 右侧 Fragment
     */
    private var mTreeRightFragment: TreeRightFragment? = null

    /**
     * 点击左边 item 的位置
     */
    private var targetPosition = 0

    /**
     * 是否正在联动滚动
     */
    private var isMoved = false

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun NavigateFragmentBinding.initView() {

        requestLoading(navigateLlMain)

        mLinearLayoutManager = LinearLayoutManager(requireActivity())

        navigateLeftSidebar.apply {

            layoutManager = mLinearLayoutManager

            addItemDecoration(
                DividerItemDecoration(
                    requireActivity(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        sendEvent(NavigateContract.Event.LoadData)
    }

    override fun reLoadData() {
        sendEvent(NavigateContract.Event.LoadData)
    }

    /**
     * 渲染 State
     */
    override fun renderState(state: NavigateContract.State) {

        when {

            state.isLoading -> {
                requestLoading(mBinding.navigateLlMain)
            }

            state.list.isNotEmpty() -> {

                requestSuccess()

                setNavigateData(
                    state.list,
                    state.currentPosition
                )
            }
        }
    }

    /**
     * 处理 Effect
     */
    override fun handleEffect(effect: NavigateContract.Effect) {

        when (effect) {

            is NavigateContract.Effect.ShowToast -> {
                toast(effect.msg)
            }
        }
    }

    /**
     * 右边联动左边
     */
    override fun check(position: Int, isScroll: Boolean) {
        setChecked(position, isScroll)
    }

    /**
     * 设置导航数据
     */
    private fun setNavigateData(
        datas: List<NavigateListEntity>,
        currentPosition: Int
    ) {

        mNavigateListEntities = datas

        val leftTitles = datas.map {
            it.name
        }

        if (mNavigateLeftBarAdapter == null) {

            mNavigateLeftBarAdapter = LeftBarAdapter(
                requireActivity(),
                leftTitles,
                object : RvListener {

                    override fun onItemClick(
                        id: Int,
                        position: Int
                    ) {

                        isMoved = true

                        targetPosition = position

                        sendEvent(
                            NavigateContract.Event.SelectLeftTab(
                                position
                            )
                        )

                        setChecked(position, true)
                    }
                })

            mBinding.navigateLeftSidebar.adapter =
                mNavigateLeftBarAdapter

        } else {

            mNavigateLeftBarAdapter?.setCheckedPosition(
                currentPosition
            )
        }

        createFragment()
    }

    /**
     * 设置选中状态
     *
     * @param position 位置
     * @param isLeft 是否左边点击
     */
    private fun setChecked(
        position: Int,
        isLeft: Boolean
    ) {

        if (isLeft) {

            mNavigateLeftBarAdapter?.setCheckedPosition(position)

            // 计算右边对应位置
            var count = 0

            for (i in 0 until position) {
                count += mNavigateListEntities[i].articles.size
            }

            count += position

            mTreeRightFragment?.setData(count)

            // 左边点击
            ItemHeaderDecoration.currentTag =
                targetPosition.toString()

        } else {

            if (isMoved) {

                isMoved = false

            } else {

                mNavigateLeftBarAdapter?.setCheckedPosition(
                    position
                )
            }

            // 右边滑动联动左边
            ItemHeaderDecoration.currentTag =
                position.toString()
        }

        moveToCenter(position)
    }

    /**
     * 将当前选中的 item 移动到中间
     */
    private fun moveToCenter(position: Int) {

        val firstVisiblePosition =
            mLinearLayoutManager.findFirstVisibleItemPosition()

        val childIndex = position - firstVisiblePosition

        if (childIndex < 0 ||
            childIndex >= mBinding.navigateLeftSidebar.childCount
        ) {
            return
        }

        val childView: View =
            mBinding.navigateLeftSidebar.getChildAt(childIndex)
                ?: return

        val offset =
            childView.top -
                    mBinding.navigateLeftSidebar.height / 2

        mBinding.navigateLeftSidebar.smoothScrollBy(
            0,
            offset
        )
    }

    /**
     * 创建右边 Fragment
     */
    private fun createFragment() {

        val fragmentTransaction =
            childFragmentManager.beginTransaction()

        mTreeRightFragment =
            childFragmentManager.findFragmentByTag(
                "navigatefragment"
            ) as? TreeRightFragment

        if (mTreeRightFragment == null) {

            mTreeRightFragment =
                TreeRightFragment.newInstance(true)

            mTreeRightFragment?.let {

                it.setListener(this)

                fragmentTransaction.replace(
                    R.id.navigate_right_sidebar,
                    it,
                    "navigatefragment"
                )

                fragmentTransaction.commitAllowingStateLoss()
            }
        }
    }
}