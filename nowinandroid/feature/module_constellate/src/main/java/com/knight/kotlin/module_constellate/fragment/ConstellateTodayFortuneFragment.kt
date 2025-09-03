package com.knight.kotlin.module_constellate.fragment

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.library_base.route.RouteFragment
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_constellate.R
import com.knight.kotlin.module_constellate.activity.ConstellateFateActivity
import com.knight.kotlin.module_constellate.adapter.ConstellateDateAdapter
import com.knight.kotlin.module_constellate.adapter.ConstellateFortuneTypeValueAdapter
import com.knight.kotlin.module_constellate.databinding.ConstellateTodayFortuneFragmentBinding
import com.knight.kotlin.module_constellate.entity.ConstellateDateEntity
import com.knight.kotlin.module_constellate.entity.ConstellateFortuneEntity
import com.knight.kotlin.module_constellate.entity.ConstellateTypeValueEntity
import com.knight.kotlin.module_constellate.vm.ConstellateFortuneVm
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter


/**
 * @author created by luguian
 * @organize
 * @Date 2025/8/6 15:58
 * @descript:日运
 */

@AndroidEntryPoint
@Route(path = RouteFragment.Constellate.ConstellateFortuneDateFragment)
class ConstellateTodayFortuneFragment : BaseFragment<ConstellateTodayFortuneFragmentBinding,ConstellateFortuneVm>(){





    private val mConstellateDateAdapter: ConstellateDateAdapter by lazy { ConstellateDateAdapter() }
    //各运势百分比
    private val mConstellateFortuneTypeValueAdapter: ConstellateFortuneTypeValueAdapter by lazy { ConstellateFortuneTypeValueAdapter() }
    //今日数据
    private var today:ConstellateFortuneEntity? = null
    //各子运势的值
    val fortuneTypeValues = mutableListOf<ConstellateTypeValueEntity>()


    private lateinit var nestedScrollView: NestedScrollView
    private val constellationIconMap = mapOf(
        "白羊座" to R.drawable.constellate_aries_icon,
        "金牛座" to R.drawable.constellate_taurus_icon,
        "双子座" to R.drawable.constellate_gemini_icon,
        "巨蟹座" to R.drawable.constellate_cancer_icon,
        "狮子座" to R.drawable.constellate_leo_icon,
        "处女座" to R.drawable.constellate_virgo_icon,
        "天秤座" to R.drawable.constellate_libra_icon,
        "天蝎座" to R.drawable.constellate_scorpio_icon,
        "射手座" to R.drawable.constellate_sagittarius_icon,
        "摩羯座" to R.drawable.constellate_capricorn_icon,
        "水瓶座" to R.drawable.constellate_aquarius_icon,
        "双鱼座" to R.drawable.constellate_pisces_icon,
    )



    companion object {
        fun newInstance(todayFortune: ConstellateFortuneEntity) : ConstellateTodayFortuneFragment {
            val mConstellateTodayFortuneFragment = ConstellateTodayFortuneFragment()
            val args = Bundle()
            args.putParcelable("today", todayFortune)
            mConstellateTodayFortuneFragment.arguments = args
            return mConstellateTodayFortuneFragment
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun ConstellateTodayFortuneFragmentBinding.initView() {
        nestedScrollView = requireActivity().findViewById(R.id.nest_fortune)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            today = arguments?.getParcelable("today", ConstellateFortuneEntity::class.java)
        } else {
            today = arguments?.getParcelable("today")
        }
        today?.run {
            tvLuckyColor.text = lucky_color
            tvLuckyNumber.text = lucky_number
            ivLuckyConstellate.setBackgroundResource(getConstellationIcon(lucky_star))
            tvConstellateSuitable.text = yi
            tvConstellateTaboo.text = ji

            val mAllConstellateTypeValueEntity = ConstellateTypeValueEntity("综合","all",all.replace("%", "").toInt())
            val mLoveConstellateTypeValueEntity = ConstellateTypeValueEntity("爱情","love",love.replace("%", "").toInt())
            val mWorkConstellateTypeValueEntity = ConstellateTypeValueEntity("事学","work",work.replace("%", "").toInt())
            val mMoneyConstellateTypeValueEntity = ConstellateTypeValueEntity("财富","money",money.replace("%", "").toInt())
            val mHealthConstellateTypeValueEntity = ConstellateTypeValueEntity("健康","health",health.replace("%", "").toInt())
            val mDiscussConstellateTypeValueEntity = ConstellateTypeValueEntity("商谈","discuss",discuss.replace("%", "").toInt())
            fortuneTypeValues.add(mAllConstellateTypeValueEntity)
            fortuneTypeValues.add(mLoveConstellateTypeValueEntity)
            fortuneTypeValues.add(mWorkConstellateTypeValueEntity)
            fortuneTypeValues.add(mMoneyConstellateTypeValueEntity)
            fortuneTypeValues.add(mHealthConstellateTypeValueEntity)
            fortuneTypeValues.add(mDiscussConstellateTypeValueEntity)
        }

        rvConstellateDate.init(LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false), mConstellateDateAdapter, false)
        mConstellateDateAdapter.submitList(getWeekData())


        rvConstellateTypeValue.init(LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false),mConstellateFortuneTypeValueAdapter, false)
        mConstellateFortuneTypeValueAdapter.submitList(fortuneTypeValues)
        nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            if (ViewInitUtils.isViewVisibleInScroll(nestedScrollView,rvConstellateTypeValue)) {

                mConstellateFortuneTypeValueAdapter.executeAnimation()
                }
            (requireActivity() as? ConstellateFateActivity)?.updateToolbarColor(scrollY)
        }

    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }
    override fun reLoadData() {

    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun getWeekData():List<ConstellateDateEntity> {
        val today = LocalDate.now()
        val dayFormatter = DateTimeFormatter.ofPattern("d")

        val dayInfoList = (-4..2).map {
            val date = today.plusDays(it.toLong())
            val dayNumber = date.dayOfWeek.value
            val display = when (date) {
                today -> "今"
                today.plusDays(1) -> "明"
                else -> date.format(dayFormatter)
            }
            ConstellateDateEntity(dayNumber, display)
        }

        dayInfoList.forEach {
            println("星期数字：${it.dayNumber}, 显示：${it.display}")
        }

        return dayInfoList
    }


    fun getConstellationIcon(name: String): Int {
        return constellationIconMap[name] ?: R.drawable.constellate_leo_icon
    }

    override fun onDestroy() {
        super.onDestroy()
        mConstellateFortuneTypeValueAdapter.cancelAnimation()
    }






}