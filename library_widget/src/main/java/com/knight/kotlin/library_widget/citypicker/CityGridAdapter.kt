package com.knight.kotlin.library_widget.citypicker


import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_base.ktx.getScreenWidth
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_widget.databinding.CityHotGridItemBinding


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/17 14:37
 * @descript:城市网格适配器
 */
class CityGridAdapter(val mInnerListener:InnerListener):BaseQuickAdapter<CityBean,CityGridAdapter.VH>() {

    // 自定义ViewHolder类
    class VH(
        parent: ViewGroup,
        val binding: CityHotGridItemBinding = CityHotGridItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)




    override fun onBindViewHolder(holder: VH, position: Int, item: CityBean?) {

        item?.run {
            val pos = holder.getAbsoluteAdapterPosition()

            //设置item宽高
            val space = 16.dp2px()
            val padding = context.resources.getDimensionPixelSize(com.knight.kotlin.library_widget.R.dimen.widget_city_default_padding)
            val indexBarWidth = context.resources.getDimensionPixelSize(com.knight.kotlin.library_widget.R.dimen.widget_city_index_bar_width)
            val itemWidth: Int = (getScreenWidth() - padding - space * (3 - 1) - indexBarWidth) / 3
            val lp: ViewGroup.LayoutParams = holder.binding.cityGridItemLayout.getLayoutParams()
            lp.width = itemWidth
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT
            holder.binding.cityGridItemLayout.setLayoutParams(lp)

            holder.binding.cityGirdItemName.setText(city)
            holder.binding.cityGridItemLayout.setOnClickListener(View.OnClickListener {
                if (mInnerListener != null) {
                    mInnerListener.dismiss(pos, item)
                }
            })
        }

    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

}