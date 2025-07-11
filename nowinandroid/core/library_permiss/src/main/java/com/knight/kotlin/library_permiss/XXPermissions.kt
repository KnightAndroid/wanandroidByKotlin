

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.annotation.IntRange
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.knight.kotlin.library_permiss.DefaultPermissionDescription
import com.knight.kotlin.library_permiss.DefaultPermissionInterceptor
import com.knight.kotlin.library_permiss.OnPermissionCallback
import com.knight.kotlin.library_permiss.OnPermissionDescription
import com.knight.kotlin.library_permiss.OnPermissionInterceptor
import com.knight.kotlin.library_permiss.OnPermissionPageCallback
import com.knight.kotlin.library_permiss.core.OnPermissionFlowCallback
import com.knight.kotlin.library_permiss.fragment.factory.PermissionFragmentFactory
import com.knight.kotlin.library_permiss.fragment.factory.PermissionFragmentFactoryByApp
import com.knight.kotlin.library_permiss.fragment.factory.PermissionFragmentFactoryBySupport
import com.knight.kotlin.library_permiss.manifest.AndroidManifestParser
import com.knight.kotlin.library_permiss.permission.PermissionType
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.start.StartActivityAgent
import com.knight.kotlin.library_permiss.tools.PermissionApi
import com.knight.kotlin.library_permiss.tools.PermissionSettingPage
import com.knight.kotlin.library_permiss.tools.PermissionUtils
import com.knight.kotlin.library_permiss.tools.PermissionVersion

class XXPermissions private constructor(
    private val mContext: Context?,
    private val mAppFragment: Fragment? = null,
    private val mSupportFragment: androidx.fragment.app.Fragment? = null
) {

    companion object {
        /** Permission settings page request code */
        const val REQUEST_CODE = 1024 + 1

        /** Global permission request interceptor type */
        private var sPermissionInterceptorClass: Class<out OnPermissionInterceptor>? = null

        /** Global permission description type */
        private var sPermissionDescriptionClass: Class<out OnPermissionDescription>? = null

        /** Global check mode */
        private var sCheckMode: Boolean? = null

        /**
         * Set the request object
         *
         * @param context Current Activity, can be the top Activity in the stack
         */

        fun with(context: Context): XXPermissions {
            return XXPermissions(context)
        }


        fun with(appFragment: Fragment): XXPermissions {
            return XXPermissions(appFragment.activity)
        }
        /**
         * Set whether to enable error detection mode (global setting)
         */
        @JvmStatic
        fun setCheckMode(checkMode: Boolean) {
            sCheckMode = checkMode
        }

        /**
         * Set the permission request interceptor (global setting)
         */
        @JvmStatic
        fun setPermissionInterceptor(clazz: Class<out OnPermissionInterceptor>?) {
            sPermissionInterceptorClass = clazz
        }

        /**
         * Get the global permission request interceptor
         */
        @JvmStatic
        fun getPermissionInterceptor(): OnPermissionInterceptor {
            if (sPermissionInterceptorClass != null) {
                try {
                    return sPermissionInterceptorClass!!.newInstance()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return DefaultPermissionInterceptor()
        }

        /**
         * Set the permission description (global setting)
         *
         * The reason for only exposing Class objects instead of ordinary objects is to prevent
         * the reuse of the same object globally, which might lead to internal field conflicts.
         * The best solution to avoid this issue is to not reuse the same object.
         */
        @JvmStatic
        fun setPermissionDescription(clazz: Class<out OnPermissionDescription>?) {
            sPermissionDescriptionClass = clazz
        }

        /**
         * Get the global permission description
         */
        @JvmStatic
        fun getPermissionDescription(): OnPermissionDescription {
            if (sPermissionDescriptionClass != null) {
                try {
                    return sPermissionDescriptionClass!!.newInstance()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return DefaultPermissionDescription()
        }

        /**
         * Check if one or more permissions are all granted
         */
        @JvmStatic
        fun isGrantedPermission(context: Context, permission: IPermission): Boolean {
            return permission.isGrantedPermission(context)
        }

        @JvmStatic
        fun isGrantedPermissions(context: Context, permissions: Array<IPermission>): Boolean {
            return isGrantedPermissions(context, PermissionUtils.asArrayList(*permissions))
        }

        @JvmStatic
        fun isGrantedPermissions(context: Context, permissions: List<IPermission>): Boolean {
            return PermissionApi.isGrantedPermissions(context, permissions)
        }

        /**
         * Get granted permissions from the permission list
         */
        @JvmStatic
        fun getGrantedPermissions(context: Context, permissions: Array<IPermission>): List<IPermission> {
            return getGrantedPermissions(context, PermissionUtils.asArrayList(*permissions))
        }

        @JvmStatic
        fun getGrantedPermissions(context: Context, permissions: List<IPermission>): List<IPermission> {
            return PermissionApi.getGrantedPermissions(context, permissions)
        }

        /**
         * Get denied permissions from the permission list
         */
        @JvmStatic
        fun getDeniedPermissions(context: Context, permissions: Array<IPermission>): List<IPermission> {
            return getDeniedPermissions(context, PermissionUtils.asArrayList(*permissions))
        }

        @JvmStatic
        fun getDeniedPermissions(context: Context, permissions: List<IPermission>): List<IPermission> {
            return PermissionApi.getDeniedPermissions(context, permissions)
        }

        /**
         * Check if a permission is a special permission
         */
        @JvmStatic
        fun isSpecialPermission(permission: IPermission): Boolean {
            return permission.getPermissionType() === PermissionType.SPECIAL
        }

        /**
         * Check if the permission list contains special permissions
         */
        @JvmStatic
        fun containsSpecialPermission(permissions: Array<IPermission>): Boolean {
            return containsSpecialPermission(PermissionUtils.asArrayList(*permissions))
        }

        @JvmStatic
        fun containsSpecialPermission(permissions: List<IPermission>): Boolean {
            return PermissionApi.containsSpecialPermission(permissions)
        }

        /**
         * Check if a permission is a background permission
         */
        @JvmStatic
        fun isBackgroundPermission(context: Context, permission: IPermission): Boolean {
            return permission.isBackgroundPermission(context)
        }

        /**
         * Check if the permission list contains background permissions
         */
        @JvmStatic
        fun containsBackgroundPermission(context: Context, permissions: Array<IPermission>): Boolean {
            return containsBackgroundPermission(context, PermissionUtils.asArrayList(*permissions))
        }

        @JvmStatic
        fun containsBackgroundPermission(context: Context, permissions: List<IPermission>): Boolean {
            return PermissionApi.containsBackgroundPermission(context, permissions)
        }

        /**
         * Check if two permissions are equal
         */
        @JvmStatic
        fun equalsPermission(permission1: IPermission, permission2: IPermission): Boolean {
            return PermissionUtils.equalsPermission(permission1, permission2)
        }

        @JvmStatic
        fun equalsPermission(permission1: IPermission, permission2: String): Boolean {
            return PermissionUtils.equalsPermission(permission1, permission2)
        }

        @JvmStatic
        fun equalsPermission(permissionName1: String, permission2: String): Boolean {
            return PermissionUtils.equalsPermission(permissionName1, permission2)
        }

        /**
         * Check if the permission list contains a specific permission
         */
        @JvmStatic
        fun containsPermission(permissions: List<IPermission>, permission: IPermission): Boolean {
            return PermissionUtils.containsPermission(permissions, permission)
        }

        @JvmStatic
        fun containsPermission(permissions: List<IPermission>, permissionName: String): Boolean {
            return PermissionUtils.containsPermission(permissions, permissionName)
        }

        /**
         * Check if one or more permissions have the "Don't ask again" option checked.
         *
         * Note: Do not call this method before requesting permissions. It should only be called
         * within the `OnPermissionCallback.onDenied(List, boolean)` method.
         * If you check for "Don't ask again" without having requested the permission after app startup,
         * the system will always return true, implying "Don't ask again," but the permission can still
         * be requested. The system prevents knowing if "Don't ask again" is checked until the permission
         * has been requested.
         */
        @JvmStatic
        fun isDoNotAskAgainPermission(activity: Activity, permission: IPermission): Boolean {
            return permission.isDoNotAskAgainPermission(activity)
        }

        @JvmStatic
        fun isDoNotAskAgainPermissions(activity: Activity, permissions: Array<IPermission>): Boolean {
            return isDoNotAskAgainPermissions(activity, PermissionUtils.asArrayList(*permissions))
        }

        @JvmStatic
        fun isDoNotAskAgainPermissions(activity: Activity, permissions: List<IPermission>): Boolean {
            return PermissionApi.isDoNotAskAgainPermissions(activity, permissions)
        }

        /* android.content.Context */

        @JvmStatic
        fun startPermissionActivity(context: Context) {
            startPermissionActivity(context, ArrayList(0))
        }

        @JvmStatic
        fun startPermissionActivity(context: Context, vararg permissions: IPermission) {
            startPermissionActivity(context, PermissionUtils.asArrayList(*permissions))
        }

        /**
         * Navigate to the application permission settings page
         *
         * @param permissions The list of permissions that were not granted or were denied
         */
        @JvmStatic
        fun startPermissionActivity(context: Context, permissions: List<IPermission>) {
            val activity = PermissionUtils.findActivity(context)
            if (activity != null) {
                startPermissionActivity(activity, permissions)
                return
            }
            val intentList: MutableList<Intent?> = PermissionApi.getBestPermissionSettingIntent(context, permissions).toMutableList()
            StartActivityAgent.startActivity(context, intentList)
        }

        /* android.app.Activity */

        @JvmStatic
        fun startPermissionActivity(activity: Activity) {
            startPermissionActivity(activity, ArrayList(0))
        }

        @JvmStatic
        fun startPermissionActivity(activity: Activity, vararg permissions: IPermission) {
            startPermissionActivity(activity, PermissionUtils.asArrayList(*permissions))
        }

        @JvmStatic
        fun startPermissionActivity(activity: Activity, permissions: List<IPermission>) {
            startPermissionActivity(activity, permissions, REQUEST_CODE)
        }

        @JvmStatic
        fun startPermissionActivity(
            activity: Activity,
            permissions: List<IPermission>,
            @IntRange(from = 1, to = 65535) requestCode: Int
        ) {

            val intentList: MutableList<Intent?> = PermissionApi.getBestPermissionSettingIntent(activity, permissions).toMutableList()
            StartActivityAgent.startActivityForResult(
                activity,
                intentList, requestCode
            )
        }

        @JvmStatic
        fun startPermissionActivity(
            activity: Activity,
            permission: IPermission,
            callback: OnPermissionPageCallback?
        ) {
            startPermissionActivity(activity, PermissionUtils.asArrayList(permission), callback)
        }

        @JvmStatic
        fun startPermissionActivity(
            activity: Activity,
            permissions: List<IPermission>,
            callback: OnPermissionPageCallback?
        ) {
            if (PermissionUtils.isActivityUnavailable(activity)) {
                return
            }
            if (permissions.isEmpty()) {

                val intentList: MutableList<Intent?> = PermissionSettingPage.getCommonPermissionSettingIntent(activity).toMutableList()

                StartActivityAgent.startActivity(activity, intentList)
                return
            }
            val fragmentFactory = generatePermissionFragmentFactory(activity)
            fragmentFactory.createAndCommitFragment(
                permissions,
                PermissionType.SPECIAL,
                object : OnPermissionFlowCallback {
                    override fun onRequestPermissionFinish() {
                        if (PermissionUtils.isActivityUnavailable(activity)) {
                            return
                        }
                        dispatchPermissionPageCallback(activity, permissions, callback)
                    }
                }
            )
        }

        /* android.app.Fragment */
        @JvmStatic
        fun startPermissionActivity(appFragment: Fragment) {
            startPermissionActivityWithList(appFragment, emptyList())
        }

        @JvmStatic
        fun startPermissionActivityWithVarargs(appFragment: Fragment, vararg permissions: IPermission) {
            startPermissionActivityWithList(appFragment, permissions.toList())
        }

        @JvmStatic
        fun startPermissionActivityWithList(
            appFragment: Fragment,
            permissions: List<IPermission>,
            @IntRange(from = 1, to = 65535) requestCode: Int = REQUEST_CODE
        ) {
            if (PermissionUtils.isFragmentUnavailable(appFragment)) return
            val activity = appFragment.activity ?: return
            if (PermissionUtils.isActivityUnavailable(activity)) return

            if (permissions.isEmpty()) {


                val intentList: MutableList<Intent?> = PermissionSettingPage.getCommonPermissionSettingIntent(activity).toMutableList()
                StartActivityAgent.startActivity(appFragment, intentList)
                return
            }

            val intentTempList: MutableList<Intent?> = PermissionApi.getBestPermissionSettingIntent(activity, permissions).toMutableList()
            StartActivityAgent.startActivityForResult(
                appFragment,
                intentTempList,
                requestCode
            )
        }

        @JvmStatic
        fun startPermissionActivityWithCallback(
            appFragment: Fragment,
            permission: IPermission,
            callback: OnPermissionPageCallback?
        ) {
            startPermissionActivityWithCallbackList(appFragment, listOf(permission), callback)
        }

        @JvmStatic
        fun startPermissionActivityWithCallbackList(
            appFragment: Fragment,
            permissions: List<IPermission>,
            callback: OnPermissionPageCallback?
        ) {
            if (PermissionUtils.isFragmentUnavailable(appFragment)) return
            val activity = appFragment.activity ?: return
            if (PermissionUtils.isActivityUnavailable(activity)) return

            if (permissions.isEmpty()) {
                val intentList: MutableList<Intent?> = PermissionSettingPage.getCommonPermissionSettingIntent(activity).toMutableList()

                StartActivityAgent.startActivity(appFragment, intentList)
                return
            }

            val fragmentFactory = generatePermissionFragmentFactory(activity, appFragment)
            fragmentFactory.createAndCommitFragment(
                permissions,
                PermissionType.SPECIAL,
                object : OnPermissionFlowCallback {
                    override fun onRequestPermissionFinish() {
                        if (PermissionUtils.isActivityUnavailable(activity)  || PermissionUtils.isFragmentUnavailable(appFragment)) {
                            return
                        }
                        dispatchPermissionPageCallback(activity, permissions, callback)
                    }
                }
            )
        }


        /**
         * Create Fragment Factory
         */
        private fun generatePermissionFragmentFactory(activity: Activity): PermissionFragmentFactory<*, *> {
            return generatePermissionFragmentFactory(activity, null)
        }


        private fun generatePermissionFragmentFactory(
            activity: Activity,
            appFragment: Fragment?
        ): PermissionFragmentFactory<*, *> {
            val fragmentFactory: PermissionFragmentFactory<*, *>
            if (appFragment != null) {
                // appFragment.getChildFragmentManager requires minSdkVersion >= 17
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
         * Dispatch permission settings page callback
         */
        private fun dispatchPermissionPageCallback(
            context: Context,
            permissions: List<IPermission>,
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

    /** Applied permission list */
    private val mPermissions: MutableList<IPermission> = ArrayList()

    /** Permission request interceptor */
    private var mPermissionInterceptor: OnPermissionInterceptor? = null

    /** Permission request description */
    private var mPermissionDescription: OnPermissionDescription? = null

    /** Do not check mode */
    private var mCheckMode: Boolean? = null

    private constructor(context: Context) : this(context, null, null)

    private constructor(appFragment: Fragment) : this(appFragment.activity, appFragment, null)



    /**
     * Add a single permission
     */
    fun permission(permission: IPermission): XXPermissions {
        // This writing ensures that if a permission is added repeatedly, the last one takes precedence
        mPermissions.remove(permission)
        mPermissions.add(permission)
        return this
    }

    /**
     * Add multiple permissions
     */
    fun permissions(permissions: List<IPermission>): XXPermissions {
        if (permissions.isEmpty()) {
            return this
        }
        for (i in permissions.indices) {
            permission(permissions[i])
        }
        return this
    }

    fun permissions(vararg permissions: IPermission): XXPermissions {
        return permissions(PermissionUtils.asArrayList(*permissions))
    }

    /**
     * Set the permission request interceptor
     */
    fun interceptor(permissionInterceptor: OnPermissionInterceptor?): XXPermissions {
        mPermissionInterceptor = permissionInterceptor
        return this
    }

    /**
     * Set the permission request description
     */
    fun description(permissionDescription: OnPermissionDescription?): XXPermissions {
        mPermissionDescription = permissionDescription
        return this
    }

    /**
     * Set to not trigger error detection mechanism
     */
    fun unchecked(): XXPermissions {
        mCheckMode = false
        return this
    }

    /**
     * Request permissions
     */
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

        val context = mContext
        val appFragment = mAppFragment
        val permissionInterceptor = mPermissionInterceptor!!
        val permissionDescription = mPermissionDescription!!

        // Permission request list (why not directly use the field? Because the framework needs to be compatible with new and old permissions,
        // and old permissions are automatically added in lower versions to avoid duplicate additions)
        val permissions: MutableList<IPermission> = ArrayList(mPermissions)

        // Get Activity object from Context object
        val activity = PermissionUtils.findActivity(context)

        if (isCheckMode(context)) {
            // Check if the passed Activity or Fragment status is normal
            activity?.let { PermissionChecker.checkActivityStatus(it) }
            if (appFragment != null) {
                PermissionChecker.checkAppFragmentStatus(appFragment)
            }
            // Check if the passed permissions are normal
            PermissionChecker.checkPermissionList(activity!!, permissions, AndroidManifestParser.getAndroidManifestInfo(context)!!)
        }

        // Check if Activity is unavailable
        if (PermissionUtils.isActivityUnavailable(activity)) {
            return
        }

        // Optimize the requested permission list
        PermissionApi.addOldPermissionsByNewPermissions(activity!!, permissions)

        // Determine if all requested permissions have been granted
        if (PermissionApi.isGrantedPermissions(context, permissions)) {
            // If yes, do not request permissions, but notify that permissions have been granted
            permissionInterceptor.grantedPermissionRequest(activity, permissions, permissions, true, callback)
            permissionInterceptor.finishPermissionRequest(activity, permissions, true, callback)
            return
        }

        // Check if App package Fragment is unavailable
        if (appFragment != null && PermissionUtils.isFragmentUnavailable(appFragment)) {
            return
        }



        // Create Fragment factory
        val fragmentFactory = generatePermissionFragmentFactory(activity,appFragment)

        // Request ungranted permissions
        permissionInterceptor.launchPermissionRequest(activity, permissions, fragmentFactory, permissionDescription, callback)
    }

    /**
     * Revoke permissions and kill the current process
     *
     * @return true for success, false for failure
     */
    fun revokeOnKill(): Boolean {
        val context = mContext ?: return false

        val permissions = mPermissions

        if (permissions.isEmpty()) {
            return false
        }

        if (!PermissionVersion.isAndroid13()) {
            return false
        }

        try {
            if (permissions.size == 1) {
                // API Doc: https://developer.android.google.cn/reference/android/content/Context#revokeSelfPermissionOnKill(java.lang.String)
                context.revokeSelfPermissionOnKill(permissions[0].getPermissionName())
            } else {
                // API Doc: https://developer.android.google.cn/reference/android/content/Context#revokeSelfPermissionsOnKill(java.util.Collection%3Cjava.lang.String%3E)
                context.revokeSelfPermissionsOnKill(PermissionUtils.convertPermissionList(permissions))
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
     * Check if currently in check mode
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