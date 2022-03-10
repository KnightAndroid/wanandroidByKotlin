package com.knight.kotlin.module_home.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.widget.ImageView
import android.widget.RelativeLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.module_home.R
import com.knight.kotlin.module_home.entity.TopArticleBean

/**
 * Author:Knight
 * Time:2022/3/8 17:47
 * Description:TopArticleAroundAdapter
 */
class TopArticleAroundAdapter(data:MutableList<TopArticleBean>):
    BaseQuickAdapter<TopArticleBean, BaseViewHolder>(R.layout.home_toparticle_around_item,data) {

    private var selectItem = 0

    fun setSelectItem(selectItem: Int) {
        this.selectItem = selectItem
    }

    fun getSelectItem(): Int {
        return selectItem
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (getSelectItem() == position) {
            holder.itemView.findViewById<RelativeLayout>(R.id.home_rl_toparticle).setScaleX(1.3f)
            holder.itemView.findViewById<RelativeLayout>(R.id.home_rl_toparticle).setScaleY(1.3f)
        } else {
            holder.itemView.findViewById<RelativeLayout>(R.id.home_rl_toparticle).setScaleX(1.0f)
            holder.itemView.findViewById<RelativeLayout>(R.id.home_rl_toparticle).setScaleY(1.0f)
        }
    }


    override fun convert(holder: BaseViewHolder, item: TopArticleBean) {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.setColor(Color.parseColor("#55aff4"))
        gradientDrawable.shape = GradientDrawable.OVAL
        holder.itemView.findViewById<ImageView>(R.id.home_iv_toparticle).background = gradientDrawable
        holder.setText(R.id.home_tv_toparticle_author,item.author.substring(0,1))
    }
}