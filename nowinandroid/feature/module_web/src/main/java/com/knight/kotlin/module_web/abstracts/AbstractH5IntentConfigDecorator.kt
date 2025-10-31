package com.knight.kotlin.module_web.abstracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.knight.kotlin.library_base.entity.WebDataEntity
import com.peakmain.webview.interfaces.H5IntentConfig
import com.peakmain.webview.manager.H5UtilsParams

/**
 * author ：knight
 * createTime：2025/10/30
 * mail:15015706912@163.com
 * describe：H5启动装饰器类
 */
abstract class AbstractH5IntentConfigDecorator(private val decoratorConfig: H5IntentConfig) :
    H5IntentConfig {
    val params = H5UtilsParams.instance
    override fun startActivity(context: Context?, url: String) {
        decoratorConfig.startActivity(context, url)
    }


    override fun startActivity(context: Context?, bean: WebDataEntity) {
        decoratorConfig.startActivity(context, bean)
    }

    override fun startActivityForResult(
        context: Activity?,
        bean: WebDataEntity,
        requestCode: Int
    ) {
        decoratorConfig.startActivityForResult(context, bean, requestCode)
    }


    override fun startActivityForResult(
        context: Activity?,
        url: String,
        requestCode: Int
    ) {
        decoratorConfig.startActivityForResult(context, url, requestCode)
    }


    override fun startActivityForResult(
        context: Fragment?,
        bean: WebDataEntity,
        requestCode: Int
    ) {
        decoratorConfig.startActivityForResult(context, bean, requestCode)
    }



    override fun startActivityForResult(
        context: Fragment?,
        url: String,
        requestCode: Int
    ) {
        decoratorConfig.startActivityForResult(context, url, requestCode)
    }

    override fun startActivityForResult(
        context: Fragment?,
        launcher: ActivityResultLauncher<Intent>?,
        bean: WebDataEntity
    ) {
        decoratorConfig.startActivityForResult(context, launcher, bean)
    }


    override fun startActivityForResult(
        context: Fragment?,
        launcher: ActivityResultLauncher<Intent>?,
        url: String
    ) {
        decoratorConfig.startActivityForResult(context, launcher, url)
    }



    override fun startActivityForResult(
        context: FragmentActivity?,
        launcher: ActivityResultLauncher<Intent>?,
        bean: WebDataEntity
    ) {
        decoratorConfig.startActivityForResult(context, launcher, bean)
    }



    override fun startActivityForResult(
        context: FragmentActivity?,
        launcher: ActivityResultLauncher<Intent>?,
        url: String
    ) {
        decoratorConfig.startActivityForResult(context, launcher, url)
    }




    override fun startActivity(context: Context?, intent: Intent) {
        decoratorConfig.startActivity(context,intent)
    }

    override fun startActivityForResult(context: Context?, intent: Intent, requestCode: Int) {
        decoratorConfig.startActivityForResult(context,intent,requestCode)
    }
}