package com.knight.kotlin.module_set.activity

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.activity.BaseActivity
import com.core.library_base.ktx.init
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.knight.kotlin.library_common.util.CacheUtils
import com.core.library_base.util.GsonUtils
import com.knight.kotlin.library_common.util.LanguageFontSizeUtils
import com.core.library_base.vm.EmptyViewModel
import com.google.common.reflect.TypeToken
import com.knight.kotlin.library_util.JsonUtils
import com.knight.kotlin.library_util.SystemUtils
import com.knight.kotlin.library_widget.ktx.setSafeOnItemClickListener
import com.knight.kotlin.module_set.R
import com.knight.kotlin.module_set.adapter.SelectLanguageAdapter
import com.knight.kotlin.module_set.databinding.SetLanguageActivityBinding
import com.knight.kotlin.module_set.entity.LanguageEntity
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Type

/**
 * Author:Knight
 * Time:2022/5/31 16:11
 * Description:SelectLanguageActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Set.SetLanguageActivity)
class SelectLanguageActivity : BaseActivity<SetLanguageActivityBinding, EmptyViewModel>() {



    private lateinit var mLanguageList:MutableList<LanguageEntity>

    private lateinit var currentSelecLanguage :String

    private var currentLanguage = CacheUtils.getLanguageMode()
    private val mSelectLanguageAdapter: SelectLanguageAdapter by lazy {
        SelectLanguageAdapter(
            arrayListOf()
        )
    }
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun SetLanguageActivityBinding.initView() {
        includeLanguageToolbar.baseIvBack.setOnClick { finish() }
        includeLanguageToolbar.baseTvTitle.setText(getString(R.string.set_more_language))
        includeLanguageToolbar.baseTvRight.visibility = View.VISIBLE
        includeLanguageToolbar.baseTvRight.setText(getString(R.string.set_language_save))
        includeLanguageToolbar.baseTvRight.setOnClick { saveLanguage()  }
        setRvLanguageSelect.init(LinearLayoutManager(this@SelectLanguageActivity),mSelectLanguageAdapter,true)
        getLanguageData()
        initListener()
    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    /**
     *
     * 初始化数据
     */
    private fun getLanguageData() {
        val type: Type = object : TypeToken<List<LanguageEntity>>() {}.type
        val jsonData = JsonUtils.getJson(this, "languageselect.json")
        mLanguageList = GsonUtils.getList(jsonData, type)
        for (i in mLanguageList.indices) {
            if (currentLanguage == mLanguageList.get(i).englishName) {
                mLanguageList.get(i).select = true
                break
            }
        }
        mSelectLanguageAdapter.submitList(mLanguageList)
    }

    private fun initListener() {
        mSelectLanguageAdapter.run {
            setSafeOnItemClickListener { adapter, view, position ->
                for (i in mLanguageList.indices) {
                    mLanguageList.get(i).select = false
                }
                mLanguageList.get(position).select = true
                mSelectLanguageAdapter.notifyDataSetChanged()
                currentSelecLanguage = mLanguageList.get(position).englishName

            }
        }
    }

    /**
     *
     * 保存语言模式
     */
    private fun saveLanguage() {
        CacheUtils.setLanguageType(currentSelecLanguage)
        LanguageFontSizeUtils.setAppLanguage(this)
        SystemUtils.restartApp(this)
    }

}