package com.knight.kotlin.library_scan.decode

import android.app.Activity
import android.graphics.ImageFormat
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.knight.kotlin.library_scan.entity.ScanCodeEntity
import com.knight.kotlin.library_scan.listener.OnScanCodeListener
import com.knight.kotlin.library_scan.utils.AudioUtil
import java.nio.ByteBuffer
import java.util.Hashtable
import java.util.Vector

/**
 * Author:Knight
 * Time:2022/2/14 11:24
 * Description:ScanCodeAnalyzer
 */
class ScanCodeAnalyzer(
    mActivity: Activity,
    scanCodeEntity: ScanCodeEntity,
    onScancodeListenner: OnScanCodeListener
) : ImageAnalysis.Analyzer {
    private var audioUtil: AudioUtil
    private var reader: MultiFormatReader
    private var scanCodeEntity: ScanCodeEntity = scanCodeEntity
    private var onScancodeListenner: OnScanCodeListener = onScancodeListenner

    init {
        this.onScancodeListenner = onScancodeListenner
        audioUtil = AudioUtil(mActivity, this.scanCodeEntity.getAudioId())
        reader = initReader()
    }

    private fun toByteArray(byteArray: ByteBuffer): ByteArray {
        byteArray.rewind()
        val data = ByteArray(byteArray.remaining())
        byteArray[data]
        return data
    }


    private fun initReader(): MultiFormatReader {
        val formatReader = MultiFormatReader()
        val hints: Hashtable<*, *> = Hashtable<Any?, Any?>()
        val decodeFormats: Vector<BarcodeFormat> = Vector<BarcodeFormat>()
        decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS as Collection<BarcodeFormat>)
        decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS as Collection<BarcodeFormat>)
        decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS as Collection<BarcodeFormat>)
        (hints as MutableMap<DecodeHintType,
                Vector<BarcodeFormat>>)[DecodeHintType.POSSIBLE_FORMATS] =
            decodeFormats
        (hints as MutableMap<DecodeHintType, String>)[DecodeHintType.CHARACTER_SET] = "UTF-8"
        formatReader.setHints(hints as Map<DecodeHintType, *>)
        return formatReader
    }




    override fun analyze(image: ImageProxy) {
        if (ImageFormat.YUV_420_888 != image.format) {
            image.close()
            return
        }
        //将buffer 数据写入数组
        val planeProxy = image.planes[0]
        val byteBuffer = planeProxy.buffer
        val data = this.toByteArray(byteBuffer)

        //图片宽高
        val height = image.height
        val width = image.width
        //图片旋转
        //图片旋转
        val rotationData = ByteArray(data.size)
        var j = 0
        var k = 0
        for (y in 0 until height) {
            for (x in 0 until width) {
                j = x * height + height - y - 1
                k = x + y * width
                rotationData[j] = data[k]
            }
        }
        val source =
            PlanarYUVLuminanceSource(rotationData, height, width, 0, 0, height, width, false)
        val bitmap = BinaryBitmap(HybridBinarizer(source))

        try {
            val result = reader.decode(bitmap)
            val scanCodeEntity = scanCodeEntity
            val isPlay = if (scanCodeEntity != null) scanCodeEntity.isPlayAudio() else null
            if (isPlay == true) {
                audioUtil.playBeepSoundAndVibrate()
            }
            val onScanCodeListener = onScancodeListenner
            onScanCodeListener.onCodeResult(result.text)
        } catch (e: Exception) {
            image.close()
        } finally {
            image.close()
        }
    }

    fun getScanCodeModel(): ScanCodeEntity {
        return scanCodeEntity
    }

    fun getOnScancodeListenner(): OnScanCodeListener {
        return onScancodeListenner
    }
}