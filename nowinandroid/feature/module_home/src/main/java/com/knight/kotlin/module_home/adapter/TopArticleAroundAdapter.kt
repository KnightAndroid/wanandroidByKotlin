package com.knight.kotlin.module_home.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.module_home.databinding.HomeToparticleAroundItemBinding
import com.knight.kotlin.module_home.entity.TopArticleBean

/**
 * Author:Knight
 * Time:2022/3/8 17:47
 * Description:TopArticleAroundAdapter
 */
class TopArticleAroundAdapter:
    BaseQuickAdapter<TopArticleBean, TopArticleAroundAdapter.VH>() {


    // 自定义ViewHolder类
    class VH(
        parent: ViewGroup,
        val binding: HomeToparticleAroundItemBinding = HomeToparticleAroundItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)
    private var selectItem = 0

    fun setSelectItem(selectItem: Int) {
        this.selectItem = selectItem
    }

    fun getSelectItem(): Int {
        return selectItem
    }

    override fun onBindViewHolder(holder: VH, position: Int, item: TopArticleBean?) {
        item?.run{
            if (getSelectItem() == position) {
                holder.binding.homeRlToparticle.setScaleX(1.3f)
                holder.binding.homeRlToparticle.setScaleY(1.3f)
            } else {
                holder.binding.homeRlToparticle.setScaleX(1.0f)
                holder.binding.homeRlToparticle.setScaleY(1.0f)
            }
            val gradientDrawable = GradientDrawable()
            gradientDrawable.setColor(Color.parseColor("#55aff4"))
            gradientDrawable.shape = GradientDrawable.OVAL
            holder.binding.homeIvToparticle.background = gradientDrawable
            holder.binding.homeTvToparticleAuthor.setText(item.author.substring(0,1))
        }

    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        val vh = VH(parent)
        return vh
    }


}