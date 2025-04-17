package com.knight.kotlin.library_widget.citypicker


import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseMultiItemAdapter
import com.knight.kotlin.library_base.ktx.getScreenWidth
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_widget.SpacesItemDecoration
import com.knight.kotlin.library_widget.databinding.CityDefaultListItemBinding
import com.knight.kotlin.library_widget.databinding.CityHotItemBinding
import com.knight.kotlin.library_widget.databinding.CityLocationItemBinding
import com.knight.kotlin.library_widget.ktx.init


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/17 14:00
 * @descript:城市列表适配器
 */
class CityListAdapter(val data:MutableList<CityListBean>):BaseMultiItemAdapter<CityListBean>(data) {



    lateinit var mInnerListener: InnerListener
    lateinit var mLayoutManager:LinearLayoutManager




    //当前城市
    class LocationCityVH(viewBinding:CityLocationItemBinding): RecyclerView.ViewHolder(viewBinding.root)
    //热门城市
    class HotListCityVH(viewBinding:CityHotItemBinding): RecyclerView.ViewHolder(viewBinding.root)
    //普通城市
    class DefaultListCityVh(viewBinding:CityDefaultListItemBinding): RecyclerView.ViewHolder(viewBinding.root)


    var stateChanged:Boolean = false

    /**
     *
     * 更新定位城市
     */
    fun updateLocateState(location: CityBean) {
        data.removeAt(0)
        val locationListData = CityListBean(CityEnum.LOCATION.type, mutableListOf(location))
        data.add(0, locationListData)
        stateChanged = true
        refreshLocationItem()
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
        if (TextUtils.equals(index.substring(0, 1),"定位")){
            mLayoutManager.scrollToPositionWithOffset(0, 0)
            return
        }

        if (TextUtils.equals(index,"热门")){
            mLayoutManager.scrollToPositionWithOffset(1, 0)
            return
        }

        val size: Int =  data.get(2).data.size
        for (i in 0 until size) {
            if (TextUtils.equals(index.substring(0, 1), data.get(2).data.get(i).city.substring(0, 1))) {
                mLayoutManager.scrollToPositionWithOffset(i+2, 0)
                return
            }


        }

//        for (i in 0 until size) {
//            if (TextUtils.equals(index.substring(0, 1), data.get(i)..substring(0, 1))) {
//                if (mLayoutManager != null) {
//                    mLayoutManager.scrollToPositionWithOffset(i, 0)
//                    if (TextUtils.equals(index.substring(0, 1), "当")) {
//                        //防止滚动时进行刷新
//                        Handler().postDelayed(Runnable { if (stateChanged) notifyItemChanged(0) }, 1000)
//                    }
//                    return
//                }
//            }
//        }
    }


    init {
        addItemType(CityEnum.LOCATION.type,object :OnMultiItemAdapterListener<CityListBean,LocationCityVH>{
            override fun onBind(holder: LocationCityVH, position: Int, item: CityListBean?) {
                item?.let {
                    val binding = CityLocationItemBinding.bind(holder.itemView)
                    val pos = holder.absoluteAdapterPosition
                    //设置宽高

                    val padding = context.resources.getDimensionPixelSize(com.knight.kotlin.library_widget.R.dimen.widget_city_default_padding)
                    val indexBarWidth = context.resources.getDimensionPixelSize(com.knight.kotlin.library_widget.R.dimen.widget_city_index_bar_width)
                    val itemWidth: Int = (getScreenWidth() - padding - 10.dp2px() * (3 - 1) - indexBarWidth) / 3
                    val lp: ViewGroup.LayoutParams = binding.listItemLocationLayout.getLayoutParams()
                    lp.width = itemWidth
                    lp.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    binding.listItemLocationLayout.setLayoutParams(lp)

                }


            }

            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): LocationCityVH {
                // 创建 viewholder
                val viewBinding = CityLocationItemBinding.inflate(LayoutInflater.from(context), parent, false)
                return LocationCityVH(viewBinding)
            }
        })


        addItemType(CityEnum.HOT.type,object:OnMultiItemAdapterListener<CityListBean,HotListCityVH>{
            override fun onBind(holder: HotListCityVH, position: Int, item: CityListBean?) {
                item?.run {
                    val binding = CityHotItemBinding.bind(holder.itemView)
                    val cityGridAdapter = CityGridAdapter(mInnerListener)
                    // 绑定 item 数据

                    //binding.cityHotList.setHasFixedSize(true)
                    binding.cityHotList.init(
                        GridLayoutManager(context,3, LinearLayoutManager.VERTICAL, false),
                        cityGridAdapter,
                        true
                    )
                    binding.cityHotList.addItemDecoration(
                        SpacesItemDecoration(10)
                    )
                    cityGridAdapter.submitList(item.data)
                }

            }

            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): HotListCityVH {
                // 创建 viewholder
                val viewBinding = CityHotItemBinding.inflate(LayoutInflater.from(context), parent, false)
                return HotListCityVH(viewBinding)
            }

        })

        addItemType(CityEnum.NORMAL.type,object :OnMultiItemAdapterListener<CityListBean,DefaultListCityVh>{
            override fun onBind(holder: DefaultListCityVh, position: Int, item: CityListBean?) {
                item?.let {
                    val binding = CityDefaultListItemBinding.bind(holder.itemView)
                    val mDefaultCityAdapter = CityDefaultAdapter(mInnerListener)
                    binding.cityDefaultList.init(
                        LinearLayoutManager(context),
                        mDefaultCityAdapter,
                        true
                    )
                    mDefaultCityAdapter.submitList(item.data)
                 //   binding.cityDefaultList.addItemDecoration(SectionItemDecoration(context, item.data), 0)

                }
            }

            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): DefaultListCityVh {
                // 创建 viewholder
                val viewBinding = CityDefaultListItemBinding.inflate(LayoutInflater.from(context), parent, false)
                return DefaultListCityVh(viewBinding)
            }
        }).onItemViewType { position, list -> // 根据数据，返回对应的 ItemViewType
            list[position].type
        }

    }


}