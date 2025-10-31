package com.peakmain.webview.implement

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.knight.kotlin.library_base.config.WebViewConstants
import com.knight.kotlin.library_base.entity.WebDataEntity
import com.knight.kotlin.module_web.activity.WebViewActivity
import com.peakmain.webview.interfaces.H5IntentConfig

/**
 * author ：knight
 * createTime：2023/04/10
 * mail:15015706912@163.com
 * describe：
 */
class DefaultH5IntentConfigImpl : H5IntentConfig {
    override fun startActivity(context: Context?, url: String) {
        context?.startActivity(
            Intent(context, WebViewActivity::class.java)
                .putExtra(WebViewConstants.WEB_URL, url)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    override fun startActivity(context: Context?, bean: WebDataEntity) {
        context?.startActivity(
            Intent(context, WebViewActivity::class.java)
                .putExtra(WebViewConstants.WEB_PARAMS, bean)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    override fun startActivity(context: Context?, intent: Intent) {
        context?.startActivity(intent)
    }


    override fun startActivityForResult(
        context: Activity?,
        bean: WebDataEntity,
        requestCode: Int,
    ) {
        val intent = Intent(context, WebViewActivity::class.java)
            .putExtra(WebViewConstants.WEB_PARAMS, bean)
        context?.startActivityForResult(intent, requestCode)
    }

    override fun startActivityForResult(
        context: Fragment?,
        bean: WebDataEntity,
        requestCode: Int,
    ) {
        if (context == null || context.context == null) return
        val intent = Intent(context.context, WebViewActivity::class.java)
            .putExtra(WebViewConstants.WEB_PARAMS, bean)
        context.startActivityForResult(intent, requestCode)
    }

    override fun startActivityForResult(context: Activity?, url: String, requestCode: Int) {

    }

    override fun startActivityForResult(context: Fragment?, url: String, requestCode: Int) {

    }


    override fun startActivityForResult(
        context: FragmentActivity?,
        launcher: ActivityResultLauncher<Intent>?,
        bean: WebDataEntity,
    ) {
        val intent = Intent(context, WebViewActivity::class.java)
            .putExtra(WebViewConstants.WEB_PARAMS, bean)
        launcher?.launch(intent)
    }

    override fun startActivityForResult(
        context: Fragment?,
        launcher: ActivityResultLauncher<Intent>?,
        bean: WebDataEntity,
    ) {
        if (context == null || context.context == null) return
        val intent = Intent(context.context, WebViewActivity::class.java)
            .putExtra(WebViewConstants.WEB_PARAMS, bean)
        launcher?.launch(intent)
    }

    override fun startActivityForResult(
        context: FragmentActivity?,
        launcher: ActivityResultLauncher<Intent>?,
        url: String
    ) {

    }

    override fun startActivityForResult(
        context: Fragment?,
        launcher: ActivityResultLauncher<Intent>?,
        url: String
    ) {

    }

    override fun startActivityForResult(context: Context?, intent: Intent, requestCode: Int) {
        if(context==null)return
        if (context is FragmentActivity) {
            context.startActivityForResult(intent, requestCode)
        } else if(context is Fragment){
            context.startActivityForResult(intent, requestCode)
        }
    }
}