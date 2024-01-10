package com.knight.kotlin.library_util

import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import com.knight.kotlin.library_util.toast.ToastUtils
import com.wyjson.router.GoRouter
import com.wyjson.router.model.Card
import java.io.Serializable
import kotlin.reflect.KClass
import kotlin.reflect.KType


/**
 * Author:Knight
 * Time:2021/12/21 14:52
 * Description:Utils
 * 一些扩展方法
 */


/**
 * 显示toast
 * @param msg String 文本
 * @param duration Int 显示时间
 */
fun toast(msg:String,duration: Int = Toast.LENGTH_SHORT) {
    ToastUtils.show(msg)
}


/**
 * 显示toast
 * @param msgId Int String资源ID
 * @param duration Int 显示时间
 */
fun toast(msgId:Int,duration:Int = Toast.LENGTH_SHORT) {
    ToastUtils.show(msgId)
}


/**
 * 跳转activity
 *
 */
fun startPage(activity:String) {
    GoRouter.getInstance().build(activity).go()
}

/**
 * 带基本参数跳转activity
 *
 */
fun startPageWithParams(page:String,vararg params:Pair<String,Any>) {
    GoRouter.getInstance().build(page).putExtras(*params).go()
}

/**
 * 带序列化基本参数跳转指定界面
 *
 */
fun startPageWithParcelableArrayListParams(page:String, params: Pair<String,ArrayList<out Parcelable>>) {
    GoRouter.getInstance().build(page).withParcelableArrayList(params.first,params.second).go()
}


/**
 * 携带整形集合跳转指定界面
 *
 */
fun startPageWithIntArrayListParams(page:String, params: Pair<String,ArrayList<Int>>) {
    GoRouter.getInstance().build(page).withIntegerArrayList(params.first,params.second).go()
}

/**
 * 携带String集合跳转指定界面
 *
 */
fun startPageWithStringArrayListParams(page:String, params: Pair<String,ArrayList<String>>) {
    GoRouter.getInstance().build(page).withStringArrayList(params.first,params.second).go()
}


/**
 *
 * 携带基本参数跳转指定界面
 */
fun Card.putExtras(vararg params: Pair<String, Any>):Card {
    if (params.isEmpty()) return this
    params.forEach { (key, value) ->
        when (value) {
          is Int -> withInt(key,value)
          is Byte -> withByte(key,value)
          is Char -> withChar(key,value)
          is Long -> withLong(key, value)
          is Float -> withFloat(key, value)
          is Short -> withShort(key, value)
          is Double -> withDouble(key, value)
          is Boolean -> withBoolean(key, value)
          is Bundle -> withBundle(key, value)
          is String -> withString(key, value)
          is ByteArray -> withByteArray(key, value)
          is CharArray -> withCharArray(key, value)
          is FloatArray -> withFloatArray(key, value)
          is Parcelable -> withParcelable(key, value)
          is ShortArray -> withShortArray(key, value)
          is CharSequence -> withCharSequence(key, value)
          is Parcelable -> withParcelable(key,value)
          is Serializable -> withSerializable(key, value)
        }
    }
    return this
}

fun KType.isClass(cls: KClass<*>): Boolean {
    return this.classifier == cls
}

/**
 * 判断类型
 *
 */
val KType.isTypeString: Boolean get() = this.isClass(String::class)
val KType.isTypeInt: Boolean get() = this.isClass(Int::class) || this.isClass(java.lang.Integer::class)
val KType.isTypeLong: Boolean get() = this.isClass(Long::class) || this.isClass(java.lang.Long::class)
val KType.isTypeByte: Boolean get() = this.isClass(Byte::class) || this.isClass(java.lang.Byte::class)
val KType.isTypeShort: Boolean get() = this.isClass(Short::class) || this.isClass(java.lang.Short::class)
val KType.isTypeChar: Boolean get() = this.isClass(Char::class) || this.isClass(java.lang.Character::class)
val KType.isTypeBoolean: Boolean get() = this.isClass(Boolean::class) || this.isClass(java.lang.Boolean::class)
val KType.isTypeFloat: Boolean get() = this.isClass(Float::class) || this.isClass(java.lang.Float::class)
val KType.isTypeDouble: Boolean get() = this.isClass(Double::class) || this.isClass(java.lang.Double::class)
val KType.isTypeByteArray: Boolean get() = this.isClass(ByteArray::class)