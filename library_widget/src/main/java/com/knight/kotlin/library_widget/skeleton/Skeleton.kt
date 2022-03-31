package com.knight.kotlin.library_widget.skeleton

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Author:Knight
 * Time:2022/3/31 15:47
 * Description:Skeleton
 */
object Skeleton {
    fun bind(recyclerView: RecyclerView): RecyclerViewSkeletonScreen.Builder {
        return RecyclerViewSkeletonScreen.Builder(recyclerView)
    }

    fun bind(view: View): ViewSkeletonScreen.Builder {
        return ViewSkeletonScreen.Builder(view)
    }
}