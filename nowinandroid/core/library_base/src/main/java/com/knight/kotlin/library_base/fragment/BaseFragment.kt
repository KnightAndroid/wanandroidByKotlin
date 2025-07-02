package com.knight.kotlin.library_base.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.viewbinding.ViewBinding
import com.core.library_base.R
import com.core.library_base.annotation.EventBusRegister
import com.core.library_base.ktx.ClickAction
import com.core.library_base.loadsir.EmptyCallBack
import com.core.library_base.loadsir.ErrorCallBack
import com.core.library_base.util.BindingReflex
import com.core.library_base.util.EventBusUtils
import com.core.library_base.util.ViewRecreateHelper
import com.core.library_base.view.BaseView
import com.core.library_base.vm.BaseViewModel
import com.core.library_base.widget.loadcircleview.ProgressHud
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.knight.kotlin.library_base.ktx.createViewModel
import com.knight.kotlin.library_base.ktx.subscribeData
import com.knight.kotlin.library_base.utils.CacheUtils
import com.wyjson.router.GoRouter

/**
 * Author:Knight
 * Time:2021/12/22 14:48
 * Description:BaseFragment
 * Fragment 基类
 */
abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel> : Fragment(), BaseView<VB>,
    ClickAction {

    /**
     * 私有的ViewBinding
     *
     */
    private var _binding: VB? = null

    /**
     *
     * 是否第一次加载
     */
    private var isFirst: Boolean = true

    protected val mBinding get() = _binding!!
    lateinit var mViewModel: VM

    /**
     * fragment状态保存工具类
     *
     */
    private var mStatusHelper: FragmentStatusHelper? = null

    /**
     * 颜色值
     */
    protected val themeColor:Int =  CacheUtils.getThemeColor()

    /**
     * 是否暗黑模式
     */
    protected var isDarkMode: Boolean = false

    /**
     *
     * 全局加载视图
     */
    private lateinit var mLoadService: LoadService<Any>

    val loadingDialog: ProgressHud by lazy{ ProgressHud(requireActivity()) }

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
    protected abstract fun setThemeColor(isDarkMode: Boolean)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BindingReflex.reflexViewBinding(javaClass, layoutInflater)
        return _binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //处理恢复
        mStatusHelper?.onRestoreInstanceStatus(savedInstanceState)
        // ARouter 依赖注入
        //GoRouter.getInstance().injectCheck(this)
        try {
            GoRouter.getInstance().inject(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mViewModel = createViewModel()
        //注册EventBus
        if (javaClass.isAnnotationPresent(EventBusRegister::class.java)) EventBusUtils.register(this)
        isDarkMode = CacheUtils.getNormalDark()
        setThemeColor(isDarkMode)
        mBinding.initView()
        onVisible()
        subscribeData()
        initObserver()


    }

    /**
     * 根据资源 id 获取一个 View 对象
     */
    override fun <V : View?> findViewById(@IdRes id: Int): V? {
        return mBinding.root.findViewById(id)
    }

    /**
     * 回调vm调用api方法获取的数据
     *
     *
     * @param T
     * @param block
     */
    protected fun <T : Any> LiveData<T>.observerKt(block: (T) -> Unit) {
        this.observe(viewLifecycleOwner) {
            block(it)
        }
    }
    override fun isRecreate(): Boolean = mStatusHelper?.isRecreate ?: false


    override fun onSaveInstanceState(outState: Bundle) {
        if (mStatusHelper == null) {
            //仅当触发重建需要保存状态时创建对象
            mStatusHelper = FragmentStatusHelper(outState)
        } else {
            mStatusHelper?.onSaveInstanceState(outState)
        }
        super.onSaveInstanceState(outState)
    }




    /**
     * - fragment状态保存帮助类
     * - 暂时没有其他需要保存的 -- 空继承
     *
     */
    private class FragmentStatusHelper(savedInstanceState: Bundle? = null) :
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

    /**
     *
     * 默认加载
     */
    protected fun requestLoading(view: View,tint: Int = CacheUtils.getThemeColor()) {
        if (!::mLoadService.isInitialized) {
            mLoadService = LoadSir.getDefault().register(view) {
                mLoadService.showCallback(com.core.library_base.loadsir.LoadCallBack::class.java)
                reLoadData()
            }
        }
        mLoadService.showCallback(com.core.library_base.loadsir.LoadCallBack::class.java)
        val pb:ProgressBar = mLoadService.loadLayout.findViewById<ProgressBar>(R.id.base_load_pb)
        if (pb != null) {
                val colorStateList = ColorStateList.valueOf(tint)
                pb.indeterminateTintList = colorStateList

        }
    }

    /**
     *
     * 成功回调
     */
    protected fun requestSuccess() {
        if (::mLoadService.isInitialized) {
            mLoadService.showSuccess()
        }
    }


    /**
     * 请求失败的回调
     */
    protected fun requestFailure() {
        if (::mLoadService.isInitialized) {
            mLoadService.showCallback(ErrorCallBack::class.java)
        }
    }


    /**
     * 空数据请求的回调
     */
    protected fun requestEmptyData() {
        if (::mLoadService.isInitialized) {
            mLoadService.showCallback(EmptyCallBack::class.java)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        if (javaClass.isAnnotationPresent(EventBusRegister::class.java)) EventBusUtils.unRegister(
            this
        )
        super.onDestroy()
    }


}