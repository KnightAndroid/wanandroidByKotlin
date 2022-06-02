package com.knight.kotlin.module_course.activity

import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.ktx.init
import com.knight.kotlin.library_base.ktx.observeLiveData
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
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
@Route(path = RouteActivity.Course.CourseListActivity)
class CourseListActivity : BaseActivity<CourseListActivityBinding,CourseListVm>(),OnRefreshListener,OnLoadMoreListener {
    override val mViewModel: CourseListVm by viewModels()

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
        observeLiveData(mViewModel.courseLists,::setCourseData)
    }

    override fun initRequestData() {
        mViewModel.getCourses()
    }

    override fun reLoadData() {
        mViewModel.getCourses()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mViewModel.getCourses()
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