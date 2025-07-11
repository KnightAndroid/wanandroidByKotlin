package com.knight.kotlin.library_permiss.permission.special

import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import com.knight.kotlin.library_permiss.manifest.AndroidManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.PermissionManifestInfo
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.permission.common.SpecialPermission
import com.knight.kotlin.library_permiss.tools.PermissionVersion


/**
 * @author created by luguian
 * @organize
 * @Date 2025/7/11 8:55
 * @descript:画中画权限类
 */
class PictureInPicturePermission : SpecialPermission {

    companion object {
        /** 当前权限名称，仅供框架内部使用 */
        val PERMISSION_NAME: String = PermissionNames.PICTURE_IN_PICTURE

        @JvmField
        val CREATOR: Parcelable.Creator<PictureInPicturePermission> =
            object : Parcelable.Creator<PictureInPicturePermission> {
                override fun createFromParcel(source: Parcel): PictureInPicturePermission {
                    return PictureInPicturePermission(source)
                }

                override fun newArray(size: Int): Array<PictureInPicturePermission?> {
                    return arrayOfNulls(size)
                }
            }
    }

    constructor() : super()

    private constructor(source: Parcel) : super(source)

    override fun getPermissionName(): String = PERMISSION_NAME

    override fun getFromAndroidVersion(): Int = PermissionVersion.ANDROID_8

    override fun isGrantedPermission(context: Context, skipRequest: Boolean): Boolean {
        if (!PermissionVersion.isAndroid8()) {
            return true
        }
        return checkOpPermission(context, AppOpsManager.OPSTR_PICTURE_IN_PICTURE, true)
    }

    override fun getPermissionSettingIntents(context: Context): MutableList<Intent> {
        val intentList = ArrayList<Intent>(6)

        if (PermissionVersion.isAndroid8()) {
            val action = "android.settings.PICTURE_IN_PICTURE_SETTINGS"

            Intent(action).apply {
                data = getPackageNameUri(context)
                intentList.add(this)
            }

            // 如果加包名导致不能跳转，尝试不带包名跳转
            intentList.add(Intent(action))
        }

        intentList.add(getApplicationDetailsSettingIntent(context))
        intentList.add(getManageApplicationSettingIntent())
        intentList.add(getApplicationSettingIntent())
        intentList.add(getAndroidSettingIntent())

        return intentList
    }

    override fun checkSelfByManifestFile(
        activity: Activity,
        requestPermissions: List<IPermission>,
        androidManifestInfo: AndroidManifestInfo,
        permissionManifestInfoList: List<PermissionManifestInfo>,
        currentPermissionManifestInfo: PermissionManifestInfo
    ) {
        super.checkSelfByManifestFile(activity, requestPermissions, androidManifestInfo, permissionManifestInfoList, currentPermissionManifestInfo)

        val activityManifestInfoList = androidManifestInfo.activityManifestInfoList
        for (info in activityManifestInfoList) {
            if (info.supportsPictureInPicture) {
                // 找到支持画中画的 Activity，直接返回
                return
            }
        }

        throw IllegalArgumentException(
            "No Activity was found to have registered the android:supportsPictureInPicture=\"true\" property, " +
                    "Please register this property to ${activity.javaClass.name} class by AndroidManifest.xml file, " +
                    "otherwise it will lead to can't apply for the permission"
        )
    }
}
