package com.knight.kotlin.module_message.adapter

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.module_message.R
import com.knight.kotlin.module_message.entity.MessageEntity

/**
 * Author:Knight
 * Time:2023/5/16 17:17
 * Description:MessageAdapter
 */
class MessageAdapter(data:MutableList<MessageEntity>) :
    BaseQuickAdapter<MessageEntity, BaseViewHolder>(R.layout.message_readed_item,data) {
    override fun convert(holder: BaseViewHolder, item: MessageEntity) {
          item.run {
              //作者
              fromUser?.let {
                   holder.setText(R.id.message_item_author,it)
              } ?: run {
                   holder.setText(R.id.message_item_author,"it")
              }
              //Tag
              tag?.let {
                  holder.setText(R.id.message_item_tag,tag)
                  val gradientDrawable = GradientDrawable()
                  gradientDrawable.shape = GradientDrawable.RECTANGLE
                  gradientDrawable.setStroke(1,CacheUtils.getThemeColor())
                  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                      holder.getView<View>(R.id.message_item_tag).background = gradientDrawable
                  } else {
                      holder.getView<View>(R.id.message_item_tag).setBackgroundDrawable(gradientDrawable)
                  }
              } ?: run {
                    holder.setGone(R.id.message_item_tag,true)
              }

              //时间
              niceDate?.let {
                  holder.setText(R.id.message_item_nicedata,niceDate)
              } ?:run {
                  holder.setText(R.id.message_item_nicedata,"")
              }

              //标题
              title?.let {
                  holder.setText(R.id.message_tv_title,title)
              } ?: run {
                  holder.setText(R.id.message_tv_title,"")
              }

              //描述
              message?.let {
                  holder.setText(R.id.message_tv_desc,message)
              } ?:run {
                  holder.setText(R.id.message_tv_desc,"")
              }




          }
    }
}