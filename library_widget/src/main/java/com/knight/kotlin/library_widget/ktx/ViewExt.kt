package com.knight.kotlin.library_widget.ktx

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_widget.pagetransformer.CardPagerTransfromer
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


fun ViewPager2.init(
    mCompositePageTransformer: CompositePageTransformer,
    multiWidth:Int,
    pageMargin:Int
):CompositePageTransformer {
    setPageTransformer(mCompositePageTransformer)
    mCompositePageTransformer.addTransformer(MarginPageTransformer(pageMargin))
    val recyclerView = getChildAt(0) as RecyclerView
    if (orientation == ViewPager2.ORIENTATION_VERTICAL) {
        recyclerView.setPadding(
            paddingLeft,
            multiWidth + Math.abs(pageMargin),
            paddingRight,
            multiWidth + Math.abs(pageMargin)
        )
    } else {
        recyclerView.setPadding(
            multiWidth + Math.abs(pageMargin),
            paddingTop,
            multiWidth + Math.abs(pageMargin),
            paddingBottom
        )
    }
    recyclerView.clipToPadding = false
    mCompositePageTransformer.addTransformer(CardPagerTransfromer(0.85f))
    return mCompositePageTransformer
}


//扩展adapter 点击本身防止短时间内重复点击Item
var adapterLastClickTime = 0L
fun BaseQuickAdapter<*, *>.setOnItemClickListener(interval:Long = 1000, action:(adapter:BaseQuickAdapter<*,*>, view: View, position:Int) -> Unit) {
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
fun BaseQuickAdapter<*, *>.addOnItemChildClickListener(interval: Long = 1000, action: (adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Unit) {
    addOnItemChildClickListener{ adapter, view, position ->
        val currentTime = System.currentTimeMillis()
        if (adapterChildLastClickTime != 0L && (currentTime - adapterChildLastClickTime < interval)) {
            return@addOnItemChildClickListener
        }
        adapterChildLastClickTime = currentTime
        action(adapter, view, position)
    }
}
