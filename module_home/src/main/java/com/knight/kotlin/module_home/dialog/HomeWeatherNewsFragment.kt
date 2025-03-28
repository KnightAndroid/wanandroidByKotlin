package com.knight.kotlin.module_home.dialog

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import androidx.lifecycle.Observer
import com.knight.kotlin.library_base.entity.TodayWeatherDataBean
import com.knight.kotlin.library_base.fragment.BaseDialogFragment
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_util.DateUtils
import com.knight.kotlin.library_util.TimeUtils
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.module_home.databinding.HomeTodayWeatherNewsDialogBinding
import com.knight.kotlin.module_home.vm.HomeWeatherNewVm
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/27 15:04
 * @descript:天气弹窗
 */
@AndroidEntryPoint
class HomeWeatherNewsFragment:BaseDialogFragment<HomeTodayWeatherNewsDialogBinding,HomeWeatherNewVm>() {

    private var weather: TodayWeatherDataBean?=null

    companion object {
        fun newInstance(todayWeather: TodayWeatherDataBean,max_degree:String,min_degree:String) : HomeWeatherNewsFragment {
            val mHomeWeatherNewsFragment = HomeWeatherNewsFragment()
            val args = Bundle()
            args.putParcelable("weather", todayWeather)
            args.putString("max_degree",max_degree)
            args.putString("min_degree",min_degree)
            mHomeWeatherNewsFragment.arguments = args
            return mHomeWeatherNewsFragment
        }
    }


    override fun getGravity() = Gravity.CENTER

    override fun initObserver() {

    }

    override fun initRequestData() {
          mViewModel.getTodayImage("js","0","1").observe(this, Observer { data ->
              ImageLoader.loadRoundedCornerPhoto(requireActivity(),"https://cn.bing.com" + data.images.get(0).urlbase + "_640x480.jpg", mBinding.homeTodayWeatherItem.ivTodayBg,4.dp2px())

          })

         mViewModel.getTwoHourRainFall(22.5256393434,114.0494336236,"precipitation",2,TimeUtils.getDefaultTimeZoneId()).observe(this,{data->
             var rainAmount:Float = 0f
             for (number in data.hourly.precipitation) {
                 rainAmount + number
             }
             if (rainAmount > 0) {
                 mBinding.homeTodayWeatherItem.tvRainInfo.text = "未来两小时有降雨"
             } else {
                 mBinding.homeTodayWeatherItem.tvRainInfo.text = "未来两小时无降雨"
             }
         })
    }

    override fun reLoadData() {

    }

    override fun HomeTodayWeatherNewsDialogBinding.initView() {
        homeTodayWeatherItem.tvWeekDay.text = DateUtils.getWeekday()
        val max_degree:String
        val min_degree:String
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            weather = arguments?.getParcelable("weather",TodayWeatherDataBean::class.java)
        } else {
            weather = arguments?.getParcelable("weather")
        }
        max_degree = arguments?.getString("max_degree") ?: ""
        min_degree = arguments?.getString("min_degree") ?: ""
        homeTodayWeatherItem.tvTodayTemperature.text = weather?.degree + "°C"
        homeTodayWeatherItem.tvTodayWeather.text = weather?.weather
        homeTodayWeatherItem.tvTipInfo.text = "\t\t今天最高温"+max_degree+"°，最低温"+min_degree+"°，多喝水，多运动，注意补充水分，保持新起愉悦"

    }
}