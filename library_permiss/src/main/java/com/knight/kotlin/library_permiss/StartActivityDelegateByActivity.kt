package com.knight.kotlin.library_permiss

import android.app.Activity
import android.content.Intent
import com.knight.kotlin.library_permiss.PermissionActivityIntentHandler.IStartActivityDelegate



/**
 * @Description
 * @Author knight
 * @Time 2025/6/8 20:38
 *
 */

internal class StartActivityDelegateByActivity( activity: Activity) :
    IStartActivityDelegate {
    
    private val mActivity: Activity = activity

    override fun startActivity(intent: Intent) {
        mActivity.startActivity(intent)
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        mActivity.startActivityForResult(intent, requestCode)
    }
}