package com.knight.kotlin.module_set.activity

import android.content.res.Configuration
import android.view.View
import android.widget.CompoundButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.flyjingfish.android_aop_core.annotations.SingleClick
import com.google.common.reflect.TypeToken
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.ktx.init
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.GsonUtils
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_util.JsonUtils
import com.knight.kotlin.library_util.SystemUtils
import com.knight.kotlin.library_widget.ktx.setItemClickListener
import com.knight.kotlin.module_set.R
import com.knight.kotlin.module_set.adapter.SelectDarkModeAdapter
import com.knight.kotlin.module_set.databinding.SetDarkmodeActivityBinding
import com.knight.kotlin.module_set.entity.DarkSelectEntity
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Type

/**
 * Author:Knight
 * Time:2022/5/20 15:41
 * Description:DarkModeActivity
 */

@AndroidEntryPoint
@Route(path = RouteActivity.Set.DarkModelActivity)
class DarkModeActivity : BaseActivity<SetDarkmodeActivityBinding, EmptyViewModel>() {


    /**
     *
     * 是否深色模式
     *
     */
    private var isDark = false
    private var isFollowSystem = false
    private val mSelectDarkModeAdapter: SelectDarkModeAdapter by lazy {
        SelectDarkModeAdapter(
            arrayListOf()
        )
    }

    private lateinit var mDataList: MutableList<DarkSelectEntity>

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun SetDarkmodeActivityBinding.initView() {
        includeDarkmodeToolbar.baseIvBack.setOnClick { finish() }
        includeDarkmodeToolbar.baseTvTitle.setText(getString(R.string.set_dark_mode))
        includeDarkmodeToolbar.baseTvRight.visibility = View.VISIBLE
        includeDarkmodeToolbar.baseTvRight.setText(getString(R.string.set_dark_mode_save))
        setRvDarkmodelSelect.init(
            LinearLayoutManager(this@DarkModeActivity),
            mSelectDarkModeAdapter,
            true
        )
        setCbSelectSystem.setOnCheckedChangeListener(onCheckedChangeAllowSystem)
        initListener()
        setOnClickListener(includeDarkmodeToolbar.baseTvRight)


    }

    override fun initObserver() {

    }


    override fun initRequestData() {
        mBinding.setCbSelectSystem.isChecked = CacheUtils.getFollowSystem()
        if (CacheUtils.getFollowSystem()) {
            mBinding.setRlManualSelect.visibility = View.GONE
            mBinding.setTvManualSystem.visibility = View.GONE
        } else {
            mBinding.setRlManualSelect.visibility = View.VISIBLE
            mBinding.setTvManualSystem.visibility = View.VISIBLE
        }

        val type: Type = object : TypeToken<List<DarkSelectEntity>>() {}.type
        val jsonData = JsonUtils.getJson(this, "darkselect.json")
        mDataList = GsonUtils.getList(jsonData, type)
        //如果是普通模式
        if (CacheUtils.getNormalDark()) {
            mDataList.get(0).select = false
            mDataList.get(1).select = true
        } else {
            mDataList.get(0).select = true
            mDataList.get(1).select = false
        }

        mSelectDarkModeAdapter.setNewInstance(mDataList)
    }

    override fun reLoadData() {

    }

    private val onCheckedChangeAllowSystem =
        CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mBinding.setRlManualSelect.setVisibility(View.GONE)
                mBinding.setTvManualSystem.setVisibility(View.GONE)
            } else {
                mBinding.setRlManualSelect.setVisibility(View.VISIBLE)
                mBinding.setTvManualSystem.setVisibility(View.VISIBLE)
            }
            isFollowSystem = isChecked
        }


    private fun initListener() {
        mSelectDarkModeAdapter.run {
            setItemClickListener { adapter, view, position ->
                for (i in mDataList.indices) {
                    mDataList[i].select = false
                }
                mDataList.get(position).select = true
                //保存所选模式
                isDark = mDataList.get(position).isDark
                mSelectDarkModeAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun confim() {
        //保存是否跟随系统
        CacheUtils.setFollowSystem(isFollowSystem)
        if (isFollowSystem) {
            if (Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES) {
                //深色模式
                CacheUtils.setNormalDark(true)
            } else {
                CacheUtils.setNormalDark(false)
            }
        } else {
            CacheUtils.setNormalDark(isDark)
        }
        SystemUtils.restartApp(this)
    }

    @SingleClick
    override fun onClick(v: View) {
        when (v) {
            mBinding.includeDarkmodeToolbar.baseTvRight -> {
                confim()
            }
        }
    }

}