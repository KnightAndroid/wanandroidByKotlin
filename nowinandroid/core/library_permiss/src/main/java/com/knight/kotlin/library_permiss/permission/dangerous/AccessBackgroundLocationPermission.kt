package com.knight.kotlin.library_permiss.permission.dangerous

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.knight.kotlin.library_permiss.manifest.AndroidManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.PermissionManifestInfo
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.permission.common.DangerousPermission


/**
 * @Description
 * @Author knight
 * @Time 2025/7/10 21:00
 *
 */

class AccessBackgroundLocationPermission : DangerousPermission {
    constructor()

    private constructor(`in`: Parcel) : super(`in`)

    
    override fun getPermissionName(): String {
        return PERMISSION_NAME
    }

    override fun getPermissionGroup(): String {
        return PermissionGroups.LOCATION
    }

    override fun getFromAndroidVersion(): Int {
        return PermissionVersion.ANDROID_10
    }

    
    override fun getForegroundPermissions( context: Context?): List<IPermission> {
        // 判断当前是否运行在 Android 12 及以上
        return if (PermissionVersion.isAndroid12()) {
            // 如果是的话，那么这个前台定位权限既可以是精确定位权限也可以是模糊定位权限
            PermissionUtils.asArrayList(
                PermissionLists.getAccessFineLocationPermission(),
                PermissionLists.getAccessCoarseLocationPermission()
            )
        } else {
            // 如果不是的话，那么这个前台定位权限只能是精确定位权限
            PermissionUtils.asArrayList(PermissionLists.getAccessFineLocationPermission())
        }
    }

    override fun isGrantedPermissionByStandardVersion(
         context: Context?,
        skipRequest: Boolean
    ): Boolean {
        if (PermissionVersion.isAndroid12()) {
            // 在 Android 12 及之后的版本，前台定位权限既可以用精确定位权限也可以用模糊定位权限
            if (!PermissionLists.getAccessFineLocationPermission()
                    .isGrantedPermission(context, skipRequest) &&
                !PermissionLists.getAccessCoarseLocationPermission()
                    .isGrantedPermission(context, skipRequest)
            ) {
                return false
            }
        } else {
            // 在 Android 11 及之前的版本，前台定位权限需要精确定位权限
            if (!PermissionLists.getAccessFineLocationPermission()
                    .isGrantedPermission(context, skipRequest)
            ) {
                return false
            }
        }
        return super.isGrantedPermissionByStandardVersion(context, skipRequest)
    }

    override fun isGrantedPermissionByLowVersion(
         context: Context?,
        skipRequest: Boolean
    ): Boolean {
        return PermissionLists.getAccessFineLocationPermission()
            .isGrantedPermission(context, skipRequest)
    }

    override fun isDoNotAskAgainPermissionByStandardVersion( activity: Activity): Boolean {
        // 如果前台定位权限没有授予，那么后台定位权限不再询问的状态要跟随前台定位权限
        if (PermissionVersion.isAndroid12()) {
            // 在 Android 12 及之后的版本，前台定位权限既可以用精确定位权限也可以用模糊定位权限
            if (!PermissionLists.getAccessFineLocationPermission().isGrantedPermission(activity) &&
                !PermissionLists.getAccessCoarseLocationPermission().isGrantedPermission(activity)
            ) {
                return PermissionLists.getAccessFineLocationPermission()
                    .isDoNotAskAgainPermission(activity) &&
                        PermissionLists.getAccessCoarseLocationPermission()
                            .isDoNotAskAgainPermission(activity)
            }
        } else {
            // 在 Android 11 及之前的版本，前台定位权限需要精确定位权限
            if (!PermissionLists.getAccessFineLocationPermission().isGrantedPermission(activity)) {
                return PermissionLists.getAccessFineLocationPermission()
                    .isDoNotAskAgainPermission(activity)
            }
        }
        return super.isDoNotAskAgainPermissionByStandardVersion(activity)
    }

    override fun isDoNotAskAgainPermissionByLowVersion( activity: Activity?): Boolean {
        return PermissionLists.getAccessFineLocationPermission().isDoNotAskAgainPermission(activity)
    }

    override fun getRequestIntervalTime( context: Context?): Int {
        // 经过测试，在 Android 11 设备上面，先申请前台权限，然后立马申请后台权限大概率会出现失败
        // 这里为了避免这种情况出现，所以加了一点延迟，这样就没有什么问题了
        // 为什么延迟时间是 150 毫秒？ 经过实践得出 100 还是有概率会出现失败，但是换成 150 试了很多次就都没有问题了
        // 官方的文档地址：https://developer.android.google.cn/about/versions/11/privacy?hl=zh-cn
        return if (isSupportRequestPermission(context)) 150 else 0
    }

    protected override fun checkSelfByManifestFile(
         activity: Activity?,
         requestPermissions: List<IPermission?>?,
         androidManifestInfo: AndroidManifestInfo?,
         permissionManifestInfoList: List<PermissionManifestInfo?>?,
        @Nullable currentPermissionManifestInfo: PermissionManifestInfo
    ) {
        super.checkSelfByManifestFile(
            activity,
            requestPermissions,
            androidManifestInfo,
            permissionManifestInfoList,
            currentPermissionManifestInfo
        )
        // 如果您的应用以 Android 12 为目标平台并且您请求 ACCESS_FINE_LOCATION 权限
        // 则还必须请求 ACCESS_COARSE_LOCATION 权限。您必须在单个运行时请求中包含这两项权限
        // 如果您尝试仅请求 ACCESS_FINE_LOCATION，则系统会忽略该请求并在 Logcat 中记录以下错误消息：
        // ACCESS_FINE_LOCATION must be requested with ACCESS_COARSE_LOCATION
        // 官方适配文档：https://developer.android.google.cn/develop/sensors-and-location/location/permissions/runtime?hl=zh-cn#approximate-request
        if (PermissionVersion.getTargetVersion(activity) >= PermissionVersion.ANDROID_12) {
            checkPermissionRegistrationStatus(
                permissionManifestInfoList,
                PermissionNames.ACCESS_COARSE_LOCATION
            )
            checkPermissionRegistrationStatus(
                permissionManifestInfoList,
                PermissionNames.ACCESS_FINE_LOCATION
            )
        } else {
            checkPermissionRegistrationStatus(
                permissionManifestInfoList,
                PermissionNames.ACCESS_FINE_LOCATION
            )
        }
    }

    protected override fun checkSelfByRequestPermissions(
         activity: Activity?,
         requestPermissions: List<IPermission?>
    ) {
        super.checkSelfByRequestPermissions(activity, requestPermissions)
        // 如果您的应用以 Android 12 为目标平台并且您请求 ACCESS_FINE_LOCATION 权限
        // 则还必须请求 ACCESS_COARSE_LOCATION 权限。您必须在单个运行时请求中包含这两项权限
        // 如果您尝试仅请求 ACCESS_FINE_LOCATION，则系统会忽略该请求并在 Logcat 中记录以下错误消息：
        // ACCESS_FINE_LOCATION must be requested with ACCESS_COARSE_LOCATION
        // 官方适配文档：https://developer.android.google.cn/develop/sensors-and-location/location/permissions/runtime?hl=zh-cn#approximate-request
        require(
            !(PermissionVersion.getTargetVersion(activity) >= PermissionVersion.ANDROID_12 &&
                    PermissionUtils.containsPermission(
                        requestPermissions,
                        PermissionNames.ACCESS_COARSE_LOCATION
                    ) && !PermissionUtils.containsPermission(
                requestPermissions,
                PermissionNames.ACCESS_FINE_LOCATION
            ))
        ) { "Applying for background positioning permissions must include \"" + PermissionNames.ACCESS_FINE_LOCATION + "\"" }

        var thisPermissionIndex = -1
        var accessFineLocationPermissionIndex = -1
        var accessCoarseLocationPermissionIndex = -1
        for (i in requestPermissions.indices) {
            val permission = requestPermissions[i]
            if (PermissionUtils.equalsPermission(permission, this)) {
                thisPermissionIndex = i
            } else if (PermissionUtils.equalsPermission(
                    permission,
                    PermissionNames.ACCESS_FINE_LOCATION
                )
            ) {
                accessFineLocationPermissionIndex = i
            } else if (PermissionUtils.equalsPermission(
                    permission,
                    PermissionNames.ACCESS_COARSE_LOCATION
                )
            ) {
                accessCoarseLocationPermissionIndex = i
            }
        }

        require(!(accessFineLocationPermissionIndex != -1 && accessFineLocationPermissionIndex > thisPermissionIndex)) {
            "Please place the " + getPermissionName() +
                    "\" permission after the \"" + PermissionNames.ACCESS_FINE_LOCATION + "\" permission"
        }

        require(!(accessCoarseLocationPermissionIndex != -1 && accessCoarseLocationPermissionIndex > thisPermissionIndex)) {
            "Please place the \"" + getPermissionName() +
                    "\" permission after the \"" + PermissionNames.ACCESS_COARSE_LOCATION + "\" permission"
        }
    }

    companion object CREATOR : Parcelable.Creator<AccessBackgroundLocationPermission> {

        /** 当前权限名称，注意：该常量字段仅供框架内部使用，不提供给外部引用，
         * 如果需要获取权限名称的字符串，请直接通过 [PermissionNames] 类获取
         */
        val PERMISSION_NAME: String = PermissionNames.ACCESS_BACKGROUND_LOCATION

        override fun createFromParcel(parcel: Parcel): AccessBackgroundLocationPermission {
            return AccessBackgroundLocationPermission(parcel)
        }

        override fun newArray(size: Int): Array<AccessBackgroundLocationPermission?> {
            return arrayOfNulls(size)
        }
    }
    ✅

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
    }

    override fun describeContents(): Int {
        return 0
    }


}