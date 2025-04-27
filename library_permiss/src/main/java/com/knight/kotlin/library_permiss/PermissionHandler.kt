package com.knight.kotlin.library_permiss

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.AndroidVersion.getAndroidVersionCode
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid13
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid6
import com.knight.kotlin.library_permiss.PermissionHelper.findAndroidVersionByPermission
import com.knight.kotlin.library_permiss.fragment.RequestDangerousPermissionFragment
import com.knight.kotlin.library_permiss.fragment.RequestSpecialPermissionFragment
import com.knight.kotlin.library_permiss.listener.OnPermissionCallback
import com.knight.kotlin.library_permiss.listener.OnPermissionInterceptor
import com.knight.kotlin.library_permiss.listener.OnPermissionPageCallback
import com.knight.kotlin.library_permiss.listener.OnRequestPermissionsResultCallback
import com.knight.kotlin.library_permiss.permissions.PermissionApi
import com.knight.kotlin.library_permiss.permissions.PermissionApi.getDeniedPermissions
import com.knight.kotlin.library_permiss.permissions.PermissionApi.getGrantedPermissions
import com.knight.kotlin.library_permiss.permissions.PermissionApi.isDoNotAskAgainPermissions
import com.knight.kotlin.library_permiss.permissions.PermissionApi.isGrantedPermission
import com.knight.kotlin.library_permiss.permissions.PermissionApi.isGrantedPermissions
import com.knight.kotlin.library_permiss.permissions.PermissionApi.recheckPermissionResult
import com.knight.kotlin.library_permiss.permissions.PermissionGroupType
import com.knight.kotlin.library_permiss.utils.PermissionUtils
import java.util.concurrent.atomic.AtomicInteger


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/27 11:32
 * @descript:
 */
@Suppress("deprecation")
class PermissionHandler(activity: Activity, allPermissions: List<String>) {
    /** 权限回调对象  */

    private lateinit var mCallBack: OnPermissionCallback

    /** 权限请求拦截器  */
    private lateinit var mInterceptor: OnPermissionInterceptor

    private val mActivity: Activity = activity

    private val mAllPermissions = allPermissions

    /**
     * 设置权限监听回调监听
     */
    fun setOnPermissionCallback(callback: OnPermissionCallback) {
        mCallBack = callback
    }

    /**
     * 设置权限请求拦截器
     */
    fun setOnPermissionInterceptor( interceptor: OnPermissionInterceptor) {
        mInterceptor = interceptor
    }

    /**
     * 开始权限请求
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun startPermissionRequest() {
        if (mAllPermissions.isEmpty()) {
            return
        }

        val allDangerousPermissions: MutableList<String> = ArrayList()
        val allSpecialPermissions: MutableList<String> = ArrayList()

        // 对危险权限和特殊权限进行分类
        for (permission in mAllPermissions) {
            if (PermissionApi.isSpecialPermission(permission)) {
                allSpecialPermissions.add(permission)
            } else {
                allDangerousPermissions.add(permission)
            }
        }

        val unauthorizedSpecialPermissions = getUnauthorizedSpecialPermissions(mActivity, allSpecialPermissions)
        val unauthorizedDangerousPermissions = getUnauthorizedDangerousPermissions(mActivity, allDangerousPermissions)

        // 判断权限集合中第一个权限是特殊权限还是危险权限，如果是特殊权限就先申请所有的特殊权限，如果是危险权限就先申请所有的危险权限
        if (PermissionHelper.isSpecialPermission(mAllPermissions[0])) {
            // 请求所有的特殊权限
            requestAllSpecialPermission(mActivity, unauthorizedSpecialPermissions) {
                // 请求完特殊权限后，接下来请求危险权限
                requestAllDangerousPermission(
                    mActivity, unauthorizedDangerousPermissions
                ) { this.postDelayedHandlerRequestPermissionsResult() }
            }
        } else {
            // 请求所有的危险权限
            requestAllDangerousPermission(mActivity, unauthorizedDangerousPermissions) {
                // 请求完危险权限后，接下来请求特殊权限
                requestAllSpecialPermission(
                    mActivity, unauthorizedSpecialPermissions
                ) { this.postDelayedHandlerRequestPermissionsResult() }
            }
        }
    }

    /**
     * 延迟处理权限请求结果
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun postDelayedHandlerRequestPermissionsResult() {
        PermissionUtils.postDelayed({ this.handlePermissionRequestResult() }, 300)
    }

    /**
     * 处理权限请求结果
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun handlePermissionRequestResult() {
        if (mInterceptor == null) {
            return
        }

        val callback = mCallBack

        val interceptor: OnPermissionInterceptor = mInterceptor

        val allPermissions = mAllPermissions

        val grantResults = IntArray(allPermissions.size)
        for (i in grantResults.indices) {
            val permission = allPermissions[i]
            grantResults[i] = if (isGrantedPermission(mActivity, permission)) PackageManager.PERMISSION_GRANTED else PackageManager.PERMISSION_DENIED

            grantResults[i] = if (recheckPermissionResult(
                    mActivity, permission, grantResults[i] == PackageManager.PERMISSION_GRANTED
                )
            )
                PackageManager.PERMISSION_GRANTED
            else
                PackageManager.PERMISSION_DENIED
        }

        // 获取已授予的权限
        val grantedPermissions = getGrantedPermissions(allPermissions, grantResults)

        // 如果请求成功的权限集合大小和请求的数组一样大时证明权限已经全部授予
        if (grantedPermissions.size == allPermissions.size) {
            // 代表申请的所有的权限都授予了
            interceptor.grantedPermissionRequest(mActivity, allPermissions, grantedPermissions, true, callback)
            // 权限申请结束
            interceptor.finishPermissionRequest(mActivity, allPermissions, false, callback)
            return
        }

        // 获取被拒绝的权限
        val deniedPermissions = getDeniedPermissions(allPermissions, grantResults)

        // 代表申请的权限中有不同意授予的，如果有某个权限被永久拒绝就返回 true 给开发人员，让开发者引导用户去设置界面开启权限
        interceptor.deniedPermissionRequest(
            mActivity, allPermissions, deniedPermissions,
            isDoNotAskAgainPermissions(mActivity, deniedPermissions), callback
        )

        // 证明还有一部分权限被成功授予，回调成功接口
        if (!grantedPermissions.isEmpty()) {
            interceptor.grantedPermissionRequest(mActivity, allPermissions, grantedPermissions, false, callback)
        }

        // 权限申请结束
        interceptor.finishPermissionRequest(mActivity, allPermissions, false, callback)
    }

    companion object {
        /**
         * 发起权限请求
         */
        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        fun request(
           activity: Activity, allPermissions: List<String>,
           callback: OnPermissionCallback,interceptor: OnPermissionInterceptor
        ) {
            val permissionHandler = PermissionHandler(activity, allPermissions)
            permissionHandler.setOnPermissionInterceptor(interceptor)
            permissionHandler.setOnPermissionCallback(callback)
            permissionHandler.startPermissionRequest()
        }

        /**
         * 获取未授权的特殊权限
         */
        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        private fun getUnauthorizedSpecialPermissions(
            activity: Activity,
            allSpecialPermissions: List<String>
        ): List<String> {
            val unauthorizedSpecialPermissions: MutableList<String> = ArrayList()
            for (permission in allSpecialPermissions) {
                if (isGrantedPermission(activity, permission)) {
                    // 已经授予过了，可以跳过
                    continue
                }

                // 如果当前设备的版本还没有出现过这个特殊权限，并且权限还没有授权的情况，证明这个特殊权限有向下兼容的权限
                // 这种情况就不要跳转到权限设置页，例如 MANAGE_EXTERNAL_STORAGE 权限
                if (getAndroidVersionCode() < findAndroidVersionByPermission(permission)) {
                    continue
                }

                unauthorizedSpecialPermissions.add(permission)
            }
            return unauthorizedSpecialPermissions
        }

        /**
         * 获取未授权的危险权限
         */
        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        private fun getUnauthorizedDangerousPermissions(
            activity: Activity,
            allDangerousPermissions: List<String>
        ): List<List<String>> {
            // 已处理的危险权限列表
            val alreadyProcessedDangerousPermissions: MutableList<String> = ArrayList()

            // 记录需要申请的危险权限或者权限组
            val unauthorizedDangerousPermissions: MutableList<MutableList<String>> = ArrayList()

            for (dangerousPermission in allDangerousPermissions) {
                // 如果这个危险权限在前面已经处理过了，就不再处理

                if (alreadyProcessedDangerousPermissions.contains(dangerousPermission)) {
                    continue
                }
                alreadyProcessedDangerousPermissions.add(dangerousPermission)

                // 查询权限所在的权限组类型
                val permissionGroupType: PermissionGroupType? = PermissionHelper.queryDangerousPermissionGroupType(dangerousPermission)
                if (permissionGroupType == null) {
                    // 如果这个权限没有组别，就直接单独做为一次权限申请
                    unauthorizedDangerousPermissions.add(PermissionUtils.asArrayList(dangerousPermission))
                    continue
                }

                // 如果这个权限有组别，那么就获取这个组别的全部权限
                val dangerousPermissionGroup: MutableList<String> = PermissionHelper.getDangerousPermissionGroup(permissionGroupType)!!.toMutableList()
                // 对这个组别的权限进行逐个遍历
                val iterator = dangerousPermissionGroup.iterator()
                while (iterator.hasNext()) {
                    val permission = iterator.next()

                    if (findAndroidVersionByPermission(permission) > getAndroidVersionCode()) {
                        // 如果申请的权限是新系统才出现的，但是当前是旧系统运行，就从权限组中移除
                        iterator.remove()
                        continue
                    }

                    // 判断申请的权限列表中是否有包含权限组中的权限
                    if (allDangerousPermissions.contains(permission)) {
                        // 如果包含的话，就加入到已处理的列表中，这样遍历到它的时候就会忽略掉
                        alreadyProcessedDangerousPermissions.add(permission)
                    } else {
                        // 如果不包含的话，就从权限组中移除
                        iterator.remove()
                    }
                }

                // 如果这个权限组为空，证明剩余的权限是在高版本系统才会出现，这里无需再次发起申请
                if (dangerousPermissionGroup.isEmpty()) {
                    continue
                }

                // 如果这个权限组已经全部授权，就不纳入申请的范围内
                if (isGrantedPermissions(activity, dangerousPermissionGroup)) {
                    continue
                }

                // 判断申请的权限组是否包含后台权限（例如后台定位权限，后台传感器权限），如果有的话，不能在一起申请，需要进行拆分申请
                val backgroundPermission = PermissionHelper.getBackgroundPermissionByGroup(dangerousPermissionGroup)
                if (!TextUtils.isEmpty(backgroundPermission)) {
                    val foregroundPermissions: MutableList<String> = ArrayList(dangerousPermissionGroup)
                    foregroundPermissions.remove(backgroundPermission)

                    // 添加前台权限（前提得是没有授权）
                    if (!foregroundPermissions.isEmpty() &&
                        !isGrantedPermissions(activity, foregroundPermissions)
                    ) {
                        unauthorizedDangerousPermissions.add(foregroundPermissions)
                    }
                    // 添加后台权限
                    backgroundPermission?.let {
                        unauthorizedDangerousPermissions.add(PermissionUtils.asArrayList(it))
                    }

                    continue
                }

                // 直接申请权限组（不区分前台权限和后台权限）
                unauthorizedDangerousPermissions.add(dangerousPermissionGroup)
            }

            return unauthorizedDangerousPermissions
        }

        /**
         * 请求所有的特殊权限
         */
        private fun requestAllSpecialPermission(
            activity: Activity,
            specialPermissions: List<String>,
            finishRunnable: Runnable
        ) {
            if (specialPermissions.isEmpty()) {
                finishRunnable.run()
                return
            }

            val index = AtomicInteger()
            requestSingleSpecialPermission(activity, specialPermissions[index.get()], object : Runnable {
                override fun run() {
                    index.incrementAndGet()
                    if (index.get() < specialPermissions.size) {
                        requestSingleSpecialPermission(activity, specialPermissions[index.get()], this)
                        return
                    }
                    finishRunnable.run()
                }
            })
        }

        /**
         * 请求单个特殊权限
         */
        private fun requestSingleSpecialPermission(
            activity: Activity,
            specialPermission: String,
            finishRunnable: Runnable
        ) {
            RequestSpecialPermissionFragment.launch(activity, listOf(specialPermission), object : OnPermissionPageCallback {
                override fun onGranted() {
                    finishRunnable.run()
                }

                override fun onDenied() {
                    finishRunnable.run()
                }
            })
        }

        /**
         * 申请所有危险权限
         */
        private fun requestAllDangerousPermission(
            activity: Activity,
            dangerousPermissions: List<List<String>>,
            finishRunnable: Runnable
        ) {
            if (!isAndroid6()) {
                // 如果是 Android 6.0 以下，没有危险权限的概念
                finishRunnable.run()
                return
            }


            if (dangerousPermissions.isEmpty()) {
                finishRunnable.run()
                return
            }

            val index = AtomicInteger()
            requestSingleDangerousPermission(activity, dangerousPermissions[index.get()], object : Runnable {
                override fun run() {
                    index.incrementAndGet()
                    if (index.get() < dangerousPermissions.size) {
                        var delayMillis: Long = 0
                        val permissions = dangerousPermissions[index.get()]
                        if (PermissionHelper.containsBackgroundPermission(permissions)) {
                            // 经过测试，在 Android 13 设备上面，先申请前台权限，然后立马申请后台权限大概率会出现失败
                            // 这里为了避免这种情况出现，所以加了一点延迟，这样就没有什么问题了
                            // 为什么延迟时间是 150 毫秒？ 经过实践得出 100 还是有概率会出现失败，但是换成 150 试了很多次就都没有问题了
                            delayMillis = (if (isAndroid13()) 150 else 0).toLong()
                        }
                        if (delayMillis == 0L) {
                            requestSingleDangerousPermission(activity, permissions, this)
                        } else {
                            PermissionUtils.postDelayed({ requestSingleDangerousPermission(activity, permissions, this) }, delayMillis)
                        }
                        return
                    }
                    finishRunnable.run()
                }
            })
        }

        /**
         * 申请单个危险权限
         */
        private fun requestSingleDangerousPermission(
            activity: Activity,
            dangerousPermissions: List<String>,
            finishRunnable: Runnable
        ) {
            RequestDangerousPermissionFragment.launch(activity, dangerousPermissions, object : OnRequestPermissionsResultCallback {
                override fun onRequestPermissionsResult(permissions: Array<String>, grantResults: IntArray) {
                    finishRunnable.run()
                }
            })
        }
    }


}