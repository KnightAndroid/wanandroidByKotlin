package com.knight.kotlin.module_mine.adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.module_mine.databinding.MineOpensourceItemBinding
import com.knight.kotlin.module_mine.entity.OpenSourceBean

/**
 * Author:Knight
 * Time:2022/2/16 11:40
 * Description:OpenSourceAdapter
 */
class OpenSourceAdapter: BaseQuickAdapter<OpenSourceBean, OpenSourceAdapter.VH>() {

    class VH(
        parent: ViewGroup,
        val binding: MineOpensourceItemBinding = MineOpensourceItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)




    override fun onBindViewHolder(holder: VH, position: Int, item: OpenSourceBean?) {
        //赋值
        item?.run {
            holder.binding.mineOpensourceTitle.setText(name)
            holder.binding.mineOpensourceDesc.setText(desc)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
               holder.binding.mineOpensourceAbroadlink.text = Html.fromHtml("<u>$abroadlink</u>", Html.FROM_HTML_MODE_LEGACY)
            } else {
                holder.binding.mineOpensourceAbroadlink.text = Html.fromHtml("<u>$abroadlink</u>")
            }
            holder.binding.mineOpensourceTitle.setTextColor(CacheUtils.getThemeColor())
            holder.binding.mineOpensourceAbroadlink.setTextColor(CacheUtils.getThemeColor())

        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}