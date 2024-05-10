package com.knight.kotlin.module_eye_daily.adapter


import android.content.res.ColorStateList
import android.widget.ImageView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_util.DateUtils
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
                       data.content.data?.run {
                           //加载封面
                           cover?.feed?.let {
                               ImageLoader.loadStringPhoto(
                                   context,
                                   it, holder.getView(R.id.iv_daily_cover)
                               )
                           }

                           //加载栏目
                           holder.setText(R.id.tv_daily_category,category)
                           //加载时间
                           holder.setText(R.id.tv_daily_item_video_time,DateUtils.formatDateMsByMS(duration * 1000))

                           //加载头像
                           author?.run {
                               ImageLoader.loadStringPhoto(context,icon,holder.getView(R.id.eye_iv_daily_author))
                               holder.setText(R.id.tv_desc_name,name)
                           } ?:run {
                               ImageLoader.loadStringPhoto(context,tags[0].headerImage,holder.getView(R.id.eye_iv_daily_author))
                               holder.setText(R.id.tv_desc_name,tags[0].name)
                           }
                           //加载标题
                           holder.setText(R.id.tv_daily_title,title)
                       }
                   }

                   else -> {}
               }


           }
    }


}