package com.knight.kotlin.module_home.dialog

import android.view.Gravity
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.fragment.BaseDialogFragment
import com.knight.kotlin.library_base.ktx.getScreenHeight
import com.knight.kotlin.library_base.ktx.init
import com.knight.kotlin.library_base.util.StatusBarUtils
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_widget.DividerItemDecoration
import com.knight.kotlin.library_widget.citypicker.CityBean
import com.knight.kotlin.library_widget.citypicker.CityEnum
import com.knight.kotlin.library_widget.citypicker.CityListAdapter
import com.knight.kotlin.library_widget.citypicker.CityListBean
import com.knight.kotlin.library_widget.citypicker.InnerListener
import com.knight.kotlin.library_widget.citypicker.SectionItemDecoration
import com.knight.kotlin.library_widget.citypicker.view.SideIndexBar
import com.knight.kotlin.module_home.databinding.HomeCityDialogPickerBinding
import com.knight.kotlin.module_home.vm.HomeCityGroupVm
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/17 16:56
 * @descript:选择城市列表弹窗
 */
@AndroidEntryPoint
class HomeCityGroupFragment:BaseDialogFragment<HomeCityDialogPickerBinding,HomeCityGroupVm>(),
    SideIndexBar.OnIndexTouchedChangedListener,InnerListener {
    override fun getGravity()= Gravity.BOTTOM
    override fun cancelOnTouchOutSide() = true

    override fun initObserver() {

    }

    private val mCityListAdapter: CityListAdapter by lazy { CityListAdapter(mutableListOf()) }


    override fun initRequestData() {
       mViewModel.getCityGroupData("pc").observe(this,{data->

           val locationData = CityBean("深圳","广东")
           val locations = mutableListOf<CityBean>()
           locations.add(locationData)


           val hotCitys= mutableListOf<CityBean>()
           hotCitys.add(CityBean("北京", "北京"))
           hotCitys.add(CityBean("上海", "上海"))
           hotCitys.add(CityBean("广州", "广东"))
           hotCitys.add(CityBean("深圳", "广东"))


           val size = data.size
           val normalCity = mutableListOf<CityBean>()
           for (i in 0 until size) {
               data[i].group
               normalCity.addAll(data[i].city)
           }
           val allCitys:MutableList<CityListBean> = mutableListOf()
           allCitys.add(0,CityListBean(CityEnum.LOCATION.type,locations))
           allCitys.add(1,CityListBean(CityEnum.HOT.type,hotCitys))
           allCitys.add(2,CityListBean(CityEnum.NORMAL.type,normalCity))


//           val seconds : MutableList<CityBean> = mutableListOf()
//           seconds.addAll(locations)
//           seconds.addAll(hotCitys)
           mBinding.cityRecyclerview.addItemDecoration(SectionItemDecoration(requireActivity(), normalCity), 0)
           mBinding.cityRecyclerview.addItemDecoration(DividerItemDecoration(requireActivity(),), 1)
           mCityListAdapter.submitList(allCitys)
    })
    }

    override fun reLoadData() {

    }


    override fun onStart() {
        super.onStart()

        val window = dialog?.window
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            getScreenHeight() - 30.dp2px()
        )
    }
    override fun HomeCityDialogPickerBinding.initView() {
        sideIndexBar.setNavigationBarHeight(StatusBarUtils.getStatusBarHeight(requireActivity()))
        sideIndexBar.setOverlayTextView(indexOverlay).setOnIndexChangedListener(this@HomeCityGroupFragment)
        mCityListAdapter.mInnerListener = this@HomeCityGroupFragment

        val mLinearLayoutManager = LinearLayoutManager(requireActivity())
        mCityListAdapter.mLayoutManager = mLinearLayoutManager
        cityRecyclerview.init(
            mLinearLayoutManager,
            mCityListAdapter,
            true
        )
    }

    override fun onIndexChanged(index: String?, position: Int) {

    }

    override fun dismiss(position: Int, data: CityBean) {
        TODO("Not yet implemented")
    }

    override fun locate() {

    }
}