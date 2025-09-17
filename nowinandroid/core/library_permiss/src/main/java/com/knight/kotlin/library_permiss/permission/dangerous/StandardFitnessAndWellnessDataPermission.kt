package com.knight.kotlin.library_permiss.permission.dangerous

import android.content.Context
import android.os.Parcel
import android.os.Parcelable


/**
 * @author created by luguian
 * @organize
 * @Date 2025/9/17 15:36
 * @descript:健身和健康数据权限标准实现类
 */
class StandardFitnessAndWellnessDataPermission(permissionName: String, fromAndroidVersion: Int) : HealthDataBasePermission() {
    /** 权限名称  */
    
    private val mPermissionName = permissionName

    /** 权限出现的 Android 版本  */
    private val mFromAndroidVersion = fromAndroidVersion

    // 辅助构造函数，用于 Parcel
    private constructor(parcel: Parcel) : this(
        requireNotNull(parcel.readString()),
        parcel.readInt()
    )

    // 如果你想手动控制写入 Parcel，可以覆盖 writeToParcel（Parcelize 已经处理好）
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
        val CREATOR: Parcelable.Creator<StandardFitnessAndWellnessDataPermission> =
            object : Parcelable.Creator<StandardFitnessAndWellnessDataPermission> {
                override fun createFromParcel(source: Parcel): StandardFitnessAndWellnessDataPermission =
                    StandardFitnessAndWellnessDataPermission(source)

                override fun newArray(size: Int): Array<StandardFitnessAndWellnessDataPermission?> =
                    arrayOfNulls(size)
            }
    }
}