package com.knight.kotlin.library_permiss

import android.content.Intent
import androidx.fragment.app.Fragment
import com.knight.kotlin.library_permiss.PermissionActivityIntentHandler.IStartActivityDelegate



/**
 * @Description
 * @Author knight
 * @Time 2025/6/8 20:40
 *
 */

internal class StartActivityDelegateByFragmentApp( fragment: Fragment) :
    IStartActivityDelegate {
    
    private val mFragment: Fragment = fragment

    override fun startActivity(intent: Intent) {

        mFragment.startActivity(intent)
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        mFragment.startActivityForResult(intent, requestCode)
    }
}