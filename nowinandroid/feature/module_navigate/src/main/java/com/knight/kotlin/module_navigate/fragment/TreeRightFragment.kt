package com.knight.kotlin.module_navigate.fragment

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.core.library_base.route.RouteActivity
import com.core.library_base.route.RouteFragment
import com.knight.kotlin.library_base.utils.ArouteUtils
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.knight.kotlin.library_util.startPageWithParams
import com.knight.kotlin.module_navigate.R
import com.knight.kotlin.module_navigate.adapter.HierachyClassifyDetailAdapter
import com.knight.kotlin.module_navigate.databinding.NavigateRightTreeFragmentBinding
import com.knight.kotlin.module_navigate.entity.HierachyChildrenEntity
import com.knight.kotlin.module_navigate.entity.HierachyListEntity
import com.knight.kotlin.module_navigate.entity.HierachyRightBeanEntity
import com.knight.kotlin.module_navigate.entity.NavigateChildrenEntity
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
class TreeRightFragment : BaseFragment<NavigateRightTreeFragmentBinding, NavigateRightTreeVm>(),CheckListener {
    private lateinit var mHierachyClassifyDetailAdapter:HierachyClassifyDetailAdapter

    private val mDatas: ArrayList<HierachyRightBeanEntity> = ArrayList<HierachyRightBeanEntity>()
    private lateinit var mDecoration: ItemHeaderDecoration
    private var move = false
    private var mIndex = 0
    private var checkListener: CheckListener? = null
    private var isNavigate = false
    private var mManager: FlexboxLayoutManager? = null



    companion object {
        fun newInstance(isNavigate: Boolean): TreeRightFragment {
            val treeRightFragment: TreeRightFragment = TreeRightFragment()
            val args = Bundle()
            args.putBoolean("isNavigate",isNavigate)
            treeRightFragment.setArguments(args)
            return treeRightFragment
        }
    }
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun NavigateRightTreeFragmentBinding.initView() {
        requestLoading(navigateLlRight)
        isNavigate = arguments?.getBoolean("isNavigate") ?: false
        treeTightRv.addOnScrollListener(RecyclerViewListener())
        mManager = FlexboxLayoutManager(requireActivity())
        mManager?.flexDirection = FlexDirection.ROW
        //左对齐
        //左对齐
        mManager?.justifyContent = JustifyContent.FLEX_START
        mManager?.alignItems = AlignItems.CENTER
        treeTightRv.layoutManager = mManager
        mHierachyClassifyDetailAdapter = HierachyClassifyDetailAdapter(requireActivity(),mDatas,object:RvListener{
            override fun onItemClick(id: Int, position: Int) {
                //点击标题
                if (id == R.id.navigate_root) {
                    if (!isNavigate) {
                        //不是导航才能跳到tab切换页面
                        GoRouter.getInstance().build(RouteActivity.Navigate.HierachyTabActivity)
                            .withStringArrayList("childrenNames",ArrayList(mDatas.get(position).childrenName))
                            .withIntegerArrayList("cids",ArrayList(mDatas.get(position).cid))
                            .withString("titleName",mDatas.get(position).titleName)
                            .go()
                    }
                } else if (id == R.id.navigate_tv_content) {
                    if (isNavigate) {
                        //是导航的话 点击子item 直接跳到webview
                        ArouteUtils.startWebArticle(mDatas[position].link,mDatas[position].name,mDatas[position].id,mDatas[position].collect,"","",mDatas[position].name,mDatas[position].name,"")
                    } else {
                        //如果是体系直接跳到列表页面
                        startPageWithParams(RouteActivity.Navigate.HierachyDetailActivity,"cid" to mDatas.get(position).id,"titleName" to mDatas.get(position).name)
                    }
                }
            }
        })
        treeTightRv.adapter = mHierachyClassifyDetailAdapter

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        if (isNavigate) {
            mViewModel.getTreeNavigateData().observerKt {
                setNavigateData(it)
            }
        } else {
            mViewModel.getTreeHierachyData().observerKt {
                setHierachyData(it)
            }
        }
    }

    override fun reLoadData() {
        if (isNavigate) {
            mViewModel.getTreeNavigateData().observerKt {
                setNavigateData(it)
            }
        } else {
            mViewModel.getTreeHierachyData().observerKt {
                setHierachyData(it)
            }
        }
    }

    fun setListener(listener: CheckListener) {
        checkListener = listener
    }

    inner class RecyclerViewListener : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (move && newState == RecyclerView.SCROLL_STATE_IDLE) {
                move = false
                val n: Int = mIndex - (mManager?.findFirstVisibleItemPosition() ?:0)
                if (0 <= n && n < mBinding.treeTightRv.getChildCount()) {
                    val top: Int = mBinding.treeTightRv.getChildAt(n).getTop()
                    mBinding.treeTightRv.smoothScrollBy(0, top)
                }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (move) {
                move = false
                val n: Int = mIndex - (mManager?.findFirstVisibleItemPosition() ?: 0)
                if (0 <= n && n < mBinding.treeTightRv.getChildCount()) {
                    val top: Int = mBinding.treeTightRv.getChildAt(n).getTop()
                    mBinding.treeTightRv.scrollBy(0, top)
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
        val firstItem = mManager?.findFirstVisibleItemPosition() ?: 0
        val lastItem = mManager?.findLastVisibleItemPosition() ?: 0
        if (n <= firstItem) {
            if (n < (firstItem -10)) {
                mBinding.treeTightRv.scrollToPosition(n+ 5)
            } else {
                mBinding.treeTightRv.scrollToPosition(n)
            }

        } else if (n <= lastItem) {
            val top: Int = mBinding.treeTightRv.getChildAt(n - firstItem).getTop()
            mBinding.treeTightRv.scrollBy(0, top)
        } else {
            mBinding.treeTightRv.scrollToPosition(n)
            move = true
        }
    }


    /**
     *
     * 获取导航数据
     *
     */
    fun setNavigateData(data:MutableList<NavigateListEntity>){
        requestSuccess()
        for (i in data.indices) {
            val hierachyRightBeanEntity = HierachyRightBeanEntity()
            hierachyRightBeanEntity.name = data[i].name
            hierachyRightBeanEntity.isTitle = true
            hierachyRightBeanEntity.titleName = data[i].name
            hierachyRightBeanEntity.tag = i.toString()
            mDatas.add(hierachyRightBeanEntity)
            val navigateChildrenEntities: List<NavigateChildrenEntity> = data[i].articles
            for (j in navigateChildrenEntities.indices) {
                val hierachyRightBodyBeanEntity = HierachyRightBeanEntity()
                hierachyRightBodyBeanEntity.name = navigateChildrenEntities[j].title
                hierachyRightBodyBeanEntity.tag = i.toString()
                hierachyRightBodyBeanEntity.titleName = navigateChildrenEntities[j].superChapterName
                hierachyRightBodyBeanEntity.link = navigateChildrenEntities[j].link
                hierachyRightBodyBeanEntity.id = navigateChildrenEntities[j].id
                hierachyRightBodyBeanEntity.isTitle = false
                hierachyRightBodyBeanEntity.collect = navigateChildrenEntities[j].collect
                mDatas.add(hierachyRightBodyBeanEntity)
            }
        }
        mDecoration = ItemHeaderDecoration(requireActivity(),mDatas)
        mBinding.treeTightRv.addItemDecoration(mDecoration)
        checkListener?.let {
            mDecoration.setCheckListener(it)
        }
        mHierachyClassifyDetailAdapter.notifyDataSetChanged()
        mDecoration.setData(mDatas)
    }

    /**
     * 获取体系数据
     *
     */
    fun setHierachyData(data:MutableList<HierachyListEntity>){
        requestSuccess()
        var total = 0
        for (i in data.indices) {
            //一级标签 也就是大标题
            val hierachyRightBeanEntity = HierachyRightBeanEntity()
            hierachyRightBeanEntity.name = data[i].name
            hierachyRightBeanEntity.isTitle = true
            hierachyRightBeanEntity.titleName = data[i].name
            hierachyRightBeanEntity.tag = i.toString()
            hierachyRightBeanEntity.id = data[i].id
            hierachyRightBeanEntity.parentName = data[i].name
            hierachyRightBeanEntity.total = data[i].children.size
            hierachyRightBeanEntity.position = total
            val hierachyChildrenEntities1: List<HierachyChildrenEntity> = data[i].children
            val childName = ArrayList<String>()
            val childCid = ArrayList<Int>()
            for (k in hierachyChildrenEntities1.indices) {
                childName.add(hierachyChildrenEntities1[k].name)
                childCid.add(hierachyChildrenEntities1[k].id)
            }
            hierachyRightBeanEntity.childrenName = childName
            hierachyRightBeanEntity.cid = childCid
            mDatas.add(hierachyRightBeanEntity)
            total++
            val hierachyChildrenEntities: List<HierachyChildrenEntity> = data[i].children
            for (j in hierachyChildrenEntities.indices) {
                val hierachyRightBodyBeanEntity = HierachyRightBeanEntity()
                hierachyRightBodyBeanEntity.name = hierachyChildrenEntities[j].name
                hierachyRightBodyBeanEntity.tag = i.toString()
                hierachyRightBodyBeanEntity.titleName = data[i].name
                hierachyRightBodyBeanEntity.id = hierachyChildrenEntities[j].id
                hierachyRightBodyBeanEntity.position = total
                total++
                mDatas.add(hierachyRightBodyBeanEntity)
            }
        }
        mDecoration = ItemHeaderDecoration(requireActivity(),mDatas)
        mBinding.treeTightRv.addItemDecoration(mDecoration)
        checkListener?.let {
            mDecoration.setCheckListener(it)
        }
        mHierachyClassifyDetailAdapter.notifyDataSetChanged()
        mDecoration.setData(mDatas)

    }



    override fun check(position: Int, isScroll: Boolean) {
        checkListener?.check(position, isScroll)
    }

}