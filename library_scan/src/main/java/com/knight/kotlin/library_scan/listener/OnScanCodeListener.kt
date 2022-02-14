package com.knight.kotlin.library_scan.listener

/**
 * Author:Knight
 * Time:2022/2/14 13:51
 * Description:OnScanCodeListener
 */
open interface OnScanCodeListener {
    fun onCodeResult(code: String?)
}