package com.knight.kotlin.library_permiss.permission.dangerous

import android.content.Context
import android.os.Parcel
import android.os.Parcelable


/**
 * @author created by luguian
 * @organize
 * @Date 2025/9/17 15:39
 * @descript:健康记录权限标准实现类
 */
class StandardHealthRecordsPermission( permissionName: String, fromAndroidVersion: Int) : HealthDataBasePermission() {
    /** 权限名称  */
    
    private val mPermissionName = permissionName

    /** 权限出现的 Android 版本  */
    private val mFromAndroidVersion = fromAndroidVersion

    // 辅助构造函数，用于 Parcel
    private constructor(parcel: Parcel) : this(
        requireNotNull(parcel.readString()),
        parcel.readInt()
    )

    // 可选：手动覆盖 writeToParcel（Parcelize 已自动处理）
    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeString(mPermissionName)
        dest.writeInt(mFromAndroidVersion)
    }

    
    override fun getPermissionName(): String {
        return mPermissionName
    }

    override fun getFromAndroidVersion( context: Context): Int {
        return mFromAndroidVersion
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<StandardHealthRecordsPermission> =
            object : Parcelable.Creator<StandardHealthRecordsPermission> {
                override fun createFromParcel(source: Parcel): StandardHealthRecordsPermission =
                    StandardHealthRecordsPermission(source)

                override fun newArray(size: Int): Array<StandardHealthRecordsPermission?> =
                    arrayOfNulls(size)
            }
    }
}