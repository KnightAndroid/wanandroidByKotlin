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
 * @Description 读取外部存储权限类
 * @Author knight
 * @Time 2025/7/10 21:39
 *
 */

class ReadExternalStoragePermission : DangerousPermission {
    constructor()

    private constructor(`in`: Parcel) : super(`in`)

    
    override fun getPermissionName(): String {
        return PERMISSION_NAME
    }

    override fun getPermissionGroup(): String {
        return PermissionGroups.STORAGE
    }

    override fun getFromAndroidVersion(): Int {
        return PermissionVersion.ANDROID_6
    }

    override fun isGrantedPermissionByStandardVersion(
         context: Context,
        skipRequest: Boolean
    ): Boolean {
        if (PermissionVersion.isAndroid13() && PermissionVersion.getCurrentVersion() >= PermissionVersion.ANDROID_13) {
            return PermissionLists.getReadMediaImagesPermission()
                .isGrantedPermission(context, skipRequest) &&
                    PermissionLists.getReadMediaVideoPermission()
                        .isGrantedPermission(context, skipRequest) &&
                    PermissionLists.getReadMediaAudioPermission()
                        .isGrantedPermission(context, skipRequest)
        }
        return super.isGrantedPermissionByStandardVersion(context, skipRequest)
    }

    override fun isDoNotAskAgainPermissionByStandardVersion( activity: Activity): Boolean {
        if (PermissionVersion.isAndroid13() && PermissionVersion.getCurrentVersion() >= PermissionVersion.ANDROID_13) {
            return PermissionLists.getReadMediaImagesPermission()
                .isDoNotAskAgainPermission(activity) &&
                    PermissionLists.getReadMediaVideoPermission()
                        .isDoNotAskAgainPermission(activity) &&
                    PermissionLists.getReadMediaAudioPermission()
                        .isDoNotAskAgainPermission(activity)
        }
        return super.isDoNotAskAgainPermissionByStandardVersion(activity)
    }

    protected override fun checkSelfByManifestFile(
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
        // 如果申请的是 Android 10 获取媒体位置权限，则绕过本次检查
        if (PermissionUtils.containsPermission(
                requestPermissions,
                PermissionNames.ACCESS_MEDIA_LOCATION
            )
        ) {
            return
        }

        val applicationManifestInfo = androidManifestInfo.applicationManifestInfo ?: return

        val targetSdkVersion: Int = PermissionVersion.getTargetVersion(activity)
        // 是否适配了分区存储
        val scopedStorage: Boolean =
            PermissionUtils.getBooleanByMetaData(activity, META_DATA_KEY_SCOPED_STORAGE, false)
        // 如果在已经适配 Android 10 的情况下
        check(!(targetSdkVersion >= PermissionVersion.ANDROID_10 && !applicationManifestInfo.requestLegacyExternalStorage && !scopedStorage)) {
            "Please register the android:requestLegacyExternalStorage=\"true\" " +
                    "attribute in the AndroidManifest.xml file, otherwise it will cause incompatibility with the old version"
        }

        // 如果在已经适配 Android 11 的情况下
        require(!(targetSdkVersion >= PermissionVersion.ANDROID_11 && !scopedStorage)) {
            "The storage permission application is abnormal. If you have adapted the scope storage, " +
                    "please register the <meta-data android:name=\"ScopedStorage\" android:value=\"true\" /> attribute in the AndroidManifest.xml file. " +
                    "If there is no adaptation scope storage, please use \"" + PermissionNames.MANAGE_EXTERNAL_STORAGE + "\" to apply for permission"
        }
    }

    override fun checkSelfByRequestPermissions(
         activity: Activity,
         requestPermissions: List<IPermission>
    ) {
        super.checkSelfByRequestPermissions(activity, requestPermissions)

        require(!(PermissionVersion.getTargetVersion(activity) >= PermissionVersion.ANDROID_13)) {
            ("When the project targetSdkVersion >= 33, the \"" + PermissionNames.READ_EXTERNAL_STORAGE +
                    "\" permission cannot be applied for, and some problems will occur." + "Because after testing, if targetSdkVersion >= 33 applies for \"" +
                    PermissionNames.READ_EXTERNAL_STORAGE + "\" or \"" + PermissionNames.WRITE_EXTERNAL_STORAGE +
                    "\", it will be directly rejected by the system and no authorization dialog box will be displayed."
                    + "If the App has been adapted for scoped storage, the should be requested \"" + PermissionNames.READ_MEDIA_IMAGES + "\" or \"" +
                    PermissionNames.READ_MEDIA_VIDEO + "\" or \"" + PermissionNames.READ_MEDIA_AUDIO + "\" permission."
                    + "If the App does not need to adapt scoped storage, the should be requested \"" + PermissionNames.MANAGE_EXTERNAL_STORAGE + "\" permission")
        }
    }



    companion object {
        val PERMISSION_NAME: String = PermissionNames.READ_EXTERNAL_STORAGE

        /** 分区存储的 Meta Data Key（仅供内部调用）  */
        const val META_DATA_KEY_SCOPED_STORAGE: String = "ScopedStorage"
        @JvmField
        val CREATOR : Parcelable.Creator<ReadExternalStoragePermission> =


            object : Parcelable.Creator<ReadExternalStoragePermission> {
                /** 当前权限名称，注意：该常量字段仅供框架内部使用，不提供给外部引用，
                 * 如果需要获取权限名称的字符串，请直接通过 [PermissionNames] 类获取
                 */

                override fun createFromParcel(source: Parcel): ReadExternalStoragePermission {
                    return ReadExternalStoragePermission(source)
                }

                override fun newArray(size: Int): Array<ReadExternalStoragePermission?> {
                    return arrayOfNulls(size)
                }
            }
    }
}