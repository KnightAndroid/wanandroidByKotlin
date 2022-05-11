package com.knight.kotlin.module_mine.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.module_mine.R
import com.knight.kotlin.module_mine.entity.MyDetailCoinEntity

/**
 * Author:Knight
 * Time:2022/5/11 10:20
 * Description:MyCointsAdapter
 */
class MyCointsAdapter(data:MutableList<MyDetailCoinEntity>):
    BaseQuickAdapter<MyDetailCoinEntity, BaseViewHolder>(R.layout.mine_coindetail_item,data)  {
    override fun convert(holder: BaseViewHolder, item: MyDetailCoinEntity) {
        item.run {
            holder.setText(R.id.mine_tv_detailpointtitle,reason)
            holder.setText(R.id.mine_tv_coincount, "+$coinCount")
            holder.setTextColor(R.id.mine_tv_coincount,CacheUtils.getThemeColor())
            holder.setText(R.id.mine_tv_timereason,desc)
        }
    }
}