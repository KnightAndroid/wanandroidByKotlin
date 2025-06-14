package com.knight.kotlin.module_navigate.listener

/**
 * Author:Knight
 * Time:2022/5/5 16:01
 * Description:RvListener
 */
//RecyclerView的item点击事件
open interface RvListener {
    fun onItemClick(id: Int, position: Int)
}