package com.knight.kotlin.module_set.activity

import android.app.TimePickerDialog
import android.view.View
import android.widget.CompoundButton
import android.widget.TimePicker
import androidx.activity.viewModels
import com.flyjingfish.android_aop_core.annotations.SingleClick
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.vm.EmptyViewModel
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
class AutoNightTimeActivity : BaseActivity<SetAutonighttimeActivityBinding,EmptyViewModel>() {
    override val mViewModel: EmptyViewModel by viewModels()

    private val mCarlendar:Calendar = Calendar.getInstance()
    private var temphourOfNight = CacheUtils.getStartNightModeHour()
    private var tempminuterNight = CacheUtils.getStartNightModeMinuter()
    private var temphourOfDay = CacheUtils.getStartDayModeHour()
    private var tempminuterDay = CacheUtils.getStartDayModeMinuter()



    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun SetAutonighttimeActivityBinding.initView() {
        includeAutoNightToolbar.baseIvBack.setOnClick { finish() }
        includeAutoNightToolbar.baseTvTitle.setText(getString(R.string.set_night_mode))
        includeAutoNightToolbar.baseTvRight.visibility = View.VISIBLE
        includeAutoNightToolbar.baseTvRight.setText(getString(R.string.set_night_mode_save))
        includeAutoNightToolbar.baseTvRight.setOnClick {
            saveAutoNightTime()
        }

        if (CacheUtils.getAutoNightMode()) {
            setCbNightMode.isChecked = true
            setRlStartDayTime.visibility = View.VISIBLE
            setRlStartNightTime.visibility = View.VISIBLE
        } else {
            setCbNightMode.isChecked  = false
        }

        setCbNightMode.setOnCheckedChangeListener(object:CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked) {
                    mBinding.setRlStartDayTime.visibility = View.VISIBLE
                    mBinding.setRlStartNightTime.visibility = View.VISIBLE
                } else {
                    mBinding.setRlStartDayTime.visibility = View.GONE
                    mBinding.setRlStartNightTime.visibility = View.GONE
                }
            }
        })

        setTvNightTimeValue.setText(temphourOfNight + ":" + tempminuterNight)
        setDayTimeValue.setText(temphourOfDay + ":" +tempminuterDay)
        setOnClickListener(setRlStartNightTime,setRlStartDayTime)
    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }


    /**
     * 保存
     * 自动设置夜间模式切换时间
     *
     */
    private fun saveAutoNightTime() {
        if (mBinding.setCbNightMode.isChecked) {
            CacheUtils.setOpenAutoNightMode(true)
            CacheUtils.setStartNightModeHour(temphourOfNight)
            CacheUtils.setStartNightModeMinuter(tempminuterNight)
            CacheUtils.setStartDayModeHour(temphourOfDay)
            CacheUtils.setStartDayModeMinuter(tempminuterDay)
        } else {
            CacheUtils.setOpenAutoNightMode(false)
        }
        finish()
    }

    @SingleClick
    override fun onClick(v: View) {
        when (v) {

            /**
             *
             * 设置晚间模式时间
             */
            mBinding.setRlStartNightTime -> {
                val nightDialog = TimePickerDialog(this@AutoNightTimeActivity,
                    com.knight.kotlin.library_widget.R.style.dialog_time_style,object:TimePickerDialog.OnTimeSetListener{
                    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                        temphourOfNight = if (hourOfDay < 10) "0$hourOfDay" else hourOfDay.toString()
                        tempminuterNight = if (minute < 10) "0$minute" else minute.toString()
                        mBinding.setTvNightTimeValue.setText(temphourOfNight + ":" +tempminuterNight)
                    }
                },mCarlendar.get(Calendar.HOUR_OF_DAY),mCarlendar.get(Calendar.MINUTE),true)

                nightDialog.show()
            }

            mBinding.setRlStartDayTime -> {
                val dayDialog = TimePickerDialog(this@AutoNightTimeActivity,com.knight.kotlin.library_widget.R.style.dialog_time_style,object : TimePickerDialog.OnTimeSetListener{
                    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                        temphourOfDay = if (hourOfDay < 10) "0$hourOfDay" else hourOfDay.toString()
                        tempminuterDay = if (minute < 10) "0$minute" else minute.toString()
                        mBinding.setDayTimeValue.setText(temphourOfDay + ":" + tempminuterDay)
                    }
                },mCarlendar.get(Calendar.HOUR_OF_DAY),mCarlendar.get(Calendar.MINUTE),true)
                dayDialog.show()
            }

        }
    }


}