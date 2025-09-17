package com.hjq.permissions

import PermissionChecker
import android.app.Activity
import android.content.Context
import androidx.annotation.IntRange
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.hjq.permissions.XXPermissions.REQUEST_CODE
import com.hjq.permissions.XXPermissions.getPermissionDescription
import com.hjq.permissions.XXPermissions.getPermissionInterceptor
import com.hjq.permissions.XXPermissions.sCheckMode
import com.knight.kotlin.library_permiss.DefaultPermissionDescription
import com.knight.kotlin.library_permiss.DefaultPermissionInterceptor
import com.knight.kotlin.library_permiss.OnPermissionCallback
import com.knight.kotlin.library_permiss.OnPermissionDescription
import com.knight.kotlin.library_permiss.OnPermissionInterceptor
import com.knight.kotlin.library_permiss.core.OnPermissionFragmentCallback
import com.knight.kotlin.library_permiss.fragment.factory.PermissionFragmentFactory
import com.knight.kotlin.library_permiss.fragment.factory.PermissionFragmentFactoryByApp
import com.knight.kotlin.library_permiss.fragment.factory.PermissionFragmentFactoryBySupport
import com.knight.kotlin.library_permiss.manifest.AndroidManifestParser
import com.knight.kotlin.library_permiss.permission.PermissionChannel
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.start.StartActivityAgent
import com.knight.kotlin.library_permiss.tools.PermissionApi
import com.knight.kotlin.library_permiss.tools.PermissionSettingPage
import com.knight.kotlin.library_permiss.tools.PermissionUtils

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/XXPermissions
 *    time   : 2018/06/15
 *    desc   : Android 权限请求入口类
 */
@Suppress("unused", "DEPRECATION")
object XXPermissions {

    /** 权限设置页跳转请求码 */
    const val REQUEST_CODE = 1024 + 1

    /** 权限申请拦截器的类型（全局生效） */
    private var sPermissionInterceptorClass: Class<out OnPermissionInterceptor>? = null

    /** 权限请求描述器的类型（全局生效） */
    private var sPermissionDescriptionClass: Class<out OnPermissionDescription>? = null

    /** 是否为检查模式（全局生效） */
    var sCheckMode: Boolean? = null

    /**
     * 设置请求的对象
     *
     * @param context          当前 Activity，可以传入栈顶的 Activity
     */
    fun with(context: Context): XXPermissionsBuilder {
        return XXPermissionsBuilder(context)
    }

    fun with(appFragment: Fragment): XXPermissionsBuilder {
        return XXPermissionsBuilder(appFragment)
    }



    /**
     * 设置是否开启错误检测模式（全局设置）
     */
    fun setCheckMode(checkMode: Boolean) {
        sCheckMode = checkMode
    }

    /**
     * 设置权限申请拦截器（全局设置）
     */
    fun setPermissionInterceptor(clazz: Class<out OnPermissionInterceptor>?) {
        sPermissionInterceptorClass = clazz
    }

    /**
     * 获取权限申请拦截器（全局）
     */
    
    fun getPermissionInterceptor(): OnPermissionInterceptor {
        return sPermissionInterceptorClass?.let {
            try {
                it.newInstance()
            } catch (e: Exception) {
                e.printStackTrace()
                DefaultPermissionInterceptor()
            }
        } ?: DefaultPermissionInterceptor()
    }

    /**
     * 设置权限描述器（全局设置）
     *
     * 这里解释一下，为什么不开放普通对象，而是只开放 Class 对象，这是因为如果用普通对象，那么就会导致全局都复用这一个对象
     * 而这个会带来一个后果，就是可能出现类内部字段的使用冲突，为了避免这一个问题，最好的解决方案是不去复用同一个对象
     */
    fun setPermissionDescription(clazz: Class<out OnPermissionDescription>?) {
        sPermissionDescriptionClass = clazz
    }

    /**
     * 获取权限描述器（全局）
     */
    
    fun getPermissionDescription(): OnPermissionDescription {
        return sPermissionDescriptionClass?.let {
            try {
                it.newInstance()
            } catch (e: Exception) {
                e.printStackTrace()
                DefaultPermissionDescription()
            }
        } ?: DefaultPermissionDescription()
    }

    /**
     * 判断一个或多个权限是否全部授予了
     */
    fun isGrantedPermission(context: Context, permission: IPermission): Boolean {
        return permission.isGrantedPermission(context)
    }


    fun isGrantedPermissions(context: Context, permissions: List<IPermission>): Boolean {
        return PermissionApi.isGrantedPermissions(context, permissions)
    }

    /**
     * 从权限列表中获取已授予的权限
     */

    fun getGrantedPermissions(context: Context, permissions: List<IPermission>): List<IPermission> {
        return PermissionApi.getGrantedPermissions(context, permissions)
    }

    /**
     * 从权限列表中获取没有授予的权限
     */

    fun getDeniedPermissions(context: Context, permissions: List<IPermission>): List<IPermission> {
        return PermissionApi.getDeniedPermissions(context, permissions)
    }

    /**
     * 判断两个权限是否相等
     */
    fun equalsPermission(permission1: IPermission, permission2: IPermission): Boolean {
        return PermissionUtils.equalsPermission(permission1, permission2)
    }

    fun equalsPermission(permission1: IPermission, permission2: String): Boolean {
        return PermissionUtils.equalsPermission(permission1, permission2)
    }

    fun equalsPermission(permissionName1: String, permission2: String): Boolean {
        return PermissionUtils.equalsPermission(permissionName1, permission2)
    }

    /**
     * 判断权限列表中是否包含某个权限
     */
    fun containsPermission(permissions: List<IPermission>, permission: IPermission): Boolean {
        return PermissionUtils.containsPermission(permissions, permission)
    }

    fun containsPermission(permissions: List<IPermission>, permissionName: String): Boolean {
        return PermissionUtils.containsPermission(permissions, permissionName)
    }

    /**
     * 判断某个权限是否为健康权限
     */
    fun isHealthPermission(permission: IPermission): Boolean {
        return PermissionApi.isHealthPermission(permission)
    }

    /**
     * 判断一个或多个权限是否被勾选了不再询问的选项
     *
     * 如果判断的权限中包含了危险权限，则需要特别注意：
     * 2. 如果在应用启动后，没有向系统申请过这个危险权限，而是直接去判断它有没有勾选不再询问的选项，这样得到的结果是不准的，建议在权限回调方法中调用，除此之外没有更好的解决方法
     * 3. 如果危险权限在申请的过程中，如果用户不是通过点击《不允许》选项来取消权限，而是通过点击返回键或者点击系统授权框外层区域来取消授权的，这样得到的结果是不准的，这个问题无解
     */
    fun isDoNotAskAgainPermission(activity: Activity, permission: IPermission): Boolean {
        return permission.isDoNotAskAgainPermission(activity)
    }

    fun isDoNotAskAgainPermissions(activity: Activity, permissions: List<IPermission>): Boolean {
        return PermissionApi.isDoNotAskAgainPermissions(activity, permissions)
    }

    /* android.content.Context */

    /**
     * 跳转到应用权限设置页
     *
     * @param permissions           没有授予或者被拒绝的权限组
     */
    fun startPermissionActivity(
        activity: Activity,
        permissions: List<IPermission>,
        @IntRange(from = 1, to = 65535) requestCode: Int
    ) {
        StartActivityAgent.startActivityForResult(
            activity,
            PermissionApi.getBestPermissionSettingIntent(activity, permissions, true),
            requestCode
        )
    }

    fun startPermissionActivity(
        activity: Activity,
        permission: IPermission,
         callback: OnPermissionCallback
    ) {
        startPermissionActivity(activity, PermissionUtils.asArrayList(permission), callback)
    }

    fun startPermissionActivity(
        activity: Activity,
        permissions: List<IPermission>,
         callback: OnPermissionCallback
    ) {
        if (PermissionUtils.isActivityUnavailable(activity)) {
            return
        }
        if (permissions.isEmpty()) {
            StartActivityAgent.startActivity(activity, PermissionSettingPage.getCommonPermissionSettingIntent(activity))
            return
        }
        val fragmentFactory = generatePermissionFragmentFactory(activity,null)
        fragmentFactory.createAndCommitFragment(
            permissions,
            PermissionChannel.START_ACTIVITY_FOR_RESULT,
            object : OnPermissionFragmentCallback {
                override fun onRequestPermissionFinish() {
                    if (PermissionUtils.isActivityUnavailable(activity)) {
                        return
                    }
                    dispatchPermissionPageCallback(activity, permissions, callback)
                }
            })
        }
    }

    /* android.app.Fragment */

    fun startPermissionActivity(appFragment: Fragment) {
        startPermissionActivityWithList(appFragment, emptyList())
    }

    fun startPermissionActivityWithVarargs(appFragment: Fragment, vararg permissions: IPermission) {
        startPermissionActivityWithList(appFragment, permissions.toList())
    }


    fun startPermissionActivityWithList(
        appFragment: Fragment,
        permissions: List<IPermission>,
        @IntRange(from = 1, to = 65535) requestCode: Int = REQUEST_CODE
    ) {
        if (PermissionUtils.isFragmentUnavailable(appFragment)) {
            return
        }
        val activity = appFragment.requireActivity()
        if (PermissionUtils.isActivityUnavailable(activity) || PermissionUtils.isFragmentUnavailable(appFragment)) {
            return
        }
        if (permissions.isEmpty()) {
            StartActivityAgent.startActivity(appFragment, PermissionSettingPage.getCommonPermissionSettingIntent(activity))
            return
        }
        StartActivityAgent.startActivityForResult(
            appFragment,
            PermissionApi.getBestPermissionSettingIntent(activity, permissions, true),
            requestCode
        )
    }

    fun startPermissionActivity(
        appFragment: Fragment,
        permission: IPermission,
         callback: OnPermissionCallback?
    ) {
        startPermissionActivity(appFragment, PermissionUtils.asArrayList(permission), callback)
    }

    fun startPermissionActivity(
        appFragment: Fragment,
        permissions: List<IPermission>,
         callback: OnPermissionCallback?
    ) {
        if (PermissionUtils.isFragmentUnavailable(appFragment)) {
            return
        }
        val activity = appFragment.requireActivity()
        if (PermissionUtils.isActivityUnavailable(activity) || PermissionUtils.isFragmentUnavailable(appFragment)) {
            return
        }
        if (permissions.isEmpty()) {
            StartActivityAgent.startActivity(appFragment, PermissionSettingPage.getCommonPermissionSettingIntent(activity))
            return
        }
        val fragmentFactory = generatePermissionFragmentFactory(activity, appFragment)
        fragmentFactory.createAndCommitFragment(permissions,
            PermissionChannel.START_ACTIVITY_FOR_RESULT,
            object : OnPermissionFragmentCallback {
                override fun onRequestPermissionFinish() {
                    if (PermissionUtils.isActivityUnavailable(activity) || PermissionUtils.isFragmentUnavailable(appFragment)) {
                        return
                    }
                    dispatchPermissionPageCallback(activity, permissions, callback)
                }
            })
    }

    /**
     * 创建 Fragment 工厂
     */

    private fun generatePermissionFragmentFactory(
        activity: Activity,
         appFragment: Fragment?
    ): PermissionFragmentFactory<*, *> {
        return when {
            appFragment != null -> {
                // appFragment.childFragmentManager 需要 minSdkVersion >=  17
                PermissionFragmentFactoryByApp(appFragment.requireActivity(), appFragment.childFragmentManager)
            }
            activity is FragmentActivity -> {
                PermissionFragmentFactoryBySupport(activity, activity.supportFragmentManager)
            }
            else -> {
                PermissionFragmentFactoryByApp(activity, null)
            }
        }
    }

    /**
     * 派发权限设置页回调
     */
    private fun dispatchPermissionPageCallback(
        context: Context,
        permissions: List<IPermission>,
         callback: OnPermissionCallback?
    ) {
        if (callback == null) {
            return
        }
        val grantedList = ArrayList<IPermission>(permissions.size)
        val deniedList = ArrayList<IPermission>(permissions.size)
        // 遍历请求的权限，并且根据权限的授权状态进行分类
        for (permission in permissions) {
            if (permission.isGrantedPermission(context, false)) {
                grantedList.add(permission)
            } else {
                deniedList.add(permission)
            }
        }
        callback.onResult(grantedList, deniedList)
    }

    class XXPermissionsBuilder internal constructor(private val context: Context) {

        
        private val appFragment: Fragment? = null

        
        constructor(appFragment: Fragment) : this(appFragment.requireActivity()) {
            // 这里通过 secondary constructor 初始化 appFragment
            // Kotlin 中主构造函数必须先执行，所以通过这种方式处理
        }

   

        /** 申请的权限列表 */
        
        private val mRequestList = ArrayList<IPermission>()

        /** 权限请求拦截器 */
        
        private var mPermissionInterceptor: OnPermissionInterceptor? = null

        /** 权限请求描述 */
        
        private var mPermissionDescription: OnPermissionDescription? = null

        /** 设置不检查 */
        
        private var mCheckMode: Boolean? = null

        /**
         * 添加单个权限
         */
        fun permission(permission: IPermission): XXPermissionsBuilder {
            // 这种写法的作用：如果出现重复添加的权限，则以最后添加的权限为主
            mRequestList.remove(permission)
            mRequestList.add(permission)
            return this
        }

        /**
         * 添加多个权限
         */
        fun permissions(permissions: List<IPermission>): XXPermissionsBuilder {
            if (permissions.isEmpty()) {
                return this
            }
            permissions.forEach { permission(it) }
            return this
        }

        /**
         * 设置权限请求拦截器
         */
        fun interceptor( permissionInterceptor: OnPermissionInterceptor?): XXPermissionsBuilder {
            mPermissionInterceptor = permissionInterceptor
            return this
        }

        /**
         * 设置权限请求描述
         */
        fun description( permissionDescription: OnPermissionDescription?): XXPermissionsBuilder {
            mPermissionDescription = permissionDescription
            return this
        }

        /**
         * 设置不触发错误检测机制
         */
        fun unchecked(): XXPermissionsBuilder {
            mCheckMode = false
            return this
        }

        /**
         * 请求权限
         */
        fun request( callback: OnPermissionCallback) {
            val context = context
            if (context == null) {
                return
            }

            val permissionInterceptor = mPermissionInterceptor ?: getPermissionInterceptor()
            val permissionDescription = mPermissionDescription ?: getPermissionDescription()

            // 权限请求列表（为什么直接不用字段？因为框架要兼容新旧权限，在低版本下会自动添加旧权限申请，为了避免重复添加）
            val requestList = ArrayList(mRequestList)

            // 从 Context 对象中获得 Activity 对象
            val activity = PermissionUtils.findActivity(context)

             activity?.let {
                 if (isCheckMode(context)) {
                     // 检查传入的 Activity 或者 Fragment 状态是否正常
                     PermissionChecker.checkActivityStatus(activity!!)
                     if (appFragment != null) {
                         PermissionChecker.checkAppFragmentStatus(appFragment)
                     }
                     // 检查传入的权限是否正常
                     AndroidManifestParser.getAndroidManifestInfo(context)?.let { PermissionChecker.checkPermissionList(activity, requestList, it) }
                 }

                 // 检查 Activity 是不是不可用
                 if (PermissionUtils.isActivityUnavailable(activity)) {
                     return
                 }

                 // 优化所申请的权限列表

                     PermissionApi.addOldPermissionsByNewPermissions(activity, requestList)


                 // 判断要申请的权限是否都授予了
                 if (PermissionApi.isGrantedPermissions(context, requestList)) {
                     // 如果是的话，就不申请权限，而是通知权限申请成功
                     if (activity != null) {
                         permissionInterceptor.onRequestPermissionEnd(activity, true, requestList, requestList, ArrayList(), callback)
                     }
                     return
                 }

                 val fragmentFactory = when {
                     appFragment != null -> {
                         if (PermissionUtils.isFragmentUnavailable(appFragment)) {
                             return
                         }

                             generatePermissionFragmentFactory(activity, appFragment)

                     }
                     else -> {
                         generatePermissionFragmentFactory(activity,null)

                     }
                 }

                 // 申请没有授予过的权限

                     permissionInterceptor.onRequestPermissionStart(activity, requestList, fragmentFactory, permissionDescription, callback)

             }


        }

        /**
         * 当前是否为检测模式
         */
        private fun isCheckMode(context: Context): Boolean {
            if (mCheckMode == null) {
                if (sCheckMode == null) {
                    sCheckMode = PermissionUtils.isDebugMode(context)
                }
                mCheckMode = sCheckMode
            }
            return mCheckMode!!
        }

}