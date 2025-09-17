package com.knight.kotlin.library_permiss.permission.special

import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import com.knight.kotlin.library_permiss.manifest.AndroidManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.ActivityManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.PermissionManifestInfo
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.permission.common.SpecialPermission
import com.knight.kotlin.library_permiss.tools.PermissionVersion
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid8


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

    override fun getFromAndroidVersion( context: Context): Int {
        return PermissionVersion.ANDROID_8
    }

    override fun isGrantedPermission( context: Context, skipRequest: Boolean): Boolean {
        if (!isAndroid8()) {
            return true
        }
        return checkOpPermission(context!!, AppOpsManager.OPSTR_PICTURE_IN_PICTURE, true)
    }

    
    override fun getPermissionSettingIntents( context: Context, skipRequest: Boolean): MutableList<Intent> {
        val intentList: MutableList<Intent> = ArrayList(6)
        var intent: Intent

        if (isAndroid8()) {
            // android.provider.Settings.ACTION_PICTURE_IN_PICTURE_SETTINGS
            val action = "android.settings.PICTURE_IN_PICTURE_SETTINGS"

            intent = Intent(action)
            intent.setData(getPackageNameUri(context))
            intentList.add(intent)

            // 如果是因为加包名的数据后导致不能跳转，就把包名的数据移除掉
            intent = Intent(action)
            intentList.add(intent)
        }

        intent = getApplicationDetailsSettingIntent(context)
        intentList.add(intent)

        intent = getManageApplicationSettingIntent()
        intentList.add(intent)

        intent = getApplicationSettingIntent()
        intentList.add(intent)

        intent = getAndroidSettingIntent()
        intentList.add(intent)

        return intentList
    }

     override fun checkSelfByManifestFile(
         activity: Activity,
         requestList: List<IPermission>,
         manifestInfo: AndroidManifestInfo,
         permissionInfoList: List<PermissionManifestInfo>,
         currentPermissionInfo: PermissionManifestInfo
    ) {
        super.checkSelfByManifestFile(activity, requestList, manifestInfo, permissionInfoList, currentPermissionInfo)
        val activityInfoList: List<ActivityManifestInfo> = manifestInfo.activityInfoList
        for (i in activityInfoList.indices) {
            val supportsPictureInPicture = activityInfoList[i].supportsPictureInPicture
            if (supportsPictureInPicture) {
                // 终止循环并返回
                return
            }
        }

        /*
         没有找到有任何 Service 注册过 android:permission="android.permission.BIND_NOTIFICATION_LISTENER_SERVICE" 属性，
         请注册该属性给 NotificationListenerService 的子类到 AndroidManifest.xml 文件中，否则会导致无法申请该权限
         */
        throw IllegalArgumentException(
            ("No Activity was found to have registered the android:supportsPictureInPicture=\"true\" property, " +
                    "Please register this property to " + activity.javaClass.name + " class by AndroidManifest.xml file, "
                    + "otherwise it will lead to can't apply for the permission")
        )
    }
}
