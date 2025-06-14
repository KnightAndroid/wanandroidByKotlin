package com.knight.kotlin.module_navigate.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.knight.kotlin.module_navigate.listener.RvListener

/**
 * Author:Knight
 * Time:2022/5/5 15:59
 * Description:RvHolder
 */
abstract class RvHolder<T>(itemView: View,listener:RvListener) : RecyclerView.ViewHolder(itemView) {

    protected val mListener = listener

    init {
        itemView.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                mListener.onItemClick(v?.id ?: 0,adapterPosition)
            }
        })

    }

    abstract fun bindHolder(t: T, position: Int)
}