package com.core.library_base.loadsir


import com.kingja.loadsir.callback.Callback
import com.knight.kotlin.library_base.R


/**
 * Author:Knight
 * Time:2022/1/18 15:47
 * Description:EmptyCallBack
 */
class EmptyCallBack:Callback() {
    override fun onCreateView(): Int {
        return R.layout.base_layout_empty
    }
}