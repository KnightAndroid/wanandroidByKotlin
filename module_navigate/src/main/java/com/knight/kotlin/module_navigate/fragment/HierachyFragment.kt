package com.knight.kotlin.module_navigate.fragment

import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.module_navigate.R
import com.knight.kotlin.module_navigate.adapter.LeftBarAdapter
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
class HierachyFragment : BaseFragment<NavigateHierachyFragmentBinding, HierachyVm>(),CheckListener {


    private var mHierachyLeftBarAdapter: LeftBarAdapter? = null
    private var mLinearLayoutManager: LinearLayoutManager? = null
    private var mHierachyListEntities: List<HierachyListEntity>? = null

    /**
     *
     * 右边选择
     */
    private var mTreeRightFragment: TreeRightFragment? = null

    /**
     * 点击左边某一个具体的item的位置
     *
     */
    private var targetPosition = 0
    private var isMoved = false
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun NavigateHierachyFragmentBinding.initView() {
        requestLoading(hierachyLl)
        mLinearLayoutManager = LinearLayoutManager(requireActivity())
        hierachyLeftSidebar.layoutManager = mLinearLayoutManager
        hierachyLeftSidebar.addItemDecoration(
            DividerItemDecoration(
                requireActivity(),
                DividerItemDecoration.VERTICAL
            )
        )
    }

    override fun initRequestData() {
        mViewModel.getHierachyData().observerKt {
            setHierachyData(it)
        }
    }

    override fun reLoadData() {
        mViewModel.getHierachyData().observerKt {
            setHierachyData(it)
        }
    }

    override fun initObserver() {

    }

    fun setHierachyData(datas:MutableList<HierachyListEntity>) {
        requestSuccess()
        mHierachyListEntities = datas
        val list: MutableList<String> = ArrayList()
        //左边名字
        for (i in datas.indices) {
            list.add(datas.get(i).name)
        }

        mHierachyLeftBarAdapter = LeftBarAdapter(requireActivity(),list,object: RvListener {
            override fun onItemClick(id: Int, position: Int) {
                mTreeRightFragment?.let {
                    isMoved = true
                    targetPosition = position
                    setChecked(position,true)
                }
            }
        })
        mBinding.hierachyLeftSidebar.adapter = mHierachyLeftBarAdapter
        createFragment()

    }

    private fun setChecked(position: Int, isLeft: Boolean) {
        if (isLeft) {
            mHierachyLeftBarAdapter?.setCheckedPosition(position)
            //此处的位置需要根据每个分类的集合来进行计算
            var count = 0
            mHierachyListEntities?.let {
                for (i in 0 until position) {
                    count += it[i].children.size
                }
            }
            count += position
            mTreeRightFragment?.setData(count)
            //凡是点击左边，将左边点击的位置作为当前的tag
            ItemHeaderDecoration.currentTag = targetPosition.toString()
        } else {
            if (isMoved) {
                isMoved = false
            } else {
                mHierachyLeftBarAdapter?.setCheckedPosition(position)
            }

            //如果是滑动右边联动左边，则按照右边传过来的位置作为tag
            ItemHeaderDecoration.currentTag = position.toString()
        }
        moveToCenter(position)
    }

    /**
     *
     * 将当前选中的item居中
     * @param position 位置
     */
    private fun moveToCenter(position: Int) {
        //将点击的position转换为当前屏幕上可见的item的位置以便于计算距离顶部的高度，从而进行移动居中
        val childAt: View =
            mBinding.hierachyLeftSidebar.getChildAt(position - (mLinearLayoutManager?.findFirstVisibleItemPosition() ?:0))
        if (childAt != null) {
            val y: Int = childAt.top - mBinding.hierachyLeftSidebar.getHeight() / 2
            mBinding.hierachyLeftSidebar.smoothScrollBy(0, y)
        }
    }

    override fun check(position: Int, isScroll: Boolean) {
        setChecked(position, isScroll)
    }
    fun createFragment() {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        mTreeRightFragment =
            childFragmentManager.findFragmentByTag("hierachyfragment") as TreeRightFragment?
        if (mTreeRightFragment == null) {
            mTreeRightFragment =TreeRightFragment.newInstance(false)
            mTreeRightFragment?.let{
                it.setListener(this)
                fragmentTransaction.replace(R.id.hierachy_right_sidebar,it, "hierachyfragment")
                fragmentTransaction.commitNowAllowingStateLoss()
            }



        }
    }

}