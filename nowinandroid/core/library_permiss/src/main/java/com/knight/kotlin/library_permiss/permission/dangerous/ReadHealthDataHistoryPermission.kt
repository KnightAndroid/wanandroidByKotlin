package com.knight.kotlin.library_permiss.permission.dangerous

import android.app.Activity
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.tools.PermissionApi
import com.knight.kotlin.library_permiss.tools.PermissionVersion


/**
 * @author created by luguian
 * @organize
 * @Date 2025/9/17 15:19
 * @descript:读取以往的健康数据权限类
 */
class ReadHealthDataHistoryPermission : HealthDataBasePermission {
    constructor()

    private constructor(`in`: Parcel) : super(`in`)

    
    override fun getPermissionName(): String {
        return PERMISSION_NAME
    }

    override fun getFromAndroidVersion( context: Context): Int {
        return PermissionVersion.ANDROID_15
    }

    override fun checkSelfByRequestPermissions(activity: Activity, requestList: List<IPermission>) {
        super.checkSelfByRequestPermissions(activity, requestList)

        var thisPermissionIndex = -1
        var otherHealthDataPermissionIndex = -1
        for (i in requestList.indices) {
            val permission = requestList[i]
            if (PermissionUtils.equalsPermission(permission, this)) {
                thisPermissionIndex = i
            } else if (PermissionApi.isHealthPermission(permission) &&
                !PermissionUtils.equalsPermission(permission, PermissionNames.READ_HEALTH_DATA_IN_BACKGROUND)
            ) {
                otherHealthDataPermissionIndex = i
            }
        }

        require(!(otherHealthDataPermissionIndex != -1 && otherHealthDataPermissionIndex > thisPermissionIndex)) {
            "Please place the \"" + getPermissionName() +
                    "\" permission after the \"" + requestList[otherHealthDataPermissionIndex] + "\" permission"
        }
    }

    companion object {
        /** 当前权限名称，注意：该常量字段仅供框架内部使用，不提供给外部引用，如果需要获取权限名称的字符串，请直接通过 [PermissionNames] 类获取  */
        val PERMISSION_NAME: String = PermissionNames.READ_HEALTH_DATA_HISTORY
        @JvmField
        val CREATOR : Parcelable.Creator<ReadHealthDataHistoryPermission> =


            object : Parcelable.Creator<ReadHealthDataHistoryPermission> {
                /** 当前权限名称，注意：该常量字段仅供框架内部使用，不提供给外部引用，
                 * 如果需要获取权限名称的字符串，请直接通过 [PermissionNames] 类获取
                 */

                override fun createFromParcel(source: Parcel): ReadHealthDataHistoryPermission {
                    return ReadHealthDataHistoryPermission(source)
                }

                override fun newArray(size: Int): Array<ReadHealthDataHistoryPermission?> {
                    return arrayOfNulls(size)
                }
            }
    }
}