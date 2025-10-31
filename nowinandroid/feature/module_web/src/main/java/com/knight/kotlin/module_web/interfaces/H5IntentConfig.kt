package com.peakmain.webview.interfaces

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.knight.kotlin.library_base.entity.WebDataEntity

/**
 * author ：knight
 * createTime：2023/04/10
 * mail:15015706912@163.com
 * describe：
 */
interface H5IntentConfig {
    fun startActivity(context: Context?, url: String)
    fun startActivity(context: Context?, bean: WebDataEntity)
    fun startActivityForResult(context: Activity?, bean: WebDataEntity, requestCode: Int)
    fun startActivityForResult(context: Fragment?, bean: WebDataEntity, requestCode: Int)

    fun startActivityForResult(context: Activity?, url: String, requestCode: Int)
    fun startActivityForResult(context: Fragment?, url: String, requestCode: Int)





    fun startActivityForResult(
        context: FragmentActivity?,
        launcher: ActivityResultLauncher<Intent>?,
        bean: WebDataEntity,
    )

    fun startActivityForResult(
        context: Fragment?,
        launcher: ActivityResultLauncher<Intent>?,
        bean: WebDataEntity,
    )


    fun startActivityForResult(
        context: FragmentActivity?,
        launcher: ActivityResultLauncher<Intent>?,
        url: String,
    )

    fun startActivityForResult(
        context: Fragment?,
        launcher: ActivityResultLauncher<Intent>?,
        url: String,
    )



    fun startActivity(context: Context?, intent: Intent)
    fun startActivityForResult(context: Context?, intent: Intent, requestCode: Int)
}