package com.knight.kotlin.library_permiss.delegate

import android.app.Activity
import android.content.Context
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.AndroidVersion
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.utils.PermissionUtils
import com.knight.kotlin.library_permiss.utils.PermissionUtils.checkSelfPermission
import com.knight.kotlin.library_permiss.utils.PermissionUtils.equalsPermission


/**
 * Author:Knight
 * Time:2023/8/30 15:58
 * Description:PermissionDelegateImplV28
 */
@RequiresApi(api = AndroidVersion.ANDROID_9)
open class PermissionDelegateImplV28 :PermissionDelegateImplV26() {

     override fun isGrantedPermission(
       context: Context,
       permission: String
    ): Boolean {
        return if (equalsPermission(
                permission, Permission.ACCEPT_HANDOVER
            )
        ) {
            checkSelfPermission(context, permission)
        } else super.isGrantedPermission(context, permission)
    }

    override fun isDoNotAskAgainPermission(
        activity: Activity,
        permission: String
    ): Boolean {
        if (PermissionUtils.equalsPermission(permission, Permission.ACCEPT_HANDOVER)) {
            return !PermissionUtils.checkSelfPermission(activity, permission) &&
                    !PermissionUtils.shouldShowRequestPermissionRationale(activity, permission);
        }
        return super.isDoNotAskAgainPermission(activity, permission);
    }
}