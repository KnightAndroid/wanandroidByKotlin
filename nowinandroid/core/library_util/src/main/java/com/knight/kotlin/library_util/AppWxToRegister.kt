package com.knight.kotlin.library_util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tencent.mm.opensdk.openapi.WXAPIFactory


/**
 * @author created by luguian
 * @organize
 * @Date 2025/5/6 14:20
 * @descript:
 */
class AppWxToRegister: BroadcastReceiver() {
    override fun onReceive(context: Context?, p1: Intent?) {
        val api = WXAPIFactory.createWXAPI(context, null,false)

        // 将该app注册到微信
        api.registerApp("wx3eca5a44fe87d60a")
    }
}