package com.knight.kotlin.library_permiss.permission.common

import android.app.Activity
import android.content.Context
import android.os.Parcel
import com.knight.kotlin.library_permiss.permission.base.BasePermission
import com.knight.kotlin.library_permiss.utils.PhoneRomUtils.isEmui
import com.knight.kotlin.library_permiss.utils.PhoneRomUtils.isHarmonyOs


/**
 * @Description
 * @Author knight
 * @Time 2025/7/10 20:55
 * 
 */

abstract class SpecialPermission : BasePermission {
    protected constructor() : super()

    protected constructor(`in`: Parcel?) : super(`in`)

    
    override fun getPermissionType(): PermissionType {
        return PermissionType.SPECIAL
    }

    override fun isDoNotAskAgainPermission( activity: Activity?): Boolean {
        return false
    }

    override fun getResultWaitTime( context: Context?): Int {
        if (!isSupportRequestPermission(context)) {
            return 0
        }

        // 特殊权限一律需要一定的等待时间
        var waitTime: Int
        waitTime = if (PermissionVersion.isAndroid11()) {
            200
        } else {
            300
        }

        if (isEmui() || isHarmonyOs()) {
            // 需要加长时间等待，不然某些华为机型授权了但是获取不到权限
            waitTime = if (PermissionVersion.isAndroid8()) {
                300
            } else {
                500
            }
        }
        return waitTime
    }

    /**
     * 当前权限是否强制在清单文件中静态注册
     */
    override fun isRegisterPermissionByManifestFile(): Boolean {
        // 特殊权限默认不需要在清单文件中注册，这样定义是为了避免外层在自定义特殊权限的时候，还要去重写此方法
        return false
    }
}