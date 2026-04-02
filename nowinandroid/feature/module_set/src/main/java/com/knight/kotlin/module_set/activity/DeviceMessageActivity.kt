package com.knight.kotlin.module_set.activity

import com.core.library_base.contact.EmptyContract
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.core.library_base.vm.EmptyMviViewModel
import com.core.library_common.ktx.screenWidth
import com.knight.kotlin.library_base.activity.BaseMviActivity
import com.knight.kotlin.library_base.ktx.screenHeightWithStatus
import com.knight.kotlin.library_util.DeviceUtils
import com.knight.kotlin.library_util.NetWorkUtils
import com.knight.kotlin.library_util.PhoneUtils
import com.knight.kotlin.module_set.R
import com.knight.kotlin.module_set.databinding.SetDeviceMessageActivityBinding
import com.knight.kotlin.module_set.entity.DeviceInfo
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2023/5/9 14:48
 * Description:DeviceMessageActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Set.DeviceMessage)
class DeviceMessageActivity :
    BaseMviActivity<
            SetDeviceMessageActivityBinding,
            EmptyMviViewModel,
            EmptyContract.Event,
            EmptyContract.State,
            EmptyContract.Effect>() {

    override fun SetDeviceMessageActivityBinding.initView() {
        title = getString(R.string.set_device_message)

        includeSetDeviceMessageToolbar.baseTvTitle.text =
            getString(R.string.set_device_message)

        includeSetDeviceMessageToolbar.baseIvBack.setOnClick {
            finish()
        }

        renderDeviceInfo()
    }

    override fun initObserver() {}

    override fun initRequestData() {}

    override fun reLoadData() {}

    override fun renderState(state: EmptyContract.State) {}

    override fun handleEffect(effect: EmptyContract.Effect) {}

    override fun setThemeColor(isDarkMode: Boolean) {}

    // =========================
    // 核心逻辑
    // =========================

    private fun renderDeviceInfo() = with(mBinding) {
        val info = collectDeviceInfo()

        tvDeviceSystemVersion.text = info.systemVersion
        tvAndroidSdkVersion.text = info.sdkVersion
        tvScreenSize.text = info.screenSize
        tvArea.text = info.country
        tvTimeZone.text = info.timeZone
        tvIpAdress.text = info.ipAddress
        tvMacAddress.text = info.macAddress
        tvSimMessage.text = info.simOperator
        tvUniqueId.text = info.deviceId
    }

    private fun collectDeviceInfo(): DeviceInfo {
        return DeviceInfo(
            systemVersion = DeviceUtils.getSystemVersion(),
            sdkVersion = DeviceUtils.getAndroidSdkVersion().toString(),
            screenSize = "${screenHeightWithStatus}x${screenWidth}",
            country = DeviceUtils.getCountry(),
            timeZone = DeviceUtils.getTimeZone(),
            ipAddress = NetWorkUtils.getIpAddress(true),
            macAddress = NetWorkUtils.getMacAddress() ?: "",
            simOperator = PhoneUtils.getSimOperatorByMnc(),
            deviceId = PhoneUtils.getDeviceUUID(this)
        )
    }
}