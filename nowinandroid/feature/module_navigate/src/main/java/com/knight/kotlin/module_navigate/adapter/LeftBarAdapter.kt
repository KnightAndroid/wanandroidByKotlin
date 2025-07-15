package com.knight.kotlin.module_navigate.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.module_navigate.R
import com.knight.kotlin.module_navigate.holder.RvHolder
import com.knight.kotlin.module_navigate.listener.RvListener

/**
 * Author:Knight
 * Time:2022/5/6 10:37
 * Description:LeftBarAdapter
 */
class LeftBarAdapter : RvAdapter<String> {

    private var checkedPosition = 0
    @JvmOverloads
    constructor(context: Context, list: List<String>, listener: RvListener)
            : super(context, list, listener) {

    }

    fun setCheckedPosition(checkedPosition: Int) {
        this.checkedPosition = checkedPosition
        notifyDataSetChanged()
    }

    override fun getLayoutId(viewType: Int) = R.layout.navigate_left_sidebar

    override fun getHolder(view: View, viewType: Int): RvHolder<String> {
        return SortHolder(view,mListener)
    }

    inner class SortHolder : RvHolder<String> {

        private var tvName: TextView
        private var navigate_tv_selectview: TextView
        private var mView: View = itemView

        @JvmOverloads
        constructor(itemView: View, listener: RvListener)
                : super(itemView, listener) {
            tvName = itemView.findViewById(R.id.navigate_tv_sort)
            navigate_tv_selectview = itemView.findViewById(R.id.navigate_tv_selectview)
        }

        override fun bindHolder(t: String, position: Int) {
            tvName.text = t
            if (position == checkedPosition) {
                navigate_tv_selectview.visibility = View.VISIBLE
                navigate_tv_selectview.setBackgroundColor(CacheUtils.getThemeColor())
                tvName.setTextColor(CacheUtils.getThemeColor())
                if (CacheUtils.getNormalDark()) {
                    mView.setBackgroundColor(Color.parseColor("#303030"))
                } else {
                    mView.setBackgroundColor(Color.parseColor("#f3f3f3"))
                }

            } else {
                tvName.setTextColor(ContextCompat.getColor(mContext, R.color.navigate_tv_sort_color))
                if (CacheUtils.getNormalDark()) {
                    mView.setBackgroundColor(Color.parseColor("#303030"))
                } else {
                    mView.setBackgroundColor(Color.parseColor("#FFFFFF"))
                }
                navigate_tv_selectview.visibility = View.INVISIBLE

            }
        }
    }

}