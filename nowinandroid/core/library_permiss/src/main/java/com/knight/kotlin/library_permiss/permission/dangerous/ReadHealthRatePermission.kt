package com.knight.kotlin.library_permiss.permission.dangerous

import android.app.Activity
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.knight.kotlin.library_permiss.manifest.AndroidManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.PermissionManifestInfo
import com.knight.kotlin.library_permiss.permission.PermissionLists.getBodySensorsPermission
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.tools.PermissionUtils
import com.knight.kotlin.library_permiss.tools.PermissionVersion


/**
 * @author created by luguian
 * @organize
 * @Date 2025/9/17 15:23
 * @descript:读取心率数据权限类
 */
class ReadHealthRatePermission : HealthDataBasePermission {
    constructor()

    private constructor(`in`: Parcel) : super(`in`)

    
    override fun getPermissionName(): String {
        return PERMISSION_NAME
    }

    override fun getFromAndroidVersion( context: Context): Int {
        return PermissionVersion.ANDROID_14
    }

    override fun getOldPermissions(context: Context): List<IPermission> {
        return PermissionUtils.asArrayList(getBodySensorsPermission())
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
            checkPermissionRegistrationStatus(permissionInfoList, PermissionNames.BODY_SENSORS, PermissionVersion.ANDROID_13)
        }
    }

    companion object {
        /** 当前权限名称，注意：该常量字段仅供框架内部使用，不提供给外部引用，如果需要获取权限名称的字符串，请直接通过 [PermissionNames] 类获取  */
        val PERMISSION_NAME: String = PermissionNames.READ_HEART_RATE

        @JvmField
        val CREATOR : Parcelable.Creator<ReadHealthRatePermission> =


            object : Parcelable.Creator<ReadHealthRatePermission> {
                /** 当前权限名称，注意：该常量字段仅供框架内部使用，不提供给外部引用，
                 * 如果需要获取权限名称的字符串，请直接通过 [PermissionNames] 类获取
                 */

                override fun createFromParcel(source: Parcel): ReadHealthRatePermission {
                    return ReadHealthRatePermission(source)
                }

                override fun newArray(size: Int): Array<ReadHealthRatePermission?> {
                    return arrayOfNulls(size)
                }
            }
    }
}