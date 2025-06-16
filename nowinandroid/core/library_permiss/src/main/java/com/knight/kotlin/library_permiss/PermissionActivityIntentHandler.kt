package com.knight.kotlin.library_permiss

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.knight.kotlin.library_permiss.AndroidVersionTools.isAndroid13


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
         * 获取意图中最深层的子意图
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

        fun startActivity(context: Context, intent: Intent): Boolean {
            return startActivity(StartActivityDelegateByContext(context), intent)
        }

        fun startActivity(activity: Activity, intent: Intent): Boolean {
            return startActivity(StartActivityDelegateByActivity(activity), intent)
        }

        @Suppress("deprecation")
        fun startActivity( fragment: Fragment, intent: Intent): Boolean {
            return startActivity(StartActivityDelegateByFragmentApp(fragment), intent)
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
                StartActivityDelegateByActivity(activity),
                intent,
                requestCode
            )
        }

        @Suppress("deprecation")
        fun startActivityForResult(
             fragment: Fragment,
            intent: Intent,
            requestCode: Int
        ): Boolean {
            return startActivityForResult(
                StartActivityDelegateByFragmentApp(fragment),
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

}