package com.knight.kotlin.module_home.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.Base64
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.common.reflect.TypeToken
import com.knight.kotlin.library_aop.clickintercept.SingleClick
import com.knight.kotlin.library_base.annotation.EventBusRegister
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.config.CacheKey
import com.knight.kotlin.library_base.entity.LoginEntity
import com.knight.kotlin.library_base.entity.UserInfoEntity
import com.knight.kotlin.library_base.event.MessageEvent
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.ktx.getUser
import com.knight.kotlin.library_base.ktx.observeLiveData
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.EventBusUtils
import com.knight.kotlin.library_base.util.GsonUtils
import com.knight.kotlin.library_base.util.dp2px
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
import com.knight.kotlin.library_util.startPage
import com.knight.kotlin.library_util.toast.ToastUtils
import com.knight.kotlin.module_home.R
import com.knight.kotlin.module_home.constants.HomeConstants
import com.knight.kotlin.module_home.databinding.HomeFragmentBinding
import com.knight.kotlin.module_home.dialog.HomePushArticleFragment
import com.knight.kotlin.module_home.entity.EveryDayPushArticlesBean
import com.knight.kotlin.module_home.vm.HomeVm
import com.knight.library_biometric.control.BiometricControl
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.reflect.Type
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException


/**
 * Author:Knight
 * Time:2021/12/22 19:33
 * Description:HomeFragment
 */
@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteFragment.Home.HomeFragment)
class HomeFragment : BaseFragment<HomeFragmentBinding, HomeVm>() {


    /**
     * 首页Fragment
     */
    private var mFragments = mutableListOf<Fragment>()
    /**
     * 知识标签
     */
    private var knowledgeLabelList = mutableListOf<String>()
    /**
     * 获取推送文章
     */
    private lateinit var mEveryDayPushData: EveryDayPushArticlesBean
    override val mViewModel: HomeVm by viewModels()
    override fun HomeFragmentBinding.initView() {
        initMagicIndicator()
        setOnClickListener(homeIncludeToolbar.homeScanIcon,homeIncludeToolbar.homeTvLoginname,
            homeIncludeToolbar.homeIvEveryday,
            homeIncludeToolbar.homeIvAdd)
        getUser()?.let {
            homeIncludeToolbar.homeTvLoginname.text = it.username
        } ?: kotlin.run {
            homeIncludeToolbar.homeTvLoginname.text = getString(R.string.home_tv_login)
        }

    }

    /**
     *
     * 订阅LiveData
     */
    override fun initObserver() {
        observeLiveData(mViewModel.everyDayPushArticles, ::setEveryDayPushArticle)
        observeLiveData(mViewModel.articles, ::processPushArticle)
        observeLiveData(mViewModel.appUpdateMessage,::checkAppMessage)
        observeLiveData(mViewModel.userInfo,::setUserInfo)
    }

    override fun initRequestData() {
        mViewModel.getEveryDayPushArticle()
        mViewModel.checkAppUpdateMessage()
    }

    override fun setThemeColor(isDarkMode: Boolean) {
       if (!isDarkMode) {
           isWithStatusTheme(isDarkMode)
       }
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
        if (SystemUtils.getAppVersionCode(requireActivity())  < data.versionCode ) {
            if (data.versionName != activity?.let { SystemUtils.getAppVersionName(it) }) {
                UpdateAppDialogFragment.newInstance(data).showAllowingStateLoss(
                    parentFragmentManager, "dialog_update")

            }
        }
    }


    /**
     *
     * 登录
     */
    private fun setUserInfo(userInfoEntity: UserInfoEntity){
        CacheUtils.saveDataInfo(CacheKey.USER,userInfoEntity)
        Appconfig.user = userInfoEntity
        EventBusUtils.postEvent(MessageEvent(MessageEvent.MessageType.LoginSuccess))
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
           mBinding.homeIncludeToolbar.homeTvLoginname -> {
               if (mBinding.homeIncludeToolbar.homeTvLoginname.text.toString().equals(getString(R.string.home_tv_login))) {
                    if (CacheUtils.getGestureLogin()) {
                        startPage(RouteActivity.Mine.QuickLoginActivity)
                    } else if (CacheUtils.getFingerLogin()) {
                        loginBiomtric()
                    } else {
                        startPage(RouteActivity.Mine.LoginActivity)
                    }
               }
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

    override fun reLoadData() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event:MessageEvent) {
        when (event.type) {
            MessageEvent.MessageType.LoginSuccess -> {
                //登录成功
                mBinding.homeIncludeToolbar.homeTvLoginname.text = getUser()?.username
            }

            MessageEvent.MessageType.LogoutSuccess -> {
                //退出登录成功
                mBinding.homeIncludeToolbar.homeTvLoginname.setText(getString(R.string.home_tv_login))
            }
        }

    }

    /**
     * 指纹登录
     */
    private fun loginBiomtric() {
        BiometricControl.loginBlomtric(requireActivity(), object : BiometricControl.BiometricStatusCallback {
            override fun onUsePassword() {
                startPage(RouteActivity.Mine.LoginActivity)
            }

            override fun onVerifySuccess(cipher: Cipher?) {
                try {
                    val text = CacheUtils.getEncryptLoginMessage()
                    val input = Base64.decode(text, Base64.URL_SAFE)
                    val bytes = cipher?.doFinal(input)

                    /**
                     * 然后这里用原密码(当然是加密过的)调登录接口
                     */
                    val loginEntity: LoginEntity? =
                        GsonUtils.get(String(bytes ?: ByteArray(0)), LoginEntity::class.java)
                    val iv = cipher?.iv

                    //走登录接口
                    loginEntity?.let {
                        mViewModel.login(it.loginName,it.loginPassword)
                    }

                } catch (e: BadPaddingException) {
                    e.printStackTrace()
                } catch (e: IllegalBlockSizeException) {
                    e.printStackTrace()
                }
            }

            override fun onFailed() {
                startPage(RouteActivity.Mine.LoginActivity)
            }

            override fun error(code: Int, reason: String?) {
                ToastUtils.show("$code,$reason")
                startPage(RouteActivity.Mine.LoginActivity)
            }

            override fun onCancel() {
                ToastUtils.show(R.string.home_biometric_cancel)
                startPage(RouteActivity.Mine.LoginActivity)
            }
        })
    }


    /**
     * 状态栏是否跟随主题色变化
     *
     */
    private fun isWithStatusTheme(statusWIthTheme:Boolean) {
        if (!CacheUtils.getNightModeStatus()) {
            val gradientDrawable = GradientDrawable()
            gradientDrawable.shape = GradientDrawable.RECTANGLE
            gradientDrawable.cornerRadius = 45.dp2px().toFloat()
            if (statusWIthTheme) {
                gradientDrawable.setColor(Color.WHITE)
                mBinding.homeIncludeToolbar.homeTvLoginname.setTextColor(Color.WHITE)
                mBinding.homeIncludeToolbar.homeIvAdd.setBackgroundResource(R.drawable.home_icon_add_white)
                mBinding.homeIncludeToolbar.homeIvEveryday.setBackgroundResource(R.drawable.home_icon_everyday_white)
                mBinding.homeIncludeToolbar.toolbar.setBackgroundColor(CacheUtils.getThemeColor())
            } else {
                gradientDrawable.setColor(Color.parseColor("#1f767680"))
                mBinding.homeIncludeToolbar.homeTvLoginname.setTextColor(Color.parseColor("#333333"))
                mBinding.homeIncludeToolbar.homeIvEveryday.setBackgroundResource(R.drawable.home_icon_everyday)
                mBinding.homeIncludeToolbar.homeIvAdd.setBackgroundResource(R.drawable.home_icon_add)
                mBinding.homeIncludeToolbar.toolbar.setBackgroundColor(Color.WHITE)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mBinding.homeIncludeToolbar.homeRlSearch.background = gradientDrawable
            } else {
                mBinding.homeIncludeToolbar.homeRlSearch.setBackgroundDrawable(gradientDrawable)
            }


        }

    }





}