package com.knight.kotlin.library_permiss.start

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.annotation.IntRange
import androidx.fragment.app.Fragment
import com.knight.kotlin.library_permiss.tools.PermissionSettingPage
import com.knight.kotlin.library_permiss.tools.PermissionSettingPage.getAndroidSettingsIntent
import com.knight.kotlin.library_permiss.tools.PermissionUtils


/**
 * @author created by luguian
 * @organize
 * @Date 2025/7/11 9:29
 * @descript: 跳转 Activity 代理类
 */
object StartActivityAgent {
    fun startActivity(
         context: Context,
         intentList: MutableList<Intent>
    ) {
        startActivity(context, StartActivityDelegateByContext(context), intentList)
    }

    fun startActivity(
        activity: Activity,
        intentList: MutableList<Intent>
    ) {
        startActivity(activity, StartActivityDelegateByActivity(activity), intentList)
    }

    @Suppress("deprecation")
    fun startActivity(
        fragment: Fragment,
        intentList: MutableList<Intent>
    ) {
        fragment.getActivity()?.let { startActivity(it, StartActivityDelegateByFragmentApp(fragment), intentList) }
    }



    fun startActivity(
         context: Context,
         delegate: IStartActivityDelegate,
         intentList: MutableList<Intent>
    ) {
        var iterator: MutableIterator<Intent> = intentList.iterator()
        while (iterator.hasNext()) {
            val intent = iterator.next()
            if (PermissionUtils.areActivityIntent(context, intent)) {
                continue
            }
            // 移除那些不存在的 Intent 对象，这样做的好处是：
            // 1. 拿不存在的 Intent 去跳转必定是失败的（如果项目适配了 Android 11，需要注意适配软件包可见性的特性）
            // 2. 在 Debug 代码调试的时候，可以很直观看出来有哪些 Intent 是存在的，也可以比较过滤前后的 Intent 列表
            iterator.remove()
        }


        // 当所有的 Intent 都不存在的时候，那么就默认添加一个 Android 系统设置的 Intent，这样写的原因如下：
        // 不至于用户一点申请权限就立马提示失败，用户会一头雾水，这样的体验太差了，最起码跳转一下 Android 系统设置页，这样效果会好很多
        if (intentList.isEmpty()) {
            intentList.add(getAndroidSettingsIntent())
        }


        // 由于 Iterator 接口中没有重置索引的方法，所以这里只能重新获取一次 Iterator 对象
        iterator = intentList.iterator()
        while (iterator.hasNext()) {
            val intent = iterator.next() ?: continue
            try {
                delegate.startActivity(intent)
                // 跳转成功，结束循环
                break
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    fun startActivityForResult(
         activity: Activity,
         intentList: MutableList<Intent>,
        @IntRange(from = 1, to = 65535) requestCode: Int
    ) {
        startActivityForResult(activity, StartActivityDelegateByActivity(activity), intentList, requestCode)
    }

    @Suppress("deprecation")
    fun startActivityForResult(
         fragment: Fragment,
         intentList: MutableList<Intent>,
        @IntRange(from = 1, to = 65535) requestCode: Int
    ) {
        fragment.getActivity()?.let { startActivityForResult(it, StartActivityDelegateByFragmentApp(fragment), intentList, requestCode) }
    }


    @JvmOverloads
    fun startActivityForResult(
         context: Context,
         delegate: IStartActivityDelegate,
         intentList: MutableList<Intent>,
        @IntRange(from = 1, to = 65535) requestCode: Int,
         ignoreActivityResultCallback: Runnable? = null
    ) {
        var iterator = intentList.iterator()
        while (iterator.hasNext()) {
            val intent = iterator.next()
            if (PermissionUtils.areActivityIntent(context, intent)) {
                continue
            }
            // 移除那些不存在的 Intent 对象，这样做的好处是：
            // 1. 拿不存在的 Intent 去跳转必定是失败的（如果项目适配了 Android 11，需要注意适配软件包可见性的特性）
            // 2. 在 Debug 代码调试的时候，可以很直观看出来有哪些 Intent 是存在的，也可以比较过滤前后的 Intent 列表
            iterator.remove()
        }

        // 当所有的 Intent 都不存在的时候，那么就默认添加一个 Android 系统设置的 Intent，这样写的原因如下：
        // 1. 不至于用户一点申请权限就立马提示失败，用户会一头雾水，这样的体验太差了，最起码跳转一下 Android 系统设置页，这样效果会好很多
        // 2. 假设连 Android 系统设置页都跳转失败了，但是这样做可以让系统触发回调 onActivityResult 方法，才使得整个权限请求流程形成闭环
        if (intentList.isEmpty()) {
            intentList.add(PermissionSettingPage.getAndroidSettingsIntent())
        }

        // 由于 Iterator 接口中没有重置索引的方法，所以这里只能重新获取一次 Iterator 对象
        iterator = intentList.iterator()
        while (iterator.hasNext()) {
            val intent = iterator.next()
            try {
                delegate.startActivityForResult(intent!!, requestCode)
                // 跳转成功，结束循环
                break
            } catch (e: Exception) {
                e.printStackTrace()
                // 如果下一个 Intent 不为空才去触发失败结果的回调，这是因为如果下一个 Intent 为空，则证明已经没有下一个 Intent 可以再试了，
                // 那么就不需要记录这次跳转失败的次数，这样前面 startActivityForResult 失败就会导致系统触发 onActivityResult 回调，形成闭环
                if (iterator.hasNext() && ignoreActivityResultCallback != null) {
                    ignoreActivityResultCallback.run()
                }
            }
        }
    }
}