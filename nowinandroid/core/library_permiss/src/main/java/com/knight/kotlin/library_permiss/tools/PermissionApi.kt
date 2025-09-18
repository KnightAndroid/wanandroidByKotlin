package com.knight.kotlin.library_permiss.tools

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.knight.kotlin.library_permiss.permission.PermissionChannel
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.tools.PermissionSettingPage.getCommonPermissionSettingIntent
import com.knight.kotlin.library_permiss.tools.PermissionUtils.containsPermission

import com.knight.kotlin.library_permiss.tools.PermissionUtils.equalsIntentList
import com.knight.kotlin.library_permiss.tools.PermissionVersion.getCurrentVersion
import kotlin.math.max


/**
 * Author:Knight
 * Time:2023/8/29 17:41
 * Description:PermissionApi
 */
object PermissionApi {

    /**
     * 判断某个权限是否为健康权限
     */
    fun isHealthPermission( permission: IPermission): Boolean {
        return permission.getPermissionName().startsWith("android.permission.health.")
    }

    /**
     * 判断某个权限集合是否包含需要通过 startActivityForResult 授权的权限
     */
    fun containsPermissionByStartActivityForResult( context: Context,  permissions: List<IPermission>?): Boolean {
        if (permissions == null || permissions.isEmpty()) {
            return false
        }

        for (permission in permissions) {
            if (permission.getPermissionChannel(context) === PermissionChannel.START_ACTIVITY_FOR_RESULT) {
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
        val grantedList: MutableList<IPermission> = ArrayList(permissions.size)
        for (permission in permissions) {
            if (permission.isGrantedPermission(context)) {
                grantedList.add(permission)
            }
        }
        return grantedList
    }

    /**
     * 获取已经拒绝的权限
     */
    fun getDeniedPermissions( context: Context,  permissions: List<IPermission>): List<IPermission> {
        val deniedList: MutableList<IPermission> = ArrayList(permissions.size)
        for (permission in permissions) {
            if (!permission.isGrantedPermission(context)) {
                deniedList.add(permission)
            }
        }
        return deniedList
    }

    /**
     * 在权限组中检查是否有某个权限是否被永久拒绝
     *
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
    
    fun getBestPermissionSettingIntent( context: Context,  permissions: List<IPermission>?, skipRequest: Boolean): MutableList<Intent> {
        // 如果失败的权限里面不包含特殊权限
        if (permissions == null || permissions.isEmpty()) {
            return getCommonPermissionSettingIntent(context)
        }

        // 创建一个新的集合对象，避免复用对象可能引发外层的冲突
        val realPermissions: MutableList<IPermission> = ArrayList(permissions)
        for (permission in permissions) {
            if (permission.getFromAndroidVersion(context) > getCurrentVersion()) {
                // 如果当前权限是高版本才出现的权限，则进行剔除
                realPermissions.remove(permission)
                continue
            }

            val oldPermissions = permission.getOldPermissions(context)
            // 1. 如果旧版本列表不为空，并且当前权限是需要通过 startActivityForResult 授权的权限，就剔除它对应的旧版本权限
            // 例如：MANAGE_EXTERNAL_STORAGE -> READ_EXTERNAL_STORAGE、WRITE_EXTERNAL_STORAGE
            // 2. 如果旧版本列表不为空，并且当前权限对应的旧版本权限包含了需要通过 startActivityForResult 授权的权限，就剔除它对应的旧版本权限
            // 例如：POST_NOTIFICATIONS -> NOTIFICATION_SERVICE
            if (oldPermissions != null && !oldPermissions.isEmpty() &&
                (permission.getPermissionChannel(context) === PermissionChannel.START_ACTIVITY_FOR_RESULT ||
                        containsPermissionByStartActivityForResult(context, oldPermissions))
            ) {
                realPermissions.removeAll(oldPermissions)
            }
        }

        if (realPermissions.isEmpty()) {
            return getCommonPermissionSettingIntent(context)
        }

        if (realPermissions.size == 1) {
            return realPermissions[0].getPermissionSettingIntents(context, skipRequest)
        }

        var prePermissionIntentList: List<Intent> = realPermissions[0].getPermissionSettingIntents(context, skipRequest)
        for (i in 1..<realPermissions.size) {
            val currentPermissionIntentList: MutableList<Intent> = realPermissions[i].getPermissionSettingIntents(context, skipRequest)
            // 对比这两个 Intent 列表的内容是否一致
            if (!equalsIntentList(currentPermissionIntentList, prePermissionIntentList)) {
                // 如果不一致，就结束循环
                break
            }
            // 当前权限列表在下次循环就是上一个了，记录一下，可以避免重复获取，节省代码性能
            prePermissionIntentList = currentPermissionIntentList

            // 如果集合中的 Intent 列表都一样，就直接按照当前的 Intent 列表去做跳转
            if (i == realPermissions.size - 1) {
                return currentPermissionIntentList
            }
        }
        return getCommonPermissionSettingIntent(context)
    }

    /**
     * 根据新权限添加旧权限
     */
    @Synchronized
    fun addOldPermissionsByNewPermissions( context: Context,  requestList: MutableList<IPermission>) {
        // 这里需要将 index 设置成 -1，这样走到下面循环的时候，++i 第一次循环 index 就是 0 了
        var index = -1
        // ++index 是前置递增（先将 index 的值加 1，再返回增加后的值）
        // index++ 是后置递增（先返回 i 的当前值，再将 i 的值加 1）
        while (++index < requestList.size) {
            val permission = requestList[index]
            // 如果当前运行的 Android 版本大于权限出现的 Android 版本，则证明这个权限在当前设备上不用添加旧权限
            if (getCurrentVersion() >= permission.getFromAndroidVersion(context)) {
                continue
            }
            // 通过新权限查询到对应的旧权限
            val oldPermissions = permission.getOldPermissions(context)
            if (oldPermissions == null || oldPermissions.isEmpty()) {
                continue
            }
            for (oldPermission in oldPermissions) {
                // 如果请求列表已经包含此权限，就不重复添加，直接跳过
                if (containsPermission(requestList, oldPermission)) {
                    continue
                }
                // index + 1 是将旧版本的权限添加到新版本的权限后面，这样才能确保不打乱申请的传入顺序
                requestList.add(++index, oldPermission)
            }
        }
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