package com.knight.kotlin.library_widget.overlaymenu


import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import com.knight.kotlin.library_base.BaseApp
import com.knight.kotlin.library_widget.R
import java.lang.ref.WeakReference
import kotlin.concurrent.Volatile


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/23 14:48
 * @descript:
 */
class FloatingView private constructor() : IFloatingView {
    private var mEnFloatingView: FloatingMagnetView? = null
    private var mContainer: WeakReference<FrameLayout?>? = null

    @LayoutRes
    private var mLayoutId: Int = 0

    @DrawableRes
    private var mIconRes: Int = R.drawable.widget_weather_icon_w0
    private var mLayoutParams: ViewGroup.LayoutParams = getParams()

    override fun remove(): FloatingView {
        Handler(Looper.getMainLooper()).post(Runnable {
            if (mEnFloatingView == null) {
                return@Runnable
            }
            if (ViewCompat.isAttachedToWindow(mEnFloatingView!!) && getContainer() != null) {
                getContainer()?.removeView(mEnFloatingView)
                mEnFloatingView?.onRemove()
            }
            mEnFloatingView = null
        })
        return this
    }

    private fun ensureFloatingView() {
        synchronized(this) {
            if (mEnFloatingView != null) {
                return
            }
            val enFloatingView: EnFloatingView = EnFloatingView(BaseApp.context, mLayoutId)
            mEnFloatingView = enFloatingView
            enFloatingView.setLayoutParams(mLayoutParams)
            //enFloatingView.setIconImage(mIconRes)
            addViewToWindow(enFloatingView)
        }
    }

    override fun add(): FloatingView {
        ensureFloatingView()
        return this
    }

    override fun attach(activity: Activity): FloatingView {
        attach(getActivityRoot(activity))
        return this
    }

    override fun attach(container: FrameLayout?): FloatingView {
        if (container == null || mEnFloatingView == null) {
            mContainer = WeakReference<FrameLayout?>(container)
            return this
        }
        if (mEnFloatingView?.getParent() === container) {
            return this
        }
        if (mEnFloatingView?.getParent() != null) {
            (mEnFloatingView?.getParent() as ViewGroup).removeView(mEnFloatingView)
        }
        mContainer = WeakReference<FrameLayout?>(container)
        container.addView(mEnFloatingView)
        return this
    }

    override fun detach(activity: Activity): FloatingView {
        detach(getActivityRoot(activity))
        return this
    }

    override fun detach(container: FrameLayout?): FloatingView {
        if (mEnFloatingView != null && container != null && ViewCompat.isAttachedToWindow(mEnFloatingView!!)) {
            container.removeView(mEnFloatingView)
        }
        if (getContainer() === container) {
            mContainer = null
        }
        return this
    }

    override fun getView(): FloatingMagnetView? {
        return mEnFloatingView
    }

    override fun icon(@DrawableRes resId: Int): FloatingView {
        mIconRes = resId
        return this
    }

    override fun customView(viewGroup: FloatingMagnetView): FloatingView {
        mEnFloatingView = viewGroup
        return this
    }

    override fun customView(@LayoutRes resource: Int): FloatingView {
        mLayoutId = resource
        return this
    }

    override fun layoutParams(params: ViewGroup.LayoutParams): FloatingView {
        mLayoutParams = params
        if (mEnFloatingView != null) {
            mEnFloatingView?.setLayoutParams(params)
        }
        return this
    }

    override fun listener(magnetViewListener: MagnetViewListener?): FloatingView {
       mEnFloatingView?.setMagnetViewListener(magnetViewListener)

        return this
    }

    override fun dragEnable(dragEnable: Boolean): FloatingView {

       mEnFloatingView?.updateDragState(dragEnable)

        return this
    }

    override fun setAutoMoveToEdge(autoMoveToEdge: Boolean): FloatingView {

            mEnFloatingView?.setAutoMoveToEdge(autoMoveToEdge)

        return this
    }

    private fun addViewToWindow(view: View) {
        if (getContainer() == null) {
            return
        }
        getContainer()?.addView(view)
    }

    private fun getContainer(): FrameLayout? {
        if (mContainer == null) {
            return null
        }
        return mContainer!!.get()
    }

    private fun getParams(): FrameLayout.LayoutParams {
        val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.BOTTOM or Gravity.START
        params.setMargins(13, params.topMargin, params.rightMargin, 500)
        return params
    }

    private fun getActivityRoot(activity: Activity): FrameLayout? {
        if (activity == null) {
            return null
        }
        try {
            return activity.getWindow().getDecorView().findViewById(android.R.id.content) as FrameLayout
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    companion object {
        @Volatile
        private var mInstance: FloatingView? = null
        fun get(): FloatingView? {
            if (mInstance == null) {
                synchronized(FloatingView::class.java) {
                    if (mInstance == null) {
                        mInstance = FloatingView()
                    }
                }
            }
            return mInstance
        }
    }
}