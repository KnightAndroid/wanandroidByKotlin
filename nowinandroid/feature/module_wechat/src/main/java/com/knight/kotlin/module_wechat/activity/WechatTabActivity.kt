package com.knight.kotlin.module_wechat.activity
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import com.core.library_base.contact.EmptyContract
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.core.library_base.vm.EmptyMviViewModel
import com.core.library_common.util.dp2px
import com.knight.kotlin.library_base.activity.BaseMviActivity
import com.knight.kotlin.library_base.entity.OfficialAccountEntity
import com.knight.kotlin.library_util.SystemUtils
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.library_util.bindWechatViewPager2
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_wechat.R
import com.knight.kotlin.module_wechat.databinding.WechatTabActivityBinding
import com.knight.kotlin.module_wechat.fragment.WechatOfficialAccountFragment
import com.wyjson.router.annotation.Param
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Route(path = RouteActivity.Wechat.WechatTabActivity)
class WechatTabActivity :
    BaseMviActivity<
            WechatTabActivityBinding,
            EmptyMviViewModel,
            EmptyContract.Event,
            EmptyContract.State,
            EmptyContract.Effect>() {

    @JvmField
    @Param(name = "data")
    var data: ArrayList<OfficialAccountEntity>? = null

    @JvmField
    @Param(name = "position")
    var position: Int = 0

    private var selectIndex: Int = 0

    private val wechatArticleFragments = ArrayList<Fragment>()
    private val titleDatas = ArrayList<String>()

    override fun WechatTabActivityBinding.initView() {
        mBinding.title = getString(R.string.wechat_official)
        includeWechatToolbar.baseIvBack.setOnClick { finish() }

        initData()

        wechatSearchEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val keywords = wechatSearchEt.text.toString().trim()
                if (keywords.isEmpty()) {
                    toast(R.string.wechat_search_hint)
                } else {
                    searchWeChatArticles(keywords)
                }
                true
            } else {
                false
            }
        }
    }

    override fun initObserver() {

    }

    private fun initData() {
        wechatArticleFragments.clear()
        titleDatas.clear()

        data?.forEach {
            wechatArticleFragments.add(
                WechatOfficialAccountFragment.newInstance(it.id)
            )
            titleDatas.add(it.name)
        }

        ViewInitUtils.setViewPager2Init(
            this,
            mBinding.wechatViewPager,
            wechatArticleFragments,
            isOffscreenPageLimit = true,
            isUserInputEnabled = false
        )

        mBinding.wechatIndicator.bindWechatViewPager2(
            mBinding.wechatViewPager,
            titleDatas
        ) {
            selectIndex = it
        }

        mBinding.wechatViewPager.currentItem = position
    }

    // ======================
    // MVI 必须实现的方法
    // ======================

    override fun renderState(state: EmptyContract.State) {
        // no-op
    }

    override fun handleEffect(effect: EmptyContract.Effect) {
        // no-op
    }

    override fun initRequestData() {
        // no-op
    }

    override fun reLoadData() {
        // no-op
    }

    override fun setThemeColor(isDarkMode: Boolean) {
        val cursorDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(themeColor)
            setSize(2.dp2px(), 2.dp2px())
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mBinding.wechatSearchEt.textCursorDrawable = cursorDrawable
        } else {
            SystemUtils.setCursorDrawableColor(
                mBinding.wechatSearchEt,
                themeColor
            )
        }
    }

    /**
     * 搜索当前选中公众号的文章
     */
    private fun searchWeChatArticles(keyword: String) {
        (wechatArticleFragments[selectIndex] as? WechatOfficialAccountFragment)
            ?.searchArticlesByKeyWords(keyword)
    }
}