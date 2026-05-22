package com.knight.kotlin.module_mine.fragment

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.util.Base64
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.library_base.annotation.EventBusRegister
import com.core.library_base.event.MessageEvent
import com.core.library_base.route.RouteActivity
import com.core.library_base.route.RouteFragment
import com.core.library_base.util.EventBusUtils
import com.core.library_base.util.GsonUtils
import com.core.library_common.util.ColorUtils
import com.core.library_common.util.dp2px
import com.flyjingfish.android_aop_core.annotations.SingleClick
import com.google.common.reflect.TypeToken
import com.knight.kotlin.library_aop.loginintercept.LoginCheck
import com.knight.kotlin.library_base.entity.LoginEntity
import com.knight.kotlin.library_base.fragment.BaseMviFragment
import com.knight.kotlin.library_common.config.Appconfig
import com.knight.kotlin.library_common.config.CacheKey
import com.knight.kotlin.library_common.entity.UserInfoEntity
import com.knight.kotlin.library_common.ktx.getUser
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.library_util.JsonUtils
import com.knight.kotlin.library_util.SystemUtils
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.library_util.startPage
import com.knight.kotlin.library_util.startPageWithParams
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.library_util.toast.ToastUtils
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.library_widget.ktx.setSafeOnItemChildClickListener
import com.knight.kotlin.library_widget.ktx.setSafeOnItemClickListener
import com.knight.kotlin.module_mine.R
import com.knight.kotlin.module_mine.activity.LoginActivity
import com.knight.kotlin.module_mine.activity.QuickLoginActivity
import com.knight.kotlin.module_mine.adapter.MineItemAdapter
import com.knight.kotlin.module_mine.adapter.OpenSourceAdapter
import com.knight.kotlin.module_mine.contract.MineContract
import com.knight.kotlin.module_mine.databinding.MineFragmentBinding
import com.knight.kotlin.module_mine.entity.MineItemEntity
import com.knight.kotlin.module_mine.entity.OpenSourceBean
import com.knight.kotlin.module_mine.entity.UserInfoMessageEntity
import com.knight.kotlin.module_mine.vm.MineViewModel
import com.knight.library_biometric.control.BiometricControl
import com.knight.library_biometric.listener.BiometricStatusCallback
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.scwang.smart.refresh.layout.simple.SimpleMultiListener
import com.wyjson.router.GoRouter
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException

/**nab
 * Author:Knight
 * Time:2021/12/23 18:12
 * Description:MineFragment
 */
@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteFragment.Mine.MineFragment)
class MineFragment :
    BaseMviFragment<
            MineFragmentBinding,
            MineViewModel,
            MineContract.Event,
            MineContract.State,
            MineContract.Effect>(),
    OnRefreshListener {

    /**
     * 是否打开二楼
     */
    private var openTwoLevel = false

    /**
     * 开源库适配器
     */
    private val mOpenSourceAdapter: OpenSourceAdapter by lazy {
        OpenSourceAdapter()
    }

    /**
     * 我的操作项适配器
     */
    private val mMineItemAdapter: MineItemAdapter by lazy {
        MineItemAdapter()
    }

    override fun MineFragmentBinding.initView() {

        getUser()?.let {

            mineTvUsername.text = it.username

            mineIvMessage.visibility = View.VISIBLE

        } ?: run {

            mineIvMessage.visibility = View.GONE
        }

        setOnClickListener(
            mineTvUsername,
            mineLlRank,
            mineIvMessage,
            mineIconFab,
            mineRlMain
        )

        mineRefreshLayout.setOnRefreshListener(this@MineFragment)

        mineRefreshLayout.autoRefresh()

        mineTwoLevelHeader.setEnablePullToCloseTwoLevel(true)

        mineRefreshLayout.setOnMultiListener(
            object : SimpleMultiListener() {

                override fun onHeaderMoving(
                    header: RefreshHeader?,
                    isDragging: Boolean,
                    percent: Float,
                    offset: Int,
                    headerHeight: Int,
                    maxDragHeight: Int
                ) {
                }

                override fun onRefresh(refreshLayout: RefreshLayout) {
                }

                override fun onStateChanged(
                    refreshLayout: RefreshLayout,
                    oldState: RefreshState,
                    newState: RefreshState
                ) {

                    when (oldState) {

                        RefreshState.TwoLevel -> {

                            mineTwoLevelContent
                                .animate()
                                .alpha(0f)
                                .setDuration(0)
                        }

                        RefreshState.TwoLevelReleased -> {

                            openTwoLevel = true

                            mineIconFab.visibility = View.VISIBLE
                        }

                        RefreshState.TwoLevelFinish -> {

                            openTwoLevel = false

                            mineIconFab.visibility = View.GONE
                        }

                        else -> {}
                    }
                }
            }
        )

        mineTwoLevelHeader.setOnTwoLevelListener {

            mineTwoLevelContent
                .animate()
                .alpha(1f)
                .duration = 1000

            true
        }

        initTwoLevel()

        initMineItemAdapter()
    }

    override fun initObserver() {

    }

    /**
     * 初始化请求
     */
    override fun initRequestData() {

        getUser()?.let {

            sendEvent(
                MineContract.Event.RefreshUserInfo
            )
        }
    }

    /**
     * 渲染状态
     */
    override fun renderState(state: MineContract.State) {

        if (state.isLoading) {

            requestLoading(mBinding.mineRefreshLayout)

        } else {

            requestSuccess()
        }

        state.userCoinInfo?.let {

            setUserInfoCoin(it)
        }

        state.userInfo?.let {

            setUserInfo(it)
        }
    }

    /**
     * 处理副作用
     */
    override fun handleEffect(effect: MineContract.Effect) {

        when (effect) {

            is MineContract.Effect.ShowToast -> {

                toast(effect.msg)
            }

            MineContract.Effect.LoginSuccess -> {

            }

            is MineContract.Effect.LoginFailure -> {

            }
            else ->{}
        }
    }

    override fun setThemeColor(isDarkMode: Boolean) {

        mBinding.mineTvUsername.setTextColor(themeColor)

        mBinding.mineTvLevel.setTextColor(themeColor)

        mBinding.mineTvRank.setTextColor(themeColor)

        val gradientDrawable = GradientDrawable()

        gradientDrawable.shape = GradientDrawable.RECTANGLE

        gradientDrawable.cornerRadius =
            10.dp2px().toFloat()

        gradientDrawable.setColor(themeColor)

        mBinding.mineRvItem.background =
            gradientDrawable

        mBinding.mineIconFab.backgroundTintList =
            ColorUtils.createColorStateList(
                themeColor,
                themeColor
            )
    }

    /**
     * 设置用户金币
     */
    private fun setUserInfoCoin(
        userInfoMessageEntity: UserInfoMessageEntity
    ) {

        CacheUtils.saveUserRank(
            userInfoMessageEntity.coinInfo.rank
        )

        val gradientDrawable = GradientDrawable()

        gradientDrawable.shape =
            GradientDrawable.OVAL

        gradientDrawable.setColor(
            ColorUtils.getRandColorCode()
        )

        mBinding.mineIvHead.background =
            gradientDrawable

        mBinding.mineTvUserabbr.text =
            userInfoMessageEntity.coinInfo.username.substring(
                0,
                1
            )

        mBinding.mineTvUsername.text =
            userInfoMessageEntity.coinInfo.username

        mBinding.mineTvLevel.text =
            getString(R.string.mine_gradle) +
                    userInfoMessageEntity.coinInfo.level

        mBinding.mineTvRank.text =
            getString(R.string.mine_rank) +
                    userInfoMessageEntity.coinInfo.rank
    }

    /**
     * 设置登录信息
     */
    private fun setUserInfo(
        userInfo: UserInfoEntity
    ) {

        mBinding.mineIvMessage.visibility =
            View.VISIBLE

        Appconfig.user = userInfo

        CacheUtils.saveDataInfo(
            CacheKey.USER,
            userInfo
        )

        EventBusUtils.postEvent(
            MessageEvent(
                MessageEvent.MessageType.LoginSuccess
            )
        )

        sendEvent(
            MineContract.Event.RefreshUserInfo
        )
    }

    override fun reLoadData() {

        sendEvent(
            MineContract.Event.RefreshUserInfo
        )
    }

    @SingleClick
    override fun onClick(v: View) {

        when (v) {

            mBinding.mineTvUsername -> {

                if (getUser() == null) {

                    if (CacheUtils.getGestureLogin() == true) {

                        startActivity(
                            Intent(
                                activity,
                                QuickLoginActivity::class.java
                            )
                        )

                    } else if (CacheUtils.getFingerLogin()) {

                        loginBlomtric()

                    } else {

                        startActivity(
                            Intent(
                                activity,
                                LoginActivity::class.java
                            )
                        )
                    }
                }
            }

            mBinding.mineLlRank -> {

                startPage(
                    RouteActivity.Mine.UserCoinRankActivity
                )
            }

            mBinding.mineIvMessage -> {

                startPage(
                    RouteActivity.Message.MessageActivity
                )
            }

            mBinding.mineIconFab -> {

                mBinding.mineTwoLevelHeader.finishTwoLevel()
            }
        }
    }

    /**
     * 初始化二楼
     */
    private fun initTwoLevel() {

        mBinding.secondOpenframeRv.init(
            LinearLayoutManager(requireActivity()),
            mOpenSourceAdapter
        )

        val type =
            object : TypeToken<List<OpenSourceBean>>() {}.type

        val jsonData =
            JsonUtils.getJson(
                requireActivity(),
                "opensourceproject.json"
            )

        val mDataList:
                MutableList<OpenSourceBean> =
            GsonUtils.getList(
                jsonData,
                type
            )

        mOpenSourceAdapter.submitList(
            mDataList
        )

        mOpenSourceAdapter.run {

            setSafeOnItemChildClickListener(
                R.id.mine_opensource_abroadlink
            ) { _, _, position ->

                GoRouter.getInstance()
                    .build(RouteActivity.Web.WebPager)
                    .withString(
                        "webUrl",
                        items[position].abroadlink
                    )
                    .withString(
                        "webTitle",
                        items[position].name
                    )
                    .go()
            }

            setSafeOnItemChildClickListener(
                R.id.mine_iv_abroadcopy
            ) { _, _, position ->

                SystemUtils.copyContent(
                    requireActivity(),
                    items[position].abroadlink
                )

                ToastUtils.show(
                    com.core.library_base.R.string.base_success_copylink
                )
            }

            setSafeOnItemClickListener { _, _, position ->

                GoRouter.getInstance()
                    .build(RouteActivity.Web.WebPager)
                    .withString(
                        "webUrl",
                        items[position].abroadlink
                    )
                    .withString(
                        "webTitle",
                        items[position].name
                    )
                    .go()
            }
        }
    }

    /**
     * 初始化功能列表
     */
    private fun initMineItemAdapter() {

        mBinding.mineRvItem.init(
            LinearLayoutManager(requireActivity()),
            mMineItemAdapter
        )

        val type =
            object : TypeToken<List<MineItemEntity>>() {}.type

        val jsonData =
            JsonUtils.getJson(
                requireActivity(),
                "mineitem.json"
            )

        val mDataList:
                MutableList<MineItemEntity> =
            GsonUtils.getList(
                jsonData,
                type
            )

        mMineItemAdapter.submitList(
            mDataList
        )

        mMineItemAdapter.run {

            setSafeOnItemClickListener { _, _, position ->

                when (items[position].id) {

                    1 -> {
                        goVideos()
                    }

                    2 -> {
                        goCoinsDetail()
                    }

                    3 -> {
                        goMyCollectArticles()
                    }

                    4 -> {
                        goMyShareArticles()
                    }

                    5 -> {

                        startPage(
                            RouteActivity.Mine.HistoryRecordActivity
                        )
                    }

                    6 -> {

                        GoRouter.getInstance()
                            .build(RouteActivity.Set.SetActivity)
                            .go()
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {

        when (event.type) {

            MessageEvent.MessageType.LoginSuccess -> {

                sendEvent(
                    MineContract.Event.RefreshUserInfo
                )

                mBinding.mineIvMessage.visibility =
                    View.VISIBLE

                mMineItemAdapter.notifyDataSetChanged()
            }

            MessageEvent.MessageType.LogoutSuccess -> {

                sendEvent(
                    MineContract.Event.Logout
                )

                mBinding.mineTvUserabbr.text = ""

                mBinding.mineTvUsername.text =
                    getString(R.string.mine_please_login)

                mBinding.mineTvLevel.text =
                    getString(R.string.mine_nodata_gradle)

                mBinding.mineTvRank.text =
                    getString(R.string.mine_nodata_rank)

                mBinding.mineIvMessage.visibility =
                    View.GONE

                mBinding.mineIvHead.background =
                    null

                ImageLoader.loadCircleIntLocalPhoto(
                    requireActivity(),
                    R.drawable.mine_iv_default_head,
                    mBinding.mineIvHead
                )

                mMineItemAdapter.notifyDataSetChanged()
            }

            else -> {}
        }
    }

    private fun goVideos() {

        startPage(
            RouteActivity.Video.VideoMainActivity
        )
    }

    @LoginCheck
    private fun goCoinsDetail() {

        val userCoins =
            getUser()?.coinCount ?: 0

        startPageWithParams(
            RouteActivity.Mine.MyPointsActivity,
            "userCoin" to userCoins.toString()
        )
    }

    @LoginCheck
    private fun goMyCollectArticles() {

        startPage(
            RouteActivity.Mine.MyCollectArticleActivity
        )
    }

    @LoginCheck
    private fun goMyShareArticles() {

        startPage(
            RouteActivity.Mine.MyShareArticlesActivity
        )
    }

    /**
     * 指纹登录
     */
    private fun loginBlomtric() {

        BiometricControl.setStatusCallback(
            object : BiometricStatusCallback {

                override fun onUsePassword() {

                    startActivity(
                        Intent(
                            activity,
                            LoginActivity::class.java
                        )
                    )
                }

                override fun onVerifySuccess(
                    cipher: Cipher?
                ) {

                    try {

                        val text =
                            CacheUtils.getEncryptLoginMessage()

                        val input =
                            Base64.decode(
                                text,
                                Base64.URL_SAFE
                            )

                        val bytes =
                            cipher?.doFinal(input)

                        val loginEntity: LoginEntity? =
                            GsonUtils.get(
                                String(
                                    bytes ?: ByteArray(0)
                                ),
                                LoginEntity::class.java
                            )

                        loginEntity?.let {

                            sendEvent(
                                MineContract.Event.Login(
                                    it.loginName,
                                    it.loginPassword
                                )
                            )
                        }

                    } catch (e: BadPaddingException) {

                        e.printStackTrace()

                    } catch (e: IllegalBlockSizeException) {

                        e.printStackTrace()
                    }
                }

                override fun onFailed() {

                    startActivity(
                        Intent(
                            activity,
                            LoginActivity::class.java
                        )
                    )
                }

                override fun error(
                    code: Int,
                    reason: String?
                ) {

                    toast("$code,$reason")

                    startActivity(
                        Intent(
                            activity,
                            LoginActivity::class.java
                        )
                    )
                }

                override fun onCancel() {

                    toast(
                        R.string.mine_tv_quicklogin_cancel
                    )

                    startActivity(
                        Intent(
                            activity,
                            LoginActivity::class.java
                        )
                    )
                }
            }
        ).loginBlomtric(requireActivity())
    }

    override fun onRefresh(
        refreshLayout: RefreshLayout
    ) {

        mBinding.mineRefreshLayout.finishRefresh()

        sendEvent(
            MineContract.Event.RefreshUserInfo
        )
    }

    override fun onDestroy() {

        BiometricControl.setunListener()

        super.onDestroy()
    }
}