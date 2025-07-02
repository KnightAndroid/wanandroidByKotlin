package com.knight.kotlin.module_message.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.core.library_base.route.RouteFragment
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_message.adapter.MessageAdapter
import com.knight.kotlin.module_message.databinding.MessageFragmentBinding
import com.knight.kotlin.module_message.entity.MessageListEntity
import com.knight.kotlin.module_message.vm.MessageVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2023/5/16 16:11
 * Description:MessageFragment
 */

@AndroidEntryPoint
@Route(path = RouteFragment.Message.MessageFragment)
class MessageFragment : BaseFragment<MessageFragmentBinding, MessageVm>(), OnRefreshListener,
    OnLoadMoreListener {

    //页码
    private var page:Int = 1

    /**
     *
     * 是否已读
     */
    private var readed:Boolean = false

    private val mMessageAdapter:MessageAdapter by lazy {MessageAdapter()}


    companion object {
        fun newInstance(readed:Boolean): MessageFragment {
            val messageFragment = MessageFragment()
            val args = Bundle()
            args.putBoolean("readed",readed)
            messageFragment.arguments = args
            return messageFragment
        }
    }


    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun MessageFragmentBinding.initView() {
        readed = arguments?.getBoolean("readed") ?: false
        includeMessage.baseFreshlayout.setOnRefreshListener(this@MessageFragment)
        includeMessage.baseFreshlayout.setOnLoadMoreListener(this@MessageFragment)
        mBinding.includeMessage.baseBodyRv.init(LinearLayoutManager(activity),mMessageAdapter,false)
    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        requestLoading(mBinding.includeMessage.baseFreshlayout)
        if (readed) {
            mViewModel.getMessageByReaded(page).observerKt {
                setMessages(it)
            }
        } else {
            mViewModel.getMessageByUnReaded(page).observerKt {
                setMessages(it)
            }
        }

    }


    //设置数据
    private fun setMessages(data:MessageListEntity) {
        requestSuccess()
        mBinding.includeMessage.baseFreshlayout.finishRefresh()
        mBinding.includeMessage.baseFreshlayout.finishLoadMore()
        if (page == 1) {
            if (data.datas.size > 0) {
                mMessageAdapter.submitList(data.datas)
            } else {
                requestEmptyData()
            }

        } else {
            mMessageAdapter.addAll(data.datas)
        }
        if (data.datas.size < 10) {
            mBinding.includeMessage.baseFreshlayout.setEnableLoadMore(false)
        } else {
            page ++
        }


    }

    override fun reLoadData() {
        page = 1
        if (readed) {
            mViewModel.getMessageByReaded(page).observerKt {
                setMessages(it)
            }
        } else {
            mViewModel.getMessageByUnReaded(page).observerKt {
                setMessages(it)
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        page = 1
        mBinding.includeMessage.baseFreshlayout.setEnableLoadMore(true)
        if (readed) {
            mViewModel.getMessageByReaded(page).observerKt {
                setMessages(it)
            }
        } else {
            mViewModel.getMessageByUnReaded(page).observerKt {
                setMessages(it)
            }
        }
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        if (readed) {
            mViewModel.getMessageByReaded(page).observerKt {
                setMessages(it)
            }
        } else {
            mViewModel.getMessageByUnReaded(page).observerKt {
                setMessages(it)
            }
        }
    }
}