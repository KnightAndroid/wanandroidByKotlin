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
import com.knight.kotlin.library_permiss.permission.PermissionLists.getReadExternalStoragePermission
import com.knight.kotlin.library_permiss.permission.PermissionLists.getWriteExternalStoragePermission
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.permission.common.SpecialPermission
import com.knight.kotlin.library_permiss.tools.PermissionUtils
import com.knight.kotlin.library_permiss.tools.PermissionVersion
import com.knight.kotlin.library_permiss.tools.PermissionVersion.getTargetVersion
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid10
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid11


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

    override fun getFromAndroidVersion( context: Context): Int {
        return PermissionVersion.ANDROID_11
    }

    
    override fun getOldPermissions(context: Context): List<IPermission> {
        // Android 11 以下访问完整的文件管理需要用到读写外部存储的权限
        return PermissionUtils.asArrayList(
            getReadExternalStoragePermission(),
            getWriteExternalStoragePermission()
        )
    }

    override fun isGrantedPermission( context: Context, skipRequest: Boolean): Boolean {
        if (!isAndroid11()) {
            // 这个是 Android 10 上面的历史遗留问题，假设申请的是 MANAGE_EXTERNAL_STORAGE 权限
            // 必须要在 AndroidManifest.xml 中注册 android:requestLegacyExternalStorage="true"
            // Environment.isExternalStorageLegacy API 解释：是否采用的是非分区存储的模式
            if (isAndroid10() && !Environment.isExternalStorageLegacy()) {
                return false
            }
            return getReadExternalStoragePermission().isGrantedPermission(context, skipRequest) &&
                    getWriteExternalStoragePermission().isGrantedPermission(context, skipRequest)
        }
        // 是否有所有文件的管理权限
        return Environment.isExternalStorageManager()
    }

    
    override fun getPermissionSettingIntents( context: Context, skipRequest: Boolean): MutableList<Intent> {
        val intentList: MutableList<Intent> = ArrayList(3)
        var intent: Intent

        if (isAndroid11()) {
            intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.setData(getPackageNameUri(context))
            intentList.add(intent)

            intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            intentList.add(intent)
        }

        intent = getAndroidSettingIntent()
        intentList.add(intent)

        return intentList
    }

    override fun isRegisterPermissionByManifestFile(): Boolean {
        // 表示当前权限需要在 AndroidManifest.xml 文件中进行静态注册
        return true
    }

    protected override fun checkSelfByManifestFile(
         activity: Activity,
         requestList: List<IPermission>,
         manifestInfo: AndroidManifestInfo,
         permissionInfoList: List<PermissionManifestInfo>,
         currentPermissionInfo: PermissionManifestInfo
    ) {
        super.checkSelfByManifestFile(activity, requestList, manifestInfo, permissionInfoList, currentPermissionInfo)
        // 如果权限出现的版本小于 minSdkVersion，则证明该权限可能会在旧系统上面申请，需要在 AndroidManifest.xml 文件注册一下旧版权限
        if (getFromAndroidVersion(activity) > getMinSdkVersion(activity, manifestInfo)) {
            checkPermissionRegistrationStatus(permissionInfoList, PermissionNames.READ_EXTERNAL_STORAGE, PermissionVersion.ANDROID_10)
            checkPermissionRegistrationStatus(permissionInfoList, PermissionNames.WRITE_EXTERNAL_STORAGE, PermissionVersion.ANDROID_10)
        }

        // 如果申请的是 Android 10 获取媒体位置权限，则绕过本次检查
        if (PermissionUtils.containsPermission(requestList, PermissionNames.ACCESS_MEDIA_LOCATION)) {
            return
        }

        val applicationInfo = manifestInfo.applicationInfo ?: return

        // 如果在已经适配 Android 10 的情况下，但是 android:requestLegacyExternalStorage 的属性为 false（假设没有注册该属性的情况下则获取到的值为 false）
        check(!(getTargetVersion(activity) >= PermissionVersion.ANDROID_10 && !applicationInfo.requestLegacyExternalStorage)) {
            "Please register the android:requestLegacyExternalStorage=\"true\" " +
                    "attribute in the AndroidManifest.xml file, otherwise it will cause incompatibility with the old version"
        }
    }

    protected override fun checkSelfByRequestPermissions( activity: Activity,  requestList: List<IPermission>) {
        super.checkSelfByRequestPermissions(activity, requestList)
        // 检测是否有旧版的存储权限，有的话直接抛出异常，请不要自己动态申请这两个权限
        // 框架会在 Android 10 以下的版本上自动添加并申请这两个权限
        require(
            !(PermissionUtils.containsPermission(requestList, PermissionNames.READ_EXTERNAL_STORAGE) ||
                    PermissionUtils.containsPermission(requestList, PermissionNames.WRITE_EXTERNAL_STORAGE))
        ) {
            "If you have applied for \"" + getPermissionName() + "\" permissions, " +
                    "do not apply for the \"" + PermissionNames.READ_EXTERNAL_STORAGE +
                    "\" or \"" + PermissionNames.WRITE_EXTERNAL_STORAGE + "\" permissions"
        }

        // 因为 MANAGE_EXTERNAL_STORAGE 权限范围很大，有了它就可以读取媒体文件，不需要再叠加申请媒体权限
        require(
            !(PermissionUtils.containsPermission(requestList, PermissionNames.READ_MEDIA_IMAGES) ||
                    PermissionUtils.containsPermission(requestList, PermissionNames.READ_MEDIA_VIDEO) ||
                    PermissionUtils.containsPermission(requestList, PermissionNames.READ_MEDIA_AUDIO))
        ) {
            ("Because the \"" + getPermissionName() + "\" permission range is very large, "
                    + "you can read media files with it, and there is no need to apply for additional media permissions.")
        }
    }
}
