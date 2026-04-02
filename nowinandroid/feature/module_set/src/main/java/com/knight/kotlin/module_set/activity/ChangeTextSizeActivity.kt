package com.knight.kotlin.module_set.activity

import android.content.res.Resources
import android.view.View
import com.core.library_base.contact.EmptyContract
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.core.library_base.vm.EmptyMviViewModel
import com.core.library_common.util.px2sp
import com.knight.kotlin.library_base.BaseApp
import com.knight.kotlin.library_base.activity.BaseMviActivity
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.library_util.SystemUtils
import com.knight.kotlin.library_widget.ChangeSizeView
import com.knight.kotlin.module_set.R
import com.knight.kotlin.module_set.databinding.SetChangetextsizeActivityBinding
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import me.jessyan.autosize.AutoSize

/**
 * Author:Knight
 * Time:2022/6/23 15:31
 * Description:ChangeTextSizeActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Set.SetChangeTextSizeActivity)
class ChangeTextSizeActivity :
    BaseMviActivity<
            SetChangetextsizeActivityBinding,
            EmptyMviViewModel,
            EmptyContract.Event,
            EmptyContract.State,
            EmptyContract.Effect>() {

    /**
     * 字体缩放因子
     */
    private var fontSizeScale: Float = 1f

    private var position: Int = 0

    override fun SetChangetextsizeActivityBinding.initView() {
        title = getString(R.string.set_changetextsize)

        includeChangetextToolbar.baseIvBack.setOnClick { finish() }

        includeChangetextToolbar.baseTvTitle.text =
            getString(R.string.set_changetextsize)

        includeChangetextToolbar.baseTvRight.apply {
            visibility = View.VISIBLE
            text = getString(R.string.set_changetextsize_save)
            setOnClick { saveFontSize() }
        }

        initData()
        initListener()
    }

    override fun initObserver() {
        // no-op
    }

    override fun initRequestData() {
        // no-op
    }

    override fun reLoadData() {
        // no-op
    }

    override fun renderState(state: EmptyContract.State) {
        // no-op
    }

    override fun handleEffect(effect: EmptyContract.Effect) {
        // no-op
    }

    override fun setThemeColor(isDarkMode: Boolean) {
        // 可扩展
    }

    // =========================
    // 初始化
    // =========================

    private fun initData() {
        fontSizeScale = CacheUtils.getSystemFontSize()

        // 反推位置
        position = ((fontSizeScale - 0.875f) / 0.125f).toInt()

        mBinding.setCsvFontSize.setDefaultPosition(position)

        updatePreview(position)
    }

    private fun initListener() {
        mBinding.setCsvFontSize.setChangeCallbackListener(
            object : ChangeSizeView.OnChangeCallbackListener {
                override fun onChangeListener(position: Int) {
                    updatePreview(position)
                }
            }
        )
    }

    // =========================
    // UI更新
    // =========================

    private fun updatePreview(position: Int) {
        this.position = position

        fontSizeScale = calculateScale(position)

        updateContentTextSize()
        updateTitleTextSize()
    }

    /**
     * 内容字体
     */
    private fun updateContentTextSize() {
        val basePx =
            resources.getDimensionPixelSize(com.core.library_base.R.dimen.base_dimen_16)

        val sp = basePx.px2sp()

        val resultSize = fontSizeScale * sp

        mBinding.setTvSample.textSize = resultSize
    }

    /**
     * 标题字体
     */
    private fun updateTitleTextSize() {
        val titleBasePx =
            resources.getDimensionPixelSize(com.core.library_base.R.dimen.base_dimen_18)
        val rightBasePx =
            resources.getDimensionPixelSize(com.core.library_base.R.dimen.base_dimen_15)

        val titleSize = fontSizeScale * titleBasePx.px2sp()
        val rightSize = fontSizeScale * rightBasePx.px2sp()

        mBinding.includeChangetextToolbar.baseTvTitle.textSize = titleSize
        mBinding.includeChangetextToolbar.baseTvRight.textSize = rightSize
    }

    // =========================
    // 业务逻辑
    // =========================

    private fun calculateScale(position: Int): Float {
        return 0.875f + 0.125f * position
    }

    private fun saveFontSize() {
        CacheUtils.setSystemFontSize(fontSizeScale)

        // 标准字体恢复 AutoSize
        if (fontSizeScale == 1.0f && !AutoSize.checkInit()) {
            AutoSize.checkAndInit(BaseApp.application)
        }

        SystemUtils.restartApp(this)
    }

    // =========================
    // 关键：防止系统字体影响
    // =========================

    override fun getResources(): Resources {
        val res = super.getResources()
        val config = res.configuration

        if (config.fontScale != 1.0f) {
            config.fontScale = 1.0f
            res.updateConfiguration(config, res.displayMetrics)
        }
        return res
    }
}