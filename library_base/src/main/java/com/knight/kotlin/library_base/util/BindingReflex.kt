package com.knight.kotlin.library_base.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType
import java.util.Objects

/**
 * Author:Knight
 * Time:2021/12/16 15:00
 * Description:BindingReflex
 * 用于反射获取ViewModel 和ViewBinding
 */
object BindingReflex {

    /**
     * 反射获取ViewBinding
     * @param <V> ViewBinding 实现类
     * @param aClass 当前类
     * @param from layouinflater
     */
    @Suppress("UNCHECKED_CAST")
    fun <V : ViewBinding> reflexViewBinding(aClass: Class<*>, from: LayoutInflater?): V {
        try {
             val actualTypeArguments =
                 (Objects.requireNonNull(aClass.genericSuperclass) as ParameterizedType).actualTypeArguments
             for (i in actualTypeArguments.indices) {
                 val tClass:Class<*>
                 try {
                     tClass = actualTypeArguments[i] as Class<*>
                 } catch (e:Exception) {
                     continue
                 }
                 if (ViewBinding::class.java.isAssignableFrom(tClass)) {
                     val inflate = tClass.getMethod("inflate",LayoutInflater::class.java)
                     return inflate.invoke(null,from) as V
                 }
             }
            return reflexViewBinding(aClass.superclass,from)
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
            throw RuntimeException(e.message)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            throw RuntimeException(e.message)
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
            throw RuntimeException(e.targetException.message)
        }

    }


    /**
     *
     * 反射获取ViewBinding
     */
    @Suppress("UNCHECKED_CAST")
    private fun <V : ViewBinding> reflexViewBinding(
        aClass:Class<*>,
        from:LayoutInflater?,
        viewGroup: ViewGroup?,
        b:Boolean
    ):V {
        try {
            val actualTypeArguments =
                (Objects.requireNonNull(aClass.genericSuperclass) as ParameterizedType).actualTypeArguments
            for (i in actualTypeArguments.indices) {
                val tClass:Class<*>
                try {
                    tClass = actualTypeArguments[i] as Class<*>
                } catch (e : Exception){
                    continue
                }
                if (ViewBinding::class.java.isAssignableFrom(tClass)) {
                    val inflater = tClass.getDeclaredMethod(
                        "inflate",
                        LayoutInflater::class.java,
                        ViewGroup::class.java,
                        Boolean::class.javaPrimitiveType
                    )
                    return inflater.invoke(null,from,viewGroup,b) as V
                }
            }
            return reflexViewBinding<ViewBinding>(aClass.superclass,from,viewGroup,b) as V
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e:IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        throw RuntimeException("ViewBinding初始化失败")
    }

    /**
     * 反射获取ViewModel
     * @param <VM> ViewModel 实现类
     * @param aClass 当前class
     * @param owner 生命周期管理
     * @return ViewModel实例
     */
    @Suppress("UNCHECKED_CAST")
    private fun <VM: ViewModel> reflexViewModel(aClass: Class<*>,owner : ViewModelStoreOwner) :VM {
        try {
            val actualTypeArguments =
                (Objects.requireNonNull(aClass.genericSuperclass) as ParameterizedType).actualTypeArguments
            for (i in actualTypeArguments.indices) {
                val tClass:Class<*>
                try {
                    tClass = actualTypeArguments[i] as Class<*>
                } catch (e:Exception) {
                    continue
                }
                if (ViewModel::class.java.isAssignableFrom(tClass)) {
                    return ViewModelProvider(owner)[tClass as Class<VM>]
                }
            }
            return reflexViewModel(aClass.superclass,owner)
        } catch (e:Exception){
            e.printStackTrace()
        }
        throw RuntimeException("ViewModel初始化失败")
    }


    /**
     * 反射获取ViewModel，这个方法只提供
     * 如果fragment的父Activity有相同的ViewModel 那么生成的ViewModel将会是同一个实例，做到Fragment与Activity的数据同步
     * 或者说是同一个Activity中的多个Fragment同步使用用一个ViewMode达到数据之间的同步
     */
    @Suppress("UNCHECKED_CAST")
    private fun <VM:ViewModel> reflexViewModelShared(aClass: Class<*>,fragment: Fragment):VM {
        try {
            val actualTypeArguments =
                (Objects.requireNonNull(aClass.genericSuperclass) as ParameterizedType).actualTypeArguments
            for (i in actualTypeArguments.indices) {
                val tClass:Class<*>
                try {
                    tClass = actualTypeArguments[i] as Class<*>
                } catch (e:Exception){
                    continue
                }
                if (ViewModel::class.java.isAssignableFrom(tClass)) {
                    return ViewModelProvider(fragment.requireActivity())[tClass as Class<VM>]
                }
            }
            return reflexViewModelShared(aClass.superclass,fragment)
        } catch (e:Exception) {
            e.printStackTrace()
        }
        throw RuntimeException("ViewModel初始化失败")
    }
}