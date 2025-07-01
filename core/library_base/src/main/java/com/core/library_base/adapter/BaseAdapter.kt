package com.core.library_base.adapter

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Author:Knight
 * Time:2024/3/5 15:12
 * Description:BaseAdapter
 */
abstract class BaseAdapter <VH : RecyclerView.ViewHolder, T> constructor(diffUtil: DiffUtil.ItemCallback<T>) : ListAdapter<T, VH>(diffUtil) {
    protected var mList = mutableListOf<T>()
    private var itemClickListener: OnItemClickListener?= null

    fun setOnClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    fun View.setOnItemClick(position: Int) {
        setOnClickListener {
            itemClickListener?.let {
                it.onItemClick(mList[position])
            }
        }
    }

    fun setList(list: MutableList<T>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    fun appendList(list:MutableList<T>) {
        mList.addAll(list)
        notifyDataSetChanged()
    }

    fun getDatas(): MutableList<T> {
        return mList
    }

    override fun getItemCount() = mList.size

    interface OnItemClickListener {
        fun <E> onItemClick(e: E)
    }
}