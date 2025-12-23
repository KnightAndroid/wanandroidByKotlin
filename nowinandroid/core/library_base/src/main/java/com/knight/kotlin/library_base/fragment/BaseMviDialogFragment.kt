package com.knight.kotlin.library_base.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.core.library_base.ktx.ClickAction
import com.core.library_base.util.BindingReflex
import com.core.library_base.util.ViewRecreateHelper
import com.core.library_base.view.BaseView
import com.core.library_base.vm.BaseMviViewModel
import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState
import com.knight.kotlin.library_base.ktx.createViewModel
import com.core.library_base.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.reflect.Field

/**
 * @Description
 * @Author knight
 * @Time 2025/12/23 21:25
 *
 */
abstract class BaseMviDialogFragment<
        VB : ViewBinding,
        VM : BaseMviViewModel<EVENT, STATE, EFFECT>,
        EVENT : ViewEvent,
        STATE : ViewState,
        EFFECT : ViewSideEffect
        > : DialogFragment(),
    BaseView<VB>,
    ClickAction {

    /**
     * 私有的 ViewBinding
     */
    private var _binding: VB? = null
    protected val mBinding get() = _binding!!

    /**
     * ViewModel
     */
    protected lateinit var mViewModel: VM

    /**
     * fragment 状态保存工具类
     */
    private var mStatusHelper: FragmentStatusHelper? = null

    /**
     * fragment 状态保存帮助类
     */
    private class FragmentStatusHelper(savedInstanceState: Bundle? = null) :
        ViewRecreateHelper(savedInstanceState)

    override fun isRecreate(): Boolean =
        mStatusHelper?.isRecreate ?: false

    override fun onSaveInstanceState(outState: Bundle) {
        if (mStatusHelper == null) {
            mStatusHelper = FragmentStatusHelper(outState)
        } else {
            mStatusHelper?.onSaveInstanceState(outState)
        }
        super.onSaveInstanceState(outState)
    }

    /**
     * Dialog 显示方向
     */
    protected abstract fun getGravity(): Int

    /**
     * 是否屏蔽点击遮罩层消失弹窗
     */
    protected abstract fun cancelOnTouchOutSide(): Boolean

    /**
     * 渲染 State
     */
    protected abstract fun renderState(state: STATE)

    /**
     * 处理 Effect
     */
    protected abstract fun handleEffect(effect: EFFECT)

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

        mBinding.initView()

        collectState()
        collectEffect()

        initRequestData()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setWindowAnimations(R.style.base_dialog_anim)

        mViewModel = createViewModel()

        dialog.setCanceledOnTouchOutside(cancelOnTouchOutSide())
        if (!cancelOnTouchOutSide()) {
            dialog.setCancelable(false)
            dialog.setOnKeyListener { _, keyCode, _ ->
                keyCode == KeyEvent.KEYCODE_BACK
            }
        }
        getGravity()
        return dialog
    }

    /**
     * 根据资源 id 获取 View
     */
    override fun <V : View?> findViewById(@IdRes id: Int): V? {
        return mBinding.root.findViewById(id)
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val params = it.window?.attributes
            params?.width = ViewGroup.LayoutParams.MATCH_PARENT
            params?.height = ViewGroup.LayoutParams.WRAP_CONTENT
            params?.gravity = getGravity()
            it.window?.attributes = params
        }
    }

    /**
     * 发送 MVI Event
     */
    protected fun sendEvent(event: EVENT) {
        mViewModel.setEvent(event)
    }

    /**
     * 收集 State
     */
    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.viewState.collectLatest {
                    renderState(it)
                }
            }
        }
    }

    /**
     * 收集 Effect
     */
    private fun collectEffect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.effect.collectLatest {
                    handleEffect(it)
                }
            }
        }
    }

    open fun showAllowingStateLoss(manager: FragmentManager, tag: String?) {
        try {
            val dismissed: Field =
                DialogFragment::class.java.getDeclaredField("mDismissed")
            dismissed.isAccessible = true
            dismissed.set(this, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            val shown: Field =
                DialogFragment::class.java.getDeclaredField("mShownByMe")
            shown.isAccessible = true
            shown.set(this, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val ft: FragmentTransaction = manager.beginTransaction()
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}