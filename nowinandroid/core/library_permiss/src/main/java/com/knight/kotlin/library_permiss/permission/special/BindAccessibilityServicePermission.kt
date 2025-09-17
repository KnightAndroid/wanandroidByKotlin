package com.knight.kotlin.library_permiss.permission.special

import android.accessibilityservice.AccessibilityService
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.provider.Settings
import android.text.TextUtils
import com.knight.kotlin.library_permiss.manifest.AndroidManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.IntentFilterManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.MetaDataManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.PermissionManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.ServiceManifestInfo
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.permission.common.SpecialPermission
import com.knight.kotlin.library_permiss.tools.PermissionUtils
import com.knight.kotlin.library_permiss.tools.PermissionVersion


/**
 * @author created by luguian
 * @organize
 * @Date 2025/7/11 8:46
 * @descript:无障碍服务权限类
 */
class BindAccessibilityServicePermission( accessibilityServiceClassName: String) : SpecialPermission() {
    /** 无障碍 Service 类名  */
    
    private val mAccessibilityServiceClassName = accessibilityServiceClassName

    constructor( accessibilityServiceClass: Class<out AccessibilityService?>) : this(accessibilityServiceClass.name)
    
    private constructor(parcel: Parcel) : this(parcel.readString() ?: "")

    override fun writeToParcel( dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeString(mAccessibilityServiceClassName)
    }

    
    override fun getPermissionName(): String {
        return PERMISSION_NAME
    }

    override fun getFromAndroidVersion(context: Context): Int {
        return PermissionVersion.ANDROID_4_1
    }

    override fun isGrantedPermission( context: Context, skipRequest: Boolean): Boolean {
        val enabledNotificationListeners = Settings.Secure.getString(context.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
        if (TextUtils.isEmpty(enabledNotificationListeners)) {
            return false
        }

        val serviceClassName = if (PermissionUtils.isClassExist(mAccessibilityServiceClassName)) mAccessibilityServiceClassName else null
        // hello.litiaotiao.app/hello.litiaotiao.app.LttService:com.hjq.permissions.demo/com.hjq.permissions.demo.DemoAccessibilityService
        val allComponentNameArray = enabledNotificationListeners.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (component in allComponentNameArray) {
            val componentName = ComponentName.unflattenFromString(component) ?: continue
            if (serviceClassName != null) {
                // 精准匹配：匹配应用包名及 Service 类名
                if (context.packageName == componentName.packageName &&
                    serviceClassName == componentName.className
                ) {
                    return true
                }
            } else {
                // 模糊匹配：仅匹配应用包名
                if (context.packageName == componentName.packageName) {
                    return true
                }
            }
        }
        return false
    }


    override fun getPermissionSettingIntents(context: Context, skipRequest: Boolean): MutableList<Intent> {
        val intentList: MutableList<Intent> = ArrayList(2)
        // 这里解释一下为什么只能跳转到无障碍设置页？而不是当前应用的无障碍设置页？
        // 这是因为系统没有开放这个途径给应用层去实现，所以实现不了，你可能会说，这不是瞎扯？
        // 我明明看到 Settings 类中有一个意图叫 ACTION_ACCESSIBILITY_DETAILS_SETTINGS，怎么就实现不了？
        // 能看到不代表能用，OK？这个 Action 我已经帮大家试过了，普通应用没有办法跳转的，放弃吧
        intentList.add(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        intentList.add(getAndroidSettingIntent())
        return intentList
    }

    override fun checkCompliance( activity: Activity,  requestList: List<IPermission>,  manifestInfo: AndroidManifestInfo) {
        super.checkCompliance(activity, requestList, manifestInfo)
        require(!TextUtils.isEmpty(mAccessibilityServiceClassName)) { "Pass the ServiceClass parameter as empty" }
        require(PermissionUtils.isClassExist(mAccessibilityServiceClassName)) { "The passed-in $mAccessibilityServiceClassName is an invalid class" }
    }

    protected override fun checkSelfByManifestFile(
         activity: Activity,
         requestList: List<IPermission>,
         manifestInfo: AndroidManifestInfo,
         permissionInfoList: List<PermissionManifestInfo>,
         currentPermissionInfo: PermissionManifestInfo
    ) {
        super.checkSelfByManifestFile(activity, requestList, manifestInfo, permissionInfoList, currentPermissionInfo)

        val serviceInfoList: List<ServiceManifestInfo> = manifestInfo.serviceInfoList
        for (serviceInfo in serviceInfoList) {
            if (serviceInfo == null) {
                continue
            }

            if (!PermissionUtils.reverseEqualsString(mAccessibilityServiceClassName, serviceInfo.name)) {
                // 不是目标的 Service，继续循环
                continue
            }

            require(!(serviceInfo.permission == null || !PermissionUtils.equalsPermission(this, serviceInfo.permission))) {
                ("Please register permission node in the AndroidManifest.xml file, for example: "
                        + "<service android:name=\"" + mAccessibilityServiceClassName + "\" android:permission=\"" + getPermissionName() + "\" />")
            }

            val action = "android.accessibilityservice.AccessibilityService"
            // 当前是否注册了无障碍服务的意图
            var registeredAccessibilityServiceAction = false
            val intentFilterInfoList: List<IntentFilterManifestInfo> = serviceInfo.intentFilterInfoList
            if (intentFilterInfoList != null) {
                for (intentFilterInfo in intentFilterInfoList) {
                    if (intentFilterInfo.actionList.contains(action)) {
                        registeredAccessibilityServiceAction = true
                        break
                    }
                }
            }

            if (!registeredAccessibilityServiceAction) {
                val xmlCode = ("\t\t<intent-filter>\n"
                        + "\t\t    <action android:name=\"" + action + "\" />\n"
                        + "\t\t</intent-filter>")
                throw IllegalArgumentException(
                    """Please add an intent filter for "$mAccessibilityServiceClassName" in the AndroidManifest.xml file.
$xmlCode"""
                )
            }

            val metaDataName = AccessibilityService.SERVICE_META_DATA
            // 当前是否注册了无障碍服务的 MetaData
            var registeredAccessibilityServiceMetaData = false
            val metaDataInfoList: List<MetaDataManifestInfo> = serviceInfo.metaDataInfoList
            if (metaDataInfoList != null) {
                for (metaDataInfo in metaDataInfoList) {
                    if (metaDataName == metaDataInfo.name && metaDataInfo.resource != 0) {
                        registeredAccessibilityServiceMetaData = true
                        break
                    }
                }
            }

            if (!registeredAccessibilityServiceMetaData) {
                val xmlCode = ("\t\t<meta-data>\n"
                        + "\t\t    android:name=\"" + metaDataName + "\"\n"
                        + "\t\t    android:resource=\"@xml/accessibility_service_config" + "\"" + " />")
                throw IllegalArgumentException(
                    """Please add an meta data for "$mAccessibilityServiceClassName" in the AndroidManifest.xml file.
$xmlCode"""
                )
            }

            // 符合要求，中断所有的循环并返回，避免走到后面的抛异常代码
            return
        }

        // 这个 Service 组件没有在清单文件中注册
        throw IllegalArgumentException("The \"$mAccessibilityServiceClassName\" component is not registered in the AndroidManifest.xml file")
    }

    
    fun getAccessibilityServiceClassName(): String {
        return mAccessibilityServiceClassName
    }

    companion object {
        val PERMISSION_NAME: String = PermissionNames.BIND_ACCESSIBILITY_SERVICE
        @JvmField
        val CREATOR : Parcelable.Creator<BindAccessibilityServicePermission> =


            object : Parcelable.Creator<BindAccessibilityServicePermission> {
                /** 当前权限名称，注意：该常量字段仅供框架内部使用，不提供给外部引用，
                 * 如果需要获取权限名称的字符串，请直接通过 [PermissionNames] 类获取
                 */

                override fun createFromParcel(source: Parcel): BindAccessibilityServicePermission {
                    return BindAccessibilityServicePermission(source)
                }

                override fun newArray(size: Int): Array<BindAccessibilityServicePermission?> {
                    return arrayOfNulls(size)
                }
            }
    }

}