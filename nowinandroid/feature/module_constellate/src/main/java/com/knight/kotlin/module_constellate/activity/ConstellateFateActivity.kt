package com.knight.kotlin.module_constellate.activity


import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.core.library_base.vm.EmptyViewModel
import com.core.library_common.util.dp2px
import com.google.android.material.tabs.TabLayoutMediator
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_common.util.LanguageFontSizeUtils
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.module_constellate.R
import com.knight.kotlin.module_constellate.databinding.ConstellateFortuneActivityBinding
import com.knight.kotlin.module_constellate.entity.ConstellateTypeEntity
import com.knight.kotlin.module_constellate.fragment.ConstellateFortuneDateFragment
import com.wyjson.router.annotation.Param
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize 
 * @Date 2025/6/19 9:25
 * @descript:星座具体信息
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Constellate.ConstellateFortuneActivity)
class ConstellateFateActivity : BaseActivity<ConstellateFortuneActivityBinding, EmptyViewModel>(){

    @JvmField
    @Param(name="constellate")
    var constellate: ConstellateTypeEntity? = null
    private lateinit var typeArrayIcons: TypedArray

    val titleFortunes = mutableListOf("日运", "周运", "月运", "年运","爱情")
    private val mFragments = mutableListOf<Fragment>()


    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun ConstellateFortuneActivityBinding.initView() {
        constellate?.run {
            val displayName = if (LanguageFontSizeUtils.isChinese()) name else enName
            mBinding.title = displayName + getString(R.string.constellate_detail_toolbar_title)
        }

        constellateFateToolbar.baseIvBack.setBackgroundResource(com.core.library_base.R.drawable.base_right_whitearrow)
        constellateFateToolbar.baseTvTitle.setTextColor(Color.WHITE)
        constellateFateToolbar.baseIvBack.setOnClick {
            finish()
        }

        constellate?.run {
            typeArrayIcons = resources.obtainTypedArray(R.array.constellate_type)
            val drawable: Drawable? = typeArrayIcons.getDrawable(position) // 'index' 将是你的动态索引
            constellateHeadIv.setImageDrawable(drawable)
        }

        constellateHeadIv.post {
            val headLocation = IntArray(2)
            val cloudLocation = IntArray(2)

            constellateHeadIv.getLocationInWindow(headLocation)
            constellateCloudView.getLocationInWindow(cloudLocation)

            val relativeX = headLocation[0] - cloudLocation[0] + constellateHeadIv.width / 2f
            val relativeY = headLocation[1] - cloudLocation[1] + constellateHeadIv.height / 2f
            val radius = constellateHeadIv.width / 2f + 12f

            constellateCloudView.setAvoidCircle(relativeX, relativeY, radius)
        }
        constellateCloudView.setCloudImages(R.drawable.constellate_cloud_behind, R.drawable.constellate_cloud_front,0.5f,1.0f)
        constellateCloudView.start()

        mFragments.add(ConstellateFortuneDateFragment())
        mFragments.add(ConstellateFortuneDateFragment())
        mFragments.add(ConstellateFortuneDateFragment())
        mFragments.add(ConstellateFortuneDateFragment())

        ViewInitUtils.setViewPager2Init(this@ConstellateFateActivity,constellateViewPager,mFragments,
            isOffscreenPageLimit = true,
            isUserInputEnabled = false
        )
        TabLayoutMediator(constellateTabLayout, constellateViewPager) { tab, pos ->
            tab.text = titleFortunes[pos]
        }.attach()
        val tabStrip = constellateTabLayout.getChildAt(0) as ViewGroup
        for (i in 0 until tabStrip.childCount) {
            val tab = tabStrip.getChildAt(i)
            tab.setPadding(4.dp2px(), 0, 4.dp2px(), 0)  // 左右间距为 12dp，你可以改成其他数值
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.constellateCloudView.stop()
    }

}