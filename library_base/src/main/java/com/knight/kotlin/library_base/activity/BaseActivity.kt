package com.knight.kotlin.library_base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.knight.kotlin.library_base.view.BaseView
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.BindingReflex
import com.knight.kotlin.library_util.EventBusUtils
import com.knight.kotlin.library_util.ViewRecreateHelper
import com.knight.kotlin.library_util.annotation.EventBusRegister
import com.knight.kotlin.library_util.network.AutoRegisterNetListener
import com.knight.kotlin.library_util.network.NetworkStateChangeListener

/**
 * Author:Knight
 * Time:2021/12/15 16:06
 * Description:BaseActivity
 */
abstract class BaseActivity<VB : ViewBinding,VM : BaseViewModel> : AppCompatActivity(),BaseView<VB>,NetworkStateChangeListener {

    protected val mBinding :VB by lazy( mode = LazyThreadSafetyMode.NONE ) {
        BindingReflex.reflexViewBinding(javaClass,layoutInflater)
    }

    protected abstract val mViewModel:VM

    /**
     * activity页面重建帮助类
     */
    private var mStatusHelper:ActivityRecreateHelper? = null


    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        //处理保存的状态
        mStatusHelper?.onRestoreInstanceStatus(savedInstanceState)
        //ARouter 依赖注入
        ARouter.getInstance().inject(this)
        //注册EventBus
        if (javaClass.isAnnotationPresent(EventBusRegister::class.java)) EventBusUtils.register(this)

    }


    /**
     * 初始化网络状态监听
     * @return Unit
     *
     */
    private fun initNetworkListener() {
        lifecycle.addObserver(AutoRegisterNetListener(this))
    }

    /**
     * 网络类型更改回调
     * @param type Int 网络类型
     * @return Unit
     *
     */
    override fun networkTypeChange(type:Int) {

    }



    override fun isRecreate(): Boolean = mStatusHelper?.isRecreate ?: false

    override fun onSaveInstanceState(outState: Bundle) {
        if (mStatusHelper == null) {
            //仅当触发重建需要保存状态时创建对象
            mStatusHelper = ActivityRecreateHelper(outState)
        } else {
            mStatusHelper?.onSaveInstanceState(outState)
        }
        super.onSaveInstanceState(outState)
    }

    /**
     *
     * activity重建帮助工具类
     */
    private class ActivityRecreateHelper(savedInstanceState:Bundle?=null) :
            ViewRecreateHelper(savedInstanceState)

    override fun onDestroy() {
        if (javaClass.isAnnotationPresent(EventBusRegister::class.java)) EventBusUtils.unRegister(this)
        super.onDestroy()
    }

}