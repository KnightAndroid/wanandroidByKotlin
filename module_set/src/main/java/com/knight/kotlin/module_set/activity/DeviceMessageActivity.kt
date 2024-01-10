package com.knight.kotlin.module_set.activity

import androidx.activity.viewModels
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.ktx.screenHeightWithStatus
import com.knight.kotlin.library_base.ktx.screenWidth
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_util.DeviceUtils
import com.knight.kotlin.library_util.NetWorkUtils
import com.knight.kotlin.library_util.PhoneUtils
import com.knight.kotlin.module_set.R
import com.knight.kotlin.module_set.databinding.SetDeviceMessageActivityBinding
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2023/5/9 14:48
 * Description:DeviceMessageActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Set.DeviceMessage)
class DeviceMessageActivity : BaseActivity<SetDeviceMessageActivityBinding,EmptyViewModel>() {
    override val mViewModel: EmptyViewModel by viewModels()
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun SetDeviceMessageActivityBinding.initView() {
        includeSetDeviceMessageToolbar.baseTvTitle.setText(R.string.set_device_message)
        includeSetDeviceMessageToolbar.baseIvBack.setOnClickListener {
            finish()
        }
        tvDeviceSystemVersion.text = DeviceUtils.getSystemVersion()
        tvAndroidSdkVersion.text = DeviceUtils.getAndroidSdkVersion().toString()
        tvScreenSize.text = "${screenHeightWithStatus}x${screenWidth}"
        tvArea.text = DeviceUtils.getCountry()
        tvTimeZone.text = DeviceUtils.getTimeZone()
        tvIpAdress.text = NetWorkUtils.getIpAddress(true)
        tvMacAddress.text = NetWorkUtils.getMacAddress()
        tvSimMessage.text = PhoneUtils.getSimOperatorByMnc()
        tvUniqueId.text = PhoneUtils.getDeviceUUID(this@DeviceMessageActivity)
    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

}