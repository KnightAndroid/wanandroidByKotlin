package com.knight.kotlin.library_permiss.permission.base

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Parcel
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.manifest.AndroidManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.PermissionManifestInfo
import java.lang.reflect.InvocationTargetException


/**
 * @Description
 * @Author knight
 * @Time 2025/7/10 20:52
 *
 */

abstract class BasePermission : IPermission {
    protected constructor()

    protected constructor(`in`: Parcel?)

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel( dest: Parcel, flags: Int) {}

    
    override fun toString(): String {
        return getPermissionName()!!
    }

    override fun equals(obj: Any?): Boolean {
        // 如果要对比的对象和当前对象的内存地址一样，那么就返回 true
        if (obj === this) {
            return true
        }
        // 重写 equals 方法是为了 List 和 Map 集合有能力辨别不同的权限对象是不是来自同一个权限
        // 如果这两个权限对象的名称一样，那么就认为它们是同一个权限
        if (obj is IPermission) {
            return PermissionUtils.equalsPermission(this, obj)
        } else if (obj is String) {
            return PermissionUtils.equalsPermission(this, obj)
        }
        return false
    }

    
    protected fun getPackageNameUri( context: Context?): Uri {
        return PermissionUtils.getPackageNameUri(context)
    }

    
    protected fun getApplicationDetailsSettingIntent( context: Context?): Intent {
        return PermissionSettingPage.getApplicationDetailsSettingsIntent(context, this)
    }

    
    protected fun getAndroidSettingIntent(): Intent {
        return PermissionSettingPage.getAndroidSettingsIntent()
    }

    override fun checkCompliance(
         activity: Activity?,
         requestPermissions: List<IPermission?>?,
         androidManifestInfo: AndroidManifestInfo?
    ) {
        // 检查 targetSdkVersion 是否符合要求
        checkSelfByTargetSdkVersion(activity)
        // 检查 AndroidManifest.xml 是否符合要求
        if (androidManifestInfo != null) {
            val permissionManifestInfoList = androidManifestInfo.permissionManifestInfoList
            val currentPermissionManifestInfo =
                findPermissionInfoByList(permissionManifestInfoList, getPermissionName())
            checkSelfByManifestFile(
                activity, requestPermissions, androidManifestInfo, permissionManifestInfoList,
                currentPermissionManifestInfo!!
            )
        }
        // 检查请求的权限列表是否符合要求
        checkSelfByRequestPermissions(activity, requestPermissions)
    }

    /**
     * 检查 targetSdkVersion 是否符合要求，如果不合规则会抛出异常
     */
    protected fun checkSelfByTargetSdkVersion( context: Context?) {
        val minTargetSdkVersion = getMinTargetSdkVersion()
        // 必须设置正确的 targetSdkVersion 才能正常检测权限
        if (PermissionVersion.getTargetVersion(context) >= minTargetSdkVersion) {
            return
        }

        throw IllegalStateException(
            "Request \"" + getPermissionName() + "\" permission, " +
                    "The targetSdkVersion SDK must be " + minTargetSdkVersion +
                    " or more, if you do not want to upgrade targetSdkVersion, " +
                    "please apply with the old permission"
        )
    }

    /**
     * 当前权限是否在清单文件中静态注册
     */
    protected abstract fun isRegisterPermissionByManifestFile(): Boolean

    /**
     * 检查 AndroidManifest.xml 是否符合要求，如果不合规则会抛出异常
     */
    protected fun checkSelfByManifestFile(
         activity: Activity?,
         requestPermissions: List<IPermission?>?,
         androidManifestInfo: AndroidManifestInfo?,
         permissionManifestInfoList: List<PermissionManifestInfo>?,
         currentPermissionManifestInfo: PermissionManifestInfo
    ) {
        if (!isRegisterPermissionByManifestFile()) {
            return
        }
        // 检查当前权限有没有在清单文件中静态注册，如果有注册，还要检查注册 maxSdkVersion 属性有没有问题
        checkPermissionRegistrationStatus(currentPermissionManifestInfo, getPermissionName())
    }

    /**
     * 检查请求的权限列表是否符合要求，如果不合规则会抛出异常
     */
    protected fun checkSelfByRequestPermissions(
         activity: Activity?,
         requestPermissions: List<IPermission?>?
    ) {
        // default implementation ignored
        // 默认无任何实现，交由子类自己去实现
    }

    companion object {
        @get:NonNull
        protected val manageApplicationSettingIntent: Intent
            get() = PermissionSettingPage.getManageApplicationSettingsIntent()

        @get:NonNull
        protected val applicationSettingIntent: Intent
            get() = PermissionSettingPage.getApplicationSettingsIntent()

        protected fun checkPermissionRegistrationStatus(
             permissionManifestInfoList: List<PermissionManifestInfo>?,
             checkPermission: String?,
            lowestMaxSdkVersion: Int = Int.MAX_VALUE
        ) {
            var permissionManifestInfo: PermissionManifestInfo? = null
            if (permissionManifestInfoList != null) {
                permissionManifestInfo =
                    findPermissionInfoByList(permissionManifestInfoList, checkPermission)
            }
            checkPermissionRegistrationStatus(
                permissionManifestInfo!!,
                checkPermission,
                lowestMaxSdkVersion
            )
        }

        /**
         * 检查权限的注册状态，如果是则会抛出异常
         */
        protected fun checkPermissionRegistrationStatus(
             permissionManifestInfo: PermissionManifestInfo,
             checkPermission: String?,
            lowestMaxSdkVersion: Int = Int.MAX_VALUE
        ) {
            checkNotNull(permissionManifestInfo) {
                "Please register permissions in the AndroidManifest.xml file " +
                        "<uses-permission android:name=\"" + checkPermission + "\" />"
            }

            val manifestMaxSdkVersion = permissionManifestInfo.maxSdkVersion
            require(manifestMaxSdkVersion >= lowestMaxSdkVersion) {
                "The AndroidManifest.xml file " +
                        "<uses-permission android:name=\"" + checkPermission +
                        "\" android:maxSdkVersion=\"" + manifestMaxSdkVersion +
                        "\" /> does not meet the requirements, " +
                        (if (lowestMaxSdkVersion != Int.MAX_VALUE) "the minimum requirement for maxSdkVersion is $lowestMaxSdkVersion" else "please delete the android:maxSdkVersion=\"$manifestMaxSdkVersion\" attribute")
            }
        }

        /**
         * 获得当前项目的 minSdkVersion
         */
        protected fun getMinSdkVersion(
             context: Context,
             androidManifestInfo: AndroidManifestInfo?
        ): Int {
            if (PermissionVersion.isAndroid7()) {
                return context.applicationInfo.minSdkVersion
            }

            if (androidManifestInfo?.usesSdkManifestInfo == null) {
                return PermissionVersion.ANDROID_4_2
            }
            return androidManifestInfo.usesSdkManifestInfo!!.minSdkVersion
        }

        /**
         * 从权限列表中获取指定的权限信息
         */
        
        fun findPermissionInfoByList(
             permissionManifestInfoList: List<PermissionManifestInfo>,
             permissionName: String?
        ): PermissionManifestInfo? {
            var permissionManifestInfo: PermissionManifestInfo? = null
            for (info in permissionManifestInfoList) {
                if (PermissionUtils.equalsPermission(info.name, permissionName)) {
                    permissionManifestInfo = info
                    break
                }
            }
            return permissionManifestInfo
        }

        /**
         * 判断某个危险权限是否授予了
         */
        @RequiresApi(PermissionVersion.ANDROID_6)
        fun checkSelfPermission( context: Context,  permission: String): Boolean {
            return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        }

        /**
         * 判断是否应该向用户显示请求权限的理由
         */
        @RequiresApi(PermissionVersion.ANDROID_6)
        fun shouldShowRequestPermissionRationale(
             activity: Activity,
             permission: String?
        ): Boolean {
            // 解决 Android 12 调用 shouldShowRequestPermissionRationale 出现内存泄漏的问题
            // Android 12L 和 Android 13 版本经过测试不会出现这个问题，证明 Google 在新版本上已经修复了这个问题
            // 但是对于 Android 12 仍是一个历史遗留问题，这是我们所有 Android App 开发者不得不面对的一个事情
            // issue 地址：https://github.com/getActivity/XXPermissions/issues/133
            if (PermissionVersion.getCurrentVersion() === PermissionVersion.ANDROID_12) {
                try {
                    // 另外针对这个问题，我还给谷歌的 AndroidX 项目无偿提供了解决方案，目前 Merge Request 已被合入主分支
                    // 我相信通过这一举措，将解决全球近 10 亿台 Android 12 设备出现的内存泄露问题
                    // Pull Request 地址：https://github.com/androidx/androidx/pull/435
                    val packageManager: PackageManager =
                        activity.getApplication().getPackageManager()
                    val method = PackageManager::class.java.getMethod(
                        "shouldShowRequestPermissionRationale",
                        String::class.java
                    )
                    return method.invoke(packageManager, permission) as Boolean
                } catch (e: NoSuchMethodException) {
                    e.printStackTrace()
                } catch (e: InvocationTargetException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
            }
            return activity.shouldShowRequestPermissionRationale(permission)
        }

        /**
         * 通过 AppOpsManager 判断某个权限是否授予
         *
         * @param opName               需要传入 [AppOpsManager] 类中的以 OPSTR 开头的字段
         * @param defaultGranted       当判断不了该权限状态的时候，是否返回已授予状态
         */
        @RequiresApi(PermissionVersion.ANDROID_4_4)
        fun checkOpPermission(
             context: Context,
             opName: String,
            defaultGranted: Boolean
        ): Boolean {
            val opMode = getOpPermissionMode(context, opName)
            if (opMode == AppOpsManager.MODE_ERRORED) {
                return defaultGranted
            }
            return opMode == AppOpsManager.MODE_ALLOWED
        }

        /**
         * 通过 AppOpsManager 判断某个权限是否授予
         *
         * @param opFieldName               要反射 [AppOpsManager] 类中的字段名称
         * @param opDefaultValue            当反射获取不到对应字段的值时，该值作为替补
         * @param defaultGranted            当判断不了该权限状态的时候，是否返回已授予状态
         */
        @RequiresApi(PermissionVersion.ANDROID_4_4)
        fun checkOpPermission(
            context: Context,
            opFieldName: String,
            opDefaultValue: Int,
            defaultGranted: Boolean
        ): Boolean {
            val opMode = getOpPermissionMode(context, opFieldName, opDefaultValue)
            if (opMode == AppOpsManager.MODE_ERRORED) {
                return defaultGranted
            }
            return opMode == AppOpsManager.MODE_ALLOWED
        }

        /**
         * 获取 AppOpsManager 某个权限的状态
         *
         * @param opName               需要传入 [AppOpsManager] 类中的以 OPSTR 开头的字段
         */
        @RequiresApi(PermissionVersion.ANDROID_4_4)
        @Suppress("deprecation")
        fun getOpPermissionMode( context: Context,  opName: String): Int {
            val appOpsManager = if (PermissionVersion.isAndroid6()) {
                context.getSystemService<AppOpsManager>(AppOpsManager::class.java)
            } else {
                context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            }
            // 虽然这个 SystemService 永远不为空，但是不怕一万，就怕万一，开展防御性编程
            if (appOpsManager == null) {
                return AppOpsManager.MODE_ERRORED
            }
            try {
                return if (PermissionVersion.isAndroid10()) {
                    appOpsManager.unsafeCheckOpNoThrow(
                        opName,
                        context.applicationInfo.uid,
                        context.packageName
                    )
                } else {
                    appOpsManager.checkOpNoThrow(
                        opName,
                        context.applicationInfo.uid,
                        context.packageName
                    )
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                return AppOpsManager.MODE_ERRORED
            }
        }

        /**
         * 获取 AppOpsManager 某个权限的状态
         *
         * @param opName                要反射 [AppOpsManager] 类中的字段名称
         * @param opDefaultValue        当反射获取不到对应字段的值时，该值作为替补
         */
        @RequiresApi(PermissionVersion.ANDROID_4_4)
        fun getOpPermissionMode(
            context: Context,
             opName: String,
            opDefaultValue: Int
        ): Int {
            val appOpsManager = if (PermissionVersion.isAndroid6()) {
                context.getSystemService<AppOpsManager>(AppOpsManager::class.java)
            } else {
                context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            }
            // 虽然这个 SystemService 永远不为空，但是不怕一万，就怕万一，开展防御性编程
            if (appOpsManager == null) {
                return AppOpsManager.MODE_ERRORED
            }
            try {
                val appOpsClass = Class.forName(AppOpsManager::class.java.name)
                var opValue: Int
                try {
                    val opField: Field = appOpsClass.getDeclaredField(opName)
                    opValue = opField.get(Int::class.java)
                } catch (e: NoSuchFieldException) {
                    opValue = opDefaultValue
                }
                val checkOpNoThrowMethod = appOpsClass.getMethod(
                    "checkOpNoThrow", Integer.TYPE, Integer.TYPE,
                    String::class.java
                )
                return (checkOpNoThrowMethod.invoke(
                    appOpsManager,
                    opValue,
                    context.applicationInfo.uid,
                    context.packageName
                ) as Int)
            } catch (e: Exception) {
                e.printStackTrace()
                return AppOpsManager.MODE_ERRORED
            }
        }

        /**
         * 判断 AppOpsManager 是否存在某个 Op 权限
         *
         * @param opName                要反射 [AppOpsManager] 类中的字段名称
         */
        @RequiresApi(PermissionVersion.ANDROID_4_4)
        fun isExistOpPermission(opName: String): Boolean {
            try {
                val appOpsClass = Class.forName(AppOpsManager::class.java.name)
                appOpsClass.getDeclaredField(opName)
                // 证明有这个字段，返回 true
                return true
            } catch (ignored: Exception) {
                // default implementation ignored
                return false
            }
        }
    }
}