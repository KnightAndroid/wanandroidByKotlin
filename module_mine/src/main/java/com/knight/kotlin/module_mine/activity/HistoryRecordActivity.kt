package com.knight.kotlin.module_mine.activity

import android.graphics.Point
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.ktx.getUser
import com.knight.kotlin.library_base.ktx.init
import com.knight.kotlin.library_base.ktx.observeLiveData
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.util.ArouteUtils
import com.knight.kotlin.library_database.entity.HistoryReadRecordsEntity
import com.knight.kotlin.library_util.DialogUtils
import com.knight.kotlin.library_widget.floatmenu.FloatMenu
import com.knight.kotlin.library_widget.ktx.setItemClickListener
import com.knight.kotlin.module_mine.R
import com.knight.kotlin.module_mine.adapter.HistoryRecordAdapter
import com.knight.kotlin.module_mine.databinding.MineHistoryrecordActivityBinding
import com.knight.kotlin.module_mine.vm.HistoryRecordViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/5/17 17:26
 * Description:HistoryRecordActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Mine.HistoryRecordActivity)
class HistoryRecordActivity :BaseActivity<MineHistoryrecordActivityBinding,HistoryRecordViewModel>(),OnLoadMoreListener,OnRefreshListener{
    override val mViewModel: HistoryRecordViewModel by viewModels()

    private var endStation = 10
    private var startPosition = 0
    private var deleteSelectItem = -1
    private val point = Point()
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    private val mHistoryRecordAdapter:HistoryRecordAdapter by lazy { HistoryRecordAdapter(arrayListOf()) }

    override fun MineHistoryrecordActivityBinding.initView() {
        includeHistoryrecordToolbar.baseTvTitle.setText(getString(R.string.mine_me_history_records))
        includeHistoryrecordToolbar.baseIvBack.setOnClick { finish() }
        includeHistoryrecordToolbar.baseTvRight.visibility = View.VISIBLE
        includeHistoryrecordToolbar.baseTvRight.setText(getString(R.string.mine_cleanall))
        includeHistoryrecordToolbar.baseTvRight.setOnClick {
            DialogUtils.getConfirmDialog(this@HistoryRecordActivity,getString(R.string.mine_cancel_all_historyrecord),
                {dialog, which ->
                    mViewModel.deleteAllHistoryRecord()
                }){
                    dialog, which ->
            }

        }


        includeHistoryrecordRv.baseBodyRv.init(LinearLayoutManager(this@HistoryRecordActivity),mHistoryRecordAdapter,true)
        includeHistoryrecordRv.baseFreshlayout.setOnLoadMoreListener(this@HistoryRecordActivity)
        includeHistoryrecordRv.baseFreshlayout.setOnRefreshListener(this@HistoryRecordActivity)
        requestLoading(includeHistoryrecordRv.baseFreshlayout)
        initListener()
    }

    override fun initObserver() {
        observeLiveData(mViewModel.historyReadcords,::getHistoryRecords)
        observeLiveData(mViewModel.deleteHistoryRecordSuccess,::deleteHistoryRecord)
        observeLiveData(mViewModel.deleteAllHistoryRecordSuccess,::deleteAllHistoryRecord)
    }

    override fun initRequestData() {
        mViewModel.queryPartHistoryRecords(startPosition,endStation, getUser()?.id ?: 0)
    }

    override fun reLoadData() {
        startPosition = 0
        endStation = 10
        mViewModel.queryPartHistoryRecords(startPosition,endStation, getUser()?.id ?: 0)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mViewModel.queryPartHistoryRecords(startPosition,endStation + 10,getUser()?.id ?: 0)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        startPosition = 0
        endStation = 10
        mViewModel.queryPartHistoryRecords(startPosition,endStation, getUser()?.id ?: 0)
    }

    private fun getHistoryRecords(data:MutableList<HistoryReadRecordsEntity>) {
        mBinding.includeHistoryrecordRv.baseFreshlayout.finishRefresh()
        mBinding.includeHistoryrecordRv.baseFreshlayout.finishLoadMore()
        requestSuccess()
        if (startPosition == 0) {
            if (data.size == 0) {
                requestEmptyData()
            } else {
                mHistoryRecordAdapter.setNewInstance(data)
                if (data.size == 10) {
                    mBinding.includeHistoryrecordRv.baseFreshlayout.setEnableLoadMore(true)
                    startPosition = endStation
                } else {
                    mBinding.includeHistoryrecordRv.baseFreshlayout.setEnableLoadMore(false)
                }
            }
        } else {
            mHistoryRecordAdapter.addData(data)
            if (data.size == 10) {
                mBinding.includeHistoryrecordRv.baseFreshlayout.setEnableLoadMore(true)
                startPosition = endStation
            } else {
                mBinding.includeHistoryrecordRv.baseFreshlayout.setEnableLoadMore(false)
            }
        }

    }


    private fun initListener() {
        mHistoryRecordAdapter.run {
            setItemClickListener { adapter, view, position ->
                ArouteUtils.startWebArticle(
                    data[position].webUrl,
                    data[position].title,
                    data[position].articleId,
                    data[position].isCollect,
                    data[position].envelopePic,
                    data[position].articledesc,
                    data[position].chapterName,
                    data[position].author,
                    ""
                )

            }

            setOnItemLongClickListener { adapter, view, position ->
                val floatMenu = FloatMenu(this@HistoryRecordActivity,view)
                floatMenu.items("删除")
                floatMenu.show(point)
                floatMenu.setOnItemClickListener(object:FloatMenu.OnItemClickListener{
                    override fun onClick(v: View?, itemPosition: Int) {
                        deleteSelectItem = position
                        mViewModel.deleteHistoryRecord(data[position].id)
                    }
                })
                false
            }
        }
    }

    /**
     * 删除指定阅读历史记录成功
     *
     */
    private fun deleteHistoryRecord(data:Boolean) {
        mHistoryRecordAdapter.data.removeAt(deleteSelectItem)
        mHistoryRecordAdapter.notifyItemRemoved(deleteSelectItem)
    }


    /**
     *
     * 删除全部阅读历史数据
     */
    private fun deleteAllHistoryRecord(data:Boolean) {
        onRefresh(mBinding.includeHistoryrecordRv.baseFreshlayout)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            point.x = ev.rawX.toInt()
            point.y = ev.rawY.toInt()
        }
        return super.dispatchTouchEvent(ev)
    }

}