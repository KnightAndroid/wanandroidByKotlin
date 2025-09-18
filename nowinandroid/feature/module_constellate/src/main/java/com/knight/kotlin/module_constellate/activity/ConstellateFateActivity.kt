package com.knight.kotlin.module_constellate.activity


import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.graphics.ColorUtils
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.core.library_common.util.dp2px
import com.google.android.material.tabs.TabLayoutMediator
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.ktx.statusHeight
import com.knight.kotlin.library_common.util.LanguageFontSizeUtils
import com.knight.kotlin.library_util.DateUtils
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.library_widget.ktx.setViewPager2Height
import com.knight.kotlin.module_constellate.R
import com.knight.kotlin.module_constellate.databinding.ConstellateFortuneActivityBinding
import com.knight.kotlin.module_constellate.dialog.ConstellateSelectDialog
import com.knight.kotlin.module_constellate.entity.ConstellateResponseEntity
import com.knight.kotlin.module_constellate.entity.ConstellateTypeEntity
import com.knight.kotlin.module_constellate.enums.FortuneTimeType
import com.knight.kotlin.module_constellate.fragment.ConstellateTodayFortuneFragment
import com.knight.kotlin.module_constellate.vm.ConstellateFortuneVm
import com.knight.kotlin.module_constellate.vm.ConstellateShareVm
import com.wyjson.router.annotation.Param
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


/**
 * @author created by luguian
 * @organize
 * @Date 2025/6/19 9:25
 * @descript:星座具体信息
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Constellate.ConstellateFortuneActivity)
class ConstellateFateActivity : BaseActivity<ConstellateFortuneActivityBinding, ConstellateFortuneVm>() {

    @JvmField
    @Param(name = "constellate")
    var constellate: ConstellateTypeEntity? = null
    var constellateResponseEntity : ConstellateResponseEntity?=null
    private lateinit var typeArrayIcons: TypedArray

    val titleFortunes = mutableListOf("日运", "周运", "月运", "年运")
    private val mFragments = mutableListOf<Fragment>()
    private var scrollThreshold = 0
    private val sharedViewModel: ConstellateShareVm by viewModels()
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        mViewModel.getConstellateFortuneWorkStudy(constellate?.name ?: "") { errorMsg ->
            // 这里处理失败逻辑
            getConstellateFortunebyName("","")
        }.observerKt { fortune ->
            getConstellateFortunebyName(fortune.study,fortune.work)
        }


    }

    override fun reLoadData() {

    }

    /**
     *
     * 根据对应星座获取详情
     */
    fun getConstellateFortunebyName(study:String,work:String,isFirstInit:Boolean = true) {
        mViewModel.getConstellateFortune(constellate?.enName?:"","today").observerKt {
            mFragments.clear()
            mBinding.constellateCloudView.start()
            constellateResponseEntity = it
            //这里如何拿到外面 it.study值？
            it.tomorrow.study_children_text = study
            it.tomorrow.work_children_text = work
            mBinding.tvShortcomment.text = it.tomorrow.notice
            mFragments.add(ConstellateTodayFortuneFragment.newInstance(it.tomorrow,FortuneTimeType.DAY))
            mFragments.add(ConstellateTodayFortuneFragment.newInstance(it.week,FortuneTimeType.WEEK))
            mFragments.add(ConstellateTodayFortuneFragment.newInstance(it.month,FortuneTimeType.MONTH))
            mFragments.add(ConstellateTodayFortuneFragment.newInstance(it.year,FortuneTimeType.YEAR))

            ViewInitUtils.setViewPager2Init(this@ConstellateFateActivity,mBinding.constellateViewPager,mFragments,
                isOffscreenPageLimit = true,
                isUserInputEnabled = false
            )

            if (isFirstInit) {
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


    }

    override fun ConstellateFortuneActivityBinding.initView() {
        scrollThreshold = statusHeight.dp2px()
        constellateFateToolbar.baseIvBack.setOnClick {
            finish()
        }

        mBinding.constellateFateToolbar.baseIvBack.background = null
        constellateFateToolbar.baseIvBack.setImageResource(com.core.library_base.R.drawable.base_right_whitearrow)
        constellateFateToolbar.baseTvTitle.setTextColor(Color.WHITE)
        //constellateFateToolbar.baseCompatToolbar.setBackgroundColor(Color.parseColor("#6B87FA"))




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
        constellateCloudView.setCloudImages(R.drawable.constellate_cloud_behind, R.drawable.constellate_cloud_front, 0.5f, 1.0f)


        updateConstellateMessage()

        tvConstellateName.setOnClick {
            ConstellateSelectDialog.newInstance(constellate?.name ?: "").show(supportFragmentManager, "constellateSelect")

        }

        nestFortune.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                // 滚动监听逻辑
                updateToolbarColor(scrollY)
            }
        )

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.selectedConstellate.collect { entity ->
                    constellate = entity
                    updateConstellateMessage()

                    mViewModel.getConstellateFortuneWorkStudy(constellate?.name ?: "") { errorMsg ->
                        // 这里处理失败逻辑
                        getConstellateFortunebyName("","",false)
                    }.observerKt { fortune ->
                        getConstellateFortunebyName(fortune.study,fortune.work,false)
                    }
                }
            }
        }

        mBinding.constellateViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        mBinding.tvFirstTime.text = DateUtils.getCurrentDay().toString()
                        mBinding.tvEndTime.text = "/${DateUtils.getCurrentMonth()}月"
                        mBinding.tvShortcomment.text = constellateResponseEntity?.tomorrow?.notice
                    }
                    1 -> {
                        mBinding.tvFirstTime.text = DateUtils.getCurrentWeekRangeSundayStart()
                        mBinding.tvEndTime.text = "/${DateUtils.getCurrentMonth()}月"
                        mBinding.tvShortcomment.text = constellateResponseEntity?.week?.notice
                    }
                    2 -> {
                        mBinding.tvFirstTime.text = DateUtils.getCurrentMonth().toString()
                        mBinding.tvEndTime.text = "月"
                        mBinding.tvShortcomment.text = constellateResponseEntity?.month?.notice
                    }
                    3 -> {
                        mBinding.tvFirstTime.text = DateUtils.getCurrentYear().toString()
                        mBinding.tvEndTime.text = "年"
                        mBinding.tvShortcomment.text = constellateResponseEntity?.year?.notice
                    }
                }

//                val recyclerView = mBinding.constellateViewPager.getChildAt(0) as RecyclerView
//                val view = recyclerView.layoutManager?.findViewByPosition(position)
//                view?.post {
//                    val params = mBinding.constellateViewPager.layoutParams
//                    params.height = view.measuredHeight
//                    mBinding.constellateViewPager.layoutParams = params
//                }

                setViewPager2Height(mBinding.constellateViewPager,position)
            }
        })
    }

    /**
     *
     *
     */
    fun updateConstellateMessage() {
        constellate?.run {
            val displayName = if (LanguageFontSizeUtils.isChinese()) name else enName
            mBinding.title = displayName + getString(R.string.constellate_detail_toolbar_title)
        }

        constellate?.run {
            typeArrayIcons = resources.obtainTypedArray(R.array.constellate_type)
            val drawable: Drawable? = typeArrayIcons.getDrawable(position) // 'index' 将是你的动态索引
            mBinding.constellateHeadIv.setImageDrawable(drawable)
        }

        if (LanguageFontSizeUtils.isChinese()) {
            mBinding.tvConstellateName.text = constellate?.name
        } else {
            mBinding.tvConstellateName.text = constellate?.enName
        }

    }

    /**
     *
     * 更新标题栏颜色 信息
     */
    fun updateToolbarColor(scrollY: Int) {
        // 判断是否在顶部附近 (0 - 40dp 范围内)
        // 根据滚动距离计算透明度（0f - 1f）
        var alpha = scrollY.toFloat() / scrollThreshold
        if (alpha > 1f) alpha = 1f
        if (alpha < 0f) alpha = 0f

        // 设置 Toolbar 背景颜色，带透明度
        mBinding.constellateFateToolbar.baseCompatToolbar.setBackgroundColor(Color.argb(
            (alpha * 255).toInt(), // 透明度 (0~255)
            255,                   // R
            255,                   // G
            255                    // B
        ))

        // 箭头颜色渐变：白 -> 黑
        val arrowColor = ColorUtils.blendARGB(
            Color.WHITE,
            Color.BLACK,
            alpha
        )
        mBinding.constellateFateToolbar.baseIvBack.setColorFilter(arrowColor)
        mBinding.constellateFateToolbar.baseTvTitle.setTextColor(arrowColor)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.constellateCloudView.stop()
    }

}