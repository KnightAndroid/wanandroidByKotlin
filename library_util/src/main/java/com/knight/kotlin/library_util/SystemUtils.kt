package com.knight.kotlin.library_util

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat


/**
 * Author:Knight
 * Time:2022/1/10 16:08
 * Description:SystemUtils
 */
object SystemUtils {


    /**
     * 返回当前APP版本号
     */
    fun getAppVersionCode(context: Context): Long {
        var versioncode: Long = 0
        try {
            val pm: PackageManager = context.getPackageManager()
            val pi: PackageInfo = pm.getPackageInfo(context.getPackageName(), 0)
            versioncode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                pi.longVersionCode
            } else {
                pi.versionCode.toLong()
            }
        } catch (e: Exception) {
            Log.e("VersionInfo", "Exception", e)
        }
        return versioncode
    }


    /**
     * 返回应用程序名称
     */
    fun getAppName(context:Context) :String {
        val pm:PackageManager = context.packageManager;
        val pi: PackageInfo = pm.getPackageInfo(context.packageName, 0)
        return context.resources.getString(pi.applicationInfo.labelRes)
    }

    /**
     * 返回当前程序版本名
     */
    fun getAppVersionName(context: Context): String? {
        var versionName: String? = null
        try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(context.packageName, 0)
            versionName = pi.versionName
        } catch (e: java.lang.Exception) {
            Log.e("VersionInfo", "Exception", e)
        }
        return versionName
    }

    /**
     *
     * 复制文本到剪切板
     */
    fun copyContent(context: Context, copyText: String?) {
        //获取剪贴板管理器：
        val cm: ClipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // 创建普通字符型ClipData
        val mClipData = ClipData.newPlainText("Label", copyText)
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData)
    }

    /**
     *
     * 设置颜色
     * @param editText
     * @param color
     */
    @SuppressLint("SoonBlockedPrivateApi")
    fun setCursorDrawableColor(editText: EditText, color: Int) {
        try {
            val cursorDrawableResField = TextView::class.java.getDeclaredField("mCursorDrawableRes")
            cursorDrawableResField.isAccessible = true
            val cursorDrawableRes = cursorDrawableResField.getInt(editText)
            val editorField = TextView::class.java.getDeclaredField("mEditor")
            editorField.isAccessible = true
            val editor = editorField[editText]
            val clazz: Class<*> = editor.javaClass
            val res = editText.context.resources
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val drawableForCursorField = clazz.getDeclaredField("mDrawableForCursor")
                drawableForCursorField.isAccessible = true
                val drawable = res.getDrawable(cursorDrawableRes, editText.context.theme)
                drawable.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
                drawableForCursorField[editor] = drawable
            } else {
                val cursorDrawableField = clazz.getDeclaredField("mCursorDrawable")
                cursorDrawableField.isAccessible = true
                val drawables = arrayOfNulls<Drawable>(2)
                drawables[0] = ContextCompat.getDrawable(editText.context, cursorDrawableRes)
                drawables[1] = ContextCompat.getDrawable(editText.context, cursorDrawableRes)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    drawables[0]!!.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_IN)
                    drawables[1]!!.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_IN)
                } else {
                    drawables[0]!!.setColorFilter(color, PorterDuff.Mode.SRC_IN)
                    drawables[1]!!.setColorFilter(color, PorterDuff.Mode.SRC_IN)
                }
                cursorDrawableField[editor] = drawables
            }
        } catch (t: Throwable) {
        }
    }

    /**
     *
     * 延迟显示软键盘
     * @param view
     */
    fun showDelaySoftKeyBoard(view: View) {
        Handler(Looper.getMainLooper()).postDelayed({
            showSoftKeyBoard(
                view
            )
        }, 500)
    }


    /**
     *
     * 显示软键盘
     * @param view
     */
    fun showSoftKeyBoard(view: View) {
        try {
            view.isFocusable = true
            view.isFocusableInTouchMode = true
            view.requestFocus()
            val imm =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.RESULT_UNCHANGED_SHOWN)
            imm.toggleSoftInput(
                InputMethodManager.HIDE_IMPLICIT_ONLY,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    /**
     *
     * 监听输入框
     * @param et
     * @param tv
     */
    fun editTextChangeListener(et: EditText, tv: TextView) {
        et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                if (s.toString() != "") {
                    tv.text = "搜索"
                } else {
                    tv.text = "取消"
                }
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.toString() != "") {
                    tv.text = "搜索"
                } else {
                    tv.text = "取消"
                }
            }
        })
    }


    /**
     *
     * 重启应用
     * @param context
     */
    fun restartApp(context: Context) {
        val packageManager = context.packageManager ?: return
        val intent = packageManager.getLaunchIntentForPackage(context.packageName)
        if (intent != null) {
            intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        or Intent.FLAG_ACTIVITY_CLEAR_TASK
            )
            context.startActivity(intent)
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }







}