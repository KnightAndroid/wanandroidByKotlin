package com.knight.kotlin.library_permiss.fragment.impl.app

import com.knight.kotlin.library_permiss.core.RequestPermissionDelegateImpl
import com.knight.kotlin.library_permiss.core.RequestPermissionDelegateImplByDangerous

/**
 * @Description
 * @Author knight
 * @Time 2025/6/8 19:27
 *
 */

class PermissionFragmentAppByDangerous : PermissionFragmentApp() {

    private val mRequestPermissionDelegateImpl: RequestPermissionDelegateImpl =
        RequestPermissionDelegateImplByDangerous(
            this
        )

    /** @noinspection ClassEscapesDefinedScope
     */

    override fun getRequestPermissionDelegateImpl(): RequestPermissionDelegateImpl {
        return mRequestPermissionDelegateImpl
    }
}