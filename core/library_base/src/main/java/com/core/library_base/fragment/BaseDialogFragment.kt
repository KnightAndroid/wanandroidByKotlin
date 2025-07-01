package com.core.library_base.fragment

import android.app.Dialog
import android.content.DialogInterface
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
import androidx.viewbinding.ViewBinding
import com.core.library_base.ktx.ClickAction
import com.core.library_base.ktx.createViewModel
import com.core.library_base.util.BindingReflex
import com.core.library_base.util.ViewRecreateHelper
import com.core.library_base.view.BaseView
import com.core.library_base.vm.BaseViewModel
import com.knight.kotlin.library_base.R
import java.lang.reflect.Field


/**
 * Author:Knight
 * Time:2021/12/30 11:09
 * Description:BaseDialogFragment
 */
abstract class BaseDialogFragment<VB: ViewBinding,VM: BaseViewModel> : DialogFragment(),
    BaseView<VB>, ClickAction {

    /**
     * 私有的ViewBinding
     */
    private var _binding:VB? = null
    protected val mBinding get() = _binding!!

    lateinit var mViewModel:VM

    /**
     * fragment状态保存工具类
     *
     */
    private var mStatusHelper: FragmentStatusHelper? = null

    /**
     * - fragment状态保存帮助类
     * - 暂时没有其他需要保存的 -- 空继承
     *
     */
    private class FragmentStatusHelper(savedInstanceState:Bundle? = null) :
        ViewRecreateHelper(savedInstanceState)

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
     *
     * 方向
     * @return
     */
    protected abstract fun getGravity(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        _binding = BindingReflex.reflexViewBinding(javaClass,layoutInflater)
        return _binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.initView()
        initObserver()
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
            dialog.setOnKeyListener(DialogInterface.OnKeyListener { dialog, keyCode, event ->
                keyCode == KeyEvent.KEYCODE_BACK
            })
        }
        getGravity()
        return dialog
    }


    /**
     *
     * 是否屏蔽点击遮罩层消失弹窗
     */
    abstract fun cancelOnTouchOutSide() : Boolean

    /**
     * 根据资源 id 获取一个 View 对象
     */
    override fun <V : View?> findViewById(@IdRes id: Int): V? {
        return mBinding.root.findViewById(id)
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        dialog?.let {
            val params = it.window?.attributes
            params?.width = ViewGroup.LayoutParams.MATCH_PARENT
            params?.height = ViewGroup.LayoutParams.WRAP_CONTENT
            params?.gravity = getGravity()
            it.window?.attributes = params
        }
    }

    open fun showAllowingStateLoss(manager: FragmentManager, tag: String?) {
        try {
            val dismissed: Field = DialogFragment::class.java.getDeclaredField("mDismissed")
            dismissed.isAccessible = true
            dismissed.set(this, false)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        try {
            val shown: Field = DialogFragment::class.java.getDeclaredField("mShownByMe")
            shown.isAccessible = true
            shown.set(this, true)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
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