package com.knight.kotlin.module_home.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.core.library_common.util.ColorUtils
import com.knight.kotlin.library_common.entity.OfficialAccountEntity
import com.knight.kotlin.module_home.databinding.HomeOfficialAccountsItemBinding

/**
 * Author:Knight
 * Time:2022/3/11 16:32
 * Description:OfficialAccountAdapter
 */
class OfficialAccountAdapter : BaseQuickAdapter<OfficialAccountEntity, OfficialAccountAdapter.VH>() {

    // 自定义ViewHolder类
    class VH(
        parent: ViewGroup,
        val binding: HomeOfficialAccountsItemBinding = HomeOfficialAccountsItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: OfficialAccountEntity?) {
        item?.run {
           val gradientDrawable = GradientDrawable()
           gradientDrawable.shape = GradientDrawable.OVAL
           gradientDrawable.setColor(ColorUtils.getRandColorCode())
           holder.binding.homeIvOfficialAccount.background = gradientDrawable
           holder.binding.homeTvOfficialaccountName.setText(name)
           holder.binding.tvHeadUsername.setText(name.substring(0,1))
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

}