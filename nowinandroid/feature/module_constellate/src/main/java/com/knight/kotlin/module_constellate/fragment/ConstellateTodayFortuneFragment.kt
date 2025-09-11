package com.knight.kotlin.module_constellate.fragment

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.library_base.route.RouteFragment
import com.core.library_common.util.dp2px
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_util.DateUtils
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.library_widget.RecyclerItemDecoration
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_constellate.R
import com.knight.kotlin.module_constellate.activity.ConstellateFateActivity
import com.knight.kotlin.module_constellate.adapter.ConstellateDateAdapter
import com.knight.kotlin.module_constellate.adapter.ConstellateFortuneTypeDescAdapter
import com.knight.kotlin.module_constellate.adapter.ConstellateFortuneTypeValueAdapter
import com.knight.kotlin.module_constellate.adapter.ConstellateWeekDateAdapter
import com.knight.kotlin.module_constellate.databinding.ConstellateTodayFortuneFragmentBinding
import com.knight.kotlin.module_constellate.entity.ConstellateDateEntity
import com.knight.kotlin.module_constellate.entity.ConstellateFortuneChildrenEntity
import com.knight.kotlin.module_constellate.entity.ConstellateFortuneEntity
import com.knight.kotlin.module_constellate.entity.ConstellateTypeValueEntity
import com.knight.kotlin.module_constellate.enums.FortuneTimeType
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




    //日运 日期适配器
    private val mConstellateDateAdapter: ConstellateDateAdapter by lazy { ConstellateDateAdapter() }
    //周运 周日期适配器
    private val mConstellateWeekDateAdapter:ConstellateWeekDateAdapter by lazy { ConstellateWeekDateAdapter() }

    //各运势百分比
    private val mConstellateFortuneTypeValueAdapter: ConstellateFortuneTypeValueAdapter by lazy { ConstellateFortuneTypeValueAdapter() }
    //今日数据
    private var today:ConstellateFortuneEntity? = null
    //各运势详情
    val fortuneTypeValues = mutableListOf<ConstellateTypeValueEntity>()
    private val mConstellateTypeDescAdapter:ConstellateFortuneTypeDescAdapter by lazy { ConstellateFortuneTypeDescAdapter() }
    private var fortuneTimeType: FortuneTimeType? = null

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
        fun newInstance(todayFortune: ConstellateFortuneEntity,type:FortuneTimeType) : ConstellateTodayFortuneFragment {
            val mConstellateTodayFortuneFragment = ConstellateTodayFortuneFragment()
            val args = Bundle()
            args.putParcelable("today", todayFortune)
            args.putString("type",type.name)
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
        fortuneTimeType = arguments?.getString("type")?.let { FortuneTimeType.valueOf(it) }


        today?.run {
            if (fortuneTimeType == FortuneTimeType.DAY || fortuneTimeType == FortuneTimeType.WEEK) {
                llLuckyMessage.visibility = View.VISIBLE
                tvLuckyColor.text = lucky_color
                tvLuckyNumber.text = lucky_number
                if (fortuneTimeType == FortuneTimeType.DAY) {
                    rlLuckyStar.visibility = View.VISIBLE
                    ivLuckyConstellate.setBackgroundResource(getConstellationIcon(lucky_star))
                } else {
                    rlLuckyStar.visibility = View.GONE
                }
            } else {
                llLuckyMessage.visibility = View.GONE
            }


            tvConstellateSuitable.text = yi
            tvConstellateTaboo.text = ji

            val mAllConstellateTypeValueEntity = ConstellateTypeValueEntity("综合","all",all.replace("%", "").toInt(),all_text)
            val mLoveConstellateTypeValueEntity = ConstellateTypeValueEntity("爱情","love",love.replace("%", "").toInt(),love_text)
            val studyWorkchildren:MutableList<ConstellateFortuneChildrenEntity> = mutableListOf()
            val mWorkConstellateTypeValueEntity = if (fortuneTimeType == FortuneTimeType.DAY) {
                val mChildWorkEntity = ConstellateFortuneChildrenEntity("工作", work_children_text)
                val mChildStudyEntity = ConstellateFortuneChildrenEntity("学习", study_children_text)
                studyWorkchildren.add(mChildWorkEntity)
                studyWorkchildren.add(mChildStudyEntity)
                ConstellateTypeValueEntity("事学","work", work.replace("%", "").toInt(), work_text, studyWorkchildren)
            } else {
                ConstellateTypeValueEntity("事学","work", work.replace("%", "").toInt(), work_text)
            }
            val mMoneyConstellateTypeValueEntity = ConstellateTypeValueEntity("财富","money",money.replace("%", "").toInt(),money_text)
            val mHealthConstellateTypeValueEntity = ConstellateTypeValueEntity("健康","health",health.replace("%", "").toInt(),health_text)

            fortuneTypeValues.add(mAllConstellateTypeValueEntity)
            fortuneTypeValues.add(mLoveConstellateTypeValueEntity)
            fortuneTypeValues.add(mWorkConstellateTypeValueEntity)
            fortuneTypeValues.add(mMoneyConstellateTypeValueEntity)
            fortuneTypeValues.add(mHealthConstellateTypeValueEntity)
            if (fortuneTimeType == FortuneTimeType.DAY) {
                val mDiscussConstellateTypeValueEntity = ConstellateTypeValueEntity("商谈","discuss",discuss.replace("%", "").toInt(),"")
                fortuneTypeValues.add(mDiscussConstellateTypeValueEntity)
            }

        }

        when (fortuneTimeType) {
            FortuneTimeType.DAY -> {
                rvConstellateDate.init(LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false), mConstellateDateAdapter, false)
                mConstellateDateAdapter.submitList(getWeekData())
            }
            FortuneTimeType.WEEK -> {
                rvConstellateDate.init(LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false), mConstellateWeekDateAdapter, false)
                mConstellateWeekDateAdapter.submitList(DateUtils.getFiveWeeksLabels())
            }

            FortuneTimeType.MONTH -> {

            }

            FortuneTimeType.YEAR -> {

            }
            null -> {

            }

        }



        rvConstellateTypeValue.init(LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false),mConstellateFortuneTypeValueAdapter, false)
        rvConstellateFortuneValue.init(LinearLayoutManager(requireActivity()),mConstellateTypeDescAdapter,false)
        rvConstellateFortuneValue.addItemDecoration(

            RecyclerItemDecoration(0,10.dp2px(),0,0)
        )


        mConstellateFortuneTypeValueAdapter.submitList(fortuneTypeValues)
        mConstellateTypeDescAdapter.submitList(fortuneTypeValues)
        rvConstellateTypeValue.post {
            nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
                if (ViewInitUtils.isViewVisibleInScroll(nestedScrollView,rvConstellateTypeValue)) {

                    mConstellateFortuneTypeValueAdapter.executeVisibleAnimation(rvConstellateTypeValue)
                }
                (requireActivity() as? ConstellateFateActivity)?.updateToolbarColor(scrollY)
            }
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
        //mConstellateFortuneTypeValueAdapter.cancelAnimation()
    }






}