package com.knight.kotlin.module_course.activity

import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.ktx.init
import com.knight.kotlin.library_base.ktx.observeLiveData
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_util.startPageWithParams
import com.knight.kotlin.library_widget.ktx.setItemClickListener
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
class CourseDetailListActivity:BaseActivity<CourseDetailListActivityBinding,CourseDetailListVm>(),OnRefreshListener,OnLoadMoreListener {
    override val mViewModel: CourseDetailListVm by viewModels()


    @JvmField
    @Param(name = "cid")
    var cid:Int = 0

    private var page:Int = 0

    //课程列表类适配器
    private val mCourseDetailListAdapter: CourseDetailListAdapter by lazy { CourseDetailListAdapter(arrayListOf()) }
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
        observeLiveData(mViewModel.courseDetailList,::setCourseDetailData)
    }

    override fun initRequestData() {
        mViewModel.getDetailCourses(page,cid)
    }

    override fun reLoadData() {
        mViewModel.getDetailCourses(page,cid)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        page = 0
        mBinding.includeCourseDetailRv.baseFreshlayout.setEnableLoadMore(true)
        mViewModel.getDetailCourses(page, cid)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mViewModel.getDetailCourses(page, cid)
    }

    private fun setCourseDetailData(data:CourseDetailListEntity){
        requestSuccess()
        mBinding.includeCourseDetailRv.baseFreshlayout.finishRefresh()
        mBinding.includeCourseDetailRv.baseFreshlayout.finishLoadMore()
        if (data.datas.size > 0) {
            if (page == 0) {
                mCourseDetailListAdapter.setNewInstance(data.datas)
            } else {
                mCourseDetailListAdapter.addData(data.datas)
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
            setItemClickListener { adapter, view, position ->
                startPageWithParams(
                    RouteActivity.Web.WebPager,
                    "webUrl" to data[position].link,
                    "webTitle" to data[position].title)
            }
        }
    }
}