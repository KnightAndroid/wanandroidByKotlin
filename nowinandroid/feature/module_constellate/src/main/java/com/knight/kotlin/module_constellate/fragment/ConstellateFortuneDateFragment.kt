package com.knight.kotlin.module_constellate.fragment

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.library_base.route.RouteFragment
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_constellate.adapter.ConstellateDateAdapter
import com.knight.kotlin.module_constellate.databinding.ConstellateFortuneDateFragmentBinding
import com.knight.kotlin.module_constellate.entity.ConstellateDateEntity
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
class ConstellateFortuneDateFragment : BaseFragment<ConstellateFortuneDateFragmentBinding,ConstellateFortuneVm>(){



    private val mConstellateDateAdapter: ConstellateDateAdapter by lazy { ConstellateDateAdapter() }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun ConstellateFortuneDateFragmentBinding.initView() {

        rvConstellateDate.init(LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false), mConstellateDateAdapter, false)
        mConstellateDateAdapter.submitList(getWeekData())

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

        // 测试输出
        dayInfoList.forEach {
            println("星期数字：${it.dayNumber}, 显示：${it.display}")
        }

        return dayInfoList
    }
}