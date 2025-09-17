package com.knight.kotlin.library_permiss.permission.dangerous

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.health.connect.HealthConnectManager
import android.os.Parcel
import com.knight.kotlin.library_permiss.manifest.AndroidManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.PermissionManifestInfo
import com.knight.kotlin.library_permiss.permission.PermissionGroups
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.permission.common.DangerousPermission
import com.knight.kotlin.library_permiss.tools.PermissionVersion
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid14
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid15


/**
 * @author created by luguian
 * @organize
 * @Date 2025/9/17 15:10
 * @descript:健康数据通用权限类
 */
abstract class HealthDataBasePermission : DangerousPermission {
    protected constructor() : super()

    protected constructor(`in`: Parcel) : super(`in`)

    
    override fun getPermissionPageType(context: Context): PermissionPageType {
        return PermissionPageType.OPAQUE_ACTIVITY
    }

    override fun getPermissionGroup( context: Context): String {
        return PermissionGroups.HEALTH
    }

    
    override fun getPermissionSettingIntents( context: Context, skipRequest: Boolean): List<Intent> {
        val intentList = super.getPermissionSettingIntents(context, skipRequest)

        var intent: Intent
        // 在某些 Android 14 ~ Android 15 手机上面，权限设置页是没有健康数据共享权限的入口的，
        // 所以这里直接跳转到健康数据共享的权限设置页，Android 16 则直接跳转到应用详情页就可以了
        if (isAndroid14() && !PermissionVersion.isAndroid16()) {
            val healthIntentList: MutableList<Intent> = ArrayList(3)

            // 亲测 ACTION_MANAGE_HEALTH_PERMISSIONS 这个意图在 Android 14 可以正常跳转，但是 Android 15 跳转会出现异常，意思是没有权限可以跳转到这个页面
            // java.lang.SecurityException: Permission Denial: starting Intent { act=android.health.connect.action.MANAGE_HEALTH_PERMISSIONS xflg=0x4
            // cmp=com.google.android.healthconnect.controller/com.android.healthconnect.controller.PermissionControllerEntryPoint (has extras) } from
            // ProcessRecord{18b95b4 25796:com.hjq.permissions.demo/u0a222} (pid=25796, uid=10222) requires android.permission.GRANT_RUNTIME_PERMISSIONS
            if (!isAndroid15()) {
                val action = HealthConnectManager.ACTION_MANAGE_HEALTH_PERMISSIONS
                intent = Intent(action)
                intent.putExtra(Intent.EXTRA_PACKAGE_NAME, context.packageName)
                healthIntentList.add(intent)

                // 如果是因为加包名的数据后导致不能跳转，就把包名的数据移除掉
                intent = Intent(action)
                healthIntentList.add(intent)
            }

            // android.provider.Settings.ACTION_HEALTH_HOME_SETTINGS
            intent = Intent("android.health.connect.action.HEALTH_HOME_SETTINGS")
            healthIntentList.add(intent)

            // 将健康数据共享的权限设置页添加到意图列表中，放在集合的最前面，才会优先去跳转到这些意图
            intentList.addAll(0, healthIntentList)
        }

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
        val healthAction = if (PermissionVersion.isAndroid16()) {
            Intent.ACTION_VIEW_PERMISSION_USAGE
        } else {
            "android.intent.action.VIEW_PERMISSION_USAGE"
        }
        val healthCategory = if (PermissionVersion.isAndroid16()) {
            HealthConnectManager.CATEGORY_HEALTH_PERMISSIONS
        } else {
            "android.intent.category.HEALTH_PERMISSIONS"
        }

        // 当前是否注册了健康隐私政策页面的意图
        var registeredHealthPrivacyPolicyAction = false
        for (activityInfo in manifestInfo.activityInfoList) {
            val intentFilterInfoList = activityInfo.intentFilterInfoList ?: continue
            for (intentFilterInfo in intentFilterInfoList) {
                if (intentFilterInfo.actionList.contains(healthAction) &&
                    intentFilterInfo.categoryList.contains(healthCategory)
                ) {
                    registeredHealthPrivacyPolicyAction = true
                    break
                }
            }
            if (registeredHealthPrivacyPolicyAction) {
                // 如果已经注册，就不再往下遍历
                break
            }
        }

        if (!registeredHealthPrivacyPolicyAction) {
            val xmlCode = ("\t\t<intent-filter>\n"
                    + "\t\t    <action android:name=\"" + healthAction + "\" />\n"
                    + "\t\t    <category android:name=\"" + healthCategory + "\" />\n"
                    + "\t\t</intent-filter>")
            // 必须指定显示应用的隐私权政策对话框
            // https://developer.android.google.cn/health-and-fitness/guides/health-connect/develop/get-started?hl=zh-cn#show-privacy-policy
            // 入口点，用户可以在以下方法进入：
            //   1. 应用详情页 > 权限 > 健康数据共享 > 阅读隐私政策
            //   2. 转到设置 > 安全与隐私权 > 隐私权 > Health Connect > 选定应用 > 阅读隐私政策
            //   3. 转到设置 > 安全与隐私权 > 隐私权 > 隐私信息中心 > 查看其他权限 > Health Connect > 选定应用 > 阅读隐私政策
            //   4. 转到设置 > 安全与隐私权 > 隐私权 > 权限管理器 > Health Connect > 选定应用 > 阅读隐私政策
            throw IllegalArgumentException(
                """Please add an intent filter for "${activity.getClass()}" in the AndroidManifest.xml file.
$xmlCode"""
            )
        }
    }
}