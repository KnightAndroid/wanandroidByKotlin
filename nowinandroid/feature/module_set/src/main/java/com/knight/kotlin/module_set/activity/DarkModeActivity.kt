package com.knight.kotlin.module_set.activity

import android.content.res.Configuration
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.library_base.contact.EmptyContract
import com.core.library_base.ktx.init
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.core.library_base.util.GsonUtils
import com.core.library_base.vm.EmptyMviViewModel
import com.google.common.reflect.TypeToken
import com.knight.kotlin.library_base.activity.BaseMviActivity
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.library_util.JsonUtils
import com.knight.kotlin.library_util.SystemUtils
import com.knight.kotlin.library_widget.ktx.setSafeOnItemClickListener
import com.knight.kotlin.module_set.R
import com.knight.kotlin.module_set.adapter.SelectDarkModeAdapter
import com.knight.kotlin.module_set.databinding.SetDarkmodeActivityBinding
import com.knight.kotlin.module_set.entity.DarkSelectEntity
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/5/20 15:41
 * Description:DarkModeActivity
 */

@AndroidEntryPoint
@Route(path = RouteActivity.Set.DarkModelActivity)
class DarkModeActivity :
    BaseMviActivity<
            SetDarkmodeActivityBinding,
            EmptyMviViewModel,
            EmptyContract.Event,
            EmptyContract.State,
            EmptyContract.Effect>() {

    private val mAdapter: SelectDarkModeAdapter by lazy {
        SelectDarkModeAdapter()
    }

    private val mDataList = mutableListOf<DarkSelectEntity>()

    private var isFollowSystem = false
    private var isDark = false

    override fun SetDarkmodeActivityBinding.initView() {
        title = getString(R.string.set_dark_mode)

        includeDarkmodeToolbar.baseIvBack.setOnClick { finish() }

        includeDarkmodeToolbar.baseTvTitle.text =
            getString(R.string.set_dark_mode)

        includeDarkmodeToolbar.baseTvRight.apply {
            visibility = View.VISIBLE
            text = getString(R.string.set_dark_mode_save)
            setOnClick { confirm() }
        }

        initRecyclerView()
        initSwitch()
        initListener()
    }

    override fun initObserver() {}

    override fun initRequestData() {
        initData()
    }

    override fun reLoadData() {}

    override fun renderState(state: EmptyContract.State) {}

    override fun handleEffect(effect: EmptyContract.Effect) {}

    override fun setThemeColor(isDarkMode: Boolean) {}

    // =========================
    // 初始化
    // =========================

    private fun initRecyclerView() {
        mBinding.setRvDarkmodelSelect.init(
            LinearLayoutManager(this),
            mAdapter,
            true
        )
    }

    private fun initSwitch() {
        isFollowSystem = CacheUtils.getFollowSystem()
        mBinding.setCbSelectSystem.isChecked = isFollowSystem

        updateManualVisible(isFollowSystem)

        mBinding.setCbSelectSystem.setOnCheckedChangeListener { _, isChecked ->
            isFollowSystem = isChecked
            updateManualVisible(isChecked)
        }
    }

    private fun initData() {
        val type = object : TypeToken<List<DarkSelectEntity>>() {}.type
        val json = JsonUtils.getJson(this, "darkselect.json")

        mDataList.clear()
        mDataList.addAll(GsonUtils.getList(json, type))

        val isNormalDark = CacheUtils.getNormalDark()

        // 设置选中状态
        mDataList.forEachIndexed { index, item ->
            item.select = if (isNormalDark) {
                index == 1
            } else {
                index == 0
            }
        }

        isDark = isNormalDark

        mAdapter.submitList(mDataList.toList()) // 提交新引用
    }

    private fun initListener() {
        mAdapter.setSafeOnItemClickListener { _, _, position ->
            updateSelect(position)
        }
    }

    // =========================
    // UI逻辑
    // =========================

    private fun updateManualVisible(followSystem: Boolean) {
        val visible = if (followSystem) View.GONE else View.VISIBLE
        mBinding.setRlManualSelect.visibility = visible
        mBinding.setTvManualSystem.visibility = visible
    }

    private fun updateSelect(position: Int) {
        mDataList.forEach { it.select = false }

        val item = mDataList[position]
        item.select = true

        isDark = item.isDark

        // ⭐关键优化：避免 notifyDataSetChanged
        mAdapter.submitList(mDataList.toList())
    }

    // =========================
    // 保存逻辑
    // =========================

    private fun confirm() {
        CacheUtils.setFollowSystem(isFollowSystem)

        if (isFollowSystem) {
            val isSystemDark =
                (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
                        Configuration.UI_MODE_NIGHT_YES

            CacheUtils.setNormalDark(isSystemDark)
        } else {
            CacheUtils.setNormalDark(isDark)
        }

        SystemUtils.restartApp(this)
    }
}