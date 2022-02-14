package com.knight.kotlin.module_home.fragment

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.common.reflect.TypeToken
import com.knight.kotlin.library_aop.clickintercept.SingleClick
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.ktx.observeLiveData
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.GsonUtils
import com.knight.kotlin.library_common.entity.AppUpdateBean
import com.knight.kotlin.library_common.fragment.UpdateAppDialogFragment
import com.knight.kotlin.library_database.entity.PushDateEntity
import com.knight.kotlin.library_permiss.XXPermissions
import com.knight.kotlin.library_permiss.listener.OnPermissionCallback
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_scan.activity.ScanCodeActivity
import com.knight.kotlin.library_scan.annoation.ScanStyle
import com.knight.kotlin.library_scan.decode.ScanCodeConfig
import com.knight.kotlin.library_util.DateUtils
import com.knight.kotlin.library_util.JsonUtils.getJson
import com.knight.kotlin.library_util.SystemUtils
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.library_util.bindViewPager2
import com.knight.kotlin.library_util.toast.ToastUtils
import com.knight.kotlin.module_home.constants.HomeConstants
import com.knight.kotlin.module_home.databinding.HomeFragmentBinding
import com.knight.kotlin.module_home.dialog.HomePushArticleFragment
import com.knight.kotlin.module_home.entity.EveryDayPushArticlesBean
import com.knight.kotlin.module_home.vm.HomeVm
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Type


/**
 * Author:Knight
 * Time:2021/12/22 19:33
 * Description:HomeFragment
 */
@AndroidEntryPoint
@Route(path = RouteFragment.Home.HomeFragment)
class HomeFragment : BaseFragment<HomeFragmentBinding, HomeVm>() {


    /**
     *
     * 首页Fragment
     */
    private var mFragments = mutableListOf<Fragment>()
    /**
     * 知识标签
     */
    private var knowledgeLabelList = mutableListOf<String>()
    /**
     *
     * 获取推送文章
     */
    private lateinit var mEveryDayPushData: EveryDayPushArticlesBean
    override val mViewModel: HomeVm by viewModels()
    override fun HomeFragmentBinding.initView() {
        initMagicIndicator()
        setOnClickListener(mBinding.homeIncludeToolbar.homeScanIcon,
            mBinding.homeIncludeToolbar.homeIvEveryday,
            mBinding.homeIncludeToolbar.homeIvAdd)

    }

    /**
     *
     * 订阅LiveData
     */
    override fun initObserver() {
        observeLiveData(mViewModel.everyDayPushArticles, ::setEveryDayPushArticle)
        observeLiveData(mViewModel.articles, ::processPushArticle)
        observeLiveData(mViewModel.appUpdateMessage,::checkAppMessage)
    }

    override fun initRequestData() {
        mViewModel.getEveryDayPushArticle()
        mViewModel.checkAppUpdateMessage()
    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    /**
     * 获取推送文章具体数据
     */
    private fun setEveryDayPushArticle(data: EveryDayPushArticlesBean) {
        mEveryDayPushData = data
        if (data.pushStatus && DateUtils.isToday(data.time)) {
            //查询本地推送文章时间 进行判断
            mViewModel.queryPushDate()
        }

    }

    //处理是否进行弹窗
    private fun processPushArticle(datas: List<PushDateEntity>) {
        if (datas != null && datas.isNotEmpty()) {
            var pushDateEntity = datas[0]
            if (pushDateEntity.time == mEveryDayPushData.time) {
                pushDateEntity.time = mEveryDayPushData.time
                mViewModel.updatePushArticlesDate(pushDateEntity)
                HomePushArticleFragment.newInstance(mEveryDayPushData.datas).showAllowingStateLoss(parentFragmentManager,"dialog_everydaypush")
            }
        } else {
            val pushTempDateEntity = PushDateEntity()
            pushTempDateEntity.time = mEveryDayPushData.time
            mViewModel.insertPushArticlesDate(pushTempDateEntity)
            HomePushArticleFragment.newInstance(mEveryDayPushData.datas).showAllowingStateLoss(parentFragmentManager,"dialog_everydaypush")
        }
    }

    /**
     *
     * 检查APP更新
     */
    private fun checkAppMessage(data: AppUpdateBean) {
        //如果本地安装包大于远端 证明本地安装的说测试包 无需更新
        if (activity?.let { SystemUtils.getAppVersionCode(it) }!! < data.versionCode ) {
            if (data.versionName != activity?.let { SystemUtils.getAppVersionName(it) }) {
                UpdateAppDialogFragment.newInstance(data).showAllowingStateLoss(
                    parentFragmentManager, "dialog_update")

            }
        }
    }

    /**
     * 初始化指示器
     */
    private fun initMagicIndicator() {
        knowledgeLabelList = CacheUtils.getDataInfo(
            "knowledgeLabel",
            object : TypeToken<List<String>>() {}.type
        )
        if (knowledgeLabelList.isNullOrEmpty()) {
            val type: Type = object : TypeToken<List<String>>() {}.type
            val jsonData = getJson(requireActivity(), "searchkeywords.json")
            knowledgeLabelList = GsonUtils.getList(jsonData, type)
        }
        mFragments.clear()
        for (i in knowledgeLabelList.indices) {
            if (i == 0) {
                mFragments.add(HomeRecommendFragment())
            } else {
                mFragments.add(HomeArticleFragment())
            }
        }

        ViewInitUtils.setViewPager2Init(requireActivity(),mBinding.viewPager,mFragments,
            isOffscreenPageLimit = true,
            isUserInputEnabled = false
        )
        mBinding.magicIndicator.bindViewPager2(mBinding.viewPager,knowledgeLabelList) {
            HomeConstants.ARTICLE_TYPE = knowledgeLabelList[it]
        }

    }
    @SingleClick
    override fun onClick(v: View) {
        when (v) {
           mBinding.homeIncludeToolbar.homeScanIcon->{
               XXPermissions.with(this@HomeFragment)
                   ?.permission(Permission.CAMERA)
                   ?.request(object :OnPermissionCallback{
                       override fun onGranted(permissions: List<String>, all: Boolean) {
                           if (all) {

                               ScanCodeConfig.Builder()
                                   .setFragment(this@HomeFragment)
                                   .setActivity(activity)
                                   .setPlayAudio(true)
                                   .setStyle(ScanStyle.FULL_SCREEN)
                                   .build().start(ScanCodeActivity::class.java)
                           }
                       }
                   })
           }
           mBinding.homeIncludeToolbar.homeIvEveryday->{
               HomePushArticleFragment.newInstance(mEveryDayPushData.datas).showAllowingStateLoss(parentFragmentManager,"dialog_everydaypush")
           }

           mBinding.homeIncludeToolbar.homeIvAdd->{
               TODO("发布文章界面")
           }



        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //接收扫码结果
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ScanCodeConfig.QUESTCODE && data != null) {
                val extras = data.extras
                if (extras != null) {
                    val result = extras.getString(ScanCodeConfig.CODE_KEY)
                    ToastUtils.show(result)
                }
            }
        }
    }


}