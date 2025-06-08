package com.knight.kotlin.library_permiss

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.knight.kotlin.library_permiss.PermissionActivityIntentHandler.IStartActivityDelegate
import com.knight.kotlin.library_permiss.utils.PermissionUtils


/**
 * @Description
 * @Author knight
 * @Time 2025/6/8 20:39
 *
 */

internal class StartActivityDelegateByContext( context: Context) : IStartActivityDelegate {
    
    private val mContext = context

    override fun startActivity(intent: Intent) {

        mContext.startActivity(intent)
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int) {

        val activity: Activity? = PermissionUtils.findActivity(mContext)
        if (activity != null) {
            activity.startActivityForResult(intent, requestCode)
            return
        }
        startActivity(intent)
    }
}