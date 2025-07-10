package com.knight.kotlin.library_permiss.permission.common

import android.os.Parcel
import android.os.Parcelable



/**
 * @Description
 * @Author knight
 * @Time 2025/7/10 20:56
 *
 */

class StandardDangerousPermission(
     permissionName: String?,
     permissionGroup: String?,
    fromAndroidVersion: Int
) :
    DangerousPermission() {
    /** 权限名称  */
    
    private val mPermissionName = permissionName

    /** 权限组别  */
    
    private val mPermissionGroup = permissionGroup

    /** 权限出现的 Android 版本  */
    private val mFromAndroidVersion = fromAndroidVersion

    private constructor(`in`: Parcel) : this(`in`.readString(), `in`.readString(), `in`.readInt())

    constructor( permissionName: String?, fromAndroidVersion: Int) : this(
        permissionName,
        null,
        fromAndroidVersion
    )

    override fun writeToParcel( dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeString(mPermissionName)
        dest.writeString(mPermissionGroup)
        dest.writeInt(mFromAndroidVersion)
    }

    
    override fun getPermissionName(): String? {
        return mPermissionName
    }

    
    override fun getPermissionGroup(): String? {
        return mPermissionGroup
    }

    override fun getFromAndroidVersion(): Int {
        return mFromAndroidVersion
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StandardDangerousPermission> {
        override fun createFromParcel(parcel: Parcel): StandardDangerousPermission {
            return StandardDangerousPermission(parcel)
        }

        override fun newArray(size: Int): Array<StandardDangerousPermission?> {
            return arrayOfNulls(size)
        }
    }


}