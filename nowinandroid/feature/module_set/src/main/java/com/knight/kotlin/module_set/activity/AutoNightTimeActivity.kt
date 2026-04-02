package com.knight.kotlin.module_set.activity

import android.app.TimePickerDialog
import android.view.View
import com.core.library_base.contact.EmptyContract
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.core.library_base.vm.EmptyMviViewModel
import com.knight.kotlin.library_base.activity.BaseMviActivity
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.module_set.R
import com.knight.kotlin.module_set.databinding.SetAutonighttimeActivityBinding
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

/**
 * Author:Knight
 * Time:2022/5/31 17:36
 * Description:AutoNightTimeActivity
 */


@AndroidEntryPoint
@Route(path = RouteActivity.Set.SetAutoNightActivity)
class AutoNightTimeActivity :
    BaseMviActivity<
            SetAutonighttimeActivityBinding,
            EmptyMviViewModel,
            EmptyContract.Event,
            EmptyContract.State,
            EmptyContract.Effect>() {

    private val mCalendar: Calendar = Calendar.getInstance()

    private var tempHourOfNight = CacheUtils.getStartNightModeHour()
    private var tempMinuteNight = CacheUtils.getStartNightModeMinuter()
    private var tempHourOfDay = CacheUtils.getStartDayModeHour()
    private var tempMinuteDay = CacheUtils.getStartDayModeMinuter()

    override fun SetAutonighttimeActivityBinding.initView() {
        title = getString(R.string.set_night_mode)

        includeAutoNightToolbar.baseIvBack.setOnClick { finish() }
        includeAutoNightToolbar.baseTvTitle.text = getString(R.string.set_night_mode)

        includeAutoNightToolbar.baseTvRight.apply {
            visibility = View.VISIBLE
            text = getString(R.string.set_night_mode_save)
            setOnClick { saveAutoNightTime() }
        }

        initSwitch()
        initTimeView()
        initClick()
    }

    override fun initObserver() {
        // 如果后面有状态流，这里监听
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
        // 这里你如果想适配颜色，可以补充
    }

    // =========================
    // 初始化逻辑
    // =========================

    private fun initSwitch() {
        val isOpen = CacheUtils.getAutoNightMode()
        mBinding.setCbNightMode.isChecked = isOpen

        updateTimeLayoutVisible(isOpen)

        mBinding.setCbNightMode.setOnCheckedChangeListener { _, isChecked ->
            updateTimeLayoutVisible(isChecked)
        }
    }

    private fun initTimeView() {
        mBinding.setTvNightTimeValue.text = "$tempHourOfNight:$tempMinuteNight"
        mBinding.setDayTimeValue.text = "$tempHourOfDay:$tempMinuteDay"
    }

    private fun initClick() {
        mBinding.setRlStartNightTime.setOnClick { showNightPicker() }
        mBinding.setRlStartDayTime.setOnClick { showDayPicker() }
    }

    private fun updateTimeLayoutVisible(show: Boolean) {
        mBinding.setRlStartDayTime.visibility = if (show) View.VISIBLE else View.GONE
        mBinding.setRlStartNightTime.visibility = if (show) View.VISIBLE else View.GONE
    }

    // =========================
    // 时间选择
    // =========================

    private fun showNightPicker() {
        TimePickerDialog(
            this,
            com.knight.kotlin.library_widget.R.style.dialog_time_style,
            { _, hourOfDay, minute ->
                tempHourOfNight = formatTime(hourOfDay)
                tempMinuteNight = formatTime(minute)
                mBinding.setTvNightTimeValue.text =
                    "$tempHourOfNight:$tempMinuteNight"
            },
            mCalendar.get(Calendar.HOUR_OF_DAY),
            mCalendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun showDayPicker() {
        TimePickerDialog(
            this,
            com.knight.kotlin.library_widget.R.style.dialog_time_style,
            { _, hourOfDay, minute ->
                tempHourOfDay = formatTime(hourOfDay)
                tempMinuteDay = formatTime(minute)
                mBinding.setDayTimeValue.text =
                    "$tempHourOfDay:$tempMinuteDay"
            },
            mCalendar.get(Calendar.HOUR_OF_DAY),
            mCalendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun formatTime(value: Int): String {
        return if (value < 10) "0$value" else value.toString()
    }

    // =========================
    // 保存逻辑
    // =========================

    private fun saveAutoNightTime() {
        if (mBinding.setCbNightMode.isChecked) {
            CacheUtils.setOpenAutoNightMode(true)
            CacheUtils.setStartNightModeHour(tempHourOfNight)
            CacheUtils.setStartNightModeMinuter(tempMinuteNight)
            CacheUtils.setStartDayModeHour(tempHourOfDay)
            CacheUtils.setStartDayModeMinuter(tempMinuteDay)
        } else {
            CacheUtils.setOpenAutoNightMode(false)
        }
        finish()
    }
}