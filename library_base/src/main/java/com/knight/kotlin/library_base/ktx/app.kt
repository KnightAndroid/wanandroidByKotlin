package com.knight.kotlin.library_base.ktx

import android.content.Context
import android.content.pm.PackageManager.NameNotFoundException
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build


/**
 * @author created by luguian
 * @organize
 * @Date 2024/12/9 17:25
 * @descript:
 */
val Context.versionName: String
    get() {
        return try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            packageInfo.versionName ?: ""
        } catch (e: NameNotFoundException) {
            ""
        }
    }


val Context.versionCode: Long
    get() {
        return try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else {
                packageInfo.versionCode.toLong()
            }
        } catch (e: NameNotFoundException) {
            0
        }
    }


val Context.isDark: Boolean
    get() {
        val uiMode = resources.configuration.uiMode
        return uiMode.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    }


val Context.networkName: String
    get() {
        val connectMgr by lazy {
            getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val nc =
                connectMgr?.getNetworkCapabilities(connectMgr?.activeNetwork) ?: return "NONE"
            return when {
                nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WIFI"
                nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "MOBILE"
                else -> "NONE"
            }
        } else {

            return when (connectMgr?.activeNetworkInfo?.type) {
                ConnectivityManager.TYPE_MOBILE -> "MOBILE"
                ConnectivityManager.TYPE_WIFI -> "WIFI"
                else -> "NONE"
            }

        }

    }



