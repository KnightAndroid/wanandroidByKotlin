package com.core.library_base.ktx

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.core.library_base.loadsir.ErrorCallBack
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.knight.kotlin.library_base.R


/**
 * Author:Knight
 * Time:2022/1/19 10:15
 * Description:CustomViewExt
 */


/**
 * 注册加载视图
 *
 * @param view 需要加载的view
 * @param callback 回调方法
 * @return
 */
fun loadServiceInit(view: View, callback: () -> Unit): LoadService<Any> {
    val loadService = LoadSir.getDefault().register(view) {
        //点击重试时触发的操作
        callback.invoke()
    }
    loadService.showCallback(com.core.library_base.loadsir.LoadCallBack::class.java)
    return loadService
}


/**
 * 提示错误信息
 * @param message 错误信息
 */
fun LoadService<*>.setErrorText(message:String) {
    this.setCallBack(ErrorCallBack::class.java){ _, view ->
        //错误信息
        view.findViewById<TextView>(R.id.tv_error_data).text = message
    }
}


//绑定普通的Recycelview
fun RecyclerView.init(
    layoutManager:RecyclerView.LayoutManager,
    bindAdapter:RecyclerView.Adapter<*>,
    isScroll:Boolean = true
): RecyclerView{
    setLayoutManager(layoutManager)
    //设置 setHasFixedSize(true) 后，RecyclerView会假设所有的Item的高度是固定的，不会因为Item的变化而触发重新计算布局，避免requestLayout导致的资源浪费。
    setHasFixedSize(true)
    adapter = bindAdapter
    isNestedScrollingEnabled = isScroll
    return this
}
