package com.knight.kotlin.library_permiss.permission.special

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.os.Parcel
import android.os.Parcelable
import android.provider.Settings
import com.knight.kotlin.library_permiss.manifest.AndroidManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.PermissionManifestInfo
import com.knight.kotlin.library_permiss.permission.PermissionLists
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.permission.common.SpecialPermission
import com.knight.kotlin.library_permiss.tools.PermissionUtils
import com.knight.kotlin.library_permiss.tools.PermissionVersion


/**
 * @author created by luguian
 * @organize
 * @Date 2025/7/11 8:51
 * @descript:所有文件访问权限类
 */
class ManageExternalStoragePermission : SpecialPermission {

    companion object {
        /** 当前权限名称，仅供框架内部使用 */
        val PERMISSION_NAME: String = PermissionNames.MANAGE_EXTERNAL_STORAGE

        @JvmField
        val CREATOR: Parcelable.Creator<ManageExternalStoragePermission> =
            object : Parcelable.Creator<ManageExternalStoragePermission> {
                override fun createFromParcel(source: Parcel): ManageExternalStoragePermission {
                    return ManageExternalStoragePermission(source)
                }

                override fun newArray(size: Int): Array<ManageExternalStoragePermission?> {
                    return arrayOfNulls(size)
                }
            }
    }

    constructor() : super()

    private constructor(source: Parcel) : super(source)

    override fun getPermissionName(): String = PERMISSION_NAME

    override fun getFromAndroidVersion(): Int = PermissionVersion.ANDROID_11

    override fun getOldPermissions(context: Context): List<IPermission> {
        // Android 11 以下访问完整的文件管理需要读写权限
        return PermissionUtils.asArrayList(
            PermissionLists.getReadExternalStoragePermission(),
            PermissionLists.getWriteExternalStoragePermission()
        )
    }

    override fun isGrantedPermission(context: Context, skipRequest: Boolean): Boolean {
        return if (!PermissionVersion.isAndroid11()) {
            if (PermissionVersion.isAndroid10() && !Environment.isExternalStorageLegacy()) {
                false
            } else {
                PermissionLists.getReadExternalStoragePermission().isGrantedPermission(context, skipRequest) &&
                        PermissionLists.getWriteExternalStoragePermission().isGrantedPermission(context, skipRequest)
            }
        } else {
            Environment.isExternalStorageManager()
        }
    }

    override fun getPermissionSettingIntents(context: Context): MutableList<Intent> {
        val intentList = ArrayList<Intent>(3)

        if (PermissionVersion.isAndroid11()) {
            Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                data = getPackageNameUri(context)
                intentList.add(this)
            }
            intentList.add(Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION))
        }

        intentList.add(getAndroidSettingIntent())
        return intentList
    }

    override fun isRegisterPermissionByManifestFile(): Boolean = true

    override fun checkSelfByManifestFile(
        activity: Activity,
        requestPermissions: List<IPermission>,
        androidManifestInfo: AndroidManifestInfo,
        permissionManifestInfoList: List<PermissionManifestInfo>,
        currentPermissionManifestInfo: PermissionManifestInfo
    ) {
        super.checkSelfByManifestFile(activity, requestPermissions, androidManifestInfo, permissionManifestInfoList, currentPermissionManifestInfo)

        // 如果权限出现的版本高于 minSdkVersion，则检查旧权限注册
        if (getFromAndroidVersion() > getMinSdkVersion(activity, androidManifestInfo)) {
            checkPermissionRegistrationStatus(
                permissionManifestInfoList,
                PermissionNames.READ_EXTERNAL_STORAGE,
                PermissionVersion.ANDROID_10
            )
            checkPermissionRegistrationStatus(
                permissionManifestInfoList,
                PermissionNames.WRITE_EXTERNAL_STORAGE,
                PermissionVersion.ANDROID_10
            )
        }

        // 忽略 ACCESS_MEDIA_LOCATION 的检测
        if (PermissionUtils.containsPermission(requestPermissions, PermissionNames.ACCESS_MEDIA_LOCATION)) return

        val appInfo = androidManifestInfo.applicationManifestInfo ?: return

        // target >= Android 10 且未声明 requestLegacyExternalStorage="true"
        if (PermissionVersion.getTargetVersion(activity) >= PermissionVersion.ANDROID_10 &&
            !appInfo.requestLegacyExternalStorage
        ) {
            throw IllegalStateException(
                "Please register the android:requestLegacyExternalStorage=\"true\" attribute in the AndroidManifest.xml file, " +
                        "otherwise it will cause incompatibility with the old version"
            )
        }
    }

    override fun checkSelfByRequestPermissions(
        activity: Activity,
        requestPermissions: List<IPermission>
    ) {
        super.checkSelfByRequestPermissions(activity, requestPermissions)

        if (PermissionUtils.containsPermission(requestPermissions, PermissionNames.READ_EXTERNAL_STORAGE) ||
            PermissionUtils.containsPermission(requestPermissions, PermissionNames.WRITE_EXTERNAL_STORAGE)
        ) {
            throw IllegalArgumentException(
                "If you have applied for \"${getPermissionName()}\" permissions, do not apply for the \"" +
                        PermissionNames.READ_EXTERNAL_STORAGE + "\" or \"" +
                        PermissionNames.WRITE_EXTERNAL_STORAGE + "\" permissions"
            )
        }

        if (PermissionUtils.containsPermission(requestPermissions, PermissionNames.READ_MEDIA_IMAGES) ||
            PermissionUtils.containsPermission(requestPermissions, PermissionNames.READ_MEDIA_VIDEO) ||
            PermissionUtils.containsPermission(requestPermissions, PermissionNames.READ_MEDIA_AUDIO)
        ) {
            throw IllegalArgumentException(
                "Because the \"${getPermissionName()}\" permission range is very large, " +
                        "you can read media files with it, and there is no need to apply for additional media permissions."
            )
        }
    }
}
