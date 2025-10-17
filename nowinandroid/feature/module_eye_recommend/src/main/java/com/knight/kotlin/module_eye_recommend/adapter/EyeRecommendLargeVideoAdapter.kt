package com.knight.kotlin.module_eye_recommend.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.knight.kotlin.library_util.startPageWithAnimate
import com.knight.kotlin.module_eye_recommend.databinding.EyeRecommendLargeVideoItemBinding
import com.knight.kotlin.module_eye_recommend.entity.EyeRecommendVideoEntity


/**
 * @author created by luguian
 * @organize
 * @Date 2025/10/13 16:56
 * @descript: 开眼大视频适配器
 */
class EyeRecommendLargeVideoAdapter(private val activity: Activity): BaseQuickAdapter<EyeRecommendVideoEntity,EyeRecommendLargeVideoAdapter.VH>() {
    override fun onBindViewHolder(
        holder: VH,
        position: Int,
        item: EyeRecommendVideoEntity?
    ) {
        item?.run {
            holder.binding.viewModel = this
            holder.binding.tvEyeRecommendCategory.text =
                this.tags.takeIf { it.isNotEmpty() }?.joinToString(" ") { it.title } ?: ""

            holder.binding.ivEyeRecommendCover.setOnClick {
                startPageWithAnimate(
                    activity,
                    RouteActivity.EyeVideo.EyeVideoDetail, holder.binding.root,
                    activity.getString(com.core.library_base.R.string.base_daily_share_image),
                    "video_id" to video_id.toLong()
                )
            }




        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): VH {
        return VH(parent)
    }

    class VH(
        parent: ViewGroup,
        val binding: EyeRecommendLargeVideoItemBinding = EyeRecommendLargeVideoItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)
}