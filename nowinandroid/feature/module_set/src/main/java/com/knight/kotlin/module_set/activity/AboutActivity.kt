package com.knight.kotlin.module_set.activity

import android.view.View
import com.knight.kotlin.library_base.activity.BaseActivity
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.flyjingfish.android_aop_core.annotations.SingleClick

import com.knight.kotlin.library_base.fragment.UpdateAppDialogFragment
import com.knight.kotlin.library_util.SystemUtils
import com.knight.kotlin.library_util.startPage
import com.knight.kotlin.library_util.startPageWithParams
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_set.R
import com.knight.kotlin.module_set.databinding.SetAboutActivityBinding
import com.knight.kotlin.module_set.vm.AboutVm
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/8/25 15:34
 * Description:AboutActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Set.AboutActivity)
class AboutActivity: BaseActivity<SetAboutActivityBinding, AboutVm>() {


    override fun setThemeColor(isDarkMode: Boolean) {
        mBinding.tvAboutAppName.setTextColor(themeColor)
        mBinding.cvAbout.setBackgroundColor(themeColor)
    }

    override fun SetAboutActivityBinding.initView() {
        mBinding.includeAboutToobar.baseTvTitle.text = getString(R.string.set_about)
        mBinding.includeAboutToobar.baseIvBack.setOnClick {
            finish()
        }
        mBinding.tvAboutAppName.text = SystemUtils.getAppName(this@AboutActivity)
        mBinding.tvAboutAppVersion.text = SystemUtils.getAppVersionName(this@AboutActivity).plus("(").plus( SystemUtils.getAppVersionCode(this@AboutActivity)).plus(")")
        setOnClickListener(setRlAppupdateRecord,setRlCheckUpdate,tvServiceProtocol,tvPrivacyProtocol,setRlAccessPartner)
    }

    override fun initObserver() {
    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    @SingleClick
    override fun onClick(v: View) {
        when (v) {
            mBinding.setRlAppupdateRecord -> {
                startPage(RouteActivity.Set.AppRecordMessageActivity)
            }
            mBinding.setRlCheckUpdate -> {
                mViewModel.checkAppUpdateMessage().observerKt {
                    //如果本地安装包大于远端 证明本地安装的说测试包 无需更新
                    if (SystemUtils.getAppVersionCode(this)  < it.versionCode ) {
                        if (it.versionName != SystemUtils.getAppVersionName(this) ) {
                            UpdateAppDialogFragment.newInstance(it).showAllowingStateLoss(
                                supportFragmentManager, "dialog_update")
                        }
                    } else {
                        toast(getString(R.string.set_no_update_version))
                    }
                }
            }

            mBinding.tvServiceProtocol -> {
                startPageWithParams(RouteActivity.Web.WebPager,
                    "webUrl" to "file:android_asset/wanandroid_useragree.html",
                    "webTitle" to getString(R.string.set_suser_ervice_protocol))
            }

            mBinding.tvPrivacyProtocol -> {
                startPageWithParams(RouteActivity.Web.WebPager,
                    "webUrl" to "file:android_asset/wanandroid_userprivacy.html",
                    "webTitle" to getString(R.string.set_privacy_protocol))
            }

            mBinding.setRlAccessPartner -> {
                startPageWithParams(RouteActivity.Web.WebPager,
                    "webUrl" to "file:android_asset/access_partner_directory.html",
                    "webTitle" to getString(R.string.set_access_partner_directory))
            }
        }
    }

}