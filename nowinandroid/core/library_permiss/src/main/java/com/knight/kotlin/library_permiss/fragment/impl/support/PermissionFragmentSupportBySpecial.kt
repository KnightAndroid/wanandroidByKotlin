package com.knight.kotlin.library_permiss.fragment.impl.support

import com.knight.kotlin.library_permiss.core.RequestPermissionDelegateImpl
import com.knight.kotlin.library_permiss.core.RequestPermissionDelegateImplBySpecial

/**
 * @Description
 * @Author knight
 * @Time 2025/6/8 19:36
 *
 */

class PermissionFragmentSupportBySpecial : PermissionFragmentSupport() {

    private val mRequestPermissionDelegateImpl: RequestPermissionDelegateImpl =
        RequestPermissionDelegateImplBySpecial(
            this
        )

    /** @noinspection ClassEscapesDefinedScope
     */

    override fun getRequestPermissionDelegateImpl(): RequestPermissionDelegateImpl {
        return mRequestPermissionDelegateImpl
    }
}