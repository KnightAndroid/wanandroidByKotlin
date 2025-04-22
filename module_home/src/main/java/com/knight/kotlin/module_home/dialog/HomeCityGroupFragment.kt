package com.knight.kotlin.module_home.dialog

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.knight.kotlin.library_base.fragment.BaseDialogFragment
import com.knight.kotlin.library_base.ktx.dimissLoadingDialog
import com.knight.kotlin.library_base.ktx.getLocation
import com.knight.kotlin.library_base.ktx.getScreenHeight
import com.knight.kotlin.library_base.ktx.init
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.ktx.showLoadingDialog
import com.knight.kotlin.library_base.util.StatusBarUtils
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_util.LogUtils
import com.knight.kotlin.library_widget.GroupCityListBean
import com.knight.kotlin.library_widget.citypicker.CityBean
import com.knight.kotlin.library_widget.citypicker.CityListAdapter
import com.knight.kotlin.library_widget.citypicker.CityNormalSectionItemDecoration
import com.knight.kotlin.library_widget.citypicker.InnerListener
import com.knight.kotlin.library_widget.citypicker.view.SideIndexBar
import com.knight.kotlin.module_home.adapter.SearchCityAdpter
import com.knight.kotlin.module_home.databinding.HomeCityDialogPickerBinding
import com.knight.kotlin.module_home.vm.HomeCityGroupVm
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/17 16:56
 * @descript:选择城市列表弹窗
 */
@AndroidEntryPoint
class HomeCityGroupFragment : BaseDialogFragment<HomeCityDialogPickerBinding, HomeCityGroupVm>(),
    SideIndexBar.OnIndexTouchedChangedListener, InnerListener {

    interface OnChooseCityListener {
        fun onChooseCity(data: CityBean)
    }
    private var searchJob: Job? = null


    override fun getGravity() = Gravity.BOTTOM
    override fun cancelOnTouchOutSide() = true

    override fun initObserver() {

    }

    private val mCityListAdapter: CityListAdapter by lazy { CityListAdapter(this) }
    private val mSearchCityAdpter: SearchCityAdpter by lazy {SearchCityAdpter()  }
    private var listener: OnChooseCityListener? = null
    override fun initRequestData() {
        showLoadingDialog()
        mViewModel.getCityGroupData("pc").observe(this, { data ->
            dimissLoadingDialog()
            val locationData = CityBean(getLocation()?.city ?: "", getLocation()?.province ?: "")
            val locations = mutableListOf<CityBean>()
            locations.add(locationData)

            val locationGroups = GroupCityListBean("当前", locations)

            val hotCitys = mutableListOf<CityBean>()
            hotCitys.add(CityBean("北京", "北京"))
            hotCitys.add(CityBean("上海", "上海"))
            hotCitys.add(CityBean("广州", "广东"))
            hotCitys.add(CityBean("深圳", "广东"))
            hotCitys.add(CityBean("郑州", "河南"))
            hotCitys.add(CityBean("西安", "陕西"))
            hotCitys.add(CityBean("南京", "江苏"))
            hotCitys.add(CityBean("郑州", "河南"))
            hotCitys.add(CityBean("武汉", "湖北"))
            hotCitys.add(CityBean("成都", "四川"))
            hotCitys.add(CityBean("沈阳", "辽宁"))
            hotCitys.add(CityBean("天津", "天津"))
            val hotGroups = GroupCityListBean("热门", hotCitys)

            data.add(0, locationGroups)
            data.add(1, hotGroups)
            mBinding.rvCity.addItemDecoration(CityNormalSectionItemDecoration(data), 0)
            mCityListAdapter.submitList(data)
        })
    }

    override fun reLoadData() {

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = parentFragment as? OnChooseCityListener
            ?: throw RuntimeException("父 Fragment 必须实现 OnChooseCityListener 接口")
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
        rvCity.init(
            mLinearLayoutManager,
            mCityListAdapter,
            true
        )

        rvSearchCity.init(
            LinearLayoutManager(requireActivity()),
            mSearchCityAdpter,
            true
        )
        
        rvSearchCity.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        ivClose.setOnClick {
            dismiss()
        }
        homeTvSearchCityCancel.setOnClick {
            homeSearchCityEt.setText("")
            homeSearchCityEt.clearFocus()
            homeTvSearchCityCancel.visibility = View.GONE
            rvCity.visibility = View.VISIBLE
            rvSearchCity.visibility = View.GONE
        }


        homeSearchCityEt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
            homeTvSearchCityCancel.visibility = View.VISIBLE
            rvCity.visibility = View.GONE
            rvSearchCity.visibility = View.VISIBLE
        }
    }


        homeSearchCityEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchJob?.cancel()
            }

            override fun afterTextChanged(s: Editable?) {
                searchJob = CoroutineScope(Dispatchers.Main).launch {
                    delay(1000)
                    getSerachCityByKey()
                }
            }
        })
    }

    override fun onIndexChanged(index: String, position: Int) {
        mCityListAdapter.scrollToSection(index)
    }

    override fun click(position: Int, data: CityBean) {
       listener?.let {
           it.onChooseCity(data)
           dismiss()
       }
    }

    /**
     *
     * 搜索城市
     */
    fun getSerachCityByKey() {
        mViewModel.getSearchCityByKey("xw", mBinding.homeSearchCityEt.text?.trim().toString()).observe(this, { data ->
            LogUtils.d("sds"+data.size.toString())
            mSearchCityAdpter.submitList(data.values.toList())
        })
    }


    override fun onDetach() {
        super.onDetach()
        listener = null
    }


}