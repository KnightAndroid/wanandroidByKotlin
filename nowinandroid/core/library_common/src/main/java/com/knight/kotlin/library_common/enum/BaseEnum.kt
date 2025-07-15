package com.knight.kotlin.library_common.enum

import android.content.Context


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/26 11:16
 * @descript:
 */
interface BaseEnum {
    val id: String
    val nameArrayId: Int
    val valueArrayId: Int
    fun getName(context: Context): String
}

interface VoiceEnum : BaseEnum {
    val voiceArrayId: Int
    fun getVoice(context: Context): String
}

interface UnitEnum<T : Number> : VoiceEnum {
    val convertUnit: (T) -> Double
    fun getValueWithoutUnit(valueInDefaultUnit: T): T
    fun getValueTextWithoutUnit(valueInDefaultUnit: T): String
    fun getValueText(context: Context, valueInDefaultUnit: T): String
    fun getValueText(context: Context, valueInDefaultUnit: T, rtl: Boolean): String
    fun getValueVoice(context: Context, valueInDefaultUnit: T): String
    fun getValueVoice(context: Context, valueInDefaultUnit: T, rtl: Boolean): String
}
