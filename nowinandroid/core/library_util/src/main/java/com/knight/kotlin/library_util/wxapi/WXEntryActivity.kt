package com.knight.kotlin.library_util.wxapi


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_util.R
import com.knight.kotlin.library_util.toast
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory


/**
 * @author created by luguian
 * @organize 
 * @Date 2025/5/6 16:45
 * @descript:微信回调页面
 */
class WXEntryActivity : AppCompatActivity(), IWXAPIEventHandler {
    private lateinit var api: IWXAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = WXAPIFactory.createWXAPI(this, com.knight.kotlin.library_base.config.Appconfig.WX_APP_ID, false)
        api.handleIntent(getIntent(), this)

    }

    override fun onResp(resp: BaseResp) {

        var result = 0
        when (resp.errCode) {
            BaseResp.ErrCode.ERR_OK -> result = R.string.util_errcode_success
            BaseResp.ErrCode.ERR_USER_CANCEL -> result = R.string.util_errcode_cancel
            BaseResp.ErrCode.ERR_AUTH_DENIED -> result = R.string.util_errcode_deny
            BaseResp.ErrCode.ERR_UNSUPPORT -> result = R.string.util_errcode_unsupported
            else -> result = R.string.util_errcode_unknown
        }
        toast(getString(result))
        // 必须 finish 否则会停留在 WXEntryActivity 导致白屏
        finish()
    }

    override fun onReq(req: BaseReq?) {
        finish() // 防止分享回调时异常停留
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        api.handleIntent(intent, this)
    }
}