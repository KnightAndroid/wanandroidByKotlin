package com.knight.kotlin.library_util

import android.graphics.Bitmap
import com.baidu.navisdk.vi.EnvDrawText
import com.knight.kotlin.library_base.BaseApp
import com.knight.kotlin.library_util.bitmap.BitmapUtils
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXImageObject
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXTextObject
import com.tencent.mm.opensdk.openapi.WXAPIFactory


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/18 17:01
 * @descript:分享工具
 */
object ShareSdkUtils {

    private const val mTargetScene = SendMessageToWX.Req.WXSceneSession
    /**
     *
     * 分享文字到微信
      */
    fun shareWxText(title:String,desc:String) {
        //初始化一个 WXTextObject 对象，填写分享的文本内容
        val api = WXAPIFactory.createWXAPI(BaseApp.context, "wx3eca5a44fe87d60a",false)
        val textObj = WXTextObject()
        textObj.text = title

        //用 WXTextObject 对象初始化一个 WXMediaMessage 对象
        val msg = WXMediaMessage()
        msg.mediaObject = textObj
        msg.description = desc

        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("text")
        req.message = msg
        req.scene = mTargetScene

        //调用api接口，发送数据到微信
        api.sendReq(req)


     }

    /**
     *
     * 分享图片到微信
     */
    fun shareWxImg(bmp : Bitmap) {
        val api = WXAPIFactory.createWXAPI(BaseApp.context, "wx3eca5a44fe87d60a",false)
        //初始化 WXImageObject 和 WXMediaMessage 对象
        val imgObj = WXImageObject(bmp)
        val msg = WXMediaMessage()
        msg.mediaObject = imgObj


        //设置缩略图
        val thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true)
        EnvDrawText.bmp.recycle()
        msg.thumbData = BitmapUtils.bmpToByteArray(thumbBmp, true)
        //构造一个Req
        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("img")
        req.message = msg
        req.scene = mTargetScene
         //调用api接口，发送数据到微信
        api.sendReq(req)


    }




    private fun buildTransaction(type: String?): String {
        return if ((type == null)) System.currentTimeMillis().toString() else type + System.currentTimeMillis()
    }
}