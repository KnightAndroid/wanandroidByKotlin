package com.knight.kotlin.library_base.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.core.view.WindowInsetsCompat
import androidx.window.layout.WindowMetricsCalculator


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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            val ic = activity.window.insetsController
            if (ic != null) {
                //让状态栏变亮 0,WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS 让状态栏字体变白
                if (com.knight.kotlin.library_common.util.CacheUtils.getNormalDark()) {
                    ic.setSystemBarsAppearance(0, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
                } else {
                    if (com.knight.kotlin.library_common.util.CacheUtils.getNightModeStatus()) {
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
            if (com.knight.kotlin.library_common.util.CacheUtils.getNormalDark()) {
                //白色字体
                activity.window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            } else {
                if (com.knight.kotlin.library_common.util.CacheUtils.getNightModeStatus()) {
                    activity.window.decorView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                } else {
                    activity.window.decorView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            }
        }
    }

    /**
     *
     * 返回状态栏高度
     * @param context
     * @return
     */
    @SuppressLint("DiscouragedApi", "InternalInsetResource")
    fun getStatusBarHeight(context: Context): Int {

        val metrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val insets =
                metrics.getWindowInsets()
                .getInsetsIgnoringVisibility(WindowInsetsCompat.Type.systemBars())
            return insets.top
        } else {
            val resourceId: Int =
                context.resources.getIdentifier("status_bar_height", "dimen", "android")
            return context.resources.getDimensionPixelSize(resourceId)
        }




    }


    /**
     * 设置状态栏字体颜色
     * @param activity 当前界面
     * @param dark 是否是黑色
     */
    fun setAndroidNativeLightStatusBar(activity: Activity, dark: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            val ic = activity.window.insetsController
            if (ic != null) {
                //让状态栏变亮 0,WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS 让状态栏字体变白
                if (dark) {
                    ic.setSystemBarsAppearance(0,WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
                } else {
                    ic.setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
                }
            }
        } else {
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


    /**
     * 设置透明状态栏字体黑色还是白色
     *
     *
     * @param activity
     * @param dark true 字体黑色
     */
    fun transparentStatusBarWithFont(activity: Activity, dark: Boolean) {
        //5.0以上系统状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val ic = activity.window.insetsController
            if (ic != null) {
                //让状态栏变亮 0,WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS 让状态栏字体变白
                if (dark) {
                    ic.setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                } else {
                    ic.setSystemBarsAppearance(
                        0,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                }
                activity.window.setDecorFitsSystemWindows(false)
                activity.window.statusBarColor = Color.TRANSPARENT
            }
        } else {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.statusBarColor = Color.TRANSPARENT
            if (dark) {
                activity.window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

            } else {
                activity.window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            }

        }
    }

}