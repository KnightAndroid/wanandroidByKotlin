package com.knight.kotlin.library_base.util

import android.content.Context
import android.content.res.Resources
import android.text.BidiFormatter
import androidx.annotation.ArrayRes
import com.knight.kotlin.library_base.enum.BaseEnum
import com.knight.kotlin.library_base.enum.UnitEnum
import com.knight.kotlin.library_base.enum.VoiceEnum
import kotlin.math.pow
import kotlin.math.roundToInt


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/26 11:17
 * @descript:资源Array读取帮助类
 */
object ResArrayUtils {

    fun getName(
        context: Context,
        enum: BaseEnum,
    ) = getNameByValue(
        res = context.resources,
        value = enum.id,
        nameArrayId = enum.nameArrayId,
        valueArrayId = enum.valueArrayId
    )!!

    fun getVoice(
        context: Context,
        enum: VoiceEnum,
    ) = getNameByValue(
        res = context.resources,
        value = enum.id,
        nameArrayId = enum.voiceArrayId,
        valueArrayId = enum.valueArrayId
    )!!

    fun getNameByValue(
        res: Resources,
        value: String,
        @ArrayRes nameArrayId: Int,
        @ArrayRes valueArrayId: Int,
    ): String? {
        val names = res.getStringArray(nameArrayId)
        val values = res.getStringArray(valueArrayId)
        return getNameByValue(value, names, values)
    }

    private fun getNameByValue(
        value: String,
        names: Array<String>,
        values: Array<String>,
    ) = values.zip(names).firstOrNull { it.first == value }?.second

    fun getValueTextWithoutUnit(
        enum: UnitEnum<Double>,
        valueInDefaultUnit: Double,
        decimalNumber: Int,
    ) = BidiFormatter
        .getInstance()
        .unicodeWrap(
            formatDouble(enum.getValueWithoutUnit(valueInDefaultUnit), decimalNumber)
        )

    fun getValueTextWithoutUnit(
        enum: UnitEnum<Int>,
        valueInDefaultUnit: Int,
    ) = BidiFormatter
        .getInstance()
        .unicodeWrap(
            formatInt(enum.getValueWithoutUnit(valueInDefaultUnit))
        )

    fun getValueText(
        context: Context,
        enum: UnitEnum<Double>,
        valueInDefaultUnit: Double,
        decimalNumber: Int,
        rtl: Boolean,
    ) = if (rtl) {
        BidiFormatter
            .getInstance()
            .unicodeWrap(
                formatDouble(enum.getValueWithoutUnit(valueInDefaultUnit), decimalNumber)
            ) + "\u202f" + getName(context, enum)
    } else {
        formatDouble(enum.getValueWithoutUnit(valueInDefaultUnit), decimalNumber) + "\u202f" + getName(context, enum)
    }

    fun getValueText(
        context: Context,
        enum: UnitEnum<Int>,
        valueInDefaultUnit: Int,
        rtl: Boolean,
    ) = if (rtl) {
        BidiFormatter
            .getInstance()
            .unicodeWrap(
                formatInt(enum.getValueWithoutUnit(valueInDefaultUnit))
            ) + "\u202f" + getName(context, enum)
    } else {
        formatInt(enum.getValueWithoutUnit(valueInDefaultUnit)) + "\u202f" + getName(context, enum)
    }

    fun getVoiceText(
        context: Context,
        enum: UnitEnum<Double>,
        valueInDefaultUnit: Double,
        decimalNumber: Int,
        rtl: Boolean,
    ) = if (rtl) {
        BidiFormatter
            .getInstance()
            .unicodeWrap(
                formatDouble(
                    enum.getValueWithoutUnit(valueInDefaultUnit),
                    decimalNumber
                )
            ) + "\u202f" + getVoice(context, enum)
    } else {
        formatDouble(enum.getValueWithoutUnit(valueInDefaultUnit), decimalNumber) + "\u202f" + getVoice(context, enum)
    }

    fun getVoiceText(
        context: Context,
        enum: UnitEnum<Int>,
        valueInDefaultUnit: Int,
        rtl: Boolean,
    ) = if (rtl) {
        BidiFormatter
            .getInstance()
            .unicodeWrap(
                formatInt(enum.getValueWithoutUnit(valueInDefaultUnit))
            ) + "\u202f" + getVoice(context, enum)
    } else {
        formatInt(enum.getValueWithoutUnit(valueInDefaultUnit)) + "\u202f" + getVoice(context, enum)
    }

    fun formatDouble(value: Double, decimalNumber: Int = 2): String {
        val factor = 10.0.pow(decimalNumber)
        return if (
            value.roundToInt() * factor == (value * factor).roundToInt().toDouble()
        ) {
            value.roundToInt().toString()
        } else {
            String.format("%." + decimalNumber + "f", value)
        }
    }

    fun formatInt(value: Int) = String.format("%d", value)
}