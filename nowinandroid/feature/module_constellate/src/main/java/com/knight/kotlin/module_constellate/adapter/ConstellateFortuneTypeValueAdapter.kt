package com.knight.kotlin.module_constellate.adapter

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_base.ktx.getScreenWidth
import com.knight.kotlin.library_common.util.LanguageFontSizeUtils
import com.knight.kotlin.module_constellate.databinding.ConstellateFortuneTypeValueItemBinding
import com.knight.kotlin.module_constellate.entity.ConstellateTypeValueEntity


/**
 * @author created by luguian
 * @organize
 * @Date 2025/9/3 10:32
 * @descript:各类型运势指数值
 */
class ConstellateFortuneTypeValueAdapter :
    BaseQuickAdapter<ConstellateTypeValueEntity, ConstellateFortuneTypeValueAdapter.VH>() {

    class VH(
        parent: ViewGroup,
        val binding: ConstellateFortuneTypeValueItemBinding = ConstellateFortuneTypeValueItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var mConstellateTypeValueEntity: ConstellateTypeValueEntity
        var hasAnimated = false // 标记动画是否执行过

        fun onBindView(item: ConstellateTypeValueEntity) {
            mConstellateTypeValueEntity = item
            hasAnimated = false
            // 设置文本
            binding.tvFortuneType.text = if (LanguageFontSizeUtils.isChinese()) {
                item.name
            } else {
                item.enName
            }
            binding.tvFortuneTypeValue.text = Html.fromHtml(
                "<font color='#000000'>${item.value}</font><font color='#666666'>%</font>",
                Html.FROM_HTML_MODE_LEGACY
            )
        }

        fun executeAnimation() {
//            if (!hasAnimated) {
//                hasAnimated = true
//                binding.pbTypeFortuneValue.doOnPreDraw {
//
//                        //   binding.pbTypeFortuneValue.setProgress(0)
//                        binding.pbTypeFortuneValue.setProgress(mConstellateTypeValueEntity.value, true)
//
//                }
//
//            }

            if (!hasAnimated && binding.pbTypeFortuneValue.width > 0 && binding.pbTypeFortuneValue.height > 0) {
                hasAnimated = true
                binding.pbTypeFortuneValue.setProgress(mConstellateTypeValueEntity.value, true)
            }
        }

        fun cancelAnimation() {
            binding.pbTypeFortuneValue.cancelAnimator()
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int, item: ConstellateTypeValueEntity?) {
        val itemWidth = getScreenWidth() / items.size
        item?.let {
            holder.onBindView(it)
            holder.binding.clFortuneTypeValue.layoutParams.width = itemWidth
        }
    }

    /**
     * 遍历 RecyclerView 可见 Holder 执行动画
     */
    fun executeVisibleAnimation(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
        val first = layoutManager.findFirstVisibleItemPosition()
        val last = layoutManager.findLastVisibleItemPosition()

        for (i in first..last) {
            val holder = recyclerView.findViewHolderForAdapterPosition(i) as? VH
            holder?.executeAnimation()
        }
    }

    fun cancelAllAnimation(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
        val first = layoutManager.findFirstVisibleItemPosition()
        val last = layoutManager.findLastVisibleItemPosition()

        for (i in first..last) {
            val holder = recyclerView.findViewHolderForAdapterPosition(i) as? VH
            holder?.cancelAnimation()
        }
    }
}