package com.knight.kotlin.library_widget.skeleton

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Author:Knight
 * Time:2022/3/31 16:06
 * Description:ViewReplacer
 */
class ViewReplacer constructor(sourceView: View) {
    private val TAG = ViewReplacer::class.java.name
    private var mSourceView: View = sourceView
    private var mTargetView: View? = null
    private var mTargetViewResID = -1
    private var mCurrentView: View? = null
    private var mSourceParentView: ViewGroup? = null
    private var mSourceViewLayoutParams: ViewGroup.LayoutParams? = null
    private var mSourceViewIndexInParent = 0
    private var mSourceViewId = 0

    init {
        mSourceViewLayoutParams = mSourceView.getLayoutParams()
        mCurrentView = mSourceView
        mSourceViewId = mSourceView.getId()
    }

    fun replace(targetViewResID: Int) {
        if (mTargetViewResID == targetViewResID) {
            return
        }
        if (init()) {
            mTargetViewResID = targetViewResID
            replace(
                LayoutInflater.from(mSourceView.context)
                    .inflate(mTargetViewResID, mSourceParentView, false)
            )
        }
    }

    fun replace(targetView: View) {
        if (mCurrentView === targetView) {
            return
        }
        if (targetView.parent != null) {
            (targetView.parent as ViewGroup).removeView(targetView)
        }
        if (init()) {
            mTargetView = targetView
            mSourceParentView!!.removeView(mCurrentView)
            mTargetView?.setId(mSourceViewId)
            mSourceParentView!!.addView(
                mTargetView,
                mSourceViewIndexInParent,
                mSourceViewLayoutParams
            )
            mCurrentView = mTargetView
        }
    }


    fun restore() {
        if (mSourceParentView != null) {
            mSourceParentView?.removeView(mCurrentView)
            mSourceParentView?.addView(
                mSourceView,
                mSourceViewIndexInParent,
                mSourceViewLayoutParams
            )
            mCurrentView = mSourceView
            mTargetView = null
            mTargetViewResID = -1
        }
    }

    fun getSourceView(): View {
        return mSourceView
    }

    fun getTargetView(): View? {
        return mTargetView
    }

    fun getCurrentView(): View? {
        return mCurrentView
    }

    private fun init(): Boolean {
        if (mSourceParentView == null) {
            mSourceParentView = mSourceView.parent as ViewGroup
            if (mSourceParentView == null) {
                Log.e(TAG, "the source view have not attach to any view")
                return false
            }
            val count = mSourceParentView?.getChildCount() ?: 0
            for (index in 0 until count) {
                if (mSourceView === mSourceParentView?.getChildAt(index)) {
                    mSourceViewIndexInParent = index
                    break
                }
            }
        }
        return true
    }
}