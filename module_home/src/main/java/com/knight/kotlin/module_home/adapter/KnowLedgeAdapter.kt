package com.knight.kotlin.module_home.adapter

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_widget.flowlayout.TagInfo
import com.knight.kotlin.module_home.R

/**
 * Author:Knight
 * Time:2022/4/24 17:14
 * Description:KnowLedgeAdapter
 */
class KnowLedgeAdapter(data:MutableList<TagInfo>):BaseQuickAdapter<TagInfo,BaseViewHolder>(
    R.layout.home_knowledgelabel_item,data) {
    private var isEdit = false
    override fun convert(holder: BaseViewHolder, item: TagInfo) {
        holder.setText(R.id.home_tv_knowledge, item.tagName)
        holder.setTextColor(R.id.home_tv_knowledge, CacheUtils.getThemeColor())
        val gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        gradientDrawable.setStroke(2, CacheUtils.getThemeColor())
        gradientDrawable.cornerRadius = 6.dp2px().toFloat()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            holder.getView<View>(R.id.home_tv_knowledge).setBackground(gradientDrawable)
        } else {
            holder.getView<View>(R.id.home_tv_knowledge)
                .setBackgroundDrawable(gradientDrawable)
        }
        //如果是编辑状态
        if (isEdit) {
            holder.setVisible(R.id.home_iv_moreknowledge_delete, true)
        } else {
            holder.setGone(R.id.home_iv_moreknowledge_delete, true)
        }

    }
    fun setIsEdit(isEdit: Boolean) {
        this.isEdit = isEdit
        notifyDataSetChanged()
    }

    fun getIsEdit(): Boolean {
        return isEdit
    }

}