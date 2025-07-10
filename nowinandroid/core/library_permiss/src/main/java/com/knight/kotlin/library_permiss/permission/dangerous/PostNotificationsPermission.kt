package com.knight.kotlin.library_permiss.permission.dangerous

import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.permission.common.DangerousPermission


/**
 * @Description
 * @Author knight
 * @Time 2025/7/10 21:38
 *
 */

class PostNotificationsPermission : DangerousPermission {
    constructor()

    private constructor(`in`: Parcel) : super(`in`)

    @NonNull
    override fun getPermissionName(): String {
        return PERMISSION_NAME
    }

    override fun getFromAndroidVersion(): Int {
        return PermissionVersion.ANDROID_13
    }

    @NonNull
    override fun getOldPermissions(context: Context?): List<IPermission> {
        // Android 13 以下开启通知栏服务，需要用到旧的通知栏权限（框架自己虚拟出来的）
        return PermissionUtils.asArrayList(PermissionLists.getNotificationServicePermission())
    }

    override fun isGrantedPermissionByLowVersion(
        @NonNull context: Context?,
        skipRequest: Boolean
    ): Boolean {
        return PermissionLists.getNotificationServicePermission()
            .isGrantedPermission(context, skipRequest)
    }

    override fun isDoNotAskAgainPermissionByLowVersion(@NonNull activity: Activity?): Boolean {
        return PermissionLists.getNotificationServicePermission()
            .isDoNotAskAgainPermission(activity)
    }

    @NonNull
    override fun getPermissionSettingIntents(@NonNull context: Context?): List<Intent> {
        // Github issue 地址：https://github.com/getActivity/XXPermissions/issues/208
        // POST_NOTIFICATIONS 要跳转到权限设置页和 NOTIFICATION_SERVICE 权限是一样的
        return PermissionLists.getNotificationServicePermission()
            .getPermissionSettingIntents(context)
    }


    companion object CREATOR : Parcelable.Creator<PostNotificationsPermission> {

        /** 当前权限名称，注意：该常量字段仅供框架内部使用，不提供给外部引用，如果需要获取权限名称的字符串，请直接通过 [PermissionNames] 类获取  */
        val PERMISSION_NAME: String = PermissionNames.POST_NOTIFICATIONS

        override fun createFromParcel(source: Parcel): PostNotificationsPermission? {
            return PostNotificationsPermission(source)
        }

        override fun newArray(size: Int): Array<PostNotificationsPermission?> {
            return arrayOfNulls(size)
        }

    }
}