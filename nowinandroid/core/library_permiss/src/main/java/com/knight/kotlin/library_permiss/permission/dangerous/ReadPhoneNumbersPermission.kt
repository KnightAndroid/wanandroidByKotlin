package com.knight.kotlin.library_permiss.permission.dangerous

import android.app.Activity
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.knight.kotlin.library_permiss.manifest.AndroidManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.PermissionManifestInfo
import com.knight.kotlin.library_permiss.permission.PermissionGroups
import com.knight.kotlin.library_permiss.permission.PermissionLists.getReadPhoneStatePermission
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.permission.common.DangerousPermission
import com.knight.kotlin.library_permiss.tools.PermissionUtils
import com.knight.kotlin.library_permiss.tools.PermissionVersion


/**
 * @Description 读取手机号码权限类
 * @Author knight
 * @Time 2025/7/10 21:44
 *
 */

class ReadPhoneNumbersPermission : DangerousPermission {
    constructor()

    private constructor(`in`: Parcel) : super(`in`)

    
    override fun getPermissionName(): String {
        return PERMISSION_NAME
    }

    override fun getPermissionGroup( context: Context): String {
        return PermissionGroups.PHONE
    }

    override fun getFromAndroidVersion( context: Context): Int {
        return PermissionVersion.ANDROID_8
    }

    
    override fun getOldPermissions(context: Context): List<IPermission> {
        // Android 8.0 以下读取电话号码需要用到读取电话状态的权限
        return PermissionUtils.asArrayList(getReadPhoneStatePermission())
    }

    override fun isGrantedPermissionByLowVersion( context: Context, skipRequest: Boolean): Boolean {
        return getReadPhoneStatePermission().isGrantedPermission(context, skipRequest)
    }

    override fun isDoNotAskAgainPermissionByLowVersion( activity: Activity): Boolean {
        return getReadPhoneStatePermission().isDoNotAskAgainPermission(activity)
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
            checkPermissionRegistrationStatus(permissionInfoList, PermissionNames.READ_PHONE_STATE, PermissionVersion.ANDROID_7_1)
        }
    }
    companion object {
        val PERMISSION_NAME: String = PermissionNames.READ_PHONE_NUMBERS
        @JvmField
        val CREATOR : Parcelable.Creator<ReadPhoneNumbersPermission> =


            object : Parcelable.Creator<ReadPhoneNumbersPermission> {
                /** 当前权限名称，注意：该常量字段仅供框架内部使用，不提供给外部引用，
                 * 如果需要获取权限名称的字符串，请直接通过 [PermissionNames] 类获取
                 */

                override fun createFromParcel(source: Parcel): ReadPhoneNumbersPermission {
                    return ReadPhoneNumbersPermission(source)
                }

                override fun newArray(size: Int): Array<ReadPhoneNumbersPermission?> {
                    return arrayOfNulls(size)
                }
            }
    }
}