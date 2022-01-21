package com.knight.kotlin.library_permiss

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.utils.PermissionUtils

/**
 * Author:Knight
 * Time:2022/1/20 15:26
 * Description:PermissionSettingPage
 */
class PermissionSettingPage {
    companion object{
        /**
         * 根据传入的权限自动选择最合适的权限设置页
         *
         * @param permissions                 请求失败的权限
         */
        fun getSmartPermissionIntent(context: Context, permissions: List<String?>?): Intent? {
            // 如果失败的权限里面不包含特殊权限
            if (permissions == null || permissions.isEmpty() ||
                !PermissionUtils.containsSpecialPermission(permissions)
            ) {
                return getApplicationDetailsIntent(context)
            }
            if (PermissionUtils.isAndroid11() && permissions.size == 3 &&
                permissions.contains(Permission.MANAGE_EXTERNAL_STORAGE) &&
                permissions.contains(Permission.READ_EXTERNAL_STORAGE) &&
                permissions.contains(Permission.WRITE_EXTERNAL_STORAGE)
            ) {
                return getStoragePermissionIntent(context)
            }

            // 如果当前只有一个权限被拒绝了
            if (permissions.size == 1) {
                val permission = permissions[0]
                if (Permission.MANAGE_EXTERNAL_STORAGE.equals(permission)) {
                    return getStoragePermissionIntent(context)
                }
                if (Permission.REQUEST_INSTALL_PACKAGES.equals(permission)) {
                    return getInstallPermissionIntent(context)
                }
                if (Permission.SYSTEM_ALERT_WINDOW.equals(permission)) {
                    return getWindowPermissionIntent(context)
                }
                if (Permission.WRITE_SETTINGS.equals(permission)) {
                    return getSettingPermissionIntent(context)
                }
                if (Permission.NOTIFICATION_SERVICE.equals(permission)) {
                    return getNotifyPermissionIntent(context)
                }
                if (Permission.PACKAGE_USAGE_STATS.equals(permission)) {
                    return getPackagePermissionIntent(context)
                }
            }
            return getApplicationDetailsIntent(context)
        }

        /**
         * 获取应用详情界面意图
         */
        fun getApplicationDetailsIntent(context: Context): Intent {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = getPackageNameUri(context)
            return intent
        }

        /**
         * 获取安装权限设置界面意图
         */
        fun getInstallPermissionIntent(context: Context): Intent {
            var intent: Intent? = null
            if (PermissionUtils.isAndroid8()) {
                intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
                intent.data = getPackageNameUri(context)
            }
            if (intent == null || !areActivityIntent(context, intent)) {
                intent = getApplicationDetailsIntent(context)
            }
            return intent
        }

        /**
         * 获取悬浮窗权限设置界面意图
         */
        fun getWindowPermissionIntent(context: Context): Intent {
            var intent: Intent? = null
            if (PermissionUtils.isAndroid6()) {
                intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                // 在 Android 11 加包名跳转也是没有效果的，官方文档链接：
                // https://developer.android.google.cn/reference/android/provider/Settings#ACTION_MANAGE_OVERLAY_PERMISSION
                intent.data = getPackageNameUri(context)
            }
            if (intent == null || !areActivityIntent(context, intent)) {
                intent = getApplicationDetailsIntent(context)
            }
            return intent
        }

        /**
         * 获取通知栏权限设置界面意图
         */
        fun getNotifyPermissionIntent(context: Context): Intent {
            var intent: Intent? = null
            if (PermissionUtils.isAndroid8()) {
                intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                //intent.putExtra(Settings.EXTRA_CHANNEL_ID, context.getApplicationInfo().uid);
            }
            if (intent == null || !areActivityIntent(context, intent)) {
                intent = getApplicationDetailsIntent(context)
            }
            return intent
        }



        /**
         * 获取系统设置权限界面意图
         */
        fun getSettingPermissionIntent(context: Context): Intent {
            var intent: Intent? = null
            if (PermissionUtils.isAndroid6()) {
                intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                intent.data = getPackageNameUri(context)
            }
            if (intent == null || !areActivityIntent(context, intent)) {
                intent = getApplicationDetailsIntent(context)
            }
            return intent
        }

        /**
         * 获取存储权限设置界面意图
         */
        fun getStoragePermissionIntent(context: Context): Intent {
            var intent: Intent? = null
            if (PermissionUtils.isAndroid11()) {
                intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = getPackageNameUri(context)
            }
            if (intent == null || !areActivityIntent(context, intent)) {
                intent = getApplicationDetailsIntent(context)
            }
            return intent
        }

        /**
         * 获取读取包权限设置界面意图
         */
        fun getPackagePermissionIntent(context: Context): Intent {
            var intent: Intent? = null
            if (PermissionUtils.isAndroid5()) {
                intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                if (PermissionUtils.isAndroid10()) {
                    // 经过测试，只有在 Android 10 及以上加包名才有效果
                    // 如果在 Android 10 以下加包名会导致无法跳转
                    intent.data = getPackageNameUri(context)
                }
            }
            if (intent == null || !areActivityIntent(context, intent)) {
                intent = getApplicationDetailsIntent(context)
            }
            return intent
        }

        /**
         * 判断这个意图的 Activity 是否存在
         */
        private fun areActivityIntent(context: Context, intent: Intent): Boolean {
            return !context.packageManager.queryIntentActivities(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
            ).isEmpty()
        }

        /**
         * 获取包名 Uri 对象
         */
        private fun getPackageNameUri(context: Context): Uri? {
            return Uri.parse("package:" + context.packageName)
        }



    }


}