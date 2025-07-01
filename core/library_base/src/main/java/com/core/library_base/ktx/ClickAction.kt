package com.core.library_base.ktx

import android.view.View
import androidx.annotation.IdRes

/**
 * Author:Knight
 * Time:2022/1/17 10:25
 * Description:ClickAction
 */
interface ClickAction: View.OnClickListener {

    fun<V : View?> findViewById(@IdRes id:Int):V?

    fun setOnCLickListener(@IdRes vararg ids:Int){
        setOnClickListener(this,*ids)
    }

    fun setOnClickListener(listener:View.OnClickListener?,@IdRes vararg ids:Int) {
        for (id:Int in ids) {
            findViewById<View?>(id)?.setOnClickListener(listener)
        }
    }


    fun setOnClickListener(vararg views:View) {
        setOnClickListener(this,*views)

    }


    fun setOnClickListener(listener:View.OnClickListener?,vararg views:View?) {
        for (view:View? in views) {
            view?.setOnClickListener(listener)
        }
    }


    override fun onClick(v: View) {

    }



}