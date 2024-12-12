package com.knight.kotlin.library_network.header

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Build
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.config.Appconfig.SESSION_ID
import com.knight.kotlin.library_base.config.Appconfig.X_API_KEY
import com.knight.kotlin.library_base.ktx.networkName
import com.knight.kotlin.library_base.ktx.screenHeight
import com.knight.kotlin.library_base.ktx.screenWidth
import com.knight.kotlin.library_network.bean.AuthToken
import java.util.Locale
import java.util.UUID


/**
 * @author created by luguian
 * @organize
 * @Date 2024/12/9 17:52
 * @descript:
 */
val Context.headerSp: SharedPreferences
    get() = this.getSharedPreferences("header_sp", MODE_PRIVATE)
class HeaderStorage (mContext: Context) {

    private val context:Context
    init {
        context = mContext
    }
    var udId = ""
        get() {
            val cid = field.takeIf { it.isNotEmpty() } ?: context.headerSp.getString(
                UDID, null
            ) ?: UUID.randomUUID().toString().replace("-", "").also {
                context.headerSp.edit().putString(UDID, it).apply()
            }
            return cid.also { field = it }
        }

    var deviceId = "default"
        get() {
            val auth =
                field.takeIf { it != "default" } ?: context.headerSp.getString(
                    X_THEFAIR_CID, ""
                ) ?: ""
            return auth.also { field = it }
        }

    private var auth = "default"
        get() {
            val auth =
                field.takeIf { it != "default" } ?: context.headerSp.getString(
                    X_THEFAIR_AUTH, ""
                ) ?: ""
            return auth.also { field = it }
        }

    var refreshToken = "default"
        get() {
            val auth =
                field.takeIf { it != "default" } ?: context.headerSp.getString(
                    REFRESH_TOKEN, ""
                ) ?: ""
            return auth.also { field = it }
        }

    private val cookie: String
        get() {
            return listOf(
                "ky_udid=$udId",
                "ky_auth=",
                "APPID=${Appconfig.APP_ID}",
                "PHPSESSID=$SESSION_ID"
            ).joinToString(";")
        }

    val ua: String
        get() {
            return "EYEPETIZER/${Appconfig.VERSION_CODE} (${Build.MODEL};android;${Build.VERSION.RELEASE};" +
                    "${Locale.getDefault()};android;${Appconfig.VERSION_NAME};cn-bj;pp;${deviceId};${context.networkName};" +
                    "${context.screenWidth}*${context.screenHeight}) native/1.0"
        }

    val header: Map<String, String>
        get() {
            return mutableMapOf(
                "X-THEFAIR-CID" to deviceId,
                "X-THEFAIR-AUTH" to auth,
                "X-THEFAIR-APPID" to Appconfig.APP_ID,
                "x-api-key" to X_API_KEY,
                "X-THEFAIR-UA" to ua,
                "User-Agent" to ua,
                "Cookie" to cookie
            )
        }

    val screen: String
        get() = "${context.screenWidth}*${context.screenHeight}"

    fun updateHeader(authToken: AuthToken) {
        context.headerSp.edit()
            .putString(X_THEFAIR_AUTH, authToken.access_token)
            .putString(X_THEFAIR_CID, authToken.device_id)
            .putString(REFRESH_TOKEN, authToken.refresh_token)
            .apply()

        auth = authToken.access_token
        deviceId = authToken.device_id
        refreshToken = authToken.refresh_token
    }

    companion object {
        private const val X_THEFAIR_CID = "x_thefair_cid"
        private const val X_THEFAIR_AUTH = "x_thefair_auth"
        private const val REFRESH_TOKEN = "refresh_token"
        private const val UDID = "udid"
    }


}