package com.knight.kotlin.module_home.adapter

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.FloatEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_widget.utils.WeatherUtils
import com.knight.kotlin.module_home.adapter.WeatherIndexAdapter.VH
import com.knight.kotlin.module_home.databinding.HomeWeatherAirPollutantItemBinding
import com.knight.kotlin.module_home.entity.PollutantBean


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/16 11:30
 * @descript:天气污染物适配器
 */
class WeatherPullutantAdapter:BaseQuickAdapter<PollutantBean,WeatherPullutantAdapter.VH>() {

    private val mHolderList: MutableList<VH> = mutableListOf()

    // 自定义ViewHolder类
    class VH(
        parent: ViewGroup,
        val binding: HomeWeatherAirPollutantItemBinding = HomeWeatherAirPollutantItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var mPollutantBean:  PollutantBean
        private var mAttachAnimatorSet: AnimatorSet? = null
        private var mExecuteAnimation = true
        fun onBindView(PollutantBean:  PollutantBean) {
            mPollutantBean = PollutantBean
        }
        fun executeAnimation() {
            if (mExecuteAnimation) {
                mPollutantBean?.let { item ->
                    mExecuteAnimation = false
                    val progressColor = ValueAnimator.ofObject(
                        ArgbEvaluator(),
                        ContextCompat.getColor(itemView.context, com.knight.kotlin.library_widget.R.color.widget_air_colorLevel_1),
                        WeatherUtils.getColor(itemView.context, mPollutantBean.pollutantIndex.getAirQualityLevel(mPollutantBean.concentration))
                    )
                    progressColor.addUpdateListener { animation: ValueAnimator ->
                        binding.itemAqiProgress.setProgressColor((animation.animatedValue as Int))
                    }
//                    val backgroundColor = ValueAnimator.ofObject(
//                        ArgbEvaluator(),
//                        com.google.android.material.R.attr.colorOutline,
//                        ColorUtils.setAlphaComponent(WeatherUtils.getColor(itemView.context, mPollutantBean.pollutantIndex.getAirQualityLevel(mPollutantBean.concentration)), (255 * 0.1).toInt())
//                    )
//                    backgroundColor.addUpdateListener { animation: ValueAnimator ->
//                        binding.itemAqiProgress.setProgressBackgroundColor((animation.animatedValue as Int))
//                    }
                    val aqiNumber = ValueAnimator.ofObject(FloatEvaluator(), 0, mPollutantBean.concentration)
                    aqiNumber.addUpdateListener { animation: ValueAnimator ->
                        binding.itemAqiProgress.progress = 100.0f * animation.animatedValue as Float / mPollutantBean.pollutantIndex.getMaxValue()
                    }
                    mAttachAnimatorSet = AnimatorSet().apply {
                        playTogether(progressColor, aqiNumber)
                        interpolator = DecelerateInterpolator(3f)
                        duration = (mPollutantBean.concentration / mPollutantBean.pollutantIndex.getMaxValue() * 5000).toLong()
                        start()
                    }
                }
            }
        }

        fun cancelAnimation() {
            mAttachAnimatorSet?.let {
                if (it.isRunning) it.cancel()
            }
            mAttachAnimatorSet = null
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int, item: PollutantBean?) {
        item?.run {
            holder.binding.itemAqiProgress.apply {
                progress = 0f
                max = pollutantIndex.getMaxValue()
                setProgressColor(WeatherUtils.getColor(context, pollutantIndex.getAirQualityLevel(concentration)))
                setProgressBackgroundColor(
                    ColorUtils.setAlphaComponent(WeatherUtils.getColor(context, item.pollutantIndex.getAirQualityLevel(item.concentration)), (255 * 0.1).toInt())
                )

            }

            holder.binding.itemAqiTitle.text = pollutantIndex.name
            holder.binding.itemAqiContent.text = concentration.toString() + "/"+ unit
            holder.onBindView(item)
        }

        mHolderList.add(holder)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

    fun executeAnimation() {
        for (viewHolder in mHolderList) {
            viewHolder.executeAnimation()
        }
    }

    fun cancelAnimation() {
        for (viewHolder in mHolderList) {
            viewHolder.cancelAnimation()
        }
        mHolderList.clear()
    }
}