package com.knight.kotlin.module_home.proxy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dalvik.system.DexClassLoader


/**
 * @author created by luguian
 * @organize
 * @Date 2025/6/23 15:38
 * @descript:代理activity
 */
class ProxyActivity : AppCompatActivity() {

    private var pluginFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apkPath = intent.getStringExtra("apkPath")!!
        val fragmentClassName = intent.getStringExtra("className")!!

        // dex 优化目录
        val dexDir = filesDir.resolve("dex").apply { if (!exists()) mkdirs() }
        val dexClassLoader = DexClassLoader(apkPath, dexDir.absolutePath, null, classLoader)

        val clazz = dexClassLoader.loadClass(fragmentClassName)
        pluginFragment = clazz.getDeclaredConstructor().newInstance() as Fragment

        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, pluginFragment!!)
            .commit()
    }
}