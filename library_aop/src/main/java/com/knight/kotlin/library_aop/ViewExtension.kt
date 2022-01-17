package com.knight.kotlin.library_aop

import android.view.View

/**
 * Author:Knight
 * Time:2022/1/14 16:32
 * Description:ViewExtension
 */
fun View.needQuickClick(){
    this.setTag(this.id,true)
}

fun View.isNeedQuickClick(): Boolean{
    if(this.getTag(this.id) is Boolean){
        return this.getTag(this.id) as Boolean
    }
    return false
}