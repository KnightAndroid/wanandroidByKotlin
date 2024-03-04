package com.knight.kotlin.module_vedio.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.module_vedio.entity.VedioListEntity

/**
 * Author:Knight
 * Time:2024/2/26 14:43
 * Description:VedioMainAdapter
 */
class VedioMainAdapter(data:MutableList<VedioListEntity>) : BaseQuickAdapter<VedioListEntity,BaseViewHolder>(
    com.knight.kotlin.module_vedio.R.layout.vedio_main_item,data) {
    override fun convert(holder: BaseViewHolder, item: VedioListEntity) {
        item.run {
            ImageLoader.loadVedioFirstFrame(context,item.joke.videoUrl,holder.getView(
                com.knight.kotlin.module_vedio.R.id.iv_vedio),10)

        }

    }


}