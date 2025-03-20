package com.knight.kotlin.module_realtime.fragment


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.module_realtime.R
import com.knight.kotlin.module_realtime.databinding.RealtimeNovelFragmentBinding
import com.knight.kotlin.module_realtime.vm.RealTimeNovelVm
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/20 9:30
 * @descript:小说界面
 */
@AndroidEntryPoint
@Route(path = RouteFragment.RealTime.RealTimeNovelFragment)
class RealTimeNovelFragment : BaseFragment<RealtimeNovelFragmentBinding,RealTimeNovelVm>() {
    private val mFragments = mutableListOf<Fragment>()





    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
         mViewModel.getDataByTab("wise","novel").observerKt {
             var  mNavDatas = it.tag.get(0).content

             for (index  in 0 until  it.tag.get(0).content.size) {
                mFragments.add(RealTimeNovelChildFragment().also { fragment ->
                    fragment.arguments = bundleOf("category" to it.tag.get(0).content.get(index))
                })

             }

             if (mFragments.size > 0) {
                 ViewInitUtils.setViewPager2Init(requireActivity(),mBinding.realtimeNovelViewPager,mFragments,
                     isOffscreenPageLimit = true,
                     isUserInputEnabled = false
                 )

                 TabLayoutMediator(mBinding.realtimeNovelTabLayout, mBinding.realtimeNovelViewPager) { tab, pos ->
                     tab.text = mNavDatas[pos]}.attach()

                 //mBinding.realtimeNovelTabLayout.setTabTextColors(Color.parseColor("#BBABAB"), Color.parseColor("#F84C48"))
                 //选中条颜色
                // mBinding.realtimeNovelTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#F84C48"))

                 for (i in 0 until mBinding.realtimeNovelTabLayout.tabCount) {
                     val tab = mBinding.realtimeNovelTabLayout.getTabAt(i)
                     tab?.setCustomView(setTabView(requireActivity(),mNavDatas[i]))
                 }
             }
         }
    }

    override fun reLoadData() {

    }

    override fun RealtimeNovelFragmentBinding.initView() {

    }

    fun setTabView(context: Context,title:String): View {
        val mInflater = LayoutInflater.from(context)
        val view: View = mInflater.inflate(R.layout.realtime_novel_tab_indicator, null)
        val tv = view.findViewById<TextView>(R.id.tab_text)
        tv.setText(title)
        return view
    }

}