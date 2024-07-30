package com.knight.kotlin.module_home.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.reflect.TypeToken
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.util.GsonUtils
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_util.JsonUtils
import com.knight.kotlin.library_util.startPage
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_home.adapter.EyepetizerCategoryAdapter
import com.knight.kotlin.module_home.databinding.HomeEyeClassifyFragmentBinding
import com.knight.kotlin.module_home.entity.EyeCategoryBean
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2024/4/18 10:42
 * Description:HomeEyeClassifyFragment
 */
@AndroidEntryPoint
@Route(path = RouteFragment.Home.HomeEyeClassifyFragment)
class HomeEyeClassifyFragment : BaseFragment<HomeEyeClassifyFragmentBinding,EmptyViewModel>() {

    private val mOpenSourceAdapter: EyepetizerCategoryAdapter by lazy { EyepetizerCategoryAdapter() }

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun HomeEyeClassifyFragmentBinding.initView() {
        mBinding.root.rotation = 180f
        initEyeCategoryAdapter()

    }

    private fun initEyeCategoryAdapter() {
        mBinding.rvEyepetizerCategory.init(LinearLayoutManager(requireActivity()), mOpenSourceAdapter,false)
        //初始化标签
        val type = object : TypeToken<List<EyeCategoryBean>>() {}.type
        val jsonData: String = JsonUtils.getJson(requireActivity(), "eyecategory.json")
        val mDataList: MutableList<EyeCategoryBean> =
            GsonUtils.getList(jsonData, type)
        mOpenSourceAdapter.submitList(mDataList)

        mOpenSourceAdapter.run {
            setOnItemClickListener { adapter, view, position ->
                startPage(RouteActivity.EyeDaily.DailyListActivity)

            }
        }
    }

}