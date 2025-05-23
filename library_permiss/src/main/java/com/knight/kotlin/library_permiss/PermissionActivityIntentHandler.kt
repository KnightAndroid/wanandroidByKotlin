package com.knight.kotlin.library_permiss

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid13
import com.knight.kotlin.library_permiss.utils.PermissionUtils.findActivity


/**
 * Author:Knight
 * Time:2023/8/30 17:32
 * Description:PermissionActivityIntentHandler
 */
class PermissionActivityIntentHandler {

    interface IStartActivityDelegate {
        fun startActivity(intent: Intent)
        fun startActivityForResult( intent: Intent, requestCode: Int)
    }

    companion object {
        /** 存取子意图所用的 Intent Key */
        private const val SUB_INTENT_KEY = "sub_intent_key"
        /**
         * 从父意图中获取子意图
         */
        fun findSubIntentBySuperIntent( mainIntent: Intent?): Intent? {
            val subIntent: Intent?
            subIntent = if (isAndroid13()) {
                mainIntent!!.getParcelableExtra(SUB_INTENT_KEY, Intent::class.java)
            } else {
                mainIntent!!.getParcelableExtra(SUB_INTENT_KEY)
            }
            return subIntent
        }

        /**
         * 获取意图中最底层的子意图
         */
        fun findDeepIntent( superIntent: Intent?): Intent? {
            val subIntent = findSubIntentBySuperIntent(superIntent)
            return subIntent?.let { findDeepIntent(it) } ?: superIntent
        }
        /**
         * 将子意图添加到主意图中
         */
        fun addSubIntentForMainIntent(
             mainIntent: Intent?,
             subIntent: Intent?
        ): Intent? {
            if (mainIntent == null && subIntent != null) {
                return subIntent
            }
            if (subIntent == null) {
                return mainIntent
            }
            val deepSubIntent = findDeepIntent(mainIntent)
            deepSubIntent?.putExtra(SUB_INTENT_KEY, subIntent)
            return mainIntent
        }

        fun startActivity( context: Context, intent: Intent): Boolean {
            return startActivity(StartActivityDelegateContextImpl(context), intent)
        }

        fun startActivity( activity: Activity, intent: Intent): Boolean {
            return startActivity(StartActivityDelegateActivityImpl(activity), intent)
        }

        fun startActivity(fragment: Fragment, intent: Intent): Boolean {
            return startActivity(StartActivityDelegateFragmentImpl(fragment), intent)
        }

        fun startActivity(
            delegate: IStartActivityDelegate,
             intent: Intent
        ): Boolean {
            return try {
                delegate.startActivity(intent)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                val subIntent = findSubIntentBySuperIntent(intent) ?: return false
                startActivity(delegate, subIntent)
            }
        }

        fun startActivityForResult(
             activity: Activity,
             intent: Intent,
            requestCode: Int
        ): Boolean {
            return startActivityForResult(
                StartActivityDelegateActivityImpl(activity),
                intent,
                requestCode
            )
        }

        fun startActivityForResult(
             fragment: Fragment,
             intent: Intent,
            requestCode: Int
        ): Boolean {
            return startActivityForResult(
                StartActivityDelegateFragmentImpl(fragment),
                intent,
                requestCode
            )
        }


        fun startActivityForResult(
             delegate: IStartActivityDelegate,
             intent: Intent,
            requestCode: Int
        ): Boolean {
            return try {
                delegate.startActivityForResult(intent, requestCode)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                val subIntent = findSubIntentBySuperIntent(intent) ?: return false
                startActivityForResult(delegate, subIntent, requestCode)
            }
        }
    }

    private class StartActivityDelegateContextImpl(val mContext: Context) :
        IStartActivityDelegate {
        override fun startActivity( intent: Intent) {
            mContext.startActivity(intent)
        }

        override fun startActivityForResult( intent: Intent, requestCode: Int) {
            val activity = findActivity(mContext)
            if (activity != null) {
                activity.startActivityForResult(intent, requestCode)
                return
            }
            startActivity(intent)
        }
    }

    private class StartActivityDelegateActivityImpl(val mActivity: Activity) :
        IStartActivityDelegate {
        override fun startActivity(intent: Intent) {
            mActivity.startActivity(intent)
        }

        override fun startActivityForResult(intent: Intent, requestCode: Int) {
            mActivity.startActivityForResult(intent, requestCode)
        }
    }

    private class StartActivityDelegateFragmentImpl (val mFragment: Fragment) :
        IStartActivityDelegate {
        override fun startActivity( intent: Intent) {
            mFragment.startActivity(intent)
        }

        override fun startActivityForResult( intent: Intent, requestCode: Int) {
            mFragment.startActivityForResult(intent, requestCode)
        }
    }

    private class StartActivityDelegateSupportFragmentImpl ( val fragment: Fragment) :
        IStartActivityDelegate {
        private val mFragment: Fragment = fragment

        override fun startActivity( intent: Intent) {
            mFragment.startActivity(intent)
        }

        override fun startActivityForResult(intent: Intent, requestCode: Int) {
            mFragment.startActivityForResult(intent, requestCode)
        }
    }
}