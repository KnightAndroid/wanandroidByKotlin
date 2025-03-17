package com.knight.kotlin.module_realtime.fragment

import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_realtime.adapter.RealTimeHotMainAdapter
import com.knight.kotlin.module_realtime.databinding.RealtimeMainFragmentBinding
import com.knight.kotlin.module_realtime.databinding.RealtimeTabBoardFootItemBinding
import com.knight.kotlin.module_realtime.databinding.RealtimeTabBoardHeadItemBinding
import com.knight.kotlin.module_realtime.vm.RealTimeHomeVm
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/11 16:43
 * @descript:
 */

@AndroidEntryPoint
@Route(path = RouteFragment.RealTime.RealTimeMainFragment)
class RealTimeMainFragment : BaseFragment<RealtimeMainFragmentBinding, RealTimeHomeVm>() {

    //热搜适配器
    private val mRealTimeHotMainAdapter: RealTimeHotMainAdapter by lazy { RealTimeHotMainAdapter() }



    private lateinit var mHeaderBinding: RealtimeTabBoardHeadItemBinding
    private lateinit var mFootBinding: RealtimeTabBoardFootItemBinding

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        mViewModel.getMainBaiduRealTime().observerKt {
            mRealTimeHotMainAdapter.submitList(it.cards.get(0).content)
            initHeadTabBoard()
        }
    }

    /**
     *
     * 初始化个榜单头部
     */
    private fun initHeadTabBoard() {
        if (mBinding.rvHotList.headerCount == 0) {
            if (!::mHeaderBinding.isInitialized) {
                mHeaderBinding =
                    RealtimeTabBoardHeadItemBinding.inflate(LayoutInflater.from(requireActivity()))
               mBinding.rvHotList.addHeaderView(mHeaderBinding.root)
            }
        }

       // mBinding.rvHotList.addItemDecoration(RecycleviewHeaderItemDecoration(mHeaderBinding.root))


//        if (mBinding.rvHotList.footerCount == 0) {
//            if (!::mFootBinding.isInitialized) {
//                mFootBinding =
//                    RealtimeTabBoardFootItemBinding.inflate(LayoutInflater.from(requireActivity()))
//                mBinding.rvHotList.addFooterView(mHeaderBinding.root)
//            }
//        }
    }

    override fun reLoadData() {

    }

    override fun RealtimeMainFragmentBinding.initView() {
        rvHotList.init(LinearLayoutManager(requireActivity()), mRealTimeHotMainAdapter,false)
    }
}