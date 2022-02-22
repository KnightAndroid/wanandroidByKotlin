package com.knight.kotlin.module_home.adapter

import android.os.Build
import android.text.Html
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.module_home.R
import com.knight.kotlin.module_home.entity.OpenSourceBean

/**
 * Author:Knight
 * Time:2022/2/16 11:40
 * Description:OpenSourceAdapter
 */
class OpenSourceAdapter(data:MutableList<OpenSourceBean>): BaseQuickAdapter<OpenSourceBean, BaseViewHolder>(R.layout.home_opensource_item,data) {
    override fun convert(holder: BaseViewHolder, item: OpenSourceBean) {
        //赋值
        item.run {
            holder.setText(R.id.home_opensource_title,name)
            holder.setText(R.id.home_opensource_desc,desc)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                (holder.getView<View>(R.id.home_opensource_abroadlink) as TextView).text = Html.fromHtml("<u>$abroadlink</u>", Html.FROM_HTML_MODE_LEGACY)
            } else {
                (holder.getView<View>(R.id.home_opensource_abroadlink) as TextView).text = Html.fromHtml("<u>$abroadlink</u>")
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                (holder.getView<View>(R.id.home_opensource_internallink) as TextView).text = Html.fromHtml("<u>$internallink</u>", Html.FROM_HTML_MODE_LEGACY)
            } else {
                (holder.getView<View>(R.id.home_opensource_internallink) as TextView).text = Html.fromHtml("<u>$internallink</u>")
            }

            holder.setTextColor(R.id.home_opensource_title,CacheUtils.getThemeColor())
            holder.setTextColor(R.id.home_opensource_abroadlink,CacheUtils.getThemeColor())
            holder.setTextColor(R.id.home_opensource_internallink,CacheUtils.getThemeColor())
        }
    }
}