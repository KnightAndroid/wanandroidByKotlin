package com.knight.kotlin.library_widget.citypicker

import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import java.lang.ref.WeakReference




/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/17 10:46
 * @descript:
 */
class CityPicker {
    private var mContext: WeakReference<FragmentActivity>? = null
    private var mFragment: WeakReference<Fragment>? = null
    private var mFragmentManager: WeakReference<FragmentManager>? = null

    private var enableAnim = false
    private var mAnimStyle = 0
    private var mLocation: CityBean? = null
    private var mHotCities: List<CityBean>? = null
    private var mOnPickListener: OnPickListener? = null

    private constructor()

    private constructor(fragment: Fragment) : this(fragment.requireActivity(), fragment) {
        mFragmentManager = WeakReference<FragmentManager>(fragment.getChildFragmentManager())
    }

    private constructor(activity: FragmentActivity) : this(activity, null) {
        mFragmentManager = WeakReference(activity.supportFragmentManager)
    }

    private constructor(activity: FragmentActivity, fragment: Fragment?) {
        mContext = WeakReference(activity)
        mFragment = WeakReference<Fragment>(fragment)
    }

    /**
     * 设置动画效果
     * @param animStyle
     * @return
     */
    fun setAnimationStyle(@StyleRes animStyle: Int): CityPicker {
        this.mAnimStyle = animStyle
        return this
    }

    /**
     * 设置当前已经定位的城市
     * @param location
     * @return
     */
    fun setLocatedCity(location: CityBean?): CityPicker {
        this.mLocation = location
        return this
    }

    fun setHotCities(data: List<CityBean>?): CityPicker {
        this.mHotCities = data
        return this
    }

    /**
     * 启用动画效果，默认为false
     * @param enable
     * @return
     */
    fun enableAnimation(enable: Boolean): CityPicker {
        this.enableAnim = enable
        return this
    }

    /**
     * 设置选择结果的监听器
     * @param listener
     * @return
     */
    fun setOnPickListener(listener: OnPickListener?): CityPicker {
        this.mOnPickListener = listener
        return this
    }

    fun show() {
        var ft: FragmentTransaction? = mFragmentManager!!.get()?.beginTransaction()
        val prev: Fragment? = mFragmentManager!!.get()?.findFragmentByTag(TAG)
        if (prev != null) {
            if (ft != null) {
                ft.remove(prev).commit()
            }
            ft = mFragmentManager!!.get()?.beginTransaction()
        }
        if (ft != null) {
            ft.addToBackStack(null)
        }
//        val cityPickerFragment: CityPickerDialogFragment =
//            CityPickerDialogFragment.newInstance(enableAnim)
//        cityPickerFragment.setLocatedCity(mLocation)
//        cityPickerFragment.setHotCities(mHotCities)
//        cityPickerFragment.setAnimationStyle(mAnimStyle)
//        cityPickerFragment.setOnPickListener(mOnPickListener)
//        cityPickerFragment.show(ft, TAG)
    }

    /**
     * 定位完成
     * @param location
     * @param state
     */
    fun locateComplete(location: CityBean) {
       // val fragment: CityPickerDialogFragment = mFragmentManager!!.get()?.findFragmentByTag(TAG) as CityPickerDialogFragment
      //  if (fragment != null) {
          //  fragment.locationChanged(location)
      //  }
    }

    companion object {
        private const val TAG = "CityPicker"

        fun from(fragment: Fragment): CityPicker {
            return CityPicker(fragment)
        }

        fun from(activity: FragmentActivity): CityPicker {
            return CityPicker(activity)
        }
    }
}