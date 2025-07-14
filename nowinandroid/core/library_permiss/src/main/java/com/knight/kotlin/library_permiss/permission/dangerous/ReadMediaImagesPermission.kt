package com.knight.kotlin.library_permiss.permission.dangerous

import android.app.Activity
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.knight.kotlin.library_permiss.manifest.AndroidManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.PermissionManifestInfo
import com.knight.kotlin.library_permiss.permission.PermissionGroups
import com.knight.kotlin.library_permiss.permission.PermissionLists
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.permission.common.DangerousPermission
import com.knight.kotlin.library_permiss.tools.PermissionUtils
import com.knight.kotlin.library_permiss.tools.PermissionVersion


/**
 * @Description 读取图片媒体权限类
 * @Author knight
 * @Time 2025/7/10 21:41
 *
 */

class ReadMediaImagesPermission : DangerousPermission {
    constructor()

    private constructor(`in`: Parcel) : super(`in`)

    
    override fun getPermissionName(): String {
        return PERMISSION_NAME
    }

    override fun getPermissionGroup(): String {
        return PermissionGroups.IMAGE_AND_VIDEO_MEDIA
    }

    override fun getFromAndroidVersion(): Int {
        return PermissionVersion.ANDROID_13
    }

    
    override fun getOldPermissions(context: Context): List<IPermission> {
        // Android 13 以下访问媒体文件需要用到读取外部存储的权限
        return PermissionUtils.asArrayList(PermissionLists.getReadExternalStoragePermission())
    }

    override fun isGrantedPermissionByStandardVersion(
         context: Context,
        skipRequest: Boolean
    ): Boolean {
        if (PermissionVersion.isAndroid14() && !skipRequest) {
            // 如果是在 Android 14 上面，并且是图片权限或者视频权限，则需要重新检查权限的状态
            // 这是因为用户授权部分图片或者视频的时候，READ_MEDIA_VISUAL_USER_SELECTED 权限状态是授予的
            // 但是 READ_MEDIA_IMAGES 和 READ_MEDIA_VIDEO 的权限状态是拒绝的
            // 为了权限回调不出现失败，这里只能返回 true，这样告诉外层请求其实是成功的
            return PermissionLists.getReadMediaVisualUserSelectedPermission()
                .isGrantedPermission(context, false)
        }
        return super.isGrantedPermissionByStandardVersion(context, skipRequest)
    }

    override fun isGrantedPermissionByLowVersion(
         context: Context,
        skipRequest: Boolean
    ): Boolean {
        return PermissionLists.getReadExternalStoragePermission()
            .isGrantedPermission(context, skipRequest)
    }

    override fun isDoNotAskAgainPermissionByLowVersion( activity: Activity): Boolean {
        return PermissionLists.getReadExternalStoragePermission()
            .isDoNotAskAgainPermission(activity)
    }

     override fun checkSelfByManifestFile(
         activity: Activity,
         requestPermissions: List<IPermission>,
         androidManifestInfo: AndroidManifestInfo,
         permissionManifestInfoList: List<PermissionManifestInfo>,
        currentPermissionManifestInfo: PermissionManifestInfo
    ) {
        super.checkSelfByManifestFile(
            activity, requestPermissions, androidManifestInfo, permissionManifestInfoList,
            currentPermissionManifestInfo
        )
        // 如果权限出现的版本小于 minSdkVersion，则证明该权限可能会在旧系统上面申请，需要在 AndroidManifest.xml 文件注册一下旧版权限
        if (getFromAndroidVersion() > getMinSdkVersion(activity, androidManifestInfo)) {
            checkPermissionRegistrationStatus(
                permissionManifestInfoList,
                PermissionNames.READ_EXTERNAL_STORAGE,
                PermissionVersion.ANDROID_12_L
            )
        }
    }

    override fun checkSelfByRequestPermissions(
         activity: Activity,
         requestPermissions: List<IPermission>
    ) {
        super.checkSelfByRequestPermissions(activity, requestPermissions)
        // 检测是否有添加读取外部存储权限，有的话直接抛出异常，请不要自己手动添加这个权限，框架会在 Android 13 以下的版本上自动添加并申请这个权限
        require(
            !PermissionUtils.containsPermission(
                requestPermissions,
                PermissionNames.READ_EXTERNAL_STORAGE
            )
        ) {
            ("You have added the \"" + getPermissionName() + "\" permission, "
                    + "please do not add the \"" + PermissionNames.READ_EXTERNAL_STORAGE + "\" permission, "
                    + "this conflicts with the framework's automatic compatibility policy.")
        }
    }





    companion object {
        val PERMISSION_NAME: String = PermissionNames.READ_MEDIA_IMAGES
        @JvmField
        val CREATOR : Parcelable.Creator<ReadMediaImagesPermission> =


            object : Parcelable.Creator<ReadMediaImagesPermission> {
                /** 当前权限名称，注意：该常量字段仅供框架内部使用，不提供给外部引用，
                 * 如果需要获取权限名称的字符串，请直接通过 [PermissionNames] 类获取
                 */

                override fun createFromParcel(source: Parcel):ReadMediaImagesPermission {
                    return ReadMediaImagesPermission(source)
                }

                override fun newArray(size: Int): Array<ReadMediaImagesPermission?> {
                    return arrayOfNulls(size)
                }
            }
    }
}