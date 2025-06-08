package com.knight.kotlin.library_permiss

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