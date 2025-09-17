package com.knight.kotlin.library_permiss.permission.special

import android.Manifest.permission
import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.provider.Settings
import com.knight.kotlin.library_permiss.manifest.AndroidManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.PermissionManifestInfo
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.permission.common.SpecialPermission
import com.knight.kotlin.library_permiss.tools.PermissionVersion
import com.knight.kotlin.library_permiss.tools.PermissionVersion.getTargetVersion
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid12
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid13


/**
 * @author created by luguian
 * @organize
 * @Date 2025/7/11 8:58
 * @descript:闹钟权限类
 */
class ScheduleExactAlarmPermission : SpecialPermission {

    companion object {
        const val PERMISSION_NAME = PermissionNames.SCHEDULE_EXACT_ALARM

        @JvmField
        val CREATOR = object : Parcelable.Creator<ScheduleExactAlarmPermission> {
            override fun createFromParcel(source: Parcel): ScheduleExactAlarmPermission {
                return ScheduleExactAlarmPermission(source)
            }

            override fun newArray(size: Int): Array<ScheduleExactAlarmPermission?> {
                return arrayOfNulls(size)
            }
        }
    }
    constructor() : super()
    private constructor(parcel: Parcel) : super(parcel)

    override fun getPermissionName(): String = PERMISSION_NAME

    override fun getFromAndroidVersion( context: Context): Int {
        return PermissionVersion.ANDROID_12
    }

    override fun isGrantedPermission( context: Context, skipRequest: Boolean): Boolean {
        if (!isAndroid12()) {
            return true
        }
        val alarmManager = context.getSystemService(AlarmManager::class.java) ?: return false
        // 虽然这个 SystemService 永远不为空，但是不怕一万，就怕万一，开展防御性编程
        return alarmManager.canScheduleExactAlarms()
    }


    override fun getPermissionSettingIntents( context: Context, skipRequest: Boolean): MutableList<Intent> {
        val intentList: MutableList<Intent> = ArrayList(6)
        var intent: Intent

        if (isAndroid12()) {
            intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            intent.setData(getPackageNameUri(context))
            intentList.add(intent)

            // 如果是因为加包名的数据后导致不能跳转，就把包名的数据移除掉
            intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
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

    override fun isRegisterPermissionByManifestFile(): Boolean {
        // 不使用父类的方式来检查清单权限有没有注册，但是不代表不检查，这个权限比较复杂，需要自定义检查
        return false
    }

    override fun checkSelfByManifestFile(
         activity: Activity,
         requestList: List<IPermission>,
         manifestInfo: AndroidManifestInfo,
         permissionInfoList: List<PermissionManifestInfo>,
         currentPermissionInfo: PermissionManifestInfo
    ) {
        super.checkSelfByManifestFile(activity, requestList, manifestInfo, permissionInfoList, currentPermissionInfo)
        val useExactAlarmPermissionName = if (isAndroid13()) {
            permission.USE_EXACT_ALARM
        } else {
            "android.permission.USE_EXACT_ALARM"
        }

        if (getTargetVersion(activity) >= PermissionVersion.ANDROID_13 &&
            findPermissionInfoByList(permissionInfoList, useExactAlarmPermissionName) != null
        ) {
            // 如果当前项目适配了 Android 13 的话，并且在清单文件中注册了 USE_EXACT_ALARM 权限，那么 SCHEDULE_EXACT_ALARM 权限在清单文件中可以这样注册
            // <uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM" android:maxSdkVersion="32" />
            // 相关文档地址：https://developer.android.google.cn/reference/android/Manifest.permission#USE_EXACT_ALARM
            // 如果你的应用要上架 GooglePlay，那么需要慎重添加 USE_EXACT_ALARM 权限，因为不是日历、闹钟、时钟这类应用添加 USE_EXACT_ALARM 权限很难通过 GooglePlay 上架审核
            checkPermissionRegistrationStatus(permissionInfoList, getPermissionName(), PermissionVersion.ANDROID_12_L)
            return
        }

        checkPermissionRegistrationStatus(permissionInfoList, getPermissionName())
    }
}
