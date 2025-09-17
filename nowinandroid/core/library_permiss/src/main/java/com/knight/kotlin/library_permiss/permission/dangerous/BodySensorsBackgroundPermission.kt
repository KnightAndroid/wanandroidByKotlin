package com.knight.kotlin.library_permiss.permission.dangerous

import android.app.Activity
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.knight.kotlin.library_permiss.manifest.AndroidManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.PermissionManifestInfo
import com.knight.kotlin.library_permiss.permission.PermissionGroups
import com.knight.kotlin.library_permiss.permission.PermissionLists.getBodySensorsPermission
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.permission.common.DangerousPermission
import com.knight.kotlin.library_permiss.tools.PermissionUtils
import com.knight.kotlin.library_permiss.tools.PermissionVersion
import com.knight.kotlin.library_permiss.tools.PermissionVersion.getTargetVersion


/**
 * @Description 后台传感器权限类
 * @Author knight
 * @Time 2025/7/10 21:33
 *
 */

class BodySensorsBackgroundPermission : DangerousPermission {
    constructor()

    private constructor(`in`: Parcel) : super(`in`)

 
    override fun getPermissionName(): String {
        return PERMISSION_NAME
    }

    
    override fun getPermissionPageType( context: Context): PermissionPageType {
        if (DeviceOs.isHyperOs() || DeviceOs.isMiui()) {
            return PermissionPageType.TRANSPARENT_ACTIVITY
        }
        return PermissionPageType.OPAQUE_ACTIVITY
    }

    override fun getPermissionGroup( context: Context): String {
        return PermissionGroups.SENSORS
    }

    override fun getFromAndroidVersion( context: Context): Int {
        return PermissionVersion.ANDROID_13
    }

    
    override fun getForegroundPermissions( context: Context): List<IPermission> {
        return PermissionUtils.asArrayList(getBodySensorsPermission())
    }

    override fun isBackgroundPermission( context: Context): Boolean {
        // 表示当前权限是后台权限
        return true
    }

    override fun isGrantedPermissionByStandardVersion( context: Context, skipRequest: Boolean): Boolean {
        // 判断后台传感器权限授予之前，需要先判断前台传感器权限是否授予，如果前台传感器权限没有授予，那么后台传感器权限就算授予了也没用
        if (!getBodySensorsPermission().isGrantedPermission(context, skipRequest)) {
            return false
        }
        return super.isGrantedPermissionByStandardVersion(context, skipRequest)
    }

    override fun isGrantedPermissionByLowVersion( context: Context, skipRequest: Boolean): Boolean {
        return getBodySensorsPermission().isGrantedPermission(context, skipRequest)
    }

    override fun isDoNotAskAgainPermissionByStandardVersion( activity: Activity): Boolean {
        // 如果前台传感器权限没有授予，那么后台传感器权限不再询问的状态要跟随前台传感器权限
        if (!getBodySensorsPermission().isGrantedPermission(activity)) {
            return getBodySensorsPermission().isDoNotAskAgainPermission(activity)
        }
        return super.isDoNotAskAgainPermissionByStandardVersion(activity)
    }

    override fun isDoNotAskAgainPermissionByLowVersion( activity: Activity): Boolean {
        return getBodySensorsPermission().isDoNotAskAgainPermission(activity)
    }

    override fun getRequestIntervalTime( context: Context): Int {
        // 经过测试，在 Android 13 设备上面，先申请前台权限，然后立马申请后台权限大概率会出现失败
        // 这里为了避免这种情况出现，所以加了一点延迟，这样就没有什么问题了
        // 为什么延迟时间是 150 毫秒？ 经过实践得出 100 还是有概率会出现失败，但是换成 150 试了很多次就都没有问题了
        return if (isSupportRequestPermission(context)) 150 else 0
    }

    protected override fun checkSelfByManifestFile(
         activity: Activity,
         requestList: List<IPermission>,
         manifestInfo: AndroidManifestInfo,
         permissionInfoList: List<PermissionManifestInfo>,
         currentPermissionInfo: PermissionManifestInfo
    ) {
        super.checkSelfByManifestFile(activity, requestList, manifestInfo, permissionInfoList, currentPermissionInfo)
        // 申请后台的传感器权限必须要先注册前台的传感器权限
        checkPermissionRegistrationStatus(permissionInfoList, PermissionNames.BODY_SENSORS)
    }

    protected override fun checkSelfByRequestPermissions( activity: Activity,  requestList: List<IPermission>) {
        super.checkSelfByRequestPermissions(activity, requestList)
        // 当项目 targetSdkVersion >= 36 时，不能申请 BODY_SENSORS_BACKGROUND 权限，应该请求在后台读取健康数据权限：READ_HEALTH_DATA_IN_BACKGROUND
        require(!(getTargetVersion(activity) >= PermissionVersion.ANDROID_16)) {
            "When the project targetSdkVersion is greater than or equal to " + PermissionVersion.ANDROID_16 +
                    ", the \"" + getPermissionName() + "\" permission cannot be requested, but the \"" +
                    PermissionNames.READ_HEALTH_DATA_IN_BACKGROUND + "\" permission should be requested instead"
        }

        // 必须要申请前台传感器权限才能申请后台传感器权限
        require(
            PermissionUtils.containsPermission(
                requestList,
                PermissionNames.BODY_SENSORS
            )
        ) { "Applying for background sensor permissions must contain \"" + PermissionNames.BODY_SENSORS + "\"" }

        var thisPermissionIndex = -1
        var bodySensorsPermissionindex = -1
        for (i in requestList.indices) {
            val permission = requestList[i]
            if (PermissionUtils.equalsPermission(permission!!, this)) {
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


    companion object {
        val PERMISSION_NAME: String = PermissionNames.BODY_SENSORS_BACKGROUND
        @JvmField
        val CREATOR : Parcelable.Creator<BodySensorsBackgroundPermission> =


            object : Parcelable.Creator<BodySensorsBackgroundPermission> {
                /** 当前权限名称，注意：该常量字段仅供框架内部使用，不提供给外部引用，
                 * 如果需要获取权限名称的字符串，请直接通过 [PermissionNames] 类获取
                 */

                override fun createFromParcel(source: Parcel): BodySensorsBackgroundPermission {
                    return BodySensorsBackgroundPermission(source)
                }

                override fun newArray(size: Int): Array<BodySensorsBackgroundPermission?> {
                    return arrayOfNulls(size)
                }
            }
    }

}