package com.knight.kotlin.library_util

import cn.sharesdk.framework.Platform
import cn.sharesdk.framework.PlatformActionListener
import cn.sharesdk.onekeyshare.OnekeyShare
import com.mob.MobSDK


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/18 17:01
 * @descript:分享工具
 */
object ShareSdkUtils {

    /**
     *
     * 分享第三方app平台方法
      */
    fun sharePlatform() {
         val oks = OnekeyShare()
         // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
         oks.setTitle("标题")
         // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
         oks.setTitleUrl("http://sharesdk.cn")
         // text是分享文本，所有平台都需要这个字段
         oks.text = "我是分享文本"
         //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
         oks.setImageUrl("https://download.sdk.mob.com/web/images/2019/07/30/14/1564468183056/750_750_65.12.png")
         // url仅在微信（包括好友和朋友圈）中使用
         oks.setUrl("http://sharesdk.cn")
         //分享回调
         oks.callback = object : PlatformActionListener {
             override fun onComplete(platform: Platform, i: Int, hashMap: HashMap<String, Any>) {
                 // 分享成功回调
                 toast("分享成功")
             }

             override fun onError(platform: Platform, i: Int, throwable: Throwable) {
                 // 分享失败回调   platform:平台对象，i:表示当前的动作(9表示分享)，throwable:异常信息
             }

             override fun onCancel(platform: Platform, i: Int) {
                 // 分享取消回调
             }
         }
         // 启动分享
         oks.show(MobSDK.getContext())
     }
}