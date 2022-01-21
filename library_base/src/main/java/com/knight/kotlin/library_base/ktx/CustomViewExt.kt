package com.knight.kotlin.library_base.ktx

import android.view.View
import android.widget.TextView
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.knight.kotlin.library_base.R
import com.knight.kotlin.library_base.loadsir.ErrorCallBack
import com.knight.kotlin.library_base.loadsir.LoadCallBack

/**
 * Author:Knight
 * Time:2022/1/19 10:15
 * Description:CustomViewExt
 */


/**
 * 注册加载视图
 *
 */
fun LoadServiceInit(view: View, callback: () -> Unit): LoadService<Any> {
    val loadsir = LoadSir.getDefault().register(view) {
        //点击重试时触发的操作
        callback.invoke()
    }
    loadsir.showCallback(LoadCallBack::class.java)
    return loadsir
}


/**
 * 提示错误信息
 */
fun LoadService<*>.setErrorText(message:String) {
    this.setCallBack(ErrorCallBack::class.java){_,view ->
        //错误信息
        view.findViewById<TextView>(R.id.tv_error_data).text = message
    }
}
