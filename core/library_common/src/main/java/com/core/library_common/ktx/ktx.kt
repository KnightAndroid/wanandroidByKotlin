package com.core.library_common.ktx

import android.app.Application
import android.content.Context
import com.core.library_common.provider.ApplicationProvider


/**
 * @Description
 * @Author knight
 * @Time 2025/7/14 22:00
 *
 */

/**
 * 获取屏幕宽度
 */
val Context.screenWidth
    get() = resources.displayMetrics.widthPixels

/**
 * 获取屏幕高度
 */
val Context.screenHeight
    get() = resources.displayMetrics.heightPixels



/**
 *
 * 获取ApplicationContext
 */
fun getApplicationContext(): Application {
    return ApplicationProvider.getInstance()!!.getApplication()
}
