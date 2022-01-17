package com.knight.kotlin.library_util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log


/**
 * Author:Knight
 * Time:2022/1/10 16:08
 * Description:SystemUtils
 */
object SystemUtils {


    /**
     * 返回当前APP版本号
     */
    fun getAppVersionCode(context: Context): Long {
        var versioncode: Long = 0
        try {
            val pm: PackageManager = context.getPackageManager()
            val pi: PackageInfo = pm.getPackageInfo(context.getPackageName(), 0)
            versioncode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                pi.longVersionCode
            } else {
                pi.versionCode.toLong()
            }
        } catch (e: Exception) {
            Log.e("VersionInfo", "Exception", e)
        }
        return versioncode
    }

    /**
     * 返回当前程序版本名
     */
    fun getAppVersionName(context: Context): String? {
        var versionName: String? = null
        try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(context.packageName, 0)
            versionName = pi.versionName
        } catch (e: java.lang.Exception) {
            Log.e("VersionInfo", "Exception", e)
        }
        return versionName
    }

    /**
     *
     * 复制文本到剪切板
     */
    fun copyContent(context: Context, copyText: String?) {
        //获取剪贴板管理器：
        val cm: ClipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // 创建普通字符型ClipData
        val mClipData = ClipData.newPlainText("Label", copyText)
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData)
    }





}