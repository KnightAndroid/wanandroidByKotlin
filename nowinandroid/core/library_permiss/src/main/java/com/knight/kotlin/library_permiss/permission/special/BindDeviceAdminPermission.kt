package com.knight.kotlin.library_permiss.permission.special

import android.app.Activity
import android.app.admin.DeviceAdminReceiver
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable.Creator
import com.knight.kotlin.library_permiss.manifest.AndroidManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.PermissionManifestInfo
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.permission.common.SpecialPermission
import com.knight.kotlin.library_permiss.tools.PermissionUtils
import com.knight.kotlin.library_permiss.tools.PermissionVersion


/**
 * @author created by luguian
 * @organize
 * @Date 2025/7/11 8:47
 * @descript:设备管理器权限类
 */
class BindDeviceAdminPermission : SpecialPermission {

    companion object {
        @JvmField
        val CREATOR: Creator<BindDeviceAdminPermission> =
            object : Creator<BindDeviceAdminPermission> {
                override fun createFromParcel(source: Parcel): BindDeviceAdminPermission {
                    return BindDeviceAdminPermission(source)
                }

                override fun newArray(size: Int): Array<BindDeviceAdminPermission?> {
                    return arrayOfNulls(size)
                }
            }
    }

    /** 设备管理器的 BroadcastReceiver 类名 */
    val deviceAdminReceiverClassName: String

    /** 申请设备管理器权限的附加说明 */
    val extraAddExplanation: String?

    constructor(
        deviceAdminReceiverClass: Class<out DeviceAdminReceiver>,
        extraAddExplanation: String?
    ) : this(deviceAdminReceiverClass.name, extraAddExplanation)

    constructor(
        deviceAdminReceiverClassName: String,
        extraAddExplanation: String?
    ) {
        this.deviceAdminReceiverClassName = deviceAdminReceiverClassName
        this.extraAddExplanation = extraAddExplanation
    }

    private constructor(parcel: Parcel) : this(
        requireNotNull(parcel.readString()) {
            "DeviceAdminReceiverClassName must not be null"
        },
        parcel.readString()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeString(deviceAdminReceiverClassName)
        dest.writeString(extraAddExplanation)
    }

    override fun getPermissionName(): String {
        return PermissionNames.BIND_DEVICE_ADMIN
    }

    override fun getFromAndroidVersion(): Int {
        return PermissionVersion.ANDROID_2_2
    }

    override fun isGrantedPermission(context: Context, skipRequest: Boolean): Boolean {
        val devicePolicyManager: DevicePolicyManager? = if (PermissionVersion.isAndroid6()) {
            context.getSystemService(DevicePolicyManager::class.java)
        } else {
            context.getSystemService(Context.DEVICE_POLICY_SERVICE) as? DevicePolicyManager
        }

        return devicePolicyManager?.isAdminActive(
            ComponentName(context, deviceAdminReceiverClassName)
        ) == true
    }

    override fun getPermissionSettingIntents(context: Context): MutableList<Intent> {
        val intentList = mutableListOf<Intent>()

        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
            putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, ComponentName(context, deviceAdminReceiverClassName))
            putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, extraAddExplanation)
        }
        intentList.add(intent)

        intentList.add(getAndroidSettingIntent())

        return intentList
    }

    override fun checkCompliance(
        activity: Activity,
        requestPermissions: List<IPermission>,
        androidManifestInfo: AndroidManifestInfo
    ) {
        super.checkCompliance(activity, requestPermissions, androidManifestInfo)
        require(deviceAdminReceiverClassName.isNotEmpty()) {
            "Pass the BroadcastReceiverClass parameter as empty"
        }
        require(PermissionUtils.isClassExist(deviceAdminReceiverClassName)) {
            "The passed-in $deviceAdminReceiverClassName is an invalid class"
        }
    }

    override fun checkSelfByManifestFile(
        activity: Activity,
        requestPermissions: List<IPermission>,
        androidManifestInfo: AndroidManifestInfo,
        permissionManifestInfoList: List<PermissionManifestInfo>,
        currentPermissionManifestInfo: PermissionManifestInfo
    ) {
        super.checkSelfByManifestFile(
            activity,
            requestPermissions,
            androidManifestInfo,
            permissionManifestInfoList,
            currentPermissionManifestInfo
        )

        for (receiverInfo in androidManifestInfo.broadcastReceiverManifestInfoList) {
            if (receiverInfo == null) continue
            if (!PermissionUtils.reverseEqualsString(deviceAdminReceiverClassName, receiverInfo.name)) continue

            if (receiverInfo.permission == null ||
                !PermissionUtils.equalsPermission(this, receiverInfo.permission)
            ) {
                throw IllegalArgumentException(
                    "Please register permission node in the AndroidManifest.xml file, for example: " +
                            "<receiver android:name=\"$deviceAdminReceiverClassName\" android:permission=\"${getPermissionName()}\" />"
                )
            }
            return
        }

        throw IllegalArgumentException("The \"$deviceAdminReceiverClassName\" component is not registered in the AndroidManifest.xml file")
    }
}