package com.core.library_base.loadsir

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.ProgressBar
import com.kingja.loadsir.callback.Callback
import com.knight.kotlin.library_base.R


/**
 * Author:Knight
 * Time:2022/1/18 15:52
 * Description:LoadCallBack
 */
class LoadCallBack:Callback() {

    private lateinit var loadProgressBar: ProgressBar

    override fun onCreateView(): Int {
        return R.layout.base_layout_load
    }


    override fun onViewCreate(context: Context,view: View){
        super.onViewCreate(context, view)
        loadProgressBar = view.findViewById(R.id.base_load_pb)

    }



    fun setIndeterminateTint(tint:Int) {
        loadProgressBar.indeterminateTintList = ColorStateList.valueOf(tint)
    }





}