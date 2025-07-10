package com.knight.kotlin.library_permiss

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.knight.kotlin.library_permiss.AndroidVersionTools.getCurrentAndroidVersionCode
import com.knight.kotlin.library_permiss.AndroidVersionTools.isAndroid13

import com.knight.kotlin.library_permiss.PermissionHelper.findAndroidVersionByPermission
import com.knight.kotlin.library_permiss.PermissionIntentManager.getApplicationDetailsIntent
import com.knight.kotlin.library_permiss.fragment.factory.PermissionFragmentFactory
import com.knight.kotlin.library_permiss.fragment.factory.PermissionFragmentFactoryByApp
import com.knight.kotlin.library_permiss.fragment.factory.PermissionFragmentFactoryBySupport
import com.knight.kotlin.library_permiss.listener.OnPermissionCallback
import com.knight.kotlin.library_permiss.listener.OnPermissionDescription
import com.knight.kotlin.library_permiss.listener.OnPermissionFlowCallback
import com.knight.kotlin.library_permiss.listener.OnPermissionInterceptor
import com.knight.kotlin.library_permiss.listener.OnPermissionPageCallback
import com.knight.kotlin.library_permiss.permissions.PermissionApi
import com.knight.kotlin.library_permiss.permissions.PermissionApi.addOldPermissionsByNewPermissions
import com.knight.kotlin.library_permiss.permissions.PermissionApi.adjustPermissionsSort
import com.knight.kotlin.library_permiss.permissions.PermissionApi.getBestPermissionSettingIntent
import com.knight.kotlin.library_permiss.permissions.PermissionType
import com.knight.kotlin.library_permiss.utils.PermissionUtils
import com.knight.kotlin.library_permiss.utils.PermissionUtils.asArrayList
import com.knight.kotlin.library_permiss.utils.PermissionUtils.asArrayLists
import com.knight.kotlin.library_permiss.utils.PermissionUtils.findActivity
import com.knight.kotlin.library_permiss.utils.PermissionUtils.getAndroidManifestInfo
import com.knight.kotlin.library_permiss.utils.PermissionUtils.isDebugMode


/**
 * Author:Knight
 * Time:2022/1/20 16:57
 * Description:XXPermissions
 */
class XXPermissions constructor(context: Context) {
    /** 权限设置页跳转请求码  */
    /** Context 对象  */

    private var mContext: Context? = null

    init {
        mContext = context
    }
    companion object {
        const val REQUEST_CODE: Int = 1024 + 1
        /** 权限请求拦截器  */
        private var sPermissionInterceptor: OnPermissionInterceptor? = null

        /** 设置权限请求描述  */
        private var sPermissionDescriptionClass: Class<out OnPermissionDescription>? = null

        /** 当前是否为检查模式  */
        private var sCheckMode: Boolean? = null
        /**
         * 设置请求的对象
         *
         * @param context          当前 Activity，可以传入栈顶的 Activity
         */
        fun with( context: Context): XXPermissions {
            return XXPermissions(context)
        }

        fun with( appFragment: Fragment): XXPermissions? {
            return appFragment.activity?.let { with(it) }
        }


        /**
         * 设置全局的检查模式
         */
        fun setCheckMode(checkMode: Boolean) {
            sCheckMode = checkMode
        }

        /**
         * 设置全局的权限请求拦截器
         */
        fun setPermissionInterceptor(permissionInterceptor: OnPermissionInterceptor?) {
            sPermissionInterceptor = permissionInterceptor
        }

        /**
         * 获取全局权限请求拦截器
         */

        fun getPermissionInterceptor(): OnPermissionInterceptor {
            if (sPermissionInterceptor == null) {
                sPermissionInterceptor = object : OnPermissionInterceptor {}
            }
            return sPermissionInterceptor!!
        }

        /**
         * 设置全局的权限说明的类型
         *
         * 这里解释一下，为什么不开放普通对象，而是只开放 Class 对象，这是因为如果用普通对象，那么就会导致全局都复用这一个对象
         * 而这个会带来一个后果，就是可能出现类内部字段的使用冲突，为了避免这一个问题，最好的解决方案是不去复用同一个对象
         */
        fun setPermissionDescription(permissionDescriptionClass: Class<out OnPermissionDescription>?) {
            sPermissionDescriptionClass = permissionDescriptionClass
        }

        /**
         * 获取全局的权限说明
         */

        fun getPermissionDescription(): OnPermissionDescription {
            var permissionDescription: OnPermissionDescription? = null
            if (sPermissionDescriptionClass != null) {
                try {
                    permissionDescription = sPermissionDescriptionClass!!.newInstance()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (permissionDescription == null) {
                permissionDescription = object : OnPermissionDescription {
                    override fun onRequestPermissionStart(
                        activity: Activity,
                        requestPermissions: List<String?>
                    ) {
                        // default implementation ignored
                    }

                    override fun onRequestPermissionEnd(
                        activity: Activity,
                        requestPermissions: List<String?>
                    ) {
                        // default implementation ignored
                    }
                }
            }
            return permissionDescription
        }

        /**
         * 判断一个或多个权限是否全部授予了
         */
        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        fun isGrantedPermissions(context: Context, vararg permissions: String): Boolean {
            return isGrantedPermissions(context, asArrayList(*permissions))
        }

        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        fun isGrantedPermissions(
            context: Context,
            vararg permissions: Array<String>
        ): Boolean {
            return isGrantedPermissions(context, asArrayLists<String>(*permissions))
        }


        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        fun isGrantedPermissions(
             context: Context,
             permissions: List<String>
        ): Boolean {
            return PermissionApi.isGrantedPermissions(context, permissions)
        }


        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        fun getGrantedPermissions(
            context: Context,
            permissions: List<String>
        ): List<String> {
            return PermissionApi.getGrantedPermissions(context, permissions)
        }



        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        fun getDeniedPermissions(
            context: Context,
            permissions: List<String>
        ): List<String> {
            return PermissionApi.getDeniedPermissions(context, permissions)
        }

        /**
         * 判断某个权限出现的版本是否高于当前的设备的版本
         */
        fun isHighVersionPermission( permission: String?): Boolean {
            return findAndroidVersionByPermission(permission) > getCurrentAndroidVersionCode()
        }


        fun containsSpecialPermission( permissions: List<String?>): Boolean {
            return PermissionApi.containsSpecialPermission(permissions)
        }

        /**
         * 判断某个权限是否为后台权限
         */
        fun isBackgroundPermission( permission: String?): Boolean {
            return PermissionApi.isBackgroundPermission(permission!!)
        }


        fun containsBackgroundPermission( permissions: List<String?>): Boolean {
            return PermissionApi.containsBackgroundPermission(permissions)
        }



        /**
         * 跳转到应用权限设置页
         *
         * @param permissions           没有授予或者被拒绝的权限组
         */
        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        fun startPermissionActivity(context: Context, permissions: List<String>) {
            val activity = findActivity(context)
            if (activity != null) {
                startPermissionActivity(activity, permissions,REQUEST_CODE)
                return
            }
            val intent = getBestPermissionSettingIntent(context!!, permissions)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            PermissionActivityIntentHandler.startActivity(context, intent)
        }

        


        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        fun startPermissionActivity(
            appFragment: Fragment,
            permissions: List<String>,
            requestCode: Int
        ) {
            if (PermissionUtils.isFragmentUnavailable(appFragment)) {
                return
            }
            val activity: Activity? = appFragment.activity
            if (PermissionUtils.isActivityUnavailable(activity) || PermissionUtils.isFragmentUnavailable(
                    appFragment
                )
            ) {
                return
            }
            if (permissions.isEmpty()) {
                PermissionActivityIntentHandler.startActivity(
                    appFragment, getApplicationDetailsIntent(
                        activity!!
                    )
                )
                return
            }
            val intent = getBestPermissionSettingIntent(activity!!, permissions)
            PermissionActivityIntentHandler.startActivityForResult(appFragment, intent, requestCode)
        }


        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        fun startPermissionActivity(
             activity: Activity,
             permissions: List<String>,
            requestCode: Int
        ) {
            val intent = getBestPermissionSettingIntent(activity, permissions)
            PermissionActivityIntentHandler.startActivityForResult(activity, intent, requestCode)
        }

        fun startPermissionActivity(
             activity: Activity,
             permission: String,
            callback: OnPermissionPageCallback?
        ) {
            startPermissionActivity(activity, asArrayList(permission), callback)
        }

        fun startPermissionActivity(
             activity: Activity,
             permissions: Array<String>,
            callback: OnPermissionPageCallback?
        ) {
            startPermissionActivity(activity, asArrayLists(permissions), callback)
        }

        fun startPermissionActivity(
             activity: Activity,
             permissions: List<String>,
            callback: OnPermissionPageCallback?
        ) {
            if (PermissionUtils.isActivityUnavailable(activity)) {
                return
            }
            if (permissions.isEmpty()) {
                PermissionActivityIntentHandler.startActivity(
                    activity!!, getApplicationDetailsIntent(
                        activity!!
                    )
                )
                return
            }
            val fragmentFactory: PermissionFragmentFactory<*, *> =
                generatePermissionFragmentFactory(activity)
            fragmentFactory.createAndCommitFragment(permissions, PermissionType.SPECIAL,
                object :OnPermissionFlowCallback {
                    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
                    override fun onRequestPermissionFinish() {
                        if (PermissionUtils.isActivityUnavailable(activity)) {
                            return
                        }
                        dispatchPermissionPageCallback(activity, permissions, callback)
                    }

                })
        }


        private fun generatePermissionFragmentFactory(activity: Activity): PermissionFragmentFactory<*, *> {
            return generatePermissionFragmentFactory(activity, null)
        }


        private fun generatePermissionFragmentFactory(
            activity: Activity,
            appFragment: Fragment?
        ): PermissionFragmentFactory<*, *> {
            val fragmentFactory: PermissionFragmentFactory<*, *>
            if (appFragment != null) {
                // appFragment.getChildFragmentManager 需要 minSdkVersion >=  17
                fragmentFactory = PermissionFragmentFactoryByApp(
                    appFragment.requireActivity(),
                    appFragment.childFragmentManager
                )
            } else if (activity is FragmentActivity) {
                val fragmentActivity = activity
                fragmentFactory = PermissionFragmentFactoryBySupport(
                    fragmentActivity,
                    fragmentActivity.supportFragmentManager
                )
            } else {
                fragmentFactory = PermissionFragmentFactoryByApp(activity, null)
            }
            return fragmentFactory
        }
        
        

        /**
         * 派发权限设置页回调
         */
        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        private fun dispatchPermissionPageCallback(
            context: Context,
            permissions: List<String>,
            callback: OnPermissionPageCallback?
        ) {
            if (callback == null) {
                return
            }
            if (isGrantedPermissions(context, permissions)) {
                callback.onGranted()
            } else {
                callback.onDenied()
            }
        }

    }
    









    /** 申请的权限列表  */
    
    private var mPermissions: MutableList<String> = ArrayList()


    /** App 包下的 Fragment 对象  */
    
    private var mAppFragment: Fragment? = null


    /** 权限请求拦截器  */
    
    private var mPermissionInterceptor: OnPermissionInterceptor? = null

    /** 权限请求描述  */
    
    private var mPermissionDescription: OnPermissionDescription? = null

    /** 设置不检查  */
    
    private var mCheckMode: Boolean? = null




    /**
     * 添加权限组
     */
    fun permission(@PermissionLimit  permission: String?): XXPermissions {
        if (permission == null) {
            return this
        }
        if (PermissionUtils.containsPermission(mPermissions, permission)) {
            return this
        }
        mPermissions.add(permission)
        return this;
    }

    fun permission(vararg permissions: Array<String>): XXPermissions {
        return permission(PermissionUtils.asArrayLists(*permissions))
    }

    fun permission(permissions: List<String>): XXPermissions {
        if (permissions == null || permissions.isEmpty()) {
            return this
        }
        if (mPermissions == null) {
            mPermissions = ArrayList(permissions)
            return this
        }
        for (permission in permissions) {
            if (mPermissions?.contains(permission) == true) {
                continue
            }
            mPermissions?.add(permission)
        }
        return this
    }

    /**
     * 设置权限请求拦截器
     */
    fun interceptor( permissionInterceptor: OnPermissionInterceptor?): XXPermissions {
        mPermissionInterceptor = permissionInterceptor
        return this
    }

    /**
     * 设置权限请求描述
     */
    fun description( permissionDescription: OnPermissionDescription?): XXPermissions {
        mPermissionDescription = permissionDescription
        return this
    }

    /**
     * 设置不触发错误检测机制
     */
    fun unchecked(): XXPermissions {
        mCheckMode = false
        return this
    }

    /**
     * 请求权限
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun request(callback: OnPermissionCallback) {
        if (mContext == null) {
            return
        }

        if (mPermissionInterceptor == null) {
            mPermissionInterceptor = getPermissionInterceptor()
        }

        if (mPermissionDescription == null) {
            mPermissionDescription = getPermissionDescription()
        }

        val context: Context = mContext!!

        val appFragment = mAppFragment


        val permissionInterceptor = mPermissionInterceptor

        val permissionDescription = mPermissionDescription!!

        // 权限请求列表（为什么直接不用字段？因为框架要兼容新旧权限，在低版本下会自动添加旧权限申请，为了避免重复添加）
        val permissions: MutableList<String> = ArrayList(mPermissions)

        // 从 Context 对象中获得 Activity 对象
        val activity = findActivity(context)

        if (isCheckMode(context)) {
            // 检查传入的 Activity 或者 Fragment 状态是否正常
            PermissionChecker.checkActivityStatus(activity!!)
            if (appFragment != null) {
                PermissionChecker.checkAppFragmentStatus(appFragment)
            }
            // 检查传入的权限是否正常
            PermissionChecker.checkPermissionList(permissions)
            // 获取清单文件信息
            val androidManifestInfo = getAndroidManifestInfo(context)
            // 检查申请的读取媒体位置权限是否符合规范
            PermissionChecker.checkMediaLocationPermission(context, permissions)
            // 检查申请的存储权限是否符合规范
            PermissionChecker.checkStoragePermission(context, permissions, androidManifestInfo)
            // 检查申请的传感器权限是否符合规范
            PermissionChecker.checkBodySensorsPermission(permissions)
            // 检查申请的定位权限是否符合规范
            PermissionChecker.checkLocationPermission(permissions)
            // 检查申请的画中画权限是否符合规范
            PermissionChecker.checkPictureInPicturePermission(activity, permissions, androidManifestInfo)
            // 检查申请的通知栏监听权限是否符合规范
            PermissionChecker.checkNotificationListenerPermission(permissions, androidManifestInfo)
            // 检查蓝牙和 WIFI 权限申请是否符合规范
            PermissionChecker.checkNearbyDevicesPermission(permissions, androidManifestInfo)
            // 检查对照片和视频的部分访问权限申请是否符合规范
            PermissionChecker.checkReadMediaVisualUserSelectedPermission(permissions)
            // 检查读取应用列表权限是否符合规范
            PermissionChecker.checkGetInstallAppsPermission(context, permissions, androidManifestInfo)
            // 检查申请的权限和 targetSdk 版本是否能吻合
            PermissionChecker.checkTargetSdkVersion(context, permissions)
            // 检测权限有没有在清单文件中注册
            PermissionChecker.checkManifestPermissions(context, permissions, androidManifestInfo)
        }

        // 优化所申请的权限列表
        addOldPermissionsByNewPermissions(permissions)
        // 优化申请的权限顺序
        adjustPermissionsSort(permissions)

        // 检查 Activity 是不是不可用
        if (PermissionUtils.isActivityUnavailable(activity)) {
            return
        }

        // 判断要申请的权限是否都授予了
        if (PermissionApi.isGrantedPermissions(context, permissions)) {
            // 如果是的话，就不申请权限，而是通知权限申请成功
            permissionInterceptor!!.grantedPermissionRequest(
                activity,
                permissions,
                permissions,
                true,
                callback
            )
            permissionInterceptor.finishPermissionRequest(activity!!, permissions, true, callback)
            return
        }

        // 检查 App 包下的 Fragment 是不是不可用
        if (appFragment != null && PermissionUtils.isFragmentUnavailable(appFragment)) {
            return
        }



        // 创建 Fragment 工厂
        val fragmentFactory =
            generatePermissionFragmentFactory(activity!!,appFragment)

        // 申请没有授予过的权限
        permissionInterceptor!!.launchPermissionRequest(
            activity,
            permissions,
            fragmentFactory,
            permissionDescription,
            callback
        )
    }

    /**
     * 撤销权限并杀死当前进程
     *
     * @return          返回 true 代表成功，返回 false 代表失败
     */
    fun revokeOnKill(): Boolean {
        val context = mContext ?: return false

        val permissions: List<String?> = mPermissions

        if (permissions.isEmpty()) {
            return false
        }

        if (!isAndroid13()) {
            return false
        }

        try {
            if (permissions.size == 1) {
                // API 文档：https://developer.android.google.cn/reference/android/content/Context#revokeSelfPermissionOnKill(java.lang.String)
                context.revokeSelfPermissionOnKill(permissions[0]!!)
            } else {
                // API 文档：https://developer.android.google.cn/reference/android/content/Context#revokeSelfPermissionsOnKill(java.util.Collection%3Cjava.lang.String%3E)
                context.revokeSelfPermissionsOnKill(permissions)
            }
            return true
        } catch (e: IllegalArgumentException) {
            if (isCheckMode(context)) {
                throw e
            }
            e.printStackTrace()
            return false
        }
    }

    /**
     * 当前是否为检测模式
     */
    private fun isCheckMode( context: Context): Boolean {
        if (mCheckMode == null) {
            if (sCheckMode == null) {
                sCheckMode = isDebugMode(context)
            }
            mCheckMode = sCheckMode
        }
        return mCheckMode!!
    }





    /**
     * 判断某个权限是否为特殊权限
     */
    fun isSpecialPermission( permission: String?): Boolean {
        return PermissionApi.isSpecialPermission(permission)
    }






    /**
     * 判断一个或多个权限是否被勾选了不再询问的选项
     *
     * 注意不能在请求权限之前调用，一定要在 [OnPermissionCallback.onDenied] 方法中调用
     * 如果你在应用启动后，没有申请过这个权限，然后去判断它有没有勾选不再询问的选项，这样系统会一直返回 true，也就是不再询问
     * 但是实际上还能继续申请，系统只是不想让你知道权限是否勾选了不再询问的选项，你必须要申请过这个权限，才能去判断这个权限是否勾选了不再询问的选项
     */


    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun isDoNotAskAgainPermissions(
         activity: Activity,
         permissions: List<String>
    ): Boolean {
        return PermissionApi.isDoNotAskAgainPermissions(activity!!, permissions!!)
    }












}