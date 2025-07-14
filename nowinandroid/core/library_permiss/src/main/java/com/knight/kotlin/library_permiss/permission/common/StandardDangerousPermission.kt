package com.knight.kotlin.library_permiss.permission.common

import android.os.Parcel
import android.os.Parcelable



/**
 * @Description
 * @Author knight
 * @Time 2025/7/10 20:56
 *
 */

class StandardDangerousPermission : DangerousPermission, Parcelable {

    /** 权限名称 */

    private val mPermissionName: String

    /** 权限组别 */
    private val mPermissionGroup: String?

    /** 权限出现的 Android 版本 */
    private val mFromAndroidVersion: Int

    constructor(permissionName: String, fromAndroidVersion: Int) : this(permissionName, null, fromAndroidVersion)

    constructor(permissionName: String, permissionGroup: String?, fromAndroidVersion: Int) {
        mPermissionName = permissionName
        mPermissionGroup = permissionGroup
        mFromAndroidVersion = fromAndroidVersion
    }

    private constructor(parcel: Parcel) : super(parcel) {
        mPermissionName = parcel.readString()!!
        mPermissionGroup = parcel.readString()
        mFromAndroidVersion = parcel.readInt()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeString(mPermissionName)
        dest.writeString(mPermissionGroup)
        dest.writeInt(mFromAndroidVersion)
    }

    override fun describeContents(): Int = 0

    override fun getPermissionName(): String = mPermissionName

    override fun getPermissionGroup(): String? = mPermissionGroup

    override fun getFromAndroidVersion(): Int = mFromAndroidVersion

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<StandardDangerousPermission> =
            object : Parcelable.Creator<StandardDangerousPermission> {
                override fun createFromParcel(source: Parcel): StandardDangerousPermission {
                    return StandardDangerousPermission(source)
                }

                override fun newArray(size: Int): Array<StandardDangerousPermission?> {
                    return arrayOfNulls(size)
                }
            }
    }
}