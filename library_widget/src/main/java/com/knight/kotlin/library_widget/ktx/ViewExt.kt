package com.knight.kotlin.library_widget.ktx

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.yanzhenjie.recyclerview.SwipeRecyclerView

/**
 * Author:Knight
 * Time:2022/2/16 15:55
 * Description:ViewExt
 */


//绑定SwipeRecycleview
fun SwipeRecyclerView.init(
    layoutManager: RecyclerView.LayoutManager,
    bindAdapter:RecyclerView.Adapter<*>,
    isScroll:Boolean = true
):SwipeRecyclerView {
    setLayoutManager(layoutManager)
    setHasFixedSize(true)
    adapter = bindAdapter
    isNestedScrollingEnabled = isScroll
    return this
}


//扩展adapter 点击本身防止短时间内重复点击Item
var adapterLastClickTime = 0L
fun BaseQuickAdapter<*, *>.setItemClickListener(interval:Long = 1000,action:(adapter:BaseQuickAdapter<*,*>, view: View, position:Int) -> Unit) {
    setOnItemClickListener { adapter, view, position ->
        val currentTime = System.currentTimeMillis()
        if (adapterLastClickTime != 0L && (currentTime - adapterLastClickTime < interval)) {
            return@setOnItemClickListener
        }
        adapterLastClickTime = currentTime
        action(adapter, view, position)
    }
}

//扩展adapter 点击子child防止短时间内重复点击
var adapterChildLastClickTime = 0L
fun BaseQuickAdapter<*,*>.setItemChildClickListener(interval: Long = 1000, action: (adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Unit) {
    setOnItemChildClickListener { adapter, view, position ->
        val currentTime = System.currentTimeMillis()
        if (adapterChildLastClickTime != 0L && (currentTime - adapterChildLastClickTime < interval)) {
            return@setOnItemChildClickListener
        }
        adapterChildLastClickTime = currentTime
        action(adapter, view, position)
    }
}

