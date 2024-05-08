package com.knight.kotlin.module_eye_daily.adapter


import android.content.res.ColorStateList
import android.widget.ImageView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.module_eye_daily.R
import com.knight.kotlin.module_eye_daily.constants.EyeDailyConstants
import com.knight.kotlin.module_eye_daily.entity.EyeDailyItemEntity

/**
 * Author:Knight
 * Time:2024/5/8 10:06
 * Description:EyeDailyAdapter
 */
class EyeDailyAdapter(data : MutableList<EyeDailyItemEntity>):
    BaseMultiItemQuickAdapter<EyeDailyItemEntity, BaseViewHolder>(){

     init {
         addItemType(EyeDailyConstants.TEXT_TYPE,R.layout.eye_daily_text_item)
         addItemType(EyeDailyConstants.IMAGE_TYPE,R.layout.eye_daily_image_item)
     }




       override fun convert(holder: BaseViewHolder, item: EyeDailyItemEntity) {
           item.run {
               when(holder.itemViewType) {
                   EyeDailyConstants.TEXT_TYPE -> {
                          holder.setText(R.id.tv_eye_daily_title,data.text)
                          val iv = holder.getView<ImageView>(R.id.iv_eye_daily)
                          iv.setImageResource(R.drawable.eye_daily_title_icon)
                          iv.imageTintList = ColorStateList.valueOf(CacheUtils.getThemeColor())
                   }

                   EyeDailyConstants.IMAGE_TYPE -> {
                       data.content.data.cover?.feed?.let {
                           ImageLoader.loadStringPhoto(context,
                               it,holder.getView(R.id.iv_daily_cover))
                       }
                   }

                   else -> {}
               }


           }
    }


}