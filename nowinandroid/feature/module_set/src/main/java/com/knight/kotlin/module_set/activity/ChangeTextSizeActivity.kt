package com.knight.kotlin.module_set.activity

import android.content.res.Resources
import android.view.View
import com.knight.kotlin.library_base.BaseApp
import com.knight.kotlin.library_base.activity.BaseActivity
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.knight.kotlin.library_base.utils.CacheUtils
import com.core.library_base.util.px2sp
import com.core.library_base.vm.EmptyViewModel

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
class ChangeTextSizeActivity : BaseActivity<SetChangetextsizeActivityBinding, EmptyViewModel>() {


    /**
     *
     * 字体缩放因子
     */
    private var fontSizeScale:Float = 1f

    private var position:Int = 0

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun SetChangetextsizeActivityBinding.initView() {
        includeChangetextToolbar.baseTvTitle.setText(R.string.set_changetextsize)
        includeChangetextToolbar.baseIvBack.setOnClick { finish() }
        includeChangetextToolbar.baseTvRight.visibility = View.VISIBLE
        includeChangetextToolbar.baseTvRight.setText(R.string.set_changetextsize_save)
        fontSizeScale = CacheUtils.getSystemFontSize()
        position = ((fontSizeScale - 0.875) / 0.125).toInt()
        setCsvFontSize.setDefaultPosition(position)
        changeSize(position)
        mBinding.includeChangetextToolbar.baseTvRight.setOnClick {
            CacheUtils.setSystemFontSize(fontSizeScale)
            //如果是设置标准字体那就开启屏幕适配
            if (fontSizeScale == 1.0f && !AutoSize.checkInit()) {
                AutoSize.checkAndInit(BaseApp.application)
            }
            SystemUtils.restartApp(this@ChangeTextSizeActivity)
        }
        setCsvFontSize.setChangeCallbackListener(object:ChangeSizeView.OnChangeCallbackListener{
            override fun onChangeListener(position: Int) {
                changeSize(position)
                changeTitleSize()
            }
        })

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    private fun changeSize(position:Int) {
        val dimension = resources.getDimensionPixelSize(com.core.library_base.R.dimen.base_dimen_16)
        //根据position 获取字体倍数
        fontSizeScale = (0.875 + 0.125 * position).toFloat()
        //放大后获取缩小的sp单位
        val resultTextSize = (fontSizeScale * dimension.px2sp()).toInt()
        mBinding.setTvSample.setTextSize(resultTextSize.toFloat())

    }


    /**
     *
     * 改变标题字体大下
     */
    private fun changeTitleSize() {
        val titleBaseDimension = resources.getDimensionPixelSize(com.core.library_base.R.dimen.base_dimen_18)
        val titleRightDimension = resources.getDimensionPixelSize(com.core.library_base.R.dimen.base_dimen_15)
        val titleCenterTextSize = (fontSizeScale * titleBaseDimension.px2sp()).toInt()
        val titleRightTextSize = (fontSizeScale * titleRightDimension.px2sp()).toInt()
        mBinding.includeChangetextToolbar.baseTvTitle.setTextSize(titleCenterTextSize.toFloat())
        mBinding.includeChangetextToolbar.baseTvRight.setTextSize(titleRightTextSize.toFloat())
    }

    /**
     * 用这个方法 为了初始化选中字体缩放因子 保证基准是以1 调整字体大小 不然基准是以缩放因子
     */
    override fun getResources(): Resources {
        val res = super.getResources()
        if (res != null) {
            val config = res.configuration
            if (config != null && config.fontScale != 1.0f) {
                config.fontScale = 1.0f
                res.updateConfiguration(config, res.displayMetrics)
            }
        }
        return res
    }

}