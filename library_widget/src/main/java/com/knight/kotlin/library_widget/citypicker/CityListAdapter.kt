package com.knight.kotlin.library_widget.citypicker

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.shape.ShapeAppearanceModel
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_widget.GroupCityListBean
import com.knight.kotlin.library_widget.databinding.CityDefaultItemBinding


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/17 16:05
 * @descript:城市item布局
 */
class CityListAdapter(val mInnerListener:InnerListener): BaseQuickAdapter<GroupCityListBean, CityListAdapter.VH>() {



    var stateChanged:Boolean = false
    lateinit var mLayoutManager:LinearLayoutManager
    /**
     *
     * 更新定位城市
     */
    fun updateLocateState(location: CityBean) {
//        data.removeAt(0)
//        val locationListData = CityListBean(CityEnum.LOCATION.type, mutableListOf(location))
//        data.add(0, locationListData)
//        stateChanged = true
//        refreshLocationItem()
    }

    fun refreshLocationItem() {
        //如果定位城市的item可见则进行刷新
        if (stateChanged) {
            stateChanged = false
            notifyItemChanged(0)
        }
    }


    /**
     * 滚动RecyclerView到索引位置
     * @param index
     */
    fun scrollToSection(index: String) {
        val size = items.size
        for (i in 0 until size) {
            if (TextUtils.equals(index, items.get(i).group)) {
                if (mLayoutManager != null) {
                    mLayoutManager.scrollToPositionWithOffset(i, 0)
                    return
                }
            }
        }
    }


    // 自定义ViewHolder类

    class VH(
        parent: ViewGroup,
        val binding: CityDefaultItemBinding = CityDefaultItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
        val chipCache: MutableList<Chip> = mutableListOf(), // 缓存 Chip 视图
        val mInnerListener: InnerListener
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            // 预先创建一些 Chip 视图并添加到 chipCache，初始时隐藏
            for (i in 0 until 15) { // 假设每个 Item 最多显示 15 个 Chip，根据实际情况调整
                val chip = Chip(ContextThemeWrapper(itemView.context, com.knight.kotlin.library_base.R.style.base_MyChipTheme), null, 0).apply {
                    shapeAppearanceModel = ShapeAppearanceModel.Builder()
                        .setAllCornerSizes(12f)
                        .build()
                    chipStartPadding = 10.dp2px().toFloat()
                    chipEndPadding = 10.dp2px().toFloat()
                    textStartPadding = 8.dp2px().toFloat()
                    textEndPadding = 8.dp2px().toFloat()
                    chipMinHeight = 48.dp2px().toFloat()
                    setBackgroundColor(Color.parseColor("#EDEDED"))
                    val marginLayoutParams = ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    marginLayoutParams.setMargins(10.dp2px(), 5.dp2px(), 10.dp2px(), 5.dp2px()) // 设置左、上、右、下边距
                    layoutParams = marginLayoutParams
                    visibility = View.GONE // 初始时隐藏

                }
                binding.cityChipGroup.addView(chip)
                chipCache.add(chip)
            }
            binding.cityChipGroup.setTag(com.knight.kotlin.library_widget.R.id.city_tag, emptyList<CityBean>())
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int, item: GroupCityListBean?) {
        item?.run {
            val binding = holder.binding
            val chipGroup = binding.cityChipGroup
            val newCities = item.city

            val currentTag = chipGroup.getTag(com.knight.kotlin.library_widget.R.id.city_tag) as? List<CityBean>

            if (currentTag != newCities) {
                chipGroup.setTag(com.knight.kotlin.library_widget.R.id.city_tag, newCities)

                // 显示和更新必要的 Chip 视图
                for (i in newCities.indices) {
                    if (i < holder.chipCache.size) {
                        val chip = holder.chipCache[i]
                        chip.text = newCities[i].city
                        chip.visibility = View.VISIBLE
                        chip.setOnClick {

                           /* 在这里处理点击事件 */
                           mInnerListener.click(position,newCities[i])

                        }
                        if (chip.parent != chipGroup) {
                            chipGroup.addView(chip)
                        }
                    } else {
                        // 如果预创建的不够，仍然需要创建新的（这种情况应该尽量避免）
                        val newChip = createChip(holder.itemView.context, position,newCities[i])
                        chipGroup.addView(newChip)
                        holder.chipCache.add(newChip)
                    }
                }

                // 隐藏多余的 Chip 视图
                for (i in newCities.size until holder.chipCache.size) {
                    holder.chipCache[i].visibility = View.GONE
                }
            }
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int, item: GroupCityListBean?, payloads: List<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position)
        } else {
            val binding = holder.binding
            val chipGroup = binding.cityChipGroup
            val newCities = payloads.firstOrNull() as? List<CityBean>

            if (newCities != null) {
                chipGroup.setTag(com.knight.kotlin.library_widget.R.id.city_tag, newCities)

                for (i in newCities.indices) {
                    if (i < holder.chipCache.size) {
                        val chip = holder.chipCache[i]
                        chip.text = newCities[i].city
                        chip.visibility = View.VISIBLE
                        if (chip.parent != chipGroup) {
                            chipGroup.addView(chip)
                        }
                    } else {
                        val newChip = createChip(holder.itemView.context,position, newCities[i])
                        chipGroup.addView(newChip)
                        holder.chipCache.add(newChip)
                    }
                }

                for (i in newCities.size until holder.chipCache.size) {
                    holder.chipCache[i].visibility = View.GONE
                }
            }
        }
    }




    // 新增创建 Chip 的方法
    private fun createChip(context: Context,position:Int, city :CityBean): Chip {
        return Chip(ContextThemeWrapper(context, com.knight.kotlin.library_base.R.style.base_MyChipTheme), null, 0).apply {
            shapeAppearanceModel = ShapeAppearanceModel.Builder()
                .setAllCornerSizes(12f)  // 设置圆角
                .build()
            chipStartPadding = 10.dp2px().toFloat()
            chipEndPadding = 10.dp2px().toFloat()
            textStartPadding = 8.dp2px().toFloat()
            textEndPadding = 8.dp2px().toFloat()
            chipMinHeight = 48.dp2px().toFloat()
            setBackgroundColor(Color.parseColor("#EDEDED"))
            text = city.city // 设置文字内容
            val marginLayoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            marginLayoutParams.setMargins(10.dp2px(), 5.dp2px(), 10.dp2px(), 5.dp2px()) // 设置左、上、右、下边距
            layoutParams = marginLayoutParams
            setOnClickListener {
                // 在此处理点击事件
                mInnerListener.click(position,city)
            }
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent, mInnerListener = mInnerListener)
    }
}