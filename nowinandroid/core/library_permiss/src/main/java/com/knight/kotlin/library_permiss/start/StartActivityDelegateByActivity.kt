package com.knight.kotlin.library_permiss.start

import android.app.Activity
import android.content.Intent
import androidx.annotation.IntRange



/**
 * @Description {@link android.app.Activity} 跳转 Activity 实现
 * @Author knight
 * @Time 2025/6/8 20:38
 *
 */

class StartActivityDelegateByActivity( activity: Activity) : IStartActivityDelegate {
    
    private val mActivity = activity

    override fun startActivity(intent: Intent) {
        if (intent == null) {
            return
        }
        mActivity.startActivity(intent)
    }

    override fun startActivityForResult(intent: Intent, @IntRange(from = 1, to = 65535) requestCode: Int) {
        mActivity.startActivityForResult(intent, requestCode)
    }
}