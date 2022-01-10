package com.knight.kotlin.module_project.fragment

import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.module_project.databinding.ProjectActivityBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2021/12/23 16:33
 * Description:ProjectFragment
 *
 */
@AndroidEntryPoint
@Route(path = RouteFragment.Project.ProjectFragment)
class ProjectFragment:BaseFragment<ProjectActivityBinding, EmptyViewModel>() {
    override val mViewModel: EmptyViewModel by viewModels()
    override fun ProjectActivityBinding.initView() {

    }

    override fun initObserver() {
    }

    override fun initRequestData() {
    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }
}