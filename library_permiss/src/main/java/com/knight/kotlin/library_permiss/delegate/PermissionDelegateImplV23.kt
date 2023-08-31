package com.knight.kotlin.library_permiss.delegate

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.AndroidVersion
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid10
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid11
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid12
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid13
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid6
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid8
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid9
import com.knight.kotlin.library_permiss.PermissionIntentManager
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.utils.PermissionUtils.areActivityIntent
import com.knight.kotlin.library_permiss.utils.PermissionUtils.checkSelfPermission
import com.knight.kotlin.library_permiss.utils.PermissionUtils.equalsPermission
import com.knight.kotlin.library_permiss.utils.PermissionUtils.getPackageNameUri
import com.knight.kotlin.library_permiss.utils.PermissionUtils.isSpecialPermission
import com.knight.kotlin.library_permiss.utils.PermissionUtils.shouldShowRequestPermissionRationale
import com.knight.kotlin.library_permiss.utils.PhoneRomUtils


/**
 * Author:Knight
 * Time:2023/8/30 15:47
 * Description:PermissionDelegateImplV23
 */
@RequiresApi(api = AndroidVersion.ANDROID_6)
open class PermissionDelegateImplV23 : PermissionDelegateImplV21(){
    override fun isGrantedPermission(
        context: Context,
        permission: String
    ): Boolean {
        // 向下兼容 Android 13 新权限
        if (!isAndroid13()) {
            if (equalsPermission(permission, Permission.POST_NOTIFICATIONS)) {
                // 交给父类处理
                return super.isGrantedPermission(context, permission)
            }
            if (equalsPermission(permission, Permission.NEARBY_WIFI_DEVICES)) {
                return checkSelfPermission(
                    context, Permission.ACCESS_FINE_LOCATION
                )
            }
            if (equalsPermission(permission, Permission.BODY_SENSORS_BACKGROUND)) {
                return checkSelfPermission(
                    context, Permission.BODY_SENSORS
                )
            }
            if (equalsPermission(permission, Permission.READ_MEDIA_IMAGES) ||
                equalsPermission(permission, Permission.READ_MEDIA_VIDEO) ||
                equalsPermission(permission, Permission.READ_MEDIA_AUDIO)
            ) {
                return checkSelfPermission(
                    context, Permission.READ_EXTERNAL_STORAGE
                )
            }
        }

        // 向下兼容 Android 12 新权限
        if (!isAndroid12()) {
            if (equalsPermission(permission, Permission.BLUETOOTH_SCAN)) {
                return checkSelfPermission(
                    context!!, Permission.ACCESS_FINE_LOCATION
                )
            }
            if (equalsPermission(permission, Permission.BLUETOOTH_CONNECT) ||
                equalsPermission(permission, Permission.BLUETOOTH_ADVERTISE)
            ) {
                return true
            }
        }

        // 向下兼容 Android 11 新权限
        if (!isAndroid11()) {

            // 检测管理所有文件权限
            if (equalsPermission(permission, Permission.MANAGE_EXTERNAL_STORAGE)) {
                return checkSelfPermission(
                    context!!, Permission.READ_EXTERNAL_STORAGE
                ) &&
                        checkSelfPermission(context, Permission.WRITE_EXTERNAL_STORAGE)
            }
        }

        // 向下兼容 Android 10 新权限
        if (!isAndroid10()) {
            if (equalsPermission(permission, Permission.ACCESS_BACKGROUND_LOCATION)) {
                return checkSelfPermission(
                    context, Permission.ACCESS_FINE_LOCATION
                )
            }
            if (equalsPermission(permission, Permission.ACTIVITY_RECOGNITION)) {
                return true
            }
            if (equalsPermission(permission, Permission.ACCESS_MEDIA_LOCATION)) {
                return checkSelfPermission(
                    context!!, Permission.READ_EXTERNAL_STORAGE
                )
            }
        }

        // 向下兼容 Android 9.0 新权限
        if (!isAndroid9()) {
            if (equalsPermission(permission, Permission.ACCEPT_HANDOVER)) {
                return true
            }
        }

        // 向下兼容 Android 8.0 新权限
        if (!isAndroid8()) {
            if (equalsPermission(permission, Permission.ANSWER_PHONE_CALLS)) {
                return true
            }
            if (equalsPermission(permission, Permission.READ_PHONE_NUMBERS)) {
                return checkSelfPermission(
                    context!!, Permission.READ_PHONE_STATE
                )
            }
        }

        // 交给父类处理
        if (equalsPermission(permission, Permission.GET_INSTALLED_APPS) ||
            equalsPermission(permission, Permission.POST_NOTIFICATIONS)
        ) {
            return super.isGrantedPermission(context, permission)
        }
        if (isSpecialPermission(permission)) {
            // 检测系统权限
            if (equalsPermission(permission, Permission.WRITE_SETTINGS)) {
                return isGrantedSettingPermission(context)
            }

            // 检测勿扰权限
            if (equalsPermission(permission, Permission.ACCESS_NOTIFICATION_POLICY)) {
                return isGrantedNotDisturbPermission(context)
            }

            // 检测电池优化选项权限
            return if (equalsPermission(
                    permission, Permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                )
            ) {
                isGrantedIgnoreBatteryPermission(context)
            } else super.isGrantedPermission(context, permission)
        }
        return checkSelfPermission(context, permission)
    }

    override fun isPermissionPermanentDenied(
        activity: Activity,
        permission: String
    ): Boolean {
        // 向下兼容 Android 13 新权限
        if (!isAndroid13()) {
            if (equalsPermission(permission, Permission.POST_NOTIFICATIONS)) {
                return super.isPermissionPermanentDenied(activity, permission)
            }
            if (equalsPermission(permission, Permission.NEARBY_WIFI_DEVICES)) {
                return !checkSelfPermission(
                    activity, Permission.ACCESS_FINE_LOCATION
                ) &&
                        !shouldShowRequestPermissionRationale(
                            activity, Permission.ACCESS_FINE_LOCATION
                        )
            }
            if (equalsPermission(permission, Permission.BODY_SENSORS_BACKGROUND)) {
                return !checkSelfPermission(
                    activity, Permission.BODY_SENSORS
                ) &&
                        !shouldShowRequestPermissionRationale(
                            activity, Permission.BODY_SENSORS
                        )
            }
            if (equalsPermission(permission, Permission.READ_MEDIA_IMAGES) ||
                equalsPermission(permission, Permission.READ_MEDIA_VIDEO) ||
                equalsPermission(permission, Permission.READ_MEDIA_AUDIO)
            ) {
                return !checkSelfPermission(
                    activity, Permission.READ_EXTERNAL_STORAGE
                ) &&
                        !shouldShowRequestPermissionRationale(
                            activity!!, Permission.READ_EXTERNAL_STORAGE
                        )
            }
        }

        // 向下兼容 Android 12 新权限
        if (!isAndroid12()) {
            if (equalsPermission(permission, Permission.BLUETOOTH_SCAN)) {
                return !checkSelfPermission(
                    activity, Permission.ACCESS_FINE_LOCATION
                ) &&
                        !shouldShowRequestPermissionRationale(
                            activity, Permission.ACCESS_FINE_LOCATION
                        )
            }
            if (equalsPermission(permission, Permission.BLUETOOTH_CONNECT) ||
                equalsPermission(permission, Permission.BLUETOOTH_ADVERTISE)
            ) {
                return false
            }
        }

        // 向下兼容 Android 10 新权限
        if (!isAndroid10()) {
            if (equalsPermission(permission, Permission.ACCESS_BACKGROUND_LOCATION)) {
                return !checkSelfPermission(
                    activity, Permission.ACCESS_FINE_LOCATION
                ) &&
                        !shouldShowRequestPermissionRationale(
                            activity, Permission.ACCESS_FINE_LOCATION
                        )
            }
            if (equalsPermission(permission, Permission.ACTIVITY_RECOGNITION)) {
                return false
            }
            if (equalsPermission(permission, Permission.ACCESS_MEDIA_LOCATION)) {
                return !checkSelfPermission(
                    activity, Permission.READ_EXTERNAL_STORAGE
                ) &&
                        !shouldShowRequestPermissionRationale(
                            activity, Permission.READ_EXTERNAL_STORAGE
                        )
            }
        }

        // 向下兼容 Android 9.0 新权限
        if (!isAndroid9()) {
            if (equalsPermission(permission, Permission.ACCEPT_HANDOVER)) {
                return false
            }
        }

        // 向下兼容 Android 8.0 新权限
        if (!isAndroid8()) {
            if (equalsPermission(permission, Permission.ANSWER_PHONE_CALLS)) {
                return false
            }
            if (equalsPermission(permission, Permission.READ_PHONE_NUMBERS)) {
                return !checkSelfPermission(
                    activity, Permission.READ_PHONE_STATE
                ) &&
                        !shouldShowRequestPermissionRationale(
                            activity, Permission.READ_PHONE_STATE
                        )
            }
        }

        // 交给父类处理
        if (equalsPermission(permission, Permission.GET_INSTALLED_APPS) ||
            equalsPermission(permission, Permission.POST_NOTIFICATIONS)
        ) {
            return super.isPermissionPermanentDenied(activity, permission)
        }
        return if (isSpecialPermission(
                permission
            )
        ) {
            // 特殊权限不算，本身申请方式和危险权限申请方式不同，因为没有永久拒绝的选项，所以这里返回 false
            false
        } else !checkSelfPermission(activity, permission) &&
                !shouldShowRequestPermissionRationale(
                    activity, permission
                )
    }

    override fun getPermissionIntent(
        context: Context,
        permission: String
    ): Intent? {
        if (equalsPermission(permission, Permission.WRITE_SETTINGS)) {
            return getSettingPermissionIntent(context)
        }
        if (equalsPermission(permission, Permission.ACCESS_NOTIFICATION_POLICY)) {
            return getNotDisturbPermissionIntent(context)
        }
        return if (equalsPermission(
                permission, Permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            )
        ) {
            getIgnoreBatteryPermissionIntent(context)
        } else super.getPermissionIntent(context, permission)
    }

    companion object {
        /**
         * 是否有系统设置权限
         */
        private fun isGrantedSettingPermission(context: Context): Boolean {
            return if (isAndroid6()) {
                Settings.System.canWrite(context)
            } else true
        }

        /**
         * 获取系统设置权限界面意图
         */
        private fun getSettingPermissionIntent(context: Context): Intent? {
            var intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = getPackageNameUri(context)
            if (!areActivityIntent(context, intent)) {
                intent = PermissionIntentManager.getApplicationDetailsIntent(context)
            }
            return intent
        }

        /**
         * 是否有勿扰模式权限
         */
        private fun isGrantedNotDisturbPermission(context: Context): Boolean {
            return context.getSystemService(NotificationManager::class.java).isNotificationPolicyAccessGranted
        }

        /**
         * 获取勿扰模式设置界面意图
         */
        private fun getNotDisturbPermissionIntent(context: Context): Intent? {
            // android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_DETAIL_SETTINGS
            var intent = Intent("android.settings.NOTIFICATION_POLICY_ACCESS_DETAIL_SETTINGS")
            intent.data = getPackageNameUri(context)

            // issue 地址：https://github.com/getActivity/XXPermissions/issues/190
            // 这里解释一下，为什么要排除鸿蒙系统，因为用代码能检测到有这个 Intent，也能跳转过去，但是会被马上拒绝
            // 测试过了其他厂商系统及 Android 原生系统都没有这个问题，就只有鸿蒙有这个问题
            // 只因为这个 Intent 是隐藏的意图，所以就不让用，鸿蒙 2.0 和 3.0 都有这个问题
            // 别问鸿蒙 1.0 有没有问题，问就是鸿蒙一发布就 2.0 了，1.0 版本都没有问世过
            // ------------------------ 我是一条华丽的分割线 ----------------------------
            // issue 地址：https://github.com/getActivity/XXPermissions/issues/233
            // 经过测试，荣耀下面这些机子都会出现加包名跳转不过去的问题
            // 荣耀 magic4 Android 13  MagicOs 7.0
            // 荣耀 80 Pro Android 12  MagicOs 7.0
            // 荣耀 X20 SE Android 11  MagicOs 4.1
            // 荣耀 Play5 Android 10  MagicOs 4.0
            if (PhoneRomUtils.isHarmonyOs() || PhoneRomUtils.isMagicOs()) {
                intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
            }
            if (!areActivityIntent(context, intent)) {
                intent = PermissionIntentManager.getApplicationDetailsIntent(context)
            }
            return intent
        }

        /**
         * 是否忽略电池优化选项
         */
        private fun isGrantedIgnoreBatteryPermission(context: Context): Boolean {
            return context.getSystemService(PowerManager::class.java)
                .isIgnoringBatteryOptimizations(context.packageName)
        }

        /**
         * 获取电池优化选项设置界面意图
         */
        private fun getIgnoreBatteryPermissionIntent(context: Context): Intent? {
            var intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            intent.data = getPackageNameUri(context)
            if (!areActivityIntent(context, intent)) {
                intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
            }
            if (!areActivityIntent(context, intent)) {
                intent = PermissionIntentManager.getApplicationDetailsIntent(context)
            }
            return intent
        }
    }
}