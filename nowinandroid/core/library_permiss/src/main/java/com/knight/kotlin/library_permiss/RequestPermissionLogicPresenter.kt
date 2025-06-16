package com.knight.kotlin.library_permiss

import android.app.Activity
import android.os.Build
import android.text.TextUtils
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.AndroidVersionTools.getCurrentAndroidVersionCode
import com.knight.kotlin.library_permiss.AndroidVersionTools.isAndroid6
import com.knight.kotlin.library_permiss.PermissionHelper.findAndroidVersionByPermission
import com.knight.kotlin.library_permiss.PermissionHelper.getBackgroundPermissionByGroup
import com.knight.kotlin.library_permiss.PermissionHelper.getDangerousPermissionGroup
import com.knight.kotlin.library_permiss.PermissionHelper.getMaxIntervalTimeByPermissions
import com.knight.kotlin.library_permiss.PermissionHelper.queryDangerousPermissionGroupType
import com.knight.kotlin.library_permiss.PermissionHelper.queryForegroundPermissionByBackgroundPermission
import com.knight.kotlin.library_permiss.PermissionTaskHandler.sendTask
import com.knight.kotlin.library_permiss.listener.OnPermissionCallback
import com.knight.kotlin.library_permiss.listener.OnPermissionDescription
import com.knight.kotlin.library_permiss.listener.OnPermissionFlowCallback
import com.knight.kotlin.library_permiss.listener.OnPermissionInterceptor
import com.knight.kotlin.library_permiss.permissions.PermissionApi.areAllDangerousPermission
import com.knight.kotlin.library_permiss.permissions.PermissionApi.isBackgroundPermission
import com.knight.kotlin.library_permiss.permissions.PermissionApi.isDoNotAskAgainPermissions
import com.knight.kotlin.library_permiss.permissions.PermissionApi.isGrantedPermission
import com.knight.kotlin.library_permiss.permissions.PermissionApi.isGrantedPermissions
import com.knight.kotlin.library_permiss.permissions.PermissionApi.isSpecialPermission
import com.knight.kotlin.library_permiss.permissions.PermissionType
import com.knight.kotlin.library_permiss.utils.PermissionUtils


/**
 * @Description
 * @Author knight
 * @Time 2025/6/8 20:18
 *
 */

internal class RequestPermissionLogicPresenter(
    activity: Activity,
    requestPermissions: List<String>,
    fragmentFactory: PermissionFragmentFactory<*, *>,
    permissionInterceptor: OnPermissionInterceptor,
    permissionDescription: OnPermissionDescription,
    callback: OnPermissionCallback
) {
    
    private val mActivity: Activity = activity

    
    private val mRequestPermissions = requestPermissions

    
    private val mFragmentFactory = fragmentFactory

    
    private val mPermissionInterceptor = permissionInterceptor

    
    private val mPermissionDescription = permissionDescription

    
    private val mCallBack = callback

    /**
     * 开始权限请求
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun request() {
        if (mRequestPermissions.isEmpty()) {
            return
        }

        val unauthorizedPermissions = getUnauthorizedPermissions(mActivity, mRequestPermissions)
        if (unauthorizedPermissions.isEmpty()) {
            // 证明没有权限可以请求，直接处理权限请求结果
            handlePermissionRequestResult()
            return
        }

        var firstPermissions: List<String?>? = null
        val iterator = unauthorizedPermissions.iterator()

        while (iterator.hasNext() && (firstPermissions == null || firstPermissions.isEmpty())) {
            firstPermissions = iterator.next()
        }
        if (firstPermissions == null || firstPermissions.isEmpty()) {
            // 证明没有权限可以请求，直接处理权限请求结果
            handlePermissionRequestResult()
            return
        }

        val activity: Activity = mActivity
        val fragmentFactory = mFragmentFactory
        val permissionDescription = mPermissionDescription

        // 发起权限请求
        requestPermissions(
            activity,
            firstPermissions,
            fragmentFactory,
            permissionDescription,
            object : Runnable {
                override fun run() {
                    var nextPermissions: List<String?>? = null
                    while (iterator.hasNext() && (nextPermissions == null || nextPermissions.isEmpty())) {
                        nextPermissions = iterator.next()
                    }

                    if (nextPermissions == null || nextPermissions.isEmpty()) {
                        // 证明请求已经全部完成，延迟发送权限处理结果
                        postDelayedHandlerRequestPermissionsResult()
                        return
                    }

                    // 如果下一个请求的权限是后台权限
                    if (nextPermissions.size == 1 && isBackgroundPermission(
                            nextPermissions[0]!!
                        )
                    ) {
                        val foregroundPermissions = queryForegroundPermissionByBackgroundPermission(
                            nextPermissions[0]!!
                        )
                        // 如果这个后台权限对应的前台权限没有申请成功，则不要去申请后台权限，因为申请了也没有用，系统肯定不会给通过的
                        // 如果这种情况下还硬要去申请，等下还可能会触发权限说明弹窗，但是没有实际去申请权限的情况
                        if (foregroundPermissions != null && !foregroundPermissions.isEmpty() && !isGrantedPermissions(
                                activity,
                                foregroundPermissions
                            )
                        ) {
                            // 直接进行下一轮申请
                            this.run()
                            return
                        }
                    }

                    val finalPermissions: List<String?> = nextPermissions
                    val maxWaitTimeByPermissions = getMaxIntervalTimeByPermissions(nextPermissions)
                    if (maxWaitTimeByPermissions == 0) {
                        requestPermissions(
                            activity, finalPermissions, fragmentFactory, permissionDescription,
                            this
                        )
                    } else {
                        sendTask({
                            requestPermissions(
                                activity, finalPermissions, fragmentFactory, permissionDescription,
                                this
                            )
                        }, maxWaitTimeByPermissions.toLong())
                    }
                }
            })
    }

    /**
     * 延迟处理权限请求结果
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun postDelayedHandlerRequestPermissionsResult() {
        sendTask({ this.handlePermissionRequestResult() }, 100)
    }

    /**
     * 延迟解锁 Activity 方向
     */
    private fun postDelayedUnlockActivityOrientation(activity: Activity) {
        // 延迟执行是为了让外层回调中的代码能够顺序执行完成
        sendTask({ ActivityOrientationControl.unlockActivityOrientation(activity) }, 100)
    }

    /**
     * 处理权限请求结果
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun handlePermissionRequestResult() {
        val callback = mCallBack

        val interceptor = mPermissionInterceptor

        val requestPermissions = mRequestPermissions

        val activity: Activity = mActivity

        // 如果当前 Activity 不可用，就不继续往下执行代码
        if (PermissionUtils.isActivityUnavailable(activity)) {
            return
        }

        val grantedPermissions: MutableList<String> = ArrayList(requestPermissions.size)
        val deniedPermissions: MutableList<String> = ArrayList(requestPermissions.size)
        // 遍历请求的权限，并且根据权限的授权状态进行分类
        for (permission in requestPermissions) {
            if (isGrantedPermission(activity, permission!!, false)) {
                grantedPermissions.add(permission)
            } else {
                deniedPermissions.add(permission)
            }
        }

        // 如果请求成功的权限集合大小和请求的数组一样大时证明权限已经全部授予
        if (grantedPermissions.size == requestPermissions.size) {
            // 代表申请的所有的权限都授予了
            interceptor.grantedPermissionRequest(
                activity,
                requestPermissions,
                grantedPermissions,
                true,
                callback
            )
            // 权限申请结束
            interceptor.finishPermissionRequest(activity, requestPermissions, false, callback)
            // 延迟解锁 Activity 屏幕方向
            postDelayedUnlockActivityOrientation(activity)
            return
        }

        // 代表申请的权限中有不同意授予的，如果有某个权限被永久拒绝就返回 true 给开发人员，让开发者引导用户去设置界面开启权限
        interceptor.deniedPermissionRequest(
            activity, requestPermissions, deniedPermissions,
            isDoNotAskAgainPermissions(activity, deniedPermissions), callback
        )

        // 证明还有一部分权限被成功授予，回调成功接口
        if (!grantedPermissions.isEmpty()) {
            interceptor.grantedPermissionRequest(
                activity,
                requestPermissions,
                grantedPermissions,
                false,
                callback
            )
        }

        // 权限申请结束
        interceptor.finishPermissionRequest(activity, requestPermissions, false, callback)

        // 延迟解锁 Activity 屏幕方向
        postDelayedUnlockActivityOrientation(activity)
    }

    companion object {
        /**
         * 获取未授权的危险权限
         */
        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        private fun getUnauthorizedPermissions(
            activity: Activity,
            requestPermissions: List<String>
        ): List<List<String?>> {
            // 未授权的权限列表
            val unauthorizedPermissions: MutableList<List<String?>> =
                ArrayList(requestPermissions.size)
            // 已处理的权限列表
            val alreadyDonePermissions: MutableList<String> = ArrayList(requestPermissions.size)
            // 遍历需要请求的权限列表
            for (permission in requestPermissions) {
                // 如果这个权限在前面已经处理过了，就不再处理

                if (PermissionUtils.containsPermission(alreadyDonePermissions, permission)) {
                    continue
                }
                alreadyDonePermissions.add(permission)

                // 如果这个权限已授权，就不纳入申请的范围内
                if (isGrantedPermission(activity, permission!!)) {
                    continue
                }

                // 如果当前设备的版本还没有出现过这个特殊权限，并且权限还没有授权的情况，证明这个特殊权限有向下兼容的权限
                // 这种情况就不要跳转到权限设置页，例如 MANAGE_EXTERNAL_STORAGE 权限
                if (getCurrentAndroidVersionCode() < findAndroidVersionByPermission(permission)) {
                    continue
                }

                // ---------------------------------- 下面处理特殊权限的逻辑 ------------------------------------------ //
                if (isSpecialPermission(permission)) {
                    // 如果这是一个特殊权限，那么就作为单独的一次权限进行处理
                    unauthorizedPermissions.add(PermissionUtils.asArrayList(permission))
                    continue
                }

                // ---------------------------------- 下面处理危险权限的逻辑 ------------------------------------------ //

                // 查询危险权限所在的权限组类型
                val permissionGroupType = queryDangerousPermissionGroupType(
                    permission
                )
                if (permissionGroupType == null) {
                    // 如果这个权限没有组别，就直接单独做为一次权限申请
                    unauthorizedPermissions.add(PermissionUtils.asArrayList(permission))
                    continue
                }

                // 如果这个权限有组别，那么就获取这个组别的全部权限
                val dangerousPermissions: MutableList<String> = ArrayList(
                    getDangerousPermissionGroup(permissionGroupType)
                )
                // 对这个组别的权限进行逐个遍历
                val iterator = dangerousPermissions.iterator()
                while (iterator.hasNext()) {
                    val dangerousPermission = iterator.next()
                    // 如果这个危险权限在前面已经处理过了，就不再处理
                    if (PermissionUtils.containsPermission(
                            alreadyDonePermissions,
                            dangerousPermission
                        )
                    ) {
                        continue
                    }
                    alreadyDonePermissions.add(dangerousPermission)

                    if (findAndroidVersionByPermission(dangerousPermission) >
                        getCurrentAndroidVersionCode()
                    ) {
                        // 如果申请的权限是新系统才出现的，但是当前是旧系统运行，就从权限组中移除
                        iterator.remove()
                        continue
                    }

                    // 判断申请的权限列表中是否有包含权限组中的权限
                    if (!PermissionUtils.containsPermission(
                            requestPermissions,
                            dangerousPermission
                        )
                    ) {
                        // 如果不包含的话，就从权限组中移除
                        iterator.remove()
                    }
                }

                // 如果这个权限组为空，证明剩余的权限是在高版本系统才会出现，这里无需再次发起申请
                if (dangerousPermissions.isEmpty()) {
                    continue
                }

                // 如果这个权限组已经全部授权，就不纳入申请的范围内
                if (isGrantedPermissions(activity, dangerousPermissions)) {
                    continue
                }

                // 判断申请的权限组是否包含后台权限（例如后台定位权限，后台传感器权限），如果有的话，不能在一起申请，需要进行拆分申请
                val backgroundPermission = getBackgroundPermissionByGroup(dangerousPermissions)
                if (TextUtils.isEmpty(backgroundPermission)) {
                    // 如果不包含后台权限，则直接添加到待申请的列表
                    unauthorizedPermissions.add(dangerousPermissions)
                    continue
                }

                val foregroundPermissions: MutableList<String> = ArrayList(dangerousPermissions)
                foregroundPermissions.remove(backgroundPermission)

                // 添加前台权限（前提得是没有授权）
                if (!foregroundPermissions.isEmpty() &&
                    !isGrantedPermissions(activity, foregroundPermissions)
                ) {
                    unauthorizedPermissions.add(foregroundPermissions)
                }
                // 添加后台权限
                unauthorizedPermissions.add(PermissionUtils.asArrayList(backgroundPermission))
            }

            return unauthorizedPermissions
        }

        /**
         * 发起一次权限请求
         */
        private fun requestPermissions(
            activity: Activity, permissions: List<String?>,
            fragmentFactory: PermissionFragmentFactory<*, *>,
            permissionDescription: OnPermissionDescription,
            finishRunnable: Runnable
        ) {
            if (permissions.isEmpty()) {
                finishRunnable.run()
                return
            }

            val permissionType: PermissionType =
                if (areAllDangerousPermission(permissions)) PermissionType.DANGEROUS else PermissionType.SPECIAL
            if (permissionType === PermissionType.DANGEROUS && !isAndroid6()) {
                // 如果是 Android 6.0 以下，没有危险权限的概念
                finishRunnable.run()
                return
            }

            val continueRequestRunnable = Runnable {
                fragmentFactory.createAndCommitFragment(
                    permissions,
                    permissionType,
                    object : OnPermissionFlowCallback {
                        override fun onRequestPermissionNow() {
                            permissionDescription.onRequestPermissionStart(activity, permissions)
                        }

                        override fun onRequestPermissionFinish() {
                            permissionDescription.onRequestPermissionEnd(activity, permissions)
                            finishRunnable.run()
                        }

                        override fun onRequestPermissionAnomaly() {
                            permissionDescription.onRequestPermissionEnd(activity, permissions)
                        }
                    })
            }

            permissionDescription.askWhetherRequestPermission(
                activity,
                permissions,
                continueRequestRunnable,
                finishRunnable
            )
        }
    }
}