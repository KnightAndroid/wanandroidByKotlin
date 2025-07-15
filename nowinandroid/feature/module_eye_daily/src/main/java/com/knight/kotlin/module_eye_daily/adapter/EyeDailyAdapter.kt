package com.knight.kotlin.module_eye_daily.adapter


import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseMultiItemAdapter
import com.core.library_base.ktx.setOnClick
import com.core.library_base.ktx.toJson
import com.core.library_base.route.RouteActivity
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.library_common.config.Appconfig
import com.knight.kotlin.library_base.config.EyeTypeConstants
import com.knight.kotlin.library_base.entity.EyeDailyItemEntity
import com.knight.kotlin.library_base.entity.EyeVideoDetailEntity
import com.knight.kotlin.library_util.startPageWithAnimate
import com.knight.kotlin.module_eye_daily.R
import com.knight.kotlin.module_eye_daily.databinding.EyeDailyImageItemBinding
import com.knight.kotlin.module_eye_daily.databinding.EyeDailyTextItemBinding

/**
 * Author:Knight
 * Time:2024/5/8 10:06
 * Description:EyeDailyAdapter
 */
class EyeDailyAdapter(data: List<EyeDailyItemEntity>, activity: Activity):
    BaseMultiItemAdapter<EyeDailyItemEntity>(data){


    // 类型 1 的 viewholder
    class EyeDailyTextItemVH(val viewBinding: EyeDailyTextItemBinding) : RecyclerView.ViewHolder(viewBinding.root)

    // 类型 2 的 viewholder
    class EyeDailyImageItemVH(val viewBinding: EyeDailyImageItemBinding) : RecyclerView.ViewHolder(viewBinding.root)

    // 在 init 初始化的时候，添加多类型
    init {
        addItemType(EyeTypeConstants.TEXT_TYPE, object : OnMultiItemAdapterListener<EyeDailyItemEntity, EyeDailyTextItemVH> { // 类型 1
            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): EyeDailyTextItemVH {
                // 创建 viewholder
                val viewBinding = EyeDailyTextItemBinding.inflate(LayoutInflater.from(context), parent, false)
                return EyeDailyTextItemVH(viewBinding)
            }

            override fun onBind(holder: EyeDailyTextItemVH, position: Int, item: EyeDailyItemEntity?) {
                // 绑定 item 数据
                val binding = DataBindingUtil.getBinding<EyeDailyTextItemBinding>(holder.itemView)
                binding?.model = item?.data
                binding?.imageLocal = R.drawable.eye_daily_title_icon
                binding?.colorStateList = ColorStateList.valueOf(CacheUtils.getThemeColor())
            }
        }).addItemType(EyeTypeConstants.IMAGE_TYPE, object : OnMultiItemAdapterListener<EyeDailyItemEntity, EyeDailyImageItemVH> { // 类型 2
            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): EyeDailyImageItemVH {
                // 创建 viewholder
                val viewBinding = EyeDailyImageItemBinding.inflate(LayoutInflater.from(context), parent, false)
                return EyeDailyImageItemVH(viewBinding)
            }

            override fun onBind(holder: EyeDailyImageItemVH, position: Int, item: EyeDailyItemEntity?) {
                // 绑定 item 数据
                val binding = DataBindingUtil.getBinding<EyeDailyImageItemBinding>(holder.itemView)
                binding?.model = item?.data?.content
                binding?.ivDailyCover?.setOnClick{
                    val videoDetailData = EyeVideoDetailEntity(
                        item?.data?.content!!.data.id,
                        item?.data?.content!!.data.title,
                        item?.data?.content!!.data.playUrl,
                        item?.data?.content!!.data.category,

                        item?.data?.content!!.data.author?.latestReleaseTime ?: System.currentTimeMillis(),
                        item?.data?.content!!.data.description,
                        item?.data?.content!!.data.consumption.collectionCount,
                        item?.data?.content!!.data.consumption.replyCount,
                        item?.data?.content!!.data.consumption.shareCount,
                        item?.data?.content!!.data.author?.icon ?: "",
                        item?.data?.content!!.data.author?.name ?: "",
                        item?.data?.content!!.data.author?.description ?: "",
                        item?.data?.content!!.data.cover?.blurred ?: ""
                    )
                    startPageWithAnimate(
                        activity,
                        RouteActivity.EyeVideo.EyeVideoDetail, binding.root,
                        activity.getString(com.core.library_base.R.string.base_daily_share_image),
                        Appconfig.EYE_VIDEO_PARAM_KEY to toJson(videoDetailData)
                    )
                }
            }

            override fun isFullSpanItem(itemType: Int): Boolean {
                // 使用GridLayoutManager时，此类型的 item 是否是满跨度
                return true
            }

        }).onItemViewType { position, list -> // 根据数据，返回对应的 ItemViewType
            var index = position
            if (position == list.size) {
                index = index - 1
            }
            if (list[index].type == EyeTypeConstants.TEXT_HEAD_TYPE) {
                EyeTypeConstants.TEXT_TYPE
            } else EyeTypeConstants.IMAGE_TYPE

        }
    }
}