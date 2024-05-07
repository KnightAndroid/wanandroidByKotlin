package com.knight.kotlin.module_project.fragment

import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.ktx.toHtml
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.library_util.bindWechatViewPager2
import com.knight.kotlin.module_project.databinding.ProjectActivityBinding
import com.knight.kotlin.module_project.entity.ProjectTypeBean
import com.knight.kotlin.module_project.vm.ProjectVm
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2021/12/23 16:33
 * Description:ProjectFragment
 *
 */
@AndroidEntryPoint
@Route(path = RouteFragment.Project.ProjectFragment)
class ProjectFragment:BaseFragment<ProjectActivityBinding, ProjectVm>() {

    private val mFragments = mutableListOf<ProjecArticleFragment>()
    private val mTitileDatas = mutableListOf<String>()

    override fun ProjectActivityBinding.initView() {
        requestLoading(projectViewPager)
    }

    override fun initObserver() {
    }

    override fun initRequestData() {
        mViewModel.getProjectTitle().observerKt {
            setProjectTypes(it)
        }
    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun reLoadData() {
        mViewModel.getProjectTitle()
    }

    private fun setProjectTypes(projectTypes:MutableList<ProjectTypeBean>) {
        requestSuccess()
        mFragments.clear()
        mTitileDatas.clear()
        val projectTypeBean = ProjectTypeBean()
        projectTypeBean.name = "最新项目"
        projectTypes.add(projectTypeBean)
        mTitileDatas.add(projectTypeBean.name)
        mFragments.add(ProjecArticleFragment.newInstance(0,true))
        for (i in projectTypes.indices) {
            mTitileDatas.add(projectTypes[i].name.toHtml().toString())
            mFragments.add(ProjecArticleFragment.newInstance(projectTypes[i].id, false))
        }
        ViewInitUtils.setViewPager2Init(requireActivity(),mBinding.projectViewPager,mFragments,
            isOffscreenPageLimit = true,
            isUserInputEnabled = false
        )
        mBinding.projectIndicator.bindWechatViewPager2(mBinding.projectViewPager,mTitileDatas)


    }
}