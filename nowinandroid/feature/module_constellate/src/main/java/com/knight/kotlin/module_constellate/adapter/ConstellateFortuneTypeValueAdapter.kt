package com.knight.kotlin.module_constellate.adapter

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
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
class ConstellateFortuneTypeValueAdapter : BaseQuickAdapter<ConstellateTypeValueEntity,ConstellateFortuneTypeValueAdapter.VH>(){



    private val mHolderList: MutableList<VH> = mutableListOf()

    class VH(
        parent: ViewGroup,
        val binding: ConstellateFortuneTypeValueItemBinding = ConstellateFortuneTypeValueItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var mConstellateTypeValueEntity:  ConstellateTypeValueEntity
        fun onBindView(PollutantBean:  ConstellateTypeValueEntity) {
            mConstellateTypeValueEntity = PollutantBean
        }
        fun executeAnimation() {
            binding.pbTypeFortuneValue.setProgress(mConstellateTypeValueEntity.value,true)
        }

        fun cancelAnimation() {
            binding.pbTypeFortuneValue.cancelAnimator()
        }
    }




    override fun onBindViewHolder(holder: VH, position: Int, item: ConstellateTypeValueEntity?) {
        val itemWidth = getScreenWidth() / items.size
        item?.run {
          //  holder.binding.pbTypeFortuneValue.setProgress(value,false)
            holder.binding.tvFortuneType.text = if (LanguageFontSizeUtils.isChinese()) {
                 name
            } else {
                enName
            }
            holder.binding.tvFortuneTypeValue.text = Html.fromHtml(
                "<font color='#000000'>$value</font><font color='#666666'>%</font>",
                Html.FROM_HTML_MODE_LEGACY
            )
            holder.onBindView(this)
            mHolderList.add(holder)
        }

        holder.binding.clFortuneTypeValue.layoutParams.width = itemWidth

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