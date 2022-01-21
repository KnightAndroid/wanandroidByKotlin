package com.knight.kotlin.library_base.loadsir

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.ProgressBar
import com.kingja.loadsir.callback.Callback
import com.knight.kotlin.library_base.R
import com.knight.kotlin.library_base.util.CacheUtils


/**
 * Author:Knight
 * Time:2022/1/18 15:52
 * Description:LoadCallBack
 */
class LoadCallBack:Callback() {

    private lateinit var load_progress_bar: ProgressBar

    override fun onCreateView(): Int {
        return R.layout.base_layout_load
    }


    override fun onViewCreate(context: Context,view: View){
        super.onViewCreate(context, view)
        load_progress_bar = view.findViewById(R.id.base_load_pb)
        load_progress_bar.indeterminateTintList = ColorStateList.valueOf(CacheUtils.getThemeColor())
    }





}