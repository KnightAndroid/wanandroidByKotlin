package com.knight.kotlin.module_set.activity

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
import com.knight.kotlin.library_common.util.LanguageFontSizeUtils
import com.knight.kotlin.library_util.JsonUtils
import com.knight.kotlin.library_util.SystemUtils
import com.knight.kotlin.library_widget.ktx.setSafeOnItemClickListener
import com.knight.kotlin.module_set.R
import com.knight.kotlin.module_set.adapter.SelectLanguageAdapter
import com.knight.kotlin.module_set.databinding.SetLanguageActivityBinding
import com.knight.kotlin.module_set.entity.LanguageEntity
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/5/31 16:11
 * Description:SelectLanguageActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Set.SetLanguageActivity)
class SelectLanguageActivity :
    BaseMviActivity<
            SetLanguageActivityBinding,
            EmptyMviViewModel,
            EmptyContract.Event,
            EmptyContract.State,
            EmptyContract.Effect>() {

    private val mAdapter by lazy { SelectLanguageAdapter(arrayListOf()) }

    private val mLanguageList = mutableListOf<LanguageEntity>()

    private var currentLanguage = CacheUtils.getLanguageMode()
    private var selectedLanguage: String = currentLanguage // ⭐关键修复

    override fun SetLanguageActivityBinding.initView() {
        title = getString(R.string.set_more_language)

        includeLanguageToolbar.baseIvBack.setOnClick { finish() }

        includeLanguageToolbar.baseTvTitle.text =
            getString(R.string.set_more_language)

        includeLanguageToolbar.baseTvRight.apply {
            visibility = View.VISIBLE
            text = getString(R.string.set_language_save)
            setOnClick { saveLanguage() }
        }

        initRecyclerView()
        initData()
        initListener()
    }

    override fun initObserver() {}
    override fun initRequestData() {}
    override fun reLoadData() {}
    override fun renderState(state: EmptyContract.State) {}
    override fun handleEffect(effect: EmptyContract.Effect) {}
    override fun setThemeColor(isDarkMode: Boolean) {}

    // =========================
    // 初始化
    // =========================

    private fun initRecyclerView() {
        mBinding.setRvLanguageSelect.init(
            LinearLayoutManager(this),
            mAdapter,
            true
        )
    }

    private fun initData() {
        val type = object : TypeToken<List<LanguageEntity>>() {}.type
        val json = JsonUtils.getJson(this, "languageselect.json")

        mLanguageList.clear()
        mLanguageList.addAll(GsonUtils.getList(json, type))

        // 设置默认选中
        mLanguageList.forEach {
            it.select = it.englishName == currentLanguage
        }

        mAdapter.submitList(mLanguageList.toList())
    }

    private fun initListener() {
        mAdapter.setSafeOnItemClickListener { _, _, position ->
            updateSelect(position)
        }
    }

    // =========================
    // UI逻辑
    // =========================

    private fun updateSelect(position: Int) {
        mLanguageList.forEach { it.select = false }

        val item = mLanguageList[position]
        item.select = true

        selectedLanguage = item.englishName

        // ⭐关键：使用 Diff 刷新
        mAdapter.submitList(mLanguageList.toList())
    }

    // =========================
    // 保存逻辑
    // =========================

    private fun saveLanguage() {
        CacheUtils.setLanguageType(selectedLanguage)

        LanguageFontSizeUtils.setAppLanguage(this)

        SystemUtils.restartApp(this)
    }
}