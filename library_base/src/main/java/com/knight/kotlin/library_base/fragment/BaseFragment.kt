package com.knight.kotlin.library_base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.knight.kotlin.library_base.annotation.EventBusRegister
import com.knight.kotlin.library_base.util.BindingReflex
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.EventBusUtils
import com.knight.kotlin.library_base.util.ViewRecreateHelper
import com.knight.kotlin.library_base.view.BaseView
import com.knight.kotlin.library_base.vm.BaseViewModel

/**
 * Author:Knight
 * Time:2021/12/22 14:48
 * Description:BaseFragment
 * Fragment 基类
 */
abstract class BaseFragment<VB: ViewBinding,VM:BaseViewModel>:Fragment(),BaseView<VB> {

    /**
     * 私有的ViewBinding
     *
     */
    private var _binding:VB? = null

    /**
     *
     * 是否第一次加载
     */
    private var isFirst:Boolean = true

    protected val mBinding get() = _binding!!
    protected abstract val mViewModel:VM

    /**
     * fragment状态保存工具类
     *
     */
    private var mStatusHelper:FragmentStatusHelper? = null

    /**
     *
     * 颜色值
     */
    protected var themeColor:Int?=null

    /**
     * 是否暗黑模式
     */
    protected var isDarkMode:Boolean = false

    /**
     * 懒加载
     */
    protected open fun lazyLoadData() {
        initRequestData()
    }

    /**
     * 主题色设置
     *
     */
    protected abstract fun setThemeColor(isDarkMode:Boolean)
    override fun onCreateView(
        inflater:LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        _binding = BindingReflex.reflexViewBinding(javaClass,layoutInflater)
        return _binding?.root
    }


    override fun onViewCreated(view:View,savedInstanceState:Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //处理恢复
        mStatusHelper?.onRestoreInstanceStatus(savedInstanceState)
        // ARouter 依赖注入
        ARouter.getInstance().inject(this)
        //注册EventBus
        if (javaClass.isAnnotationPresent(EventBusRegister::class.java)) EventBusUtils.register(this)
        isDarkMode = CacheUtils.getNormalDark()
        initThemeColor()
        setThemeColor(isDarkMode)
        mBinding.initView()
        onVisible()
        initObserver()


    }


    override fun isRecreate():Boolean = mStatusHelper?.isRecreate ?: false



    override fun onSaveInstanceState(outState:Bundle) {
        if (mStatusHelper == null) {
            //仅当触发重建需要保存状态时创建对象
            mStatusHelper = FragmentStatusHelper(outState)
        } else {
            mStatusHelper?.onSaveInstanceState(outState)
        }
        super.onSaveInstanceState(outState)
    }

    /**
     * 获取主题颜色
     *
     */
    protected fun initThemeColor() {
        themeColor = CacheUtils.getThemeColor()
    }


    /**
     * - fragment状态保存帮助类
     * - 暂时没有其他需要保存的 -- 空继承
     *
     */
    private class FragmentStatusHelper(savedInstanceState:Bundle? = null) :
            ViewRecreateHelper(savedInstanceState)


    override fun onResume() {
        super.onResume()
        onVisible()
    }
    /**
     *
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            lazyLoadData()
            isFirst = false
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        if (javaClass.isAnnotationPresent(EventBusRegister::class.java)) EventBusUtils.unRegister(this)
        super.onDestroy()
    }


}