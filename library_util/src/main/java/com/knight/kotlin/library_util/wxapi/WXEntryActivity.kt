package com.knight.kotlin.library_util.wxapi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory


/**
 * @author created by luguian
 * @organize 
 * @Date 2025/5/6 16:45
 * @descript:
 */
class WXEntryActivity : AppCompatActivity(), IWXAPIEventHandler {
    private lateinit var api: IWXAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = WXAPIFactory.createWXAPI(this, "wx3eca5a44fe87d60a", false)
        api.handleIntent(getIntent(), this)
        Log.d("WXEntryActivity", "onCreate called")
    }

    override fun onResp(resp: BaseResp) {
        Log.d("WXEntryActivity", "onResp called")
        // 分享成功、失败、取消的处理
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            // 成功
        }

        // 必须 finish 否则会停留在 WXEntryActivity 导致白屏
        finish()
    }

    override fun onReq(req: BaseReq?) {
        Log.d("WXEntryActivity", "onReq called")
        finish() // 防止分享回调时异常停留
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        setIntent(intent)
        api.handleIntent(intent, this)
    }
}