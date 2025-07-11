package com.knight.kotlin.library_permiss.start

import android.content.Intent
import androidx.annotation.IntRange
import androidx.fragment.app.Fragment

/**
 * @Description
 * @Author knight
 * @Time 2025/6/8 20:40
 *
 */

class StartActivityDelegateByFragmentApp( fragment: Fragment) : IStartActivityDelegate {
    
    private val mFragment = fragment

    override fun startActivity(intent: Intent) {
        if (intent == null) {
            return
        }
        mFragment.startActivity(intent)
    }

    override fun startActivityForResult(intent: Intent, @IntRange(from = 1, to = 65535) requestCode: Int) {
        if (intent == null) {
            return
        }
        mFragment.startActivityForResult(intent, requestCode)
    }
}