package com.knight.kotlin.library_permiss.permission.special

import android.app.Activity
import android.app.admin.DeviceAdminReceiver
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable.Creator
import android.text.TextUtils
import com.knight.kotlin.library_permiss.manifest.AndroidManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.BroadcastReceiverManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.IntentFilterManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.MetaDataManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.PermissionManifestInfo
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.permission.common.SpecialPermission
import com.knight.kotlin.library_permiss.tools.PermissionUtils
import com.knight.kotlin.library_permiss.tools.PermissionVersion
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid6


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

    /** 设备管理器的 BroadcastReceiver 类名  */

    private val mDeviceAdminReceiverClassName: String

    /** 申请设备管理器权限的附加说明  */

    private val mExtraAddExplanation: String?

    constructor(
        deviceAdminReceiverClass: Class<out DeviceAdminReceiver>,
        extraAddExplanation: String?
    ) : this(deviceAdminReceiverClass.name, extraAddExplanation)

    constructor(
        deviceAdminReceiverClassName: String,
        extraAddExplanation: String?
    ) {
        this.mDeviceAdminReceiverClassName = deviceAdminReceiverClassName
        this.mExtraAddExplanation = extraAddExplanation
    }

    private constructor(parcel: Parcel) : this(
        requireNotNull(parcel.readString()) {
            "DeviceAdminReceiverClassName must not be null"
        },
        parcel.readString()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeString(mDeviceAdminReceiverClassName)
        dest.writeString(mExtraAddExplanation)
    }

    override fun getPermissionName(): String {
        return PermissionNames.BIND_DEVICE_ADMIN
    }

    override fun getFromAndroidVersion( context: Context): Int {
        return PermissionVersion.ANDROID_2_2
    }

    override fun isGrantedPermission( context: Context, skipRequest: Boolean): Boolean {
        val devicePolicyManager = if (isAndroid6()) {
            context.getSystemService(DevicePolicyManager::class.java)
        } else {
            context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        }
        // 虽然这个 SystemService 永远不为空，但是不怕一万，就怕万一，开展防御性编程
        if (devicePolicyManager == null) {
            return false
        }
        return devicePolicyManager.isAdminActive(ComponentName(context, mDeviceAdminReceiverClassName))
    }

    
    override fun getPermissionSettingIntents( context: Context, skipRequest: Boolean): MutableList<Intent> {
        val intentList: MutableList<Intent> = ArrayList(2)

        var intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, ComponentName(context, mDeviceAdminReceiverClassName))
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, mExtraAddExplanation)
        intentList.add(intent)

        intent = getAndroidSettingIntent()
        intentList.add(intent)

        return intentList
    }

    override fun checkCompliance( activity: Activity,  requestList: List<IPermission>,  manifestInfo: AndroidManifestInfo) {
        super.checkCompliance(activity, requestList, manifestInfo)
        require(!TextUtils.isEmpty(mDeviceAdminReceiverClassName)) { "Pass the BroadcastReceiverClass parameter as empty" }
        require(PermissionUtils.isClassExist(mDeviceAdminReceiverClassName)) { "The passed-in $mDeviceAdminReceiverClassName is an invalid class" }
    }

    protected override fun checkSelfByManifestFile(
         activity: Activity,
         requestList: List<IPermission>,
         manifestInfo: AndroidManifestInfo,
         permissionInfoList: List<PermissionManifestInfo>,
         currentPermissionInfo: PermissionManifestInfo
    ) {
        super.checkSelfByManifestFile(activity, requestList, manifestInfo, permissionInfoList, currentPermissionInfo)

        val receiverInfoList: List<BroadcastReceiverManifestInfo> = manifestInfo.receiverInfoList
        for (receiverInfo in receiverInfoList) {
            if (receiverInfo == null) {
                continue
            }

            if (!PermissionUtils.reverseEqualsString(mDeviceAdminReceiverClassName, receiverInfo.name)) {
                // 不是目标的 BroadcastReceiver，继续循环
                continue
            }

            require(!(receiverInfo.permission == null || !PermissionUtils.equalsPermission(this, receiverInfo.permission))) {
                ("Please register permission node in the AndroidManifest.xml file, for example: "
                        + "<receiver android:name=\"" + mDeviceAdminReceiverClassName + "\" android:permission=\"" + getPermissionName() + "\" />")
            }

            val action = DeviceAdminReceiver.ACTION_DEVICE_ADMIN_ENABLED
            // 当前是否注册了设备管理器广播的意图
            var registeredDeviceAdminReceiverAction = false
            val intentFilterInfoList: List<IntentFilterManifestInfo> = receiverInfo.intentFilterInfoList
            if (intentFilterInfoList != null) {
                for (intentFilterInfo in intentFilterInfoList) {
                    if (intentFilterInfo.actionList.contains(action)) {
                        registeredDeviceAdminReceiverAction = true
                        break
                    }
                }
            }

            if (!registeredDeviceAdminReceiverAction) {
                val xmlCode = ("\t\t<intent-filter>\n"
                        + "\t\t    <action android:name=\"" + action + "\" />\n"
                        + "\t\t</intent-filter>")
                throw IllegalArgumentException(
                    """Please add an intent filter for "$mDeviceAdminReceiverClassName" in the AndroidManifest.xml file.
$xmlCode"""
                )
            }

            val metaDataName = DeviceAdminReceiver.DEVICE_ADMIN_META_DATA
            // 当前是否注册了设备管理器广播的 MetaData
            var registeredDeviceAdminReceiverMetaData = false
            val metaDataInfoList: List<MetaDataManifestInfo> = receiverInfo.metaDataInfoList
            if (metaDataInfoList != null) {
                for (metaDataInfo in metaDataInfoList) {
                    if (metaDataName == metaDataInfo.name && metaDataInfo.resource != 0) {
                        registeredDeviceAdminReceiverMetaData = true
                        break
                    }
                }
            }

            if (!registeredDeviceAdminReceiverMetaData) {
                val xmlCode = ("\t\t<meta-data>\n"
                        + "\t\t    android:name=\"" + metaDataName + "\"\n"
                        + "\t\t    android:resource=\"@xml/device_admin_config" + "\"" + " />")
                throw IllegalArgumentException(
                    """Please add an meta data for "$mDeviceAdminReceiverClassName" in the AndroidManifest.xml file.
$xmlCode"""
                )
            }

            // 符合要求，中断所有的循环并返回，避免走到后面的抛异常代码
            return
        }

        // 这个 BroadcastReceiver 组件没有在清单文件中注册
        throw IllegalArgumentException("The \"$mDeviceAdminReceiverClassName\" component is not registered in the AndroidManifest.xml file")
    }

    
    fun getDeviceAdminReceiverClassName(): String {
        return mDeviceAdminReceiverClassName
    }

    
    fun getExtraAddExplanation(): String? {
        return mExtraAddExplanation
    }
}