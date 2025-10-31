package com.knight.kotlin.module_course.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.core.library_base.ktx.init
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_util.startPageWithParams
import com.knight.kotlin.library_widget.ktx.setSafeOnItemClickListener
import com.knight.kotlin.module_course.R
import com.knight.kotlin.module_course.adapter.CourseDetailListAdapter
import com.knight.kotlin.module_course.databinding.CourseDetailListActivityBinding
import com.knight.kotlin.module_course.entity.CourseDetailListEntity
import com.knight.kotlin.module_course.vm.CourseDetailListVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Param
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/6/2 17:08
 * Description:CourseDetailListActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Course.CourseDetailListActivity)
class CourseDetailListActivity: BaseActivity<CourseDetailListActivityBinding, CourseDetailListVm>(),OnRefreshListener,OnLoadMoreListener {

    @JvmField
    @Param(name = "cid")
    var cid:Int = 0

    private var page:Int = 0

    //课程列表类适配器
    private val mCourseDetailListAdapter: CourseDetailListAdapter by lazy { CourseDetailListAdapter() }
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun CourseDetailListActivityBinding.initView() {
        includeCourseDetailToolbar.baseTvTitle.setText(getString(R.string.course_detail_title))
        includeCourseDetailToolbar.baseIvBack.setOnClick { finish() }
        includeCourseDetailRv.baseBodyRv.init(LinearLayoutManager(this@CourseDetailListActivity),mCourseDetailListAdapter,true)
        includeCourseDetailRv.baseFreshlayout.setOnRefreshListener(this@CourseDetailListActivity)
        includeCourseDetailRv.baseFreshlayout.setOnLoadMoreListener(this@CourseDetailListActivity)
        requestLoading(includeCourseDetailRv.baseFreshlayout)
        initListener()
    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        mViewModel.getDetailCourses(page,cid).observerKt {
            setCourseDetailData(it)
        }
    }

    override fun reLoadData() {
        mViewModel.getDetailCourses(page,cid).observerKt {
            setCourseDetailData(it)
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        page = 0
        mBinding.includeCourseDetailRv.baseFreshlayout.setEnableLoadMore(true)
        mViewModel.getDetailCourses(page, cid).observerKt {
            setCourseDetailData(it)
        }
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mViewModel.getDetailCourses(page, cid).observerKt {
            setCourseDetailData(it)
        }
    }

    private fun setCourseDetailData(data:CourseDetailListEntity){
        requestSuccess()
        mBinding.includeCourseDetailRv.baseFreshlayout.finishRefresh()
        mBinding.includeCourseDetailRv.baseFreshlayout.finishLoadMore()
        if (data.datas.size > 0) {
            if (page == 0) {
                mCourseDetailListAdapter.submitList(data.datas)
            } else {
                mCourseDetailListAdapter.addAll(data.datas)
            }

            if (data.datas.size == 0) {
                mBinding.includeCourseDetailRv.baseFreshlayout.setEnableLoadMore(false)
            } else {
                page ++
            }
        } else {
            mBinding.includeCourseDetailRv.baseFreshlayout.setEnableLoadMore(false)
        }
    }

    private fun initListener() {
        mCourseDetailListAdapter.run {
            setSafeOnItemClickListener { adapter, view, position ->
                startPageWithParams(
                    RouteActivity.Web.WebPager,
                    "webUrl" to items[position].link,
                    "webTitle" to items[position].title)
            }
        }
    }
}