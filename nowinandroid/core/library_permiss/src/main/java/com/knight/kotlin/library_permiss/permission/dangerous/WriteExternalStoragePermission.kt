package com.knight.kotlin.library_permiss.permission.dangerous

import android.app.Activity
import android.content.Context
import android.os.Environment
import android.os.Parcel
import android.os.Parcelable
import com.knight.kotlin.library_permiss.manifest.AndroidManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.PermissionManifestInfo
import com.knight.kotlin.library_permiss.permission.PermissionGroups
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.permission.common.DangerousPermission
import com.knight.kotlin.library_permiss.tools.PermissionUtils
import com.knight.kotlin.library_permiss.tools.PermissionVersion


/**
 * @Description 写入外部存储权限类
 * @Author knight
 * @Time 2025/7/10 21:45
 *
 */

class WriteExternalStoragePermission : DangerousPermission {
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
        if (PermissionVersion.isAndroid11() && PermissionVersion.getTargetVersion(context) >= PermissionVersion.ANDROID_11) {
            // 这里补充一下这样写的具体原因：
            // 1. 当 targetSdk >= Android 11 并且在此版本及之上申请 WRITE_EXTERNAL_STORAGE，虽然可以弹出授权框，但是没有什么实际作用
            //    相关文档地址：https://developer.android.google.cn/reference/android/Manifest.permission#WRITE_EXTERNAL_STORAGE
            //                https://developer.android.google.cn/about/versions/11/privacy/storage?hl=zh-cn#permissions-target-11
            //    开发者可能会在清单文件注册 android:maxSdkVersion="29" 属性，这样会导致 WRITE_EXTERNAL_STORAGE 权限申请失败，这里需要返回 true 给外层
            // 2. 当 targetSdk >= Android 13 并且在此版本及之上申请 WRITE_EXTERNAL_STORAGE，会被系统直接拒绝
            //    不会弹出系统授权对话框，框架为了保证不同 Android 版本的回调结果一致性，这里需要返回 true 给到外层
            // 基于上面这两个原因，所以当项目 targetSdk >= Android 11 并且运行在 Android 11 及以上的设备上面时
            // 判断 WRITE_EXTERNAL_STORAGE 权限，结果无论是否授予，最终都会直接返回 true 给外层
            return true
        }
        if (PermissionVersion.isAndroid10() && PermissionVersion.getTargetVersion(context) >= PermissionVersion.ANDROID_10) {
            // Environment.isExternalStorageLegacy API 解释：是否采用的是非分区存储的模式
            return Environment.isExternalStorageLegacy()
        }
        return super.isGrantedPermissionByStandardVersion(context, skipRequest)
    }

    override fun isDoNotAskAgainPermissionByStandardVersion( activity: Activity): Boolean {
        if (PermissionVersion.isAndroid11() && PermissionVersion.getTargetVersion(activity) >= PermissionVersion.ANDROID_11) {
            return false
        }
        // Environment.isExternalStorageLegacy API 解释：是否采用的是非分区存储的模式
        if (PermissionVersion.isAndroid10() && PermissionVersion.getTargetVersion(activity) >= PermissionVersion.ANDROID_10 &&
            Environment.isExternalStorageLegacy()
        ) {
            return false
        }
        return super.isDoNotAskAgainPermissionByStandardVersion(activity)
    }

    override fun isRegisterPermissionByManifestFile(): Boolean {
        // 不使用父类的方式来检查清单权限有没有注册，但是不代表不检查，这个权限比较复杂，需要自定义检查
        return false
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
        val applicationManifestInfo = androidManifestInfo.applicationManifestInfo ?: return

        // 如果当前 targetSdk 版本比较低，甚至还没有到分区存储的版本，就直接跳过后面的检查，只检查当前权限有没有在清单文件中静态注册
        if (PermissionVersion.getTargetVersion(activity) < PermissionVersion.ANDROID_10) {
            checkPermissionRegistrationStatus(permissionManifestInfoList, getPermissionName())
            return
        }

        // 判断：当前项目是否适配了Android 11，并且还在清单文件中是否注册了 MANAGE_EXTERNAL_STORAGE 权限
        if (PermissionVersion.getTargetVersion(activity) >= PermissionVersion.ANDROID_11 &&
            findPermissionInfoByList(
                permissionManifestInfoList,
                PermissionNames.MANAGE_EXTERNAL_STORAGE
            ) != null
        ) {
            // 如果有的话，那么 maxSdkVersion 就必须是 Android 10 及以上的版本
            checkPermissionRegistrationStatus(
                permissionManifestInfoList,
                getPermissionName(),
                PermissionVersion.ANDROID_10
            )
        } else {
            // 检查这个权限有没有在清单文件中注册，WRITE_EXTERNAL_STORAGE 权限比较特殊，要单独拎出来判断
            // 如果在清单文件中注册了 android:requestLegacyExternalStorage="true" 属性，即可延长一个 Android 版本适配
            // 所以 requestLegacyExternalStorage 属性在开启的状态下，对 maxSdkVersion 属性的要求延长一个版本
            checkPermissionRegistrationStatus(
                permissionManifestInfoList,
                getPermissionName(),
                if (applicationManifestInfo.requestLegacyExternalStorage) PermissionVersion.ANDROID_10 else PermissionVersion.ANDROID_9
            )
        }

        // 如果申请的是 Android 10 获取媒体位置权限，则跳过后面的检查
        if (PermissionUtils.containsPermission(
                requestPermissions,
                PermissionNames.ACCESS_MEDIA_LOCATION
            )
        ) {
            return
        }

        val targetSdkVersion: Int = PermissionVersion.getTargetVersion(activity)
        // 是否适配了分区存储
        val scopedStorage: Boolean = PermissionUtils.getBooleanByMetaData(
            activity,
            ReadExternalStoragePermission.META_DATA_KEY_SCOPED_STORAGE,
            false
        )
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




        companion object CREATOR: Parcelable.Creator<WriteExternalStoragePermission> {
        /** 当前权限名称，注意：该常量字段仅供框架内部使用，不提供给外部引用，如果需要获取权限名称的字符串，请直接通过 [PermissionNames] 类获取  */
        val PERMISSION_NAME: String = PermissionNames.WRITE_EXTERNAL_STORAGE

            override fun createFromParcel(source: Parcel): WriteExternalStoragePermission? {
                return WriteExternalStoragePermission(source)
            }

            override fun newArray(size: Int): Array<WriteExternalStoragePermission?> {
                return arrayOfNulls(size)
            }

    }
}