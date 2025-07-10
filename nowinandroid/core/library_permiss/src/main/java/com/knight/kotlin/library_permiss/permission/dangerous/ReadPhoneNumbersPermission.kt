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
 * @Time 2025/7/10 21:44
 *
 */

class ReadPhoneNumbersPermission : DangerousPermission {
    constructor()

    private constructor(`in`: Parcel) : super(`in`)

    @NonNull
    override fun getPermissionName(): String {
        return PERMISSION_NAME
    }

    override fun getPermissionGroup(): String {
        return PermissionGroups.PHONE
    }

    override fun getFromAndroidVersion(): Int {
        return PermissionVersion.ANDROID_8
    }

    @NonNull
    override fun getOldPermissions(context: Context?): List<IPermission> {
        // Android 8.0 以下读取电话号码需要用到读取电话状态的权限
        return PermissionUtils.asArrayList(PermissionLists.getReadPhoneStatePermission())
    }

    override fun isGrantedPermissionByLowVersion(
        @NonNull context: Context?,
        skipRequest: Boolean
    ): Boolean {
        return PermissionLists.getReadPhoneStatePermission()
            .isGrantedPermission(context, skipRequest)
    }

    override fun isDoNotAskAgainPermissionByLowVersion(@NonNull activity: Activity?): Boolean {
        return PermissionLists.getReadPhoneStatePermission().isDoNotAskAgainPermission(activity)
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
        // 如果权限出现的版本小于 minSdkVersion，则证明该权限可能会在旧系统上面申请，需要在 AndroidManifest.xml 文件注册一下旧版权限
        if (getFromAndroidVersion() > getMinSdkVersion(activity, androidManifestInfo)) {
            checkPermissionRegistrationStatus(
                permissionManifestInfoList,
                PermissionNames.READ_PHONE_STATE,
                PermissionVersion.ANDROID_7_1
            )
        }
    }



        companion object CREATOR: Parcelable.Creator<ReadPhoneNumbersPermission> {
        /** 当前权限名称，注意：该常量字段仅供框架内部使用，不提供给外部引用，如果需要获取权限名称的字符串，请直接通过 [PermissionNames] 类获取  */
        val PERMISSION_NAME: String = PermissionNames.READ_PHONE_NUMBERS

                override fun createFromParcel(source: Parcel): ReadPhoneNumbersPermission? {
                    return ReadPhoneNumbersPermission(source)
                }

                override fun newArray(size: Int): Array<ReadPhoneNumbersPermission?> {
                    return arrayOfNulls(size)
                }

    }
}