package com.knight.kotlin.module_project.fragment

import com.core.library_base.ktx.toHtml
import com.core.library_base.route.RouteFragment
import com.knight.kotlin.library_base.fragment.BaseMviFragment
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.library_util.bindWechatViewPager2
import com.knight.kotlin.module_project.contract.ProjectContract
import com.knight.kotlin.module_project.databinding.ProjectActivityBinding
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
class ProjectFragment : BaseMviFragment<
        ProjectActivityBinding,
        ProjectVm,
        ProjectContract.Event,
        ProjectContract.State,
        ProjectContract.Effect>() {

    private val fragments = mutableListOf<ProjecArticleFragment>()
    private val titles = mutableListOf<String>()

    override fun ProjectActivityBinding.initView() {
        requestLoading(projectViewPager)
    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        sendEvent(ProjectContract.Event.LoadProjectTypes)
    }

    override fun setThemeColor(isDarkMode: Boolean) {}

    override fun reLoadData() {
        sendEvent(ProjectContract.Event.Retry)
    }

    // ❗这里只做UI状态，不做ViewPager初始化
    override fun renderState(state: ProjectContract.State) {
        when {
            state.isLoading -> requestLoading(mBinding.projectViewPager)
            state.error != null -> requestFailure()
            state.projectTypes.isNotEmpty() -> {
                requestSuccess()
            }
        }
    }

    // ✅ ViewPager初始化只在 Effect 中做（关键）
    override fun handleEffect(effect: ProjectContract.Effect) {
        when (effect) {
            is ProjectContract.Effect.ShowToast -> {
                // toast
            }

            is ProjectContract.Effect.InitViewPager -> {
                setupViewPager(effect.list)
            }
        }
    }

    private fun setupViewPager(list: List<com.knight.kotlin.module_project.entity.ProjectTypeBean>) {
        fragments.clear()
        titles.clear()

        list.forEachIndexed { index, item ->
            titles.add(item.name.toHtml().toString())
            fragments.add(
                ProjecArticleFragment.newInstance(
                    item.id,
                    index == 0
                )
            )
        }

        ViewInitUtils.setViewPager2Init(
            requireActivity(),
            mBinding.projectViewPager,
            fragments,
            isOffscreenPageLimit = true,
            isUserInputEnabled = false
        )

        mBinding.projectIndicator.bindWechatViewPager2(
            mBinding.projectViewPager,
            titles
        )
    }
}