package com.knight.kotlin.library_permiss.tools

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.knight.kotlin.library_permiss.permission.PermissionType
import com.knight.kotlin.library_permiss.permission.base.IPermission
import kotlin.math.max


/**
 * Author:Knight
 * Time:2023/8/29 17:41
 * Description:PermissionApi
 */
object PermissionApi {
    /**
     * 判断某个权限集合是否包含特殊权限
     */
    fun containsSpecialPermission( permissions: List<IPermission>?): Boolean {
        if (permissions == null || permissions.isEmpty()) {
            return false
        }

        for (permission in permissions) {
            if (permission.getPermissionType() === PermissionType.SPECIAL) {
                return true
            }
        }
        return false
    }

    /**
     * 判断某个权限集合是否包含后台权限
     */
    fun containsBackgroundPermission( context: Context,  permissions: List<IPermission>?): Boolean {
        if (permissions == null || permissions.isEmpty()) {
            return false
        }

        for (permission in permissions) {
            if (permission.isBackgroundPermission(context)) {
                return true
            }
        }
        return false
    }

    /**
     * 判断某些权限是否全部被授予
     */
    fun isGrantedPermissions( context: Context,  permissions: List<IPermission>): Boolean {
        if (permissions.isEmpty()) {
            return false
        }

        for (permission in permissions) {
            if (!permission.isGrantedPermission(context)) {
                return false
            }
        }

        return true
    }

    /**
     * 获取已经授予的权限
     */
    fun getGrantedPermissions( context: Context,  permissions: List<IPermission>): List<IPermission> {
        val grantedPermissions: MutableList<IPermission> = ArrayList(permissions.size)
        for (permission in permissions) {
            if (permission.isGrantedPermission(context)) {
                grantedPermissions.add(permission)
            }
        }
        return grantedPermissions
    }

    /**
     * 获取已经拒绝的权限
     */
    fun getDeniedPermissions( context: Context,  permissions: List<IPermission>): List<IPermission> {
        val deniedPermissions: MutableList<IPermission> = ArrayList(permissions.size)
        for (permission in permissions) {
            if (!permission.isGrantedPermission(context)) {
                deniedPermissions.add(permission)
            }
        }
        return deniedPermissions
    }

    /**
     * 在权限组中检查是否有某个权限是否被永久拒绝
     *
     * @param activity              Activity对象
     * @param permissions            请求的权限
     */
    fun isDoNotAskAgainPermissions( activity: Activity,  permissions: List<IPermission>): Boolean {
        for (permission in permissions) {
            if (permission.isDoNotAskAgainPermission(activity)) {
                return true
            }
        }
        return false
    }

    /**
     * 根据传入的权限自动选择最合适的权限设置页的意图
     */

    fun getBestPermissionSettingIntent(
        context: Context,
        permissions: List<IPermission>?
    ): MutableList<Intent> {
        if (permissions.isNullOrEmpty()) {
            return PermissionSettingPage.getCommonPermissionSettingIntent(context)
        }

        // 过滤出适配当前版本的有效权限（拷贝一份）
        val realPermissions = permissions
            .filter { it.getFromAndroidVersion() <= PermissionVersion.getCurrentVersion() }
            .toMutableList()

        // 移除与特殊权限冲突的旧权限
        val iterator = realPermissions.iterator()
        while (iterator.hasNext()) {
            val permission = iterator.next()
            val oldPermissions = permission.getOldPermissions(context)

            if (!oldPermissions.isNullOrEmpty() &&
                (permission.getPermissionType() == PermissionType.SPECIAL || containsSpecialPermission(oldPermissions))
            ) {
                // 移除其旧版本权限（若包含在列表中）
                realPermissions.removeAll(oldPermissions.toSet())
            }
        }

        if (realPermissions.isEmpty()) {
            return PermissionSettingPage.getCommonPermissionSettingIntent(context)
        }

        if (realPermissions.size == 1) {
            return realPermissions[0].getPermissionSettingIntents(context)
        }

        // 多个权限时，检测其 Intent 是否一致
        val baseIntents = realPermissions[0].getPermissionSettingIntents(context) ?: return PermissionSettingPage.getCommonPermissionSettingIntent(context)

        for (i in 1 until realPermissions.size) {
            val currentIntents = realPermissions[i].getPermissionSettingIntents(context) ?: return PermissionSettingPage.getCommonPermissionSettingIntent(context)
            if (!PermissionUtils.equalsIntentList(baseIntents, currentIntents)) {
                return PermissionSettingPage.getCommonPermissionSettingIntent(context)
            }
        }

        return baseIntents
    }

    /**
     * 根据新权限添加旧权限
     */
    @Synchronized
    fun addOldPermissionsByNewPermissions(
        context: Context,
        requestPermissions: MutableList<IPermission>
    ) {
        var index = 0
        while (index < requestPermissions.size) {
            val permission = requestPermissions[index]

            if (PermissionVersion.getCurrentVersion() >= permission.getFromAndroidVersion()) {
                index++
                continue
            }

            val oldPermissions = permission.getOldPermissions(context)
            if (oldPermissions.isNullOrEmpty()) {
                index++
                continue
            }

            // 使用 set 提高 contains 判断效率
            val permissionSet = requestPermissions.toHashSet()

            for (oldPermission in oldPermissions) {
                if (!permissionSet.contains(oldPermission)) {
                    // 插入旧权限到当前权限之后
                    requestPermissions.add(++index, oldPermission)
                    permissionSet.add(oldPermission) // 避免重复添加
                }
            }

            index++
        }
    }
    /**
     * 判断传入的权限组是不是都是危险权限
     */
    fun areAllDangerousPermission( permissions: List<IPermission>): Boolean {
        for (permission in permissions) {
            if (permission.getPermissionType() === PermissionType.SPECIAL) {
                return false
            }
        }
        return true
    }

    /**
     * 通过权限集合获取最大的间隔时间
     */
    fun getMaxIntervalTimeByPermissions( context: Context,  permissions: List<IPermission>?): Int {
        if (permissions == null) {
            return 0
        }
        var maxWaitTime = 0
        for (permission in permissions) {
            val time = permission.getRequestIntervalTime(context)
            if (time == 0) {
                continue
            }
            maxWaitTime = max(maxWaitTime.toDouble(), time.toDouble()).toInt()
        }
        return maxWaitTime
    }

    /**
     * 通过权限集合获取最大的回调等待时间
     */
    fun getMaxWaitTimeByPermissions( context: Context,  permissions: List<IPermission>?): Int {
        if (permissions == null) {
            return 0
        }
        var maxWaitTime = 0
        for (permission in permissions) {
            val time = permission.getResultWaitTime(context)
            if (time == 0) {
                continue
            }
            maxWaitTime = max(maxWaitTime.toDouble(), time.toDouble()).toInt()
        }
        return maxWaitTime
    }
}