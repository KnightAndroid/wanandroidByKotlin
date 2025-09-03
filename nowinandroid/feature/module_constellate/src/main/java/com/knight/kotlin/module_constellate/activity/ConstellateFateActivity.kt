package com.knight.kotlin.module_constellate.activity


import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.core.library_common.util.dp2px
import com.google.android.material.tabs.TabLayoutMediator
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.ktx.statusHeight
import com.knight.kotlin.library_common.util.LanguageFontSizeUtils
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.module_constellate.R
import com.knight.kotlin.module_constellate.databinding.ConstellateFortuneActivityBinding
import com.knight.kotlin.module_constellate.dialog.ConstellateSelectDialog
import com.knight.kotlin.module_constellate.entity.ConstellateTypeEntity
import com.knight.kotlin.module_constellate.fragment.ConstellateMonthFortuneFragment
import com.knight.kotlin.module_constellate.fragment.ConstellateTodayFortuneFragment
import com.knight.kotlin.module_constellate.fragment.ConstellateWeekFortuneFragment
import com.knight.kotlin.module_constellate.fragment.ConstellateYearFortuneFragment
import com.knight.kotlin.module_constellate.vm.ConstellateFortuneVm
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
class ConstellateFateActivity : BaseActivity<ConstellateFortuneActivityBinding, ConstellateFortuneVm>(){

    @JvmField
    @Param(name="constellate")
    var constellate: ConstellateTypeEntity? = null
    private lateinit var typeArrayIcons: TypedArray

    val titleFortunes = mutableListOf("日运", "周运", "月运", "年运")
    private val mFragments = mutableListOf<Fragment>()
    private var scrollThreshold = 0
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        mViewModel.getConstellateFortune(constellate?.enName?:"","today").observerKt {
            mFragments.add(ConstellateTodayFortuneFragment.newInstance(it.tomorrow))
            mFragments.add(ConstellateWeekFortuneFragment())
            mFragments.add(ConstellateMonthFortuneFragment())
            mFragments.add(ConstellateYearFortuneFragment())

            ViewInitUtils.setViewPager2Init(this@ConstellateFateActivity,mBinding.constellateViewPager,mFragments,
                isOffscreenPageLimit = true,
                isUserInputEnabled = false
            )
            TabLayoutMediator(mBinding.constellateTabLayout, mBinding.constellateViewPager) { tab, pos ->
                tab.text = titleFortunes[pos]
            }.attach()
            val tabStrip = mBinding.constellateTabLayout.getChildAt(0) as ViewGroup
            for (i in 0 until tabStrip.childCount) {
                val tab = tabStrip.getChildAt(i)
                tab.setPadding(4.dp2px(), 0, 4.dp2px(), 0)  // 左右间距为 12dp，你可以改成其他数值
            }
        }
    }

    override fun reLoadData() {

    }

    override fun ConstellateFortuneActivityBinding.initView() {
        scrollThreshold = statusHeight.dp2px()
        constellateFateToolbar.baseIvBack.setOnClick {
            finish()
        }
        constellate?.run {
            val displayName = if (LanguageFontSizeUtils.isChinese()) name else enName
            mBinding.title = displayName + getString(R.string.constellate_detail_toolbar_title)
        }

        constellateFateToolbar.baseIvBack.setBackgroundResource(com.core.library_base.R.drawable.base_right_whitearrow)
        constellateFateToolbar.baseTvTitle.setTextColor(Color.WHITE)
        //constellateFateToolbar.baseCompatToolbar.setBackgroundColor(Color.parseColor("#6B87FA"))


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



        if (LanguageFontSizeUtils.isChinese()) {
            tvConstellateName.text = constellate?.name
        } else {
            tvConstellateName.text = constellate?.enName
        }

        tvConstellateName.setOnClick{
            ConstellateSelectDialog.newInstance(constellate?.name ?: "").show(supportFragmentManager,"constellateSelect")

        }

        nestFortune.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            updateToolbarColor(scrollY)

        }


    }


    fun updateToolbarColor(scrollY: Int) {
        // 判断是否在顶部附近 (0 - 40dp 范围内)
        if (scrollY <= scrollThreshold) {
            // 在顶部附近，将文字颜色设置为白色
            mBinding.constellateFateToolbar.baseIvBack.setBackgroundResource(com.core.library_base.R.drawable.base_right_whitearrow)
            mBinding.constellateFateToolbar.baseTvTitle.setTextColor(Color.WHITE)
        } else {
            // 向下滑动超过阈值，将文字颜色设置为黑色
            mBinding.constellateFateToolbar.baseIvBack.setBackgroundResource(com.core.library_base.R.drawable.base_iv_left_arrow)
            mBinding.constellateFateToolbar.baseTvTitle.setTextColor(Color.BLACK)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.constellateCloudView.stop()
    }

}