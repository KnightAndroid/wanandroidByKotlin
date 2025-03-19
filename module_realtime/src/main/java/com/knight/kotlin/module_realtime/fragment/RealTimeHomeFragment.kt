package com.knight.kotlin.module_realtime.fragment

import android.graphics.Color
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.knight.kotlin.library_base.annotation.EventBusRegister
import com.knight.kotlin.library_base.config.EventBusKeys
import com.knight.kotlin.library_base.event.MessageEvent
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.ktx.statusHeight
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.util.EventBusUtils
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_util.ThreadUtils
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.module_realtime.databinding.RealtimeHomeFragmentBinding
import com.knight.kotlin.module_realtime.enum.HotListEnum
import com.knight.kotlin.module_realtime.vm.RealTimeHomeVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.math.abs


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/11 16:02
 * @descript:热搜首页
 */
@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteFragment.RealTime.RealTimeHomeFragment)
class RealTimeHomeFragment : BaseFragment<RealtimeHomeFragmentBinding, RealTimeHomeVm>(),OnRefreshListener{

    private val mFragments = mutableListOf<Fragment>()

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        mViewModel.getMainBaiduRealTime().observerKt {
            var  mNavDatas = it.tabBoard

            for (index  in 0 until  it.tabBoard.size) {
                if (it.tabBoard.get(index).typeName == "homepage") {
                    mFragments.add(RealTimeMainFragment())
                } else if (it.tabBoard.get(index).typeName == HotListEnum.REALTIME.name.lowercase()
                    || it.tabBoard.get(index).typeName == HotListEnum.FINANCE.name.lowercase()
                    || it.tabBoard.get(index).typeName == HotListEnum.PHRASE.name.lowercase()
                    || it.tabBoard.get(index).typeName == HotListEnum.LIVELIHOOD.name.lowercase()){
                    mFragments.add(RealTimeTextFragment().also { fragment ->
                        fragment.arguments = bundleOf("typeName" to it.tabBoard.get(index).typeName.uppercase())
                    })
                } else {
                    mFragments.add(RealTimeMainFragment())
                }


//                try {
//                    CacheUtils.saveCacheValue(it.item_list[index].nav.type, Json.encodeToString(ListSerializer(EyeMetroCard.serializer(JsonObject.serializer())),it.item_list[index].card_list.get(0).card_data?.body?.metro_list!!))
//                    api_request = it.item_list[index].card_list.last().card_data?.body?.api_request
//                } catch (e: NullPointerException) {
//                    e.printStackTrace()
//                }
//                mFragments.add(RealTimeMainFragment().also {
//                    it.arguments = bundleOf("type" to mNavDatas[index].type,"api_Request" to Json.encodeToString(EyeApiRequest.serializer(), api_request?.let { it } ?: run{ EyeApiRequest() }))
//                })
            }

            if (mFragments.size > 0) {
                ViewInitUtils.setViewPager2Init(requireActivity(),mBinding.realtimeViewPager,mFragments,
                    isOffscreenPageLimit = true,
                    isUserInputEnabled = false
                )

                TabLayoutMediator(mBinding.realtimeTabLayout, mBinding.realtimeViewPager) { tab, pos ->
                    tab.text = mNavDatas[pos].text}.attach()

                mBinding.realtimeTabLayout.setTabTextColors(Color.parseColor("#BBABAB"), Color.parseColor("#F84C48"))
                //选中条颜色
                mBinding.realtimeTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#F84C48"))
            }





        }
    }

    override fun reLoadData() {

    }

    override fun RealtimeHomeFragmentBinding.initView() {
        realtimeHomeSmartfresh.setOnRefreshListener(this@RealTimeHomeFragment)
        mBinding.hideToolbar.apply {
            layoutParams.apply {
                setPadding(0, requireActivity().statusHeight + 48.dp2px(), 0, 0)
            }
        }


        mBinding.appbar.addOnOffsetChangedListener { appbarLayout, i ->


            EventBusUtils.postEvent(MessageEvent(MessageEvent.MessageType.AppBarOffsetChanged).put(EventBusKeys.REALTIMESCROLLORIENTATION,if (i>= appbarLayout.totalScrollRange){
                0
            }else 1).put(EventBusKeys.OFFSET,i))

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        when (event.type) {
            MessageEvent.MessageType.AppBarOffsetChanged -> {
                val mHeight: Float = 320.dp2px().toFloat()
                val mOffsetY = abs(event.getInt(EventBusKeys.OFFSET)).toFloat()
                val scale: Float = if (mOffsetY / mHeight > 1) 1.toFloat() else mOffsetY / mHeight
                val alpha: Int =(scale * 255).toInt()
                mBinding.realtimeTabLayout.setBackgroundColor(Color.argb(alpha, 254, 79, 76))
               if (mOffsetY != 0f) {
                   mBinding.realtimeTabLayout.setTabTextColors(Color.argb(255, 254,165,164), Color.argb(255, 255,255,255))
                   //选中条颜色
                   mBinding.realtimeTabLayout.setSelectedTabIndicatorColor( Color.argb(255, 255,255,255))
               } else {
                   mBinding.realtimeTabLayout.setTabTextColors(Color.argb(255, 254,165,164), Color.argb(255, 248,76,72))
                   mBinding.realtimeTabLayout.setSelectedTabIndicatorColor( Color.argb(255, 248,76,72))
               }



            }
            else ->{

            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        ThreadUtils.postMainDelayed({
            mBinding.realtimeHomeSmartfresh.finishRefresh()
        },2000)
    }

}