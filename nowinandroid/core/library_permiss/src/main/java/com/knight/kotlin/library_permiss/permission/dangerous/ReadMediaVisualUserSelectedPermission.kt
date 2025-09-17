package com.knight.kotlin.library_permiss.permission.dangerous

import android.app.Activity
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.knight.kotlin.library_permiss.permission.PermissionGroups
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.permission.common.DangerousPermission
import com.knight.kotlin.library_permiss.tools.PermissionUtils
import com.knight.kotlin.library_permiss.tools.PermissionVersion


/**
 * @Description 访问部分照片和视频的权限类
 * @Author knight
 * @Time 2025/7/10 21:43
 *
 */

class ReadMediaVisualUserSelectedPermission : DangerousPermission {
    constructor()

    private constructor(`in`: Parcel) : super(`in`)

    
    override fun getPermissionName(): String {
        return PERMISSION_NAME
    }

    override fun getPermissionGroup( context: Context): String {
        return PermissionGroups.IMAGE_AND_VIDEO_MEDIA
    }

    override fun getFromAndroidVersion( context: Context): Int {
        return PermissionVersion.ANDROID_14
    }

    override fun getMinTargetSdkVersion( context: Context): Int {
        // 授予对照片和视频的部分访问权限：https://developer.android.google.cn/about/versions/14/changes/partial-photo-video-access?hl=zh-cn
        // READ_MEDIA_VISUAL_USER_SELECTED 这个权限比较特殊，不需要调高 targetSdk 的版本才能申请，但是需要和 READ_MEDIA_IMAGES 和 READ_MEDIA_VIDEO 组合使用
        // 这个权限不能单独申请，只能和 READ_MEDIA_IMAGES、READ_MEDIA_VIDEO 一起申请，否则会有问题，所以这个权限的 targetSdk 最低要求为 33 及以上
        return PermissionVersion.ANDROID_13
    }

    protected override fun checkSelfByRequestPermissions( activity: Activity,  requestList: List<IPermission>) {
        super.checkSelfByRequestPermissions(activity, requestList)

        if (PermissionUtils.containsPermission(requestList, PermissionNames.READ_MEDIA_IMAGES) ||
            PermissionUtils.containsPermission(requestList, PermissionNames.READ_MEDIA_VIDEO)
        ) {
            return
        }
        // 不能单独请求 READ_MEDIA_VISUAL_USER_SELECTED 权限，需要加上 READ_MEDIA_IMAGES 或者 READ_MEDIA_VIDEO 任一权限，又或者两个都有，否则权限申请会被系统直接拒绝
        throw IllegalArgumentException(
            "You cannot request the \"" + getPermissionName() + "\" permission alone. " +
                    "must add either \"" + PermissionNames.READ_MEDIA_IMAGES + "\" or \"" +
                    PermissionNames.READ_MEDIA_VIDEO + "\" permission, or maybe both"
        )
    }





    companion object {
        val PERMISSION_NAME: String = PermissionNames.READ_MEDIA_VISUAL_USER_SELECTED
        @JvmField
        val CREATOR : Parcelable.Creator<ReadMediaVisualUserSelectedPermission> =


            object : Parcelable.Creator<ReadMediaVisualUserSelectedPermission> {
                /** 当前权限名称，注意：该常量字段仅供框架内部使用，不提供给外部引用，
                 * 如果需要获取权限名称的字符串，请直接通过 [PermissionNames] 类获取
                 */

                override fun createFromParcel(source: Parcel): ReadMediaVisualUserSelectedPermission {
                    return ReadMediaVisualUserSelectedPermission(source)
                }

                override fun newArray(size: Int): Array<ReadMediaVisualUserSelectedPermission?> {
                    return arrayOfNulls(size)
                }
            }
    }
}