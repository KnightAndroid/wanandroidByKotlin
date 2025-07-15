package com.knight.kotlin.library_base.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File


/**
 * Author:Knight
 * Time:2022/1/12 14:28
 * Description:AppUtils
 */
object AppUtils {

    /**
     *
     * 安装apk
     * @param apkFile
     * @param context
     */
    fun installApk(apkFile: File?, context: Context) {
        val intent = Intent()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.action = Intent.ACTION_VIEW
            val fileUri: Uri? = apkFile?.let {
                FileProvider.getUriForFile(
                    context,
                    context.getApplicationContext().getPackageName().toString() + ".fileprovider",
                    it
                )
            }
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(fileUri,
                fileUri?.let { context.getContentResolver().getType(it) })
            context.startActivity(intent)
        } else {
            val uri: Uri = Uri.fromFile(apkFile)
            intent.action = Intent.ACTION_VIEW
            // 指定数据和类型
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}