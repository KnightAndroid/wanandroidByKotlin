package com.knight.kotlin.library_permiss.permission.dangerous

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import com.knight.kotlin.library_permiss.permission.PermissionLists
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.permission.common.DangerousPermission
import com.knight.kotlin.library_permiss.tools.PermissionUtils
import com.knight.kotlin.library_permiss.tools.PermissionVersion


/**
 * @Description 发送通知权限类
 * @Author knight
 * @Time 2025/7/10 21:38
 *
 */

class PostNotificationsPermission : DangerousPermission {
    constructor()

    private constructor(`in`: Parcel) : super(`in`)

    
    override fun getPermissionName(): String {
        return PERMISSION_NAME
    }

    override fun getFromAndroidVersion(): Int {
        return PermissionVersion.ANDROID_13
    }

    
    override fun getOldPermissions(context: Context): List<IPermission> {
        // Android 13 以下开启通知栏服务，需要用到旧的通知栏权限（框架自己虚拟出来的）
        return PermissionUtils.asArrayList(PermissionLists.getNotificationServicePermission())
    }

    override fun isGrantedPermissionByLowVersion(
         context: Context,
        skipRequest: Boolean
    ): Boolean {
        return PermissionLists.getNotificationServicePermission()
            .isGrantedPermission(context, skipRequest)
    }

    override fun isDoNotAskAgainPermissionByLowVersion( activity: Activity): Boolean {
        return PermissionLists.getNotificationServicePermission()
            .isDoNotAskAgainPermission(activity)
    }

    
    override fun getPermissionSettingIntents( context: Context): MutableList<Intent> {
        // Github issue 地址：https://github.com/getActivity/XXPermissions/issues/208
        // POST_NOTIFICATIONS 要跳转到权限设置页和 NOTIFICATION_SERVICE 权限是一样的
        return PermissionLists.getNotificationServicePermission()
            .getPermissionSettingIntents(context)
    }


    companion object {
        val PERMISSION_NAME: String = PermissionNames.POST_NOTIFICATIONS
        @JvmField
        val CREATOR : Parcelable.Creator<PostNotificationsPermission> =


            object : Parcelable.Creator<PostNotificationsPermission> {
                /** 当前权限名称，注意：该常量字段仅供框架内部使用，不提供给外部引用，
                 * 如果需要获取权限名称的字符串，请直接通过 [PermissionNames] 类获取
                 */

                override fun createFromParcel(source: Parcel): PostNotificationsPermission {
                    return PostNotificationsPermission(source)
                }

                override fun newArray(size: Int): Array<PostNotificationsPermission?> {
                    return arrayOfNulls(size)
                }
            }
    }
}