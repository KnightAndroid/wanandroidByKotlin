package com.knight.kotlin.library_widget.ktx

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_widget.pagetransformer.CardPagerTransfromer
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import java.util.WeakHashMap

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
//var adapterLastClickTime = 0L
//fun BaseQuickAdapter<*, *>.setFastOnItemClickListener(interval:Long = 1000, action:(adapter:BaseQuickAdapter<*,*>, view: View, position:Int) -> Unit) {
//    setOnItemClickListener { adapter, view, position ->
//        val currentTime = System.currentTimeMillis()
//        if (adapterLastClickTime != 0L && (currentTime - adapterLastClickTime < interval)) {
//            return@setOnItemClickListener
//        }
//        adapterLastClickTime = currentTime
//        action(adapter, view, position)
//    }
//}

//var adapterLastClickTime = 0L
//fun <T : Any> BaseQuickAdapter<T, *>.setFastOnItemClickListener(interval: Long = 1000, action: (adapter: BaseQuickAdapter<T, *>, view: View, position: Int) -> Unit) {
//    setOnItemClickListener(object : BaseQuickAdapter.OnItemClickListener<T> {
//        override fun onClick(adapter: BaseQuickAdapter<T, *>, view: View, position: Int) {
//            val currentTime = System.currentTimeMillis()
//        if (adapterLastClickTime != 0L && (currentTime - adapterLastClickTime < interval)) {
//            return
//        }
//        adapterLastClickTime = currentTime
//            action(adapter, view, position)
//        }
//    })
//}


private val adapterLastClickTimes = WeakHashMap<BaseQuickAdapter<*, *>, Long>()
fun <T : Any> BaseQuickAdapter<T, *>.setSafeOnItemClickListener(
    interval: Long = 1000,
    action: (adapter: BaseQuickAdapter<T, *>, view: View, position: Int) -> Unit
) {
    setOnItemClickListener { adapter, view, position ->
        val currentTime = System.currentTimeMillis()
        val lastClickTime = adapterLastClickTimes.getOrPut(adapter) { 0L }

        if (currentTime - lastClickTime < interval) {
            return@setOnItemClickListener
        }

        adapterLastClickTimes[adapter] = currentTime
        action(adapter, view, position)
    }
}


//扩展adapter 点击子child防止短时间内重复点击
//var adapterChildLastClickTime = 0L
private val adapterLastChildClickTimes = WeakHashMap<BaseQuickAdapter<*, *>, Long>()
fun <T : Any> BaseQuickAdapter<T, *>.setSafeOnItemChildClickListener(vararg viewIds: Int,interval: Long = 1000, action: (adapter: BaseQuickAdapter<T, *>, view: View, position: Int) -> Unit) {
    viewIds.forEach { viewId ->
        addOnItemChildClickListener(viewId) { adapter, view, position ->
            val currentTime = System.currentTimeMillis()
            val lastClickTime = adapterLastChildClickTimes.getOrPut(adapter) { 0L }

            if (currentTime - lastClickTime < interval) {
                return@addOnItemChildClickListener
            }

            adapterLastChildClickTimes[adapter] = currentTime
            action(adapter, view, position)
        }
    }

}

/**
 * 重新设置ViewPager2高度，解决ViewPager2内View不同高度问题。
 * 划到当前Pager，设置ViewPager2高度为当前Pager高度
 * @param viewPager2 ViewPager2
 * @param position Int 当前Pager
 * @param minHeight Int? ViewPager2 的最小高度
 */
fun setViewPager2Height(viewPager2: ViewPager2, position: Int, minHeight: Int? = null) {
    val recyclerView: RecyclerView = viewPager2.getChildAt(0) as RecyclerView
    val layoutManager: RecyclerView.LayoutManager? = recyclerView.layoutManager
    if (layoutManager != null) {
        // 查找当前页面的 View
        val view: View? = layoutManager.findViewByPosition(position)
        if (view != null) {
            // 测量 View 的高度
            view.measure(
                View.MeasureSpec.makeMeasureSpec(viewPager2.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            val measuredHeight = view.measuredHeight
            // 设置 ViewPager2 的高度
            val params = viewPager2.layoutParams
            params.height = minHeight?.coerceAtLeast(measuredHeight) ?: measuredHeight
            viewPager2.layoutParams = params
        }
    }
}
