package com.knight.kotlin.module_constellate.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_constellate.R
import com.knight.kotlin.module_constellate.databinding.ConstellateFortuneDescItemBinding
import com.knight.kotlin.module_constellate.entity.ConstellateTypeValueEntity

/**
 * @Description 运势各类型星级
 * @Author knight
 * @Time 2025/9/9 21:00
 *
 */

class ConstellateFortuneTypeDescAdapter:BaseQuickAdapter<ConstellateTypeValueEntity,ConstellateFortuneTypeDescAdapter.VH>() {


    class VH(
        parent: ViewGroup,
        val binding: ConstellateFortuneDescItemBinding = ConstellateFortuneDescItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: ConstellateTypeValueEntity?) {
        item?.run {
            if (name == "综合") {
                holder.binding.ivConstellateFortune.setBackgroundResource(R.drawable.constellate_all_icon)
                holder.binding.tvFortuneTypeName.text = "综合运势"
            } else if (name == "爱情") {
                holder.binding.ivConstellateFortune.setBackgroundResource(R.drawable.constellate_love_icon)
                holder.binding.tvFortuneTypeName.text = "爱情运势"
            } else if (name == "事学") {
                holder.binding.ivConstellateFortune.setBackgroundResource(R.drawable.constellate_career_icon)
                holder.binding.tvFortuneTypeName.text = "事业学业"
            }else if (name == "财富") {
                holder.binding.ivConstellateFortune.setBackgroundResource(R.drawable.constellate_money_icon)
                holder.binding.tvFortuneTypeName.text = "财富运势"
            }else  {
                holder.binding.ivConstellateFortune.setBackgroundResource(R.drawable.constellate_healthy_icon)
                holder.binding.tvFortuneTypeName.text = "健康运势"
            }

            holder.binding.tvFortuneDesc.text = desc
            holder.binding.fortuneStarBar.setStarRating(scoreToStars(value))
            if (children.size > 0 && !children[0].desc.isNullOrEmpty() && !children[1].desc.isNullOrEmpty()) {
                val mConstellateFortuneChildTypeDescAdapter: ConstellateFortuneChildTypeDescAdapter by lazy { ConstellateFortuneChildTypeDescAdapter() }
                holder.binding.rvConstellateChildren.init(LinearLayoutManager(context),mConstellateFortuneChildTypeDescAdapter, false)
                mConstellateFortuneChildTypeDescAdapter.submitList(children)
                holder.binding.rvConstellateChildren.visibility = View.VISIBLE
            } else {
                holder.binding.rvConstellateChildren.visibility = View.GONE
            }

        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

    override fun getItemCount(items: List<ConstellateTypeValueEntity>): Int {
        return items.size - 1
    }


    fun scoreToStars(score: Int): Float {
        // 保证范围在 0~5 之间
        return (score / 100f) * 5f
    }

}