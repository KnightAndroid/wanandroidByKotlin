package com.knight.kotlin.library_permiss.permission.common

import android.app.Activity
import android.content.Context
import android.os.Parcel
import com.knight.kotlin.library_permiss.permission.base.BasePermission
import com.knight.kotlin.library_permiss.tools.PermissionVersion
import com.knight.kotlin.library_permiss.tools.PhoneRomUtils


/**
 * @Description 特殊权限的基类
 * @Author knight
 * @Time 2025/7/10 20:55
 * 
 */

abstract class SpecialPermission : BasePermission {

    constructor() : super()

    constructor(inParcel: Parcel) : super(inParcel)


    
    override fun getPermissionChannel(context: Context): PermissionChannel {
        return PermissionChannel.START_ACTIVITY_FOR_RESULT
    }


    
    override fun getPermissionPageType( context: Context): PermissionPageType {
        return PermissionPageType.OPAQUE_ACTIVITY
    }

    override fun isDoNotAskAgainPermission( activity: Activity): Boolean {
        return false
    }

    override fun getResultWaitTime( context: Context): Int {
        if (!isSupportRequestPermission(context)) {
            return 0
        }

        // 特殊权限一律需要一定的等待时间
        var waitTime = if (PermissionVersion.isAndroid11()) {
            200
        } else {
            300
        }

        if (PhoneRomUtils.isEmui() || PhoneRomUtils.isHarmonyOs()) {
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
        // 特殊权限默认不需要在清单文件中注册
        return false
    }
}
