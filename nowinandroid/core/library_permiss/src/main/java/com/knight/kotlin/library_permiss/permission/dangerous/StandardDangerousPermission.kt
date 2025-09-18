package com.knight.kotlin.library_permiss.permission.dangerous

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.knight.kotlin.library_permiss.permission.common.DangerousPermission




/**
 * @author created by luguian
 * @organize
 * @Date 2025/9/17 15:32
 * @descript:危险权限标准实现类
 */
class StandardDangerousPermission( permissionName: String,  permissionGroup: String?=null, fromAndroidVersion: Int) :
    DangerousPermission() {
    /** 权限名称  */
    
    private val mPermissionName = permissionName

    /** 权限组别  */
    
    private val mPermissionGroup = permissionGroup

    /** 权限出现的 Android 版本  */
    private val mFromAndroidVersion = fromAndroidVersion

    // 辅助构造函数，用于 Parcel
    private constructor(parcel: Parcel) : this(
        requireNotNull(parcel.readString()),
        parcel.readString(),
        parcel.readInt()
    )

    // 如果你还需要自定义写入 Parcel，可以覆盖 writeToParcel（但 Parcelize 已经自动处理）
    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeString(mPermissionName)
        dest.writeString(mPermissionGroup)
        dest.writeInt(mFromAndroidVersion)
    }
    
    override fun getPermissionName(): String {
        return mPermissionName
    }

    
    override fun getPermissionGroup( context: Context): String? {
        return mPermissionGroup
    }

    override fun getFromAndroidVersion( context: Context): Int {
        return mFromAndroidVersion
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<StandardDangerousPermission> =
            object : Parcelable.Creator<StandardDangerousPermission> {
                override fun createFromParcel(source: Parcel): StandardDangerousPermission =
                    StandardDangerousPermission(source)

                override fun newArray(size: Int): Array<StandardDangerousPermission?> =
                    arrayOfNulls(size)
            }
    }
}