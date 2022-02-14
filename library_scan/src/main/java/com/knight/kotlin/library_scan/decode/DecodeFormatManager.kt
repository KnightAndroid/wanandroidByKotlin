package com.knight.kotlin.library_scan.decode

import com.google.zxing.BarcodeFormat
import java.util.EnumSet

/**
 * Author:Knight
 * Time:2022/2/14 11:10
 * Description:DecodeFormatManager
 */
object DecodeFormatManager {
    var PRODUCT_FORMATS = EnumSet.of(
        BarcodeFormat.UPC_A,
        BarcodeFormat.UPC_E,
        BarcodeFormat.EAN_13,
        BarcodeFormat.EAN_8,
        BarcodeFormat.RSS_14,
        BarcodeFormat.RSS_EXPANDED
    )
    var INDUSTRIAL_FORMATS: EnumSet<BarcodeFormat> = EnumSet.of(
        BarcodeFormat.CODE_39,
        BarcodeFormat.CODE_93,
        BarcodeFormat.CODE_128,
        BarcodeFormat.ITF,
        BarcodeFormat.CODABAR
    )
    val QR_CODE_FORMATS: Set<BarcodeFormat> = EnumSet.of(BarcodeFormat.QR_CODE)
    val DATA_MATRIX_FORMATS: Set<BarcodeFormat> = EnumSet.of(BarcodeFormat.DATA_MATRIX)
    var ONE_D_FORMATS: EnumSet<BarcodeFormat> = EnumSet.copyOf(PRODUCT_FORMATS)
    init {
        ONE_D_FORMATS.addAll(INDUSTRIAL_FORMATS)
    }

}