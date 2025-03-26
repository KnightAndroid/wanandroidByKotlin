package com.knight.kotlin.module_realtime.ktx

import android.view.View
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_base.entity.BaiduContent
import com.knight.kotlin.library_base.util.ArouteUtils
import com.yanzhenjie.recyclerview.SwipeRecyclerView


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/26 14:12
 * @descript:处理热搜通用点击事件
 */

//fun <T : BaiduContent, VH : RecyclerView.ViewHolder> handleAdapterClick(vararg adapters: BaseQuickAdapter<T, VH>) {
//    adapters.forEach { adapter ->
//        adapter.setOnItemClickListener { adapter, view, position ->
//            ArouteUtils.startWebArticle(
//                adapter.items[position].appUrl,
//                adapter.items[position].word,
//                adapter.items[position].index,
//                false
//            )
//        }
//    }
//}

fun <T : Any> handleAdapterClick(vararg adapters: HotAdapterInterface<T>) {
    adapters.forEach { adapter ->
        adapter.setOnItemClickListener { adapter, view, position ->
            var clickPosition: Int
            if ((adapter.recyclerView is SwipeRecyclerView)) {
                if ((adapter.recyclerView as SwipeRecyclerView).headerCount == 1) {
                    clickPosition = position - 1
                } else {
                    clickPosition = position
                }
            } else {
                clickPosition = position
            }
            if (adapter.items.isNotEmpty() && (clickPosition < adapter.items.size)) {
                val item = adapter.items[clickPosition]
                if (item is BaiduContent) { // 或者使用 adapter.items[position] as T
                    ArouteUtils.startWebArticle(
                        item.appUrl,
                        item.word,
                        item.index,
                        false
                    )
                }
            }
        }
    }
}

interface HotAdapterInterface<T : Any> {
    val items: List<T>
    fun setOnItemClickListener(listener: (adapter: BaseQuickAdapter<T, *>, view: View, position: Int) -> Unit)
}
