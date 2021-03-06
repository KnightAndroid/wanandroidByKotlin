package com.knight.kotlin.library_base.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager


/**
 * Author:Knight
 * Time:2021/12/24 15:00
 * Description:StatusBarUtils
 * 状态栏工具
 */
object StatusBarUtils {

    /**
     *
     * 设置透明状态栏
     */
    fun transparentStatusBar(activity: Activity){
        //5.0以上系统状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                val ic = activity.window.insetsController
                if (ic != null) {
                    //让状态栏变亮 0,WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS 让状态栏字体变白
                    if (CacheUtils.getNormalDark()) {
                        ic.setSystemBarsAppearance(0, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
                    } else {
                        if (CacheUtils.getNightModeStatus()) {
                            ic.setSystemBarsAppearance(0,WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
                        } else {
                            ic.setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
                        }
                    }
                    activity.window.setDecorFitsSystemWindows(false)
                    activity.window.statusBarColor = Color.TRANSPARENT
                }
            } else {
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                activity.window.statusBarColor = Color.TRANSPARENT
                if (CacheUtils.getNormalDark()) {
                    //白色字体
                    activity.window.decorView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                } else {
                    if (CacheUtils.getNightModeStatus()) {
                        activity.window.decorView.systemUiVisibility =
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    } else {
                        activity.window.decorView.systemUiVisibility =
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    }
                }
            }
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     *
     * 返回状态栏高度
     * @param context
     * @return
     */
    fun getStatusBarHeight(context: Context): Int {
        val resourceId: Int =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return context.resources.getDimensionPixelSize(resourceId)
    }


    fun setAndroidNativeLightStatusBar(activity: Activity, dark: Boolean) {
        val decor = activity.window.decorView
        if (dark) {
            decor.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decor.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

}