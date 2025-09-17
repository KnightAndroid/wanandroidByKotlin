package com.knight.kotlin.library_permiss.start

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.annotation.IntRange
import com.knight.kotlin.library_permiss.tools.PermissionUtils


/**
 * @Description
 * @Author knight
 * @Time 2025/6/8 20:39
 *
 */

class StartActivityDelegateByContext( context: Context) : IStartActivityDelegate {
    
    private val mContext = context

    override fun startActivity(intent: Intent) {
        val activity = PermissionUtils.findActivity(mContext)
        if (activity != null) {
            activity.startActivity(intent)
            return
        }

        // https://developer.android.google.cn/about/versions/pie/android-9.0-changes-all?hl=zh-cn#fant-required
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        mContext.startActivity(intent)
    }

    override fun startActivityForResult(intent: Intent, @IntRange(from = 1, to = 65535) requestCode: Int) {
        if (intent == null) {
            return
        }
        val activity = PermissionUtils.findActivity(mContext)
        if (activity != null) {
            activity.startActivityForResult(intent, requestCode)
            return
        }
        // https://developer.android.google.cn/about/versions/pie/android-9.0-changes-all?hl=zh-cn#fant-required
        if (mContext !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }
}