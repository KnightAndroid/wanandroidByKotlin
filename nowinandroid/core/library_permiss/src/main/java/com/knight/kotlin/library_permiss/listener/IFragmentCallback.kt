package com.knight.kotlin.library_permiss.listener

import android.content.Intent



/**
 * @Description
 * @Author knight
 * @Time 2025/6/8 16:56
 *
 */
interface IFragmentCallback {
    /** Fragment 可见时回调  */
    fun onFragmentResume()

    /** Fragment 解绑时回调  */
    fun onFragmentDestroy()

    /** Fragment onRequestPermissionsResult 方法回调  */
    fun onFragmentRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
    }

    /** Fragment onActivityResult 方法回调  */
    fun onFragmentActivityResult(requestCode: Int, resultCode: Int,  data: Intent?) {}
}