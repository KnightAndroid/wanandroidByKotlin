package com.knight.kotlin.module_home.dialog

import android.view.Gravity
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.fragment.BaseDialogFragment
import com.knight.kotlin.library_base.ktx.getScreenHeight
import com.knight.kotlin.library_base.ktx.init
import com.knight.kotlin.library_base.util.StatusBarUtils
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_widget.GroupCityListBean
import com.knight.kotlin.library_widget.citypicker.CityBean
import com.knight.kotlin.library_widget.citypicker.CityListAdapter
import com.knight.kotlin.library_widget.citypicker.CityNormalSectionItemDecoration
import com.knight.kotlin.library_widget.citypicker.InnerListener
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

    private val mCityListAdapter: CityListAdapter by lazy { CityListAdapter(this) }


    override fun initRequestData() {
       mViewModel.getCityGroupData("pc").observe(this,{data->

           val locationData = CityBean("深圳","广东")
           val locations = mutableListOf<CityBean>()
           locations.add(locationData)

           val locationGroups = GroupCityListBean("定位",locations)

           val hotCitys= mutableListOf<CityBean>()
           hotCitys.add(CityBean("北京", "北京"))
           hotCitys.add(CityBean("上海", "上海"))
           hotCitys.add(CityBean("广州", "广东"))
           hotCitys.add(CityBean("深圳", "广东"))
           val hotGroups = GroupCityListBean("热门",locations)

           data.add(0,locationGroups)
           data.add(1,hotGroups)
           mBinding.cityRecyclerview.addItemDecoration(CityNormalSectionItemDecoration(data), 0)
          // mBinding.cityRecyclerview.addItemDecoration(DividerItemDecoration(requireActivity(),), 1)
          // mCityListAdapter.mGroupCityList = data
           mCityListAdapter.submitList(data)
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

        val mLinearLayoutManager = LinearLayoutManager(requireActivity())
        mCityListAdapter.mLayoutManager = mLinearLayoutManager
        cityRecyclerview.init(
            mLinearLayoutManager,
            mCityListAdapter,
            true
        )
    }

    override fun onIndexChanged(index: String, position: Int) {
        mCityListAdapter.scrollToSection(index)
    }

    override fun dismiss(position: Int, data: CityBean) {
        TODO("Not yet implemented")
    }

    override fun locate() {

    }
}