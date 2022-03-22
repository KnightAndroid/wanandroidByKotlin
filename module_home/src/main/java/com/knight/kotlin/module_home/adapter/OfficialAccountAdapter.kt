package com.knight.kotlin.module_home.adapter

import android.graphics.drawable.GradientDrawable
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_base.util.ColorUtils
import com.knight.kotlin.module_home.R
import com.knight.kotlin.library_common.entity.OfficialAccountEntity

/**
 * Author:Knight
 * Time:2022/3/11 16:32
 * Description:OfficialAccountAdapter
 */
class OfficialAccountAdapter(data:MutableList<OfficialAccountEntity>) : BaseQuickAdapter<OfficialAccountEntity,BaseViewHolder>(
    R.layout.home_official_accounts_item,data) {
    override fun convert(holder: BaseViewHolder, item: OfficialAccountEntity) {
       item.run {
           val gradientDrawable = GradientDrawable()
           gradientDrawable.shape = GradientDrawable.OVAL
           gradientDrawable.setColor(ColorUtils.getRandColorCode())
           (holder.getView(R.id.home_iv_official_account) as ImageView).background = gradientDrawable
           holder.setText(R.id.home_tv_officialaccount_name,name)
           holder.setText(R.id.tv_head_username,name.substring(0,1))
       }
    }
}