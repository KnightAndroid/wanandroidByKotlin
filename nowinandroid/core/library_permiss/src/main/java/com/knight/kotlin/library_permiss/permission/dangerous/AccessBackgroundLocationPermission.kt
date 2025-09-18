package com.knight.kotlin.library_permiss.permission.dangerous

import android.app.Activity
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.core.library_devicecompat.DeviceOs
import com.knight.kotlin.library_permiss.manifest.AndroidManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.PermissionManifestInfo
import com.knight.kotlin.library_permiss.permission.PermissionGroups
import com.knight.kotlin.library_permiss.permission.PermissionLists.getAccessCoarseLocationPermission
import com.knight.kotlin.library_permiss.permission.PermissionLists.getAccessFineLocationPermission
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.PermissionPageType
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.permission.common.DangerousPermission
import com.knight.kotlin.library_permiss.tools.PermissionUtils
import com.knight.kotlin.library_permiss.tools.PermissionVersion
import com.knight.kotlin.library_permiss.tools.PermissionVersion.getTargetVersion
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid10
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid11
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid12


/**
 * @Description 后台定位权限类
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

    
    override fun getPermissionPageType( context: Context): PermissionPageType {
        // 后台定位权限在 HyperOS 或者 MIUI 上面一直是透明的 Activity
        if (DeviceOs.isHyperOs() || DeviceOs.isMiui()) {
            return PermissionPageType.TRANSPARENT_ACTIVITY
        }
        // 后台定位权限在 MagicOS 上面一直是透明的 Activity
        if (DeviceOs.isMagicOs()) {
            return PermissionPageType.TRANSPARENT_ACTIVITY
        }
        // 后台定位权限在 HarmonyOS 上面一直是透明的 Activity
        if (DeviceOs.isHarmonyOs()) {
            return PermissionPageType.TRANSPARENT_ACTIVITY
        }
        // 后台定位权限申请页在 Android 10 还是透明的 Activity，到了 Android 11 就变成了不透明的 Activity
        if (isAndroid10() && !isAndroid11()) {
            return PermissionPageType.TRANSPARENT_ACTIVITY
        }
        return PermissionPageType.OPAQUE_ACTIVITY
    }

    override fun getPermissionGroup(context: Context): String {
        return PermissionGroups.LOCATION
    }

    override fun getFromAndroidVersion( context: Context): Int {
        return PermissionVersion.ANDROID_10
    }

    
    override fun getForegroundPermissions( context: Context): List<IPermission> {
        // 判断当前是否运行在 Android 12 及以上
        return if (isAndroid12()) {
            // 如果是的话，那么这个前台定位权限既可以是精确定位权限也可以是模糊定位权限
            PermissionUtils.asArrayList(getAccessFineLocationPermission(), getAccessCoarseLocationPermission())
        } else {
            // 如果不是的话，那么这个前台定位权限只能是精确定位权限
            PermissionUtils.asArrayList(getAccessFineLocationPermission())
        }
    }

    override fun isBackgroundPermission( context: Context): Boolean {
        // 表示当前权限是后台权限
        return true
    }

    override fun isGrantedPermissionByStandardVersion( context: Context, skipRequest: Boolean): Boolean {
        if (isAndroid12()) {
            // 在 Android 12 及之后的版本，前台定位权限既可以用精确定位权限也可以用模糊定位权限
            if (!getAccessFineLocationPermission().isGrantedPermission(context, skipRequest) &&
                !getAccessCoarseLocationPermission().isGrantedPermission(context, skipRequest)
            ) {
                return false
            }
        } else {
            // 在 Android 11 及之前的版本，前台定位权限需要精确定位权限
            if (!getAccessFineLocationPermission().isGrantedPermission(context, skipRequest)) {
                return false
            }
        }
        return super.isGrantedPermissionByStandardVersion(context, skipRequest)
    }

    override fun isGrantedPermissionByLowVersion( context: Context, skipRequest: Boolean): Boolean {
        return getAccessFineLocationPermission().isGrantedPermission(context, skipRequest)
    }

    override fun isDoNotAskAgainPermissionByStandardVersion( activity: Activity): Boolean {
        // 如果前台定位权限没有授予，那么后台定位权限不再询问的状态要跟随前台定位权限
        if (isAndroid12()) {
            // 在 Android 12 及之后的版本，前台定位权限既可以用精确定位权限也可以用模糊定位权限
            if (!getAccessFineLocationPermission().isGrantedPermission(activity) &&
                !getAccessCoarseLocationPermission().isGrantedPermission(activity)
            ) {
                return getAccessFineLocationPermission().isDoNotAskAgainPermission(activity) &&
                        getAccessCoarseLocationPermission().isDoNotAskAgainPermission(activity)
            }
        } else {
            // 在 Android 11 及之前的版本，前台定位权限需要精确定位权限
            if (!getAccessFineLocationPermission().isGrantedPermission(activity)) {
                return getAccessFineLocationPermission().isDoNotAskAgainPermission(activity)
            }
        }
        return super.isDoNotAskAgainPermissionByStandardVersion(activity)
    }

    override fun isDoNotAskAgainPermissionByLowVersion( activity: Activity): Boolean {
        return getAccessFineLocationPermission().isDoNotAskAgainPermission(activity)
    }

    override fun getRequestIntervalTime( context: Context): Int {
        // 经过测试，在 Android 11 设备上面，先申请前台权限，然后立马申请后台权限大概率会出现失败
        // 这里为了避免这种情况出现，所以加了一点延迟，这样就没有什么问题了
        // 为什么延迟时间是 150 毫秒？ 经过实践得出 100 还是有概率会出现失败，但是换成 150 试了很多次就都没有问题了
        // 官方的文档地址：https://developer.android.google.cn/about/versions/11/privacy?hl=zh-cn
        return if (isSupportRequestPermission(context)) 150 else 0
    }

    override fun checkSelfByManifestFile(
         activity: Activity,
         requestList: List<IPermission>,
         manifestInfo: AndroidManifestInfo,
         permissionInfoList: List<PermissionManifestInfo>,
         currentPermissionInfo: PermissionManifestInfo
    ) {
        super.checkSelfByManifestFile(activity, requestList, manifestInfo, permissionInfoList, currentPermissionInfo)
        // 如果您的应用以 Android 12 为目标平台并且您请求 ACCESS_FINE_LOCATION 权限
        // 则还必须请求 ACCESS_COARSE_LOCATION 权限。您必须在单个运行时请求中包含这两项权限
        // 如果您尝试仅请求 ACCESS_FINE_LOCATION，则系统会忽略该请求并在 Logcat 中记录以下错误消息：
        // ACCESS_FINE_LOCATION must be requested with ACCESS_COARSE_LOCATION
        // 官方适配文档：https://developer.android.google.cn/develop/sensors-and-location/location/permissions/runtime?hl=zh-cn#approximate-request
        if (getTargetVersion(activity) >= PermissionVersion.ANDROID_12) {
            checkPermissionRegistrationStatus(permissionInfoList, PermissionNames.ACCESS_COARSE_LOCATION)
            checkPermissionRegistrationStatus(permissionInfoList, PermissionNames.ACCESS_FINE_LOCATION)
        } else {
            checkPermissionRegistrationStatus(permissionInfoList, PermissionNames.ACCESS_FINE_LOCATION)
        }
    }

   override fun checkSelfByRequestPermissions( activity: Activity,  requestList: List<IPermission>) {
        super.checkSelfByRequestPermissions(activity, requestList)
        // 如果您的应用以 Android 12 为目标平台并且您请求 ACCESS_FINE_LOCATION 权限
        // 则还必须请求 ACCESS_COARSE_LOCATION 权限。您必须在单个运行时请求中包含这两项权限
        // 如果您尝试仅请求 ACCESS_FINE_LOCATION，则系统会忽略该请求并在 Logcat 中记录以下错误消息：
        // ACCESS_FINE_LOCATION must be requested with ACCESS_COARSE_LOCATION
        // 官方适配文档：https://developer.android.google.cn/develop/sensors-and-location/location/permissions/runtime?hl=zh-cn#approximate-request
        require(
            !(getTargetVersion(activity) >= PermissionVersion.ANDROID_12 &&
                    PermissionUtils.containsPermission(requestList, PermissionNames.ACCESS_COARSE_LOCATION) && !PermissionUtils.containsPermission(
                requestList,
                PermissionNames.ACCESS_FINE_LOCATION
            ))
        ) {
            "Applying for background positioning permissions must include \"" +
                    PermissionNames.ACCESS_FINE_LOCATION + "\""
        }

        var thisPermissionIndex = -1
        var accessFineLocationPermissionIndex = -1
        var accessCoarseLocationPermissionIndex = -1
        for (i in requestList.indices) {
            val permission = requestList[i]
            if (PermissionUtils.equalsPermission(permission!!, this)) {
                thisPermissionIndex = i
            } else if (PermissionUtils.equalsPermission(permission, PermissionNames.ACCESS_FINE_LOCATION)) {
                accessFineLocationPermissionIndex = i
            } else if (PermissionUtils.equalsPermission(permission, PermissionNames.ACCESS_COARSE_LOCATION)) {
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

    companion object {
        val PERMISSION_NAME: String = PermissionNames.ACCESS_BACKGROUND_LOCATION
        @JvmField
        val CREATOR : Parcelable.Creator<AccessBackgroundLocationPermission> =


            object : Parcelable.Creator<AccessBackgroundLocationPermission> {
                /** 当前权限名称，注意：该常量字段仅供框架内部使用，不提供给外部引用，
                 * 如果需要获取权限名称的字符串，请直接通过 [PermissionNames] 类获取
                 */

                override fun createFromParcel(source: Parcel): AccessBackgroundLocationPermission {
                    return AccessBackgroundLocationPermission(source)
                }

                override fun newArray(size: Int): Array<AccessBackgroundLocationPermission?> {
                    return arrayOfNulls(size)
                }
            }
    }


}