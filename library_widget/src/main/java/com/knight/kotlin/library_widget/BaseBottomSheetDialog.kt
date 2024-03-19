package com.knight.kotlin.library_widget

import android.os.Bundle
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Author:Knight
 * Time:2024/3/19 10:16
 * Description:BaseBottomSheetDialog 底部弹出框
 */
open class BaseBottomSheetDialog : BottomSheetDialogFragment() {
    private var bottomSheet: FrameLayout?=null
    var behavior : BottomSheetBehavior<FrameLayout>?=null


    override fun onStart() {
        super.onStart()
        val dialog = dialog as BottomSheetDialog
        bottomSheet = dialog.delegate.findViewById(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            val layoutParams = bottomSheet?.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.height = height
            bottomSheet?.layoutParams = layoutParams
            behavior = BottomSheetBehavior.from(it)
            behavior?.peekHeight = height
            //初始为展开状态
            behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL,R.style.BottomSheetDialog)
        isCancelable = true
    }

    protected open val height: Int
        protected get() = resources.displayMetrics.heightPixels


}