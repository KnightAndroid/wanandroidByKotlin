package com.knight.kotlin.library_permiss

/**
 * @Description
 * @Author knight
 * @Time 2025/6/8 19:36
 *
 */

class PermissionFragmentSupportByDangerous : PermissionFragmentSupport() {

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