package com.knight.kotlin.module_project.fragment

import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.ktx.observeLiveData
import com.knight.kotlin.library_base.ktx.toHtml
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.library_util.bindWechatViewPager2
import com.knight.kotlin.module_project.databinding.ProjectActivityBinding
import com.knight.kotlin.module_project.entity.ProjectTypeBean
import com.knight.kotlin.module_project.vm.ProjectVm
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
    override val mViewModel: ProjectVm by viewModels()

    override fun ProjectActivityBinding.initView() {
        requestLoading(projectViewPager)
    }

    override fun initObserver() {
        observeLiveData(mViewModel.projectTitles,::setProjectTypes)
    }

    override fun initRequestData() {
        mViewModel.getProjectTitle()
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