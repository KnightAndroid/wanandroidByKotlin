package com.knight.kotlin.module_eye_square.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseMultiItemAdapter
import com.knight.kotlin.library_base.config.EyeCardUIType
import com.knight.kotlin.library_base.entity.EyeCardEntity
import com.knight.kotlin.library_base.entity.eye_type.EyeBannerImageList
import com.knight.kotlin.library_base.entity.eye_type.EyeBannerSquare
import com.knight.kotlin.module_eye_square.databinding.EyeSquareBannerItemBinding
import com.knight.kotlin.module_eye_square.databinding.EyeSquareTestItemBinding
import com.youth.banner.indicator.CircleIndicator
import kotlinx.serialization.json.Json


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/11 15:40
 * @descript:广场适配器
 */
class EyeSquareAdapter(activity : FragmentActivity,data :List<EyeCardEntity>): BaseMultiItemAdapter<EyeCardEntity>(data) {

    //类型1 广告
    class EyeSquareBannerVH(viewBinding:EyeSquareBannerItemBinding): RecyclerView.ViewHolder(viewBinding.root)
    //类型2 测试
    class EyeSquareTextVH(viewBinding: EyeSquareTestItemBinding): RecyclerView.ViewHolder(viewBinding.root)
    init {
        addItemType(EyeCardUIType.BANNER, object : OnMultiItemAdapterListener<EyeCardEntity, EyeSquareBannerVH> {
            override fun onBind(holder: EyeSquareBannerVH, position: Int, item: EyeCardEntity?) {
                // 绑定 item 数据
                val binding = DataBindingUtil.getBinding<EyeSquareBannerItemBinding>(holder.itemView)

                val banner: List<EyeBannerImageList>? = item?.card_data?.body?.metro_list?.map { eyeMetroCard ->
                    // 将JsonObject转换为JSON字符串
                    val jsonString = eyeMetroCard.metro_data.toString()
                    // 使用Kotlinx.serialization从字符串转换为EyeMetroCardEntity
                    Json.decodeFromString<EyeBannerImageList>(jsonString)
                }
                val bannerList = EyeBannerSquare(banner)
                binding?.viewModel = bannerList
                binding?.discoverTopBanner?.addBannerLifecycleObserver(activity)?.indicator = CircleIndicator(activity)
            }

            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): EyeSquareBannerVH {
                //创建 viewholder
                val viewBinding = EyeSquareBannerItemBinding.inflate(LayoutInflater.from(context), parent, false)
                return EyeSquareBannerVH(viewBinding)
            }
        }).addItemType(EyeCardUIType.PIC, object : OnMultiItemAdapterListener<EyeCardEntity, EyeSquareTextVH> {
            override fun onBind(holder: EyeSquareTextVH, position: Int, item: EyeCardEntity?) {
                // 绑定 item 数据
//                val binding = DataBindingUtil.getBinding<EyeSquareBannerItemBinding>(holder.itemView)
//
//                val banner : List<EyeBannerImageList>? = item?.card_data?.body?.metro_list?.map { eyeMetroCard ->
//                    // 将JsonObject转换为JSON字符串
//                    val jsonString = eyeMetroCard.metro_data.toString()
//                    // 使用Kotlinx.serialization从字符串转换为EyeMetroCardEntity
//                    Json.decodeFromString<EyeBannerImageList>(jsonString)
//                }
//                val bannerList = EyeBannerSquare(banner)
//                binding?.viewModel = bannerList
//                binding?.discoverTopBanner?.addBannerLifecycleObserver(activity)?.indicator = CircleIndicator(activity)
            }

            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): EyeSquareTextVH {
                //创建 viewholder
                val viewBinding = EyeSquareTestItemBinding.inflate(LayoutInflater.from(context), parent, false)
                return EyeSquareTextVH(viewBinding)
            }
        }).onItemViewType { position, list -> // 根据数据，返回对应的 ItemViewType
            if (list[position].type == "set_banner_list") {
                EyeCardUIType.BANNER
            } else {
                EyeCardUIType.PIC
            }
        }
    }


    fun convertViewType2SpanSize(
        position: Int,
        spanCount: Int
    ): Int {
        if (position >= itemCount) return spanCount
        return when (getItemViewType(position)) {
            EyeCardUIType.BANNER -> spanCount
            else -> 1
        }
    }


}