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
 * @Time 2025/7/10 21:33
 *
 */

class BodySensorsBackgroundPermission : DangerousPermission {
    constructor()

    private constructor(`in`: Parcel) : super(`in`)

    @NonNull
    override fun getPermissionName(): String {
        return PERMISSION_NAME
    }

    override fun getPermissionGroup(): String {
        return PermissionGroups.SENSORS
    }

    override fun getFromAndroidVersion(): Int {
        return PermissionVersion.ANDROID_13
    }

    @NonNull
    override fun getForegroundPermissions(@NonNull context: Context?): List<IPermission> {
        return PermissionUtils.asArrayList(PermissionLists.getBodySensorsPermission())
    }

    override fun isGrantedPermissionByStandardVersion(
        @NonNull context: Context?,
        skipRequest: Boolean
    ): Boolean {
        // 判断后台传感器权限授予之前，需要先判断前台传感器权限是否授予，如果前台传感器权限没有授予，那么后台传感器权限就算授予了也没用
        if (!PermissionLists.getBodySensorsPermission().isGrantedPermission(context, skipRequest)) {
            return false
        }
        return super.isGrantedPermissionByStandardVersion(context, skipRequest)
    }

    override fun isGrantedPermissionByLowVersion(
        @NonNull context: Context?,
        skipRequest: Boolean
    ): Boolean {
        return PermissionLists.getBodySensorsPermission().isGrantedPermission(context, skipRequest)
    }

    override fun isDoNotAskAgainPermissionByStandardVersion(@NonNull activity: Activity): Boolean {
        // 如果前台传感器权限没有授予，那么后台传感器权限不再询问的状态要跟随前台传感器权限
        if (!PermissionLists.getBodySensorsPermission().isGrantedPermission(activity)) {
            return PermissionLists.getBodySensorsPermission().isDoNotAskAgainPermission(activity)
        }
        return super.isDoNotAskAgainPermissionByStandardVersion(activity)
    }

    override fun isDoNotAskAgainPermissionByLowVersion(@NonNull activity: Activity?): Boolean {
        return PermissionLists.getBodySensorsPermission().isDoNotAskAgainPermission(activity)
    }

    override fun getRequestIntervalTime(@NonNull context: Context?): Int {
        // 经过测试，在 Android 13 设备上面，先申请前台权限，然后立马申请后台权限大概率会出现失败
        // 这里为了避免这种情况出现，所以加了一点延迟，这样就没有什么问题了
        // 为什么延迟时间是 150 毫秒？ 经过实践得出 100 还是有概率会出现失败，但是换成 150 试了很多次就都没有问题了
        return if (isSupportRequestPermission(context)) 150 else 0
    }

    protected override fun checkSelfByManifestFile(
        @NonNull activity: Activity?,
        @NonNull requestPermissions: List<IPermission?>?,
        @NonNull androidManifestInfo: AndroidManifestInfo?,
        @NonNull permissionManifestInfoList: List<PermissionManifestInfo?>?,
        @Nullable currentPermissionManifestInfo: PermissionManifestInfo
    ) {
        super.checkSelfByManifestFile(
            activity, requestPermissions, androidManifestInfo, permissionManifestInfoList,
            currentPermissionManifestInfo
        )
        // 申请后台的传感器权限必须要先注册前台的传感器权限
        checkPermissionRegistrationStatus(permissionManifestInfoList, PermissionNames.BODY_SENSORS)
    }

    protected override fun checkSelfByRequestPermissions(
        @NonNull activity: Activity?,
        @NonNull requestPermissions: List<IPermission?>
    ) {
        super.checkSelfByRequestPermissions(activity, requestPermissions)
        // 必须要申请前台传感器权限才能申请后台传感器权限
        require(
            PermissionUtils.containsPermission(
                requestPermissions,
                PermissionNames.BODY_SENSORS
            )
        ) { "Applying for background sensor permissions must contain \"" + PermissionNames.BODY_SENSORS + "\"" }

        var thisPermissionIndex = -1
        var bodySensorsPermissionindex = -1
        for (i in requestPermissions.indices) {
            val permission = requestPermissions[i]
            if (PermissionUtils.equalsPermission(permission, this)) {
                thisPermissionIndex = i
            } else if (PermissionUtils.equalsPermission(permission, PermissionNames.BODY_SENSORS)) {
                bodySensorsPermissionindex = i
            }
        }

        require(!(bodySensorsPermissionindex != -1 && bodySensorsPermissionindex > thisPermissionIndex)) {
            "Please place the " + getPermissionName() +
                    "\" permission after the \"" + PermissionNames.BODY_SENSORS + "\" permission"
        }
    }


        /** 当前权限名称，注意：该常量字段仅供框架内部使用，不提供给外部引用，如果需要获取权限名称的字符串，请直接通过 [PermissionNames] 类获取  */


        companion object  CREATOR: Parcelable.Creator<BodySensorsBackgroundPermission> {

            val PERMISSION_NAME: String = PermissionNames.BODY_SENSORS_BACKGROUND
            override fun createFromParcel(source: Parcel): BodySensorsBackgroundPermission? {
                return BodySensorsBackgroundPermission(source)
            }

            override fun newArray(size: Int): Array<BodySensorsBackgroundPermission?> {
                return arrayOfNulls(size)
            }
        }


}