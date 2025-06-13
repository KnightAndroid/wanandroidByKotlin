package com.knight.kotlin.module_message.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.module_message.R
import com.knight.kotlin.module_message.databinding.MessageReadedItemBinding
import com.knight.kotlin.module_message.entity.MessageEntity

/**
 * Author:Knight
 * Time:2023/5/16 17:17
 * Description:MessageAdapter
 */
class MessageAdapter :
    BaseQuickAdapter<MessageEntity, MessageAdapter.VH>() {


    class VH(
        parent: ViewGroup,
        val binding: MessageReadedItemBinding = MessageReadedItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: MessageEntity?) {
        item?.run {

            fromUser?.let {
                holder.binding.messageItemAuthor.setText(it)
            } ?: run {
                holder.binding.messageItemAuthor.setText("it")
            }

            tag?.let {
                holder.binding.messageItemAuthor.setText(tag)
                val gradientDrawable = GradientDrawable()
                gradientDrawable.shape = GradientDrawable.RECTANGLE
                gradientDrawable.setStroke(1,CacheUtils.getThemeColor())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.binding.messageItemTag.background = gradientDrawable
                } else {
                    holder.binding.messageItemTag.setBackgroundDrawable(gradientDrawable)
                }
            } ?: run {
                holder.binding.messageItemTag.visibility = View.GONE
            }


            //时间
            niceDate?.let {
                holder.binding.messageItemNicedata.setText(niceDate)
            } ?:run {
                holder.binding.messageItemNicedata.setText("")
            }

            //标题
            title?.let {
                holder.binding.messageTvTitle.setText(title)
            } ?: run {
                holder.binding.messageTvTitle.setText("")
            }

            //描述
            message?.let {
                holder.binding.messageTvDesc.setText(message)
            } ?:run {
                holder.binding.messageTvDesc.setText("")
            }
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }


}