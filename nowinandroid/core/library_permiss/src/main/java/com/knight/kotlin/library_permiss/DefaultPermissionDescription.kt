package com.knight.kotlin.library_permiss

import android.app.Activity
import com.knight.kotlin.library_permiss.permission.base.IPermission


/**
 * @author created by luguian
 * @organize
 * @Date 2025/7/11 9:57
 * @descript:权限说明默认实现
 */
internal class DefaultPermissionDescription : OnPermissionDescription {
    override fun askWhetherRequestPermission(
        activity: Activity, requestPermissions: List<IPermission>,
        continueRequestRunnable: Runnable, breakRequestRunnable: Runnable
    ) {
        // 继续执行请求任务
        continueRequestRunnable.run()
    }

    override fun onRequestPermissionStart( activity: Activity,  requestPermissions: List<IPermission>) {
        // default implementation ignored
    }

    override fun onRequestPermissionEnd( activity: Activity,  requestPermissions: List<IPermission>) {
        // default implementation ignored

    }


}