package com.knight.kotlin.library_permiss.permission.dangerous

import android.app.Activity
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.knight.kotlin.library_permiss.manifest.AndroidManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.PermissionManifestInfo
import com.knight.kotlin.library_permiss.permission.PermissionLists.getBodySensorsBackgroundPermission
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.tools.PermissionApi
import com.knight.kotlin.library_permiss.tools.PermissionVersion
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid14


/**
 * @author created by luguian
 * @organize
 * @Date 2025/9/17 15:21
 * @descript:后台读取健康数据权限类
 */
class ReadHealthDataInBackgroundPermission : HealthDataBasePermission {
    constructor()

    private constructor(`in`: Parcel) : super(`in`)

    
    override fun getPermissionName(): String {
        return PERMISSION_NAME
    }

    override fun getFromAndroidVersion( context: Context): Int {
        return PermissionVersion.ANDROID_15
    }

    
    override fun getOldPermissions(context: Context): List<IPermission>? {
        if (!isAndroid14()) {
            // 这里解释一下为什么只在 Android 14 以下的版本才返回后台传感器权限，因为在 Android 14 之前，
            // Android 传感器权限本质上是为了读取心率传感器而准备的，直到 Android 14 发布将细分到健康数据权限中的读取心率权限，
            // 然而 Android 14 并没有出现与之对应的后台权限，直到 Android 15 才出现这个后台权限，这里就出现了一个兼容的问题，
            // 在这里框架认为在 Android 14 上面用 HealthConnectManager 在后台读取心率是不需要权限的，从 Android 15 开始才需要。
            return PermissionUtils.asArrayList(getBodySensorsBackgroundPermission())
        }
        return null
    }

    protected override fun checkSelfByManifestFile(
        activity: Activity,
        requestList: List<IPermission>,
        manifestInfo: AndroidManifestInfo,
        permissionInfoList: List<PermissionManifestInfo>,
        currentPermissionInfo: PermissionManifestInfo
    ) {
        super.checkSelfByManifestFile(activity, requestList, manifestInfo, permissionInfoList, currentPermissionInfo)
        // 如果权限出现的版本小于 minSdkVersion，则证明该权限可能会在旧系统上面申请，需要在 AndroidManifest.xml 文件注册一下旧版权限
        if (getFromAndroidVersion(activity) > getMinSdkVersion(activity, manifestInfo)) {
            checkPermissionRegistrationStatus(permissionInfoList, PermissionNames.BODY_SENSORS_BACKGROUND, PermissionVersion.ANDROID_14)
        }
    }

    override fun checkSelfByRequestPermissions( activity: Activity,  requestList: List<IPermission>) {
        super.checkSelfByRequestPermissions(activity, requestList)

        var thisPermissionIndex = -1
        var readHealthDataHistoryPermissionIndex = -1
        var otherHealthDataPermissionIndex = -1
        for (i in requestList.indices) {
            val permission = requestList[i]
            if (PermissionUtils.equalsPermission(permission, this)) {
                thisPermissionIndex = i
            } else if (PermissionUtils.equalsPermission(permission, PermissionNames.READ_HEALTH_DATA_HISTORY)) {
                readHealthDataHistoryPermissionIndex = i
            } else if (PermissionApi.isHealthPermission(permission)) {
                otherHealthDataPermissionIndex = i
            }
        }

        require(!(readHealthDataHistoryPermissionIndex != -1 && readHealthDataHistoryPermissionIndex > thisPermissionIndex)) {
            "Please place the " + getPermissionName() +
                    "\" permission after the \"" + PermissionNames.READ_HEALTH_DATA_HISTORY + "\" permission"
        }

        require(!(otherHealthDataPermissionIndex != -1 && otherHealthDataPermissionIndex > thisPermissionIndex)) {
            "Please place the \"" + getPermissionName() +
                    "\" permission after the \"" + requestList[otherHealthDataPermissionIndex] + "\" permission"
        }
    }

    companion object {
        /** 当前权限名称，注意：该常量字段仅供框架内部使用，不提供给外部引用，如果需要获取权限名称的字符串，请直接通过 [PermissionNames] 类获取  */
        val PERMISSION_NAME: String = PermissionNames.READ_HEALTH_DATA_IN_BACKGROUND

        @JvmField
        val CREATOR : Parcelable.Creator<ReadHealthDataInBackgroundPermission> =


            object : Parcelable.Creator<ReadHealthDataInBackgroundPermission> {
                /** 当前权限名称，注意：该常量字段仅供框架内部使用，不提供给外部引用，
                 * 如果需要获取权限名称的字符串，请直接通过 [PermissionNames] 类获取
                 */

                override fun createFromParcel(source: Parcel): ReadHealthDataInBackgroundPermission {
                    return ReadHealthDataInBackgroundPermission(source)
                }

                override fun newArray(size: Int): Array<ReadHealthDataInBackgroundPermission?> {
                    return arrayOfNulls(size)
                }
            }
    }
}