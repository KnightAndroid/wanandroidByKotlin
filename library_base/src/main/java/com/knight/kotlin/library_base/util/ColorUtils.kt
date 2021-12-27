package com.knight.kotlin.library_base.util

import android.R
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.annotation.NonNull
import java.util.*


/**
 * Author:Knight
 * Time:2021/12/21 16:06
 * Description:ColorUtils
 * 颜色工具类
 */
object ColorUtils {

    /**
     *
     * 随机生成颜色
     * @return
     */
    fun getRandColorCode(): Int {
        val random = Random()
        return 0xff000000.toInt().toChar().code or random.nextInt(0x00ffffff.toInt().toChar().code)
    }

    /**
     *
     * 返回十六进制
     * @return
     */
    fun getRandStringColorCode(): String? {
        var r: String
        var g: String
        var b: String
        val random = Random()
        r = Integer.toHexString(random.nextInt(256)).uppercase(Locale.getDefault())
        g = Integer.toHexString(random.nextInt(256)).uppercase(Locale.getDefault())
        b = Integer.toHexString(random.nextInt(256)).uppercase(Locale.getDefault())
        r = if (r.length == 1) "0$r" else r
        g = if (g.length == 1) "0$g" else g
        b = if (b.length == 1) "0$b" else b
        return r + g + b
    }

    /**
     *
     * 返回colorList
     * @param context
     * @return
     */
    fun getOneColorStateList(context: Context?): ColorStateList? {
        val colors = intArrayOf(Color.parseColor("#55aff4"))
        val states = arrayOf(IntArray(0))
        return ColorStateList(states, colors)
    }

    /**
     *
     * 颜色透明度
     * @param color
     * @param alpha
     * @return
     */
    fun alphaColor(color: Int, alpha: Float): Int {
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb((alpha * 255).toInt(), red, green, blue)
    }

    /**
     *
     * 16进制 转RGB(String)类型颜色(无#号)
     * @param color
     * @return
     */
    fun convertToRGB(color: Int): String? {
        var red = Integer.toHexString(Color.red(color))
        var green = Integer.toHexString(Color.green(color))
        var blue = Integer.toHexString(Color.blue(color))
        if (red.length == 1) {
            red = "0$red"
        }
        if (green.length == 1) {
            green = "0$green"
        }
        if (blue.length == 1) {
            blue = "0$blue"
        }
        return red + green + blue
    }


    /**
     * ARGB(含RGB)颜色 转 16进制颜色
     *
     * @param argb ARGB(含RGB)颜色
     * @return 16进制颜色
     * @throws NumberFormatException 当{@param argb}不是一个正确的颜色格式的字符串时
     */
    fun convertToColorInt(@NonNull argb: String): Int {
        return if (argb.matches(Regex("[0-9a-fA-F]{1,6}"))) {
            when (argb.length) {
                1 -> Color.parseColor("#00000$argb")
                2 -> Color.parseColor("#0000$argb")
                3 -> {
                    val r = argb[0]
                    val g = argb[1]
                    val b = argb[2]
                    Color.parseColor(
                        StringBuilder("#")
                            .append(r).append(r)
                            .append(g).append(g)
                            .append(b).append(b)
                            .toString()
                    )
                }
                4 -> Color.parseColor("#00$argb")
                5 -> Color.parseColor("#0$argb")
                6 -> Color.parseColor("#$argb")
                else -> 0
            }
        } else {
            0
        }
    }

    /**
     *
     * 动态设置按钮选择颜色
     * @param checked 选择
     * @param normal 默认
     * @return
     */
    fun createColorStateList(checked: Int, normal: Int): ColorStateList? {
        val colors = intArrayOf(checked, normal)
        val states = arrayOfNulls<IntArray>(2)
        states[0] = intArrayOf(R.attr.state_checked, R.attr.state_enabled)
        states[1] = intArrayOf(R.attr.state_enabled)
        return ColorStateList(states, colors)
    }


    /**
     * 过滤蓝光
     *
     * @param blueFilterPercent 蓝光过滤比例[10-30-80]
     */
    fun getFilterColor(blueFilterPercent: Int): Int {
        var realFilter = blueFilterPercent
        if (realFilter < 10) {
            realFilter = 10
        } else if (realFilter > 80) {
            realFilter = 80
        }
        val a = (realFilter / 80f * 180).toInt()
        val r = (200 - realFilter / 80f * 190).toInt()
        val g = (180 - realFilter / 80f * 170).toInt()
        val b = (60 - realFilter / 80f * 60).toInt()
        return Color.argb(a, r, g, b)
    }

}