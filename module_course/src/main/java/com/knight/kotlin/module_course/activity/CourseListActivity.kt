package com.knight.kotlin.module_course.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.ktx.init
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_util.startPageWithParams
import com.knight.kotlin.library_widget.ktx.setItemClickListener
import com.knight.kotlin.module_course.R
import com.knight.kotlin.module_course.adapter.CourseListAdapter
import com.knight.kotlin.module_course.databinding.CourseListActivityBinding
import com.knight.kotlin.module_course.entity.CourseEntity
import com.knight.kotlin.module_course.vm.CourseListVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
@Route(path = RouteActivity.Course.CourseListActivity)
class CourseListActivity : BaseActivity<CourseListActivityBinding,CourseListVm>(),OnRefreshListener,OnLoadMoreListener {

    //工具类适配器
    private val mCourseListAdapter:CourseListAdapter by lazy {CourseListAdapter(arrayListOf())}
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun CourseListActivityBinding.initView() {
        includeCourseToolbar.baseTvTitle.setText(getString(R.string.course_titile))
        includeCourseToolbar.baseIvBack.setOnClick { finish() }
        includeCourseRv.baseBodyRv.init(LinearLayoutManager(this@CourseListActivity),mCourseListAdapter,false)
        includeCourseRv.baseFreshlayout.setOnRefreshListener(this@CourseListActivity)
        includeCourseRv.baseFreshlayout.setEnableLoadMore(false)
        requestLoading(includeCourseRv.baseFreshlayout)
        initListener()
    }

    override fun initObserver() {
    }

    override fun initRequestData() {
        mViewModel.getCourses().observerKt {
            setCourseData(it)
        }
    }

    override fun reLoadData() {
        mViewModel.getCourses().observerKt {
            setCourseData(it)
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mViewModel.getCourses().observerKt {
            setCourseData(it)
        }
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

    }

    private fun setCourseData(data:MutableList<CourseEntity>) {
        requestSuccess()
        mBinding.includeCourseRv.baseFreshlayout.finishRefresh()
        mCourseListAdapter.setNewInstance(data)

    }

    private fun initListener() {
        mCourseListAdapter.run {
            setItemClickListener { adapter, view, position ->
                startPageWithParams(
                    RouteActivity.Course.CourseDetailListActivity,
                    "cid" to data[position].id)
            }
        }
    }
}