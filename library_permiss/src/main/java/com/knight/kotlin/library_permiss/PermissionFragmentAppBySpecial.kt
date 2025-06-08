package com.knight.kotlin.library_permiss

/**
 * @Description
 * @Author knight
 * @Time 2025/6/8 19:28
 *
 */

class PermissionFragmentAppBySpecial : PermissionFragmentApp() {

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