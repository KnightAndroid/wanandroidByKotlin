package com.knight.kotlin.library_permiss.permission.dangerous

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.permission.common.DangerousPermission


/**
 * @Description
 * @Author knight
 * @Time 2025/7/10 21:04
 *
 */

class AccessMediaLocationPermission : DangerousPermission {
    constructor()

    private constructor(`in`: Parcel) : super(`in`)

    
    override fun getPermissionName(): String {
        return PERMISSION_NAME
    }

    override fun getFromAndroidVersion(): Int {
        return PermissionVersion.ANDROID_10
    }

    override fun isGrantedPermissionByStandardVersion(
         context: Context,
        skipRequest: Boolean
    ): Boolean {
        return isGrantedReadMediaPermission(context, skipRequest) &&
                super.isGrantedPermissionByStandardVersion(context, skipRequest)
    }

    override fun isGrantedPermissionByLowVersion(
         context: Context?,
        skipRequest: Boolean
    ): Boolean {
        return PermissionLists.getReadExternalStoragePermission()
            .isGrantedPermission(context, skipRequest)
    }

    override fun isDoNotAskAgainPermissionByStandardVersion( activity: Activity): Boolean {
        return isGrantedReadMediaPermission(activity, true) &&
                super.isDoNotAskAgainPermissionByStandardVersion(activity)
    }

    override fun isDoNotAskAgainPermissionByLowVersion( activity: Activity?): Boolean {
        return PermissionLists.getReadExternalStoragePermission()
            .isDoNotAskAgainPermission(activity)
    }

    /**
     * 判断是否授予了读取媒体的权限
     */
    private fun isGrantedReadMediaPermission(
         context: Context,
        skipRequest: Boolean
    ): Boolean {
        if (PermissionVersion.isAndroid13() && PermissionVersion.getTargetVersion(context) >= PermissionVersion.ANDROID_13) {
            // 这里为什么不加上 Android 14 和 READ_MEDIA_VISUAL_USER_SELECTED 权限判断？这是因为如果获取部分照片和视频
            // 然后申请 Permission.ACCESS_MEDIA_LOCATION 系统会返回失败，必须要选择获取全部照片和视频才可以申请该权限
            return PermissionLists.getReadMediaImagesPermission()
                .isGrantedPermission(context, skipRequest) ||
                    PermissionLists.getReadMediaVideoPermission()
                        .isGrantedPermission(context, skipRequest) ||
                    PermissionLists.getManageExternalStoragePermission()
                        .isGrantedPermission(context, skipRequest)
        }
        if (PermissionVersion.isAndroid11() && PermissionVersion.getTargetVersion(context) >= PermissionVersion.ANDROID_11) {
            return PermissionLists.getReadExternalStoragePermission()
                .isGrantedPermission(context, skipRequest) ||
                    PermissionLists.getManageExternalStoragePermission()
                        .isGrantedPermission(context, skipRequest)
        }
        return PermissionLists.getReadExternalStoragePermission()
            .isGrantedPermission(context, skipRequest)
    }

    protected override fun checkSelfByRequestPermissions(
         activity: Activity?,
         requestPermissions: List<IPermission?>
    ) {
        super.checkSelfByRequestPermissions(activity, requestPermissions)

        var thisPermissionIndex = -1
        var readMediaImagesPermissionIndex = -1
        var readMediaVideoPermissionIndex = -1
        var readMediaVisualUserSelectedPermissionIndex = -1
        var manageExternalStoragePermissionIndex = -1
        var readExternalStoragePermissionIndex = -1
        var writeExternalStoragePermissionIndex = -1
        for (i in requestPermissions.indices) {
            val permission = requestPermissions[i]
            if (PermissionUtils.equalsPermission(permission, this)) {
                thisPermissionIndex = i
            } else if (PermissionUtils.equalsPermission(
                    permission,
                    PermissionNames.READ_MEDIA_IMAGES
                )
            ) {
                readMediaImagesPermissionIndex = i
            } else if (PermissionUtils.equalsPermission(
                    permission,
                    PermissionNames.READ_MEDIA_VIDEO
                )
            ) {
                readMediaVideoPermissionIndex = i
            } else if (PermissionUtils.equalsPermission(
                    permission,
                    PermissionNames.READ_MEDIA_VISUAL_USER_SELECTED
                )
            ) {
                readMediaVisualUserSelectedPermissionIndex = i
            } else if (PermissionUtils.equalsPermission(
                    permission,
                    PermissionNames.MANAGE_EXTERNAL_STORAGE
                )
            ) {
                manageExternalStoragePermissionIndex = i
            } else if (PermissionUtils.equalsPermission(
                    permission,
                    PermissionNames.READ_EXTERNAL_STORAGE
                )
            ) {
                readExternalStoragePermissionIndex = i
            } else if (PermissionUtils.equalsPermission(
                    permission,
                    PermissionNames.WRITE_EXTERNAL_STORAGE
                )
            ) {
                writeExternalStoragePermissionIndex = i
            }
        }

        require(!(readMediaImagesPermissionIndex != -1 && readMediaImagesPermissionIndex > thisPermissionIndex)) {
            "Please place the " + getPermissionName() +
                    "\" permission after the \"" + PermissionNames.READ_MEDIA_IMAGES + "\" permission"
        }

        require(!(readMediaVideoPermissionIndex != -1 && readMediaVideoPermissionIndex > thisPermissionIndex)) {
            "Please place the \"" + getPermissionName() +
                    "\" permission after the \"" + PermissionNames.READ_MEDIA_VIDEO + "\" permission"
        }

        require(!(readMediaVisualUserSelectedPermissionIndex != -1 && readMediaVisualUserSelectedPermissionIndex > thisPermissionIndex)) {
            "Please place the \"" + getPermissionName() +
                    "\" permission after the \"" + PermissionNames.READ_MEDIA_VISUAL_USER_SELECTED + "\" permission"
        }

        require(!(manageExternalStoragePermissionIndex != -1 && manageExternalStoragePermissionIndex > thisPermissionIndex)) {
            "Please place the \"" + getPermissionName() +
                    "\" permission after the \"" + PermissionNames.MANAGE_EXTERNAL_STORAGE + "\" permission"
        }

        require(!(readExternalStoragePermissionIndex != -1 && readExternalStoragePermissionIndex > thisPermissionIndex)) {
            "Please place the \"" + getPermissionName() +
                    "\" permission after the \"" + PermissionNames.READ_EXTERNAL_STORAGE + "\" permission"
        }

        require(!(writeExternalStoragePermissionIndex != -1 && writeExternalStoragePermissionIndex > thisPermissionIndex)) {
            "Please place the \"" + getPermissionName() +
                    "\" permission after the \"" + PermissionNames.WRITE_EXTERNAL_STORAGE + "\" permission"
        }

        // 判断当前项目是否适配了 Android 13
        if (PermissionVersion.getTargetVersion(activity) >= PermissionVersion.ANDROID_13) {
            // 判断请求的权限中是否包含了某些特定权限
            if (PermissionUtils.containsPermission(
                    requestPermissions,
                    PermissionNames.READ_MEDIA_IMAGES
                ) ||
                PermissionUtils.containsPermission(
                    requestPermissions,
                    PermissionNames.READ_MEDIA_VIDEO
                ) ||
                PermissionUtils.containsPermission(
                    requestPermissions,
                    PermissionNames.MANAGE_EXTERNAL_STORAGE
                )
            ) {
                // 如果请求的权限中，包含了上面这些权限，就不往下执行
                return
            }

            // 如果不包含，你需要在外层手动添加 READ_MEDIA_IMAGES、READ_MEDIA_VIDEO、MANAGE_EXTERNAL_STORAGE 任一权限才可以申请 ACCESS_MEDIA_LOCATION 权限
            throw IllegalArgumentException(
                "You must add \"" + PermissionNames.READ_MEDIA_IMAGES + "\" or \"" +
                        PermissionNames.READ_MEDIA_VIDEO + "\" or \"" + PermissionNames.MANAGE_EXTERNAL_STORAGE +
                        "\" rights to apply for \"" + getPermissionName() + "\" rights"
            )
        }

        // 如果当前项目还没有适配 Android 13，就判断请求的权限中是否包含了某些特定权限
        if (PermissionUtils.containsPermission(
                requestPermissions,
                PermissionNames.READ_EXTERNAL_STORAGE
            ) ||
            PermissionUtils.containsPermission(
                requestPermissions,
                PermissionNames.MANAGE_EXTERNAL_STORAGE
            )
        ) {
            // 如果请求的权限中，包含了上面这些权限，就不往下执行
            return
        }

        // 如果不包含，你需要在外层手动添加 READ_EXTERNAL_STORAGE 或者 MANAGE_EXTERNAL_STORAGE 才可以申请 ACCESS_MEDIA_LOCATION 权限
        throw IllegalArgumentException(
            "You must add \"" + PermissionNames.READ_EXTERNAL_STORAGE + "\" or \"" +
                    PermissionNames.MANAGE_EXTERNAL_STORAGE + "\" rights to apply for \"" + getPermissionName() + "\" rights"
        )
    }

        companion   object CREATOR : Parcelable.Creator<AccessMediaLocationPermission> {

            /** 当前权限名称，注意：该常量字段仅供框架内部使用，不提供给外部引用，如果需要获取权限名称的字符串，请直接通过 [PermissionNames] 类获取  */
            val PERMISSION_NAME: String = PermissionNames.ACCESS_MEDIA_LOCATION
            override fun createFromParcel(parcel: Parcel): AccessMediaLocationPermission {
                return AccessMediaLocationPermission(parcel)
            }

            override fun newArray(size: Int): Array<AccessMediaLocationPermission?> {
                return arrayOfNulls(size)
            }
        }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
    }

    override fun describeContents(): Int {
        return 0
    }


}