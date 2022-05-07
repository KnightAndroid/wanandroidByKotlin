package com.knight.kotlin.module_navigate.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.knight.kotlin.module_navigate.holder.RvHolder
import com.knight.kotlin.module_navigate.listener.RvListener

/**
 * Author:Knight
 * Time:2022/5/5 15:58
 * Description:RvAdapter
 */
abstract class RvAdapter<T> constructor(context: Context, list: List<T>, listener: RvListener):RecyclerView.Adapter<RvHolder<T>>() {
    protected var mlist: List<T> = list
    protected var mContext: Context = context
    protected var mListener: RvListener = listener
    protected var mInflater: LayoutInflater = LayoutInflater.from(context)




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvHolder<T> {
        val view = mInflater.inflate(getLayoutId(viewType), parent, false)
        return getHolder(view, viewType)
    }

    protected abstract fun getLayoutId(viewType: Int): Int

    override fun onBindViewHolder(holder: RvHolder<T>, position: Int) {
        holder.bindHolder(mlist[position], position)
    }

    override fun getItemCount(): Int {
        return mlist.size
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    protected abstract fun getHolder(view: View, viewType: Int): RvHolder<T>
}