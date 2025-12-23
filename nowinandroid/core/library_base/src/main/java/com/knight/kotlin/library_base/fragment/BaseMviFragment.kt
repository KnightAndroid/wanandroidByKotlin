package com.knight.kotlin.library_base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.core.library_base.annotation.EventBusRegister
import com.core.library_base.ktx.ClickAction
import com.core.library_base.loadsir.EmptyCallBack
import com.core.library_base.loadsir.ErrorCallBack
import com.core.library_base.util.BindingReflex
import com.core.library_base.util.EventBusUtils
import com.core.library_base.util.ViewRecreateHelper
import com.core.library_base.view.BaseView
import com.core.library_base.vm.BaseMviViewModel
import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState
import com.core.library_base.widget.loadcircleview.ProgressHud
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.knight.kotlin.library_base.ktx.collectKt
import com.knight.kotlin.library_base.ktx.createViewModel
import com.knight.kotlin.library_common.util.CacheUtils
import com.wyjson.router.GoRouter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


/**
 * @author created by luguian
 * @organize
 * @Date 2025/12/23 9:43
 * @descript:MVi基类
 */
abstract class BaseMviFragment<
        VB : ViewBinding,
        VM : BaseMviViewModel<EVENT, STATE, EFFECT>,
        EVENT : ViewEvent,
        STATE : ViewState,
        EFFECT : ViewSideEffect
        > : Fragment(),
    BaseView<VB>,
    ClickAction {

    // =========================
    // ViewBinding
    // =========================
    private var _binding: VB? = null
    protected val mBinding get() = _binding!!

    // =========================
    // ViewModel
    // =========================
    protected lateinit var mViewModel: VM

    // =========================
    // Fragment 状态
    // =========================
    private var isFirst: Boolean = true
    private var mStatusHelper: FragmentStatusHelper? = null

    // =========================
    // 主题 & 配置
    // =========================
    protected val themeColor: Int =
        CacheUtils.getThemeColor()

    protected var isDarkMode: Boolean = false

    // =========================
    // LoadSir
    // =========================
    private lateinit var mLoadService: LoadService<Any>

    protected val loadingDialog by lazy {
        ProgressHud(requireActivity())
    }

    /** ========== 当前状态 ========== */
    protected val currentState: STATE
        get() = mViewModel.viewState.value


    // =========================
    // 抽象方法（MVI 核心）
    // =========================

    /** 设置主题 */
    protected abstract fun setThemeColor(isDarkMode: Boolean)


    /** 渲染 State */
    protected abstract fun renderState(state: STATE)

    /** 处理 Effect */
    protected abstract fun handleEffect(effect: EFFECT)

    // =========================
    // Fragment 生命周期
    // =========================

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BindingReflex.reflexViewBinding(javaClass, layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 恢复状态
        mStatusHelper?.onRestoreInstanceStatus(savedInstanceState)

        // Router 注入
        try {
            GoRouter.getInstance().inject(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // ViewModel
        mViewModel = createViewModel()

        // EventBus
        if (javaClass.isAnnotationPresent(EventBusRegister::class.java)) {
            EventBusUtils.register(this)
        }

        // 主题
        isDarkMode = CacheUtils.getNormalDark()
        setThemeColor(isDarkMode)

        // 初始化 View
        mBinding.initView()

        // MVI 核心
        collectState()
        collectEffect()

        // 懒加载
        onVisible()
    }


    /**
     * 根据资源 id 获取一个 View 对象
     */
    override fun <V : View?> findViewById(@IdRes id: Int): V? {
        return mBinding.root.findViewById(id)
    }


    protected fun sendEvent(event: EVENT) {
        mViewModel.setEvent(event)
    }
    // =========================
    // MVI 收集（重点）
    // =========================

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.viewState.collectLatest {
                    renderState(it)
                }
            }
        }
    }

    private fun collectEffect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.effect.collectLatest {
                    handleEffect(it)
                }
            }
        }
    }



    protected fun <T> Flow<T>.collectInFragment(
        block: (T) -> Unit
    ) {
        collectKt(viewLifecycleOwner, block = block)
    }

    // =========================
    // 懒加载逻辑（保留你原来的）
    // =========================

    protected open fun lazyLoadData() {
        initRequestData()
    }

    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            lazyLoadData()
            isFirst = false
        }
    }

    // =========================
    // LoadSir（原样保留）
    // =========================

    protected fun requestLoading(view: View, tint: Int = themeColor) {
        if (!::mLoadService.isInitialized) {
            mLoadService = LoadSir.getDefault().register(view) {
                mLoadService.showCallback(
                    com.core.library_base.loadsir.LoadCallBack::class.java
                )
                reLoadData()
            }
        }
        mLoadService.showCallback(
            com.core.library_base.loadsir.LoadCallBack::class.java
        )
    }

    protected fun requestSuccess() {
        if (::mLoadService.isInitialized) {
            mLoadService.showSuccess()
        }
    }

    protected fun requestFailure() {
        if (::mLoadService.isInitialized) {
            mLoadService.showCallback(ErrorCallBack::class.java)
        }
    }

    protected fun requestEmptyData() {
        if (::mLoadService.isInitialized) {
            mLoadService.showCallback(EmptyCallBack::class.java)
        }
    }

    // =========================
    // 状态保存
    // =========================

    override fun onSaveInstanceState(outState: Bundle) {
        if (mStatusHelper == null) {
            mStatusHelper = FragmentStatusHelper(outState)
        } else {
            mStatusHelper?.onSaveInstanceState(outState)
        }
        super.onSaveInstanceState(outState)
    }

    override fun isRecreate(): Boolean =
        mStatusHelper?.isRecreate ?: false


    override fun onResume() {
        super.onResume()
        onVisible()
    }

    private class FragmentStatusHelper(savedInstanceState: Bundle? = null) :
        ViewRecreateHelper(savedInstanceState)

    // =========================
    // 销毁
    // =========================

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        if (javaClass.isAnnotationPresent(EventBusRegister::class.java)) {
            EventBusUtils.unRegister(this)
        }
        super.onDestroy()
    }
}