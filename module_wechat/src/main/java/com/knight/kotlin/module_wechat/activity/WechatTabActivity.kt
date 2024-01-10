package com.knight.kotlin.module_wechat.activity
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_common.entity.OfficialAccountEntity
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
class WechatTabActivity : BaseActivity<WechatTabActivityBinding, EmptyViewModel>() {
    override val mViewModel: EmptyViewModel by viewModels()

    @JvmField
    @Param(name="data")
    var data: ArrayList<OfficialAccountEntity>?=null

    @JvmField
    @Param(name="position")
    var position:Int = 0

    private var selectIndex:Int = 0


    private val wechatArticleFragments = ArrayList<Fragment>()
    private val titleDatas = ArrayList<String>()
    override fun WechatTabActivityBinding.initView() {
        includeWechatToolbar.baseTvTitle.setText(getString(R.string.wechat_official))
        includeWechatToolbar.baseIvBack.setOnClick { finish() }
        initData()
        wechatSearchEt.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchKeywords = wechatSearchEt.text.toString().trim()
                if (searchKeywords.isNullOrEmpty()) {
                    toast(R.string.wechat_search_hint)
                } else {
                    serachWeChatArticles(searchKeywords)
                }
                true
            }
            false
        }
    }



    private fun initData() {
        wechatArticleFragments.clear()
        titleDatas.clear()
        for (i in 0 until (data?.size ?:0 )) {
            wechatArticleFragments.add(WechatOfficialAccountFragment.newInstance(data?.get(i)?.id ?: 0))
            titleDatas.add(data?.get(i)?.name ?:"")
        }

        ViewInitUtils.setViewPager2Init(this,mBinding.wechatViewPager,wechatArticleFragments,
            isOffscreenPageLimit = true,
            isUserInputEnabled = false
        )
        mBinding.wechatIndicator.bindWechatViewPager2(mBinding.wechatViewPager,titleDatas) {
            selectIndex = it
        }
        mBinding.wechatViewPager.currentItem = position
    }


    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun setThemeColor(isDarkMode: Boolean) {
        val cursorDrawable = GradientDrawable()
        cursorDrawable.shape = GradientDrawable.RECTANGLE
        cursorDrawable.setColor(themeColor)
        cursorDrawable.setSize(2.dp2px(),2.dp2px())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mBinding.wechatSearchEt.textCursorDrawable = cursorDrawable
        } else {
            SystemUtils.setCursorDrawableColor(mBinding.wechatSearchEt,themeColor)
        }
    }


    /**
     *
     * 根据搜索框关键字进行搜索
     */
    fun serachWeChatArticles(searchKeywords:String){
        (wechatArticleFragments.get(selectIndex) as WechatOfficialAccountFragment).searchArticlesByKeyWords(searchKeywords)
    }
}