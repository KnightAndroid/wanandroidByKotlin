package com.knight.kotlin.module_navigate.fragment

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.ktx.observeLiveData
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.module_navigate.R
import com.knight.kotlin.module_navigate.adapter.LeftBarAdapter
import com.knight.kotlin.module_navigate.databinding.NavigateFragmentBinding
import com.knight.kotlin.module_navigate.entity.NavigateListEntity
import com.knight.kotlin.module_navigate.listener.CheckListener
import com.knight.kotlin.module_navigate.listener.RvListener
import com.knight.kotlin.module_navigate.vm.NavigateVm
import com.knight.kotlin.module_navigate.widget.ItemHeaderDecoration
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/5/5 13:57
 * Description:NavigateFragment
 * 导航页面
 */
@AndroidEntryPoint
@Route(path = RouteFragment.Navigate.NavigateFragment)
class NavigateFragment : BaseFragment<NavigateFragmentBinding,NavigateVm>(),CheckListener{
    override val mViewModel: NavigateVm by viewModels()


    private var mNavigateLeftBarAdapter: LeftBarAdapter? = null
    private var mLinearLayoutManager: LinearLayoutManager? = null
    private var mNavigateListEntities: List<NavigateListEntity>? = null

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

    override fun NavigateFragmentBinding.initView() {
        requestLoading(navigateLlMain)
        mLinearLayoutManager = LinearLayoutManager(requireActivity())
        navigateLeftSidebar.layoutManager = mLinearLayoutManager
        navigateLeftSidebar.addItemDecoration(DividerItemDecoration(requireActivity(),DividerItemDecoration.VERTICAL))
    }

    override fun initObserver() {
        observeLiveData(mViewModel.navigateList,::setNavigateData)
    }

    override fun initRequestData() {
        mViewModel.getNavigateData()
    }

    override fun reLoadData() {
        mViewModel.getNavigateData()
    }

    override fun check(position: Int, isScroll: Boolean) {
        setChecked(position, isScroll)
    }

    private fun setNavigateData(datas:MutableList<NavigateListEntity>) {
        requestSuccess()
        mNavigateListEntities = datas
        val list: MutableList<String> = ArrayList()
        //左边名字
        for (i in datas.indices) {
            list.add(datas.get(i).name)
        }

        mNavigateLeftBarAdapter = LeftBarAdapter(requireActivity(),list,object:RvListener{
            override fun onItemClick(id: Int, position: Int) {
                mTreeRightFragment?.let {
                    isMoved = true
                    targetPosition = position
                    setChecked(position,true)
                }
            }
        })
        mBinding.navigateLeftSidebar.adapter = mNavigateLeftBarAdapter
        createFragment()

    }

    private fun setChecked(position: Int, isLeft: Boolean) {
        if (isLeft) {
            mNavigateLeftBarAdapter?.setCheckedPosition(position)
            //此处的位置需要根据每个分类的集合来进行计算
            var count = 0
            mNavigateListEntities?.let {
                for (i in 0 until position) {
                    count += it[i].articles.size
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
                mNavigateLeftBarAdapter?.setCheckedPosition(position)
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
            mBinding.navigateLeftSidebar.getChildAt(position - (mLinearLayoutManager?.findFirstVisibleItemPosition() ?:0))
        if (childAt != null) {
            val y: Int = childAt.top - mBinding.navigateLeftSidebar.getHeight() / 2
            mBinding.navigateLeftSidebar.smoothScrollBy(0, y)
        }
    }

    fun createFragment() {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        mTreeRightFragment =
            childFragmentManager.findFragmentByTag("navigatefragment") as TreeRightFragment?
        if (mTreeRightFragment == null) {
            mTreeRightFragment =TreeRightFragment.newInstance(true)
            mTreeRightFragment?.let{
                it.setListener(this)
                fragmentTransaction.replace(R.id.navigate_right_sidebar,it, "navigatefragment")
                fragmentTransaction.commitNowAllowingStateLoss()
            }



        }
    }
}