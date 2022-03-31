package com.knight.kotlin.library_widget.skeleton

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.knight.kotlin.library_widget.R

/**
 * Author:Knight
 * Time:2022/3/31 15:48
 * Description:ShimmerViewHolder
 */
class ShimmerViewHolder(inflater: LayoutInflater, parent: ViewGroup, innerViewResId: Int) :
    RecyclerView.ViewHolder(
        inflater.inflate(
            R.layout.layout_shimmer, parent, false
        )
    ) {
    init {
        val layout = itemView as ViewGroup
        val view = inflater.inflate(innerViewResId, layout, false)
        val lp = view.layoutParams
        if (lp != null) {
            layout.layoutParams = lp
        }
        layout.addView(view)
    }

}