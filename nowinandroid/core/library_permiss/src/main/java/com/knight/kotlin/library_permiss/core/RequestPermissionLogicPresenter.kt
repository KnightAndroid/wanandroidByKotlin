package com.knight.kotlin.library_permiss.core

import android.app.Activity
import com.knight.kotlin.library_permiss.OnPermissionCallback
import com.knight.kotlin.library_permiss.OnPermissionDescription
import com.knight.kotlin.library_permiss.OnPermissionInterceptor
import com.knight.kotlin.library_permiss.fragment.factory.PermissionFragmentFactory
import com.knight.kotlin.library_permiss.manager.ActivityOrientationManager
import com.knight.kotlin.library_permiss.permission.PermissionType
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.tools.PermissionApi
import com.knight.kotlin.library_permiss.tools.PermissionApi.areAllDangerousPermission
import com.knight.kotlin.library_permiss.tools.PermissionApi.isDoNotAskAgainPermissions
import com.knight.kotlin.library_permiss.tools.PermissionTaskHandler
import com.knight.kotlin.library_permiss.tools.PermissionTaskHandler.sendTask
import com.knight.kotlin.library_permiss.tools.PermissionUtils
import com.knight.kotlin.library_permiss.tools.PermissionVersion


/**
 * @Description
 * @Author knight
 * @Time 2025/6/8 20:18
 *
 */

class RequestPermissionLogicPresenter(
    activity: Activity,
    requestPermissions: List<IPermission>,
    fragmentFactory: PermissionFragmentFactory<*, *>,
    permissionInterceptor: OnPermissionInterceptor,
    permissionDescription: OnPermissionDescription,
    callback: OnPermissionCallback
) {
    
    private val mActivity = activity

    
    private val mRequestPermissions: List<IPermission> = requestPermissions

    
    private val mFragmentFactory = fragmentFactory

    
    private val mPermissionInterceptor = permissionInterceptor

    
    private val mPermissionDescription = permissionDescription

    
    private val mCallBack = callback

    /**
     * 开始权限请求
     */
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

        val iterator = unauthorizedPermissions.iterator()
        var firstPermissions: List<IPermission>? = null
        while (iterator.hasNext() && (firstPermissions == null || firstPermissions.isEmpty())) {
            firstPermissions = iterator.next()
        }

        if (firstPermissions == null || firstPermissions.isEmpty()) {
            // 证明没有权限可以请求，直接处理权限请求结果
            handlePermissionRequestResult()
            return
        }

        val activity = mActivity
        val fragmentFactory = mFragmentFactory
        val permissionDescription = mPermissionDescription

        // 锁定 Activity 屏幕方向
        ActivityOrientationManager.lockActivityOrientation(activity)

        // 发起权限请求
        requestPermissions(
            activity,
            firstPermissions,
            fragmentFactory,
            permissionDescription,
            object : Runnable {
                override fun run() {
                    var nextPermissions: List<IPermission>? = null
                    while (iterator.hasNext()) {
                        nextPermissions = iterator.next()

                        if (nextPermissions == null || nextPermissions.isEmpty()) {
                            // 获取到的这个权限列表不符合要求，继续循环获取，虽然前面已经筛选过一波了，
                            // 理论上不会走到这里来，但是为了代码的严谨性，这里还是要加一下判断
                            continue
                        }

                        // 这里解释一下为什么之前已经判断过权限是否授予了，还要在这里再次判断，这难道不是多此一举吗？
                        // 主要为了适配几种极端场景：

                        // 1. 用户发起相机权限和悬浮窗权限申请，在系统弹出相机权限授权框的时候，用户并没有授予，而是搞了一个骚操作，
                        //    直接跑去系统设置中，找到当前 App 的应用详情页的悬浮窗权限选项，然后就直接授予悬浮窗权限，最后再回到 App 上面，
                        //    此时系统还在傻傻等待用户授予相机权限，等用户授予了相机权限后，此时框架下一个申请的权限就是悬浮窗权限，
                        //    但是前面用户已经授予了悬浮窗权限，如果不在这里再次判断权限是否授予，就会导致一个问题，框架仍会跳转到悬浮窗设置页。

                        // 2. 偶然的一次测试，发现在 Android 12 的模拟器上申请前台定位权限（包含模糊定位和精确定位）和后台定位权限有一个问题，
                        //    用户在授予定位权限时，故意选中《大致位置》（系统默认是帮你选中《确切位置》），这样前台定位权限其实不算申请成功，
                        //    因为选中《大致位置》会导致精确定位权限没有授予；反之则不会。
                        //    用户接着又去授予了后台权限，但如果我们不再次判断是否授予，就会发起无效的后台权限请求或弹窗。

                        // 总结：第一个请求的权限列表中间不会有延迟，用户没机会干其他事，权限状态可靠；
                        //      但后续的权限就不一定了，所以需要再次判断。
                        if (PermissionApi.isGrantedPermissions(activity, nextPermissions)) {
                            // 将下一个要请求权限列表置空，表示不会请求它
                            nextPermissions = null
                            continue
                        }

                        // 如果代码走到这里来，则证明下一个请求的权限列表是符合要求的，
                        // 这里使用 break 跳出循环，然后进行下一步操作（权限请求）
                        break
                    }

                    if (nextPermissions == null || nextPermissions.isEmpty()) {
                        // 证明请求已经全部完成，延迟发送权限处理结果
                        postDelayedHandlerRequestPermissionsResult()
                        return
                    }

                    // 获取下一个要申请权限列表中的首个权限
                    val firstNextPermission = nextPermissions[0]

                    // 如果下一个请求的权限是后台权限
                    if (firstNextPermission.isBackgroundPermission(activity)) {
                        val foregroundPermissions = firstNextPermission.getForegroundPermissions(activity)
                        var grantedForegroundPermission = false

                        // 如果这个后台权限对应的前台权限没有申请成功，则不要去申请后台权限，因为申请了也没有用，系统肯定不会给通过的
                        // 如果这种情况下还硬要去申请，等下还可能会触发权限说明弹窗，但是没有实际去申请权限的情况
                        if (!foregroundPermissions.isNullOrEmpty()) {
                            for (foregroundPermission in foregroundPermissions) {
                                if (!foregroundPermission.isGrantedPermission(activity)) continue
                                // 所有的前台权限中，只要有任一个授权了，就算它是前台权限是申请通过的
                                grantedForegroundPermission = true
                                break
                            }
                        }

                        if (!grantedForegroundPermission) {
                            // 如果前台权限没有授予，就不去申请后台权限，直接进行下一轮申请
                            this.run()
                            return
                        }
                    }

                    val finalPermissions = nextPermissions
                    val maxWaitTime = PermissionApi.getMaxIntervalTimeByPermissions(activity, finalPermissions)

                    if (maxWaitTime == 0) {
                        requestPermissions(activity, finalPermissions, fragmentFactory, permissionDescription, this)
                    } else {
                       sendTask({
                            requestPermissions(activity, finalPermissions, fragmentFactory, permissionDescription, this)
                        }, maxWaitTime.toLong())
                    }
                }
            }
        )
    }
    /**
     * 延迟处理权限请求结果
     */
    private fun postDelayedHandlerRequestPermissionsResult() {
        sendTask({ this.handlePermissionRequestResult() }, 100)
    }

    /**
     * 延迟解锁 Activity 方向
     */
    private fun postDelayedUnlockActivityOrientation( activity: Activity) {
        // 延迟执行是为了让外层回调中的代码能够顺序执行完成
        PermissionTaskHandler.sendTask({
            ActivityOrientationManager.unlockActivityOrientation(
                activity
            )
        }, 100)
    }

    /**
     * 处理权限请求结果
     */
    private fun handlePermissionRequestResult() {
        val callback = mCallBack

        val interceptor = mPermissionInterceptor

        val requestPermissions: List<IPermission> = mRequestPermissions

        val activity = mActivity

        // 如果当前 Activity 不可用，就不继续往下执行代码
        if (PermissionUtils.isActivityUnavailable(activity)) {
            return
        }

        val grantedPermissions: MutableList<IPermission> =
            ArrayList<IPermission>(requestPermissions.size)
        val deniedPermissions: MutableList<IPermission> =
            ArrayList<IPermission>(requestPermissions.size)
        // 遍历请求的权限，并且根据权限的授权状态进行分类
        for (permission in requestPermissions) {
            if (permission.isGrantedPermission(activity, false)) {
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
        private fun getUnauthorizedPermissions(
            activity: Activity,
            requestPermissions: List<IPermission>
        ): List<List<IPermission>> {
            val unauthorizedPermissions = mutableListOf<List<IPermission>>() // 未授权的权限列表
            val alreadyDonePermissions = mutableListOf<IPermission>()        // 已处理的权限列表

            for (i in requestPermissions.indices) {
                val permission = requestPermissions[i]

                // 如果这个权限在前面已经处理过了，就不再处理
                if (PermissionUtils.containsPermission(alreadyDonePermissions, permission)) continue
                alreadyDonePermissions.add(permission)

                // 如果这个权限不支持申请 或 已授权，就跳过
                if (!permission.isSupportRequestPermission(activity) || permission.isGrantedPermission(activity)) continue

                // 如果是特殊权限，单独处理
                if (permission.getPermissionType() == PermissionType.SPECIAL) {
                    unauthorizedPermissions.add(listOf(permission))
                    continue
                }

                val permissionGroup = permission.getPermissionGroup()
                if (permissionGroup.isNullOrEmpty()) {
                    unauthorizedPermissions.add(listOf(permission))
                    continue
                }

                var todoPermissions: MutableList<IPermission>? = null
                for (j in i until requestPermissions.size) {
                    val todoPermission = requestPermissions[j]

                    if (todoPermission.getPermissionGroup() != permissionGroup) continue
                    if (!todoPermission.isSupportRequestPermission(activity)) continue
                    if (todoPermission.isGrantedPermission(activity)) continue

                    if (todoPermissions == null) {
                        todoPermissions = mutableListOf()
                    }
                    todoPermissions.add(todoPermission)

                    if (!PermissionUtils.containsPermission(alreadyDonePermissions, todoPermission)) {
                        alreadyDonePermissions.add(todoPermission)
                    }
                }

                if (todoPermissions.isNullOrEmpty()) continue
                if (PermissionApi.isGrantedPermissions(activity, todoPermissions)) continue

                // 拆分后台权限
                val backgroundPermissions = mutableListOf<IPermission>()
                val iterator = todoPermissions.iterator()
                while (iterator.hasNext()) {
                    val todoPermission = iterator.next()
                    if (!todoPermission.isBackgroundPermission(activity)) continue

                    iterator.remove()
                    backgroundPermissions.add(todoPermission)
                    break
                }

                if (todoPermissions.isNotEmpty()) {
                    unauthorizedPermissions.add(todoPermissions)
                }
                if (backgroundPermissions.isNotEmpty()) {
                    unauthorizedPermissions.add(backgroundPermissions)
                }
            }

            return unauthorizedPermissions
        }

        /**
         * 发起一次权限请求
         */
        private fun requestPermissions(
            activity: Activity, permissions: List<IPermission>,
            fragmentFactory: PermissionFragmentFactory<*, *>,
            permissionDescription: OnPermissionDescription,
            finishRunnable: Runnable
        ) {
            if (permissions.isEmpty()) {
                finishRunnable.run()
                return
            }

            val permissionType =
                if (areAllDangerousPermission(permissions)) PermissionType.DANGEROUS else PermissionType.SPECIAL
            if (permissionType === PermissionType.DANGEROUS && !PermissionVersion.isAndroid6()) {
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